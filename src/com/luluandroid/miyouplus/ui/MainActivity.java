package com.luluandroid.miyouplus.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.bean.DBMgr;
import com.luluandroid.miyouplus.config.BroadcastString;
import com.luluandroid.miyouplus.main.CustomApplcation;
import com.luluandroid.miyouplus.main.MyMessageReceiver;
import com.luluandroid.miyouplus.ui.fragment.ContactFragment;
import com.luluandroid.miyouplus.ui.fragment.MiQuanFragment;
import com.luluandroid.miyouplus.ui.fragment.MiyouPwdFragment;
import com.luluandroid.miyouplus.ui.fragment.RecentFragment;
import com.luluandroid.miyouplus.ui.fragment.SettingsFragment;
import com.luluandroid.miyouplus.util.SharePreferenceUtil;

/**
 * 登陆
 * @ClassName: MainActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午2:45:35
 */
public class MainActivity extends ActivityBase implements EventListener{

	public Boolean isWritedPwd;
	private LinearLayout main_bottom;
	private Button[] mTabs;
	
	private SharePreferenceUtil mShareUtil;
	
	private ContactFragment contactFragment;
	private RecentFragment recentFragment;
	private SettingsFragment settingFragment;
	private MiQuanFragment miquanFragment;
	private MiyouPwdFragment miyouPwdFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	
	ImageView iv_recent_tips,iv_contact_tips,iv_miquan_tips;//消息提示
	
	private LocalBroadcastManager lbm;
	
	private MiyouPwdBroadcastReceiver miyouPwdBroadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mShareUtil = CustomApplcation.getInstance().getSpUtil();
		//开启定时检测服务（单位为秒）-在这里检测后台是否还有未读的消息，有的话就取出来
		//如果你觉得检测服务比较耗流量和电量，你也可以去掉这句话-同时还有onDestory方法里面的stopPollService方法
//		BmobChat.getInstance(this).startPollService(30);
		//开启广播接收器
		initMiyouPwdBroadcastReceiver();
		initNewMessageBroadCast();
		initTagMessageBroadCast();
		initView();
		initTab();
	}

	private void initMiyouPwdBroadcastReceiver(){
		lbm = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadcastString.ACTION_WRITE_DOWN_CORRECT_PWD);
		intentFilter.addAction(BroadcastString.ACTION_WRITE_DOWN_INCORRECT_PWD);
		miyouPwdBroadcastReceiver = new MiyouPwdBroadcastReceiver();
		lbm.registerReceiver(miyouPwdBroadcastReceiver, intentFilter);
	}
	
	
	private void initView(){
		main_bottom = (LinearLayout)findViewById(R.id.main_bottom);
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_message);
		mTabs[1] = (Button) findViewById(R.id.btn_contract);
		mTabs[2] = (Button) findViewById(R.id.btn_set);
		mTabs[3] = (Button) findViewById(R.id.btn_miquan);
		iv_recent_tips = (ImageView)findViewById(R.id.iv_recent_tips);
		iv_contact_tips = (ImageView)findViewById(R.id.iv_contact_tips);
		iv_miquan_tips = (ImageView)findViewById(R.id.iv_miquan_tips);
		//把第一个tab设为选中状态
		
		mTabs[0].setSelected(true);
	}
	
	private void initTab(){
		contactFragment = new ContactFragment();
		recentFragment = new RecentFragment();
		settingFragment = new SettingsFragment();
		miquanFragment = new MiQuanFragment();
		miyouPwdFragment = new MiyouPwdFragment();
		
		fragments = new Fragment[] {recentFragment, contactFragment, settingFragment ,miquanFragment,miyouPwdFragment};
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recentFragment).
			add(R.id.fragment_container, contactFragment).hide(contactFragment).show(recentFragment).commit();
	}
	
	
	
	/**
	 * button点击事件
	 * @param view
	 */
	public void onTabSelect(View view) {
		switch (view.getId()) {
		case R.id.btn_message:
			index = 0;
			break;
		case R.id.btn_contract:
			index = 1;
			break;
		case R.id.btn_set:
			index = 2;
			break;
		case R.id.btn_miquan:
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		//把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
	
	public Boolean isWritedPwd() {
		if(isWritedPwd == null ){
			this.isWritedPwd = false;
		}
		return isWritedPwd;
	}

	public void setWritedPwd(Boolean isWritedPwd) {
		this.isWritedPwd = isWritedPwd;
	}
	
	public void setMiquanTipsVisble(boolean show){
		if(show){
			iv_miquan_tips.setVisibility(View.VISIBLE);
		}else{
			iv_miquan_tips.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//小圆点提示
		if(BmobDB.create(this).hasUnReadMsg()){
			iv_recent_tips.setVisibility(View.VISIBLE);
		}else{
			iv_recent_tips.setVisibility(View.GONE);
		}
		if(BmobDB.create(this).hasNewInvite()){
			iv_contact_tips.setVisibility(View.VISIBLE);
		}else{
			iv_contact_tips.setVisibility(View.GONE);
		}
		MyMessageReceiver.ehList.add(this);// 监听推送的消息
		//清空
		MyMessageReceiver.mNewNum=0;
		
		if(!isWritedPwd()&&mShareUtil.isAllowSelfPassword()){
			System.out.println("切换fragment");
			if(!miyouPwdFragment.isAdded()){
				getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, miyouPwdFragment).commit();
				System.out.println("fragment没导入");
			}
			getSupportFragmentManager().beginTransaction().hide(fragments[currentTabIndex]).show(miyouPwdFragment).setCustomAnimations(R.anim.left_in,
					R.anim.right_out).commit();
			main_bottom.setVisibility(View.INVISIBLE);
		}else{
			
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		setWritedPwd(false);
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// 取消监听推送的消息
	}
	
	@Override
	public void onMessage(BmobMsg message) {
		// TODO Auto-generated method stub
		refreshNewMsg(message);
	}
	
	
	/** 刷新界面
	  * @Title: refreshNewMsg
	  * @Description: TODO
	  * @param @param message 
	  * @return void
	  * @throws
	  */
	private void refreshNewMsg(BmobMsg message){
		// 声音提示
		boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
		if(isAllow){
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		iv_recent_tips.setVisibility(View.VISIBLE);
		//也要存储起来
		if(message!=null){
			BmobChatManager.getInstance(MainActivity.this).saveReceiveMessage(true,message);
		}
		if(currentTabIndex==0){
			//当前页面如果为会话页面，刷新此页面
			if(recentFragment != null){
				recentFragment.refresh();
			}
		}
	}
	
	NewBroadcastReceiver  newReceiver;
	
	private void initNewMessageBroadCast(){
		// 注册接收消息广播
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//优先级要低于ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);
	}
	
	/**
	 * 新消息广播接收者
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//刷新界面
			refreshNewMsg(null);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}
	
	TagBroadcastReceiver  userReceiver;
	
	private void initTagMessageBroadCast(){
		// 注册接收消息广播
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		//优先级要低于ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(userReceiver, intentFilter);
	}
	
	/**
	 * 标签消息广播接收者
	 */
	private class TagBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
			refreshInvite(message);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}
	
	private class MiyouPwdBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(BroadcastString.ACTION_WRITE_DOWN_CORRECT_PWD)){
				setWritedPwd(true);
				getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.left_in,
						R.anim.right_out).hide(fragments[4]).show(fragments[currentTabIndex]).commit();
				main_bottom.setVisibility(View.VISIBLE);
				return;
			}else if(intent.getAction().equals(BroadcastString.ACTION_WRITE_DOWN_INCORRECT_PWD)){
				ShowToast("秘友保护:密码错误！");
				setWritedPwd(false);
				moveTaskToBack(true);
			}
		}
		
	}
	
	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub
		if(isNetConnected){
			ShowToast(R.string.network_tips);
		}
	}

	@Override
	public void onAddUser(BmobInvitation message) {
		// TODO Auto-generated method stub
		refreshInvite(message);
	}
	
	/** 刷新好友请求
	  * @Title: notifyAddUser
	  * @Description: TODO
	  * @param @param message 
	  * @return void
	  * @throws
	  */
	private void refreshInvite(BmobInvitation message){
		boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
		if(isAllow){
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		iv_contact_tips.setVisibility(View.VISIBLE);
		if(currentTabIndex==1){
			if(contactFragment != null){
				contactFragment.refresh();
			}
		}else{
			//同时提醒通知
			String tickerText = message.getFromname()+"请求添加好友";
			boolean isAllowVibrate = CustomApplcation.getInstance().getSpUtil().isAllowVibrate();
			BmobNotifyManager.getInstance(this).showNotify(isAllow,isAllowVibrate,R.drawable.ic_launcher, tickerText, message.getFromname(), tickerText.toString(),NewFriendActivity.class);
		}
	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub
		showOfflineDialog(this);
	}
	
	@Override
	public void onReaded(String conversionId, String msgTime) {
		// TODO Auto-generated method stub
	}
	
	
	private static long firstTime;
	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			ShowToast("再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		lbm.unregisterReceiver(miyouPwdBroadcastReceiver);
		//关闭数据库连接
		DBMgr.getInstance(this).closeDbAndHelper();
		super.onDestroy();
		try {
			unregisterReceiver(newReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			unregisterReceiver(userReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//取消定时检测服务
//		BmobChat.getInstance(this).stopPollService();
	}
	
}
