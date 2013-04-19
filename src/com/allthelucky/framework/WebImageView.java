package com.allthelucky.framework;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.widget.ImageView;

/**
 * Web ImageView
 * 
 * @author pxw
 */
public class WebImageView extends ImageView {
    protected static final String TAG = "WebImageView";
    private int defaultImage = R.drawable.ic_launcher;
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
     * 设置请求网址
     * 
     * @param url
     */
    public void setURLAsync(String url) {
        setURLAsync(url, R.drawable.ic_launcher);
    }

    /**
     * 设置请求网址及默认资源
     * 
     * @param url
     * @param defaultImage
     */
    public void setURLAsync(String url, int defaultImage) {
        this.imageUrl = url;
        this.defaultImage = defaultImage;
        this.firstLoad();
    }

    /**
     * 第一次加载，如有更新加载网络，否则加载缓存
     */
    private void firstLoad() {
        this.loadResource(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    /**
     * 加载失败的情况下，试图仅从缓存加载
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
     * 网络加载结果处理
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
                Bitmap bitmap = WebImageBuffer.get(imageUrl);
                if (null != bitmap) {
                    setImageBitmap(bitmap);
                } else {
                    if (null != data) {
                        BitmapFactory.Options options = new Options();
                        options.inDither = false; /* 不进行图片抖动处理 */
                        options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
                        options.inSampleSize = 1; /* 图片长宽方向缩小倍数 */
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
        }
    };

    /**
     * 设置默认资源图片
     */
    public void setDefaultImage() {
        setImageDrawable(getResources().getDrawable(defaultImage));
    }

    /**
     * 重置 WebImageBuffer
     */
    public static void resetWebImageBuffer() {
        WebImageBuffer.clear();
    }

    /**
     * 图像Bitmap缓存工具
     * 
     * @author pxw
     * 
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
