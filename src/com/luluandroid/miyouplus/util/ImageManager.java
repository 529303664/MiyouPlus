package com.luluandroid.miyouplus.util;

import java.io.File;

import com.luluandroid.miyouplus.config.Conf;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ImageManager {

	
	private final int CROP_PICTURE = 3;

	
	/**
	 * 获取手机屏幕分辨率宽度
	 * 
	 * @param ctx 上下文
	 * @return 手机屏幕分辨率宽度
	 */
	public static int getPhoneWidth(Context ctx){
		WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}
	
	/**
	 * 获取手机屏幕分辨率长度
	 * @param ctx
	 * @return 手机屏幕分辨率长度
	 */
	public static int getPhoneHeight(Context ctx){
		WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getHeight();
	}
	
	/**
	 * 调整图片模糊度进行
	 * @param bkg 需要处理的图片bitmap
	 * @param view 需要显示处理后的图片的组件
	 * @param context 上下文
	 * @param isblur 确定是否要缩小范围
	 */
	public static Bitmap blur(Bitmap bkg, View view,Context context,boolean isblur) {
	    float scaleFactor = 1;
	    float radius = 20;
	    if (isblur) {
	        scaleFactor = 8;
	        radius = 2;
	    }
	 
	    Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
	            (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(overlay);
	    canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
	    canvas.scale(1 / scaleFactor, 1 / scaleFactor);
	    Paint paint = new Paint();
	    paint.setFlags(Paint.FILTER_BITMAP_FLAG);
	    paint.setAntiAlias(true);
	    System.out.println("bkg:"+bkg);
	    canvas.drawBitmap(bkg, 0, 0, paint);
	 
	    overlay = FastBlur.doBlur(overlay, (int)radius, true);
//	    Drawable temp = new BitmapDrawable(context.getResources(), overlay);
//	    view.setBackgroundDrawable(temp);
	    
//	    return temp;
	    return overlay;
	    /*view.setBackground(new BitmapDrawable(context.getResources(), overlay));*/
	}
	
	public static Bitmap BrightnessChange(Bitmap srcBitmap,int progress){
		Bitmap bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);
		int brightness = progress-127;
		ColorMatrix mColorMatrix = new ColorMatrix();
		mColorMatrix.set(new float[]{1,0,0,0,brightness,0,1,
				0,0,brightness,//改变亮度
				0,0,1,0,brightness,0,0,0,1,0});
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
		
		Canvas canvas = new Canvas(bmp);
		//在canvas上绘制一个原来的bitmap。
		canvas.drawBitmap(srcBitmap, 0,0, paint);
		return bmp;
/*		view.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bmp));*/
	}
	
}
