package com.luluandroid.miyouplus.bean;

import java.util.ArrayList;
import java.util.List;

import com.luluandroid.miyouplus.bean.Mibos.MessageType;
import com.luluandroid.miyouplus.util.TimeUtil;

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
		Log.i("DATABASE", "重新申请数据库连接");
		if(this.databaseHelper == null) {
			Log.e("DATABASE", "databaseHelper 为空!!正在申请databasehelper!!");
			this.databaseHelper = new DatabaseHelper(context);
			if(this.databaseHelper != null){
				Log.e("DATABASE", "databaseHelper 申请成功!!");
			}
		}
		if(db == null){
			db = databaseHelper.getWritableDatabase();
			Log.e("DATABASE", "databaseHelper.getWritableDatabase()  执行成功!!");
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

	public boolean tableIsNotEmpty(String tablename) {
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
		if(!db.isOpen()||db == null)
			db = getDb();
	}
	
	public boolean addTiezi(List<Mibos> miboList){
		checkDbOpen();
		db.beginTransaction();
		ContentValues cv = new ContentValues();
		for(Mibos mibo : miboList){
			try {
				cv.put(TieZiSchema.COLUMN_BMOB_ID, mibo.getObjectId());
				cv.put(TieZiSchema.COLUMN_USER, mibo.getHeadUserName());
				cv.put(TieZiSchema.COLUMN_CONTENT, mibo.getContent());
				cv.put(TieZiSchema.COLUMN_FAVOR, mibo.getFavorCount());
				cv.put(TieZiSchema.COLUMN_COMCOUNT, mibo.getCommentCount());
				cv.put(TieZiSchema.COLUMN_PARENT_ID, mibo.getParentId());
				cv.put(TieZiSchema.COLUMN_OPEN, mibo.isOpentoAll());
				cv.put(TieZiSchema.COLUMN_FRCMOL, mibo.isCommentOk());
				cv.put(TieZiSchema.COLUMN_PIC_NAME, mibo.getLocalPicName());
				cv.put(TieZiSchema.COLUMN_PIC_RES_ID, mibo.getPicResourceId());
				cv.put(TieZiSchema.COLUMN_MESSAGE_TYPE, mibo.getType().name());
				if(mibo.getUpdatedAt() == null){
					cv.put(TieZiSchema.COLUMN_MESSAGE_TIME, TimeUtil.stringToLong(mibo.getCreatedAt(), TimeUtil.FORMAT_DATE_TIME2));
				}else{
					cv.put(TieZiSchema.COLUMN_MESSAGE_TIME, TimeUtil.stringToLong(mibo.getUpdatedAt(), TimeUtil.FORMAT_DATE_TIME2));
				}
				db.insert(TieZiSchema.TABLE_NAME, null, cv);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("database", e.toString());
				db.endTransaction();
				return false;
			}finally{
				cv.clear();
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
//		db.close();
		return true;
	}
	
	public boolean updateTiezi(List<Mibos> miboList){
		boolean flag = false;
		flag = tableIsNotEmpty(TieZiSchema.TABLE_NAME);
		if(!flag)return flag;
			checkDbOpen();
			db.beginTransaction();
			ContentValues cv = new ContentValues();
			for(Mibos mibo : miboList){
				try {
					cv.put(TieZiSchema.COLUMN_BMOB_ID, mibo.getObjectId());
					cv.put(TieZiSchema.COLUMN_USER, mibo.getHeadUserName());
					cv.put(TieZiSchema.COLUMN_USER_ID, mibo.getFromUserId());
					cv.put(TieZiSchema.COLUMN_CONTENT, mibo.getContent());
					cv.put(TieZiSchema.COLUMN_FAVOR, mibo.getFavorCount());
					cv.put(TieZiSchema.COLUMN_COMCOUNT, mibo.getCommentCount());
					cv.put(TieZiSchema.COLUMN_PARENT_ID, mibo.getParentId());
					cv.put(TieZiSchema.COLUMN_OPEN, mibo.isOpentoAll());
					cv.put(TieZiSchema.COLUMN_FRCMOL, mibo.isCommentOk());
					cv.put(TieZiSchema.COLUMN_PIC_NAME, mibo.getLocalPicName());
					cv.put(TieZiSchema.COLUMN_PIC_RES_ID, mibo.getPicResourceId());
					cv.put(TieZiSchema.COLUMN_MESSAGE_TYPE, mibo.getType().name());
					if(mibo.getUpdatedAt() == null){
						cv.put(TieZiSchema.COLUMN_MESSAGE_TIME, TimeUtil.stringToLong(mibo.getCreatedAt(), TimeUtil.FORMAT_DATE_TIME2));
					}else{
						cv.put(TieZiSchema.COLUMN_MESSAGE_TIME, TimeUtil.stringToLong(mibo.getUpdatedAt(), TimeUtil.FORMAT_DATE_TIME2));
					}
					db.update(TieZiSchema.TABLE_NAME, cv, TieZiSchema.COLUMN_BMOB_ID+" = ?", new String[]{mibo.getObjectId()});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("database", e.toString());
					db.endTransaction();
					return false;
				}finally{
					cv.clear();
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		return flag;
	}
	
	public boolean deleteTiezi(List<Mibos>miboList){
		if(!tableIsNotEmpty(TieZiSchema.TABLE_NAME))
			return false;
		checkDbOpen();
		db.beginTransaction();
		for(Mibos mibo : miboList){
			db.delete(TieZiSchema.TABLE_NAME, TieZiSchema.COLUMN_BMOB_ID+" = ?",
					new String[]{mibo.getObjectId()});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return true;
	}
	
	public boolean deleteAllTiezi(){
		if(!tableIsNotEmpty(TieZiSchema.TABLE_NAME))
			return false;
		checkDbOpen();
		String sql = "DELETE FROM "+TieZiSchema.TABLE_NAME+";";
		db.execSQL(sql);
		return true;
	}

	/**
	 * @param parentId parentId 当为-1时，代表查询父贴，否则查询为某parentId下的评论
	 * @return
	 */
	public List<Mibos>queryRelatedTiezi(String parentId){
		Cursor c = queryTheCursorRelatedTiezi(parentId);
		
		if(c == null || c.getCount()<=0){
			return null;
		}
		List<Mibos> miboList = new ArrayList<Mibos>();
		while(c.moveToNext()){
			Mibos mibo = new Mibos(c.getString(c.getColumnIndex(TieZiSchema.COLUMN_USER)),
					c.getString(c.getColumnIndex(TieZiSchema.COLUMN_CONTENT)),
					c.getInt(c.getColumnIndex(TieZiSchema.COLUMN_FAVOR)),c.getString(c.getColumnIndex(TieZiSchema.COLUMN_USER_ID)));
			mibo.setObjectId(c.getString(c.getColumnIndex(TieZiSchema.COLUMN_BMOB_ID)));
			mibo.setParentId(c.getInt(c.getColumnIndex(TieZiSchema.COLUMN_PARENT_ID)));
			if(c.getInt(c.getColumnIndex(TieZiSchema.COLUMN_OPEN))==1){
				mibo.setOpentoAll(true);
			}else{
				mibo.setOpentoAll(false);
			}
			if(c.getInt(c.getColumnIndex(TieZiSchema.COLUMN_FRCMOL))==1){
				mibo.setCommentOk(true);
			}else{
				mibo.setCommentOk(false);
			}
			mibo.setLocalPicName(c.getString(c.getColumnIndex(TieZiSchema.COLUMN_PIC_NAME)));
			mibo.setPicResourceId(c.getInt(c.getColumnIndex(TieZiSchema.COLUMN_PIC_RES_ID)));
			/*if(c.getString(c.getColumnIndex(TieZiSchema.COLUMN_MESSAGE_TYPE)).equals("TEXT")){
				mibo.setType(MessageType.TEXT);
			}else{
				mibo.setType(MessageType.TEXTANDPICTURE);
			}*/
			mibo.setTableName(TieZiSchema.TABLE_NAME);
			miboList.add(mibo);
		}
		c.close();
		return miboList;
	}
	
	
	/**
	 * @param parentId 当为-2时，表示查询全部;当为-1时，代表查询父贴;否则查询为某parentId下的评论
	 * @return
	 */
	public Cursor queryTheCursorRelatedTiezi(String parentId){
		Cursor c;
		if(parentId.equals("-2")){
			c = db.rawQuery("select * from "+TieZiSchema.TABLE_NAME, null);
		}
		c = db.rawQuery("select * from "+TieZiSchema.TABLE_NAME+
				" where "+TieZiSchema.COLUMN_PARENT_ID+" = ?"+" order by "+TieZiSchema.COLUMN_MESSAGE_TIME, new String[]{parentId});
		return c;
	}
	
}
