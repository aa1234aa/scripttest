package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.ParamsSearchDto;
import com.bitnei.cloud.sys.model.TermParamRecordModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.service.impl.InstructTaskService;
import com.bitnei.cloud.sys.service.impl.TermParamRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 终端参数记录<br>
 * 描述： 终端参数记录控制器<br>
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
 * <td>2019-03-07 15:28:19</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "远程控制-终端参数记录", description = "远程控制-终端参数记录", tags = {"远程控制-终端参数记录"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class TermParamRecordController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "TERMPARAMRECORD";
    /**
     * 查看
     **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表
     **/
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页
     **/
    public static final String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
    /**
     * 新增
     **/
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑
     **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除
     **/
    public static final String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出
     **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";
    /**
     * 导入
     **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE + "_IMPORT";
    /**
     * 批量导入
     **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE + "_BATCH_IMPORT";
    /**
     * 批量更新
     **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE + "_BATCH_UPDATE";

    @Autowired
    private TermParamRecordService termParamRecordService;

    private final RealDataClient realDataClient;
    private final IVehicleService vehicleService;
    private final InstructTaskService instructTaskService;
    private final ThreadPoolExecutor instructTaskExecutor;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/termParamRecords/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        TermParamRecordModel termParamRecord = termParamRecordService.get(id);
        return ResultMsg.getResult(termParamRecord);
    }

    /**
     * 根据ID获取参数详细信息
     *
     * @return
     */
    @ApiOperation(value = "参数详细信息", notes = "根据ID获取参数详细信息：查询参数id必须传")
    @PostMapping(value = "/termParamRecords/getParamsDetail")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getParamsDetail(@RequestBody @Validated PagerInfo pagerInfo,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        return termParamRecordService.getParamsDetail(pagerInfo);
    }

    /**
     * 导出参数详细信息
     *
     * @return
     */
    @ApiOperation(value = "导出参数详细信息", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/termParamRecords/exportParamsDetail/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true,
            dataType = "String", paramType = "path")
    public void exportParamsDetail(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        termParamRecordService.exportParamsDetail(info);
        return;

    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询：vin，licensePlate，interNo，beginTime，endTime，vehModelId")
    @PostMapping(value = "/termParamRecords")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = termParamRecordService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/termParamRecord")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) TermParamRecordModel demo1,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        termParamRecordService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 下发终端参数查询命令
     *
     * @param paramsSearchDto
     * @return
     */
    @ApiOperation(value = "下发终端参数查询命令", notes = "下发终端参数查询命令")
    @PostMapping(value = "/termParamRecord/saveParamsSearch")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    @SneakyThrows
    public ResultMsg saveParamsSearch(@RequestBody ParamsSearchDto paramsSearchDto) {

        //再开线程,发送kafka查询命令
        String dicts = "";
        if (StringUtils.isNotEmpty(paramsSearchDto.getDictCodes())) {
            dicts = paramsSearchDto.getDictCodes().replaceAll(",", "|");
        }
        String finalDicts = dicts;

        List<String> vehIds;
        if (StringUtils.isEmpty(paramsSearchDto.getVehIds()) && null != paramsSearchDto.getPagerInfo()) {
            vehIds = instructTaskService.getVehicleModelsByPagerInfo(paramsSearchDto.getPagerInfo());
        } else {
            vehIds = Arrays.asList(paramsSearchDto.getVehIds().split(","));
        }

        //先保存查询记录
        termParamRecordService.save(vehIds, ServletUtil.getCurrentUser(), "查询指令",
                "手动下发参数查询指令");

        Future<ResultMsg> future = instructTaskExecutor.submit(() ->
                termParamRecordService.cmd(TermParamRecordService.QUERY, vehIds, finalDicts,
                        ServletUtil.getCurrentUser(), null));

        if (!paramsSearchDto.getVehIds().contains(",")) {
            //如果是查询单辆车，阻塞等待线程执行完毕
            return future.get();
        } else {
            //如果是多辆车,直接返回
            return ResultMsg.getResult("参数查询发送成功,等待返回状态");
        }

    }

    /**
     * 远程设置终端参数
     *
     * @param object
     * @return
     */
    @ApiOperation(value = "远程设置终端参数", notes = "远程设置终端参数")
    @PostMapping(value = "/termParamRecord/setParams")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    @SneakyThrows
    public ResultMsg setParams(@RequestBody Object object) {
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.fromObject(object);
        } catch (Exception e) {
            return ResultMsg.getResult(null, "设置参数编码不存在，请联系管理员", -1);
        }

        Object obj = termParamRecordService.verification(jsonObject);
        JSONObject js = JSONObject.fromObject(obj);
        if (null != js.get("error")){
            return ResultMsg.getResult(null, js.get("error").toString(), -1);
        }

        //取出并删除掉关于车辆的数据
        Object vehIdsStr = jsonObject.remove("vehIds");
        //把参数项的数据拿出来
        String dicts = jsonObject.toString().replace("{", "").replace("}", "");
        String finalDicts = dicts.replaceAll("\"", "");

        PagerInfo pagerInfo = (PagerInfo) jsonObject.get("pagerInfo");

        //开启线程, 发送设置kafka的命令
        List<String> vehIds;
        if (StringUtils.isEmpty(vehIdsStr.toString()) && null != pagerInfo) {
            vehIds = instructTaskService.getVehicleModelsByPagerInfo(pagerInfo);
        } else {
            vehIds = Arrays.asList(vehIdsStr.toString().split(","));
        }

        //先保存数据
        termParamRecordService.save(vehIds, ServletUtil.getCurrentUser(), "设置指令",
                "手动下发参数设置指令");

        Future<ResultMsg> future = instructTaskExecutor.submit(() ->
            termParamRecordService.cmd(TermParamRecordService.SET,
                    vehIds, finalDicts, ServletUtil.getCurrentUser(), null));

        if (vehIds.size() == 1) {
            //如果是查询单辆车，阻塞等待线程执行完毕
            return future.get();

        } else {
            return ResultMsg.getResult("远程设置终端参数设置成功,等待返回状态");
        }
    }

    public static void main(String[] args) {

        JSONObject jsonObject = JSONObject.fromObject("{\"5203\":\"123\",\"5208\":\"432\",\"vehIds\":\"402881f2698ecd1101698ff3c5fc018c,402881f2698ecd1101698ff8cc130190\"}");

        //取出并删除掉关于车辆的数据
        Object vehIds = jsonObject.remove("vehIds");
        //把参数项的数据拿出来
        String dicts = jsonObject.toString().replace("{", "").replace("}", "");
        dicts = dicts.replaceAll("\"", "");

        System.out.println(dicts);
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/termParamRecords/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) TermParamRecordModel demo1, @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        termParamRecordService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/termParamRecords/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = termParamRecordService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/termParamRecords/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true,
            dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        termParamRecordService.export(info);
        return;

    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/termParamRecords/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        termParamRecordService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/termParamRecords/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        termParamRecordService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }


}
