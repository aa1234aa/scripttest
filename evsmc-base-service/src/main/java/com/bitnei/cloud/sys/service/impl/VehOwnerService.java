package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.VehOwner;
import com.bitnei.cloud.sys.model.VehOwnerModel;
import com.bitnei.cloud.sys.service.IVehOwnerService;
import com.bitnei.cloud.sys.util.RegexUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehOwnerService实现<br>
 * 描述： VehOwnerService实现<br>
 * 授权 : (C) Copyright (c) 2017 <br>
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
 * <td>2018-10-31 15:36:59</td>
 * <td>chenpeng</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehOwnerMapper")
public class VehOwnerService extends BaseService implements IVehOwnerService {

    @Resource
    private DictMapper dictMapper;
    @Resource(name = "webRedisKit")
    private RedisKit  redisKit;

    @Override
    public Object list(PagerInfo pagerInfo) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_owner", "vo");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if(params.containsKey("ids")) {
            String ids = (String) params.get("ids");
            params.put("ids", StringUtils.split(ids,","));
        }
        // 非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<VehOwner> entries = findBySqlId("pagerModel", params);
            List<VehOwnerModel> models = new ArrayList<>();
            for(VehOwner entry: entries){
                models.add(VehOwnerModel.fromEntry(entry));
            }
            return models;
        }
        // 分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehOwnerModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                VehOwner obj = (VehOwner)entry;
                models.add(VehOwnerModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据id获取
     * @param id id
     * @return VehOwnerModel
     */
    @Override
    public VehOwnerModel get(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_owner", "vo");
        params.put("id", id);
        VehOwner entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehOwnerModel.fromEntry(entry);
    }

    @Override
    public VehOwner findByOwnerName(String ownerName) {

        return unique("findByOwnerName", ownerName);
    }

    /**
     * 新增
     * @param model 新增model
     */
    @Override
    public void insert(VehOwnerModel model) {
        VehOwner obj = new VehOwner();
        BeanUtils.copyProperties(model, obj);
        // 校验证件号码
        String message = checkCardNo(obj.getCardType(), obj.getCardNo(), "");
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        // 单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        //  判断手机号是否重复
        lockPhone(obj); ;// 当系统访问量大时 放开此注释加锁
        int byHhone = dictMapper.getByPhone(model.getTelPhone(),null);
        if(byHhone>0){
            closeLockPhone(obj); ;// 当系统访问量大时 放开此注释关锁
                throw new BusinessException("该联系电话已经注册过了,请更换联系电话");
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        closeLockPhone(obj); ;// 当系统访问量大时 放开此注释关锁
        if (res == 0) {
            throw new BusinessException("新增失败");
        }

    }

    /**
     * 编辑
     * @param model 编辑model
     */
    @Override
    public void update(VehOwnerModel model) {
        // 校验证件号码
        String message = checkCardNo(model.getCardType(), model.getCardNo(), model.getId());
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_owner", "vo");
        VehOwner obj = new VehOwner();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        //  判断手机号是否重复
        lockPhone(obj);// 当系统访问量大时 放开此注释加锁
        int byHhone = dictMapper.getByPhone(model.getTelPhone(),model.getId());
        if(byHhone>0){
            closeLockPhone(obj);// 当系统访问量大时 放开此注释关锁
            throw new BusinessException("该联系电话已经注册过了,请更换联系电话");
        }
        int res = super.updateByMap(params);
        closeLockPhone(obj);// 当系统访问量大时 放开此注释关锁
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    @Override
    public int deleteMulti(String ids) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_owner", "vo");
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }

    /**
     * 导出
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(PagerInfo pagerInfo) {
        // 获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<VehOwner>(this, "pagerModel", params, "sys/res/vehOwner/export.xls", "个人车主") {
        }.work();
    }

    /**
     * 批量导入
     * @param file 文件
     */
    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "VEHOWNER" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<VehOwnerModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model model
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(VehOwnerModel model) {
                List<String> errors = Lists.newArrayList();
                // 性别
                if(org.apache.commons.lang3.StringUtils.isNotBlank(model.getSexName())) {
                    Map<String, Object> params = ImmutableMap.of("type", "SEX", "name", model.getSexName());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setSex(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("性别填写不正确");
                    }
                }
                // 证件类型
                if(org.apache.commons.lang3.StringUtils.isNotBlank(model.getCardTypeName())) {
                    Map<String, Object> params = ImmutableMap.of("type", "CERT_TYPE", "name", model.getCardTypeName());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setCardType(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("证件类型填写不正确");
                    }
                    // 证件号码
                    String error = checkCardNo(model.getCardType(), model.getCardNo(), null);
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(error)) {
                        errors.add(error);
                    }
                }
                //  判断手机号是否重复
                int byHhone = dictMapper.getByPhone(model.getTelPhone(),model.getId());
                if(byHhone>0){
                    errors.add("该联系电话已经注册过了,请更换联系电话");
                }
                return errors;
            }

            /**
             *  保存实体
             * @param model model
             */
            @Override
            public void saveObject(VehOwnerModel model) {
                insert(model);
            }
        }.work();
    }

    /**
     * 批量更新
     *
     * @param file 文件
     */
    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "VEHOWNER" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<VehOwnerModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model model
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(VehOwnerModel model) {

                return null;
            }

            /**
             *  保存实体
             * @param model model
             */
            @Override
            public void saveObject(VehOwnerModel model) {
                update(model);
            }
        }.work();
    }

    /**
     * 自定义参数查询车主信息
     * @param params 参数
     * @return VehOwner
     */
    @Override
    public VehOwner queryVehOwnerByParam(Map<String, Object> params) {
        // 获取当权限的map
        List<VehOwner> list = findBySqlId("pagerModel", params);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 校验证件号码
     *
     * @param cardType  证件类型 1:居民身份证 2:士官证 3 学生证 4 驾驶证 5 护照 6 港澳通行证
     * @param cardNo    证件号码
     * @param id        车主id
     * @return 错误信息
     */
    private String checkCardNo(Integer cardType, String cardNo, String id) {
        if (!StringUtils.isEmpty(cardNo)) {
            // 数字和大写字母的正则校验需要加
            boolean bl = cardNo.matches(RegexUtil.NUM_OR_CAPITAL_8_30);
            if (null != cardType) {
                boolean bool = Constant.CARDTYPE.IDENTITY.equals(cardType) || Constant.CARDTYPE.DRIVINGLICENCE.equals(cardType);
                boolean booLength = cardNo.length() != 18 || !bl;
                boolean sergeantBool = cardNo.length() != 8 || !bl;
                if (bool && booLength ) {
                    return "身份证或驾驶证为18位数字或大写字母";
                } else if (Constant.CARDTYPE.SERGEANT.equals(cardType) && sergeantBool) {
                    return "军官证为8位数字或大写字母";
                } else if(!bl) {
                    return "证件号码为8-30位数字或大写字母";
                }
            } else if (!bl) {
                return "证件号码为8-30位数字或大写字母";
            }
            Map<String, Object> params = Maps.newHashMap();
            params.put("cardNo", cardNo);
            VehOwner vo = queryVehOwnerByParam(params);
            // 如果id不为空则为更新校验
            if (null != vo && !StringUtils.equals(vo.getId(), id)) {
                // 如果id为空则表示为添加校验，如果id不为空则表示为更新校验，如何查询结果id不同则表示数据库已存在此证件号，返回错误
                return "证件号码已存在";
            }
        }
        return "";
    }

    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("个人车主导入模板.xls" , VehOwnerModel.class);
    }


    /**
     * 当系统用户量大时当用此方法
     * 校验手机号
     * 高并发处理(设置分布式锁)
     * @param obj
     */
    public  void lockPhone(VehOwner obj){
        Jedis jedis=null;
        try {
            String key="veh-phone-" + obj.getTelPhone();
             jedis = redisKit.getResource();
            Long  setnx = jedis.setnx(key, "1");
            if(setnx==0){
                throw new BusinessException("该联系电话正在注册或已经注册,请更换联系电话");
            }// 10秒失效
            jedis.expire(key,10);
        }catch (Exception e){
        }finally {
            redisKit.returnResource(jedis);
        }

    }
    /**
     * 校验手机号
     * 高并发处理(释放分布式锁)
     * @param obj
     */
    public  void closeLockPhone(VehOwner obj){
        redisKit.del("veh-phone-" + obj.getTelPhone());
    }

}
