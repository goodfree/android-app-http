package com.app.library.common.manager;

import com.app.library.common.ResourceUtils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 顶部标题及Button设置工具
 * 
 * @author savant-pan
 * 
 */
public class AppTitleManager {
	private TextView titleView;
	private LinearLayout leftLayout;
	private LinearLayout rightLayout;
	private boolean showLeftLayout = false;
	private Activity activity;

	/**
	 * 仅设置标题-静态方法
	 * 
	 * @param activity
	 *            活动
	 * @param title
	 *            标题文字
	 */
	public static void setTitle(Activity activity, String title) {
		new AppTitleManager(activity).setTitle(title).show();
	}

	/**
	 * 仅设置标题-静态方法
	 * 
	 * @param activity
	 *            活动
	 * @param titleRes
	 *            标题文字资源ID
	 */
	public static void setTitle(Activity activity, int titleRes) {
		new AppTitleManager(activity).setTitle(titleRes).show();
	}

	/**
	 * 初始化构建
	 */
	protected AppTitleManager(Activity activity) {
		this.activity = activity;
		this.leftLayout = (LinearLayout) activity.findViewById(ResourceUtils.getId("app_left_view"));
		this.rightLayout = (LinearLayout) activity.findViewById(ResourceUtils.getId("app_right_view"));
		this.titleView = (TextView) activity.findViewById(ResourceUtils.getId("app_title_text"));

		// set gone as default
		this.titleView.setVisibility(View.GONE);
		this.leftLayout.setVisibility(View.GONE);
		this.rightLayout.setVisibility(View.GONE);

	}

	/**
	 * builder
	 * 
	 * @param activity
	 * @return
	 */
	public static AppTitleManager create(Activity activity) {
		return new AppTitleManager(activity);
	}

	/**
	 * 显示左边后退按键,关闭页面
	 * 
	 * @param leftView
	 * @return
	 */
	public AppTitleManager setLeftButton() {
		setLeftButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					activity.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return this;
	}

	/**
	 * 显示左边后退按键,自写监听
	 * 
	 * @param leftView
	 * @return
	 */
	public AppTitleManager setLeftButton(OnClickListener li) {
		this.showLeftLayout = true;
		this.leftLayout.setVisibility(View.VISIBLE);
		this.leftLayout.setOnClickListener(li);
		return this;
	}

	/**
	 * 添加自定义右上方布局,并自行处理事件
	 * 
	 * @param rightView
	 * @return
	 */
	public AppTitleManager setRightView(View rightView) {
		if (rightView != null) {
			this.rightLayout.addView(rightView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			this.rightLayout.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/**
	 * 显示标题
	 */
	public AppTitleManager setTitle(CharSequence title) {
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(title);
		return this;
	}

	/**
	 * 显示标题
	 */
	public AppTitleManager setTitle(int titleRes) {
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(titleRes);
		return this;
	}

	/**
	 * 显示
	 */
	public void show() {
		if (showLeftLayout) {
			this.leftLayout.setVisibility(View.VISIBLE);
		}
		this.rightLayout.setVisibility(View.VISIBLE);
		this.titleView.setVisibility(View.VISIBLE);
	}

}
