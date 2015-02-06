package com.luluandroid.miyouplus.util;

import java.io.File;

import com.luluandroid.miyouplus.config.Conf;

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
	 * 调用系统裁剪图片方法
	 * 
	 * @param uri
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 */
	public static void cropImage(Context mContext, Uri uri, int outputX,
			int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		((FragmentActivity)mContext).startActivityForResult(intent, requestCode);
	}

	public static void pictureCrop(final Context context,final int isTake) {
		
		AlertDialog.Builder picturebuilder = new AlertDialog.Builder(context);
		picturebuilder.setTitle("图片是否需要裁剪?");
		picturebuilder.setNegativeButton("取消", null);
		picturebuilder.setItems(new String[] { "裁剪", "不了" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0: // 裁剪
							showPicturePicker(context,isTake,true);
							break;
						case 1: // 取消裁剪
							showPicturePicker(context,isTake,false);
							break;
						default:
							break;
						}
					}
				});
		picturebuilder.create().show();
	}

	public static void showPicturePicker(Context context, int isTake, boolean isCrop) {
		int REQUEST_CODE;
		System.out.println("响应了showPicturePicker");
		switch (isTake) {
		case Conf.TAKE_PICTURE:
			Uri imageuri = null;
			String fileName = null;
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			if (isCrop) {
				REQUEST_CODE = Conf.CROP;

				// 删除上一次截图的临时文件
				SharedPreferences mSharedPreferences = context
						.getSharedPreferences("tempImage", Context.MODE_PRIVATE);
				ImageTools.deletePhotoAtPathAndName(Conf.APP_SDCARD_CACHE_PATH,
						mSharedPreferences.getString("tempName", ""));
				// 保存本次截图临时文件名字
				fileName = String.valueOf(System.currentTimeMillis())+".jpg";
				Editor editor = mSharedPreferences.edit();
				editor.putString("tempName", fileName);
				editor.apply();
			}else{
				REQUEST_CODE = Conf.TAKE_PICTURE;
				fileName="image.jpg";
			}
			imageuri = Uri.fromFile(new File(Conf.APP_SDCARD_CACHE_PATH,fileName));
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
			//这句话测试看行不行
			((FragmentActivity)context).startActivityForResult(openCameraIntent, REQUEST_CODE);
			break;

		case Conf.CHOOSE_PICTURE://从相册中选择图片
			Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
			if(isCrop){
				REQUEST_CODE = Conf.CROP;
			}else{
				REQUEST_CODE = Conf.CHOOSE_PICTURE;
			}
			openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			((FragmentActivity)context).startActivityForResult(openAlbumIntent, REQUEST_CODE);
			break;
		default:
			break;
		}
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
