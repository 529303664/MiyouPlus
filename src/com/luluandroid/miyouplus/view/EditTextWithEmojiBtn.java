package com.luluandroid.miyouplus.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.interfaces.EmoAfterTouchListener;
import com.rockerhieu.emojicon.EmojiconEditText;

/**
 * 带表情按钮的EmojiconEditText
 * @author lu
 *
 */
public class EditTextWithEmojiBtn extends EmojiconEditText implements EmoAfterTouchListener{

//	表情按钮的引用
	private Drawable emoDrawable;
	
	private EmoAfterTouchListener emoAfterTouchListener;
	
	public EditTextWithEmojiBtn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EditTextWithEmojiBtn(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
		// TODO Auto-generated constructor stub
	}

	public EditTextWithEmojiBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}
	
	public void setEmoAfterTouchListener(EmoAfterTouchListener emoAfterTouchListener) {
		this.emoAfterTouchListener = emoAfterTouchListener;
	}

	private void init(){
	   	//获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		emoDrawable = getCompoundDrawables()[2]; 
	       if (emoDrawable == null) { 
	    	   emoDrawable = getResources().getDrawable(R.drawable.emo_btn_seletor); 
	       } 
	       emoDrawable.setBounds(0, 0, emoDrawable.getIntrinsicWidth(), emoDrawable.getIntrinsicHeight()); 
	       setEmoIconVisible(true); 
	   
	}
	
	/**
	    * 设置表情图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	    * @param visible
	    */
	   protected void setEmoIconVisible(boolean visible) { 
	       Drawable right = visible ? emoDrawable : null; 
	       setCompoundDrawables(getCompoundDrawables()[0], 
	               getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
	   } 
	   
	   public void setEmoIconOpen(){
	    	   emoDrawable = getResources().getDrawable(R.drawable.ic_emoticon_pressed); 
	       emoDrawable.setBounds(0, 0, emoDrawable.getIntrinsicWidth(), emoDrawable.getIntrinsicHeight());
	       setCompoundDrawables(getCompoundDrawables()[0], 
	               getCompoundDrawables()[1], emoDrawable, getCompoundDrawables()[3]);
	   }
	   
	   public void setEmoIconClose(){
		   emoDrawable = getResources().getDrawable(R.drawable.ic_emoticon_normal); 
	       emoDrawable.setBounds(0, 0, emoDrawable.getIntrinsicWidth(), emoDrawable.getIntrinsicHeight());
	       setCompoundDrawables(getCompoundDrawables()[0], 
	               getCompoundDrawables()[1], emoDrawable, getCompoundDrawables()[3]);
	   }
	   
	   /**
	    * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
	    * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
	    * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	    */
	   
	   
	   @SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
	       if (getCompoundDrawables()[2] != null) { 
	           if (event.getAction() == MotionEvent.ACTION_UP) { 
	           	boolean touchable = event.getX() > (getWidth() 
	                       - getPaddingRight() - emoDrawable.getIntrinsicWidth()) 
	                       && (event.getX() < ((getWidth() - getPaddingRight())));
	               if (touchable) { 
	            	   emoAfterTouchListener.doSomeThing();
	               } 
	           } 
	       } 
	       return super.onTouchEvent(event); 
	}

	/**
	    * 设置晃动动画
	    */
	   public void setShakeAnimation(){
	   	this.setAnimation(shakeAnimation(5));
	   }
	   
	   
	   /**
	    * 晃动动画
	    * @param counts 1秒钟晃动多少下
	    * @return
	    */
	   public static Animation shakeAnimation(int counts){
	   	Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
	   	translateAnimation.setInterpolator(new CycleInterpolator(counts));
	   	translateAnimation.setDuration(1000);
	   	return translateAnimation;
	   }

	@Override
	public void doSomeThing() {
		// TODO Auto-generated method stub
		
	}
	   
}
