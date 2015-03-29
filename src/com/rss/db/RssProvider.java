package com.rss.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
//Rss数据内容管理
public class RssProvider extends ContentProvider {
	//rss内容数据库文件名
	private static final String RSS_DATABASE = "rss.db";
	//数据库表名
	private static final String RSS_TABLE = "rss_item";
	private static final int RSS_DATABASE_VERSION = 1;	
	private static final String TAG = "RssProvider";	
	//URI
	public static final Uri RSS_URI = Uri.parse("content://myrss/rss");
	
	
	public static final String RSS_ID = "_id";
	public static final String RSS_TITLE = "title";
	public static final String RSS_DESCRIPTION ="descriptionn";
	public static final String RSS_LINK = "link";
	public static final String RSS_PUBDATE = "pubDate";
	
	
	public static final int TITLE_INDEX = 1;
	public static final int DESCIPTION_INDEX = 2;
	public static final int LINK_INDEX = 3;
	public static final int PUBDATE_INDEX = 4;
	
	private static final String RSS_SQL = "CREATE　TABLE" + RSS_TABLE + "("
				+ RSS_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ RSS_TITLE + "TEXT"
				+ RSS_DESCRIPTION + "TEXT"
				+ RSS_LINK + "TEXT"
				+ RSS_PUBDATE + "DATE);";
				
	SQLiteDatabase rssDb = null;
	
	private static final int RSS = 1;
	private static final int RSSID = 2;
	
	private static UriMatcher uriMatcher;
	
	static {
		uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
		uriMatcher.addURI("myrss", "rss", RSS);
		uriMatcher.addURI("myrss", "rss/#", RSSID);
	}
	
	private class RssDateHelper extends SQLiteOpenHelper {

		public RssDateHelper(Context context) {
			super(context, RSS_DATABASE, null, RSS_DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(RSS_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + RSS_TABLE);
			onCreate(db);
		}		
		
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case RSS:
			return "vnd.android.cursor.dir/com.rss.activity";
		case RSSID:
			return "vnd.android.cursor.item/com.rss.activity";

		default:
			throw new IllegalArgumentException("Unsupport URI:"+ uri);
		}
	}
	@Override
	public Uri insert(Uri _uri, ContentValues values) {
		// TODO Auto-generated method stub
		Uri uri= null;
		long rowId = rssDb.insert(RSS_TABLE, null, values);
		
		if (rowId > 0) {
			uri = ContentUris.withAppendedId(RSS_URI, rowId);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return uri;
	}
	@Override
	public boolean onCreate() {
		
		Context context = getContext();
		RssDateHelper rssDbHelper = new RssDateHelper(context);
		
		rssDb = rssDbHelper.getWritableDatabase();
		// TODO Auto-generated method stub
		return (rssDb == null) ? false : true;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(RSS_TABLE);
		
		switch (uriMatcher.match(uri)) {
		case RSSID:
			sqb.appendWhere(RSS_ID + "=" + uri.getPathSegments().get(1));
		break;
		default:
			break;			
		}
		String orderBy = null;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = RSS_PUBDATE;
		}
		
		Cursor c = sqb.query(rssDb, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		int count;
		switch (uriMatcher.match(uri)) {
		case RSS:
			count = rssDb.update(RSS_TABLE, values, selection, selectionArgs);
		case RSSID:
			String segment = uri.getPathSegments().get(1);
		count = rssDb.update(RSS_TABLE, values, RSS_ID + "=" + segment
				+ (!TextUtils.isEmpty(selection)? "AND("
				+ selection + '(' : ""),selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unkown URI" +  uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}