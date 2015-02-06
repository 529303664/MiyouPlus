package com.luluandroid.miyouplus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

	public RoundedImageView(Context context){
		super(context);
	}
	
	public RoundedImageView(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	
	public RoundedImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Drawable drawable = getDrawable();
		
		if(drawable == null){
			return;
		}
		
		if(getWidth()==0||getHeight()==0){
			return;
		}
		
		Bitmap b = ((BitmapDrawable)drawable).getBitmap();
		int w = getWidth();
		
		Bitmap roundBitmap =getCroppedBitmap(b,w);
		canvas.drawBitmap(roundBitmap, 0, 0, null);
	}

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		// TODO Auto-generated method stub
		Bitmap sbmp;
		if(bmp.getWidth()!=radius || bmp.getHeight()!=radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		
		//重新绘制新的Bitmap,创建一个空的Bitmap和绘画
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_4444);
		Canvas canvas = new Canvas(output);
		
		final Paint paint = new Paint();
		final Rect rect = new Rect(0,0,sbmp.getWidth(),sbmp.getHeight());
		
		paint.setAntiAlias(true);//抗锯齿
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
				sbmp.getWidth() / 2+0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);
		return output;
	}
	
	

}
