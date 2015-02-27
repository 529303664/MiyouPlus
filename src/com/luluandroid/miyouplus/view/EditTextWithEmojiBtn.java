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
 * �����鰴ť��EmojiconEditText
 * @author lu
 *
 */
public class EditTextWithEmojiBtn extends EmojiconEditText implements EmoAfterTouchListener{

//	���鰴ť������
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
	   	//��ȡEditText��DrawableRight,����û���������Ǿ�ʹ��Ĭ�ϵ�ͼƬ
		emoDrawable = getCompoundDrawables()[2]; 
	       if (emoDrawable == null) { 
	    	   emoDrawable = getResources().getDrawable(R.drawable.emo_btn_seletor); 
	       } 
	       emoDrawable.setBounds(0, 0, emoDrawable.getIntrinsicWidth(), emoDrawable.getIntrinsicHeight()); 
	       setEmoIconVisible(true); 
	   
	}
	
	/**
	    * ���ñ���ͼ�����ʾ�����أ�����setCompoundDrawablesΪEditText������ȥ
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
	    * ��Ϊ���ǲ���ֱ�Ӹ�EditText���õ���¼������������ü�ס���ǰ��µ�λ����ģ�����¼�
	    * �����ǰ��µ�λ�� ��  EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ�� - ͼ��Ŀ��  ��
	    * EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ��֮�����Ǿ�������ͼ�꣬��ֱ����û�п���
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
	    * ���ûζ�����
	    */
	   public void setShakeAnimation(){
	   	this.setAnimation(shakeAnimation(5));
	   }
	   
	   
	   /**
	    * �ζ�����
	    * @param counts 1���ӻζ�������
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
