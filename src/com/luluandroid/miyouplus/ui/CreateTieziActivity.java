package com.luluandroid.miyouplus.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.bean.DBMgr;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.bean.User;
import com.luluandroid.miyouplus.config.BmobConstants;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.main.CustomApplcation;
import com.luluandroid.miyouplus.ui.LocationActivity.BaiduReceiver;
import com.luluandroid.miyouplus.ui.fragment.FragmentCreatetieziChangeBar;
import com.luluandroid.miyouplus.ui.fragment.FragmentCreatetieziNavigation;
import com.luluandroid.miyouplus.ui.fragment.FragmentImageMould;
import com.luluandroid.miyouplus.util.FileManager;
import com.luluandroid.miyouplus.util.ImageManager;
import com.luluandroid.miyouplus.util.ImageTools;
import com.luluandroid.miyouplus.util.PhotoUtil;
import com.luluandroid.miyouplus.view.dialog.DialogProgress;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class CreateTieziActivity extends ActionBarActivity implements
		EmojiconGridFragment.OnEmojiconClickedListener,
		EmojiconsFragment.OnEmojiconBackspaceClickedListener {

	private ActionBar actionbar;
	private MenuItem action_create_tiezi_item; // actionbar ������ť
	private Fragment currentFragment, emotionFragment;
	private FragmentImageMould MouldFragment;
	private FragmentCreatetieziNavigation navigationFrament;
	private FragmentCreatetieziChangeBar changeBarFragment;
	private ProgressBar progressBar;
	private FrameLayout navigationFrameLayout;
	private ImageView navigationLfBt, navigationRtBt, imageBackgroud;
	private EditText myMessageEditext,myTagEditText;
	private TextView emotionText;
	private CheckBox contactCheckBox,CommentCheckBox;
	private boolean NvIsOpen;// �ֱ����жϵ����Ƿ����ʹ���ͼƬ��Asynctask�Ƿ���������
	private DialogProgress progressDialog;
	private int currentResId = 1;//��ǰѡ����ͼƬ��ԴID
	
	private boolean isNetWork = true;
	private BaiduReceiver mBaiduReceiver;// ע��㲥�����������ڼ��������Լ���֤key
	public LocationClient mLocationClient = null;
	public BDLocationListener myLocationListener = null;
	
	private BmobUserManager userManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_tiezi);
		if (savedInstanceState == null) {
			navigationFrament = new FragmentCreatetieziNavigation(this);
			currentFragment = navigationFrament;
			getSupportFragmentManager().beginTransaction()
					.add(R.id.create_tiezi_container, navigationFrament)
					.commit();
		}
		
		userManager = BmobUserManager.getInstance(this);
		
		InitActionBar();
		initComponent();
		CustomApplcation.getInstance().addActivity(this);
		/*initSDKReceiver();
		initBaiduLBS();*/
	}

	private void initSDKReceiver(){
		// ע�� SDK �㲥������
				IntentFilter iFilter = new IntentFilter();
				iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
				iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
				mBaiduReceiver = new BaiduReceiver();
				registerReceiver(mBaiduReceiver, iFilter);
	}
	
	private void destroySDKReceiver(){
		// ȡ������ SDK �㲥
		unregisterReceiver(mBaiduReceiver);
	}
	
	private void initBaiduLBS(){
		mLocationClient = new LocationClient(this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
		initLocClient();
	}
	
	static BDLocation lastLocation = null;
	private void initLocClient(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);
		option.setOpenGps(false);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		option.setIsNeedAddress(false);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted()){
			if(isNetWork){
				mLocationClient.requestLocation();
			}else{
				mLocationClient.requestOfflineLocation();
			}
		}
		else
			BmobLog.i("locClient is null or not started");
	}
	
	/**
	 * 
	 */
	private void initComponent() {
		navigationLfBt = (ImageView) findViewById(R.id.create_tiezi_controlbar_left_button);
		navigationRtBt = (ImageView) findViewById(R.id.create_tiezi_controlbar_right_button);
		navigationFrameLayout = (FrameLayout) findViewById(R.id.create_tiezi_container);
		progressBar = (ProgressBar) findViewById(R.id.create_tiezi_progressBar1);
		imageBackgroud = (ImageView) findViewById(R.id.create_tiezi_imageview1);
		myMessageEditext = (EditText) findViewById(R.id.create_tiezi_input_editext);
		myTagEditText = (EditText) findViewById(R.id.create_tiezi_tag_edittext);
		emotionText = (TextView) findViewById(R.id.create_tiezi_emotions);
		contactCheckBox = (CheckBox)findViewById(R.id.create_tiezi_contact_checkbox);
		CommentCheckBox = (CheckBox)findViewById(R.id.create_tiezi_comment_checkbox);

		// ���ñ����fragment
		emotionFragment = (Fragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.emojicons);

		// �ȹرձ����fragment
		getSupportFragmentManager().beginTransaction().hide(emotionFragment)
				.commit();
		NvIsOpen = false;
		final Animation navigationEtAnimation = AnimationUtils.loadAnimation(
				this, R.anim.create_tiezi_navigation_enter);
		final Animation navigationExAnimation = AnimationUtils.loadAnimation(
				this, R.anim.create_tiezi_navigation_exit);
		navigationLfBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!NvIsOpen) {
					navigationLfBt.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.ic_action_collapse));
					navigationFrameLayout.startAnimation(navigationEtAnimation);
					navigationFrameLayout.setVisibility(View.VISIBLE);
					NvIsOpen = true;
				} else {
					navigationLfBt.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.ic_action_expand));
					navigationFrameLayout.startAnimation(navigationExAnimation);
					navigationFrameLayout.setVisibility(View.GONE);
					NvIsOpen = false;
				}
			}
		});
		navigationRtBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NvIsOpen = false;

			}
		});

		emotionText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (emotionFragment.isHidden()) {
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.left_in,
									R.anim.right_out).show(emotionFragment)
							.commit();
				} else {
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.left_in,
									R.anim.right_out).hide(emotionFragment)
							.commit();
				}
			}
		});

		myMessageEditext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(s)){
					action_create_tiezi_item.setIcon(R.drawable.ic_action_send_now);
					action_create_tiezi_item.setEnabled(false);
				}else{
					action_create_tiezi_item.setIcon(R.drawable.ic_action_un_send_now);
					action_create_tiezi_item.setEnabled(true);
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

	public void destroyBaiduLBS(){
		if(mLocationClient!=null && mLocationClient.isStarted()){
			mLocationClient.unRegisterLocationListener(myLocationListener);
			mLocationClient.stop();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		/*destroySDKReceiver();
		destroyBaiduLBS();*/
		recycleBitmap();
		super.onDestroy();
	}
	
	public void recycleBitmap(){
		if(tempBitmap != null && !tempBitmap.isRecycled()){
			tempBitmap.recycle();
		}
		if(cropBitmap != null && !cropBitmap.isRecycled()){
			cropBitmap.recycle();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_tiezi, menu);
		action_create_tiezi_item = menu.findItem(R.id.action_create_tiezi_write);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		switch (id) {
		case R.id.action_create_tiezi_write:
			if(!checkTagFinish()){
				return super.onOptionsItemSelected(item);
			}
			// ���ݷ���
			myHandler.sendEmptyMessage(ChannelCodes.DIALOG_SHOW);
			 saveAll();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void InitActionBar() {
		actionbar = getSupportActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(false);
	}

	public void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new DialogProgress(this, "���ڷ���,���Ժ󡭡�", "����", false, true);
		}
		progressDialog.show();
	}
	
	public void hideProgressDialog(){
		if(progressDialog == null){
			Log.e("CreateTieziActivity", "progressDialog Ϊ��,���ص���ʧ��");
			return;
		}
		progressDialog.dismiss();
	}
	
	
	
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case ChannelCodes.CREATE_TIEZI_SUCCESS:
				setResult(ChannelCodes.CREATE_TIEZI_SUCCESS);
				FileManager.getInstance().deleteFilesofSingleDir(BmobConstants.Miyou_Mibo_Pic_Path);
				hideProgressDialog();
				finish();
				break;
			case ChannelCodes.CREATE_TIEZI_FAIL:
				FileManager.getInstance().deleteFilesofSingleDir(BmobConstants.Miyou_Mibo_Pic_Path);
				hideProgressDialog();
				setResult(ChannelCodes.CREATE_TIEZI_FAIL);
				finish();
				break;
			case ChannelCodes.DIALOG_SHOW:
				showProgressDialog();
				break;
			case ChannelCodes.DIALOG_DISMISS:
				hideProgressDialog();
				break;
			default:
				break;
			}
		}

	};

	public void switchFragment(Fragment to) {
		if (currentFragment != to) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(currentFragment)
						.add(R.id.create_tiezi_container, to).commit();

			} else {
				transaction.hide(currentFragment).show(to).commit();
			}
			currentFragment = to;
		}
	}

	public void switchToChangeBarFragment() {
		if (changeBarFragment == null) {
			changeBarFragment = new FragmentCreatetieziChangeBar(this);
		}
		switchFragment(changeBarFragment);
	}

	public void switchToNavigationFragment() {
		if (navigationFrament == null) {
			navigationFrament = new FragmentCreatetieziNavigation(this);
		}
		switchFragment(navigationFrament);
	}

	public void switchToMouldFragment() {
		if (MouldFragment == null) {
			MouldFragment = new FragmentImageMould(this);
		}
		switchFragment(MouldFragment);
	}

	/*
	 * ����ɾ������
	 */
	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		EmojiconsFragment.backspace(myMessageEditext);
	}

	/*
	 * ����������
	 */
	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		EmojiconsFragment.input(myMessageEditext, emojicon);
	}

	// true��ʾ����ͼƬ���޸�,false��ʾʹ��appĬ�ϵ�ͼƬ
	private boolean picchange = false;

	/**
	 * true��ʾ����ͼƬ���޸�,false��ʾʹ��appĬ�ϵ�ͼƬ
	 * 
	 * @return
	 */
	public boolean isPicchange() {
		return picchange;
	}

	/**
	 * ����ͼƬ�Ƿ��иı�
	 * 
	 * @param picchange
	 */
	public void setPicchange(boolean picchange) {
		this.picchange = picchange;
	}

	private boolean checkTagFinish(){
		if(TextUtils.isEmpty(myTagEditText.getText().toString().trim())){
			ShowToast.showShortToast(this, "��ǩ����Ϊ�գ���");
			return false;
		}
		return true;
	}
	
	private void saveAll() {
		Mibos mibo = saveMibo();
		saveToNetWork(mibo);
	}

	private void saveToNetWork(Mibos mibo) {
		SavePicToNetWork(mibo);

	}

	private void SavePicToNetWork(final Mibos mibo) {
		if (isPicchange()) {
			// �Ƚ��б��ر���
			mibo.setLocalPicName(BmobUser.getCurrentUser(this).getUsername()
					+ System.currentTimeMillis());
			SaveLocalImage(mibo);
			// ���籣��
			final BmobFile bmobfile = new BmobFile(new File(BmobConstants.Miyou_Mibo_Pic_Path, mibo
					.getLocalPicName()+".jpg"));
			bmobfile.uploadblock(this, new UploadFileListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Log.i("CreateTieziActivity",
							"����ͼƬ�ϴ��ɹ�:"
									+ bmobfile
											.getFileUrl(CreateTieziActivity.this));
					SaveMiboToNetWork(mibo, bmobfile);
				}

				@Override
				public void onProgress(Integer value) {
					// TODO Auto-generated method stub
					super.onProgress(value);
				}

				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					Log.e("CreateTieziActivity", "����ͼƬ�ϴ�ʧ�ܣ�" + "code:" + code
							+ "\n" + "error:" + error);
					myHandler.sendEmptyMessage(ChannelCodes.CREATE_TIEZI_FAIL);
				}
			});
		} else {
			mibo.setPicResourceId(getCurrentResId());
			SaveMiboToNetWork(mibo, null);
		}
	}

	private void SaveMiboToNetWork(final Mibos mibo, BmobFile picFile) {
		if (picFile != null) {
			mibo.setPic(picFile);
		}
		mibo.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Log.i("mibo", "�ز����浽����ɹ�:" + mibo.getObjectId());
				addMiboToUser(mibo);
				// �����ѷ������ӵı��ر���
				 /*if(SaveLocal(mibo)){
					 myHandler.sendEmptyMessage(ChannelCodes.CREATE_TIEZI_SUCCESS);
				 }else{
					 myHandler.sendEmptyMessage(ChannelCodes.CREATE_TIEZI_FAIL);
				 }*/
			}

			@Override
			public void onFailure(int code, String arg1) {
				// TODO Auto-generated method stub
				Log.e("mibo", "�ز����浽����ʧ��:" + "code��" + code + " " + "error��"
						+ arg1);
				myHandler.sendEmptyMessage(ChannelCodes.CREATE_TIEZI_FAIL);
			}
		});
	}

	/**
	 * �����ѷ��͵����ӱ��ر���
	 */
	private boolean SaveLocal(Mibos mibo) {
		// TODO Auto-generated method stub
		return SaveLocalDataBase(mibo);
	}

	private Mibos saveMibo() {
		Mibos mibo = new Mibos(
				myMessageEditext.getText().toString(), Integer.valueOf("0"),((User) userManager.getCurrentUser(User.class)).getObjectId(),myTagEditText.getText().toString().trim());
		mibo.setOpentoAll(contactCheckBox.isChecked());
		mibo.setCommentOk(CommentCheckBox.isChecked());
		mibo.setLocation(new BmobGeoPoint(Double.valueOf(CustomApplcation.getInstance().getLongtitude()),Double.valueOf(CustomApplcation.getInstance().getLatitude())));
		/*if(lastLocation != null){
			mibo.setLocation(new BmobGeoPoint(lastLocation.getLongitude(), lastLocation.getLatitude()));
		}else{
			mibo.setLocation(new BmobGeoPoint(1,1));
		}*/
		return mibo;
	}

	private void addMiboToUser(Mibos mibo){
		BmobRelation mibosRelation = new BmobRelation();
		mibosRelation.add(mibo);
		((User) userManager.getCurrentUser(User.class)).setMiboRelation(mibosRelation);
		((User) userManager.getCurrentUser(User.class)).update(CreateTieziActivity.this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Log.i("CreateTieziActivity", "�����ز����û��ɹ�");
				
				myHandler.sendEmptyMessage(ChannelCodes.CREATE_TIEZI_SUCCESS);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				Log.i("CreateTieziActivity", "�����ز����û�ʧ�ܣ�"+"code:"+code+"error:"+error);
				myHandler.sendEmptyMessage(ChannelCodes.CREATE_TIEZI_FAIL);
				
			}
		});
	}
	/**
	 * �������ӵ����ݿ�
	 */
	private boolean SaveLocalDataBase(Mibos mibo) {
		// TODO Auto-generated method stub
		List<Mibos> miboList = new Vector<Mibos>();

		miboList.add(mibo);
		return DBMgr.getInstance(getApplicationContext()).addTiezi(miboList);
	}

	/**
	 * ����������Ƭ������
	 */
	Bitmap tempBitmap;
	private void SaveLocalImage(Mibos mibo) {
		// TODO Auto-generated method stub
		imageBackgroud.setDrawingCacheEnabled(true);
		tempBitmap = imageBackgroud.getDrawingCache();
		ImageTools.savePhotoToSDCard(tempBitmap, BmobConstants.Miyou_Mibo_Pic_Path, mibo
				.getLocalPicName(), ".jpg");
	}
	
	public String filePath = "";
	
	public void newWayTakePic(){
		File dir = new File(BmobConstants.Miyou_Mibo_Pic_Path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// ԭͼ
		File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss",Locale.getDefault())
				.format(new Date()));
		filePath = file.getAbsolutePath();// ��ȡ��Ƭ�ı���·��
		Uri imageUri = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent,
				BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
	}
	
	public void newWayChoosePic(){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent,
				BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
	}
	
	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}
	
	boolean isFromCamera = false;// ����������ת
	int degree = 0;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// �����޸�ͼƬ
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast.showShortToast(CreateTieziActivity.this, "SD������");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "���պ�ĽǶȣ�" + degree);
				startImageAction(Uri.fromFile(file), BmobConstants.CROP_PIC_WIDTH, BmobConstants.CROP_PIC_HEIGHT,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
				setPicchange(true);
			}
			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// �����޸�ͼƬ
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast.showShortToast(CreateTieziActivity.this,"SD������");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, BmobConstants.CROP_PIC_WIDTH, BmobConstants.CROP_PIC_HEIGHT,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
				setPicchange(true);
			} else {
				ShowToast.showShortToast(CreateTieziActivity.this,"��Ƭ��ȡʧ��");
			}

			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP:// �ü�ͷ�񷵻�
			// TODO sent to crop
			if (data == null) {
				ShowToast.showShortToast(this, "ȡ��ѡ��");
				return;
			} else {
				saveCropAvator(data);
				setPicchange(true);
			}
			// ��ʼ���ļ�·��
			filePath = "";
			break;
		default:
			break;

		}
	}
	String path;
	
	
	Bitmap cropBitmap;
	/**
	 * ����ü���ͷ��
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			cropBitmap = extras.getParcelable("data");
			if (cropBitmap != null) {
				if (isFromCamera && degree != 0) {
					cropBitmap = PhotoUtil.rotaingImageView(degree, cropBitmap);
				}
				imageBackgroud.setDrawingCacheEnabled(false);
				imageBackgroud.setImageBitmap(cropBitmap);
			}
		}
	}
	
	/**
	 * //��ȡ��ǰѡ����ͼƬ��ԴID
	 * @return
	 */
	public int getCurrentResId() {
		return currentResId;
	}

	/**
	 * ���õ�ǰѡ����ͼƬ��ԴID
	 * @param currentResId
	 */
	public void setCurrentResId(int currentResId) {
		this.currentResId = currentResId;
	}
	
	/**
	 * ����㲥�����࣬���� SDK key ��֤�Լ������쳣�㲥
	 */
	public class BaiduReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ShowToast.showShortToast(CreateTieziActivity.this, "key ��֤����! ���� AndroidManifest.xml �ļ��м�� key ����");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				isNetWork = false;
				ShowToast.showShortToast(CreateTieziActivity.this, "�������");
			}
		}
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
	            return ;
			
			if(lastLocation != null){
				if(lastLocation.getLatitude() == location.getLatitude()
						&& lastLocation.getLongitude() == location
						.getLongitude()){
					BmobLog.i("��ȡ������ͬ");
					mLocationClient.stop();
				}
				return ;
			}
			lastLocation = location;
			
			BmobLog.i("lontitude = " + location.getLongitude() + ",latitude = "
					+ location.getLatitude());
		/*StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation){
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
		} 

		Log.i("CreateTieziActivity", sb.toString());*/
		}
		
	}

}
