package com.bitnei.cloud.lee.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.lee.common.ueditor.ActionEnter;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用于处理关于ueditor插件相关的请求
 * @author Guoqing
 * @date 2017-11-29
 *
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "/" + Version.VERSION_V1 + "/ueditor")
@Api(value = "富文本编辑器", description = "富文本编辑器", tags = {"富文本编辑器"})
public class UeditorController {

	@RequestMapping(value = "/exec")
	@ResponseBody
	public String exec(
//			@RequestParam(required = false, value = "upfile") MultipartFile upfile,
//					   @RequestParam(value = "action") String action,
					   HttpServletRequest request) throws UnsupportedEncodingException{
		request.setCharacterEncoding("utf-8");
		String rootPath = request.getRealPath("/");
		return new ActionEnter( request, rootPath ).exec();
	}
	
}
