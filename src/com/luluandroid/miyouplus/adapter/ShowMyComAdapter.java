package com.luluandroid.miyouplus.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.base.BaseListAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.MiboComment;
import com.luluandroid.miyouplus.ui.MiboDetailActivity;

public class ShowMyComAdapter extends BaseListAdapter<MiboComment> {

	private Integer[] flor_image = {R.drawable.image_emoticon,R.drawable.image_emoticon2,R.drawable.image_emoticon3,R.drawable.image_emoticon4,R.drawable.image_emoticon5
			,R.drawable.image_emoticon6,R.drawable.image_emoticon7,R.drawable.image_emoticon8,R.drawable.image_emoticon9,R.drawable.image_emoticon10,R.drawable.image_emoticon11
			,R.drawable.image_emoticon12,R.drawable.image_emoticon13,R.drawable.image_emoticon14,R.drawable.image_emoticon15,R.drawable.image_emoticon16,R.drawable.image_emoticon17
			,R.drawable.image_emoticon18,R.drawable.image_emoticon19,R.drawable.image_emoticon20,R.drawable.image_emoticon21,R.drawable.image_emoticon22,R.drawable.image_emoticon23
			,R.drawable.image_emoticon24,R.drawable.image_emoticon25,R.drawable.image_emoticon26,R.drawable.image_emoticon27,R.drawable.image_emoticon28,R.drawable.image_emoticon29
			,R.drawable.image_emoticon30,R.drawable.image_emoticon31,R.drawable.image_emoticon32,R.drawable.image_emoticon33};
	
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
			convertView = mInflater.inflate(R.layout.item_show_my_comment, null);
		}
		TextView flortextView = ViewHolder.get(convertView, R.id.show_mycomment_flor);
		TextView timeTextView = ViewHolder.get(convertView, R.id.show_mycomment_head_time);
		TextView contentTextView = ViewHolder.get(convertView, R.id.show_mycomment_content);
		ImageView headImageView = ViewHolder.get(convertView, R.id.show_mycomment_head_imageview);
		flortextView.setText(position+1+"Â¥");
		if(getList().get(position).getFromUserId().equals(getList().get(position).getFromMiboUserId())){
			flortextView.setText("Â¥Ö÷");
			flortextView.setTextColor(mContext.getResources().getColor(R.color.color_bottom_text_press));
		}
		timeTextView.setText(getList().get(position).getCreatedAt());
		contentTextView.setText(getList().get(position).getContent());
		headImageView.setImageDrawable(mContext.getResources().getDrawable(flor_image[((getList().get(position).getFromUserId().charAt(0)-'0')%32)]));
		return convertView;
	}

}
