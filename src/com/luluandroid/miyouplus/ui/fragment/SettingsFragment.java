package com.luluandroid.miyouplus.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.main.CustomApplcation;
import com.luluandroid.miyouplus.ui.BlackListActivity;
import com.luluandroid.miyouplus.ui.EditMyMiboActivity;
import com.luluandroid.miyouplus.ui.FeedbackActivity;
import com.luluandroid.miyouplus.ui.FragmentBase;
import com.luluandroid.miyouplus.ui.LoginActivity;
import com.luluandroid.miyouplus.ui.SetMyInfoActivity;
import com.luluandroid.miyouplus.util.SharePreferenceUtil;

/**
 * 设置
 * 
 * @ClassName: SetFragment
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午1:00:27
 */
@SuppressLint("SimpleDateFormat")
public class SettingsFragment extends FragmentBase implements OnClickListener{

	EditText miyou_password_editText,clear_miyou_password_editText;
	Button btn_logout,miyou_password_button,clear_miyou_password_button;
	TextView tv_set_name,miyou_password_textView;
	RelativeLayout layout_info,layout_clear_miyou_password, rl_switch_miyou_password,rl_set_miyoupassword,rl_switch_notification, rl_switch_voice,
	rl_switch_vibrate,layout_blacklist,layout_mymibo,layout_mycomment,layout_myfeedback;

	ImageView iv_open_miyou_password,iv_close_miyou_password,iv_open_notification, iv_close_notification, iv_open_voice,
			iv_close_voice, iv_open_vibrate, iv_close_vibrate;
	
	View view1,view2,view3;
	SharePreferenceUtil mSharedUtil;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mSharedUtil = mApplication.getSpUtil();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_set, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		initTopBarForOnlyTitle("设置");
		//黑名单列表
		layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);
		layout_info = (RelativeLayout) findViewById(R.id.layout_info);
		rl_set_miyoupassword = (RelativeLayout) findViewById(R.id.rl_set_miyoupassword);
		rl_switch_miyou_password = (RelativeLayout) findViewById(R.id.rl_switch_miyou_password);
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		layout_mymibo = (RelativeLayout) findViewById(R.id.layout_mymibo);
		layout_mycomment = (RelativeLayout) findViewById(R.id.layout_mycomment);
		layout_myfeedback = (RelativeLayout) findViewById(R.id.layout_myfeedback);
		layout_clear_miyou_password = (RelativeLayout) findViewById(R.id.layout_clear_miyou_password);
		layout_clear_miyou_password.setOnClickListener(this);
		rl_switch_miyou_password.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_voice.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		layout_mymibo.setOnClickListener(this);
		layout_mycomment.setOnClickListener(this);
		layout_myfeedback.setOnClickListener(this);

		clear_miyou_password_editText = (EditText)findViewById(R.id.clear_miyou_password_editText);
		miyou_password_editText = (EditText)findViewById(R.id.miyou_password_editText);
		iv_open_miyou_password = (ImageView) findViewById(R.id.iv_open_miyou_password);
		iv_close_miyou_password = (ImageView) findViewById(R.id.iv_close_miyou_password);
		iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
		iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
		iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
		iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
		iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
		iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);
		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);
		view3 = (View) findViewById(R.id.view3);

		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		miyou_password_textView = (TextView)findViewById(R.id.miyou_password_textView);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		clear_miyou_password_button = (Button)findViewById(R.id.clear_miyou_password_button);
		miyou_password_button = (Button)findViewById(R.id.miyou_password_button);
		clear_miyou_password_button.setOnClickListener(this);
		miyou_password_button.setOnClickListener(this);

		// 初始化
		boolean isAllowSelfPassword = mSharedUtil.isAllowSelfPassword();
		if (isAllowSelfPassword) {
			iv_open_miyou_password.setVisibility(View.VISIBLE);
			iv_close_miyou_password.setVisibility(View.INVISIBLE);
		} else {
			iv_open_notification.setVisibility(View.INVISIBLE);
			iv_close_notification.setVisibility(View.VISIBLE);
		}
		boolean isAllowNotify = mSharedUtil.isAllowPushNotify();
		if (isAllowNotify) {
			iv_open_notification.setVisibility(View.VISIBLE);
			iv_close_notification.setVisibility(View.INVISIBLE);
		} else {
			iv_open_notification.setVisibility(View.INVISIBLE);
			iv_close_notification.setVisibility(View.VISIBLE);
		}
		boolean isAllowVoice = mSharedUtil.isAllowVoice();
		if (isAllowVoice) {
			iv_open_voice.setVisibility(View.VISIBLE);
			iv_close_voice.setVisibility(View.INVISIBLE);
		} else {
			iv_open_voice.setVisibility(View.INVISIBLE);
			iv_close_voice.setVisibility(View.VISIBLE);
		}
		boolean isAllowVibrate = mSharedUtil.isAllowVibrate();
		if (isAllowVibrate) {
			iv_open_vibrate.setVisibility(View.VISIBLE);
			iv_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_open_vibrate.setVisibility(View.INVISIBLE);
			iv_close_vibrate.setVisibility(View.VISIBLE);
		}
		
		isEmptyMiyouPassword();
		
		btn_logout.setOnClickListener(this);
		layout_info.setOnClickListener(this);
		layout_blacklist.setOnClickListener(this);
		
		clear_miyou_password_editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.toString().trim().length()<4){
					clear_miyou_password_button.setClickable(false);
				}else{
					clear_miyou_password_button.setClickable(true);
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
		
		miyou_password_editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.toString().trim().length()<4){
					miyou_password_button.setClickable(false);
				}else{
					miyou_password_button.setClickable(true);
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
	}

	private void initData() {
		tv_set_name.setText(BmobUserManager.getInstance(getActivity())
				.getCurrentUser().getUsername());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_blacklist:// 启动到黑名单页面
			startAnimActivity(new Intent(getActivity(),BlackListActivity.class));
			break;
		case R.id.layout_info:// 启动到个人资料页面
			Intent intent =new Intent(getActivity(),SetMyInfoActivity.class);
			intent.putExtra("from", "me");
			startActivity(intent);
			break;
		case R.id.btn_logout:
			CustomApplcation.getInstance().logout();
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.layout_mymibo:
			intent = new Intent(getActivity(),EditMyMiboActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("isMiboOrComment", true);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.layout_mycomment:
			intent = new Intent(getActivity(),EditMyMiboActivity.class);
			bundle = new Bundle();
			bundle.putBoolean("isMiboOrComment", false);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.layout_myfeedback:
			intent = new Intent(getActivity(),FeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_clear_miyou_password:
			actToClearPwd();
			hideSoftInputView();
			break;
		case R.id.clear_miyou_password_button:
			writeToClearPwd();
			break;
		case R.id.rl_switch_notification:
			if (iv_open_notification.getVisibility() == View.VISIBLE) {
				iv_open_notification.setVisibility(View.INVISIBLE);
				iv_close_notification.setVisibility(View.VISIBLE);
				mSharedUtil.setPushNotifyEnable(false);
				rl_switch_vibrate.setVisibility(View.GONE);
				rl_switch_voice.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.GONE);
			} else {
				iv_open_notification.setVisibility(View.VISIBLE);
				iv_close_notification.setVisibility(View.INVISIBLE);
				mSharedUtil.setPushNotifyEnable(true);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				rl_switch_voice.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
			}

			break;
		case R.id.rl_switch_voice:
			if (iv_open_voice.getVisibility() == View.VISIBLE) {
				iv_open_voice.setVisibility(View.INVISIBLE);
				iv_close_voice.setVisibility(View.VISIBLE);
				mSharedUtil.setAllowVoiceEnable(false);
			} else {
				iv_open_voice.setVisibility(View.VISIBLE);
				iv_close_voice.setVisibility(View.INVISIBLE);
				mSharedUtil.setAllowVoiceEnable(true);
			}

			break;
		case R.id.rl_switch_vibrate:
			if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_open_vibrate.setVisibility(View.INVISIBLE);
				iv_close_vibrate.setVisibility(View.VISIBLE);
				mSharedUtil.setAllowVibrateEnable(false);
			} else {
				iv_open_vibrate.setVisibility(View.VISIBLE);
				iv_close_vibrate.setVisibility(View.INVISIBLE);
				mSharedUtil.setAllowVibrateEnable(true);
			}
			break;
		case R.id.rl_switch_miyou_password:
			rl_set_miyoupassword.setVisibility(View.VISIBLE);
			view3.setVisibility(View.VISIBLE);
			break;
		case R.id.miyou_password_button:
			writeToPassword();
			hideSoftInputView();
			break;
			default:
				break;
		}
	}
	
	private void writeToClearPwd(){
		if(isCorrcetPwd(clear_miyou_password_editText.getText().toString().trim())){
			mSharedUtil.setMiYouPassword("");
			mSharedUtil.setAllowSelfPasswordEnable(false);
			iv_open_miyou_password.setVisibility(View.INVISIBLE);
			iv_close_miyou_password.setVisibility(View.VISIBLE);
			ShowToast("清除密码成功");
		}else{
			ShowToast("清除密码失败："+"密码不正确！");
		}
		clear_miyou_password_editText.setText("");
		clear_miyou_password_button.setVisibility(View.INVISIBLE);
		clear_miyou_password_editText.setVisibility(View.INVISIBLE);
	}
	
	private void actToClearPwd(){
		if(isEmptyMiyouPassword()){
			ShowToast("无设置密码");
		}else{
			clear_miyou_password_button.setVisibility(View.VISIBLE);
			clear_miyou_password_editText.setVisibility(View.VISIBLE);
		}
		miyou_password_editText.setText("");
		rl_set_miyoupassword.setVisibility(View.GONE);
		view3.setVisibility(View.GONE);
	}
	
	private boolean isEmptyMiyouPassword(){
		String password = mSharedUtil.getMiyouPassword();
		if(null == password || "".equals(password)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isCorrcetPwd(String writedPwd){
		String password = mSharedUtil.getMiyouPassword();
		return password.equals(writedPwd);
	}
	
	private void writeToPassword(){
		if(TextUtils.isEmpty(miyou_password_editText.getText().toString().trim())){
			ShowToast("密码不能为空");
			return;
		}
		if(isEmptyMiyouPassword()){
			mSharedUtil.setMiYouPassword(miyou_password_editText.getText().toString().trim());
			ShowToast(miyou_password_editText.getText().toString().trim());
			mSharedUtil.setAllowSelfPasswordEnable(true);
			iv_open_miyou_password.setVisibility(View.VISIBLE);
			iv_close_miyou_password.setVisibility(View.INVISIBLE);
			ShowToast("添加密码成功！！");
		}else{
			if(isCorrcetPwd(miyou_password_editText.getText().toString().trim())){
				mSharedUtil.setMiYouPassword(miyou_password_editText.getText().toString().trim());
				ShowToast(miyou_password_editText.getText().toString().trim());
				if(mSharedUtil.isAllowSelfPassword()){
					mSharedUtil.setAllowSelfPasswordEnable(false);
					iv_open_miyou_password.setVisibility(View.INVISIBLE);
					iv_close_miyou_password.setVisibility(View.VISIBLE);
					ShowToast("密码正确,设置已变更！！");
				}else{
					mSharedUtil.setAllowSelfPasswordEnable(true);
					iv_open_miyou_password.setVisibility(View.VISIBLE);
					iv_close_miyou_password.setVisibility(View.INVISIBLE);
				}
			}else{
				ShowToast("错误密码！！");
			}
		}
		miyou_password_editText.setText("");
		rl_set_miyoupassword.setVisibility(View.GONE);
		view3.setVisibility(View.GONE);
		
		clear_miyou_password_editText.setText("");
		clear_miyou_password_button.setVisibility(View.INVISIBLE);
		clear_miyou_password_editText.setVisibility(View.INVISIBLE);
	}
	
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
