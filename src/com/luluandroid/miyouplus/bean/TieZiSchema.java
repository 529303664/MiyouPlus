package com.luluandroid.miyouplus.bean;

import com.luluandroid.miyouplus.bean.DatabaseHelper.TableCreateInterface;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * 帖子数据表
 * @author Administrator
 *
 */
public  class TieZiSchema implements  BaseColumns,TableCreateInterface{
	private   String TEXT_TYPE = " TEXT";
	private   String COMMA_SEP = ",";
	public static final String TABLE_NAME="mibo";
	public static final String COLUMN_BMOB_ID = "bmobid";
	public static final String COLUMN_USER="user";
	public static final String COLUMN_USER_ID="userid";
	public static final String COLUMN_CONTENT="content";
	public static final String COLUMN_FAVOR="favors";
	public static final String COLUMN_COMCOUNT="comentcount";
	public static final String COLUMN_PARENT_ID="parentid";
	public static final String COLUMN_OPEN="open";
	public static final String COLUMN_FRCMOL="friendcomentonly";
	public static final String COLUMN_PIC_NAME="picname";
	public static final String COLUMN_PIC_RES_ID="picresid";
	public static final String COLUMN_MESSAGE_TYPE="messagetype";
	public static final String COLUMN_MESSAGE_TIME="messagetime";
	
	private TieZiSchema(){}
	
	private static TieZiSchema mTieZiSchema = new TieZiSchema();
	
	public static TieZiSchema getIntance(){
		return mTieZiSchema;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String tablesql = "CREATE TABLE IF NOT EXISTS"
				+" "+ TieZiSchema.TABLE_NAME + " ("
				+ TieZiSchema._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ TieZiSchema.COLUMN_BMOB_ID + TEXT_TYPE + COMMA_SEP
				+ TieZiSchema.COLUMN_USER + TEXT_TYPE + COMMA_SEP
				+ TieZiSchema.COLUMN_USER_ID + TEXT_TYPE + COMMA_SEP
				+ TieZiSchema.COLUMN_CONTENT + TEXT_TYPE + COMMA_SEP
				+ TieZiSchema.COLUMN_FAVOR + TEXT_TYPE + COMMA_SEP
				+ TieZiSchema.COLUMN_COMCOUNT + TEXT_TYPE + COMMA_SEP
				+ TieZiSchema.COLUMN_PARENT_ID+TEXT_TYPE+ COMMA_SEP
				+ TieZiSchema.COLUMN_OPEN+TEXT_TYPE+ COMMA_SEP
				+ TieZiSchema.COLUMN_FRCMOL+TEXT_TYPE+ COMMA_SEP
				+ TieZiSchema.COLUMN_PIC_NAME+TEXT_TYPE+ COMMA_SEP
				+ TieZiSchema.COLUMN_PIC_RES_ID+TEXT_TYPE+ COMMA_SEP
				+ TieZiSchema.COLUMN_MESSAGE_TYPE+TEXT_TYPE+ COMMA_SEP
				+ TieZiSchema.COLUMN_MESSAGE_TIME+TEXT_TYPE
				+" )";
		db.execSQL(tablesql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion<newVersion){
			String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(sql);
			this.onCreate(db);
		}
	}

}
