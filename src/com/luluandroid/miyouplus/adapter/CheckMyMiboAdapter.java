package com.luluandroid.miyouplus.adapter;

import java.util.List;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.base.BaseListAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.config.Conf;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.control.MiboMgr.DeleteMiboListener;
import com.luluandroid.miyouplus.ui.EditMyMiboActivity;
import com.luluandroid.miyouplus.ui.MiboDetailActivity;
import com.luluandroid.miyouplus.view.xlist.XListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckMyMiboAdapter extends BaseListAdapter<Mibos> {
	
	private MiboMgr miboMgr;
	public CheckMyMiboAdapter(Context context, List<Mibos> list) {
		super(context, list);
		miboMgr = new MiboMgr(context);
	}

	public void removeAll(){
		getList().clear();
		notifyDataSetChanged();
	}

	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.check_my_com_list_item, null);
		}
		ImageView btnDelete = ViewHolder.get(convertView, R.id.check_my_com_list_item_delete);
		TextView timeTextView = ViewHolder.get(convertView, R.id.check_my_com_list_item_createtime);
		TextView contentTextView = ViewHolder.get(convertView, R.id.check_my_com_list_item_content_textview);
		timeTextView.setText(getList().get(position).getCreatedAt());
		contentTextView.setText(getList().get(position).getContent());
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteMibo(position);
			}
		});
		contentTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linkToMyMibo(position);
			}
		});
		return convertView;
	}
	
	private void linkToMyMibo(int position){
		Intent intent = new Intent(mContext,MiboDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Conf.MIBO_SHOW_keyString, ChannelCodes.SHOW_SELECTED_MIBO);
		bundle.putSerializable(Conf.MIBO_Serealizable_key, getList().get(position));
		intent.putExtras(bundle);
		((EditMyMiboActivity)mContext).startActivity(intent);
	}

	private void deleteMibo(final int position){
		miboMgr.deleteMibo(getList().get(position), new DeleteMiboListener() {
			
			@Override
			public void onSucess() {
				// TODO Auto-generated method stub
				remove(position);
				ShowToast("É¾³ýÃØ²©³É¹¦");
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast("É¾³ýÃØ²©Ê§°Ü£º\n"+"code:"+code+" error:"+error);
			}
		});
	}

}
