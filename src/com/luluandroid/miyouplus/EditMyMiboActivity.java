package com.luluandroid.miyouplus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobUserManager;

import com.luluandroid.miyouplus.adapter.CheckMyComAdapter;
import com.luluandroid.miyouplus.adapter.CheckMyMiboAdapter;
import com.luluandroid.miyouplus.bean.MiboComment;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.control.MiboMgr.DeleteMiboListener;
import com.luluandroid.miyouplus.control.MiboMgr.FindAllCommentListener;
import com.luluandroid.miyouplus.control.MiboMgr.FindMiboListener;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.ui.LoginActivity;
import com.luluandroid.miyouplus.view.xlist.XListView;
import com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EditMyMiboActivity extends ActionBarActivity implements IXListViewListener,OnItemClickListener{

	private String LogTag = "EditMyMiboActivity";
	private boolean isMiboOrComment;
	private boolean selectAllFlag = false;
	private TextView loadingTips;
	private ActionBar actionbar;
	private XListView mListView;
	private CheckBox checkbox;
	private CheckMyComAdapter checkMyComAdapter;
	private CheckMyMiboAdapter checkMyMiboAdapter;
	private List<Mibos> miboList = new ArrayList<Mibos>();
	private List<MiboComment> commentList = new ArrayList<MiboComment>();
	private List<Integer> SelectPositionList = new ArrayList<Integer>();
	private MiboMgr miboMgr;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
				case ChannelCodes.UPDATEVIEW_MYMIBOCOM_LISTVIEW:
					if(isMiboOrComment)
						checkMyMiboAdapter.notifyDataSetChanged();
					else
						checkMyComAdapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(null != savedInstanceState){
			isMiboOrComment = savedInstanceState.getBoolean("isMiboOrComment");
		}else{
			isMiboOrComment = getIntent().getExtras().getBoolean("isMiboOrComment");
		}
		checkLogin();
		setContentView(R.layout.activity_edit_my_mibo);
		init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkLogin();
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("isMiboOrComment", isMiboOrComment);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		isMiboOrComment = savedInstanceState.getBoolean("isMiboOrComment");
	}

	private void init(){
		miboMgr = new MiboMgr(this);
		initView();
	}

	private void initView(){
		initActionbar();
		loadingTips = (TextView)findViewById(R.id.loadingtip);
		if(isMiboOrComment){
			loadingTips.setText("正在加载我的秘博……");
		}else{
			loadingTips.setText("正在加载我的评论……");
		}
		initXListView();
	}
	
	private void initXListView(){
		mListView = (XListView) findViewById(R.id.editmymibo_listview);
		// 首先不允许加载更多
		mListView.setPullLoadEnable(false);
		// 不允许下拉
		mListView.setPullRefreshEnable(false);
		// 设置监听器
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();
		
		initAdapter();
				
		mListView.setOnItemClickListener(this);
		
		findata();
	}
	
	private void initAdapter(){
		if(isMiboOrComment){
			checkMyMiboAdapter = new CheckMyMiboAdapter(this, miboList);
			mListView.setAdapter(checkMyMiboAdapter);
		}else{
			checkMyComAdapter = new CheckMyComAdapter(this, commentList);
			mListView.setAdapter(checkMyComAdapter);
		}
	}
	
	private void initActionbar(){
		actionbar = getSupportActionBar();
//		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * 检测设备是否有重复登录
	 */
	public void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			ShowToast.showShortToast(this, "您的账号已在其他设备上登录!");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}
	
	/** 隐藏软键盘
	  * hideSoftInputView
	  * @Title: hideSoftInputView
	  * @Description: TODO
	  * @param  
	  * @return void
	  * @throws
	  */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_my_mibo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.allselect) {
			ShowToast.showShortToast(this, "全选");
			selectAll();
			return true;
		}else if(id == R.id.confirm){
			ShowToast.showShortToast(this, "删除");
			if(isMiboOrComment){
				deleteMiboData();
			}else{
				deleteCommentData();
			}
			return true;
		}else if(id == R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private View childView;
	private void selectAll(){
		int size = mListView.getChildCount();
		selectAllFlag = !selectAllFlag;
		int firstVisblePosition = mListView.getFirstVisiblePosition();
		
		for(int i = 0;i<size;i++){
			childView = mListView.getChildAt(i-firstVisblePosition+1);
			if(childView!=null){
				checkbox = (CheckBox)childView.findViewById(R.id.checkBox1);
				if(checkbox!=null){
					checkbox.setChecked(selectAllFlag);
				}
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		checkbox = (CheckBox) mListView.getChildAt(position).findViewById(R.id.checkBox1);
		checkbox.setChecked(!checkbox.isChecked());
	}
	
	private void findata(){
		if(isMiboOrComment){
			miboMgr.findMyMibo(new FindMiboListener() {
				
				@Override
				public void onSuccess(List<Mibos> mibosList) {
					// TODO Auto-generated method stub
					refreshPull();
					loadingTips.setVisibility(View.GONE);
					checkMyMiboAdapter.removeAll();
					checkMyMiboAdapter.addAll(mibosList);
					mListView.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					refreshPull();
					mListView.setVisibility(View.GONE);
					loadingTips.setText("加载失败："+"code："+code+"\nerror："+error);
					loadingTips.setVisibility(View.VISIBLE);
				}
			});
		}else{
			miboMgr.findMyComments(new FindAllCommentListener() {
				
				@Override
				public void onSuccess(List<MiboComment> comments) {
					// TODO Auto-generated method stub
					refreshPull();
					loadingTips.setVisibility(View.GONE);
					checkMyComAdapter.removeAll();
					checkMyComAdapter.addAll(comments);
					mListView.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					refreshPull();
					mListView.setVisibility(View.GONE);
					loadingTips.setText("加载失败："+"\n"+"code："+code+"\nerror："+error);
					loadingTips.setVisibility(View.VISIBLE);
					
				}
			});
		}
	}
	
	private List<Mibos> checkSelectMibo(){
		SelectPositionList.clear();
		List<Mibos>deleteMibos = new ArrayList<Mibos>();
		int size = mListView.getChildCount();
		selectAllFlag = !selectAllFlag;
		int firstVisblePosition = mListView.getFirstVisiblePosition();
		
		for(int i = 0;i<size;i++){
			childView = mListView.getChildAt(i-firstVisblePosition+1);
			if(childView!=null){
				checkbox = (CheckBox)childView.findViewById(R.id.checkBox1);
				if(checkbox!=null){
					if(checkbox.isChecked()){
						deleteMibos.add(miboList.get(i));
						SelectPositionList.add(i);
					}
				}
			}
		}
		return deleteMibos;
	}
	
	private List<MiboComment> checkSelectComment(){
		SelectPositionList.clear();
		List<MiboComment>deleteComments = new ArrayList<MiboComment>();
		int size = mListView.getChildCount();
		selectAllFlag = !selectAllFlag;
		int firstVisblePosition = mListView.getFirstVisiblePosition();
		
		for(int i = 0;i<size;i++){
			childView = mListView.getChildAt(i-firstVisblePosition+1);
			if(childView!=null){
				checkbox = (CheckBox)childView.findViewById(R.id.checkBox1);
				if(checkbox!=null){
					if(checkbox.isChecked()){
						deleteComments.add(commentList.get(i));
						SelectPositionList.add(i);
					}
				}
			}
		}
		return deleteComments;
	
	}
	
	private void deleteMiboData(){
		List<Mibos>deleteMibos = checkSelectMibo();
		miboMgr.deleteselectMibo(deleteMibos, new DeleteMiboListener() {
			
			@Override
			public void onSucess() {
				// TODO Auto-generated method stub
				deletelocalMiboData();
				ShowToast.showShortToast(EditMyMiboActivity.this, "删除成功");
//				mHandler.sendEmptyMessage(ChannelCodes.UPDATEVIEW_MYMIBOCOM_LISTVIEW);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(EditMyMiboActivity.this, "删除失败\n"
				+"code"+code+" error:"+error);
			}
		});
	}
	
	private void deletelocalMiboData(){
		for(Integer i : SelectPositionList){
			miboList.remove(i);
		}
		checkMyMiboAdapter.removeAll();
		checkMyMiboAdapter.addAll(miboList);
	}
	
	
	private void deleteCommentData(){
		List<MiboComment>commentList = checkSelectComment();
		miboMgr.deleteSelectComment(commentList, new DeleteMiboListener() {
			
			@Override
			public void onSucess() {
				// TODO Auto-generated method stub
				deletelocalCommentData();
				ShowToast.showShortToast(EditMyMiboActivity.this, "删除成功");
//				mHandler.sendEmptyMessage(ChannelCodes.UPDATEVIEW_MYMIBOCOM_LISTVIEW);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(EditMyMiboActivity.this, "删除失败\n"
				+"code"+code+" error:"+error);
			}
		});
	
	}
	
	private void deletelocalCommentData(){
		for(Integer i : SelectPositionList){
			miboList.remove(i);
		}
		checkMyComAdapter.removeAll();
		checkMyComAdapter.addAll(commentList);
	}
	
	private void refreshLoad(){
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}
	
	private void refreshPull(){
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Log.i(LogTag, "执行刷新操作");
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Log.i(LogTag, "执行加载更多操作");
		
	}

}
