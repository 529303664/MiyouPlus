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
	 * ��ȡ�ֻ���Ļ�ֱ��ʿ��
	 * 
	 * @param ctx ������
	 * @return �ֻ���Ļ�ֱ��ʿ��
	 */
	public static int getPhoneWidth(Context ctx){
		WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}
	
	/**
	 * ��ȡ�ֻ���Ļ�ֱ��ʳ���
	 * @param ctx
	 * @return �ֻ���Ļ�ֱ��ʳ���
	 */
	public static int getPhoneHeight(Context ctx){
		WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getHeight();
	}
	
	/**
	 * ����ͼƬģ���Ƚ���
	 * @param bkg ��Ҫ�����ͼƬbitmap
	 * @param view ��Ҫ��ʾ������ͼƬ�����
	 * @param context ������
	 * @param isblur ȷ���Ƿ�Ҫ��С��Χ
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
				0,0,brightness,//�ı�����
				0,0,1,0,brightness,0,0,0,1,0});
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
		
		Canvas canvas = new Canvas(bmp);
		//��canvas�ϻ���һ��ԭ����bitmap��
		canvas.drawBitmap(srcBitmap, 0,0, paint);
		return bmp;
/*		view.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bmp));*/
	}
	
}
