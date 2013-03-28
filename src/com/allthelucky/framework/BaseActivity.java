package com.allthelucky.framework;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

/**
 * base activity with dialog
 * 
 * @author savant
 * 
 */
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
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
		} else {
			progressDialog.show();
		}
	}

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
