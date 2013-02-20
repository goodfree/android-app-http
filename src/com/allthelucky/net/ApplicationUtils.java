package com.allthelucky.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utils　for Application
 * 
 * @author savant
 * 
 */
public class ApplicationUtils {

    /**
     * 字节码数据 转 字符串工具
     */
    public static String bytesToString(byte[] data) {
        String ret = null;
        try {
            ret = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 字节码数据(字符)转JSONObject JSONObject
     * 
     * @param data
     * @return
     */
    public static JSONObject bytesToJSONObject(byte[] data) {
        return stringToJSONObject(bytesToString(data));
    }

    /**
     * 字符串转JSONObject工具
     * 
     * @param json
     * @return
     */
    public static JSONObject stringToJSONObject(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    /**
     * 检验网络是否有连接，有则true，无则false
     * 
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

}
