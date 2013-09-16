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

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.ScoreAdapter;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.model.BaseResponse;
import com.hoperun.telematics.mobile.model.score.Score;
import com.hoperun.telematics.mobile.model.score.ScoreRequest;
import com.hoperun.telematics.mobile.model.score.ScoreResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ScoreActivity extends DefaultActivity {
	private static final String TAG = "ScoreActivity";
	private ListView mListView;
	private ScoreAdapter mScoreAdapter;
	private TextView mScore;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_score_layout);
		setTitleBar(this, getResources().getString(R.string.member_score));

		mListView = (ListView) findViewById(R.id.listview);
		mScore = (TextView) findViewById(R.id.current_score);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onBindServiceFinish ()
	 */
	@Override
	protected void onBindServiceFinish(ComponentName className) {
		startProgressDialog(ENetworkServiceType.Score);
		if (className.getClassName().equals(NetworkService.class.getName())) {
			executeScoreRequest();
		}
	}

	private void executeScoreRequest() {
		ScoreRequest scoreRequest = new ScoreRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		scoreRequest.setScore(12000);// TODO hechen

		Gson gson = new Gson();
		String requestStr = gson.toJson(scoreRequest);
		Log.i(TAG, "score request json string: " + requestStr);

		if (TestDataManager.IS_TEST_MODE) {
			TestDataManager testDataManager = TestDataManager.getInstance();
			ScoreResponse scoreResponse = testDataManager.getScoreResponse();
			List<Score> scoreList = scoreResponse.getScoreList();
			mScoreAdapter = new ScoreAdapter(this, scoreList);
			Log.i(TAG, "scoreList size is " + scoreList.size());
			stopProgressDialog();
			mListView.setAdapter(mScoreAdapter);
		} else {
			// send request
			sendAsyncMessage(ENetworkServiceType.Score, requestStr, new INetCallback() {
				@Override
				public void callback(INetCallbackArgs args) {
					Context context = ScoreActivity.this;

					OnClickListener retryBtnListener = new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// refresh(null);
						}
					};
					if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
						stopProgressDialog();
						String payload = args.getPayload();
						LogUtil.i(TAG, "payload: " + payload);
						if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
							DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
						} else {
							try {
								ScoreResponse response = (ScoreResponse) BaseResponse.parseJsonToObject(payload,
										ScoreResponse.class);

								if (NetworkCallbackHelper.isErrorResponse(context, response)) {
									NetworkCallbackHelper.alertBusinessError(context, response.getErrorCode());
								} else {
									List<Score> list = response.getScoreList();
									mScoreAdapter = new ScoreAdapter(ScoreActivity.this, list);
									mListView.setAdapter(mScoreAdapter);
								}
							} catch (Exception e) {
								DialogHelper.alertDialog(ScoreActivity.this, R.string.warning, R.string.system_error,
										R.string.ok);
							}

						}
					} else {
						String errMsg = args.getErrorMessage();
						LogUtil.e(TAG, "error msg: " + errMsg);
						startReload(getResources().getString(R.string.error_async_return_fault), retryBtnListener);
					}
				}
			});
		}
	}

}
