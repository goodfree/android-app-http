package com.app.library.common.manager;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;

import com.app.library.common.BaseDialog;

/**
 * 对话框工具类
 * 
 * @author savant-pan
 * 
 */
public class DialogManager {

	/**
	 * 显示对话框，确定和取消自定义文字
	 * 
	 * @param context
	 * @param message
	 * @param onClickListener
	 */
	public static void showDialog(Activity context, String confirmText, String cancelText, CharSequence message,
			OnClickListener onClickListener) {
		showDialog(context, confirmText, cancelText, message, Gravity.CENTER_HORIZONTAL, onClickListener);
	}

	/**
	 * 显示对话框，确定和取消自定义文字
	 * 
	 * @param context
	 * @param confirmText
	 * @param cancelText
	 * @param message
	 * @param gravity
	 *            文字对齐方式
	 * @param onClickListener
	 */
	public static void showDialog(Activity context, String confirmText, String cancelText, CharSequence message,
			int gravity, OnClickListener onClickListener) {
		BaseDialog dialog = new BaseDialog(context);
		dialog.setCancelable(false);
		dialog.setTitle();
		dialog.setMessage(message, gravity);
		dialog.setPositiveButton(confirmText, onClickListener);
		dialog.setNegativeButton(cancelText, onClickListener);
		dialog.show();
	}

	/**
	 * 显示对“确定”+“取消”话框，
	 * 
	 * @param context
	 * @param message
	 *            提示信息
	 * @param onClickListener
	 */
	public static void showDialog(Activity context, CharSequence message, OnClickListener onClickListener) {
		dialog(context, message, 0, onClickListener);
	}

	/**
	 * 显示“确定”对话框，
	 * 
	 * @param context
	 * @param message
	 * @param onClickListener
	 */
	public static void showConfirmDialog(Activity context, CharSequence message, OnClickListener onClickListener) {
		dialog(context, message, 1, onClickListener);
	}

	/**
	 * 显示对话框，“取消”
	 * 
	 * @param context
	 * @param message
	 * @param onClickListener
	 */
	public static void showCancelDialog(Activity context, CharSequence message, OnClickListener onClickListener) {
		dialog(context, message, 2, onClickListener);
	}

	private static void dialog(Activity context, CharSequence message, int type, OnClickListener onClickListener) {
		final BaseDialog dialog = new BaseDialog(context);
		dialog.setCancelable(false);
		dialog.setTitle();
		dialog.setMessage(message);
		if (type == 0) {// 取消+确定
			dialog.setNegativeButton(onClickListener);
			dialog.setPositiveButton(onClickListener);
		} else if (type == 1) {// 确定
			dialog.setPositiveButton(onClickListener);
		} else {// 取消
			dialog.setNegativeButton(onClickListener);
		}
		if (context.isFinishing()) {
			return;
		}
		dialog.show();
	}

	/**
	 * 显示Spinner风格Dialog
	 * 
	 * @param context
	 * @param list
	 * @param listener
	 */
	public static void showSpinnerDialog(Context context, String data[], SpinnerListener listener) {
		if (data == null || data.length == 0) {
			throw new NullPointerException();
		}
		showSpinnerDialog(context, Arrays.asList(data), listener);
	}

	/**
	 * 显示Spinner风格Dialog
	 * 
	 * @param context
	 * @param list
	 * @param listener
	 */
	public static void showSpinnerDialog(Context context, List<String> list, SpinnerListener listener) {
		final SpinnerDialog dialog = new SpinnerDialog(context);
		dialog.setCancelable(true);
		dialog.init(list, listener).show();
	}

	/**
	 * 显示更新下载对话框
	 * 
	 * @param context
	 * @param message
	 *            提示消息
	 * @param url
	 *            文件下载地址
	 * @param fileName
	 *            文件名
	 * @param donwloadListener
	 */
	public static void showUpdateDialog(Context context, CharSequence message, String url, String fileName,
			UpdateListener donwloadListener) {
		final UpdateDialog dialog = new UpdateDialog(context);
		dialog.setTitle();
		dialog.setMessage(message);
		dialog.setUpdateParams(url, fileName, donwloadListener);
		dialog.show();
	}

}
