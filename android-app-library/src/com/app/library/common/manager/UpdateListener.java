package com.app.library.common.manager;

/**
 * UpdateDialog下载监听接口
 */
public interface UpdateListener {

	/**
	 * 下载已取消
	 */
	public static final int DOWNLOAD_CANCEL = -2;

	/**
	 * 下载失败
	 */
	public static final int DOWNLOAD_ERR = -1;

	/**
	 * 下载成功
	 */
	public static final int DOWNLOAD_OK = 0;

	/**
	 * 正在下载
	 */
	public static final int DOWNLOADING = 1;

	/**
	 * 下载结果提示
	 * 
	 * @param code
	 */
	public abstract void onFinish(int code);
}