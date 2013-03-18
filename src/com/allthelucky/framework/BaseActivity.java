package com.allthelucky.framework;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends Activity {

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public void showDialog() {
		if (null == progressDialog) {
			progressDialog = ProgressDialog.show(BaseActivity.this, "", "正在加载，请稍候...");
			progressDialog.setCancelable(false);
		} else {
			progressDialog.show();
		}
		progressDialog.setOnKeyListener(onKeyListener);
	}

	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				dismissDialog();
			}
			return false;
		}
	};

	public void dismissDialog() {
		if (isFinishing()) {
			return;
		}
		if (null != progressDialog && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void showToast(final String message) {
		Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		if (progressDialog != null && progressDialog.isShowing()) {
			dismissDialog();
		} else {
			super.onBackPressed();
		}
	}
}
