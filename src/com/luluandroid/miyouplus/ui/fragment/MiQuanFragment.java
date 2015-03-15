package com.luluandroid.miyouplus.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.luluandroid.miyouplus.control.MiboMgr.UpdateZanListener;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.interfaces.SearchAfterTouchListener;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;
import com.luluandroid.miyouplus.ui.EditMyMiboActivity;
import com.luluandroid.miyouplus.ui.FragmentBase;
import com.luluandroid.miyouplus.ui.LoginActivity;
import com.luluandroid.miyouplus.ui.MainActivity;
import com.luluandroid.miyouplus.ui.PopMenu;
import com.luluandroid.miyouplus.util.TimeUtil;
import com.luluandroid.miyouplus.view.MySearchEditText;
import com.luluandroid.miyouplus.view.xlist.XListView;
import com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class MiQuanFragment extends FragmentBase implements IXListViewListener {

	private int curPage = 0;
	private PopMenu popMenu;
	private TextView title;
	private XListView mListView;
	private MiBoAdapter miBoAdapter;
	private FragmentAllMiboReceiver mFragmentAllMiboReceiver;
	private LocalBroadcastManager lbm;
	private List<Mibos> Mibos;
	private MiboMgr miboMgr;
	private MySearchEditText mySearchEditText;
	private Button miquan_recovery_btn1, miquan_recovery_btn2,
			miquan_recovery_btn3, miquan_recovery_btn4;
	private LinearLayout fragment_miquan_recovery_layout;
	private List<String> tagList = new ArrayList<String>();

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
		BmobUserManager userManager = BmobUserManager
				.getInstance(getActivity());
		if (userManager.getCurrentUser() == null) {
			ShowToast("您的账号已在其他设备上登录!");
			startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().finish();
		}
	}

	/**
	 * 隐藏软键盘 hideSoftInputView
	 * 
	 * @Title: hideSoftInputView
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) getActivity()
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

	// 创建handler 用于控制UI
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case ChannelCodes.MIBO_Find_SUCCESS:
				ShowToast((String) msg.obj);
				((MainActivity) getActivity()).setMiquanTipsVisble(false);
				AfterLoaded();
				break;
			case ChannelCodes.MIBO_Find_FAILURE:
				ShowToast((String) msg.obj);
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
		initTopBarForOnlyTitle("秘圈");
		title = mHeaderLayout.getmHtvSubTitle();
		System.out.println("title 是:" + title);
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
		initSearchView();
		initSearchLayout();
	}

	private void initSearchView() {
		mySearchEditText = (MySearchEditText) findViewById(R.id.fragment_miquan_searchview);
		mySearchEditText
				.setSearchAfterTouchListener(new SearchAfterTouchListener() {

					@Override
					public void doSomeThing() {
						// TODO Auto-generated method stub
						tagList.clear();
						ShowToast("搜索:" + mySearchEditText.getText().toString());
						tagList.addAll(Arrays.asList(mySearchEditText.getText()
								.toString().replaceAll("(\\s\\s)+", " ")
								.split(" ")));
						mySearchEditText.setText("");
						hideSoftInputView();
						showOrHideMySearchView();
						loadLatestMibo();
					}
				});
	}

	private void initSearchLayout() {
		fragment_miquan_recovery_layout = (LinearLayout) getActivity()
				.findViewById(R.id.fragment_miquan_recovery_layout);
		miquan_recovery_btn1 = (Button) getActivity().findViewById(
				R.id.miquan_recovery_btn1);
		miquan_recovery_btn2 = (Button) getActivity().findViewById(
				R.id.miquan_recovery_btn2);
		miquan_recovery_btn3 = (Button) getActivity().findViewById(
				R.id.miquan_recovery_btn3);
		miquan_recovery_btn4 = (Button) getActivity().findViewById(
				R.id.miquan_recovery_btn4);
		miquan_recovery_btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tagList.clear();
				tagList.add(miquan_recovery_btn1.getText().toString().substring(1).trim());
				loadLatestMibo();
				showOrHideMySearchView();
			}
		});
		miquan_recovery_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tagList.clear();
				tagList.add(miquan_recovery_btn2.getText().toString().substring(1).trim());
				loadLatestMibo();
				showOrHideMySearchView();
			}
		});
		miquan_recovery_btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tagList.clear();
				tagList.add(miquan_recovery_btn3.getText().toString().substring(1).trim());
				loadLatestMibo();
				showOrHideMySearchView();
			}
		});
		miquan_recovery_btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tagList.clear();
				tagList.add(miquan_recovery_btn4.getText().toString().substring(1).trim());
				loadLatestMibo();
				showOrHideMySearchView();
			}
		});
	}

	private void initXListView() {
		mListView = (XListView) getActivity().findViewById(
				R.id.fragment_miquan_listview);
		// 允许上拉刷新
		mListView.setPullLoadEnable(true);
		// 允许下拉
		mListView.setPullRefreshEnable(true);
		// 设置监听器
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();
	}

	private void initPopMenu() {
		popMenu = new PopMenu(getActivity());
		popMenu.addItems(new String[] { "发帖", "发现", "我的秘博", "我的评论" });
		popMenu.setOnItemClickListener(new com.luluandroid.miyouplus.ui.PopMenu.OnItemClickListener() {

			@Override
			public void onItemClick(int index) {
				// TODO Auto-generated method stub
				switch (index) {
				case 0:
					linkToNewMibo();
					break;
				case 1:
					showOrHideMySearchView();
					break;
				case 2:
					linkToMyMiboOrComment(true);
					break;
				case 3:
					linkToMyMiboOrComment(false);
					break;
				default:
					break;
				}
				/*
				 * Toast.makeText(getActivity(), "点击第" + index + "个popmenuitem",
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		});
	}

	// 初始化操作查询秘博
	private void getItems() {

		miboMgr.findNetWorkMibo(curPage, new FindMiboListener() {

			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub
				curPage++;
				miBoAdapter.clear();
				miBoAdapter.addAll(mibosList);
				String successString = "查找了" + mibosList.size() + "条新秘博";
				Message message = mHandler.obtainMessage(
						ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				String successString = "查找了最新秘博失败:" + "code:" + code + "error:"
						+ error;
				Message message = mHandler.obtainMessage(
						ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
		});

	}

	private void initAdpter() {
		// getItems();
		miBoAdapter = new MiBoAdapter(getActivity(), Mibos, miboMgr);
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

	public void addMySearchViewContent(String content) {
		mySearchEditText.setText(content + "  "
				+ mySearchEditText.getText().toString());
		if (mySearchEditText.getVisibility() == View.GONE) {
			mySearchEditText.setVisibility(View.VISIBLE);
		}
	}

	private void showOrHideMySearchView() {
		if (mySearchEditText.getVisibility() == View.GONE) {
			mySearchEditText.setVisibility(View.VISIBLE);
			fragment_miquan_recovery_layout.setVisibility(View.VISIBLE);
		} else {
			mySearchEditText.setVisibility(View.GONE);
			fragment_miquan_recovery_layout.setVisibility(View.GONE);
		}
	}

	private void linkToMyMiboOrComment(boolean miboOrComment) {
		Intent intent = new Intent(getActivity(), EditMyMiboActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("isMiboOrComment", miboOrComment);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void linkToNewMibo() {
		ShowToast("发帖");
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
			ShowToast("发帖成功");
			break;
		case ChannelCodes.CREATE_TIEZI_FAIL:
			ShowToast("发帖失败");
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
	 * onRefresh 或者 onLordMore 方法加载完后需要停止更新的方法
	 */
	private void AfterLoaded() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	/*
	 * 或的数据后一定要加onLoad()方法，否则刷新会一直进行，根本停不下来 (non-Javadoc)
	 * 
	 * @see
	 * com.luluandroid.miyouplus.view.xlist.XListView.IXListViewListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		// 测试
		startloadAllLatestMibo();
	}

	private void startloadAllLatestMibo() {
		tagList.clear();
		loadLatestMibo();
	}

	private void loadLatestMibo() {
		curPage = 0;
		miboMgr.findTagRelatedMibo(tagList, curPage, new FindMiboListener() {

			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub
				String successString;
				if (mibosList.size() > 0) {
					miBoAdapter.clear();
					miBoAdapter.addAll(mibosList);
					curPage = 1;
					successString = "已屏蔽非法信息\n"+"查找了" + mibosList.size() + "条新秘博";
				} else {
					successString = "沒有数据了";
				}
				Message message = mHandler.obtainMessage(
						ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);

			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				String successString = "查找了最新秘博失败:" + "code:" + code + "error:"
						+ error;
				Message message = mHandler.obtainMessage(
						ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
		});
	}

	private void startloadAllOldMibo() {
		tagList.clear();
		loadOldMibo();
	}

	private void loadOldMibo() {
		miboMgr.findTagRelatedMibo(tagList, curPage, new FindMiboListener() {

			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub
				String successString;
				if (mibosList.size() > 0) {
					miBoAdapter.addAll(mibosList);
					curPage++;
					successString = "加载了" + mibosList.size() + "条秘博";
				} else {
					successString = "没有更多数据了";
				}

				Message message = mHandler.obtainMessage(
						ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				String successString = "查找了最新秘博失败:" + "code:" + code + "error:"
						+ error;
				Message message = mHandler.obtainMessage(
						ChannelCodes.MIBO_Find_SUCCESS, successString);
				mHandler.sendMessage(message);
			}
		});
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (tagList == null || tagList.isEmpty()) {
			startloadAllOldMibo();
		} else {
			loadOldMibo();
		}
	}

}
