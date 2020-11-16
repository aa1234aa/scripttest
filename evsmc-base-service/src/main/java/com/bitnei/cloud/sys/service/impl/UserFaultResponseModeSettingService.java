package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.UserFaultResponseModeSettingMapper;
import com.bitnei.cloud.sys.domain.UserFaultResponseModeSetting;
import com.bitnei.cloud.sys.model.UserFaultResponseModeSettingModel;
import com.bitnei.cloud.sys.service.IUserFaultResponseModeSettingService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserFaultResponseModeSettingService实现<br>
* 描述： UserFaultResponseModeSettingService实现<br>
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
* <td>2019-06-04 16:18:15</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UserFaultResponseModeSettingMapper" )
public class UserFaultResponseModeSettingService extends BaseService implements IUserFaultResponseModeSettingService {

    @Resource
    private UserFaultResponseModeSettingMapper userFaultResponseModeSettingMapper;

    @Override
    public UserFaultResponseModeSettingModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_fault_response_mode_setting", "fct");
        params.put("id",id);
        UserFaultResponseModeSetting entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return UserFaultResponseModeSettingModel.fromEntry(entry);
    }

    @Override
    public UserFaultResponseModeSettingModel getByUserId() {
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_fault_response_mode_setting", "fct");
        params.put("userId", userId);
        UserFaultResponseModeSetting entry = userFaultResponseModeSettingMapper.findByUserId(params);
        UserFaultResponseModeSettingModel model;
        if (null != entry) {
            model = UserFaultResponseModeSettingModel.fromEntry(entry);
        } else {
            model = new UserFaultResponseModeSettingModel();
            model.setUserId(userId);
            model.setPopup(0);
            model.setVoice(0);
            model.setAppPopup(0);
            model.setMessage(0);
            model.setEmail(0);
        }
        return model;
    }

    @Override
    public void setting(UserFaultResponseModeSettingModel model) {
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_fault_response_mode_setting", "fct");
        params.put("userId", userId);
        model.setUserId(userId);
        UserFaultResponseModeSetting entry = userFaultResponseModeSettingMapper.findByUserId(params);
        if (null == entry) {
            insert(model);
        } else {
            update(model);
        }
    }

    @Override
    public void insert(UserFaultResponseModeSettingModel model) {
        UserFaultResponseModeSetting obj = new UserFaultResponseModeSetting();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(UserFaultResponseModeSettingModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_fault_response_mode_setting", "fct");

        UserFaultResponseModeSetting obj = new UserFaultResponseModeSetting();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }
}
