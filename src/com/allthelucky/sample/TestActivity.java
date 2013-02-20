package com.allthelucky.sample;

import org.json.JSONException;
import org.json.JSONObject;

import com.allthelucky.net.RequestListener;
import com.allthelucky.net.RequestManager;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testJSONObjectRequest();
        testXMLRequest();
        requestManager = RequestManager.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (requestManager != null) {
            requestManager.cancel(TestActivity.this);
        }
    }

    private void testJSONObjectRequest() {
        RequestListener requestListener = new RequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
                System.out.println("=====================");
                System.out.println(description);

                if (RequestListener.ERR != statusCode) {
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
        RequestManager.getInstance().post(TestActivity.this, url, root, requestListener, 0);
    }

    private void testXMLRequest() {
        final String url = "http://tcopenapitest.17usoft.com/handlers/General/creditcardhandler.ashx";
        final String params = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><header><accountID>f94a3630-567d-414a-90f6-affc27856467</accountID><digitalSign>565241e17eb9ef582d9b45ab1a718392</digitalSign><reqTime>2012-11-13 09:56:02.595</reqTime><serviceName>GetHotelList</serviceName><version>20111128102912</version></header><body><cityId>395</cityId><searchFields>hotelName,address</searchFields><pageSize>10</pageSize><cs>2</cs><page>1</page><sortType>1</sortType><radius>5000</radius><clientIp>27.17.16.174</clientIp><comeDate>2012-11-13</comeDate><leaveDate>2012-11-14</leaveDate></body></request>";

        RequestListener requestListener = new RequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
                System.out.println("=====================");
                System.out.println(description);

                if (RequestListener.ERR != statusCode) {
                    String result = new String(data);
                    System.out.println(result);
                }
            }
        };
        RequestManager.getInstance().post(TestActivity.this, url, params, requestListener, 0);
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
