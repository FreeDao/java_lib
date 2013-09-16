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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.hoperun.telematics.mobile.helper.LogUtil;

/**
 * 
 * @author he_chen
 * 
 */
public class CustomApplication extends Application {

	private List<Activity> activityList = new ArrayList<Activity>();
	private static CustomApplication instance;

	private static Context mContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		mContext = getApplicationContext();
	}

	public static Context getContext() {
		return mContext;
	}

	public static CustomApplication getInstance() {
		if (null == instance) {
			instance = new CustomApplication();
		}
		return instance;
	}

	// add activity
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// finish all activity
	public void exit() {
		LogUtil.i("ExitApplication", "exit()");
		
		for (Activity activity : activityList) {
			if(!activity.isFinishing()){
				activity.finish();
			}
			
		}
		System.exit(0);
	}

}
