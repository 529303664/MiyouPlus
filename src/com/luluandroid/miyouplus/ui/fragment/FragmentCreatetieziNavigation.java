package com.luluandroid.miyouplus.ui.fragment;

import com.luluandroid.miyouplus.R;

import com.luluandroid.miyouplus.config.Conf;
import com.luluandroid.miyouplus.ui.CreateTieziActivity;
import com.luluandroid.miyouplus.util.ImageManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentCreatetieziNavigation extends Fragment {

	private Context context;
	private TextView takePhoto, gallary, resourceModel, adjustEffect;

	public FragmentCreatetieziNavigation(Context context) {
		super();
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_create_tiezi_navigation,
				container, false);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();

	}

	private void initView() {
		takePhoto = (TextView) getActivity().findViewById(
				R.id.create_tiezi_takephoto);
		gallary = (TextView) getActivity().findViewById(
				R.id.create_tiezi_gallary);
		resourceModel = (TextView) getActivity().findViewById(
				R.id.create_tiezi_model);
		adjustEffect = (TextView) getActivity().findViewById(
				R.id.create_tiezi_ps);
		resourceModel = (TextView) getActivity().findViewById(
				R.id.create_tiezi_model);
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ImageManager.pictureCrop(getActivity(), Conf.TAKE_PICTURE);
				((CreateTieziActivity)getActivity()).newWayTakePic();
			}
		});
		gallary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ImageManager.pictureCrop(getActivity(), Conf.CHOOSE_PICTURE);
				((CreateTieziActivity)getActivity()).newWayChoosePic();
			}
		});
		adjustEffect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((CreateTieziActivity) getActivity())
						.switchToChangeBarFragment();

			}
		});
		resourceModel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((CreateTieziActivity) getActivity()).switchToMouldFragment();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
