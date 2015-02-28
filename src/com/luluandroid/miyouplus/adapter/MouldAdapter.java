package com.luluandroid.miyouplus.adapter;


import com.luluandroid.miyouplus.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MouldAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mLayoutInflater;
	private Integer[] mThumbIds;
	public MouldAdapter(Context context,Integer[] mThumbIds) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mThumbIds = mThumbIds;
		this.mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mViewHolder = null;
		if(convertView==null){
			mViewHolder = new ViewHolder();
			convertView  = mLayoutInflater.inflate(R.layout.gridview_image_item, null);
			mViewHolder.imageview = (ImageView)convertView.findViewById(R.id.create_tiezi_single_image);
			mViewHolder.imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
			mViewHolder.imageview.setPadding(8, 8, 8, 8);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder)convertView.getTag();
		}
		mViewHolder.imageview.setImageDrawable(context.getResources().getDrawable(mThumbIds[position]));
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView imageview;
	}

}
