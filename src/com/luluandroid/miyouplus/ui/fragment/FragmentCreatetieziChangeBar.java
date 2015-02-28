package com.luluandroid.miyouplus.ui.fragment;



import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;
import com.luluandroid.miyouplus.util.ImageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FragmentCreatetieziChangeBar extends Fragment {

	private Context context;
	private SeekBar mSeekBar;
	private TextView tips, returnText, brightnessText;// 分别是提示，返回按钮，亮度按钮，模糊按钮
	private CheckBox mitnessText;
	private int brightnessProgress = 0;
	private Bitmap tempBitmap,tempBitmap2,mitTempBitmap;//分别是未经模糊处理，经过模糊处理的Bitmap

	public FragmentCreatetieziChangeBar(Context context) {
		super();
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_create_tiezi_changebar,
				container, false);

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		clearTempBitmap();
		super.onDestroyView();
	}

	
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(hidden == false){
			restoreTempBitmap();
		}
	}

	public void restoreTempBitmap(){
		Log.i("FragmentCreatetieziChangeBar", "restoreTempBitmap 被调用");
		if(tempBitmap != null && !tempBitmap.isRecycled()){
			tempBitmap.recycle();
			tempBitmap = null;
		}
		((ImageView) getActivity().findViewById(
				R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(true);
		tempBitmap = ((ImageView) getActivity().findViewById(
				R.id.create_tiezi_imageview1)).getDrawingCache();
	}
	
	private void clearTempBitmap(){
		Log.i("ChangeBar", "调用了clearTempBitmap方法");
		if(tempBitmap != null && !tempBitmap.isRecycled()){
			tempBitmap.recycle();
			tempBitmap = null;
			Log.i("ChangeBar", "清除tempBitmap");
		}
		if(tempBitmap2 != null && !tempBitmap2.isRecycled()){
			tempBitmap2.recycle();
			tempBitmap2 = null;
			Log.i("ChangeBar", "清除tempBitmap2");
		}
		if(mitTempBitmap != null && !mitTempBitmap.isRecycled()){
			mitTempBitmap.recycle();
			mitTempBitmap = null;
			Log.i("ChangeBar", "清除mitTempBitmap");
		}
		System.gc();
	}
	
	private void initView() {
		mSeekBar = (SeekBar) getActivity().findViewById(
				R.id.create_tiezi_seekBar1);
		tips = (TextView) getActivity().findViewById(
				R.id.create_tiezi_changebar_tips);
		returnText = (TextView) getActivity().findViewById(
				R.id.create_tiezi_changebar_return);
		brightnessText = (TextView) getActivity().findViewById(
				R.id.create_tiezi_changebar_brightness);
		mitnessText = (CheckBox) getActivity().findViewById(
				R.id.create_tiezi_changebar_mistiness);
		
		//tempDrawable 暂时保存背景的图片以便进行亮度更改
		((ImageView) getActivity().findViewById(
				R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(true);
		tempBitmap = Bitmap.createBitmap(((ImageView) getActivity().findViewById(
				R.id.create_tiezi_imageview1)).getDrawingCache());
		tempBitmap2 = Bitmap.createBitmap(tempBitmap);
		((ImageView) getActivity().findViewById(
				R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(false);
		System.out.println("initview tempBitmap:"+tempBitmap+" tempBitmap2:"+tempBitmap2);
		brightnessText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSeekBar.setProgress(brightnessProgress);
				tips.setText("当前" + "亮度："+ brightnessProgress);
			}
		});
		mitnessText.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (mitnessText.isChecked()) {
					((CreateTieziActivity)getActivity()).setPicchange(true);
/*					((ImageView) getActivity().findViewById(
							R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(true);
							((ImageView) getActivity().findViewById(
							R.id.create_tiezi_imageview1)).getDrawingCache();*/
					/*((ImageView) getActivity().findViewById(
							R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(false);*/
					mitTempBitmap = ImageManager.blur(
							tempBitmap2,
							((ImageView) getActivity().findViewById(
									R.id.create_tiezi_imageview1)),
							getActivity(), false);
					Log.i("ChangeBar", "tempBitmap2:"+tempBitmap2+" mitTempBitmap:"+mitTempBitmap);
				} else {
					mitTempBitmap = tempBitmap2;
					
				}
				((ImageView) getActivity().findViewById(
						R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(false);
				((ImageView) getActivity().findViewById(
						R.id.create_tiezi_imageview1))
						.setImageBitmap(mitTempBitmap);
			}
		});
		returnText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				((CreateTieziActivity)getActivity()).switchToNavigationFragment();
			}
		});
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				seekBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				((CreateTieziActivity)getActivity()).setPicchange(true);
				if(mitnessText.isChecked()){
					tempBitmap2 =  ImageManager.BrightnessChange(mitTempBitmap, progress);
				}else{
					tempBitmap2 =  ImageManager.BrightnessChange(tempBitmap, progress);
				}
				((ImageView) getActivity()
						.findViewById(R.id.create_tiezi_imageview1)).setDrawingCacheEnabled(false);
				((ImageView) getActivity()
						.findViewById(R.id.create_tiezi_imageview1)).setImageBitmap(tempBitmap2);
//				tempImageView.setBackgroundDrawable(ImageTools.bitmapToDrawable(tempBitmap2));
//				tempImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				mSeekBar.setProgress(progress);
				brightnessProgress = mSeekBar.getProgress();
				tips.setText("当前" + "亮度："+ progress);
			}
		});
	}
}
