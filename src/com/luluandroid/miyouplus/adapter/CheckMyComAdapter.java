package com.luluandroid.miyouplus.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.base.BaseListAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.MiboComment;

public class CheckMyComAdapter extends BaseListAdapter<MiboComment> {

	public CheckMyComAdapter(Context context, List<MiboComment> list) {
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
			convertView = mInflater.inflate(R.layout.my_bmob_list_item, null);
		}
		CheckBox checkBox = ViewHolder.get(convertView, R.id.checkBox1);
		TextView timeTextView = ViewHolder.get(convertView, R.id.createtime);
		TextView contentTextView = ViewHolder.get(convertView, R.id.content_textview);
		checkBox.setChecked(false);
		timeTextView.setText(getList().get(position).getCreatedAt());
		contentTextView.setText(getList().get(position).getContent());
		return convertView;
	}

}
