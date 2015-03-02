package com.luluandroid.miyouplus.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.ShowMyComAdapter;
import com.luluandroid.miyouplus.adapter.base.ViewHolder;
import com.luluandroid.miyouplus.bean.MiboComment;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.bean.Mibos.MessageType;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.config.Conf;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.control.MiboMgr.FindAllCommentListener;
import com.luluandroid.miyouplus.control.MiboMgr.SaveCommentAndMiboListener;
import com.luluandroid.miyouplus.interfaces.EmoAfterTouchListener;
import com.luluandroid.miyouplus.main.CustomApplcation;
import com.luluandroid.miyouplus.ui.fragment.FragmentImageMould;
import com.luluandroid.miyouplus.util.ImageLoadOptions;
import com.luluandroid.miyouplus.view.EditTextWithEmojiBtn;
import com.luluandroid.miyouplus.view.ListViewForScrollView;
import com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class MiboDetailActivity extends ActivityBase implements IXListViewListener,
		EmojiconGridFragment.OnEmojiconClickedListener,
		EmojiconsFragment.OnEmojiconBackspaceClickedListener,
		OnItemClickListener{
	private String logTag = "MiboDetailActivity";
	
	private int curPage = 0;
	
	private TextView title,miboTimeTextView,miboConTextView,mibofavorTextView,miboCommentTextView
	,miboContactTextView,tiezi_favor_img_count;
	private EditTextWithEmojiBtn myMessageEditext;
	private ImageView sendMiboImageView,miboPicImageView;
	private ImageView[] imageHead = new ImageView[9];
	private Fragment emotionFragment;
	private ShowMyComAdapter myComAdapter;
	private ListViewForScrollView mListView;
	private ScrollView scrollView;
	private RelativeLayout commentReLayout;
	private List<MiboComment> miboCommentList = new ArrayList<MiboComment>();
	private Mibos selectedMibo;
	private MiboMgr miboMgr;
	private int openMiboState;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case ChannelCodes.UPDATEVIEW_FAVOR_BG:
				handleFavorBGMsg(msg);
				break;
			case ChannelCodes.UPDATEVIEW_Comment_BG:
				handleCommentBGMsg(msg);
				break;
			case ChannelCodes.ALL_DOWN:
				checkIsShowComment();
			default:
				break;
			}
		}
		
	};
	
	private void handleFavorBGMsg(Message msg){
		int isAddedZan = msg.arg2;
		changeZanPic(isAddedZan,selectedMibo);
		readFavorList();
	}
	
	private void handleCommentBGMsg(Message msg){
		miboCommentTextView.setText(((Mibos)msg.obj).getCommentCount().toString()); 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mibodetail);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void init() {
		miboMgr = new MiboMgr(this);
		openMiboState = getIntent().getExtras().getInt(Conf.MIBO_SHOW_keyString);
		initView();
		readMibo();
		putMiboToView();
		
	}
	
	private void initView() {
		initHeadLayout();
		initCompontView();
		initXListView();
		initAdapter();
		initInputView();
	}
	
	private void initCompontView(){
		scrollView = (ScrollView)findViewById(R.id.mibodetail_scrollView1);
		miboTimeTextView = (TextView)findViewById(R.id.tiezi_head_time);
		miboConTextView = (TextView)findViewById(R.id.tiezi_content);
		mibofavorTextView = (TextView)findViewById(R.id.tiezi_bottom_favor);
		miboContactTextView = (TextView)findViewById(R.id.tiezi_bottom_mi_contact);
		miboCommentTextView = (TextView)findViewById(R.id.tiezi_bottom_mi_comment);
		tiezi_favor_img_count = (TextView)findViewById(R.id.tiezi_favor_img_count);
		miboPicImageView = (ImageView)findViewById(R.id.tiezi_imageview1);
		sendMiboImageView = (ImageView) findViewById(R.id.mibo_detail_comment_layout_send_mibo_imageview);
		
		mListView = (ListViewForScrollView) findViewById(R.id.mibo_detail_comment_listview);
		
		myMessageEditext = (EditTextWithEmojiBtn) findViewById(R.id.mibo_detail_comment_layout_input_editext);
		
		commentReLayout = (RelativeLayout)findViewById(R.id.mibo_detail_comment_re_layout);
		
		initHeadImageView();
		
		initCompontListener();
		
		// 引用表情的fragment
			emotionFragment = (Fragment) getSupportFragmentManager()
					.findFragmentById(R.id.mibo_detail_comment_emojiconsfragment);
	}
	
	private void initHeadImageView(){
		imageHead[0] = (ImageView)findViewById(R.id.tiezi_favor_img1);
		imageHead[1] = (ImageView)findViewById(R.id.tiezi_favor_img2);
		imageHead[2] = (ImageView)findViewById(R.id.tiezi_favor_img3);
		imageHead[3] = (ImageView)findViewById(R.id.tiezi_favor_img4);
		imageHead[4] = (ImageView)findViewById(R.id.tiezi_favor_img5);
		imageHead[5] = (ImageView)findViewById(R.id.tiezi_favor_img6);
		imageHead[6] = (ImageView)findViewById(R.id.tiezi_favor_img7);
		imageHead[7] = (ImageView)findViewById(R.id.tiezi_favor_img8);
		imageHead[8] = (ImageView)findViewById(R.id.tiezi_favor_img9);
	}
	
	private void initCompontListener(){
		mibofavorTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClickZan();
				ShowToast("点击了赞");
			}
		});
		
		miboCommentTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(commentReLayout.getVisibility() == View.GONE){
					requetComment();
				}else{
					scrollToBottom();
				}
				
			}
		});
		
		myMessageEditext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(s)){
					sendMiboImageView.setImageResource(R.drawable.ic_action_un_send_now);
					sendMiboImageView.setClickable(false);
				}else{
					sendMiboImageView.setImageResource(R.drawable.ic_action_send_now);
					sendMiboImageView.setClickable(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sendMiboImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendComment();
			}
		});
		
		sendMiboImageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				hideCommentLayout();
				return true;
			}
		});
		
	}

	private void initHeadLayout() {
		initTopBarForLeft("秘博");
		title = mHeaderLayout.getmHtvSubTitle();
		title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollToTop();
			}
		});

	}

	private void initXListView() {
		
		// 允许上拉刷新
		mListView.setPullLoadEnable(true);
		// 允许下拉
		mListView.setPullRefreshEnable(false);
		// 设置监听器
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		mListView.pullRefreshing();
		mListView.smoothScrollToPosition(0);
	}

	private void initInputView() {
		// 先关闭表情的fragment
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getSupportFragmentManager().beginTransaction().hide(emotionFragment)
				.commit();
			}
		});
		
		myMessageEditext.setEmoAfterTouchListener(new EmoAfterTouchListener() {
			
			@Override
			public void doSomeThing() {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				if (emotionFragment.isHidden()) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							hideSoftInputView();
							getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.left_in,
									R.anim.right_out).show(emotionFragment)
							.commit();
							myMessageEditext.setEmoIconOpen();
						}
					});
				} else {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							getSupportFragmentManager()
									.beginTransaction()
									.setCustomAnimations(R.anim.left_in,
											R.anim.right_out).hide(emotionFragment)
									.commit();
							myMessageEditext.setEmoIconClose();
							showSoftInputView();
						}
					});
					
				}
			}
		});
	}

	private void readMibo(){
		selectedMibo = (Mibos) getIntent().getSerializableExtra(Conf.MIBO_Serealizable_key);
	}
	
	private void initAdapter(){
		myComAdapter = new ShowMyComAdapter(this, miboCommentList);
		mListView.setAdapter(myComAdapter);
	}
	
	private void sendComment(){
		miboMgr.saveComment(myMessageEditext.getText().toString(), selectedMibo,
				userManager.getCurrentUserName(),userManager.getCurrentUserObjectId(),new SaveCommentAndMiboListener() {
			
			@Override
			public void onSuccess(MiboComment comments) {
				// TODO Auto-generated method stub
				hideSoftInputView();
				clearCommentLayout();
				aftersendComment();
				myComAdapter.add(comments);
				scrollToBottom();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast("发表评论失败：\n"+"code:"+code+"error:"+error);
			}
		});
		
	}
	
	private void clearCommentLayout(){
		myMessageEditext.setText("");
		commentReLayout.setVisibility(View.GONE);
	}
	
	private void aftersendComment(){
		
		BmobQuery<Mibos>query = new BmobQuery<Mibos>();
		query.getObject(this, selectedMibo.getObjectId(), new GetListener<Mibos>() {
			
			@Override
			public void onSuccess(Mibos mibo) {
				// TODO Auto-generated method stub
				selectedMibo = mibo;
				Message message = mHandler.obtainMessage(ChannelCodes.UPDATEVIEW_Comment_BG, selectedMibo);
				mHandler.sendMessage(message);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast("更新发送的评论失败：\n"+"code:"+code+"error:"+error);
			}
		});
	}
	
	private void testgetComment(){
		List<MiboComment> mbcList = new ArrayList<MiboComment>();
		MiboComment mibosComment;
		for(int i = 0;i<9;i++){
			mibosComment = new MiboComment(selectedMibo, "哇哈哈1", userManager.getCurrentUserName(),userManager.getCurrentUserObjectId());
			mbcList.add(mibosComment);
		}
		
		myComAdapter.addAll(mbcList);
		clearCommentLayout();
		ShowToast("测试获取评论列表成功！！"+9+"条评论");
	}
	
	private void readComment(){
//		testgetComment();
		miboMgr.findALlComment(selectedMibo,curPage, new FindAllCommentListener() {
			
			@Override
			public void onSuccess(List<MiboComment> comments) {
				// TODO Auto-generated method stub
				if(comments.size()>0){
					myComAdapter.addAll(comments);
					curPage++;
					ShowToast("获取评论列表成功！！"+comments.size()+"条评论");
				}else{
					ShowToast("没有更多数据了");
				}
				AfterLoaded();
				mHandler.sendEmptyMessage(ChannelCodes.ALL_DOWN);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast("获取评论列表失败：\n"+"code:"+code+"error:"+error);
				AfterLoaded();
				mHandler.sendEmptyMessage(ChannelCodes.ALL_DOWN);
			}
		});
	}
	
	private void putMiboToView(){
		if(selectedMibo == null){
			Log.i(logTag, "读取的可序列化秘博为空");
			return;
		}
		miboTimeTextView.setText(selectedMibo.getCreatedAt());
		miboConTextView.setText(selectedMibo.getContent());
		mibofavorTextView.setText(selectedMibo.getFavorCount().toString());
		miboCommentTextView.setText(selectedMibo.getCommentCount().toString());
		
		miboCommentTextView.setClickable(selectedMibo.isCommentOk());
		
		if(selectedMibo.getType()== MessageType.TEXT){
			miboPicImageView.setImageDrawable(getResources().getDrawable(FragmentImageMould.mBackGroundIds[selectedMibo.getPicResourceId()]));
			if(selectedMibo.getPicResourceId() == 0){
				miboConTextView.setTextColor(getResources().getColor(R.color.black));
			}
			/*if(selectedMibo.getPicResourceId() == 1){
				miboConTextView.setTextColor(getResources().getColor(R.color.white));
			}*/
			
		}else{
			if(CustomApplcation.getInstance().getSpUtil().isAllowSaveFlow()){
			miboPicImageView.setImageDrawable(getResources().getDrawable(FragmentImageMould.mBackGroundIds[1]));
			}else{
				ImageLoader.getInstance().displayImage(selectedMibo.getPic().getFileUrl(this),
						miboPicImageView, ImageLoadOptions.getOptions());
			}
		}
		for(String zanman : selectedMibo.getZanMan()){
			if(zanman.equals(BmobUser.getCurrentUser(this).getObjectId())){
				mibofavorTextView.setText(selectedMibo.getFavorCount().toString());
				Drawable drawable=getResources().getDrawable(R.drawable.ic_action_is_good);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				mibofavorTextView.setCompoundDrawables(drawable, null, null,null);
				break;
			}
		}
		readComment();
		readFavorList();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		TextView florName = ViewHolder.get(view, R.id.show_mycomment_flor);
		myMessageEditext.setText("回复"+florName.getText().toString().trim()+"：");
		requetComment();
	}
	
	private void readFavorList(){
		List<String>favorHeadList = selectedMibo.getZanMan();
		tiezi_favor_img_count.setText(String.valueOf(favorHeadList.size()));
		showFavorImages(favorHeadList.size());
	}
	
	private void showFavorImages(int count){
		if(count>9){
			showOrhideAllFavorImage(true);
			return;
		}
		showOrhideAllFavorImage(false);
		for(int i=0;i<count;i++){
			imageHead[i].setVisibility(View.VISIBLE);
		}
	}
	
	private void showOrhideAllFavorImage(boolean flag){
		if(flag){
			for(int i = 0;i<imageHead.length;i++){
				imageHead[i].setVisibility(View.VISIBLE);
			}
		}else{
			for(int i = 0;i<imageHead.length;i++){
				imageHead[i].setVisibility(View.GONE);
			}
		}
	}
	
	private void checkIsShowComment(){
		if(openMiboState == ChannelCodes.SHOW_SELECTED_MIBO_WITH_COMMENT){
			requetComment();
		}else if(openMiboState == ChannelCodes.SHOW_SELECTED_MIBO){
			hideSoftInputView();
			scrollToBottom();
		}
	}
	
	private void requetComment(){
		commentReLayout.setVisibility(View.VISIBLE);
		myMessageEditext.requestFocus();
		showSoftInputView();
	}
	
	private void hideCommentLayout(){
		commentReLayout.setVisibility(View.GONE);
		hideSoftInputView();
	}
	
	/**
	 * onRefresh 或者 onLordMore 方法加载完后需要停止更新的方法
	 */
	private void AfterLoaded() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		ShowToast("下拉刷新更多");
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		ShowToast("向下加載更多");
		readComment();
		
	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		EmojiconsFragment.backspace(myMessageEditext);
	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		EmojiconsFragment.input(myMessageEditext, emojicon);
	}

	public void showSoftInputView() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(myMessageEditext, 0);
			
		}
	}
	
	private void ClickZan(){
		Mibos mibo = selectedMibo;
		List<String> tempZanMan = mibo.getZanMan();
		String CurrentuserId = BmobUser.getCurrentUser(this).getObjectId();
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
			mibo.update(this,mibo.getObjectId(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Message message = mHandler.obtainMessage(ChannelCodes.UPDATEVIEW_FAVOR_BG, 0, 0);
					mHandler.sendMessage(message);
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					ShowToast("点赞失败："+"code:"+code+"error:"+error);
				}
			});
		}else{
			tempZanMan.add(i,CurrentuserId);
			mibo.setZanMan(tempZanMan);
			mibo.setFavorCount(mibo.getFavorCount()+1);
//			mibo.increment("favorCount", 1);
			mibo.update(this, mibo.getObjectId(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Message message = mHandler.obtainMessage(ChannelCodes.UPDATEVIEW_FAVOR_BG, 0, 1);
					mHandler.sendMessage(message);
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					ShowToast("点赞失败："+"code:"+code+"error:"+error);
				}
			});
		}
	}

private void changeZanPic(int isAddZan,Mibos mibo){
	Drawable drawable;
	if(isAddZan == 1){
		mibofavorTextView.setText(mibo.getFavorCount().toString());
		drawable=getResources().getDrawable(R.drawable.ic_action_is_good);
		
	}else{
		mibofavorTextView.setText(mibo.getFavorCount().toString());
		drawable=getResources().getDrawable(R.drawable.ic_action_good);
	}
	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

	mibofavorTextView.setCompoundDrawables(drawable, null, null,null);
}
	
	private void scrollToTop(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scrollView.fullScroll(ScrollView.FOCUS_UP);
			}
		});
	}
	
	private void scrollToBottom(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	
	
}
