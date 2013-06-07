An improved Android Asynchronous HTTP Library easy to use!
====================

This project, used as android-app-framwork's http library, which based on Asynchronous HTTP Library for Android.

1.android-app-framwork
https://github.com/allthelucky/android-app-framwork 

2.Asynchronous HTTP Library for Android
https://github.com/loopj/android-async-http

--------------------
UsageSample

    private static final int REQUEST_GET_ID = 0;
	private static final int REQUEST_POST_ID = 1;
	private static final int REQUEST_POST_JSON_ID = 2;
	private static final int REQUEST_POST_XML_ID = 3;

	public void sample(Context context) {
		get(context);
		postParams(context);
		postJSONObject(context);
		postXML(context);
	}

	/**
	 * get by url
	 */
	private void get(Context context) {
		RequestManager.getInstance().get(context, "http://test.com/api.php", requestListener, REQUEST_GET_ID);
	}

	/**
	 * post by RequestParams
	 */
	private void postParams(Context context) {
		final RequestParams params = new RequestParams();
		params.put("key1", "value1");
		params.put("key2", "value2");
		RequestManager.getInstance().post(context, "http://test.com/api.php", params, requestListener, REQUEST_POST_ID);
	}

	/**
	 * post by JSONObject
	 */
	private void postJSONObject(Context context) {
		final JSONObject json = new JSONObject();
		try {
			json.put("key1", "value1");
			json.put("key2", "value2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		RequestManager.getInstance().post(context, "http://test.com/api.php", json, requestListener,
				REQUEST_POST_JSON_ID);
	}

	/**
	 * post by xml
	 */
	private void postXML(Context context) {
		final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><key1>value1</key1><key2>value2</key2>";
		RequestManager.getInstance()
				.post(context, "http://test.com/api.php", xml, requestListener, REQUEST_POST_XML_ID);
	}

	/**
	 * request listener
	 */
	private RequestListener requestListener = new RequestListener() {
		@Override
		public void onStart() {
			// showDialog();
		}

		@Override
		public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
			// dismissDialog();
			if (REQUEST_GET_ID == actionId) {
				if (RequestListener.OK == statusCode) {
					// sucess
				} else {
					// handler error case
				}
			} else if (REQUEST_POST_ID == actionId) {

			} else if (REQUEST_POST_JSON_ID == actionId) {

			} else if (REQUEST_POST_XML_ID == actionId) {

			}
		}
	};
