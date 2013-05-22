package com.allthelucky.framework;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utils　for Application
 * 
 * @author savant
 * 
 */
public class BaseUtils {
	/**
	 * 字节码数据 转 字符串工具
	 */
	public static String bytesToString(byte[] data) {
		if (data == null)
			return null;
		try {
			return new String(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 字节码数据(字符)转JSONObject JSONObject
	 * 
	 * @param data
	 * @return
	 */
	public static JSONObject bytesToJSONObject(byte[] data) {
		if (data == null)
			return null;
		return stringToJSONObject(bytesToString(data));
	}

	/**
	 * 字符串转JSONObject工具
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject stringToJSONObject(String json) {
		if (json == null)
			return null;
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


}
