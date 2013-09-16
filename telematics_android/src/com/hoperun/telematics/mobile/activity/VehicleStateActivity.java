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

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.hoperun.telematics.mobile.helper.SharedPreferencesUtil;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.model.states.StatesRequest;
import com.hoperun.telematics.mobile.model.states.StatesResponse;
import com.hoperun.telematics.mobile.model.states.VehicleState;
import com.hoperun.telematics.mobile.model.weather.WeatherResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class VehicleStateActivity extends DefaultActivity {

	private final static String TAG = VehicleStateActivity.class.getName();
	private TextView vinText;
	private TextView licenseText;
	private TextView faultSummaryText;
	private ImageView faultImage;
	private List<VehicleState> stateList;
	private Button engineStateBtn;
	private Button tireStateBtn;
	private Button airConditionBtn;
	private int selectedStateIndex = -1;
	private View focusButton;
	private Button lightStateBtn;
	private ArrayList<Button> positionBtns = new ArrayList<Button>();
	private Button stateDetailBtn;
	public final static String STATE_LIST_FLAG = "vehicle_state_list";
	public final static String STATE_INDEX_IN_LIST_FLAG = "current_state_index";
	//在onstart的时候做判断是否在loading和服务是否绑定了
	private boolean isLoading = false;
	private boolean isBinded = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSummaryViews();
		setTitleBar(this, getString(R.string.state_title));
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
		super.onBindServiceFinish(className);
		if (className.getClassName().equals(NetworkService.class.getName())) {
			isBinded = true;
			startProgressDialog(ENetworkServiceType.VehicleCondition);
			getVehicleStates();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// if(isBinded && !isLoading){
		// startProgressDialog();
		// getVehicleStates();
		// }
	}

	private void initSummaryViews() {
		setContentView(R.layout.ui_vehicle_state_summary_layout);
		vinText = (TextView) findViewById(R.id.vinText);
		licenseText = (TextView) findViewById(R.id.licenseText);
		faultSummaryText = (TextView) findViewById(R.id.faultSummaryText);
		faultImage = (ImageView) findViewById(R.id.faultImage);
		engineStateBtn = (Button) findViewById(R.id.engineStateBtn);
		tireStateBtn = (Button) findViewById(R.id.tireStateBtn);
		airConditionBtn = (Button) findViewById(R.id.airConditionBtn);
		lightStateBtn = (Button) findViewById(R.id.lightStateBtn);
		stateDetailBtn = (Button) findViewById(R.id.stateDetailBtn);
		positionBtns.add(airConditionBtn);
		positionBtns.add(tireStateBtn);
		positionBtns.add(lightStateBtn);
		positionBtns.add(engineStateBtn);
		
		setVinLicense();
		
	}

	private void getVehicleStates() {
		isLoading = true;
		CacheManager cache = CacheManager.getInstance();
		String vin = cache.getVin();
		if(vin == null){
			vin = getString(R.string.testVin);
		}
		String license = cache.getLicense();
		if(license == null){
			license = getString(R.string.testLicense1);
		}
		StatesRequest request = new StatesRequest(vin,license);
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.VehicleCondition, postJson, mCallBack);
	}

	private INetCallback mCallBack = new INetCallback() {

		@Override
		public void callback(INetCallbackArgs args) {

			Context context = VehicleStateActivity.this;

			// 根据实际情况看是否有必要添加重试功能
			OnClickListener retryBtnListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startProgressDialog(ENetworkServiceType.VehicleCondition);
					getVehicleStates();
				}
			};
			// 检查是否有非业务级的错误
			if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
				isLoading = false;
				stopProgressDialog();
				String payload = args.getPayload();
				if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
					DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
				} else {
					Gson gson = new Gson();
					StatesResponse response = gson.fromJson(payload, StatesResponse.class);
					if (NetworkCallbackHelper.isErrorResponse(context, response)) {
						NetworkCallbackHelper.alertBusinessError(context, response.getErrorCode());
					} else {
						stateList = response.getStateList();
						showSummaryInfo(stateList);
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
	};

	/**
	 * 重写重新加载的时候调用的方法
	 */
	protected void reload() {
		getVehicleStates();
	}

	// private INetCallback mCallBack2 = new INetCallback() {
	//
	// @Override
	// public void callback(INetCallbackArgs args) {
	// stateList =
	// TestDataManager.getInstance().getVehicleState().getStateList();
	// new Thread() {
	// @Override
	// public void run() {
	// try {
	// Thread.sleep(1500);
	// stopProgressDialog();
	// } catch (InterruptedException e) {
	// Log.e(TAG, "test data error:" + e);
	// }
	// }
	// }.start();
	// }
	// };

	private void showSummaryInfo(List<VehicleState> list) {
		setVinLicense();
		for (int i = 0; i < list.size(); i++) {
			checkPositionBtn(i);
		}
	}
	
	private void setVinLicense(){
		CacheManager cache = CacheManager.getInstance();
		String vin = cache.getVin();
		if(vin == null){
			vin = getString(R.string.testVin);
		}
		String license = cache.getLicense();
		if(license == null){
			license = getString(R.string.testLicense1);
		}
		vinText.setText(String.format(getString(R.string.show_vin), vin));
		licenseText.setText(String.format(getString(R.string.show_license), license));
	}

	//根据faultId显示故障位置
	/**
	 * {"stateList":[{"description":"","faultId":"100","faultLevel":1,"positionId":"","positionName":"发动机/变速箱","suggestion":""},
	 * {"description":"","faultId":"201","faultLevel":1,"positionId":"","positionName":"制动防抱死系统","suggestion":"安排维修保养"},
	 * {"description":"","faultId":"303","faultLevel":3,"positionId":"","positionName":"车身稳定控制系统","suggestion":"安排维修保养"},
	 * {"description":"","faultId":"400","faultLevel":1,"positionId":"","positionName":"安全气囊系统","suggestion":""},
	 * {"description":"","faultId":"502","faultLevel":2,"positionId":"","positionName":"排放系统","suggestion":"fsdfsdfsd"},
	 * {"description":"","faultId":"600","faultLevel":1,"positionId":"","positionName":"设备终端","suggestion":""},
	 * {"description":"","faultId":"700","faultLevel":1,"positionId":"","positionName":"里程表读数","suggestion":"安排维修保养"},
	 * {"description":"","faultId":"800","faultLevel":1,"positionId":"","positionName":"剩余机油寿命","suggestion":""},
	 * {"description":"","faultId":"901","faultLevel":1,"positionId":"","positionName":"车胎压力","suggestion":"补气"}]}
	 */
	private void checkPositionBtn(final int faultIndex) {
		VehicleState vehicleState = stateList.get(faultIndex);
		for (int i = 0; i < VehicleState.FaultPositionId.positionIdArray.length; i++) {
			String positionId = vehicleState.getPositionId();
			if (positionId.equals(VehicleState.FaultPositionId.positionIdArray[i])) {
				positionBtns.get(i).setBackgroundResource(R.drawable.diagnose_piont_red);
				// TODO fanleilei,set images
				faultImage.setImageResource(R.drawable.diagnose_icon_warning);
				switch (vehicleState.getFaultLevel()) {
				// TODO faultImage.setSrc();
				}
				positionBtns.get(i).setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						faultSummaryText.setText(stateList.get(faultIndex).getDescription());

						// control animation
						if (focusButton != null) {
							focusButton.clearAnimation();
						}
						AnimationHelper.setFlickerAnimation(v);

						selectedStateIndex = faultIndex;
						focusButton = v;
					}
				});
				if (-1 == selectedStateIndex) {
					positionBtns.get(i).performClick();
				}
				break;
			}
		}
	}
	
	//根据positionId 显示故障位置
//	private void checkPositionBtn(final int faultIndex) {
//		VehicleState vehicleState = stateList.get(faultIndex);
//		for (int i = 0; i < VehicleState.FaultPositionId.positionIdArray.length; i++) {
//			String positionId = vehicleState.getPositionId();
//			if (positionId.equals(VehicleState.FaultPositionId.positionIdArray[i])) {
//				positionBtns.get(i).setBackgroundResource(R.drawable.diagnose_piont_red);
//				// TODO fanleilei,set images
//				faultImage.setImageResource(R.drawable.diagnose_icon_warning);
//				switch (vehicleState.getFaultLevel()) {
//				// TODO faultImage.setSrc();
//				}
//				positionBtns.get(i).setOnClickListener(new Button.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						faultSummaryText.setText(stateList.get(faultIndex).getDescription());
//
//						// control animation
//						if (focusButton != null) {
//							focusButton.clearAnimation();
//						}
//						AnimationHelper.setFlickerAnimation(v);
//
//						selectedStateIndex = faultIndex;
//						focusButton = v;
//					}
//				});
//				if (-1 == selectedStateIndex) {
//					positionBtns.get(i).performClick();
//				}
//				break;
//			}
//		}
//	}

	/**
	 * show the detail of vehicle state
	 */
	public void showDetail(View v) {
		if (selectedStateIndex >-1 && stateList != null || stateList.size() > selectedStateIndex) {
			Intent i = new Intent(VehicleStateActivity.this, VehicleStateDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(STATE_INDEX_IN_LIST_FLAG, this.selectedStateIndex);
			bundle.putParcelableArrayList(STATE_LIST_FLAG, (ArrayList<VehicleState>) stateList);
			i.putExtras(bundle);
			startActivity(i);
		}
	}
	
//	private static final String SHARE_KEY_STATE_INFO = "shared_state_key";
//	private void saveStateInfo(String jsonStr) {
//		SharedPreferencesUtil.getInstance().putString(SHARE_KEY_STATE_INFO, jsonStr);
//	}
//
//	private StatesResponse getSavedInfo() {
//		String payload = SharedPreferencesUtil.getInstance().getString(SHARE_KEY_STATE_INFO);
//		if (payload == null || payload.trim().length()==0) {
//			return TestDataManager.getInstance().getVehicleState();
//		} else {
//			Gson gson = new Gson();
//			return gson.fromJson(payload, StatesResponse.class);
//		}
//	}
}
