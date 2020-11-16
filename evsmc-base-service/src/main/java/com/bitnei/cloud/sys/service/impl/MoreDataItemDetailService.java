package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.client.das.HisDataClient;
import com.bitnei.cloud.common.client.model.GlobalResponse;
import com.bitnei.cloud.common.client.model.PageRequest;
import com.bitnei.cloud.common.client.model.PageResult;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.OfflineExportProgress;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.sys.dao.MoreDataItemDetailMapper;
import com.bitnei.cloud.sys.dao.OfflineExportMapper;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IMoreDataItemDetailService;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DataItemDetailService实现<br>
 * 描述： DataItemDetailService实现<br>
 * 授权 : (C) Copyright (c) 2019 <br>
 * 公司 : 北京理工新源信息科技有限公司<br>
 * ----------------------------------------------------------------------------- <br>
 * 修改历史 <br>
 * <table width="432" border="1">
 * <tr>
 * <td>版本</td>
 * <td>时间</td>
 * <td>作者</td>
 * <td>改变</td>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>2019-07-14 </td>
 * <td>李宇</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.MoreDataItemDetailMapper")
public class MoreDataItemDetailService extends CommonBaseService implements IMoreDataItemDetailService, IOfflineExportCallback,IOfflineExportService {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private MoreDataItemDetailMapper moreDataItemDetailMapper;

    @Autowired
    private WsServiceClient ws;
    @Autowired
    private DataItemDetailService dataItemDetailService;

    @Autowired
    private OfflineExportMapper mapper;

    private final HisDataClient hisDataClient;

    private final IVehicleService vehicleService;

    private final Environment env;


    @Override
    public void moreExportOffline(PagerInfo pagerInfo) {

        //校验参数begin
        addQueryCondition(pagerInfo, "isUI", "1");
        // 校验车辆
        String vin = getQueryCondition(pagerInfo, "vin");
        if(vin==null||vin.equals("")){
            throw new BusinessException("车辆不能为空");
        }
        String[] vins = vin.split(",");
        List<Map<String, Object>> vehicleByVin = moreDataItemDetailMapper.getVehicleByVin(vins);
        if(vehicleByVin.size()==0){
            throw new BusinessException("车辆vin不正确");
        }
        StringBuilder uuid=new StringBuilder();
        StringBuilder v=new StringBuilder();
        int size=vehicleByVin.size()-1;
        for(int i=0;;i++){
            uuid.append(vehicleByVin.get(i).get("uuid"));
            v.append(vehicleByVin.get(i).get("vin"));
            if(i==size){
                break;
            }
            uuid.append(",");
            v.append(",");
        }
        addQueryCondition(pagerInfo, "uuid", uuid.toString());
        addQueryCondition(pagerInfo, "vin", v.toString());
        // 获取数据项
        String nos = getQueryCondition(pagerInfo, "dataNos");
        String ruleId = getQueryCondition(pagerInfo, "ruleId");
        if(nos==null||nos.equals("")||ruleId==null||ruleId.equals("")){
            throw new BusinessException("规约和数据项不正确");
        }
        List<DataItem> dataNos = dataItemDetailService.getDataNos(nos,ruleId).stream().filter(it ->
                !DataItemKey.G6_ITEM_ServerTime.equals(it.getSeqNo())).collect(Collectors.toList());
        if(dataNos.size()==0){
            throw new BusinessException("未查询到数据项");
        }

        //离线下载方式最多下载30天
        addQueryCondition(pagerInfo, "limitDay", "30");
        checkDate(pagerInfo);


        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 透传给回调方法的第 3 个参数
        final String exportFilePrefixName = "多车数据项明细导出";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);
        //  因为多辆车，导出的zip. 无法使用封装好的createTask方法。需要自己重新写
        createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams
        );
      //  moreDataItemDetailMapper.updateName(task);
    }


    private PageRequest buildPageRequest(PagerInfo pagerInfo, List<DataItem> dataNos) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setAsc(true);
        pageRequest.setColumns(dataNos.stream().map(DataItem::getSeqNo).collect(Collectors.toList()));
        String beginTimeStr = getQueryCondition(pagerInfo, "beginTime");
        String endTimeStr = getQueryCondition(pagerInfo, "endTime");
        pageRequest.setStartTime(beginTimeStr);
        pageRequest.setEndTime(endTimeStr);
        return pageRequest;
    }




    @Override
    public void exportOfflineProcessor(@NotNull  String taskId, @NotNull  String createBy, @NotNull  Date createTime, @NotNull  String exportFileName,  @NotNull String exportMethodParams) throws Exception {

        // 计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //   -----------------------------------------------------   获取参数和检验参数(开始) ---------------------------------------------------
        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);
        String order = getQueryCondition(pagerInfo, "order");
        String nos = getQueryCondition(pagerInfo, "dataNos");
        String ruleId = getQueryCondition(pagerInfo, "ruleId");
        List<DataItem> dataNos = dataItemDetailService.getDataNos(nos,ruleId).stream().filter(it ->
                !DataItemKey.G6_ITEM_ServerTime.equals(it.getSeqNo())).collect(Collectors.toList());
        Map<String, String> map = dataNos.stream().filter(DataItemDetailService.distinctByKey(DataItem::getSeqNo))
                .collect(Collectors.toMap(DataItem::getSeqNo, DataItem::getSeqNo));
        Integer dataReadMode = DataReadMode.TRANSLATE;
        String readMode = getQueryCondition(pagerInfo, "dataReadMode");
        if (StringUtils.isNotBlank(readMode)) {
            dataReadMode = Integer.valueOf(readMode);
        }
        final Integer mode = dataReadMode;
        List<String> titleList =new ArrayList<>();
        dataNos.forEach(data->{
            if (StringUtils.isNotBlank(data.getNote())&&data.getNote().equals("HIDE")){
                return;
            }
            titleList.add(data.getName());
        });
        // 获取各个车辆的uuid
        String uuid = getQueryCondition(pagerInfo, "uuid");
        String[] uuids = uuid.split(",");
        // 获取各个车辆的VIN
        String vin = getQueryCondition(pagerInfo, "vin");
        String[] vins = vin.split(",");
        //   -----------------------------------------------------   获取参数和检验参数(结束) ---------------------------------------------------




        // 先更改数据库中的文件后缀名，改为zip
        String exc=exportFileName.substring(0, exportFileName.lastIndexOf(".")).concat(".csv");;
        final  String excFileName=exportFileName;


        // -----------------------------------------------------设置查询前的参数和线程（开始） ---------------------------------------------------
        // 完成线程的个数，进度条使用.  因为线程累加时会出现线程不安全，需要使用原子类操作
        AtomicInteger achieve = new AtomicInteger(0);
        // 需要线程的总个数
        int totalNumber=uuids.length+1;
        // 分页查询条件
        final PageRequest pageRequest = buildPageRequest(pagerInfo, dataNos);
        // 查询首页
        pageRequest.setNext(null);
        // 不获取总条数
        pageRequest.setGetCount(false);
       // pageRequest.setSkipPage(false);
        pageRequest.setPageSize(1000000);
        if(!"1".equals(order)){
            pageRequest.setAsc(false);
        }
        //支持多线程的map  储存线程池执行结果的文件路径
        Map<String,String> ret= new ConcurrentHashMap<String,String>();
        // 线程池最大条数5条
        int count=uuids.length>5?5:uuids.length;
        //因查询接口过多  使用线程池完成
        //声明线程池
        ExecutorService executor= newFixedThreadPool(count);
        // 线程同步对象
        final CountDownLatch latch = new CountDownLatch(uuids.length);

        // 所以导出数据项的list
        List<String> Nos= dataNos.stream().map(e -> e.getSeqNo()).collect(Collectors.toList());
        // 拼接标题列 名称
        String names = dataNos.stream().map(e -> CsvUtil.encode(e.getName())).collect(Collectors.joining(","));
        // -----------------------------------------------------设置查询前的参数和线程（结束） ---------------------------------------------------



        // ---------------------------------------------------- 执行线程 调用接口 创建Excel（开始）--------------------------------------------------

        for(int i=0;i<uuids.length;i++){
            // 因vid一直在更改，需要复制一个新对象来调用接口
            final PageRequest page = new PageRequest();
            try {
                BeanUtils.copyProperties(page,pageRequest);
            } catch (Exception e) {
                log.error("error", e);
                latch.countDown();
                continue;
            }
            page.setVid(uuids[i]);
            page.setReadMode(mode);
            final  String v=vins[i];

            //创建线程
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream stream=null;
                    OutputStreamWriter writer=null;

                    // 拼接Excel名称
                    String fileName= v+ "-" + exc;
                    try{
                        String vid = page.getVid() ;

                        //判断协议类型
                        VehicleModel vehicleModel = vehicleService.getByUuid(vid);
                        if (vehicleModel.isG6()) {
                            page.setRuleType(RuleTypeEnum.GB_T17691.getCode());
                        }

                        //调用接口获取明细数据
                        GlobalResponse<PageResult<Map<String, String>>> resultGlobalResponse =
                                hisDataClient.findPageByUUID(page);
                        // 创建文件 获取outputsteam
                         stream = getFile(createBy, fileName);
                         writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
                        // 第一行 列头
                        writer.append(names);
                        writer.append("\r\n");
                        // 判断接口数据是否为空
                        if (resultGlobalResponse != null
                                && resultGlobalResponse.getData() != null
                                && CollectionUtils.isNotEmpty(resultGlobalResponse.getData().getData())) {
                            // 去掉数据项中的.desc翻译项
                            final List<Map<String, String>> slimmedDataItems =dataItemDetailService.dataItemsSlimming(resultGlobalResponse.getData().getData(), map, true, true,false);
                            // 添加每一列的值
                            slimmedDataItems.forEach(e->e.put("vin",v));
                            for(Map<String, String> m : slimmedDataItems){
                                writer.append(Nos.stream().map(e-> CsvUtil.encode(m. get(e)) ).collect(Collectors.joining(",")));
                                writer.append("\r\n");
                            }
                        }
                        // 添加文件路径
                        ret.put(vid,fileName);
                    }catch (Exception e){
                        log.error("error", e);
                    }finally {
                        // 方法执行完，释放一个锁
                        latch.countDown();
                        // 关闭资源
                        if(writer!=null){
                            try {
                                writer.flush();
                                writer.close();
                            }catch (IOException x){
                                log.warn("关闭文件资源错误", createBy, fileName);
                            }
                        }
                        // 关闭资源
                        if(stream!=null){
                            try {
                                stream.close();
                            }catch (IOException x){
                                log.warn("关闭文件资源错误", createBy, fileName);
                            }
                        }

                    }
                    // 进度条进度增加,并计算当前进度
                    int andIncrement = achieve.getAndIncrement()*100/totalNumber;
                    // 更新redis进度条
                    OfflineExportProgress.updateProgress(
                            redis,
                            createBy,
                            excFileName,
                            andIncrement
                    );
                    // 更新页面进度条
                    OfflineExportProgress.pushProgress(
                            ws,
                            taskId,
                            createBy,
                            "导出中",
                            OfflineExportStateMachine.EXPORTING,
                            andIncrement,
                            ""
                    );

                }
            });
        }
            try {
                //等待线程执行完毕
                latch.await();
            } catch (InterruptedException e) {
                log.error("error" ,e);
                Thread.currentThread().interrupt();
            }
            // ---------------------------------------------------- 执行线程 调用接口 创建Excel（结束）--------------------------------------------------


            //---------------------------------------------------- 添加压缩包 更新进度条（开始） -------------------------------------------------------

            // 获取file结果
        ArrayList<File> files=new ArrayList<>();
            for(Map.Entry<String,String> e :ret.entrySet()){
                Environment environment = (Environment) ApplicationContextProvider.getBean(Environment.class);
                String property = (String)Optional.ofNullable(environment.getProperty("spring.scheduled.com.bitnei.cloud.common.handler.OfflineExportHandler.exportDirectory")).orElse(".");
                Path path = Paths.get(property, createBy, e.getValue()).toAbsolutePath();
                File file = path.toFile();
                if(file.exists()){
                    files.add(file);
                }
            }

            try {
                // 创建压缩包
                Environment environment = (Environment) ApplicationContextProvider.getBean(Environment.class);
                String property = (String)Optional.ofNullable(environment.getProperty("spring.scheduled.com.bitnei.cloud.common.handler.OfflineExportHandler.exportDirectory")).orElse(".");
                Path path = Paths.get(property, createBy, excFileName).toAbsolutePath();
                getZIP(files,path.toString());
                // 更新redis进度条
                OfflineExportProgress.updateProgress(
                        redis,
                        createBy,
                        excFileName,
                        100
                );
                // 更新页面进度条
                OfflineExportProgress.pushProgress(
                        ws,
                        taskId,
                        createBy,
                        "导出中",
                        OfflineExportStateMachine.EXPORTING,
                        100,
                        ""
                );
                stopWatch.stop();
                log.info("多车离线导出文件[{}], 共耗时[{}ms]]", new Object[]{exportFileName, stopWatch.getTotalTimeMillis()});
            } catch (Exception e) {
                throw new BusinessException("压缩包错误");
            }

    }


    /**
     * 在对应的目录穿件Excel文件，并返回
     * @param createBy
     * @param exportFileName
     * @return
     */
    public FileOutputStream getFile(String createBy,String exportFileName) {

        Environment environment = (Environment) ApplicationContextProvider.getBean(Environment.class);
        String property = (String)Optional.ofNullable(environment.getProperty("spring.scheduled.com.bitnei.cloud.common.handler.OfflineExportHandler.exportDirectory")).orElse(".");
        Path path = Paths.get(property, createBy, exportFileName).toAbsolutePath();
        File directory = path.getParent().toFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new BusinessException("创建目录[" + directory.toString() + "]失败");
        }
        File file = path.toFile();
        try{
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(ByteOrderMark.UTF_8.getBytes());
            return  stream;
        }catch (Exception e){
            if (!file.delete()) {
                log.warn("离线导出任务取消, 文件[{}/{}]删除失败", createBy, exportFileName);
            }
            throw new BusinessException("多车明细数据创建CSV文件错误 创建路径为： "+exportFileName);
        }

    }
    @Override
    public Object getRule(PagerInfo pagerInfo){
        String modelId = getQueryCondition(pagerInfo, "modelId");

        if(modelId==null||"".equals(modelId)){
            throw new BusinessException("请输入车型");
        }
        List<Map<String, Object>> roleByModel = moreDataItemDetailMapper.getRoleByModel(modelId);
        if(roleByModel.size()==0){
            throw new BusinessException("未查询到车型");
        }

        JSONArray arr=new JSONArray();
        roleByModel.forEach(e->{
            JSONObject ob=new JSONObject();
            ob.put("name",e.get("name"));
            ob.put("value",e.get("id"));
            arr.add(ob);
        });
        return arr;
    }


    @Override
    public Object getDataItem(PagerInfo pagerInfo){
        String id = getQueryCondition(pagerInfo, "ruleId");
        Object dataItem = dataItemDetailService.getDataItem(pagerInfo, id);
        JSONObject ob=new JSONObject();
        ob.put("dataItem",dataItem);
        return ob;

    }




    public void getZIP(ArrayList<File> files,String  fileName){

        try {
            File file = new File(fileName);
            ZipFile zipFile = new ZipFile(file);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(8);
            parameters.setCompressionLevel(5);
            zipFile.addFiles(files, parameters);
            ResponseStreamWriter.flushFile(file, ResponseStreamWriter.MimeEnum.ZIP);

        } catch (Exception e) {
            log.error("error", e);
            throw  new  BusinessException("压缩包错误");
        }finally {
            // 删除csv文件
            files.forEach(e->e.delete());
        }

    }






    private int insert(@NotNull final OfflineExport entity) {

        // todo xzp: 使用 AOP 实现

        entity.setCreateTime(DateUtil.getNow());
        entity.setCreateBy(ServletUtil.getCurrentUser());
        entity.setSystemName(env.getProperty("spring.application.name", "evsmc-base-service"));
        return mapper.insert(entity);
    }

    @NotNull
    @Override
    public String createTask(
            @NotNull final String exportFilePrefixName,
            @NotNull final String exportServiceName,
            @NotNull final String exportMethodName,
            @NotNull final String exportMethodParams) {

        final String createBy = ServletUtil.getCurrentUser();
        final int count = mapper.countDuplicateTask(createBy, exportServiceName,
                env.getProperty("spring.application.name", "evsmc-base-service"));
        if (count > 0) {
            throw new BusinessException("同一账号同一功能只能存在一个进行中的下载任务，直到任务取消或结束，才能再次新建.");
        }

        final String id = UtilHelper.getUUID();

        // 创建离线导出任务
        final OfflineExport entity = new OfflineExport();
        entity.setId(id);
        entity.setExportFileName(
                String.format(
                        "%s-%s-%s.zip",
                        exportFilePrefixName,
                        createBy,
                        DateFormatUtils.format(
                                System.currentTimeMillis(),
                                "yyyyMMddHHmmss"
                        )
                )
        );
        entity.setStateMachine(OfflineExportStateMachine.CREATED);
        entity.setExportServiceName(exportServiceName);
        entity.setExportMethodName(exportMethodName);
        entity.setExportMethodParams(exportMethodParams);
        entity.setExportNote("");

        final int res = this.insert(entity);
        if (1 != res) {
            throw new BusinessException("创建离线导出任务失败");
        }

        OfflineExportProgress.pushProgress(
                ws,
                id,
                createBy,
                "已创建",
                OfflineExportStateMachine.CREATED,
                0L,
                "离线导出任务创建成功"
        );

        return id;
    }

}
