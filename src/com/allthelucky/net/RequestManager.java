package com.allthelucky.net;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RequestManager {
    private final AsyncHttpClient asyncHttpClient;
    private static RequestManager INSTANCE = null;

    protected RequestManager() {
        this.asyncHttpClient = new AsyncHttpClient();
    }

    public static RequestManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestManager();
        }
        return INSTANCE;
    }

    public void cancel(Context context) {
        asyncHttpClient.cancelRequests(context, true);
    }

    /**
     * 参数列表请求
     * 
     * @param context
     * @param url
     * @param params
     * @param requestListener
     * @param actionId
     */
    public void post(Context context, String url, RequestParams params, RequestListener requestListener, int actionId) {
        asyncHttpClient.post(context, url, params, new RequestBinaryHttpResponseHandler(requestListener, actionId));
    }

    /**
     * JSON　参数请求
     * 
     * @param context
     * @param url
     * @param params
     * @param requestListener
     * @param actionId
     */
    public void post(Context context, String url, JSONObject params, RequestListener requestListener, int actionId) {
        asyncHttpClient.post(context, url, rpcToEntity(params.toString(), "application/json"), "application/json",
                new RequestBinaryHttpResponseHandler(requestListener, actionId));
    }

    /**
     * JSON　参数请求
     * 
     * @param context
     * @param url
     * @param headers
     * @param params
     * @param requestListener
     * @param actionId
     */
    public void post(Context context, String url, Header[] headers, JSONObject params, RequestListener requestListener,
            int actionId) {
        asyncHttpClient.post(context, url, headers, rpcToEntity(params.toString(), "application/json"),
                "application/json", new RequestBinaryHttpResponseHandler(requestListener, actionId));
    }

    /**
     * XML　参数请求
     * 
     * @param context
     * @param url
     * @param params
     * @param requestListener
     * @param actionId
     */
    public void post(Context context, String url, String params, RequestListener requestListener, int actionId) {
        asyncHttpClient.post(context, url, rpcToEntity(params, "application/xml"), "application/xml",
                new RequestBinaryHttpResponseHandler(requestListener, actionId));
    }

    /**
     * XML　参数请求
     * 
     * @param context
     * @param url
     * @param headers
     * @param params
     * @param requestListener
     * @param actionId
     */
    public void post(Context context, String url, Header[] headers, String params, RequestListener requestListener,
            int actionId) {
        asyncHttpClient.post(context, url, headers, rpcToEntity(params, "application/xml"), "application/xml",
                new RequestBinaryHttpResponseHandler(requestListener, actionId));
    }

    public void get(Context context, String url, RequestListener requestListener, int actionId) {
        asyncHttpClient.get(context, url, new RequestBinaryHttpResponseHandler(requestListener, actionId));
    }

    /**
     * 将JSON/XML字符串转为HttpEntity(StringEntity)
     * 
     * @param params
     * @param contentType
     * @return
     */
    public static HttpEntity rpcToEntity(String params, String contentType) {
        StringEntity entity = null;
        if (!TextUtils.isEmpty(params)) {
            try {
                entity = new StringEntity(params);
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    final class RequestBinaryHttpResponseHandler extends BinaryHttpResponseHandler {
        private RequestListener requestListener;
        private int actionId;

        public RequestBinaryHttpResponseHandler(RequestListener requestListener, int actionId) {
            this.requestListener = requestListener;
            this.actionId = actionId;
        }

        @Override
        public void onStart() {
            super.onStart();
            requestListener.onStart();
        }
        
        @Override
        public void onFinish() {
            super.onFinish();
        }

        @Override
        public void onSuccess(int statusCode, byte[] binaryData) {
            super.onSuccess(statusCode, binaryData);
            requestListener.onCompleted(binaryData, RequestListener.OK, actionId);
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            requestListener.onCompleted(null, RequestListener.ERR, actionId);
        }
    }
}
