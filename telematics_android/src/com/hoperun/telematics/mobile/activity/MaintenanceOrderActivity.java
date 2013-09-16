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

import java.util.Date;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.MaintainOrderAdapter;
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
import com.hoperun.telematics.mobile.model.maintenance.order.MaintenanceOrderItem;
import com.hoperun.telematics.mobile.model.maintenance.order.MaintenanceOrderRequest;
import com.hoperun.telematics.mobile.model.maintenance.order.MaintenanceOrderResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceOrderActivity extends DefaultActivity {

	private static final String TAG = "MaintenanceOrderActivity";

	private TextView vinText;
	private TextView licenseText;
	private TextView startDateText;
	private TextView endDateText;
	private ListView maintainOrderListView;
	private String startDate;
	private String endDate;
	private MaintainOrderAdapter adapter;

	/**
	 * minimum value is 1
	 */
	private int curIndex = 1;
	private int maxIndex;
	private List<MaintenanceOrderItem> orderList;

	private AbstractScrollListManager<MaintenanceOrderItem> scrollManager = null;

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
		setTitleBar(this, getString(R.string.maintain_order_title));
		setDefaultFilterDate();
		scrollManager = new AbstractScrollListManager<MaintenanceOrderItem>(maintainOrderListView) {

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
				Toast.makeText(MaintenanceOrderActivity.this, R.string.all_shown, Toast.LENGTH_LONG).show();
			}
		};
		// startFilter(Constants.MIN_INDEX_OF_LIST_IN_PAGE);
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
		maintainOrderListView = (ListView) findViewById(R.id.maintainList);

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
	}

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
		startProgressDialog(ENetworkServiceType.MaintenanceOrders);
		// if (TestDataManager.IS_TEST_MODE) {
		// mCallBack2.callback(null);
		// } else {
		CacheManager cache = CacheManager.getInstance();
		String vin = cache.getVin();
		String license = cache.getLicense();
		if (vin == null) {
			vin = getString(R.string.testVin);
		}
		if (license == null) {
			license = getString(R.string.testLicense1);
		}
		MaintenanceOrderRequest request = new MaintenanceOrderRequest(vin, license, startDate, endDate, String.format(
				"%s", index), String.format("%s", Constants.MAX_SIZE_OF_LIST_IN_PAGE));
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.MaintenanceOrders, postJson, mCallBack);
		// }
	}

	private INetCallback mCallBack = new INetCallback() {

		@Override
		public void callback(INetCallbackArgs args) {
			stopProgressDialog();
			Context context = MaintenanceOrderActivity.this;

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
					MaintenanceOrderResponse response = gson.fromJson(payload, MaintenanceOrderResponse.class);
					if (NetworkCallbackHelper.isErrorResponse(context, response)) {
						// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
						NetworkCallbackHelper.alertBusinessError(context, response.getErrorCode());
					} else {
						orderList = response.getOrderList();
						int totalSize = response.getTotalSize();
						int perNum = orderList.size();
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
			if (scrollManager != null) {
				curIndex = scrollManager.getCurIndex() + 1;
			} else {
				curIndex = 1;
			}
			MaintenanceOrderResponse response = TestDataManager.getInstance().getMaintenanceOrder(curIndex);
			orderList = response.getOrderList();
			int totalSize = response.getTotalSize();
			int perNum = orderList.size();
			maxIndex = totalSize % perNum == 0 ? totalSize / perNum : (totalSize / perNum + 1);
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1500);
						stopProgressDialog();
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

	/**
	 * @param orderList
	 */
	private void showListView() {
		if (adapter == null) {
			adapter = new MaintainOrderAdapter(this, orderList);
			scrollManager.initListView(adapter, maxIndex);
		} else {
			scrollManager.updatePage(adapter, orderList);
		}
	}

	// private void updateFooterBar() {
	// int maxSize = totalSize / Constants.MAX_SIZE_OF_LIST_IN_PAGE;
	// if (totalSize % Constants.MAX_SIZE_OF_LIST_IN_PAGE != 0) {
	// maxSize++;
	// }
	// pageTransferHelper = new AbsArrowPagingBar( curIndex - 1, maxSize) {
	//
	// @Override
	// public void updatePage() {
	// startProgressDialog();
	// startFilter(pageTransferHelper.getCurIndex() + 1);
	// }
	// };
	// }
}
