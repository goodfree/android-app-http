package com.app.library.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.library.LibraryConfig;

/**
 * Base class for Dialog
 * 
 * @author savant-pan
 * 
 */
public class BaseDialog {
	private final Dialog dialog;
	private boolean cancelable = false;
	private Context context;

	private ImageView iconImageVeiw;
	private TextView titleTextView;
	private TextView messageTextView;
	private Button postiveBtn;
	private Button negativeBtn;

	private LinearLayout titleLayout;
	private LinearLayout contentLayout;
	private LinearLayout buttonLayout;

	private boolean showTitleLayout = false;
	private boolean showContentLayout = false;
	private boolean showButtonLayout = false;
	final LinearLayout root;

	public BaseDialog(Context context) {
		this.context = context;
		this.root = new LinearLayout(context);
		this.dialog = new Dialog(context, ResourceUtils.getStyleId("dialog_style"));
		this.init();
	}

	private void init() {
		final View layout = LayoutInflater.from(context).inflate(ResourceUtils.getLayoutId("app_dialog_layout"), null);
		titleLayout = (LinearLayout) layout.findViewById(ResourceUtils.getId("top_layout"));
		contentLayout = (LinearLayout) layout.findViewById(ResourceUtils.getId("content_layout"));
		buttonLayout = (LinearLayout) layout.findViewById(ResourceUtils.getId("button_layout"));

		iconImageVeiw = (ImageView) layout.findViewById(ResourceUtils.getId("icon_image"));
		titleTextView = (TextView) layout.findViewById(ResourceUtils.getId("title_text"));
		messageTextView = (TextView) layout.findViewById(ResourceUtils.getId("message_text"));
		postiveBtn = (Button) layout.findViewById(ResourceUtils.getId("confirm_button"));
		negativeBtn = (Button) layout.findViewById(ResourceUtils.getId("cancel_button"));

		titleTextView.setText(LibraryConfig.ALERT_TEXT);
		postiveBtn.setText(LibraryConfig.CONFIRM_TEXT);
		negativeBtn.setText(LibraryConfig.CANCEL_TEXT);

		titleLayout.setVisibility(View.GONE);
		contentLayout.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.GONE);
		postiveBtn.setVisibility(View.GONE);
		negativeBtn.setVisibility(View.GONE);

		setContentLayout(null);// show default textView
		root.setBackgroundColor(Color.TRANSPARENT);
		root.setPadding(15, 5, 15, 5);
		root.addView(layout);

		dialog.setContentView(root);
	}

	public void setContentLayout(View contentView) {
		if (contentView != null) {
			contentLayout.removeAllViews();
			contentLayout.addView(contentView);
			contentLayout.setVisibility(View.VISIBLE);
		}
	}

	protected Context getContext() {
		return context;
	}

	public void setCancelable(boolean cancel) {
		this.cancelable = cancel;
	}

	public void setIcon(int drawable) {
		this.iconImageVeiw.setImageLevel(drawable);
	}

	public void setTitle() {
		this.setTitle(LibraryConfig.ALERT_TEXT);
	}

	public void setTitle(CharSequence title) {
		this.showTitleLayout = true;
		this.titleTextView.setText(title);
	}

	public void setMessage(CharSequence message) {
		this.setMessage(message, Gravity.CENTER_HORIZONTAL);
	}

	public void setMessage(CharSequence message, int gravity) {
		this.showContentLayout = true;
		this.messageTextView.setGravity(gravity);
		this.messageTextView.setText(message);
	}

	public void setPositiveButton(DialogInterface.OnClickListener onClickListener) {
		this.setPositiveButton(LibraryConfig.CONFIRM_TEXT, onClickListener);
	}

	public void setPositiveButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
		this.showButtonLayout = true;
		this.postiveBtn.setText(text);
		this.postiveBtn.setOnClickListener(new ClickListener(postiveBtn, onClickListener));
		this.postiveBtn.setVisibility(View.VISIBLE);
	}

	public void setNegativeButton(DialogInterface.OnClickListener onClickListener) {
		this.setNegativeButton(LibraryConfig.CANCEL_TEXT, onClickListener);
	}

	public void setNegativeButton(CharSequence text, DialogInterface.OnClickListener onClickListener) {
		this.showButtonLayout = true;
		this.negativeBtn.setText(text);
		this.negativeBtn.setOnClickListener(new ClickListener(negativeBtn, onClickListener));
		this.negativeBtn.setVisibility(View.VISIBLE);
	}

	private class ClickListener implements OnClickListener {
		private Button button;
		private DialogInterface.OnClickListener onClickListener;

		public ClickListener(Button button, DialogInterface.OnClickListener onClickListener) {
			this.button = button;
			this.onClickListener = onClickListener;
		}

		@Override
		public void onClick(View v) {
			dismiss();
			if (onClickListener == null)
				return;
			if (button == postiveBtn) {
				onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
			} else {
				onClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
			}
		}
	}
	
	public void show() {
		dialog.setCancelable(cancelable);
		if (showTitleLayout) {
			this.titleLayout.setVisibility(View.VISIBLE);
		}
		if (showContentLayout) {
			this.contentLayout.setVisibility(View.VISIBLE);
		}
		if (showButtonLayout) {
			this.buttonLayout.setVisibility(View.VISIBLE);
		}
		dialog.show();
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
