package com.allthelucky.net.sample;

import java.util.ArrayList;
import java.util.WeakHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.allthelucky.R;
import com.allthelucky.net.RequestListener;
import com.allthelucky.net.RequestManager;
import com.allthelucky.net.WebImageView;
import com.loopj.android.http.RequestParams;

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
        setContentView(R.layout.main);
        this.requestManager = RequestManager.getInstance();

        steListView();
        testGetCache();
        testHttpsRequest();
        testParamsListRequest();
        testJSONObjectRequest();
        testXMLRequest();
    }

    private void steListView() {
        ListView lv = (ListView) findViewById(R.id.listView1);
        ArrayList<String> list = new ArrayList<String>();
        list.add("http://i1.dpfile.com/s/i/app/api/images/dp-logo.797f3f7b8918e305370e7c049af1089c.png");
        list.add("http://i3.dpfile.com/s/i/app/api/images/dp-logo1.1e0b679b006c0620a33349a7cdf92a6b.png");
        list.add("http://i2.dpfile.com/s/i/app/api/images/app-logo1.7db1d07c60b54c8434da19c57acaa567.png");
        list.add("http://i2.dpfile.com/s/i/app/api/images/accr-logo3.38af0ad2ec67a5b7062d36b800b80b48.png");
        list.add("http://i3.dpfile.com/s/i/app/api/images/accr-logo4.4373ec1313563fbd92cb037f86ac5cae.png");
        list.add("http://i2.dpfile.com/s/i/app/api/images/brandstst.84014d9d118d22e8a76cd6e1ca5c7e13.png");
        list.add("http://www.baidu.com/img/shouye_b5486898c692066bd2cbaeda86d74448.gif");
        list.add("http://www.winfirm.net/uploadfile/small/201112212124444097/340x292.jpg");
        list.add("http://www.winfirm.net/uploadfile/201006/11/2141588505.jpg");
        lv.setAdapter(new ImageAdapter(this, list));
    }

    class ImageAdapter extends ArrayAdapter<String> {
        private Context context;
        private WeakHashMap<Integer, View> map;

        public ImageAdapter(Context context, ArrayList<String> list) {
            super(context, 0, list);
            this.context = context;
            this.map= new WeakHashMap<Integer, View>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          final  String url =  getItem(position);
            convertView = map.get(position);
            if(convertView!=null) {
                return convertView;
            } else {
                View v =  LayoutInflater.from(context).inflate(R.layout.image, null);
                WebImageView imageView = (WebImageView) v.findViewById(R.id.imageView1);
                imageView.setURLAsync(url);  
                map.put(position, v);
                return v;
            }
        }
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
                // System.out.println("result:" +
                // ApplicationUtils.bytesToString(data));
            }
        }
    };

    private void testGetCache() {
        final String url = "http://www.winfirm.net/helloworld.html";
        requestManager.get(TestActivity.this, url, requestListener, true, -2);
    }

    private void testHttpsRequest() {
        final String url = "https://github.com";
        requestManager.get(TestActivity.this, url, requestListener, -1);
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
