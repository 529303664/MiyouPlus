package com.luluandroid.miyouplus.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/** 首选项管理
  * @ClassName: SharePreferenceUtil
  * @Description: TODO
  * @author smile
  * @date 2014-6-10 下午4:20:14
  */
@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
	private SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String name) {
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}
	private String SHARED_KEY_NOTIFY = "shared_key_notify";
	private String SHARED_KEY_VOICE = "shared_key_sound";
	private String SHARED_KEY_VIBRATE = "shared_key_vibrate";
	private String SHARED_KEY_SELF_PASSWORD = "shared_key_self_password";
	private String SHARED_KEY_MIYOU_PASSWORD = "shared_key_miyou_password";
	
	// 是否允许推送通知
	public boolean isAllowPushNotify() {
		return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
	}

	public void setPushNotifyEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
		editor.commit();
	}

	// 允许声音
	public boolean isAllowVoice() {
		return mSharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
	}

	public void setAllowVoiceEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_VOICE, isChecked);
		editor.commit();
	}

	// 允许震动
	public boolean isAllowVibrate() {
		return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
	}

	public void setAllowVibrateEnable(boolean isChecked) {
		editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
		editor.commit();
	}
	
	// 允许密码保护秘友内容
	public boolean isAllowSelfPassword() {
			return mSharedPreferences.getBoolean(SHARED_KEY_SELF_PASSWORD, false);
		}

		public void setAllowSelfPasswordEnable(boolean isChecked) {
			editor.putBoolean(SHARED_KEY_SELF_PASSWORD, isChecked);
			editor.commit();
		}
		
		// 获取和设置密码
		public String getMiyouPassword() {
				return mSharedPreferences.getString(SHARED_KEY_MIYOU_PASSWORD, "");
			}

			public void setMiYouPassword(String password) {
				editor.putString(SHARED_KEY_MIYOU_PASSWORD, password);
				editor.commit();
			}

}
