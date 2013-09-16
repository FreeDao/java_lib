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
import java.util.Date;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.MaintainHistoryAdapter;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.AbstractEditDateText;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DateUtil;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.helper.paging.AbstractScrollListManager;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryItem;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryRequest;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceHistoryActivity extends DefaultActivity {

	private static final String TAG = "MaintenanceHistoryActivity";

	public static final String HISTORY_INDEX_IN_LIST_FLAG = "cur_index_in_list";

	public static final String HISTORY_LIST_FLAG = "history_list";

	private TextView vinText;
	private TextView licenseText;
	private TextView startDateText;
	private TextView endDateText;
	private ListView maintainHistoryListView;
	private String startDate;
	private String endDate;
	private MaintainHistoryAdapter adapter;
	private int curIndex = 1;

	/**
	 * minimum value is 1
	 */
	private int maxIndex;
	private List<MaintenanceHistoryItem> historyList;

	private AbstractScrollListManager<MaintenanceHistoryItem> scrollManager = null;

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
		setContentView(R.layout.ui_maintain_layout);

		initViews();
		setTitleBar(this, getString(R.string.maintain_hisory_title));
		setDefaultFilterDate();

		scrollManager = new AbstractScrollListManager<MaintenanceHistoryItem>(maintainHistoryListView) {

			@Override
			protected void getNewInfo(int index) {
				curIndex = index;
				startFilter(index);
			}

			@Override
			public void updatePage() {
			}

			@Override
			protected void onShownLastPage() {
				Toast.makeText(MaintenanceHistoryActivity.this, R.string.all_shown, Toast.LENGTH_LONG).show();
			}
		};
	}

	/**
	 * 3 min after binded start getting information
	 */
	@Override
	protected void onBindServiceFinish(ComponentName className) {
		super.onBindServiceFinish(className);
		if (className.getClassName().equals(NetworkService.class.getName())) {
			scrollManager.startFirstLoading();
		}

	}

	/**
	 * 
	 */
	private void initViews() {
		vinText = (TextView) findViewById(R.id.vinText);
		licenseText = (TextView) findViewById(R.id.licenseText);
		startDateText = (TextView) findViewById(R.id.startDateText);
		endDateText = (TextView) findViewById(R.id.endDateText);
		maintainHistoryListView = (ListView) findViewById(R.id.maintainList);

		CacheManager cache = CacheManager.getInstance();
		String vin = cache.getVin();
		
		if (vin == null) {
			vin = getString(R.string.testVin);
		}
		String license = cache.getLicense();
		if (license == null) {
			license = getString(R.string.testLicense1);
		}
		vinText.setText(String.format(getString(R.string.show_vin), vin));
		licenseText.setText(String.format(getString(R.string.show_license), license));
		
		maintainHistoryListView.setOnItemClickListener(itemClickListener);
	}

	private ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent i = new Intent(MaintenanceHistoryActivity.this, MaintainHistoryDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(HISTORY_INDEX_IN_LIST_FLAG, position);
			bundle.putParcelableArrayList(HISTORY_LIST_FLAG, (ArrayList<MaintenanceHistoryItem>) adapter.getList());
			i.putExtras(bundle);
			startActivity(i);
		}
	};

	private void setDefaultFilterDate() {
		CacheManager cache = CacheManager.getInstance();
		Date registerDate = cache.getRegistrationDate();
		String registerDateStr;
		if (registerDate != null) {
			registerDateStr = DateUtil.getCommonFormat(registerDate);
		} else {
			registerDateStr = TestDataManager.getInstance().getRegisterDate();
			registerDate = DateUtil.getDate(registerDateStr);
		}

		startDateText.setText(registerDateStr);
		new AbstractEditDateText(this, startDateText, registerDate) {

			@Override
			protected void setDateStr(String dateStr) {
				startDate = dateStr;
			}
		};
		Date today = new Date();
		endDateText.setText(DateUtil.getCommonFormat(today));
		new AbstractEditDateText(this, endDateText, today) {
			@Override
			protected void setDateStr(String dateStr) {
				endDate = dateStr;
			}
		};
	}

	/**
	 * called when the filterBtn button is clicked
	 * 
	 * @param v
	 */
	public void startFilter(View v) {
		curIndex = 1;
		adapter = null;
		scrollManager.startFirstLoading();
	}

	public void startFilter(int index) {
		startProgressDialog(ENetworkServiceType.MaintenanceHistory);
		// if (TestDataManager.IS_TEST_MODE) {
		// mCallBack2.callback(null);
		// } else {
		CacheManager cache = CacheManager.getInstance();
		String vin = cache.getVin();
		
		if (vin == null) {
			vin = getString(R.string.testVin);
		}
		String license = cache.getLicense();
		if (license == null) {
			license = getString(R.string.testLicense1);
		}
		MaintenanceHistoryRequest request = new MaintenanceHistoryRequest(vin, license, startDate, endDate,
				String.format("%s", index), String.format("%s", Constants.MAX_SIZE_OF_LIST_IN_PAGE));
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.MaintenanceHistory, postJson, mCallBack);
		// }
	}

	private INetCallback mCallBack = new INetCallback() {

		@Override
		public void callback(INetCallbackArgs args) {
			// String payload = args.getPayload();
			// MaintenanceHistoryResponse response =
			// MaintenanceHistoryResponse.toObject(payload);
			// historyList = response.getHistoryList();
			// int totalSize = response.getTotalSize();
			// int perNum = historyList.size();
			// maxIndex = totalSize % perNum == 0 ? totalSize / perNum :
			// (totalSize / perNum + 1);
			// showListView();
			stopProgressDialog();
			Context context = MaintenanceHistoryActivity.this;

			OnClickListener retryBtnListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startFilter(curIndex);
				}
			};
			// 检查是否有非业务级的错误
			if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
				String payload = args.getPayload();
				if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
					DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
				} else {
					Gson gson = new Gson();
					MaintenanceHistoryResponse response = gson.fromJson(payload, MaintenanceHistoryResponse.class);
					if (NetworkCallbackHelper.isErrorResponse(context, response)) {
						// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
						NetworkCallbackHelper.alertBusinessError(context, response.getErrorCode());
					} else {
						historyList = response.getHistoryList();
						int totalSize = response.getTotalSize();
						int perNum = historyList.size();
						maxIndex = totalSize % perNum == 0 ? totalSize / perNum : (totalSize / perNum + 1);
						showListView();
					}
				}
			} else {
				// 根据各接口情况选择重试或直接提示
				String errMsg = args.getErrorMessage();
				Log.e(TAG, errMsg);
				errMsg = getString(R.string.error_async_return_fault);
				DialogHelper.alertDialog(context, errMsg, retryBtnListener);
			}
		}
	};

	private INetCallback mCallBack2 = new INetCallback() {

		@Override
		public void callback(INetCallbackArgs args) {
			int curIndex = 1;
			if (scrollManager != null) {
				curIndex = scrollManager.getCurIndex();
			}
			MaintenanceHistoryResponse response = TestDataManager.getInstance().getMaintenanceHistory(curIndex);
			historyList = response.getHistoryList();
			int totalSize = response.getTotalSize();
			int perNum = historyList.size();
			maxIndex = totalSize % perNum == 0 ? totalSize / perNum : (totalSize / perNum + 1);
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1500);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// stopProgressDialog();
								showListView();
							}
						});

					} catch (InterruptedException e) {
						Log.e(TAG, "test data error:" + e);
					}
				}
			}.start();

		}
	};

	@Override
	protected void onProgressDialogCancel() {
	}

	private void showListView() {
		if (adapter == null) {
			adapter = new MaintainHistoryAdapter(this, historyList);
			scrollManager.initListView(adapter, maxIndex);
		} else {
			scrollManager.updatePage(adapter, historyList);
		}
	}
}
