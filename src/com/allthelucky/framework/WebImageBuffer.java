package com.allthelucky.framework;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * Buffer Utils for WebImageView
 * 
 * @author savant
 * 
 */
public class WebImageBuffer {
	private final static HashMap<String, Bitmap> caches = new HashMap<String, Bitmap>();

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
