package com.allthelucky.sample;

import org.json.JSONException;
import org.json.JSONObject;

import com.allthelucky.net.RequestListener;
import com.allthelucky.net.RequestManager;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestListener requestListener=new RequestListener() {
            @Override
            public void onStart() {
                
            }
            
            @Override
            public void onCompleted(byte[] data, int statusCode, int actionId) {
                System.out.println("=====================");
                if(RequestListener.ERR!=statusCode) {
                    String result = new String(data);
                    System.out.println(result);
                }
            }
        };
        final String url = "http://h.qdone.net.cn/console/mainPage!loadPage.action";
        JSONObject root = new JSONObject();
        try {
            root.put("ACTION_INVOKER", getInvoker());
            JSONObject params = new JSONObject();
            params.put("userLatitude", "");
            params.put("userLongitude", "");
            params.put("cityId", "2001");
            params.put("cityName", "武汉市");
            params.put("type", 0);
            params.put("start", 1);
            params.put("pageSize", 10);
            root.put("ACTION_INFO", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            
        RequestManager.getInstance().get(TestActivity.this, url, requestListener, 0);
    }
    
    private static JSONObject getInvoker() throws JSONException {
        JSONObject invoker = new JSONObject();
        invoker.put("CSN", "2931F2761086E59E0100");
        invoker.put("PHONE", "");
        invoker.put("OSNAME", "Android");
        invoker.put("OSVER", "2.2.1");
        invoker.put("OSDESCRIPT", "w480h800");
        return invoker;
    }
    
    
}
