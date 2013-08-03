package com.app.library.http;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * RequestChacheManager for "GET" method if isCahce
 * 
 * @author savant-pan
 * 
 */
public class RequestChacheManager {
	private static RequestChacheManager INSTANCE = null;
	private RequestDBHelper requestDBHelper = null;

	private static final String DB_NAME = "requestCache.db";
	private static final int DB_VER = 1;
	private static final String TABLE_CREATE = "create table request_cache(url varchar(32) primary key,  lastmodified varchar(16))";

	/**
	 * RequestDBHelper
	 */
	private class RequestDBHelper extends SQLiteOpenHelper {
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

	public RequestChacheManager(Context context) {
		this.requestDBHelper = new RequestDBHelper(context);
	}

	public static RequestChacheManager getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new RequestChacheManager(context);
		}
		return INSTANCE;
	}

	/**
	 * update record: add or update
	 * 
	 * @param item
	 */
	public void update(String url, long lastModified) {
		SQLiteDatabase db = requestDBHelper.getWritableDatabase();
		if (!find(url)) { // add if not exist
			db.execSQL("insert into request_cache(url, lastmodified) values(?,?)",
					new Object[] { url, String.valueOf(lastModified) });
		} else { // update is exist
			db.execSQL("update request_cache set lastmodified=? where url=?",
					new Object[] { String.valueOf(lastModified), url });
		}
	}

	/**
	 * get lastmotified value by url
	 * 
	 * @param filename
	 * @return
	 */
	public long getLastModified(String url) {
		SQLiteDatabase db = requestDBHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			long ret = 0l;
			cursor = db.rawQuery("select * from request_cache where url=?", new String[] { url });
			if (cursor.moveToFirst()) {
				final String last = cursor.getString(cursor.getColumnIndex("lastmodified"));
				ret = Long.valueOf(last);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * check exists of url
	 * 
	 * @param url
	 * @return
	 */
	private boolean find(String url) {
		SQLiteDatabase db = requestDBHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			boolean flag = false;
			cursor = db.rawQuery("select * from request_cache where url=?", new String[] { url });
			if (cursor.moveToFirst()) {
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * delete all records
	 */
	public void deletAll() {
		List<String> all = getUrls();
		for (String url : all) {
			SQLiteDatabase database = requestDBHelper.getWritableDatabase();
			database.execSQL("delete from request_cache where url=?", new Object[] { url });
		}
	}

	/**
	 * get all urls in database
	 * 
	 * @return
	 */
	private List<String> getUrls() {
		List<String> ret = new ArrayList<String>();
		SQLiteDatabase db = requestDBHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from request_cache", null);
			if (cursor.moveToFirst()) {
				do {
					final String url = cursor.getString(0);
					ret.add(url);
				} while (cursor.moveToNext());
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return ret;
	}

}
