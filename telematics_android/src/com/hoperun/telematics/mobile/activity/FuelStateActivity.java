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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.AnimationHelper;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.model.fuel.FuelRequest;
import com.hoperun.telematics.mobile.model.fuel.FuelResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class FuelStateActivity extends DefaultActivity {

	private ImageView pointerImage;
	private TextView consumeText;
	private TextView remainText;
	private static final String TAG = "FuelStateActivity";
	private FuelResponse fuelResponse;

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
		setContentView(R.layout.ui_fuel_layout);
		initViews();
		setTitleBar(this, getString(R.string.fuel_title));
	}

	/**
	 * 
	 */
	private void initViews() {
		pointerImage = (ImageView) findViewById(R.id.pointerImage);
		consumeText = (TextView) findViewById(R.id.consumeText);
		remainText = (TextView) findViewById(R.id.remainText);
	}

	@Override
	protected void onBindServiceFinish(ComponentName className) {
		super.onBindServiceFinish(className);
		if (className.getClassName().equals(NetworkService.class.getName())) {
			startProgressDialog(ENetworkServiceType.Fuel);
			getFuelInfo();
		}
	}

	/**
	 * 
	 */
	private void getFuelInfo() {
		// leilei,test code if
		// if (TestDataManager.IS_TEST_MODE) {
		// mCallBack2.callback(null);
		// } else {
		String vin = CacheManager.getInstance().getVin();
		if (vin == null) {
			vin = getString(R.string.testVin);
		}
		String license = CacheManager.getInstance().getLicense();
		if (license == null) {
			license = getString(R.string.testLicense1);
		}
		FuelRequest request = new FuelRequest(vin, license);
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.Fuel, postJson, mCallBack);
		// }
	}

	// NetworkCallbackHelper.IErrorEventListener displayer = new
	// NetworkCallbackHelper.IErrorEventListener() {
	// @Override
	// public void onResponseReturned(BaseResponse response) {
	// fuelResponse = (FuelResponse) response;
	// updateViews(fuelResponse);
	// }
	//
	// @Override
	// public void onRetryButtonClicked() {
	// // refresh(null);
	// }
	// };
	// NetworkCallbackHelper.showInfoFromResponse(FuelStateActivity.this, args,
	// FuelResponse.class, displayer);
	private INetCallback mCallBack = new INetCallback() {

		@Override
		public void callback(final INetCallbackArgs args) {
			stopProgressDialog();
			Context context = FuelStateActivity.this;

			OnClickListener retryBtnListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startProgressDialog(ENetworkServiceType.Fuel);
				}
			};
			// 检查是否有非业务级的错误
			if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
				stopProgressDialog();
				String payload = args.getPayload();
				if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
					DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
				} else {
					Gson gson = new Gson();
					fuelResponse = gson.fromJson(payload, FuelResponse.class);
					if (NetworkCallbackHelper.isErrorResponse(context, fuelResponse)) {
						// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
						NetworkCallbackHelper.alertBusinessError(context, fuelResponse.getErrorCode());
					} else {
						updateViews(fuelResponse);
					}
				}
			} else {
				// 根据各接口情况选择重试或直接提示
				String errMsg = args.getErrorMessage();
				Log.e(TAG, errMsg);
				errMsg = getString(R.string.error_async_return_fault);
				startReload(errMsg, retryBtnListener);
			}
		}
	};
	//  leilei,test code
//	private INetCallback mCallBack2 = new INetCallback() {
//
//		@Override
//		public void callback(INetCallbackArgs args) {
//			new Thread() {
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(1500);
//						stopProgressDialog();
//						mHandler.post(new Runnable() {
//
//							@Override
//							public void run() {
//								updateViews(TestDataManager.getInstance().getFuelInfo());
//							}
//						});
//					} catch (InterruptedException e) {
//						Log.e(TAG, e.getMessage(), e);
//					}
//				}
//			}.start();
//
//		}
//	};
	private static final long ANIMATION_DURATION = 1000;
	private static final float ANIMATION_START_DEGREE = 0;
	private static final float ANIMATION_END_DEGREE = 108;
	private static final float ANIMATION_DEGREE_RANGE = ANIMATION_END_DEGREE - ANIMATION_START_DEGREE;

	private void updateViews(FuelResponse response) {
		consumeText.setText(getString(R.string.fuel_consume, response.getConsumption()));
		remainText.setText(getString(R.string.fuel_remaining, response.getRemainingMileage()));
		float maxFuel = response.getMaxFuel();
		float remainFuel = response.getRemainingFuel();
		float endDegree = ANIMATION_DEGREE_RANGE * remainFuel / maxFuel;
		AnimationHelper.startOnceRotateAnimation(pointerImage, ANIMATION_START_DEGREE, endDegree, ANIMATION_DURATION);

	}
}