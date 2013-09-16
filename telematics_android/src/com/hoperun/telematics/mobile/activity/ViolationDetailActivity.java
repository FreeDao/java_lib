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
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.model.violation.ViolationInfo;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ViolationDetailActivity extends DefaultActivity {
	private static final String TAG = "ViolationDetailActivity";
	private TextView mLast;
	private TextView mNext;
	private int mCurrentPosition;
	private TextView mLicense;
	private TextView mTime;
	private TextView mAddress;
	private TextView mSummary;
	private TextView mDetail;
	private TextView mDeduction;
	private TextView mFine;
	private TextView mStateTextView;
	private ImageView mStateImageView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_violation_detail_layout);
		setTitleBar(this, getString(R.string.violation_query));

		mCurrentPosition = getIntent().getIntExtra(ViolationActivity.INTENT_KEY_POSITION, 0);

		mLicense = (TextView) findViewById(R.id.vehicle_licence);
		mTime = (TextView) findViewById(R.id.time);
		mAddress = (TextView) findViewById(R.id.address);
		mSummary = (TextView) findViewById(R.id.summary);
		mDetail = (TextView) findViewById(R.id.detail);
		mDeduction = (TextView) findViewById(R.id.deductio);
		mFine = (TextView) findViewById(R.id.fine);
		mStateTextView = (TextView) findViewById(R.id.state);
		mStateImageView = (ImageView) findViewById(R.id.state_image);
		mLast = (TextView) findViewById(R.id.last);
		mNext = (TextView) findViewById(R.id.next);

		mLast.setOnClickListener(clickListener);
		mNext.setOnClickListener(clickListener);

		initContent(mCurrentPosition);
	}

	private View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.last:
				lastInfo();
				break;
			case R.id.next:
				nextInfo();
				break;
			default:
				break;
			}
		}
	};

	private void initContent(int position) {
		Calendar calendar = Calendar.getInstance();
		CacheManager cacheManager = CacheManager.getInstance();
		ViolationInfo violationInfo = (ViolationInfo) cacheManager.getViolationList().get(position);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		// mLicense.setText(cacheManager.getLicense());
		calendar.setTime(violationInfo.getDate());
		Log.i(TAG, sdf.format(calendar.getTime()));
		mTime.setText(sdf.format(calendar.getTime()));
		mAddress.setText(violationInfo.getLocation());
		mSummary.setText(violationInfo.getSummary());
		mDetail.setText(violationInfo.getLegalBasis());
		mDeduction.setText(String.format("%s", violationInfo.getSubtractedScore()));
		mFine.setText(String.format("%s", violationInfo.getFine()));
		if (violationInfo.isDealed()) {
			mStateImageView.setImageResource(R.drawable.violation_treated);
			mStateTextView.setText(R.string.is_dealed);
		} else {
			mStateTextView.setText(R.string.not_dealed);
			mStateImageView.setImageResource(R.drawable.violation_not_treated);
		}
		if (mCurrentPosition == 1) {
			mLast.setVisibility(View.GONE);
		}
		if (mCurrentPosition == (cacheManager.getViolationList().size() - 1)) {
			mNext.setVisibility(View.GONE);
		}
	}

	private void lastInfo() {
		mCurrentPosition--;
		CacheManager cacheManager = CacheManager.getInstance();
		List<Object> violationList = cacheManager.getViolationList();
		Object item;
		while (mCurrentPosition > 0) {
			item = violationList.get(mCurrentPosition);
			if (item instanceof ViolationInfo) {
				initContent(mCurrentPosition);
				break;
			} else {
				mCurrentPosition--;
			}
		}
		// Because of the zero position must be month tag, if there is data.
		if (mCurrentPosition <= 1) {
			mLast.setVisibility(View.GONE);
		}
		if (mCurrentPosition < violationList.size() - 1) {
			mNext.setVisibility(View.VISIBLE);
		}
	}

	private void nextInfo() {
		mCurrentPosition++;
		CacheManager cacheManager = CacheManager.getInstance();
		List<Object> violationList = cacheManager.getViolationList();
		int size = violationList.size();
		Object item;
		while (mCurrentPosition < size) {
			item = violationList.get(mCurrentPosition);
			if (item instanceof ViolationInfo) {
				initContent(mCurrentPosition);
				break;
			} else {
				mCurrentPosition++;
			}
		}
		if (mCurrentPosition >= (size - 1)) {
			mNext.setVisibility(View.GONE);
		}
		if (mCurrentPosition > 0) {
			mLast.setVisibility(View.VISIBLE);
		}
	}
}
