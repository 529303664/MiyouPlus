package com.luluandroid.miyouplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	private String File_LOG_TAG = "MIYOU_FILE";
	private final File sdDir = Environment.getExternalStorageDirectory();//sd����·��
	private final File cacheDir = Environment.getDownloadCacheDirectory();//����Ŀ¼
	private final File dataDir =Environment.getDataDirectory();//data�ļ���Ŀ¼
	private final File rootDir = Environment.getRootDirectory();//Android��Ŀ¼
	private  File sdAppDir;//sd��App��Ŀ¼
	private  File sdAppCacheDir; // sd��AppCache��Ŀ¼
	private Context Applicationcontext;
	private static FileManager mFileManager=null;
	private FileManager(){
	}
	
	public static FileManager getInstance(){
		if(mFileManager==null)
			mFileManager = new FileManager();
		return mFileManager;
	}
	
//	�ڲ��洢
	public File getLocalPicDir(Context context,String albumName){
		return context.getDir(albumName, Context.MODE_PRIVATE);
	}
	
	/**
	 * @return the sdDir
	 */
	public File getSdDir() {
		return sdDir;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return Applicationcontext;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.Applicationcontext = context;
	}

	/**
	 * @return the sdAppDir
	 */
	public String getSdAppDirPath() {
		sdAppDir = Applicationcontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		return sdAppDir.getAbsolutePath();
	}

	/**
	 * @return the sdAppCacheDir
	 */
	public String getSdAppCacheDirPath() {
		sdAppCacheDir = Applicationcontext.getExternalCacheDir();
		return sdAppCacheDir.getAbsolutePath();
	}

	/**
	 * @return the cacheDir
	 */
	public File getCacheDir() {
		return cacheDir;
	}

	/**
	 * @return the dataDir
	 */
	public File getDataDir() {
		return dataDir;
	}

	/**
	 * @return the rootDir
	 */
	public File getRootDir() {
		return rootDir;
	}
	
	/**
	 * �õ���app�û���������ƬĿ¼
	 * @param albumName Ŀ¼��
	 * @return ����ָ����Ŀ¼������
	 */
	public File getAlbumStorageDir(String albumName){
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),albumName);
		if(!file.mkdirs()){
			Log.e(File_LOG_TAG, "��app�û���������ƬĿ¼ is not created");
		}
		return file;
	}
	
	/**
	 * �õ���app�û�˽�е���ƬĿ¼
	 * @param context ������
	 * @param albumName ָ����Ŀ¼��
	 * @return ����ָ����Ŀ¼������
	 */
	public File getAlbumStorageDir(Context context, String albumName){
		File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),albumName);
		if(!file.exists()){
			file.mkdirs();
			Log.e(File_LOG_TAG, "��app�û�˽�е���ƬĿ¼ is not created");
		}
		return file;
	}
	
	/**
	 * �õ���app��sd���洢����Ŀ¼
	 * @param context
	 * @param albumName
	 * @return
	 */
	public File getAlbumStorageCacheDir(Context context, String albumName){
		File file  = new File(context.getExternalCacheDir(),albumName);
		if(!file.mkdirs()){
			Log.e(File_LOG_TAG, "��app��sd���洢����Ŀ¼ is not created");
		}
		return file;
	}
	
	/**
	 * ɾ�����ڲ��洢���ļ�
	 * @param path Ŀ¼
	 * @param name �ļ���
	 * @return
	 */
	public boolean deleteFile(String path,String name){
		File deleteFile = new File(path,name);
		return deleteFile.delete();
	}

	
	/**
	 * ���sd���Ƿ��ʹ��
	 * @return
	 */
	public  boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	public boolean deleteFilesofSingleDir(String dirPath) {
		File folder = new File(dirPath);
			if(!folder.exists()){
				Log.i("file", "deleteFilesofSingleDir Ŀ¼�µ��ļ�ɾ��ʧ��");
				return false;
			}
			if(folder.isDirectory()){
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					if(files[i].isFile())
					files[i].delete();
				}
			}
			return true;
			
	}
	
	public boolean deleteDir(File folder){
		if(folder.isDirectory()){
			String[] filesName = folder.list();
			for (int i = 0; i < filesName.length; i++) {
					boolean success = deleteDir(new File(folder,filesName[i]));
					if(!success){
						return false;
					}
			}
		}
		return folder.delete();
	}
	
}
