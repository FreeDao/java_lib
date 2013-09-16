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

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper.IErrorEventListener;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.model.BaseResponse;
import com.hoperun.telematics.mobile.model.location.GeoLocation;
import com.hoperun.telematics.mobile.model.roadrescue.RoadRescueRequest;
import com.hoperun.telematics.mobile.model.roadrescue.RoadRescueResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueActivity extends DefaultActivity {
	private static final String TAG = "RoadRescueActivity";
	private TextView mCall;
	private TextView mHistory;
	private TextView mName;
	private TextView mPhoneNum;
	private TextView mMessage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_road_rescue_layout);
		setTitleBar(this, getResources().getString(R.string.road_rescue));

		mCall = (TextView) findViewById(R.id.call);
		mHistory = (TextView) findViewById(R.id.history);
		mName = (TextView) findViewById(R.id.rescuer_name);
		mPhoneNum = (TextView) findViewById(R.id.rescuer_phone_num);
		mMessage = (TextView) findViewById(R.id.rescuer_message);

		mCall.setOnClickListener(mListener);
		mHistory.setOnClickListener(mListener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseActivity#onBindServiceFinish
	 * ()
	 */
	@Override
	protected void onBindServiceFinish(ComponentName className) {
		if (className.getClassName().equals(NetworkService.class.getName())) {
			startProgressDialog();
			executeRoadRescue();
		}
	}

	private View.OnClickListener mListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.call:
				break;
			case R.id.history:
				Intent intent = new Intent();
				intent.setClass(RoadRescueActivity.this, RoadRescueHistoryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};

	private void executeRoadRescue() {
		RoadRescueRequest roadRescueRequest = new RoadRescueRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		GeoLocation geoLocation = cacheManager.getGeoLocation();
		if (geoLocation != null) {
			roadRescueRequest.setLongitude(String.format("%s", geoLocation.getLng()));
			roadRescueRequest.setLatitude(String.format("%s", geoLocation.getLat()));
		} else {
			// TODO Should use default longitude and latitude value
		}
		Gson gson = new Gson();
		String requestStr = gson.toJson(roadRescueRequest);

		if (TestDataManager.IS_TEST_MODE || true) {
			TestDataManager testDataManager = TestDataManager.getInstance();
			RoadRescueResponse roadRescueResponse = testDataManager.getRoadRescueResponse();
			stopProgressDialog();
			mName.setText(roadRescueResponse.getName());
			mPhoneNum.setText(roadRescueResponse.getPhoneNum());
			mMessage.setText(roadRescueResponse.getDymamicMessage());
		} else {
			// send remote control command
			sendAsyncMessage(ENetworkServiceType.RoadRescue, requestStr, new INetCallback() {
				@Override
				public void callback(INetCallbackArgs args) {
					stopProgressDialog();
					final String payload = (null != args.getPayload() ? args.getPayload().toString() : null);
					// ------
					IErrorEventListener displayer = new IErrorEventListener() {
						@Override
						public void onResponseReturned(BaseResponse response) {
							RoadRescueResponse roadRescueResponse = RoadRescueResponse.parseJsonToObject(payload);
							mName.setText(roadRescueResponse.getName());
							mPhoneNum.setText(roadRescueResponse.getPhoneNum());
							mMessage.setText(roadRescueResponse.getDymamicMessage());
						}

						@Override
						public void onRetryButtonClicked() {
							startProgressDialog();
							executeRoadRescue();
						}

					};
					NetworkCallbackHelper.showInfoFromResponse(RoadRescueActivity.this, args, RoadRescueResponse.class,
					        displayer);
				}
			});
		}
	}

}
