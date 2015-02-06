package com.luluandroid.miyouplus.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TEXT_TYPE = " TEXT";
	private static final String VARCHAR_TYPE = " VARCHAR(255)";
	private static final String COMMA_SEP = ",";
	public static int DATABASEVERSION = 1;
	public static String DATABASENAME = "miyouplus.db";
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.DATABASEVERSION = version;
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper(Context context){
		super(context,DATABASENAME,null,DATABASEVERSION);
	}
	
	/**
	 * 接口设计模式
	 * @author Administrator
	 *
	 */
	public static interface TableCreateInterface {
		public void onCreate(SQLiteDatabase db);

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		TieZiSchema.getIntance().onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion > newVersion)
			return;
		TieZiSchema.getIntance().onUpgrade(db, oldVersion, newVersion);
	}

}
