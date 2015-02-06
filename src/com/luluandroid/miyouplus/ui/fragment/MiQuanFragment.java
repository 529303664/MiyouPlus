package com.luluandroid.miyouplus.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.MiBoAdapter;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.config.BroadcastString;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;
import com.luluandroid.miyouplus.ui.FragmentBase;
import com.luluandroid.miyouplus.ui.PopMenu;
import com.luluandroid.miyouplus.view.xlist.XListView;
import com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener;

public class MiQuanFragment extends FragmentBase implements IXListViewListener {

	private PopMenu popMenu;
	private TextView title;
	private XListView mListView;
	private MiBoAdapter miBoAdapter;
	private FragmentAllMiboReceiver mFragmentAllMiboReceiver;
	private LocalBroadcastManager lbm;

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

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initBroadcastReceiver();
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
		}

	};

	private void init() {
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
		popMenu.addItems(new String[] { "����", "�������", "ɨһɨ" });
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

	// �����õ�list����
	private List<Mibos> getItems() {
		List<Mibos> mibos = new ArrayList<Mibos>();
		Mibos mibo;
		for (int i = 0; i < 10; i++) {
			mibo = new Mibos("�۹���", "��ǰ��" + i + "��С���ѣ����ܹ�" + "\ue32d", i);
			mibos.add(mibo);
		}
		return mibos;
	}

	private void initAdpter() {
		miBoAdapter = new MiBoAdapter(getActivity(), getItems());
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
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AfterLoaded();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		// ����
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AfterLoaded();
			}
		}, 1000);
	}

}
