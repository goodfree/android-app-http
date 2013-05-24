package com.app.library.common.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.library.common.BaseDialog;
import com.app.library.common.ResourceUtils;

class SpinnerDialog extends BaseDialog {
	private ListView listView;

	public SpinnerDialog(Context context) {
		super(context);
		this.initView();
	}

	private void initView() {
		this.listView = (ListView) LayoutInflater.from(getContext()).inflate(
				ResourceUtils.getLayoutId("app_dialog_spinner_layout"), null);
		
		super.setContentLayout(listView);
	}

	/**
	 * 设置数据及监听
	 * 
	 * @param list
	 * @param listener
	 * @return
	 */
	public SpinnerDialog init(String data[], final SpinnerListener listener) {
		List<String> list = new ArrayList<String>();
		if (data != null && data.length > 0) {
			list = Arrays.asList(data);
		}
		return init(list, listener);
	}

	/**
	 * 设置数据及监听
	 * 
	 * @param list
	 * @param listener
	 * @return
	 */
	public SpinnerDialog init(List<String> list, final SpinnerListener listener) {
		initListView(list, listener);
		return this;
	}

	private void initListView(List<String> list, final SpinnerListener listener) {
		setAdapter(list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dismiss();
				String selectedItem = (String) parent.getAdapter().getItem(position);
				listener.onSelected(position, selectedItem);
			}
		});
	}

	private void setAdapter(List<String> list) {
		ListAdapter adapter = new SpinnerListAdapter(getContext(), list);
		listView.setAdapter(adapter);
	}

	private class SpinnerListAdapter extends ArrayAdapter<String> {
		public SpinnerListAdapter(Context context, List<String> list) {
			super(context, 0, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(
					ResourceUtils.getLayoutId("app_dialog_spinner_item"), null);
			String item = getItem(position);
			textView.setText(item);
			return textView;
		}
	}
}