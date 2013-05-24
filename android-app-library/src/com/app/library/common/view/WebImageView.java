package com.app.library.common.view;

import java.util.HashMap;

import com.app.library.common.manager.RequestListener;
import com.app.library.common.manager.RequestManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.widget.ImageView;

/**
 * ImageView with a network image RequestManager
 * 
 * @author savant-pan
 */
public class WebImageView extends ImageView {
	private static final int DEFAULT_DRAWABLE = -1;
	private static final int DEFAULT_SAMPLE_SIZE = 1;

	private int defaultDrawable = DEFAULT_DRAWABLE;
	private int sampleSize = DEFAULT_SAMPLE_SIZE;
	private String imageUrl = "";
	private boolean hasRetry = false;

	/**
	 * @param context
	 */
	public WebImageView(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public WebImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * set a url
	 * 
	 * @param url
	 *            network resource address
	 */
	public void setURLAsync(String url) {
		this.setURLAsync(url, DEFAULT_DRAWABLE);
	}

	/**
	 * set a url and default drawable
	 * 
	 * @param url
	 *            network resource address
	 * @param defaultImage
	 *            drawable id
	 */
	public void setURLAsync(String url, int defaultDrawable) {
		this.setURLAsync(url, defaultDrawable, DEFAULT_SAMPLE_SIZE);
	}

	/**
	 * set a url and default drawable
	 * 
	 * @param url
	 *            network resource address
	 * @param defaultImage
	 *            drawable id
	 * @param sampleSize
	 *            sample for Options
	 */
	public void setURLAsync(String url, int defaultDrawable, int sampleSize) {
		this.imageUrl = url;
		this.defaultDrawable = defaultDrawable;
		this.sampleSize = sampleSize;
		this.firstLoad();
	}

	/**
	 * first load image
	 */
	private void firstLoad() {
		this.loadResource(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	/**
	 * retry cache when failed first time
	 */
	private void retryCache() {
		this.loadResource(WebSettings.LOAD_CACHE_ONLY);
	}

	private void loadResource(int cacheMode) {
		if (TextUtils.isEmpty(imageUrl)) {
			this.setDefaultImage();
		} else {
			RequestManager.getInstance().get(getContext(), imageUrl, requestListener, true, 0);
		}
	}

	/**
	 * load callback for RequestManager
	 */
	private RequestListener requestListener = new RequestListener() {

		@Override
		public void onStart() {

		}

		@Override
		public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
			if (RequestListener.ERR == statusCode) {
				if (!hasRetry) {
					hasRetry = true;
					retryCache();
				} else {
					setDefaultImage();
				}
			} else {
				setResult(data);
			}
		}

		private void setResult(byte[] data) {
			Bitmap bitmap = WebImageBuffer.get(imageUrl);
			if (null != bitmap) {
				setImageBitmap(bitmap);
			} else {
				if (null != data) {
					BitmapFactory.Options options = new Options();
					options.inDither = false;
					options.inPreferredConfig = null;
					options.inSampleSize = sampleSize;
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
					if (bitmap != null) {
						setImageBitmap(bitmap);
						WebImageBuffer.put(imageUrl, bitmap);
					} else {
						setDefaultImage();
					}
				} else {
					setDefaultImage();
				}
			}
		}
	};

	/**
	 * set default drawable
	 */
	public void setDefaultImage() {
		if (defaultDrawable != -1) {
			setImageDrawable(getResources().getDrawable(defaultDrawable));
		}
	}

	/**
	 * reset WebImageBuffer
	 */
	public static void resetWebImageBuffer() {
		WebImageBuffer.clear();
	}

	/**
	 * WebImageBuffer for WebImageView
	 */
	public static class WebImageBuffer {
		private final static HashMap<String, Bitmap> caches = new HashMap<String, Bitmap>();

		public synchronized static void clear() {
			try {
				caches.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public synchronized static Bitmap get(String url) {
			try {
				return caches.get(url);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public synchronized static void put(String url, Bitmap bitmap) {
			try {
				caches.put(url, bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
