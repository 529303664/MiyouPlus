package com.luluandroid.miyouplus.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.base.BaseListAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.MiboComment;

public class ShowMyComAdapter extends BaseListAdapter<MiboComment> {

	public ShowMyComAdapter(Context context, List<MiboComment> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}
	
	public void removeAll(){
		getList().clear();
		notifyDataSetChanged();
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			mInflater.inflate(R.layout.item_show_my_comment, null);
		}
		TextView flortextView = ViewHolder.get(convertView, R.id.show_mycomment_flor);
		TextView timeTextView = ViewHolder.get(convertView, R.id.show_mycomment_head_time);
		TextView contentTextView = ViewHolder.get(convertView, R.id.show_mycomment_content);
		flortextView.setText(position+"¥");
		timeTextView.setText(getList().get(position).getCreatedAt());
		contentTextView.setText(getList().get(position).getContent());
		return convertView;
	}

}
