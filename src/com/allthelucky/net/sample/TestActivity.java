package com.allthelucky.net.sample;

import org.json.JSONException;
import org.json.JSONObject;

import com.allthelucky.net.ApplicationUtils;
import com.allthelucky.net.RequestListener;
import com.allthelucky.net.RequestManager;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;

/**
 * 网络请求测试
 * 
 * @author savant
 * 
 */
public class TestActivity extends BaseActivity {
    private RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestManager = RequestManager.getInstance();
        testHttpsRequest();
        testParamsListRequest();
        testJSONObjectRequest();
        testXMLRequest();
    }

    @Override
    public void onBackPressed() {
        if (this.requestManager != null) {// 取消请求
            this.requestManager.cancel(TestActivity.this);
        }
        super.onBackPressed();
    }

    private RequestListener requestListener = new RequestListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
            System.out.println("==========" + actionId + ":" + description + "===========");
            if (RequestListener.ERR != statusCode) {
                System.out.println("result:" + ApplicationUtils.bytesToString(data));
            }
        }
    };

    private void testHttpsRequest() {
        final String url = "https://github.com";
        requestManager.get(TestActivity.this, url , requestListener, -1);
    }

    /**
     * 参数列表请求
     */
    private void testParamsListRequest() {
        final String url = "http://www.winfirm.net/api/list.asp";
        final RequestParams params = new RequestParams();
        params.put("cid", "2");
        requestManager.post(TestActivity.this, url, params, requestListener, 0);
    }

    /**
     * JSON-RPC请求
     */
    private void testJSONObjectRequest() {
        final String url = "http://h.qdone.net.cn/console/mainPage!loadPage.action";
        final JSONObject root = new JSONObject();
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
        this.requestManager.post(TestActivity.this, url, root, requestListener, 1);
    }

    /**
     * XML-RPC请求
     */
    private void testXMLRequest() {
        final String url = "http://tcopenapitest.17usoft.com/handlers/General/creditcardhandler.ashx";
        final String params = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><header><accountID>f94a3630-567d-414a-90f6-affc27856467</accountID><digitalSign>565241e17eb9ef582d9b45ab1a718392</digitalSign><reqTime>2012-11-13 09:56:02.595</reqTime><serviceName>GetHotelList</serviceName><version>20111128102912</version></header><body><cityId>395</cityId><searchFields>hotelName,address</searchFields><pageSize>10</pageSize><cs>2</cs><page>1</page><sortType>1</sortType><radius>5000</radius><clientIp>27.17.16.174</clientIp><comeDate>2012-11-13</comeDate><leaveDate>2012-11-14</leaveDate></body></request>";
        this.requestManager.post(TestActivity.this, url, params, requestListener, 2);
    }

    private static JSONObject getInvoker() throws JSONException {
        final JSONObject invoker = new JSONObject();
        invoker.put("CSN", "2931F2761086E59E0100");
        invoker.put("PHONE", "");
        invoker.put("OSNAME", "Android");
        invoker.put("OSVER", "2.2.1");
        invoker.put("OSDESCRIPT", "w480h800");
        return invoker;
    }

}
