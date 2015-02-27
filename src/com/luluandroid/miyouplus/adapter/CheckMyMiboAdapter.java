package com.luluandroid.miyouplus.adapter;

import java.util.List;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.base.BaseListAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.view.xlist.XListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CheckMyMiboAdapter extends BaseListAdapter<Mibos> {
	
	public CheckMyMiboAdapter(Context context, List<Mibos> list) {
		super(context, list);
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
