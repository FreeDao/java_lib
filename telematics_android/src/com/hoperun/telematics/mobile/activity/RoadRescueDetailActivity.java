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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RescueInfo;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueDetailActivity extends DefaultActivity {
	private static final String TAG = "RoadRescueDetailActivity";
	private TextView mRequestTime;
	private TextView mArriveTime;
	private TextView mAddress;
	private TextView mLongitude;
	private TextView mLatitude;
	private TextView mName;
	private RatingBar mRate;
	private int mCurrentPosition = 1;

	// private TextView
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_road_rescue_detail_layout);
		setTitleBar(this, getResources().getString(R.string.road_rescue_detail));

		mCurrentPosition = getIntent().getIntExtra(RoadRescueHistoryActivity.INTENT_KEY_POSITION, 1);

		mRequestTime = (TextView) findViewById(R.id.request_rescue_time);
		mArriveTime = (TextView) findViewById(R.id.arrive_time);
		mAddress = (TextView) findViewById(R.id.address);
		mLongitude = (TextView) findViewById(R.id.longitude);
		mLatitude = (TextView) findViewById(R.id.latitude);
		mName = (TextView) findViewById(R.id.rescuer_name);
		mRate = (RatingBar) findViewById(R.id.valuation);

		initContent();
	}

	private void initContent() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CacheManager cacheManager = CacheManager.getInstance();
		RescueInfo rescueInfo = (RescueInfo) cacheManager.getRoadRescueHistoryList().get(mCurrentPosition);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(rescueInfo.getTimeFrom());
		mRequestTime.setText(sdf.format(calendar.getTime()));
		calendar.setTimeInMillis(rescueInfo.getTimeTo());
		mArriveTime.setText(sdf.format(calendar.getTime()));
		mAddress.setText(rescueInfo.getAddress());
		mLongitude.setText(rescueInfo.getLongitude());
		mLatitude.setText(rescueInfo.getLatitude());
		mName.setText(rescueInfo.getRescuerName());
		mRate.setRating(rescueInfo.getRate());
	}

}
