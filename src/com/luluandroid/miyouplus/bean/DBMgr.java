package com.luluandroid.miyouplus.bean;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBMgr {
	private static DBMgr instance = null;
	private  Context context;
	private DatabaseHelper databaseHelper=null;
	private SQLiteDatabase db;
	
	
	public DBMgr(Context context) {
		super();
		this.context = context;
		databaseHelper = new DatabaseHelper(context);
		db = databaseHelper.getWritableDatabase();
//		db.execSQL("PRAGMA foreign_key=ON;");
	}
	
	public static DBMgr getInstance(Context context) {
		if(instance == null){
			instance = new DBMgr(context);
		}
		return instance;
	}

	/**
	 * 获取DB
	 * @return
	 */
	public synchronized SQLiteDatabase getDb() {
		if(this.databaseHelper == null) {
			Log.e("DATABASE", "databaseHelper 为空!!正在申请databasehelper!!");
			this.databaseHelper = new DatabaseHelper(context);
			if(this.databaseHelper != null){
				Log.e("DATABASE", "databaseHelper 申请成功!!");
			}
		}
		if(db == null){
			db = databaseHelper.getWritableDatabase();
		}
		return db;
	}
	
	/**
	 * 关闭Db
	 * @return
	 */
	public boolean closeDb(){
		if(db != null){
			db.close();
			return true;
		}else{
			Log.i("database", "数据库关闭失败：数据库已经关闭");
			return false;
		}
		
	}

	public boolean tableIsEmpty(String tablename) {
		Cursor c = db.rawQuery("SELECT * FROM " + tablename, null);
		if (c != null && c.getCount() > 0) {
			c.close();
			return true;
		} else {
			c.close();
		}
		return false;
	}
	
	/**
	 * 获取databasehelper
	 * @return
	 */
	public DatabaseHelper getDatabaseHelper() {
		if(this.databaseHelper == null){databaseHelper = new DatabaseHelper(context);}
		return databaseHelper;
	}

	/**
	 * 关闭databasehelper
	 * @return
	 */
	public boolean CloseDatabaseHelper(){
		if(this.databaseHelper != null){
			this.databaseHelper.close();
			return true;
		}else{
			Log.i("database", "数据库关闭失败：数据库Helper已经关闭");
			return false;
		}
	}
	
	public void closeDbAndHelper(){
		if(closeDb()){
			Log.i("database", "数据库关闭成功!!");
		}
		if(CloseDatabaseHelper()){
			Log.i("database", "数据库helper关闭成功!!");
		}
	}
	
	public void checkDbOpen(){
		if(!db.isOpen())
			db = getDb();
	}
	
	public boolean addTiezi(List<Mibos> miboList){
		checkDbOpen();
		db.beginTransaction();
		ContentValues cv = new ContentValues();
		for(Mibos mibo : miboList){
			try {
				cv.put(TieZiSchema.COLUMN_USER, mibo.getHeadUserName());
				cv.put(TieZiSchema.COLUMN_CONTENT, mibo.getContent());
				cv.put(TieZiSchema.COLUMN_FAVOR, mibo.getFavorCount().toString());
				cv.put(TieZiSchema.COLUMN__COMMENT_COUNT, String.valueOf(mibo.getComment().size()));
				cv.put(TieZiSchema.COLUMN_PARENT_ID, String.valueOf(mibo.getParentId()));
				cv.put(TieZiSchema.COLUMN_OPEN, String.valueOf(mibo.isOpentoAll()));
				cv.put(TieZiSchema.COLUMN_FRCMOL, String.valueOf(mibo.isFriendCommentOnly()));
				cv.put(TieZiSchema.COLUMN_PIC_NAME, mibo.getLocalPicName());
				cv.put(TieZiSchema.COLUMN_MESSAGE_TYPE, mibo.getType().name());
				if(mibo.getUpdatedAt() == null){
					cv.put(TieZiSchema.COLUMN_MESSAGE_TIME, mibo.getCreatedAt().toString());
				}else{
					cv.put(TieZiSchema.COLUMN_MESSAGE_TIME, mibo.getUpdatedAt().toString());
				}
				db.insert(TieZiSchema.TABLE_NAME, null, cv);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("database", e.toString());
				return false;
			}finally{
				cv.clear();
			}
			db.endTransaction();
		}
		return true;
	}
	
}
