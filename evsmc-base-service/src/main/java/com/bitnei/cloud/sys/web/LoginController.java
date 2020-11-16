package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.api.ResultObjMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.client.das.ForwardRecordClient;
import com.bitnei.cloud.common.client.das.ProtocolMessageClient;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.sys.model.LoginModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.ILoginService;
import com.bitnei.cloud.sys.service.IRoleDataItemLkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.mvel2.util.Make;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-module <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2018-${MOTH}-19</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Api(value = "用户登入登出", description = "提供用户登入、用户登出和获取CSRF的接口", tags = {"用户登入登出"})
@RestController
@RequestMapping(value = "/"+ Version.VERSION_V1)
@Slf4j
public class LoginController {

    @Autowired
    private ILoginService loginService;
    @Resource
    private RealDataClient realDataClient;
    @Resource
    private ForwardRecordClient forwardRecordClient;
    @Resource
    private ProtocolMessageClient protocolMessageClient;
    @Autowired
    private IRoleDataItemLkService roleDataItemLkService;

    @Resource(name = "ctoRedisKit")
    private RedisKit redisKit;


    @ApiOperation(value = "获取CSRF",notes = "用于获取csrf值，任何人都可以访问该接,服务器在cookie中写入CSRF")
    @ApiParam
    @GetMapping(value = "/first-login")
    @RequiresAuthentication
    public ResultMsg firstLogin(){

//        Map<String, String> map = realDataClient.findByUuid("314bc2b6-b9ce-4379-af2f-bec93cef741b", new String[]{"2000", "2502", "2503", "2308"}, DataReadMode.ALL);
//        ProtocolQueryModel rm = new ProtocolQueryModel();
//        rm.setUuid("dc87aebb-51f8-4138-982f-8258103cb4c8");
//        rm.setStartTime("2019-03-15 01:00:00");
//        rm.setEndTime("2019-04-15 01:00:00");
//        rm.setStart(0);
//        rm.setLimit(10);

//        PagerModel pm = protocolMessageClient.findProtocolMessage(rm);
//        log.info(new JsonSerializer().deep(true).serialize(pm));
//        Map<String, Map<String, String>> map = realDataClient.findByAllVehicle(new String[]{"2502","2503", "2000"});
//        redisKit.hget("1","1");
//        String authSql = DataAccessKit.getUserAuthSql("1", "sys_vehicle", "v");
        loginService.createCsrf();
        return ResultMsg.getResult("获取csrf值成功");
    }


    @ApiOperation(value = "用户登入",notes = "用于用户登入，通过输入用户名、密码和随机码")
    @PostMapping(value = "/login")
    public ResultMsg login(@RequestBody @Validated LoginModel model, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        Map<String,Object> objectMap = loginService.login(model.getUsername(), model.getPassword(), model.getValidCode(), model.getIsApp());
        return ResultMsg.getResult(objectMap, "登入成功" ,200);
    }


    @ApiOperation(value = "用户登出",notes = "用于用户登出，登出后CSRF和TOKEN都会被清除")
    @GetMapping(value = "/logout")
    public ResultMsg logout(){
        loginService.logout();
        return ResultMsg.getResult("登出成功");
    }

    @ApiOperation(value = "获取个人信息",notes = "获取当前用户的详细信息")
    @GetMapping(value = "/me")
    public ResultMsg me(){

        Map<String,Object> userModel = loginService.me();
        return ResultMsg.getResult(userModel);
    }

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "获取用户菜单", notes = "获取用户菜单")
    @GetMapping(value = "/menus")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg menus() {
        TreeNode treeNode = loginService.menus();
        return ResultMsg.getListResult(treeNode.getChildren());
    }




    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "获取登录随机码" , notes = "获取登录随机码")
    @GetMapping(value = "/login/randCode")
    @RequiresAuthentication
    @ResponseBody
    public ResultMsg randCode(){
        String base64Code = loginService.randCode();
        Map<String,String> map = new HashMap<>();
        map.put("src", base64Code);
        return ResultMsg.getObjectResult(map);
    }





    @ApiOperation(value = "根据roleId获取菜单按钮权限编码", notes = "根据roleId获取菜单按钮权限编码")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/roles/{id}/buttonsPermission")
    @ResponseBody
    public ResultMsg roleBtnPermission(@PathVariable String id){
        List<Map<String,Object>> list = roleDataItemLkService.queryRoleModuleByRoleId(id);
       String[] data=new String[0];
       if (CollectionUtils.isNotEmpty(list)){
           list.removeIf(l-> Integer.parseInt(l.get("hide").toString())==1);
            if (CollectionUtils.isNotEmpty(list)){
                data= list.stream().map(m -> m.get("code").toString()).toArray(String[]::new);
            }
       }
        ResultObjMsg<String[]> result = new ResultObjMsg<>();
        result.setCode(200);
        result.setType("array");
        result.setData(data);
        return result;
    }

    /**
     * 获取登录随机码
     *
     * @return
     */
    @ApiOperation(value = "获取登录随机码" , notes = "获取登录随机码")
    @GetMapping(value = "/login/randCodeToMobile/{mobile}")
    @ApiImplicitParam(name = "mobile", value = "mobile", required = true, dataType = "String",paramType = "path")
    @RequiresAuthentication
    @ResponseBody
    public ResultMsg randCodeToMobile(@PathVariable String mobile){
        String randCode = loginService.randCodeToMobile(mobile);
        //发送短信
        //Map<String,String> map = new HashMap<>();
        //map.put("randCode", randCode);
        return ResultMsg.getObjectResult("短信发送成功");
    }


    @ApiOperation(value = "app登入",notes = "用于app登入，通过输入手机号和验证码")
    @PostMapping(value = "/loginByApp")
    public ResultMsg loginByApp(@RequestBody @Validated LoginModel model, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Map<String,Object> objectMap = loginService.loginByMobile(model.getMobile(), model.getValidCode());
        return ResultMsg.getResult(objectMap, "登入成功" ,200);
    }

}


