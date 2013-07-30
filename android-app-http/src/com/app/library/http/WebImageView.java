package com.app.library.http;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView with a network image RequestManager
 * 
 * @author savant-pan
 */
public class WebImageView extends ImageView {
	private static final int DEFAULT_DRAWABLE = -1;

	private int defaultDrawable = DEFAULT_DRAWABLE;
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
		this.imageUrl = url;
		this.defaultDrawable = defaultDrawable;
		this.firstLoad();
	}

	/**
	 * first load image
	 */
	private void firstLoad() {
		this.loadResource();
	}

	/**
	 * retry cache when failed first time
	 */
	private void retryCache() {
		this.loadResource();
	}

	private void loadResource() {
		if (TextUtils.isEmpty(imageUrl)) {
			this.setDefaultImage();
		} else {
			RequestManager.getInstance().get(getContext(), imageUrl, null, requestListener, true, 0);
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
					// decode image size
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(data, 0, data.length, o);

					// Find the correct scale value. It should be the power of
					final int REQUIRED_SIZE = 100;
					int width_tmp = o.outWidth, height_tmp = o.outHeight;
					int scale = 1;
					while (true) {
						if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale *= 2;
					}

					// decode with inSampleSize
					BitmapFactory.Options options = new Options();
					options.inSampleSize = scale;
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
