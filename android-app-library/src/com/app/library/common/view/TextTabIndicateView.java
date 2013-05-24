package com.app.library.common.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.library.common.ResourceUtils;

/**
 * 文字标签控件
 * 
 * @author savant-pan
 * 
 */
public class TextTabIndicateView extends AbsTabIndicateView implements View.OnTouchListener {

	public TextTabIndicateView(Context context) {
		super(context);
	}

	public TextTabIndicateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置文字标签数据
	 * 
	 * @param titleArray
	 *            文字标签数组
	 */
	public void setupLayout(String titleArray[]) {
		if (titleArray == null || titleArray.length == 0) {
			throw new NullPointerException();
		}

		this.setupLayout(Arrays.asList(titleArray));
	}

	/**
	 * 设置文字标签数据
	 * 
	 * @param titleList
	 *            文字标签列表
	 */
	public void setupLayout(List<String> textList) {
		if (textList == null || textList.size() == 0) {
			throw new NullPointerException();
		}

		final int len = textList.size();

		final List<View> list = new ArrayList<View>();
		for (int index = 0; index < len; index++) {
			final View view = LayoutInflater.from(getContext()).inflate(
					ResourceUtils.getLayoutId("app_tab_indicate_text_item"), null);
			final TextView textView = (TextView) view.findViewById(ResourceUtils.getId("indicator_text"));
			textView.setText(textList.get(index));
			list.add(view);
		}

		super.setupTabLayout(list);
	}

	@Override
	protected void refreshItemView(View view, boolean isCurrent) {
		final View line = view.findViewById(ResourceUtils.getId("bottom_line"));
		final TextView textView = (TextView) view.findViewById(ResourceUtils.getId("indicator_text"));
		final Resources resources = getContext().getResources();
		if (isCurrent) {
			textView.setTextColor(resources.getColor(ResourceUtils.getColorId("frame")));
			line.setVisibility(View.VISIBLE);
		} else {
			textView.setTextColor(resources.getColor(ResourceUtils.getColorId("title")));
			line.setVisibility(View.INVISIBLE);
		}
	}

}
