package com.app.library.common.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.library.LibraryConfig;
import com.app.library.common.BaseDialog;
import com.app.library.common.ResourceUtils;

class UpdateDialog extends BaseDialog {
	private static final String FILE_DIR = "/temp";

	private TextView msgText;
	private TextView pctText;
	private ProgressBar pb;

	private String downloadUrl;
	private File downloadFile;
	private UpdateListener donwloadListener;

	private DownloadThread thread;

	public UpdateDialog(Context context) {
		super(context);
		this.initView();
	}

	private void initView() {
		final View layout = LayoutInflater.from(getContext()).inflate(ResourceUtils.getLayoutId("app_dialog_update_layout"), null);
		msgText = (TextView) layout.findViewById(ResourceUtils.getId("dialog_message"));
		pctText = (TextView) layout.findViewById(ResourceUtils.getId("pct_message"));
		pb = (ProgressBar) layout.findViewById(ResourceUtils.getId("pct_progressbar"));

		msgText.setText(LibraryConfig.DOWNLOADING_TEXT);
		pb.setMax(100);
		
		super.setContentLayout(layout);		
	}

	/**
	 * 下载处理Handler
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == UpdateListener.DOWNLOADING) {
				String pct = (String) msg.obj;
				if (pct != null) {
					pctText.setText(pct + "%");
					pb.setProgress(Integer.parseInt(pct));
				}
			} else {
				dismiss();
				donwloadListener.onFinish(msg.what);
			}
		}
	};

	/**
	 * 更新提示
	 */
	public void setMessage(CharSequence message) {
		msgText.setText(message);
	}

	/**
	 * 设置下载地址和SD卡保存文件名
	 * 
	 * @param url
	 * @param fileName
	 */
	public void setUpdateParams(final String url, final String fileName, final UpdateListener donwloadListener) {
		this.downloadUrl = url;
		this.downloadFile = new File(getSdcardFileName(fileName));
		this.donwloadListener = donwloadListener;
	}

	/**
	 * 更新文件路径
	 */
	public static String getSdcardFileName(String fileName) {
		return Environment.getExternalStorageDirectory() + FILE_DIR + "/" + fileName;
	}

	@Override
	public void show() {
		setTitle();
		thread = new DownloadThread(downloadUrl, downloadFile, handler);
		setNegativeButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				thread.cancel();
			}
		});
		thread.start();
		super.show();
	}

	/**
	 * 升级文件下载线程
	 * 
	 * @author savant-pan
	 * 
	 */
	class DownloadThread extends Thread {
		private static final int DEFAULT_FILE_LEN = 3500000; // ~3.5M
		private volatile boolean shutdownRequested = false;
		private String downladUrl = "";
		private File downloadFile = null;
		private Handler downloadHandler = null;
		private boolean errorOccur = false;// 下载出错标志

		public DownloadThread(String url, File file, Handler handler) {
			this.downladUrl = url;
			this.downloadFile = file;
			this.downloadHandler = handler;
		}

		@Override
		public void run() {
			try {
				download();
			} catch (Exception e) {
				e.printStackTrace();
				setResult(UpdateListener.DOWNLOAD_ERR, "");
			}
		}

		/**
		 * 下载程序
		 */
		private void download() throws Exception {
			URL url = new URL(downladUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30 * 1000);
			conn.setRequestProperty("User-agent", "Mozilla/4.0");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/vnd.android.package-archive");

			int code = conn.getResponseCode();
			if (code != 200) {
				setResult(UpdateListener.DOWNLOAD_ERR, "");
			} else {
				if (downloadFile.exists()) { // delete old one
					downloadFile.delete();
				}
				InputStream fin = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(downloadFile);

				int total = conn.getContentLength();
				if (total == -1) { // failed
					total = DEFAULT_FILE_LEN;
				}
				int len = -1;
				int init = 0;
				byte buffer[] = new byte[1024];
				while ((len = fin.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					init += len;
					String pct = (float) init / (float) total * 100 + "";
					setResult(UpdateListener.DOWNLOADING, pct.substring(0, pct.indexOf(".")));
				}
				fos.flush();
				fin.close();
				fos.close();
				setResult(UpdateListener.DOWNLOAD_OK, "");
			}
		}

		/**
		 * 结果更新
		 * 
		 * @param what
		 * @param pct
		 */
		private void setResult(int what, String pct) {
			Message msg = null;
			if (!shutdownRequested) {
				if (what == UpdateListener.DOWNLOADING) {
					msg = downloadHandler.obtainMessage(what, pct);
				} else {
					msg = downloadHandler.obtainMessage(what);
				}
			} else {
				if (!errorOccur) {
					msg = downloadHandler.obtainMessage(UpdateListener.DOWNLOAD_CANCEL);
					errorOccur = true;// 标志已出错，避免重复提示失败
				} else {
					return;
				}
			}
			downloadHandler.sendMessage(msg);
		}

		/**
		 * 取消下载
		 */
		public void cancel() {
			shutdownRequested = true;
			interrupt();
		}
	}

}