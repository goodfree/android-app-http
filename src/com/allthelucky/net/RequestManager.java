package com.allthelucky.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Request Manager based on AsyncHttpClient
 * 
 * @author savant
 * 
 */
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
        asyncHttpClient.post(context, url, params, new RequeseHttpResponseHandler(requestListener, actionId));
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
                new RequeseHttpResponseHandler(requestListener, actionId));
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
                "application/json", new RequeseHttpResponseHandler(requestListener, actionId));
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
                new RequeseHttpResponseHandler(requestListener, actionId));
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
                new RequeseHttpResponseHandler(requestListener, actionId));
    }

    public void get(Context context, String url, RequestListener requestListener, int actionId) {
        get(context, url, requestListener, false, actionId);
    }

    public void get(Context context, String url, RequestListener requestListener, boolean cache, int actionId) {
        if (!cache) {
            asyncHttpClient.get(context, url, new RequeseHttpResponseHandler(requestListener, actionId));
        } else {
            if (!hasCache(context, ApplicationUtils.urlEncode(url))) {
                asyncHttpClient.get(context, url, new RequeseHttpResponseHandler(new CacheRequestListener(context, url,
                        requestListener), actionId));
            } else {
                loadCache(context, ApplicationUtils.urlEncode(url), requestListener, actionId);
            }
        }
    }

    private class CacheRequestListener implements RequestListener {

        private Context context = null;
        private String url = "";
        private RequestListener requestListener = null;

        public CacheRequestListener(Context context, String url, RequestListener requestListener) {
            this.context = context;
            this.url = url;
            this.requestListener = requestListener;
        }

        @Override
        public void onStart() {
            if (requestListener != null) {
                requestListener.onStart();
            }
        }

        @Override
        public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
            if (requestListener != null) {
                requestListener.onCompleted(data, statusCode, description, actionId);
            }
            if (data != null && statusCode != RequestListener.ERR) {
                saveCache(context, url, data);
            }
        }
    }

    /**
     * 保存
     */
    private void saveCache(Context context, String url, byte[] data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            FileOutputStream os = context.openFileOutput(ApplicationUtils.encryptMD5(url), Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }

            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCache(final Context context, final String url, final RequestListener requestListener,
            final int actionId) {
        requestListener.onStart();
        new AsyncTask<Void, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(Void... params) {
                try {
                    InputStream is = context.openFileInput(ApplicationUtils.encryptMD5(url));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[4096];
                    int len = 0;
                    while ((len = is.read(bytes)) > 0) {
                        bos.write(bytes, 0, len);
                    }
                    bos.flush();
                    return bos.toByteArray();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(byte[] result) {
                boolean flag = (result != null);
                requestListener.onCompleted(result, (flag ? RequestListener.OK : RequestListener.ERR),
                        flag ? "load cache ok" : "load cache error", actionId);
            }
        }.execute();
    }

    /**
     * 检测缓存
     */
    private boolean hasCache(Context context, String url) {
        try {
            context.openFileInput(ApplicationUtils.encryptMD5(url));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    final class RequeseHttpResponseHandler extends AsyncHttpResponseHandler {
        private RequestListener requestListener;
        private int actionId;

        public RequeseHttpResponseHandler(RequestListener requestListener, int actionId) {
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
            requestListener.onCompleted(binaryData, RequestListener.OK, "server response ok", actionId);
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            requestListener.onCompleted(null, RequestListener.ERR, content, actionId);
        }
    }
}
