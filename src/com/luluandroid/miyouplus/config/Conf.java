package com.luluandroid.miyouplus.config;

import com.luluandroid.miyouplus.util.FileManager;

import android.os.Build.VERSION;

public class Conf {

	public static final String MIBO_Serealizable_key = "miboSeaLb_key";
	public static final String MIBO_SHOW_keyString = "miboshowkey";
	public static final String APP_SDCARD_PATH= FileManager.getInstance().getSdDir().getAbsolutePath()+"/MiYou";
	public static final String APP_SDCARD_CACHE_PATH= FileManager.getInstance().getSdDir().getAbsolutePath()+"/MiYou/Cache";
	public static final String APP_SDCARD_ALBUM_PATH = FileManager.getInstance().getSdDir().getAbsolutePath()+"/MiYou/Album";
	public static final String Local_Album_Dir_Name = "Album";
	//appsd卡album目录名字
	public static final String SD_Album_Dir_Name = "Miyou";
	
	//sdk版本
	public static final int APILevel = Integer.parseInt(VERSION.SDK);
	
	//图片处理的一些参数
	public final static int TAKE_PICTURE = 0x100;
	public final static int CHOOSE_PICTURE = 0x101;
	public final static int CROP = 0x102;
	public final static int CROP_PICTURE = 0x103;
	
	//handler 处理的一些参数
	public final static int HANDLER_CREATE_TIEZI_CB = 0x201;
	
	public final static int limitReportCount = 10;
	
}
