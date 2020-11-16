package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.ProtocolDataDto;
import com.bitnei.cloud.sys.service.IProtocolMessageService;
import com.bitnei.cloud.sys.util.MessageBean;
import com.bitnei.cloud.sys.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Api(value = "车辆监控-报文信息", description = "车辆监控-报文信息", tags = {"车辆监控-报文信息"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class ProtocolMessageController {

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "UPPACKAGEINFO";
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


    private final IProtocolMessageService protocolMessageService;

    /**
     * 查询报文列表
     *
     * @return
     */
    @ApiOperation(value = "查询报文列表", notes = "查询报文列表，参数支持：vin，licensePlate，beginTime，endTime，" +
            "type(报文类型)，orderBy: 0:倒序 1:顺序，verify")
    @PostMapping(value = "/protocolMessages")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        return protocolMessageService.findProtocolMessage(pagerInfo);
    }


    @ApiOperation(value = "报文解析", notes = "根据协议类型进行报文解析")
    @PostMapping(value = "/protocolMessages/parse")
    @ResponseBody
    public ResultMsg parse(@RequestBody ProtocolDataDto protocolDataDto) {

        if (StringUtils.isEmpty(protocolDataDto.getData())) {
            return ResultMsg.getResult(null, "报文数据为空", -1);
        }

        return ResultMsg.getResult(protocolMessageService.parse(protocolDataDto));
    }

    @ApiOperation(value = "国标解析", notes = "国标解析")
    @PostMapping(value = "/protocolMessages/doParseStation")
    @ResponseBody
    public ResultMsg doParseStation(@RequestBody ProtocolDataDto protocolDataDto) {

        if (StringUtils.isEmpty(protocolDataDto.getData())) {
            return ResultMsg.getResult(null, "报文数据为空", -1);
        }

        return ResultMsg.getResult(protocolMessageService.doParseStation(protocolDataDto.getData()));
    }

    @ApiOperation(value = "地标解析", notes = "地标解析")
    @PostMapping(value = "/protocolMessages/doParse")
    @ResponseBody
    public Object doParse(@RequestBody ProtocolDataDto protocolDataDto) {
        // datas= "232302FE4C564342334E304332475330313235323600005E1009160F350D0100000002000000030000000000000000000000000000000000000000000000000000040106F0C46F0262CC30000000000000000005000000000000000000000000000000000000000000000000000000000600000000006C";
       // protocolDataDto.setData("232302fe4c474843345631443548453230353436390100d113051b113b1d060100ffff0100ffff0100ff0100ff08010115b3271c012000c9580f140f130f110f130f100f0e0f0f0f0f0f0f0f100f0e0f0e0f0e0f0f0f110f0f0f0d0f0e0f0f0f110f130f130f130f0e0f140f120f0f0f140f100f0d0f120f140f120f0f0f110f130f0e0f120f110f0d0f150f100f120f120f150f120f140f150f0f0f110f110f0f0f0e0f100f110f140f120f100f150f0f0f130f110f130f150f110f0a0f110f0f0f130f0e0f110f110f0f0f0f0f0e0f0e0f0f0f130f100f110f130f110f120f100f0f0f110f130f1109,232302fe4c4748433456314435484532303534363901025a13051b113b1d0101030100030004040c15b3271c50012e5757640002010101404e3453165615d626fe0500067de747020c21e906018d0f5a010b0ef0011b440112400700800000000000000008010115b3271c01200001c80ef30ef50ef50ef30ef40ef30ef40ef20ef90efd0ef00ef30ef10ef10ef30ef00ef70ef40ef40ef10ef60ef10ef60ef60ef60ef70ef20ef10ef60ef40ef60ef50ef60ef30ef60ef10ef70ef80ef30ef30ef60ef60ef90ef80ef20ef70ef40ef60ef80ef20ef50ef90efa0ef40ef70ef40ef80ef80ef70ef40ef30ef40ef80ef30ef80ef40ef70ef20ef40ef40ef50ef50ef50ef50ef30ef40ef10ef10ef30ef50ef60ef30ef50ef40ef30ef30ef10ef60ef20ef70ef40ef40ef40ef40ef40ef30f570f4d0f520f500f570f510f570f4e0f560f4e0f580f4e0f4f0f480f520f4c0f4e0f4e0f510f4d0f570f4f0f430f4a0f550f4f0f570f520f540f530f550f4e0f590f540f230f4e0f550f4f0f590f520f560f550f590f530f5a0f520f580f570f170f130f150f160f160f180f180f150f1c0f180f170f190f160f130f150f1a0f100f130f110f130f110f100f0f0f130f0f0f140f110f0f0f110f130f110f0f0f110f120f110f100f120f150f130f140f140f130f120f100f160f110f0f0f120f130f140f150f130f140f130f110f1109010100484241424243424342434243424241424141404242434243434343444343424343434242414242434143424341434243424342424141414242434243434342434343424342424142418000220002001e0e0005ffff01710e0f000210041000080000084fffffff001100030050ff32");
        if (StringUtils.isEmpty(protocolDataDto.getData())) {
            return ResultMsg.getResult(null, "报文数据为空", -1);
        }

        String protocols = protocolDataDto.getData().replaceAll("\"", "");
        String[] arr= protocols.split(",");
        List list=new ArrayList<>();
        int[] packIndex = new int[1];
        packIndex[0] = 1;
        Stream.of(arr).forEach(str->{
            StringBuffer sb = new StringBuffer();
            int length = 2;
            byte buf[] = new byte[4096];
            int len = 0;
            while (length <= str.length()) {
                String src = str.substring(0, length);
                sb.append(src).append(" ");
                buf[len++] = (byte) Integer.parseInt(src, 16);
                str = str.substring(length, str.length());
                if (len % 20 == 0) {
                    sb.append("\r\n");
                }
            }
            List one=MessageUtils.uploadPacketData(buf,len,false);
            if (CollectionUtils.isNotEmpty(one)) {
                if (arr.length > 1) {
                    //分包
                    one.forEach(sub -> {
                        MessageBean messageBean = (MessageBean) sub;
                        ((MessageBean) sub).setGroup(String.format("分包%d-%s", packIndex[0], messageBean.getGroup()));
                    });
                    packIndex[0]++;
                }
                list.addAll(one);
            }

        });

        return ResultMsg.getResult(list);
    }

    /**
     * 导出报文记录
     *
     * @return
     */
    @ApiOperation(value = "导出报文记录", notes = "导出报文记录，传入参数应和分页查询保持一致")
    @GetMapping(value = "/protocolMessages/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true,
            dataType = "String", paramType = "path")
    public void exportVehicleUpgradeLog(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
      //  protocolMessageService.exportOffline(info);
        protocolMessageService.export(info);

        return;

    }

    @ApiOperation(value = "报文离线导出 " ,notes = "通过excel，离线导出")
    @PostMapping(value = "/protocolMessages/exportOffline")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody PagerInfo pagerInfo){

        protocolMessageService.exportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");

    }

}
