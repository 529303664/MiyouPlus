package com.luluandroid.miyouplus.adapter;

import java.util.List;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.rockerhieu.emojicon.EmojiconTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MiBoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mLayoutInflater;
	private List<Mibos>mMibos;
	
	public MiBoAdapter(Context context,List<Mibos>mMibos){
		this.context = context;
		this.mMibos = mMibos;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mMibos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mMibos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder =new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.tiezi_item, null);
			viewHolder.mImageView = (ImageView)convertView.findViewById(R.id.tiezi_head_imageview);
			viewHolder.headUserName = (TextView)convertView.findViewById(R.id.tiezi_head_username);
			viewHolder.tiezi_time = (TextView)convertView.findViewById(R.id.tiezi_head_time);
			viewHolder.bgImageView = (ImageView)convertView.findViewById(R.id.tiezi_imageview1);
			viewHolder.ConTextView = (EmojiconTextView)convertView.findViewById(R.id.tiezi_content);
			viewHolder.favor = (TextView)convertView.findViewById(R.id.tiezi_bottom_favor);
			viewHolder.mi_contact = (TextView)convertView.findViewById(R.id.tiezi_bottom_mi_contact);
			viewHolder.mi_comment = (TextView)convertView.findViewById(R.id.tiezi_bottom_mi_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		/*正式代码 还未完成
		 * viewHolder.mRoundedImageView.setImageBitmap(((Mibos)mMibos.get(position)).getHeadBitmap());
		 * viewHolder.bgImageView.setImageBitmap(((Mibos)mMibos.get(position)).getBackgroundBitmap());
		 * viewHolder.tiezi_time.setText(((Mibos)mMibos.get(position)).getMiboTime());
		 * */
		
		viewHolder.headUserName.setText(((Mibos)mMibos.get(position)).getHeadUserName());
		viewHolder.ConTextView.setText(((Mibos)mMibos.get(position)).getContent());
		viewHolder.favor.setText(String.valueOf(((Mibos)mMibos.get(position)).getFavorCount()));
		viewHolder.mi_comment.setText(String.valueOf(((Mibos)mMibos.get(position)).getComment().size()));
		viewHolder.mi_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "用户点击了聊天");
			}
		});
		viewHolder.mi_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "用户点击了评论");
			}
		});
		viewHolder.favor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "用户点击了赞");
			}
		});
		viewHolder.bgImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "用户点击了图片");
			}
		});
		return convertView;
	}
	
	public class ViewHolder{
		public ImageView mImageView;
		public TextView headUserName,tiezi_time;
		public ImageView bgImageView;
		public EmojiconTextView ConTextView;
		public TextView favor,mi_contact,mi_comment;
	}
}
