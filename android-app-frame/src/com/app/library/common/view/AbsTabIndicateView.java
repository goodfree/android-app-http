package com.app.library.common.view;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.app.library.common.ResourceUtils;

/**
 * 切换标签控件基类
 * 
 * @author savant-pan
 * 
 */
public abstract class AbsTabIndicateView extends LinearLayout implements View.OnTouchListener {
	private LinearLayout tabHost;
	private List<View> viewList = new Vector<View>();

	private int viewCount = 0;
	private int currentIndex = 0;
	private OnTabChangeListener onTabChangeListener;
	private boolean notify = false;

	/**
	 * 标签切换监听接口
	 */
	public interface OnTabChangeListener {
		public void onTabChanged(int position);
	}

	public AbsTabIndicateView(Context context) {
		super(context);
		this.init();
	}

	public AbsTabIndicateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(ResourceUtils.getLayoutId("app_tab_indicate_abs_layout"), this);
		this.tabHost = (LinearLayout) findViewById(ResourceUtils.getId("tab_host"));
	}

	public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
		if (onTabChangeListener == null) {
			throw new NullPointerException();
		}
		this.onTabChangeListener = onTabChangeListener;
	}

	protected void setupTabLayout(List<View> list) {
		if (list == null || list.size() == 0) {
			throw new NullPointerException();
		}

		this.viewCount = list.size();
		final LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams linelp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		linelp.gravity = Gravity.CENTER_VERTICAL;

		lp.weight = 1f / viewCount;
		for (int index = 0; index < viewCount; index++) {
			final View view = list.get(index);
			this.tabHost.addView(view, lp);
			if (index != viewCount - 1) {
				final View lineView = LayoutInflater.from(getContext()).inflate(
						ResourceUtils.getLayoutId("app_tab_indicate_abs_line"), null);
				this.tabHost.addView(lineView, linelp);
			}
			view.setOnTouchListener(this);
			this.viewList.add(view);
		}

		this.refrash();
	}

	/**
	 * 设置当前显示TAB
	 * 
	 * @param position
	 *            前显示TAB位置
	 */
	public void setCurrentTab(int position) {
		this.setCurrentTab(position, true);
	}

	/**
	 * 设置当前显示TAB
	 * 
	 * @param position
	 *            前显示TAB位置
	 * @param notify
	 *            是否通知接口返回位置
	 */
	public void setCurrentTab(int position, boolean notify) {
		this.notify = notify;
		this.currentIndex = position;
		this.refrash();
	}

	/**
	 * 事件处理
	 */
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		final View lastView = this.viewList.get(this.currentIndex);
		if (lastView != view) {
			this.setCurrentIndex(view);
			this.refrash();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 设置当前 currentIndex值
	 * 
	 * @param touchView
	 */
	private void setCurrentIndex(View touchView) {
		for (int index = 0; index < this.viewCount; index++) {// 切换刷新界面
			final View view = this.viewList.get(index);
			if (touchView == view) {
				this.currentIndex = index;
			}
		}
	}

	/**
	 * 更新条目
	 */
	private void refrash() {
		this.handler.sendEmptyMessage(0);
	}

	/**
	 * 界面刷新Handler
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			for (int index = 0; index < viewCount; index++) {// 切换刷新界面
				final View view = viewList.get(index);
				if (index == currentIndex) {
					refreshItemView(view, true);
					if (onTabChangeListener != null) {
						if (!notify) {// use only once
							notify = true;
						} else {
							onTabChangeListener.onTabChanged(index);
						}
					}
				} else {
					refreshItemView(view, false);
				}
			}
		}
	};

	/**
	 * 更新选中项，重写则自定义
	 * 
	 * @param view
	 * @param isLast
	 */
	protected abstract void refreshItemView(View view, boolean isCurrent);

}
