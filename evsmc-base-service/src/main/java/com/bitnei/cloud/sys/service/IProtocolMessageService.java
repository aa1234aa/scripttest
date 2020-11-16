package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.client.model.PagerModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.ProtocolDataDto;
import com.bitnei.cloud.sys.model.UppackageSendDetailsModel;
import com.bitnei.cloud.sys.model.VehicleUpgradeLogSumDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UppackageSendDetailsService接口<br>
* 描述： UppackageSendDetailsService接口，在xml中引用<br>
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
* <td>2019-03-05 15:53:14</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/

public interface IProtocolMessageService extends IBaseService {

    ResultMsg findProtocolMessage(PagerInfo pagerInfo);

    void export(PagerInfo pagerInfo);

    List doParseStation(String data);

    void exportOffline(PagerInfo pagerInfo);

    List parse(ProtocolDataDto protocolDataDto);
}
