package com.luluandroid.miyouplus.view.dialog;

import com.luluandroid.miyouplus.util.ImageManager;

import android.content.Context;

public class DialogProgress extends DialogBase {
	boolean hasNegative;
	boolean hasTitle;
	/**
	 * 进度dialog
	 * @param context
	 * @param message
	 * @param title
	 * @param isCancel
	 * @param isFullScreen
	 */
	public DialogProgress(Context context, String message,String title,boolean isCancel,boolean isFullScreen) {
		super(context);
		super.setMessage(message);
		this.hasNegative=false;
		this.hasTitle = true;
		super.setTitle(title);
		super.setCancel(isCancel);
		super.setIsFullScreen(false);
		
	}
	
	@Override
	protected void onBuilding() {
		super.setWidth(dip2px(mainContext, 300));
		//如果设置FullScreen为true 则设置Y无效
		super.setY(ImageManager.getPhoneHeight(mainContext)/2);
		if(hasNegative){
			super.setNameNegativeButton("取消");
		}
		if(!hasTitle){
			super.setHasTitle(false);
		}
	}
	
	public int dip2px(Context context,float dipValue){
		float scale=context.getResources().getDisplayMetrics().density;		
		return (int) (scale*dipValue+0.5f);		
	}

	@Override
	protected boolean OnClickPositiveButton() {
		 
		if(onSuccessListener != null){
			onSuccessListener.onClick(this, 1);
		}
		return true;
	}

	@Override
	protected void OnClickNegativeButton() {
		// TODO Auto-generated method stub
		if(onCancelListener != null){
			onCancelListener.onClick(this, 0);
		}
	
	}

	@Override
	protected void onDismiss() {
		// TODO Auto-generated method stub

	}

}
