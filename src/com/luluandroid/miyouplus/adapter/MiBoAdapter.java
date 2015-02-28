package com.luluandroid.miyouplus.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.bean.User;
import com.luluandroid.miyouplus.bean.Mibos.MessageType;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.config.Conf;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.control.MiboMgr.MiboCountListener;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.ui.ChatActivity;
import com.luluandroid.miyouplus.ui.MainActivity;
import com.luluandroid.miyouplus.ui.MiboDetailActivity;
import com.luluandroid.miyouplus.ui.fragment.FragmentImageMould;
import com.luluandroid.miyouplus.util.ImageLoadOptions;
import com.luluandroid.miyouplus.view.xlist.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rockerhieu.emojicon.EmojiconTextView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class MiBoAdapter extends BaseAdapter {

	private Context context;
	private MiboMgr miboMgr;
	private LayoutInflater mLayoutInflater;
	private XListView tiezi_ListView;
	private List<Mibos>mMibos;
	private User user;
	private BmobUserManager userManager;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case ChannelCodes.MIBO_Find_SUCCESS:
					notifyDataSetChanged();
					break;
				case ChannelCodes.MIBO_Find_FAILURE:
					ShowToast.showShortToast(context, (String)msg.obj);
					break;
				case ChannelCodes.UPDATEVIEW_FAVOR_BG:
					handleFavorBGMsg(msg);
					break;
				case ChannelCodes.UPDATEVIEW_Comment_BG:
					handleCommentBGMsg(msg);
				default:
					break;
			}
			
		}

	};
	
	private void handleFavorBGMsg(Message msg){
		int index = msg.arg1;
		int isAddedZan = msg.arg2;
		Mibos mibo = mMibos.get(index);
		int firstVisible = tiezi_ListView.getFirstVisiblePosition();
			ViewHolder holder = (ViewHolder)(tiezi_ListView.getChildAt(index-firstVisible+1).getTag());
			if(holder != null){
				changeZanPic(holder,isAddedZan,mibo);
				readFavorList(holder,mibo.getZanMan());
			}else{
				Log.i("MiboAdapter", "tiezi_ListView.getChildAt(index-firstVisible).getTag() 为空！");
			}
	}
	
	private void handleCommentBGMsg(Message msg){
		int index = msg.arg1;
		int count = msg.arg2;
		int firstVisible = tiezi_ListView.getFirstVisiblePosition();
		Mibos mibo = mMibos.get(index);
		mibo.setCommentCount(count);
		View updateView = tiezi_ListView.getChildAt(index-firstVisible+1);
		if(null != updateView){
			ViewHolder holder = (ViewHolder)(updateView.getTag());
			if(holder != null){
				holder.mi_comment.setText(String.valueOf(count));
			}else{
				Log.i("MiboAdapter", "tiezi_ListView.getChildAt(index-firstVisible).getTag() 为空！");
			}
		}else{
			Log.i("MiboAdapter", "tiezi_ListView.getChildAt(index-firstVisible) 为空！");
		}
			
	}
	
	public MiBoAdapter(Context context,List<Mibos>mMibos,MiboMgr miboMgr){
		this.context = context;
		this.mMibos = mMibos;
		this.miboMgr = miboMgr;
		mLayoutInflater = LayoutInflater.from(context);
		tiezi_ListView = (XListView)(((MainActivity)context).findViewById(
				R.id.fragment_miquan_listview));
		userManager = BmobUserManager.getInstance(context);
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
		return position;
	}
	
	public void setItem(int position,Mibos mibo){
		mMibos.set(position, mibo);
	}
	
	public void addItem(Mibos mibo){
		mMibos.add(mibo);
		notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		mMibos.remove(position);
		notifyDataSetChanged();
	}
	
	public void addAll(List<Mibos> miboList){
		mMibos.addAll(miboList);
		notifyDataSetChanged();
	}
	
	public void clear(){
		mMibos.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
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
			viewHolder.tiezi_favor_img_count = (TextView)convertView.findViewById(R.id.tiezi_favor_img_count);
			
			viewHolder.bgImageView.setScaleType(ScaleType.CENTER_CROP);
			
			viewHolder.imageHead[0] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img1);
			viewHolder.imageHead[1] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img2);
			viewHolder.imageHead[2] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img3);
			viewHolder.imageHead[3] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img4);
			viewHolder.imageHead[4] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img5);
			viewHolder.imageHead[5] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img6);
			viewHolder.imageHead[6] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img7);
			viewHolder.imageHead[7] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img8);
			viewHolder.imageHead[8] = (ImageView)convertView.findViewById(R.id.tiezi_favor_img9);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.ConTextView.setText(((Mibos)mMibos.get(position)).getContent());
		viewHolder.favor.setText(((Mibos)mMibos.get(position)).getFavorCount().toString());
		viewHolder.tiezi_time.setText(((Mibos)mMibos.get(position)).getCreatedAt());
		viewHolder.mi_comment.setText(((Mibos)mMibos.get(position)).getCommentCount().toString());
		if(((Mibos)mMibos.get(position)).getType()== MessageType.TEXT){
			viewHolder.bgImageView.setImageDrawable(context.getResources().getDrawable(FragmentImageMould.mBackGroundIds[((Mibos)mMibos.get(position)).getPicResourceId()]));
			if(((Mibos)mMibos.get(position)).getPicResourceId() == 0){
				viewHolder.ConTextView.setTextColor(context.getResources().getColor(R.color.black));
			}
			if(((Mibos)mMibos.get(position)).getPicResourceId() == 1){
				viewHolder.ConTextView.setTextColor(context.getResources().getColor(R.color.white));
			}
		}else{
			ImageLoader.getInstance().displayImage(((Mibos)mMibos.get(position)).getPic().getFileUrl(context), viewHolder.bgImageView, ImageLoadOptions.getOptions());
		}
		for(String zanman : ((Mibos)mMibos.get(position)).getZanMan()){
			if(zanman.equals(BmobUser.getCurrentUser(context).getObjectId())){
				viewHolder.favor.setText(mMibos.get(position).getFavorCount().toString());
				Drawable drawable=context.getResources().getDrawable(R.drawable.ic_action_is_good);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				viewHolder.favor.setCompoundDrawables(drawable, null, null,null);
				break;
			}
		}
		
		readFavorList(viewHolder,((Mibos)mMibos.get(position)).getZanMan());
		
		viewHolder.mi_contact.setClickable(((Mibos)mMibos.get(position)).isOpentoAll());
		viewHolder.mi_comment.setClickable(((Mibos)mMibos.get(position)).isCommentOk());
		
		miboMgr.findCommentCount(mMibos.get(position), new MiboCountListener() {
			
			@Override
			public void onSucess(int count) {
				// TODO Auto-generated method stub
				Message message = mHandler.obtainMessage(ChannelCodes.UPDATEVIEW_Comment_BG,position,count);
				mHandler.sendMessage(message);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "第"+position+"个更新失败：\n"+
				"code:"+code+" error:"+error);
			}
		});
		
		
		viewHolder.mi_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ShowToast.showShortToast(context, "第"+position+"聊天被点击");
				ClickContact(position);
				
			}
		});
		viewHolder.mi_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ShowToast.showShortToast(context, "第"+position+"评论被点击");
				ClickComment(position);
				
			}
		});
		viewHolder.favor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClickZan(position);
			}
		});
		viewHolder.bgImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ShowToast.showShortToast(context, "第"+position+"图片被点击");
				CLickImage(position);
			
			}
		});
		return convertView;
	}
	
	private void readFavorList(ViewHolder viewHolder,List<String> favorList){
		List<String>favorHeadList = favorList;
		viewHolder.tiezi_favor_img_count.setText(String.valueOf(favorHeadList.size()));
		showFavorImages(viewHolder,favorHeadList.size());
	}
	
	private void showFavorImages(ViewHolder viewHolder,int count){
		if(count>9){
			showOrhideAllFavorImage(viewHolder,true);
			return;
		}
		showOrhideAllFavorImage(viewHolder,false);
		for(int i=0;i<count;i++){
			viewHolder.imageHead[i].setVisibility(View.VISIBLE);
		}
	}
	
	private void showOrhideAllFavorImage(ViewHolder viewHolder,boolean flag){
		if(flag){
			for(int i = 0;i<viewHolder.imageHead.length;i++){
				viewHolder.imageHead[i].setVisibility(View.VISIBLE);
			}
		}else{
			for(int i = 0;i<viewHolder.imageHead.length;i++){
				viewHolder.imageHead[i].setVisibility(View.GONE);
			}
		}
	}
	
	private void CLickImage(int position){
		Intent intent = new Intent(context,MiboDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Conf.MIBO_SHOW_keyString, ChannelCodes.SHOW_SELECTED_MIBO);
		bundle.putSerializable(Conf.MIBO_Serealizable_key, mMibos.get(position));
		intent.putExtras(bundle);
		((MainActivity)context).startAnimActivity(intent);
	}
	
	private void ClickContact(int position){
		if(mMibos.get(position).getHeadUserName().equals(userManager.getCurrentUserName())){
			ShowToast.showShortToast(context, "不能和自己聊天");
			return;
		}
		userManager.queryUser(mMibos.get(position).getHeadUserName(), new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context,"聊天：查找秘友失败,原因:"+"code:"+arg0+" error:"+ arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) {
					user = arg0.get(0);
					startContact(user);
				} else {
					ShowToast.showShortToast(context,"聊天：查找秘友 onSuccess但查无此人");
				}
			}
		});
	}
	
	private void startContact(User user){
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra("user", user);
		((MainActivity)context).startAnimActivity(intent);
	}
	
	private void ClickComment(int position){
		Intent intent = new Intent(context,MiboDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Conf.MIBO_SHOW_keyString, ChannelCodes.SHOW_SELECTED_MIBO_WITH_COMMENT);
		bundle.putSerializable(Conf.MIBO_Serealizable_key, mMibos.get(position));
		intent.putExtras(bundle);
		((MainActivity)context).startAnimActivity(intent);
	}
	
	private void ClickZan(final int location){
		
		Mibos mibo = mMibos.get(location);
		List<String> tempZanMan = mibo.getZanMan();
		String CurrentuserId = BmobUser.getCurrentUser(context).getObjectId();
		boolean isadded = false;
		int i = 0;
		for(;i<tempZanMan.size();i++){
			if(CurrentuserId.equals(tempZanMan.get(i))){
				isadded = true;
				break;
			}
		}
		if(isadded){
			tempZanMan.remove(i);
			mibo.setZanMan(tempZanMan);
			mibo.setFavorCount(mibo.getFavorCount()-1);
//			mibo.increment("favorCount", -1);
			mibo.update(context,mibo.getObjectId(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Message message = mHandler.obtainMessage(ChannelCodes.UPDATEVIEW_FAVOR_BG, location, 0);
					mHandler.sendMessage(message);
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					ShowToast.showShortToast(context, "点赞失败："+"code:"+code+"error:"+error);
				}
			});
		}else{
			tempZanMan.add(i,CurrentuserId);
			mibo.setZanMan(tempZanMan);
			mibo.setFavorCount(mibo.getFavorCount()+1);
//			mibo.increment("favorCount", 1);
			mibo.update(context, mibo.getObjectId(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Message message = mHandler.obtainMessage(ChannelCodes.UPDATEVIEW_FAVOR_BG, location, 1);
					mHandler.sendMessage(message);
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					ShowToast.showShortToast(context, "点赞失败："+"code:"+code+"error:"+error);
				}
			});
		}
	}
	
	private void changeZanPic(ViewHolder viewHolder, int isAddZan,Mibos mibo){
		Drawable drawable;
		if(isAddZan == 1){
			viewHolder.favor.setText(mibo.getFavorCount().toString());
			drawable=context.getResources().getDrawable(R.drawable.ic_action_is_good);
			
		}else{
			viewHolder.favor.setText(mibo.getFavorCount().toString());
			drawable=context.getResources().getDrawable(R.drawable.ic_action_good);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

		viewHolder.favor.setCompoundDrawables(drawable, null, null,null);
	}
	
	/*private void addComment(String content,){
		miboMgr.saveComment(content, miboParent)
	}*/
	
	private void deleteComment(){
		
	}
	
	public class ViewHolder{
		public ImageView mImageView;
		public TextView headUserName,tiezi_time,tiezi_favor_img_count;
		public ImageView bgImageView;
		public EmojiconTextView ConTextView;
		public TextView favor,mi_contact,mi_comment;
		public ImageView[] imageHead = new ImageView[9];
	}
	
}
