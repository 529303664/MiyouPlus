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
	 * ����ϵͳ�ü�ͼƬ����
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
		picturebuilder.setTitle("ͼƬ�Ƿ���Ҫ�ü�?");
		picturebuilder.setNegativeButton("ȡ��", null);
		picturebuilder.setItems(new String[] { "�ü�", "����" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0: // �ü�
							showPicturePicker(context,isTake,true);
							break;
						case 1: // ȡ���ü�
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
		System.out.println("��Ӧ��showPicturePicker");
		switch (isTake) {
		case Conf.TAKE_PICTURE:
			Uri imageuri = null;
			String fileName = null;
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			if (isCrop) {
				REQUEST_CODE = Conf.CROP;

				// ɾ����һ�ν�ͼ����ʱ�ļ�
				SharedPreferences mSharedPreferences = context
						.getSharedPreferences("tempImage", Context.MODE_PRIVATE);
				ImageTools.deletePhotoAtPathAndName(Conf.APP_SDCARD_CACHE_PATH,
						mSharedPreferences.getString("tempName", ""));
				// ���汾�ν�ͼ��ʱ�ļ�����
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
			//��仰���Կ��в���
			((FragmentActivity)context).startActivityForResult(openCameraIntent, REQUEST_CODE);
			break;

		case Conf.CHOOSE_PICTURE://�������ѡ��ͼƬ
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
