package com.luluandroid.miyouplus.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobUserManager;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.MiBoAdapter;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.config.BroadcastString;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.control.MiboMgr;
import com.luluandroid.miyouplus.control.MiboMgr.FindMiboListener;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;
import com.luluandroid.miyouplus.ui.FragmentBase;
import com.luluandroid.miyouplus.ui.LoginActivity;
import com.luluandroid.miyouplus.ui.PopMenu;
import com.luluandroid.miyouplus.util.TimeUtil;
import com.luluandroid.miyouplus.view.xlist.XListView;
import com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class MiQuanFragment extends FragmentBase implements IXListViewListener {

	private PopMenu popMenu;
	private TextView title;
	private XListView mListView;
	private MiBoAdapter miBoAdapter;
	private FragmentAllMiboReceiver mFragmentAllMiboReceiver;
	private LocalBroadcastManager lbm;
	private List<Mibos>Mibos;
	private MiboMgr miboMgr;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment_miquan, container, false);
	}

	public void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(getActivity());
		if (userManager.getCurrentUser() == null) {
			ShowToast("�����˺����������豸�ϵ�¼!");
			startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().finish();
		}
	}
	
	
	
	/** ����������
	  * hideSoftInputView
	  * @Title: hideSoftInputView
	  * @Description: TODO
	  * @param  
	  * @return void
	  * @throws
	  */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initBroadcastReceiver();
	}
	
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkLogin();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		stopBroadcastReceiver();
		super.onStop();
	}

	
	
	// ����handler ���ڿ���UI
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case ChannelCodes.MIBO_Find_SUCCESS:
					ShowToast((String)msg.obj);
					AfterLoaded();
					break;
				case ChannelCodes.MIBO_Find_FAILURE:
					ShowToast((String)msg.obj);
					AfterLoaded();
					break;
				default:
					break;
			}
			
		}

	};

	private void init() {
		Mibos = new ArrayList<Mibos>();
		miboMgr = new MiboMgr(getActivity());
		
		initView();
	}

	private void initHeadLayout() {
		initPopMenu();
		initTopBarForOnlyTitle("��Ȧ");
		title = mHeaderLayout.getmHtvSubTitle();
		System.out.println("title ��:" + title);
		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popMenu.showAsDropDown(v);
			}
		});
		title.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (mListView.getSelectedItemPosition() == 0) {
					return false;
				}
				mListView.setSelection(0);
				// mListView.setSelectionFromTop(0, 0);
				return true;
			}
		});
	}

	private void initView() {
		initXListView();
		initHeadLayout();
		initAdpter();
	}
	
	

	private void initXListView() {
		mListView = (XListView) getActivity().findViewById(
				R.id.fragment_miquan_listview);
		// ��������ˢ��
		mListView.setPullLoadEnable(true);
		// ��������
		mListView.setPullRefreshEnable(true);
		// ���ü�����
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();
	}

	private void initPopMenu() {
		popMenu = new PopMenu(getActivity());
		popMenu.addItems(new String[] { "����", "��������", "�ҵ�����" });
		popMenu.setOnItemClickListener(new com.luluandroid.miyouplus.ui.PopMenu.OnItemClickListener() {

			@Override
			public void onItemClick(int index) {
				// TODO Auto-generated method stub
				switch (index) {
				case 0:
					linkToNewMibo();
					break;
				default:
					break;
				}
				Toast.makeText(getActivity(), "�����" + index + "��popmenuitem",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// ��ʼ��������ѯ�ز�
	private void getItems() {
		miboMgr.findinitialMibo(new FindMiboListener() {
			
			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub\
				for(Mibos mibo : mibosList){
					Mibos.add(mibo);
				}
				String successString = "��ʼ���ز���ѯ�ɹ�";
				Message message = mHandler.obtainMessage(ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				String errorString = "��ʼ���ز���ѯʧ�ܣ�"+"code:"+code+" "+"error��"+error;
				Message message = mHandler.obtainMessage(ChannelCodes.MIBO_Find_FAILURE, errorString);
				mHandler.sendMessage(message);
			}
		});
		/*List<Mibos> mibos = new ArrayList<Mibos>();
		Mibos mibo;
		for (int i = 0; i < 10; i++) {
			mibo = new Mibos("�۹���", "��ǰ��" + i + "��С���ѣ����ܹ�" + "\ue32d", i);
			mibos.add(mibo);
		}
		return mibos;*/
	}

	private void initAdpter() {
		getItems();
		miBoAdapter = new MiBoAdapter(getActivity(), Mibos,miboMgr);
		mListView.setAdapter(miBoAdapter);
	}
	
	private void initBroadcastReceiver() {
		if (lbm == null)
			lbm = LocalBroadcastManager.getInstance(getActivity());
		if (mFragmentAllMiboReceiver == null)
			mFragmentAllMiboReceiver = new FragmentAllMiboReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastString.ACTION_FRAGMENT_ALLMIBO_LISTVIEW_TOP);
		lbm.registerReceiver(mFragmentAllMiboReceiver, filter);

	}
	
	private void stopBroadcastReceiver() {
		if (lbm != null)
			lbm.unregisterReceiver(mFragmentAllMiboReceiver);
	}

	private void linkToNewMibo() {
		ShowToast("����");
		Intent intent = new Intent(getActivity(), CreateTieziActivity.class);
		startActivityForResult(intent, ChannelCodes.CREATE_TIEZI);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ChannelCodes.CREATE_TIEZI_SUCCESS:
			ShowToast("�����ɹ�");
			break;
		case ChannelCodes.CREATE_TIEZI_FAIL:
			ShowToast("����ʧ��");
			break;
		default:
			break;
		}

	}

	public class FragmentAllMiboReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					BroadcastString.ACTION_FRAGMENT_ALLMIBO_LISTVIEW_TOP)) {
				mListView.setSelection(0);
			}
		}

	}

	/**
	 * onRefresh ���� onLordMore �������������Ҫֹͣ���µķ���
	 */
	private void AfterLoaded() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	/*
	 * ������ݺ�һ��Ҫ��onLoad()����������ˢ�»�һֱ���У�����ͣ������ (non-Javadoc)
	 * 
	 * @see
	 * com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		// ����
		loadLatestMibo();
	}
	
	private boolean isAddOk(boolean NewOrOld,Mibos srcmibo,Mibos newmibo){
		if(NewOrOld){
			long srcTime = TimeUtil.stringToLong(srcmibo.getCreatedAt(), TimeUtil.FORMAT_DATE_TIME2);
			long newTime = TimeUtil.stringToLong(newmibo.getCreatedAt(), TimeUtil.FORMAT_DATE_TIME2);
			return srcTime < newTime;
		}else{
			long srcTime = TimeUtil.stringToLong(srcmibo.getCreatedAt(), TimeUtil.FORMAT_DATE_TIME2);
			long newTime = TimeUtil.stringToLong(newmibo.getCreatedAt(), TimeUtil.FORMAT_DATE_TIME2);
			return srcTime > newTime;
		}
	}
	
	private void loadLatestMibo(){
		miboMgr.findCacheOrNetWorkMibo(false, true, new FindMiboListener() {
			
			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub
				miBoAdapter.clear();
				miBoAdapter.addAll(mibosList);
				String successString = "������"+mibosList.size()+"�����ز�";
				Message message = mHandler.obtainMessage(ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				String successString = "�����������ز�ʧ��:"+"code:"+code+"error:"+error;
				Message message = mHandler.obtainMessage(ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
		});
	}
	
	private void loadOldMibo(){
			miboMgr.findCacheOrNetWorkMibo(false, false, new FindMiboListener() {
			
			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub
				int count = 0;
				Mibos srcMibo = Mibos.get(Mibos.size()-1);
				for(Mibos mibo : mibosList){
					if(isAddOk(false,srcMibo,mibo)){
						miBoAdapter.addItem(mibo);
						count++;
					}
				}
				String successString = "������"+count+"�����ز�";
				Message message = mHandler.obtainMessage(ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				String successString = "�����������ز�ʧ��:"+"code:"+code+"error:"+error;
				Message message = mHandler.obtainMessage(ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
		});
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		// ����
		loadOldMibo();
	}


}