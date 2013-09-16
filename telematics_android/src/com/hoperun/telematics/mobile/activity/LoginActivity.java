/*****************************************************************************
 *
 *                      HOPERUN PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to HopeRun
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from HopeRun.
 *
 *            Copyright (c) 2012 by HopeRun.  All rights reserved.
 *
 *****************************************************************************/
package com.hoperun.telematics.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.SharedPreferencesUtil;
import com.hoperun.telematics.mobile.model.login.LoginRequest;
import com.hoperun.telematics.mobile.model.login.LoginResponse;

/**
 * 
 * 
 */
public class LoginActivity extends DefaultActivity {

	private String TAG = "LoginActivity";
	private Button mLoginBtn;
	private EditText mAccountId;
	private EditText mPassword;
	private View mRememberAccountId;
	private LinearLayout mRememberLayout;

	private SharedPreferences preference;
	private boolean mRemembered;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login_layout);

		setTitleBar(this, getResources().getString(R.string.telematics_system));
		super.mMainBtn.setVisibility(View.INVISIBLE);
		super.mLineImageView.setVisibility(View.INVISIBLE);
		super.mUserInfoBtn.setVisibility(View.INVISIBLE);

		initWidget();

		restoreUserInfo();

		mPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				switch (actionId) {
				case EditorInfo.IME_ACTION_DONE:
					String accountId = mAccountId.getText().toString();
					String password = mPassword.getText().toString();

					if (null == accountId || "".equals(accountId)) {
						DialogHelper.alertDialog(LoginActivity.this, R.string.warning, R.string.username_hint,
								R.string.ok);
					} else if (null == password || "".equals(password)) {
						DialogHelper.alertDialog(LoginActivity.this, R.string.warning, R.string.password_hint,
								R.string.ok);
					} else {
						if (!accountId.trim().equalsIgnoreCase("test") || !password.trim().equalsIgnoreCase("test")) {
							DialogHelper.alertDialog(LoginActivity.this, R.string.warning,
									R.string.username_password_error, R.string.ok);
							return true;
						} else {
							startActivity(new Intent(LoginActivity.this, MainActivity.class));
							finish();
						}
						// startProgressDialog();
						// executeLogin(accountId, password);
						// saveUserInfo(accountId);
					}
					return true;
				default:
					break;
				}
				return false;
			}
		});
	}

	/**
	 * initWidget
	 */
	private void initWidget() {
		SharedPreferencesUtil.context = LoginActivity.this.getApplicationContext();
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mAccountId = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);
		mRememberAccountId = findViewById(R.id.remember_username);
		mRememberLayout = (LinearLayout) findViewById(R.id.remember_username_layout);

		mLoginBtn.setOnClickListener(BtnOnClickListener);
		mRememberLayout.setOnClickListener(BtnOnClickListener);
	}

	private OnClickListener BtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.login_btn:
				String accountId = mAccountId.getText().toString();
				String password = mPassword.getText().toString();

				if (null == accountId || "".equals(accountId)) {
					DialogHelper.alertDialog(LoginActivity.this, R.string.warning, R.string.username_hint, R.string.ok);
				} else if (null == password || "".equals(password)) {
					DialogHelper.alertDialog(LoginActivity.this, R.string.warning, R.string.password_hint, R.string.ok);
				} else {
					if (!accountId.trim().equalsIgnoreCase("test") || !password.trim().equalsIgnoreCase("test")) {
						DialogHelper.alertDialog(LoginActivity.this, R.string.warning,
								R.string.username_password_error, R.string.ok);
						return;
					} else {
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					// startProgressDialog();
					// executeLogin(accountId, password);
					// saveUserInfo(accountId);
				}

				break;
			case R.id.remember_username_layout:
				mRemembered = !mRemembered;
				if (mRemembered) {
					mRememberAccountId.setBackgroundResource(R.drawable.login_remembered);
				} else {
					mRememberAccountId.setBackgroundResource(R.drawable.login_not_remember);
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * execute login
	 * 
	 * @param accountId
	 * @param password
	 */
	private void executeLogin(String accountId, String password) {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setAccountId(accountId);
		loginRequest.setPassword(password);
		loginRequest.setDeviceId(getDeviceId());

		Gson gson = new Gson();
		String requstStr = gson.toJson(loginRequest);

		// send login request
		sendSyncMessage(ENetworkServiceType.Login, requstStr, new INetCallback() {
			@Override
			public void callback(INetCallbackArgs args) {

				stopProgressDialog();

				Context context = LoginActivity.this;

				// OnClickListener retryBtnListener = new OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// }
				// };
				// 检查是否有非业务级的错误
				if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {

					String payload = args.getPayload();
					if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
						DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
					} else {
						Gson gson = new Gson();
						LoginResponse response = gson.fromJson(payload, LoginResponse.class);
						if (NetworkCallbackHelper.isErrorResponse(context, response)) {
							// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
							NetworkCallbackHelper.alertBusinessError(context, response.getErrorCode());
						} else {
							CacheManager.getInstance().updateLoginResponse(response);
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					}
				} else {
					// 根据各接口情况选择重试或直接提示
					String errMsg = args.getErrorMessage();
				 
					LogUtil.e(TAG, "errorMsg: " + errMsg);
					DialogHelper.alertDialog(LoginActivity.this, R.string.warning,
							R.string.error_async_return_fault, R.string.ok);
				}

				// stopProgressDialog();
				//
				// if (!NetworkCallbackHelper.haveSystemError(LoginActivity.this, args.getStatus())) {
				// stopProgressDialog();
				// String payload = args.getPayload();
				// if (null == payload || payload.trim().length() == 0) {
				// DialogHelper.alertDialog(LoginActivity.this, R.string.error_empty_payload, R.string.ok);
				// } else {
				//
				// Gson gson = new Gson();
				// LoginResponse response = (LoginResponse) gson.fromJson(payload, LoginResponse.class);
				// if (NetworkCallbackHelper.isErrorResponse(LoginActivity.this, response)) {
				// NetworkCallbackHelper.alertBusinessError(LoginActivity.this, response.getErrorCode());
				// } else {
				// CacheManager.getInstance().updateLoginResponse(response);
				// Intent intent = new Intent();
				// intent.setClass(LoginActivity.this, MainActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
				// }
				// }
				// } else {
				// String errMsg = args.getErrorMessage();
				// if (errMsg == null || errMsg.trim().isEmpty()) {
				// errMsg = getString(R.string.error_async_return_fault);
				// }
				//
				// DialogHelper.alertDialog(LoginActivity.this, errMsg, R.string.ok);
				// }

			}
		});
	}

	/**
	 * get device id
	 * 
	 * @return deviceId
	 */
	private String getDeviceId() {

		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getDeviceId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		saveUserInfo(mAccountId.getText().toString());
	}

	/**
	 * restoreUserInfo
	 */
	private void restoreUserInfo() {
		preference = getPreferences(Context.MODE_PRIVATE);
		mRemembered = preference.getBoolean(Constants.PROPERTY_IS_USER_REMEMBERED, false);
		if (mRemembered) {
			mAccountId.setText(preference.getString(Constants.PROPERTY_USERNAME, null));
			mRememberAccountId.setBackgroundResource(R.drawable.login_remembered);
		} else {
			mRememberAccountId.setBackgroundResource(R.drawable.login_not_remember);
		}
	}

	/**
	 * saveUserInfo
	 * 
	 * @param username
	 */
	private void saveUserInfo(String username) {
		Editor editor = preference.edit();
		editor.putBoolean(Constants.PROPERTY_IS_USER_REMEMBERED, mRemembered);
		editor.putString(Constants.PROPERTY_USERNAME, mRemembered ? username : null);
		editor.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			CustomApplication.getInstance().exit();
			// Intent startMain = new Intent(Intent.ACTION_MAIN);
			// startMain.addCategory(Intent.CATEGORY_HOME);
			// startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(startMain);
			// android.os.Process.killProcess(android.os.Process.myPid());
			LogUtil.i(TAG, "keycode back.");
		}
		return false;
	}
}