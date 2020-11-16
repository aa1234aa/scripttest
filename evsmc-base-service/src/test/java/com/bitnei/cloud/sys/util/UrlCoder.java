package com.bitnei.cloud.sys.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlCoder {

	public static String urlDecoder(String decodeUrlParm) {

		String decoderStr = null;
		try {
			decoderStr = URLDecoder.decode(decodeUrlParm, "utf-8");
			System.out.println("解码" + decoderStr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decoderStr;
	}

	public static String urlEncoder(String encodeUrlParm) {
		String encodeStr = null;
		try {
			encodeStr = URLEncoder.encode(encodeUrlParm, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("编码" + encodeStr);
		return encodeStr;
	}

}
