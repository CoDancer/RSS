package com.rss.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChannelDataHelper {

	private static String DB_NAME = "RssChannel.db";
	private static int DB_VERSION = 1;
	private static SQLiteDatabase db;
	private static SqliteHelper dbHelper;
	
	public ChannelDataHelper(Context context){
		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public void Close()	{
		db.close();
		dbHelper.close();
	}
	
	public List<String> getChannelList() {
		List<String >ChannelList = new ArrayList<String>();
		
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null, null, ID+" DESC");
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			
			String channel  = cursor.getString(1);
			
			ChannelList.add(channel);
			
			cursor.moveToNext();
		}
		cursor.close();
		return ChannelList;
	}
	
	public String getUrlByChannel(String name) {
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, NAME+"=?", new String[]{name}, null, null, null);
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast() && (cursor.getString(1) != null))
			return cursor.getString(2);
		
		return null;
	}
	
	public Long SaveChannelInfo(String name, String url) {
		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(URL, url);
		Long id = db.insert(SqliteHelper.TB_NAME, null, values);
		
		return id;
	}
	
	public int DelChannelInfo(String name) {
		int id = db.delete(SqliteHelper.TB_NAME, NAME + "+'" +name + "'",null);
		return id;
	}
	
	static String ID = "id";
	static String NAME="name";
	static String URL="url";
	
	class SqliteHelper extends SQLiteOpenHelper {
		public static final String TB_NAME = "channle";
		public SqliteHelper (Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE IF NOT EXISTS " + 
			TB_NAME + "(" +
			ID + " integer primary key," + 
			NAME + " varchar, " + 
			URL + " varchar" +
			")"
			);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+ TB_NAME);
			onCreate(db);
		}
	}
	
}
