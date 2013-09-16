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
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.ViolationListAdapter;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.AnimationHelper;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DateUtil;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.model.violation.ViolationInfo;
import com.hoperun.telematics.mobile.model.violation.ViolationRequest;
import com.hoperun.telematics.mobile.model.violation.ViolationResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ViolationActivity extends DefaultActivity {
	private static final String TAG = "ViolationActivity";
	public static final String INTENT_KEY_POSITION = "position";
	public static final int DEFAULT_MAX_SIZE = 10;
	private ListView mListView;
	private ViolationListAdapter mViolationListAdapter;
	private TextView mStartDate;
	private TextView mDeadline;
	private TextView mViolationTimes;
	private LinearLayout mLoadMoreLayout;
	private View mLoadMoreView;
	private int mCurIndex = 1; // based one not zero.
	private int mMaxIndex = 1;

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
		setContentView(R.layout.ui_violation_layout);
		setTitleBar(this, getString(R.string.violation_query));

		mStartDate = (TextView) findViewById(R.id.start_date);
		mDeadline = (TextView) findViewById(R.id.deadline);
		mListView = (ListView) findViewById(R.id.violation_listview);
		mViolationTimes = (TextView) findViewById(R.id.violation_times);

		LayoutInflater inflater = LayoutInflater.from(this);
		mLoadMoreLayout = (LinearLayout) inflater.inflate(R.layout.ui_listview_footview_layout, null);
		mLoadMoreView = mLoadMoreLayout.findViewById(R.id.loadmore_view);

		mStartDate.setInputType(InputType.TYPE_NULL);
		mDeadline.setInputType(InputType.TYPE_NULL);
		mStartDate.setOnClickListener(mDateListener);
		mDeadline.setOnClickListener(mDateListener);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (mViolationListAdapter.getItem(position) instanceof ViolationInfo) {
					Intent intent = new Intent();
					intent.setClass(ViolationActivity.this, ViolationDetailActivity.class);
					intent.putExtra(INTENT_KEY_POSITION, position);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// 如果已经滚动到底部
				if (totalItemCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount) {
					Log.i(TAG, "scroll to the bottom");
					if (mCurIndex < mMaxIndex) {
						mCurIndex++;
						AnimationHelper.startRepeatSelfRotateAnimation(mLoadMoreView, 1200, null);
						executeViolationRequest();
					}
				}
			}
		});
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
			executeViolationRequest();
		}
	}

	private void executeViolationRequest() {
		ViolationRequest violationRequest = new ViolationRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		// violationRequest.setVin(cacheManager.getVin());
		// violationRequest.setLicense(cacheManager.getLicense());
		violationRequest.setLicense("京NEB810");
		Date dateFrom = DateUtil.getDate(2012, 0, 1);
		Date dateTo = DateUtil.getDate(2012, 6, 30);
		violationRequest.setDateFrom(dateFrom);
		violationRequest.setDateTo(dateTo);
		violationRequest.setIndex(mCurIndex);
		violationRequest.setMaxSize(DEFAULT_MAX_SIZE);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String requestStr = gson.toJson(violationRequest);
		Log.i(TAG, requestStr);

		// if (TestDataManager.IS_TEST_MODE && false) {
		// TestDataManager testDataManager = TestDataManager.getInstance();
		// ViolationResponse violationResponse =
		// testDataManager.getViolationData();
		// List<ViolationInfo> violationList =
		// violationResponse.getViolationList();
		// mViolationTimes.setText(String.format("%s",
		// violationResponse.getTotalSize()));
		// mViolationListAdapter = new ViolationListAdapter(this,
		// violationList);
		// stopProgressDialog();
		// mListView.setAdapter(mViolationListAdapter);
		// } else {
		sendAsyncMessage(ENetworkServiceType.Violation, requestStr, new INetCallback() {
			@Override
			public void callback(INetCallbackArgs args) {
				LogUtil.i(TAG, "payload: " + args.getPayload());
				// the listener about retry
				DialogInterface.OnClickListener retryBtnListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mCurIndex == 1) {
							startProgressDialog();
						} else {
							AnimationHelper.startRepeatSelfRotateAnimation(mLoadMoreView, 1200, null);
						}
						executeViolationRequest();
					}
				};
				// ------ Judge the business error
				if (!NetworkCallbackHelper.haveSystemError(ViolationActivity.this, args.getStatus())) {
					if (mCurIndex == 1) {
						stopProgressDialog();
					} else {
						mLoadMoreView.clearAnimation();
					}
					String payload = args.getPayload();
					if (payload == null || payload.trim().length() == 0) {
						DialogHelper.alertDialog(ViolationActivity.this, R.string.error_empty_payload, R.string.ok);
					} else {
						// 在 payload 不为空和空字符串的情况下，根据model的不同情况解析json为model
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
						ViolationResponse response = gson.fromJson(payload, ViolationResponse.class);
						if (NetworkCallbackHelper.isErrorResponse(ViolationActivity.this, response)) {
							// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
							NetworkCallbackHelper.alertBusinessError(ViolationActivity.this, response.getErrorCode());
						} else {
							// 当返回的信息为正常的数据时，则按业务逻辑正常处理和显示数据
							handleResponse(response);
						}
					}
				} else {
					// 根据各接口情况选择提示信息
					String errMsg = args.getErrorMessage();
					if (errMsg == null || errMsg.trim().length() == 0) {
						errMsg = getString(R.string.error_async_return_fault);
					}
					// 根据实际情况选择弹出的警告对话框类型或直接重试
					startReload(errMsg, retryBtnListener);
				}
			}
		});
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.activity.DefaultActivity#reload()
	 */
	@Override
	protected void reload() {
		executeViolationRequest();
	}

	private void handleResponse(ViolationResponse violationResponse) {
		int totalSize = violationResponse.getTotalSize();
		List<ViolationInfo> list = violationResponse.getViolationList();
		int vilation_times = 0;
		if (mViolationTimes.getText().toString().length() > 0) {
			vilation_times += Integer.parseInt(mViolationTimes.getText().toString());
		}
		mViolationTimes.setText(String.format("%s", list.size() + vilation_times));
		mCurIndex = violationResponse.getCurIndex();
		mMaxIndex = totalSize / DEFAULT_MAX_SIZE;
		if ((totalSize % DEFAULT_MAX_SIZE) != 0) {
			mMaxIndex++;
		}
		/*
		 * If mViolationListAdapter is null, Current is the first page.
		 */
		if (mViolationListAdapter == null) {
			mViolationListAdapter = new ViolationListAdapter(ViolationActivity.this, list);
		} else {
			mViolationListAdapter.addExtraList(list);
		}

		if ((mCurIndex < mMaxIndex) && mCurIndex == 1) {
			/*
			 * (mCurIndex < mMaxIndex) ---> It has next page. (mCurIndex == 1)
			 * ---> Current page is the first, which means no foot view is
			 * added.
			 */
			mListView.addFooterView(mLoadMoreLayout);
		} else if (mCurIndex == mMaxIndex) {
			/*
			 * mCurIndex == mMaxIndex ---> Current is the last page, foot view
			 * isn't needed.
			 */
			mListView.removeFooterView(mLoadMoreLayout);
		}
		mListView.setAdapter(mViolationListAdapter);
	}

	private View.OnClickListener mDateListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			final Calendar calendar = Calendar.getInstance();
			DatePickerDialog datePickerDialog = new DatePickerDialog(ViolationActivity.this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					calendar.set(year, monthOfYear, dayOfMonth);
					TextView textView = (TextView) v;
					textView.setText(sdf.format(calendar.getTime()));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.show();
		}
	};

}
