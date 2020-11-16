package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.LogMapper;
import com.bitnei.cloud.sys.domain.Log;
import com.bitnei.cloud.sys.model.LogModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.ILogService;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 操作日志service
 * @author zxz
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.LogMapper" )
public class LogService extends BaseService implements ILogService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Autowired
    private LogMapper mapper;
    @Autowired
    private IOfflineExportService offlineExportService;
    @Autowired
    private IUserService userService;

    /**
     * 全部查询
     * @return
     *  返回所有
     *
     * @see ResultMsg#getResult(java.lang.Object, java.lang.String, int)
     */
    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_log", "log");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))){
            long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0){
                throw new BusinessException("请输入正确的时间");
            }
            else if (endTime - beginTime >= (long)86400000*30){
                throw new BusinessException("查询时间跨度不能超过30天");
            }
        }
        else{
            String end = DateUtil.getNow();
            Date beginTimes = new Date(DateUtil.strToDate_ex_full(end).getTime() - 86400000*7);
            String begin = DateUtil.getDate(beginTimes.getTime());
            params.put("endTime",end);
            params.put("beginTime",begin);
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<Log> entries = findBySqlId("pagerModel", params);
            List<LogModel> models = new ArrayList<>();
            for(Log entry: entries){
                models.add(LogModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            // 构建分页参数
            final PageRowBounds pageRowBounds = PageRowBoundsUtil.fromPagerInfo(pagerInfo);
            // 进行分页查询
            final Page<Log> page = this.mapper.pagerModel(params, pageRowBounds);

            final PagerResult pr = new PagerResult();
            // 设置结果集
            pr.setData(
                // Entity to Model
                page
                    .stream()
                    .map(LogModel::fromEntry)
                    .collect(Collectors.toList())
            );
            // 设置记录总数
            pr.setTotal(page.getTotal());

            return pr;
        }
    }

    /**
     * 根据id获取对象
     * @param id id
     * @return LogModel
     */
    @Override
    public LogModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_log", "log");
        params.put("id",id);
        Log entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return LogModel.fromEntry(entry);
    }

    /**
     * 导出
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_log", "log");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))){
            long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0){
                throw new BusinessException("请输入正确的时间");
            }
            else if (endTime - beginTime >= (long)86400000*30){
                throw new BusinessException("查询时间跨度不能超过30天");
            }
        }
        else{
            String end = DateUtil.getNow();
            Date beginTimes = new Date(DateUtil.strToDate_ex_full(end).getTime() - 86400000*7);
            String begin = DateUtil.getDate(beginTimes.getTime());
            params.put("endTime",end);
            params.put("beginTime",begin);
        }
        new ExcelExportHandler<Log>(this, "pagerModel", params, "sys/res/log/export.xls", "操作日志") {
        }.work();
        return;
    }

    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))){
            long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0){
                throw new BusinessException("请输入正确的时间");
            }
            else if (endTime - beginTime >= (long)86400000*30){
                throw new BusinessException("查询时间跨度不能超过30天");
            }
        }
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "操作日志";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        // 创建离线导出任务
        return offlineExportService.createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams
        );
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public void exportOfflineProcessor(
        @NotNull final String taskId,
        @NotNull final String createBy,
        @NotNull final Date createTime,
        @NotNull final String exportFileName,
        @NotNull final String exportMethodParams) throws Exception {

        log.trace("执行离线导出任务:{}", exportFileName);

        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        final UserModel userModel = userService.findByUsername(createBy);
        final String userId = userModel.getId();
        final String tableName = "sys_log";
        final String tableAlias = "log";
        final String authSql = DataAccessKit.getUserAuthSql(userId, tableName, tableAlias);
        if (authSql != null) {
            builder.put(Constants.AUTH_SQL, authSql);
        }

        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);
        ServletUtil.pageInfoToMap(pagerInfo).forEach((k, v) -> {
            if (null != k && null != v) {
                builder.put(k, v);
            }
        });
        final Map<String, Object> params = new HashMap<>();
        params.putAll(builder.build());
        //默认查询最近7天的数据
        if (params.get("beginTime") == null || "".equals(String.valueOf(params.get("beginTime"))) || params.get("endTime") == null || "".equals(String.valueOf(params.get("endTime")))){
            String end = DateUtil.getNow();
            Date beginTimes = new Date(DateUtil.strToDate_ex_full(end).getTime() - 86400000*7);
            String begin = DateUtil.getDate(beginTimes.getTime());
            params.put("endTime",end);
            params.put("beginTime",begin);
        }
        final String excelTemplateFile = "sys/res/log/export.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.mapper::pagerModel,
            params,
            this::fromEntityToModel,
            this.mapper::pagerModel,
            redis,
            ws
        );
    }

    @NotNull
    private List<LogModel> fromEntityToModel(final @NotNull List<Log> entities) {

        final ArrayList<LogModel> models = Lists.newArrayList();
        for (final Log entity : entities) {

            final LogModel model = new LogModel();
            BeanUtils.copyProperties(entity, model);

            models.add(model);
        }

        DataLoader.loadNames(models);

        return models;
    }
}
