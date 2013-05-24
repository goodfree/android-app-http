package com.app.library;

import android.content.Context;

import com.app.library.common.DebugLogger;
import com.app.library.common.ResourceUtils;

/**
 * 基本库配置工具类
 * 
 * @author savant-pan
 * 
 */
public final class LibraryConfig {
	/**
	 * class中所用“确定”
	 */
	public static final String CONFIRM_TEXT = "确 定";

	/**
	 * class中所用“取 消”
	 */
	public static final String CANCEL_TEXT = "取 消";

	/**
	 * class中所用“提 示”
	 */
	public static final String ALERT_TEXT = "提 示";

	/**
	 * class中所用“正在加载，请稍候...”
	 */
	public static final String LOADING_TEXT = "正在加载，请稍候...";

	/**
	 * class中所用“正在下载，请稍候...”
	 */
	public static final String DOWNLOADING_TEXT = "正在下载，请稍候...";

	/**
	 * class中所用“程序下载失败...”
	 */
	public static final String DOWNLOAD_ERR_TEXT = "程序下载失败...";

	/**
	 * class中所用“网络请求失败，请稍候再试...”
	 */
	public static final String NETWORK_ERR_TEXT = "网络请求失败，请稍候再试...";

	/**
	 * 初始化基本库（建议在Application子类中进行）
	 * 
	 * @param context
	 *            上下文对象
	 * @param debug
	 *            调试开关标志
	 */
	public static void init(Context context, boolean debug) {
		DebugLogger.init(debug);
		ResourceUtils.init(context);
	}
}
