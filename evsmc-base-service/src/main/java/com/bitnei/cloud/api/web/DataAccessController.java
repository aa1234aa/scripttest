package com.bitnei.cloud.api.web;

import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.DataAccessKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Api(value = "开放平台-获取权限", description = "开放平台-获取权限", tags = {"开放平台-获取权限"})
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/" + Version.VERSION_V1 + "/api")
public class DataAccessController {

    /**
     * 根据用户ID获取权限
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据用户ID获取权限")
    @GetMapping(value = "/dataAccess")
    @ResponseBody
    public String getDataAccess(@RequestParam String userId, @RequestParam String table,
                                @RequestParam String alias) {

        return DataAccessKit.getUserAuthSql(userId, table, alias);
    }
}
