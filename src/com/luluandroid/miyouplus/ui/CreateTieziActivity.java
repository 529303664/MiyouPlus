package com.luluandroid.miyouplus.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.bean.DBMgr;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.bean.Mibos.MessageType;
import com.luluandroid.miyouplus.bean.User;
import com.luluandroid.miyouplus.config.ChannelCodes;
import com.luluandroid.miyouplus.config.Conf;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.main.CustomApplcation;
import com.luluandroid.miyouplus.ui.fragment.FragmentCreatetieziChangeBar;
import com.luluandroid.miyouplus.ui.fragment.FragmentCreatetieziNavigation;
import com.luluandroid.miyouplus.ui.fragment.FragmentImageMould;
import com.luluandroid.miyouplus.util.FileManager;
import com.luluandroid.miyouplus.util.ImageManager;
import com.luluandroid.miyouplus.util.ImageTools;
import com.luluandroid.miyouplus.util.TimeUtil;
import com.luluandroid.miyouplus.view.dialog.DialogProgress;
import com.luluandroid.miyouplus.view.dialog.DialogTips;
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
	private loadPictureAsynctask myLoadTask;
	private FrameLayout navigationFrameLayout;
	private ImageView navigationLfBt, navigationRtBt, imageBackgroud;
	private EditText myMessageEditext;
	private TextView emotionText;
	private CheckBox contactCheckBox,CommentCheckBox;
	private boolean NvIsOpen, IsAsynctaskOk;// �ֱ����жϵ����Ƿ����ʹ���ͼƬ��Asynctask�Ƿ���������
	private DialogProgress progressDialog;
	private int currentResId = 1;//��ǰѡ����ͼƬ��ԴID
	
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
		IsAsynctaskOk = true;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
		case R.id.home:
			if (myLoadTask != null) {
				myLoadTask.onCancelled();
			}
			finish();
		case R.id.action_create_tiezi_write:
			// ���ݷ���
			if (IsAsynctaskOk) {
				myHandler.sendEmptyMessage(ChannelCodes.DIALOG_SHOW);
				 saveAll();
			}
		default:
			break;
		}
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */
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
	
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case ChannelCodes.CREATE_TIEZI_SUCCESS:
				setResult(ChannelCodes.CREATE_TIEZI_SUCCESS);
				hideProgressDialog();
				finish();
				break;
			case ChannelCodes.CREATE_TIEZI_FAIL:
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case Conf.TAKE_PICTURE:// ������������Ƭ��Ĳ���
			// �������ڱ��ص�ͼƬȡ������С����ʾ�ڽ�����
			Bitmap bitmap = BitmapFactory.decodeFile(Conf.APP_SDCARD_CACHE_PATH
					+ "/" + "image.jpg");
			if (bitmap == null)
				break;
			// ����ͼƬ
			setPicchange(true);
			myLoadTask = new loadPictureAsynctask(progressBar, imageBackgroud,
					true);
			myLoadTask.execute(bitmap);
			break;
		case Conf.CHOOSE_PICTURE:
			ContentResolver resolver = getContentResolver();
			// ��Ƭ��ԭʼ��Դ��ַ
			if (data == null)
				break;
			Uri originalUri = data.getData();

			try {
				Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
						originalUri);
				if (photo != null) {
					// ����ͼƬ
					setPicchange(true);
					myLoadTask = new loadPictureAsynctask(progressBar,
							imageBackgroud, false);
					myLoadTask.execute(photo);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Conf.CROP:
			Uri uri = null;
			if (data != null) {
				uri = data.getData();
			} else {
				String fileName = getSharedPreferences("tempImage",
						Context.MODE_PRIVATE).getString("tempName", "");
				uri = Uri.fromFile(new File(Conf.APP_SDCARD_CACHE_PATH,
						fileName));
			}
			// ���ֻ���Ļ�ֱ��ʵ�һ����ʽ�ü�ͼƬ
			ImageManager.cropImage(CreateTieziActivity.this, uri,
					ImageManager.getPhoneWidth(this),
					ImageManager.getPhoneHeight(this) / 2, Conf.CROP_PICTURE);
			break;
		case Conf.CROP_PICTURE:
			Bitmap photo = null;
			if (data == null)
				break;
			Uri photoUri = data.getData();
			if (photoUri != null)
				photo = BitmapFactory.decodeFile(photoUri.getPath());
			if (photo == null) {
				Bundle extra = data.getExtras();
				if (extra != null) {
					photo = (Bitmap) extra.get("data");
				} else
					break;
			}
			// ����ͼƬ
			setPicchange(true);
			myLoadTask = new loadPictureAsynctask(progressBar, imageBackgroud,
					false);
			myLoadTask.execute(photo);
			break;
		default:
			break;
		}
	}

	// �޸ĺ��ͼƬ·��
	public static String fileSavePath = "";

	class loadPictureAsynctask extends AsyncTask<Bitmap, Integer, String> {

		private Bitmap pictureImage;
		private ProgressBar progressBar;
		private ImageView imageBackgroud;
		private boolean flag;// ѹ���Ͳ�ѹ����־λ

		public loadPictureAsynctask(ProgressBar progressBar,
				ImageView imageBackgroud, boolean flag) {
			super();
			this.progressBar = progressBar;
			this.imageBackgroud = imageBackgroud;
			this.flag = flag;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			ShowToast.showShortToast(CreateTieziActivity.this, "��Ƭ���ر���ɹ�");
			fileSavePath = "";
			progressBar.setIndeterminate(false);
			progressBar.setVisibility(View.GONE);
			if (pictureImage != null) {
				pictureImage.recycle();
				pictureImage = null;
			}
			IsAsynctaskOk = true;
			super.onCancelled();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			IsAsynctaskOk = true;
			if ("success".equals(result)) {
				/*
				 * imageBackgroud.setBackgroundDrawable(ImageTools
				 * .bitmapToDrawable(pictureImage));
				 */
				imageBackgroud.setImageBitmap(pictureImage);
				Log.i("userImageFilePath", "ִ�е�����Ƭ��һ��");
				Log.i("userImageFilePath", fileSavePath);
				// ���濪ʼ�ϴ�ͼƬ
			}
			ShowToast.showShortToast(CreateTieziActivity.this, "��Ƭ���ر���"
					+ result);
			progressBar.setIndeterminate(false);
			progressBar.setVisibility(View.GONE);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			fileSavePath = "";
			IsAsynctaskOk = false;
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setIndeterminate(true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(Bitmap... params) {
			// TODO Auto-generated method stub
			// �ֽ�����ʽ�洢ͼƬ
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ByteArrayInputStream isBm = null;
			String strfilename = String.valueOf(System.currentTimeMillis());
			System.out.println("params[0]:" + params[0]);
			if (flag == true) {// ѹ��
				// ����ԭͼƬ��40%������Ϊ������ʾ��ͼƬ
				params[0].compress(CompressFormat.JPEG, 40, baos);

			} else {
				params[0].compress(CompressFormat.JPEG, 100, baos);
			}
			isBm = new ByteArrayInputStream(baos.toByteArray());
			System.out.println("isBm:" + isBm);
			try {
				pictureImage = ImageTools
						.compressBySize(
								isBm,
								ImageManager
										.getPhoneWidth(CreateTieziActivity.this) / 2,
								ImageManager
										.getPhoneHeight(CreateTieziActivity.this) / 2);
				ImageTools.deletePhotoAtPathAndName(Conf.APP_SDCARD_CACHE_PATH,
						"image" + ".jpg");
				ImageTools.savePhotoToSDCard(pictureImage,
						Conf.APP_SDCARD_CACHE_PATH, strfilename, ".jpg");
				fileSavePath = Conf.APP_SDCARD_CACHE_PATH + "/" + strfilename
						+ ".jpg";
				params[0].recycle();
				baos.close();
				isBm.close();
				return "success";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Compress", "��Ƭѹ��ʧ��" + e.toString());
				e.printStackTrace();
				return "failure";
			}
		}
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
//			final BmobFile bmobfile = new BmobFile(new File(Conf.APP_SDCARD_ALBUM_PATH, mibo.getLocalPicName()+".jpg"));
			final BmobFile bmobfile = new BmobFile(new File(FileManager.getInstance().
					getAlbumStorageDir(this, Conf.SD_Album_Dir_Name), mibo
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
		Mibos mibo = new Mibos(BmobUser.getCurrentUser(this).getUsername(),
				myMessageEditext.getText().toString(), Integer.valueOf("0"),((User) userManager.getCurrentUser(User.class)).getObjectId());
		mibo.setMyUser((User) userManager.getCurrentUser(User.class));
		mibo.setOpentoAll(contactCheckBox.isChecked());
		mibo.setCommentOk(CommentCheckBox.isChecked());
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
		/*if(!DBMgr.getInstance(getApplicationContext()).addTiezi(miboList)){
			return false;
		}
		return DBMgr.getInstance(getApplicationContext()).addTiezi(mibo.getComment());
*/		return DBMgr.getInstance(getApplicationContext()).addTiezi(miboList);
	}

	/**
	 * ����������Ƭ������
	 */
	private void SaveLocalImage(Mibos mibo) {
		// TODO Auto-generated method stub
		imageBackgroud.setDrawingCacheEnabled(true);
		Bitmap tempBitmap = imageBackgroud.getDrawingCache();
		FileManager.getInstance().deleteFilesofSingleDir(
				Conf.APP_SDCARD_CACHE_PATH);
		/*ImageTools
				.savePhotoToSDCard(tempBitmap, Conf.APP_SDCARD_ALBUM_PATH, mibo
						.getLocalPicName(), ".jpg");*/
		ImageTools.savePhotoToAppPrivateSD(tempBitmap, FileManager.getInstance().
				getAlbumStorageDir(this, Conf.SD_Album_Dir_Name), mibo
				.getLocalPicName(), ".jpg");
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

}
