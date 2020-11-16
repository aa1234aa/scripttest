package com.bitnei.cloud.lee.common.ueditor.define;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class MIMEType {

	public static final Map<String, String> types = Maps.newHashMap();

	static {
		types.put( "image/gif", ".gif" );
		types.put( "image/jpeg", ".jpg" );
		types.put( "image/jpg", ".jpg" );
		types.put( "image/png", ".png" );
		types.put( "image/bmp", ".bmp" );
	}
	
	public static String getSuffix ( String mime ) {
		return MIMEType.types.get( mime );
	}
	
}
