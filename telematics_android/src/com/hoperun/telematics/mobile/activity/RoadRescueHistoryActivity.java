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

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.RoadRescueHistoryAdapter;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DateUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper.IErrorEventListener;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.model.BaseResponse;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RescueInfo;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RoadRescueHistoryRequest;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RoadRescueHistoryResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueHistoryActivity extends DefaultActivity {
	private static final String TAG = "RoadRescueHistoryActivity";
	public static final String INTENT_KEY_POSITION = "position";
	private static final int ROAD_RESCUE_DEFAULT_MAX_SIZE = 20;
	private TextView mUserId;
	private TextView mDateFrom;
	private TextView mDateTo;
	private ImageView mQuery;
	private ListView mListView;
	private RoadRescueHistoryAdapter mRoadRescueHistoryAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_road_rescue_history_layout);
		setTitleBar(this, getResources().getString(R.string.road_rescue_history_query));

		mUserId = (TextView) findViewById(R.id.user_id);
		mDateFrom = (TextView) findViewById(R.id.date_from);
		mDateTo = (TextView) findViewById(R.id.date_to);
		mQuery = (ImageView) findViewById(R.id.query_img);
		mListView = (ListView) findViewById(R.id.history_listview);

		mDateFrom.setOnClickListener(mDateListener);
		mDateTo.setOnClickListener(mDateListener);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (mRoadRescueHistoryAdapter.getItem(position) instanceof RescueInfo) {
					Intent intent = new Intent(RoadRescueHistoryActivity.this, RoadRescueDetailActivity.class);
					intent.putExtra(INTENT_KEY_POSITION, position);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
		});

		Calendar calendar = Calendar.getInstance();
		mDateFrom.setText(String.format("%s-%s-%s", calendar.get(Calendar.YEAR), "01", "01"));
		mDateTo.setText(String.format("%s-%s-%s", calendar.get(Calendar.YEAR), "12", "31"));
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
			executeRoadRescueHistory();
		}
	}

	private View.OnClickListener mDateListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			final Calendar calendar = Calendar.getInstance();
			DatePickerDialog datePickerDialog = new DatePickerDialog(RoadRescueHistoryActivity.this,
			        new OnDateSetListener() {
				        @Override
				        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					        calendar.set(year, monthOfYear, dayOfMonth);
					        TextView textView = (TextView) v;
					        textView.setText(sdf.format(calendar.getTime()));
				        }
			        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
			        calendar.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.show();
		}
	};

	private void executeRoadRescueHistory() {
		RoadRescueHistoryRequest roadRescueHistoryRequest = new RoadRescueHistoryRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		roadRescueHistoryRequest.setAccountId(cacheManager.getAccountId());
		roadRescueHistoryRequest.setDateFrom(DateUtil.getDate(2012, 0, 1));
		roadRescueHistoryRequest.setDateTo(DateUtil.getDate(2012, 6, 15));
		roadRescueHistoryRequest.setIndex(1);
		roadRescueHistoryRequest.setMaxSize(ROAD_RESCUE_DEFAULT_MAX_SIZE);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String requestStr = gson.toJson(roadRescueHistoryRequest);
		if (TestDataManager.IS_TEST_MODE || true) {
			TestDataManager testDataManager = TestDataManager.getInstance();
			RoadRescueHistoryResponse roadRescueHistoryResponse = testDataManager.getRoadRescueHistoryResponse();
			mRoadRescueHistoryAdapter = new RoadRescueHistoryAdapter(this, roadRescueHistoryResponse.getRescueList());
			mListView.setAdapter(mRoadRescueHistoryAdapter);
			stopProgressDialog();
		} else {
			// send remote control command
			sendAsyncMessage(ENetworkServiceType.RoadRescue, requestStr, new INetCallback() {
				@Override
				public void callback(INetCallbackArgs args) {
					// ------
					stopProgressDialog();
					final String payload = (null != args.getPayload() ? args.getPayload().toString() : null);
					// ------
					IErrorEventListener displayer = new IErrorEventListener() {
						@Override
						public void onResponseReturned(BaseResponse response) {
							updateRoadRescueHistory(payload);
						}

						@Override
						public void onRetryButtonClicked() {
							startProgressDialog();
							executeRoadRescueHistory();
						}

					};
					NetworkCallbackHelper.showInfoFromResponse(RoadRescueHistoryActivity.this, args,
					        RoadRescueHistoryResponse.class, displayer);
				}
			});
		}
	}

	private void updateRoadRescueHistory(String payload) {
		RoadRescueHistoryResponse roadRescueHistoryResponse = RoadRescueHistoryResponse.parseJsonToObject(payload);
		mRoadRescueHistoryAdapter = new RoadRescueHistoryAdapter(RoadRescueHistoryActivity.this,
		        roadRescueHistoryResponse.getRescueList());
		mListView.setAdapter(mRoadRescueHistoryAdapter);
	}
}
