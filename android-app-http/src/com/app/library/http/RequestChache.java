package com.app.library.http;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RequestChache {
	private static RequestChache INSTANCE = null;
	private RequestDBHelper requestDBHelper;

	public RequestChache(Context context) {
		this.requestDBHelper = new RequestDBHelper(context);
	}

	public static RequestChache getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new RequestChache(context);
		}
		return INSTANCE;
	}

	/**
	 * 更新数据，
	 * 
	 * @param item
	 */
	public void update(String url, long lastModified) {
		if (!find(url)) { // 创建
			add(url, lastModified);
		} else { // 有则更新
			SQLiteDatabase db = requestDBHelper.getWritableDatabase();
			db.execSQL("update request_cache set lastmodified=? where url=?",
					new Object[] { String.valueOf(lastModified), url });
		}
	}

	private void add(String url, long lastModified) {
		SQLiteDatabase db = requestDBHelper.getWritableDatabase();
		db.execSQL("insert into request_cache(url, lastmodified) values(?,?)",
				new Object[] { url, String.valueOf(lastModified) });
	}
	
	/**
	 * 取最近更新
	 * @param filename
	 * @return
	 */
	public long getLastModified(String url) {
		SQLiteDatabase db = requestDBHelper.getReadableDatabase();
		Cursor cursor = null;
		long ret = 0l;

		try {
			cursor = db.rawQuery("select * from request_cache where url=?", new String[] { url });
			if (cursor.moveToFirst()) {
				String last = cursor.getString(cursor.getColumnIndex("lastmodified"));
				ret = Long.valueOf(last);
			}
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return ret;
	}

	/**
	 * 查询存在
	 * 
	 * @param filename
	 * @return
	 */
	private boolean find(String url) {
		SQLiteDatabase db = requestDBHelper.getReadableDatabase();
		Cursor cursor = null;
		boolean flag = false;

		try {
			cursor = db.rawQuery("select * from request_cache where url=?", new String[] { url });
			if (cursor.moveToFirst()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return flag;
	}

	class RequestDBHelper extends SQLiteOpenHelper {
		private static final String DB_NAME = "requestCache.db";
		private static final int DB_VER = 1;
		private static final String TABLE_CREATE = "create table request_cache(url varchar(32) primary key,  lastmodified varchar(16))";

		public RequestDBHelper(Context context) {
			super(context, DB_NAME, null, DB_VER);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

		}

	}
}
