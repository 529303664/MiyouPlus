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
 * ��½
 * @ClassName: MainActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 ����2:45:35
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
	
	ImageView iv_recent_tips,iv_contact_tips,iv_miquan_tips;//��Ϣ��ʾ
	
	private LocalBroadcastManager lbm;
	
	private MiyouPwdBroadcastReceiver miyouPwdBroadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mShareUtil = CustomApplcation.getInstance().getSpUtil();
		//������ʱ�����񣨵�λΪ�룩-���������̨�Ƿ���δ������Ϣ���еĻ���ȡ����
		//�������ü�����ȽϺ������͵�������Ҳ����ȥ����仰-ͬʱ����onDestory���������stopPollService����
//		BmobChat.getInstance(this).startPollService(30);
		//�����㲥������
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
		//�ѵ�һ��tab��Ϊѡ��״̬
		
		mTabs[0].setSelected(true);
	}
	
	private void initTab(){
		contactFragment = new ContactFragment();
		recentFragment = new RecentFragment();
		settingFragment = new SettingsFragment();
		miquanFragment = new MiQuanFragment();
		miyouPwdFragment = new MiyouPwdFragment();
		
		fragments = new Fragment[] {recentFragment, contactFragment, settingFragment ,miquanFragment,miyouPwdFragment};
		// �����ʾ��һ��fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recentFragment).
			add(R.id.fragment_container, contactFragment).hide(contactFragment).show(recentFragment).commit();
	}
	
	
	
	/**
	 * button����¼�
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
		//�ѵ�ǰtab��Ϊѡ��״̬
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
		
		//СԲ����ʾ
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
		MyMessageReceiver.ehList.add(this);// �������͵���Ϣ
		//���
		MyMessageReceiver.mNewNum=0;
		
		if(!isWritedPwd()&&mShareUtil.isAllowSelfPassword()){
			System.out.println("�л�fragment");
			if(!miyouPwdFragment.isAdded()){
				getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, miyouPwdFragment).commit();
				System.out.println("fragmentû����");
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
		MyMessageReceiver.ehList.remove(this);// ȡ���������͵���Ϣ
	}
	
	@Override
	public void onMessage(BmobMsg message) {
		// TODO Auto-generated method stub
		refreshNewMsg(message);
	}
	
	
	/** ˢ�½���
	  * @Title: refreshNewMsg
	  * @Description: TODO
	  * @param @param message 
	  * @return void
	  * @throws
	  */
	private void refreshNewMsg(BmobMsg message){
		// ������ʾ
		boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
		if(isAllow){
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		iv_recent_tips.setVisibility(View.VISIBLE);
		//ҲҪ�洢����
		if(message!=null){
			BmobChatManager.getInstance(MainActivity.this).saveReceiveMessage(true,message);
		}
		if(currentTabIndex==0){
			//��ǰҳ�����Ϊ�Ựҳ�棬ˢ�´�ҳ��
			if(recentFragment != null){
				recentFragment.refresh();
			}
		}
	}
	
	NewBroadcastReceiver  newReceiver;
	
	private void initNewMessageBroadCast(){
		// ע�������Ϣ�㲥
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//���ȼ�Ҫ����ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);
	}
	
	/**
	 * ����Ϣ�㲥������
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//ˢ�½���
			refreshNewMsg(null);
			// �ǵðѹ㲥���ս��
			abortBroadcast();
		}
	}
	
	TagBroadcastReceiver  userReceiver;
	
	private void initTagMessageBroadCast(){
		// ע�������Ϣ�㲥
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		//���ȼ�Ҫ����ChatActivity
		intentFilter.setPriority(3);
		registerReceiver(userReceiver, intentFilter);
	}
	
	/**
	 * ��ǩ��Ϣ�㲥������
	 */
	private class TagBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
			refreshInvite(message);
			// �ǵðѹ㲥���ս��
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
				ShowToast("���ѱ���:�������");
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
	
	/** ˢ�º�������
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
			//ͬʱ����֪ͨ
			String tickerText = message.getFromname()+"������Ӻ���";
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
	 * ���������η��ؼ����˳�
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			ShowToast("�ٰ�һ���˳�����");
		}
		firstTime = System.currentTimeMillis();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		lbm.unregisterReceiver(miyouPwdBroadcastReceiver);
		//�ر����ݿ�����
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
		//ȡ����ʱ������
//		BmobChat.getInstance(this).stopPollService();
	}
	
}
