package com.luluandroid.miyouplus.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.config.BroadcastString;
import com.luluandroid.miyouplus.ui.FragmentBase;
import com.luluandroid.miyouplus.ui.MainActivity;
import com.luluandroid.miyouplus.util.SharePreferenceUtil;

public class MiyouPwdFragment extends FragmentBase implements OnClickListener{

	private LocalBroadcastManager lbm;
	
	private EditText passwordEditText;
	private Button confirmBtn,cancelBtn;
	
	private SharePreferenceUtil mShareUtil;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initLbm();
		initView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_miyoupwd, container, false);
	}
	
	private void initLbm(){
		lbm = LocalBroadcastManager.getInstance(getActivity());
	}
	
	private void initView(){
		mShareUtil = mApplication.getSpUtil();
		passwordEditText = (EditText)findViewById(R.id.fragment_miyoupwd_editText);
		confirmBtn = (Button)findViewById(R.id.fragment_miyoupwd_accept);
		cancelBtn = (Button)findViewById(R.id.fragment_miyoupwd_cancel);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		confirmBtn.setClickable(false);
		passwordEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.toString().trim().length()<4){
					confirmBtn.setClickable(false);
				}else{
					confirmBtn.setClickable(true);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int vid = v.getId();
		switch(vid){
			case R.id.fragment_miyoupwd_accept:
				WriteToPwd();
				break;
			case R.id.fragment_miyoupwd_cancel:
				passwordEditText.setText("");
				lbm.sendBroadcast(new Intent(BroadcastString.ACTION_WRITE_DOWN_INCORRECT_PWD));
				break;
			default:
				break;
		}
	}
	
	private void WriteToPwd(){
		if(mShareUtil.getMiyouPassword().equals(passwordEditText.getText().toString().trim())){
			passwordEditText.setText("");
			lbm.sendBroadcast(new Intent(BroadcastString.ACTION_WRITE_DOWN_CORRECT_PWD));
		}else{
			passwordEditText.setText("");
			lbm.sendBroadcast(new Intent(BroadcastString.ACTION_WRITE_DOWN_INCORRECT_PWD));
		}
	}
	
}
