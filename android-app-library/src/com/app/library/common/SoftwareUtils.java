package com.app.library.common;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 软件更新工具类
 * 
 * @author savant-pan
 * 
 */
public class SoftwareUtils {
	private static final String FILE_DIR = "/temp";

	/**
	 * 判断程序是否安装
	 * 
	 * @param context
	 *            上下文
	 * @param pkgName
	 *            程序包名
	 * @return
	 */
	public static boolean isApplicationInstalled(final Context context, final String pkgName) {
		if (TextUtils.isEmpty(pkgName)) {
			return false;
		}
		if (null == context) {
			throw new IllegalArgumentException("context may not be null.");
		}
		try {
			context.getPackageManager().getPackageInfo(pkgName, 0);
			return true;
		} catch (final NameNotFoundException e) {
			// Application not installed.
		}
		return false;
	}

	/**
	 * 取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		return getVersionCode(context, context.getPackageName());
	}

	/**
	 * 取软件版本号
	 * 
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static int getVersionCode(Context context, String pkgName) {
		return getPackageInfo(context, pkgName).versionCode;
	}

	/**
	 * 取软件版名
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getVersionName(context, context.getPackageName());
	}

	/**
	 * 取软件版名
	 * 
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static String getVersionName(Context context, String pkgName) {
		return getPackageInfo(context, pkgName).versionName;
	}

	/**
	 * 判断是版本是否更新
	 * 
	 * @param context
	 * @param pkgName
	 * @param versionCodeNew
	 * @return
	 */
	public static boolean hasNewVersion(Context context, String pkgName, int versionCodeNew) {
		if (null == context) {
			throw new IllegalArgumentException("context may not be null.");
		}
		try {
			int versionCode = getVersionCode(context, pkgName);
			if (versionCode < versionCodeNew) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private static PackageInfo getPackageInfo(Context context, String pkgName) {
		PackageInfo packageInfo = null;
		try {
			PackageManager pm = context.getPackageManager();
			packageInfo = pm.getPackageInfo(pkgName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			packageInfo = new PackageInfo();
			packageInfo.versionCode = 1;
			packageInfo.versionName = "1.0";
		}
		return packageInfo;
	}

	/**
	 * 更新文件路径
	 */
	public static String getSdcardFileName(String fileName) {
		return Environment.getExternalStorageDirectory() + FILE_DIR + "/" + fileName;
	}

	/**
	 * 检测是否有SD卡[有测清空程序目录下文件]
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasSDCard(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			String dir = Environment.getExternalStorageDirectory() + FILE_DIR;
			File f = new File(dir);
			if (!f.exists()) {
				f.mkdir();
			} else {
				try {
					for (File t : f.listFiles()) {
						t.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 启动安装
	 */
	public static void startInstall(final Context context, String fileName) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + getSdcardFileName(fileName)),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
