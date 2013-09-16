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
package com.hoperun.telematics.mobile;

import com.hoperun.telematics.mobile.activity.CustomApplication;
import com.hoperun.telematics.mobile.activity.DefaultActivity;
import com.hoperun.telematics.mobile.activity.LoginActivity;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.view.ShaderView;
import com.hoperun.telematics.mobile.view.ShaderView.AnimationComplete;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @author chen_guigui
 * 
 */
public class StartActivity extends DefaultActivity {
	private static final String TAG = "StartActivity";
	private LinearLayout mAnimLayout;
	private ImageView mAnimImageView;
	private ShaderView mShaderView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_start_layout);

		CustomApplication.getInstance().addActivity(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		startAnim();
	}

	private void startAnim() {
		mAnimLayout = (LinearLayout) findViewById(R.id.anim_layout);
		mAnimImageView = (ImageView) findViewById(R.id.anim_imageview);
		mShaderView = (ShaderView) findViewById(R.id.shaderview);
		mShaderView.setContentString(getString(R.string.telematics_system));
		mShaderView.setAnimationComplete(new AnimationComplete() {
			@Override
			public void complete() {
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});

		new Thread(mShaderView).start();
		mAnimImageView.setVisibility(View.GONE);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_scale_rotate_animation_in);
		mAnimImageView.startAnimation(animation);
		mAnimImageView.setVisibility(View.VISIBLE);
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
