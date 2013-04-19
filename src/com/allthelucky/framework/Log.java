package com.allthelucky.framework;

/**
 * @description: overide Log utils
 * 
 * @author pxw(www.allthelucky.com)
 * 
 */
public final class Log {

	private static boolean mDebug = false;

	public static void init(final boolean debug) {
		mDebug = debug;
	}

	public static void e(final String tag, final String msg) {
		if (mDebug) {
			android.util.Log.e(tag, msg);
		}
	}

	public static void e(final String tag, final String msg, final Throwable tr) {
		if (mDebug) {
			android.util.Log.e(tag, msg, tr);
		}
	}

	public static void d(final String tag, final String msg) {
		if (mDebug) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void d(final String tag, final String msg, final Throwable tr) {
		if (mDebug) {
			android.util.Log.d(tag, msg, tr);
		}
	}

}
