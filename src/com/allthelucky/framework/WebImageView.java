package com.allthelucky.framework;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 网络图片控件
 */
public class WebImageView extends ImageView {

    private Context context;

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
        init(context);
    }

    /**
     * @param context
     */
    private void init(Context context) {
        this.context = context;
    }

    /**
     * Sets the content of this ImageView to the specified URL.
     * 
     * @param url
     *            The URL of an image
     */
    public void setURLAsync(String url) {
        setURLAsync(url, android.R.drawable.btn_default);
    }

    /**
     * @param url
     * @param loadNoCache
     */
    public synchronized void setURLAsync(String url, int defaultImage) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestManager.getInstance().get(context, url, new ImageRequestListener(url, defaultImage), true, 0);
    }

    final class ImageRequestListener implements RequestListener {
        private String url;
        private int defaultImage;

        public ImageRequestListener(String url, int defaultImage) {
            this.url = url;
            this.defaultImage = defaultImage;
        }

        @Override
        public void onStart() {
            
        }

        @Override
        public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
            if (actionId == 0) {
                if (null != data && statusCode != RequestListener.ERR) {
                    BitmapFactory.Options options = new Options();
                    options.inDither = false; /* 不进行图片抖动处理 */
                    options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
                    options.inSampleSize = 2; /* 图片长宽方向缩小倍数 */
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) {
                        setImageBitmap(bitmap);
                        WebImageBuffer.put(url, bitmap);
                    } else {
                        bitmap = WebImageBuffer.get(url);
                        if (null != bitmap) {
                            setImageBitmap(bitmap);
                        }
                    }
                } else {
                    Bitmap bitmap = WebImageBuffer.get(url);
                    if (bitmap == null) {
                        setImageDrawable(getResources().getDrawable(defaultImage));
                    } else {
                        setImageBitmap(bitmap);
                    }
                }
            } else {
                setImageDrawable(getResources().getDrawable(defaultImage));
            }
        }
    }

}
