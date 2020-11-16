package com.bitnei.cloud.compent.kafka.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.compent.kafka.domain.KafkaSearchDto;
import com.bitnei.cloud.compent.kafka.service.KafkaConsumerService;
import com.bitnei.cloud.sys.model.DictModel;
import com.bitnei.cloud.sys.service.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "kafka-消息查询", description = "kafka-消息查询", tags = {"kafka-消息查询"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaConsumerService kafkaConsumerService;
    private final IDictService dictService;

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "KAFKA";

    /**
     * 列表
     **/
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";

    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @PostMapping(value = "/createSearchConsumer")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg createSearchConsumer(@RequestBody KafkaSearchDto kafkaSearchDto) {

        validate(kafkaSearchDto);

        Map<String, String> dictMap = dictService.findByDictType("KAFKA_TOPIC")
                .stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getNote));
        if (dictMap.containsKey(kafkaSearchDto.getTopic())) {
            kafkaSearchDto.setTopic(dictMap.get(kafkaSearchDto.getTopic()));
        } else {
            throw new BusinessException("查询topic不存在");
        }

        return ResultMsg.getResult(kafkaConsumerService.createSearchConsumers(kafkaSearchDto));
    }

    /**
     * 校验kafka查询参数
     *
     * @param kafkaSearchDto
     * @return
     */
    private void validate(KafkaSearchDto kafkaSearchDto) {

        if (StringUtils.isEmpty(kafkaSearchDto.getTopic())) {
            throw new BusinessException("查询topic不能为空");
        }
        if (StringUtils.isEmpty(kafkaSearchDto.getBeginTime()) || StringUtils.isEmpty(kafkaSearchDto.getEndTime())) {
            throw new BusinessException("查询消息写入时间区间不能为空");
        }

        int minites = com.bitnei.cloud.common.util.DateUtil.getTimesDiff(3,
                kafkaSearchDto.getBeginTime(), kafkaSearchDto.getEndTime());

        if (minites > 30) {
            throw new BusinessException("查询时间区间不能超过30分钟");
        }

        if ("5".equals(kafkaSearchDto.getTopic()) || "6".equals(kafkaSearchDto.getTopic())) {
            if (StringUtils.isEmpty(kafkaSearchDto.getRegex())) {
                throw new BusinessException("'报文数据'和'实时数据'查询需要填写车辆vin过滤");
            }
        }
    }
}
