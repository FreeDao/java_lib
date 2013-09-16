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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseActivity;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.helper.DialogHelper;

/**
 * 
 * @author he_chen
 * 
 */
public class DefaultActivity extends BaseActivity {

	public ImageView mMainBtn;
	public ImageView mLineImageView;
	public ImageView mUserInfoBtn;
	protected final static int MESSAGE_REQUEST_TIMEOUT = 1;
	private static final int MAX_RELOAD_TIMES = 3;
	private int reLoadTimes = MAX_RELOAD_TIMES;

	protected boolean canReload() {
		if (reLoadTimes > 0) {
			reLoadTimes--;
			return true;
		} else {
			return false;
		}
	}

	protected void initReloadTimes(){
		reLoadTimes = MAX_RELOAD_TIMES;
	}
	
	protected void reload() {

	}

	protected void startReload(String errMsg, DialogInterface.OnClickListener retryBtnListener) {
		if (canReload()) {
			reload();
		} else {
			stopProgressDialog();
			DialogHelper.alertDialog(this, errMsg, retryBtnListener);
		}
	}

	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dealHandlerRequest(msg);
		};
	};

	/**
	 * 
	 * setTitleBar:please describe
	 * 
	 */
	public void setTitleBar(Activity activity, String title) {
		FrameLayout mTitleBarLayout = (FrameLayout) findViewById(R.id.title_bar_layout);
		TextView titleBarText = (TextView) mTitleBarLayout.findViewById(R.id.title_bar_text);
		titleBarText.setText(title);

		mMainBtn = (ImageView) mTitleBarLayout.findViewById(R.id.main_btn);
		mLineImageView = (ImageView) mTitleBarLayout.findViewById(R.id.line_btn);
		mUserInfoBtn = (ImageView) mTitleBarLayout.findViewById(R.id.userInfo_btn);
		mMainBtn.setOnClickListener(onClickListener);
		mUserInfoBtn.setOnClickListener(onClickListener);

		CustomApplication.getInstance().addActivity(activity);
	}

	/**
	 * onClickListener
	 */
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.main_btn:
				intent = new Intent(DefaultActivity.this, MainActivity.class);
				startActivity(intent);

				break;
			case R.id.userInfo_btn:
				Toast.makeText(DefaultActivity.this, "UserInfo Button", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	public void dealHandlerRequest(Message msg) {
		switch (msg.what) {
		case MESSAGE_REQUEST_TIMEOUT: {
			ENetworkServiceType serviceType = (ENetworkServiceType) msg.obj;
 			cancelAsyncMessage(serviceType);
			Toast.makeText(this, R.string.timeout, Toast.LENGTH_LONG).show();
			break;
		}
		}
	}
}
