package com.luluandroid.miyouplus.ui.fragment;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.adapter.MouldAdapter;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;
import com.luluandroid.miyouplus.view.MyGridView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentImageMould extends Fragment {

	private Context context;
	private TextView returnTextView;
	private MyGridView mGridView;
	
			
	public static final Integer[] mThumbIds = {
			R.drawable.s_1,R.drawable.s_2,R.drawable.s_3,R.drawable.s_4,R.drawable.s_5,R.drawable.s_6,
			R.drawable.s_7,R.drawable.s_8,R.drawable.s_9,R.drawable.s_10,R.drawable.s_11,R.drawable.s_12,
			R.drawable.s_13,R.drawable.s_14,R.drawable.s_15,R.drawable.s_16,R.drawable.s_17,R.drawable.s_18,
			R.drawable.s_19,R.drawable.s_20,R.drawable.s_21,R.drawable.s_22,R.drawable.s_23,R.drawable.s_24,
			R.drawable.s_25,R.drawable.s_26,R.drawable.s_27,R.drawable.s_28,R.drawable.s_29,R.drawable.s_30,
	};
	public static final Integer[] mBackGroundIds = {
			R.drawable.l_1,R.drawable.l_2,R.drawable.l_3,R.drawable.l_4,R.drawable.l_5,R.drawable.l_6,
			R.drawable.l_7,R.drawable.l_8,R.drawable.l_9,R.drawable.l_10,R.drawable.l_11,R.drawable.l_12,
			R.drawable.l_13,R.drawable.l_14,R.drawable.l_15,R.drawable.l_16,R.drawable.l_17,R.drawable.l_18,
			R.drawable.l_19,R.drawable.l_20,R.drawable.l_21,R.drawable.l_22,R.drawable.l_23,R.drawable.l_24,
			R.drawable.l_25,R.drawable.l_26,R.drawable.l_27,R.drawable.l_28,R.drawable.l_29,R.drawable.l_30,
	};
	public FragmentImageMould(Context context){
		super();
		this.context = context;
	}
		
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_create_tiezi_mould, container, false);
		return view;
	}

	private void initView(){
		returnTextView = (TextView)getActivity().findViewById(R.id.create_tiezi_mould_return);
		mGridView = (MyGridView)getActivity().findViewById(R.id.create_tiezi_gridview);
		if(mGridView==null){
			System.out.println("mGridView为空");
		}
		mGridView.setAdapter(new MouldAdapter(getActivity(),mThumbIds));
		mGridView.requestFocus();
		mGridView.smoothScrollToPosition(0);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//点击不同的素材模板给发帖模块背景进行配色 
				((CreateTieziActivity)getActivity()).setPicchange(false);
				((ImageView)getActivity().findViewById(R.id.create_tiezi_imageview1)).setImageResource(mBackGroundIds[position]);
				((CreateTieziActivity)getActivity()).setCurrentResId(position);
				Toast.makeText(context, ""+position,Toast.LENGTH_SHORT).show();//显示信息;
			}
		});
		returnTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((CreateTieziActivity)getActivity()).switchToNavigationFragment();
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	
	
}
