package com.luluandroid.miyouplus.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.base.BaseListAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.MiboComment;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.control.MiboMgr.DeleteMiboListener;

public class CheckMyComAdapter extends BaseListAdapter<MiboComment> {

	private MiboMgr miboMgr;
	public CheckMyComAdapter(Context context, List<MiboComment> list) {
		super(context, list);
		miboMgr = new MiboMgr(context);
		// TODO Auto-generated constructor stub
	}
	
	public void removeAll(){
		getList().clear();
		notifyDataSetChanged();
	}
	
	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.my_bmob_list_item, null);
		}
		ImageView btnToDelete = ViewHolder.get(convertView, R.id.my_bmob_list_item_delete);
		TextView timeTextView = ViewHolder.get(convertView, R.id.my_bmob_list_item_createtime);
		TextView contentTextView = ViewHolder.get(convertView, R.id.my_bmob_list_item_content_textview);
		timeTextView.setText(getList().get(position).getCreatedAt());
		contentTextView.setText(getList().get(position).getContent());
		btnToDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				miboMgr.removeComment(getList().get(position), new DeleteMiboListener() {
					
					@Override
					public void onSucess() {
						// TODO Auto-generated method stub
						remove(position);
						ShowToast("É¾³ýÆÀÂÛ³É¹¦");
					}
					
					@Override
					public void onFailure(int code, String error) {
						// TODO Auto-generated method stub
						ShowToast("É¾³ýÆÀÂÛÊ§°Ü£º\n"+"code:"+code+" error:"+error);
					}
				});
			}
		});
		return convertView;
	}

}
