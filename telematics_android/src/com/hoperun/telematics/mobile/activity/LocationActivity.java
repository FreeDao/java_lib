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
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseAMapActivity;
import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;
import com.hoperun.telematics.mobile.framework.service.LocationService;
import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationProviderType;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.map.overlay.LocationOverlay;
import com.hoperun.telematics.mobile.model.location.LocationRequest;
import com.hoperun.telematics.mobile.model.location.LocationResponse;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class LocationActivity extends BaseAMapActivity {
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint mPoint;
	private LocationResponse locationResponse;
	private double latitude;
	private double longitude;
	private String TAG = "LocationActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.ui_location_layout);

		initView();

		startLocationService();

		setTitleBar(this, getResources().getString(R.string.location));
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.location_map_id);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		mMapController.setZoom(11);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseAMapActivity#onBindServiceFinish
	 * (android.content.ComponentName)
	 */
	@Override
	protected void onBindServiceFinish(ComponentName className) {
		if (className.getClassName().equals(LocationService.class.getName())) {
			getLocationInfo();
		}

		// if (className.getClassName().equals(LocationService.class.getName()))
		// locateCurrentPlace();
	}

	private void locateCurrentPlace() {
		getLastLocation(new ILocationListener() {

			@Override
			public void callback(ILocationEventArgs args) {
				Location location = args.getLocation();
				if (location == null) {
					if (args.getLocationType() == ELocationProviderType.NONE) {
						DialogHelper.alertDialog(LocationActivity.this, R.string.gps_msg, R.string.gps_ok);
					} else {
						DialogHelper.alertDialog(LocationActivity.this, R.string.gps_locate_fail, R.string.gps_ok);
					}
				} else {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					LogUtil.d(TAG, "经度：" + longitude + "," + "纬度：" + latitude);
				}

			}
		});
		// registerLocationListener(new ILocationListener() {
		//
		// @Override
		// public void callback(ILocationEventArgs args) {
		//
		//
		// }
		// });

	}

	/**
	 * 获取位置信息
	 */
	private void getLocationInfo() {
		// if (TestDataManager.IS_TEST_MODE) {
		// locationResponse =
		// TestDataManager.getInstance().getLocationResponse();
		// locationMyPlace();
		// // AMapServiceImpl aMapServiceImpl = AMapServiceImpl.getInstance();
		// // aMapServiceImpl.setTrafficMode(mMapView);
		// } else {
		startProgressDialog(ENetworkServiceType.Location);
		LocationRequest request = new LocationRequest();
		String accountid = CacheManager.getInstance().getAccountId();
		LogUtil.d(TAG, "ACCOUNTID:" + accountid);
		request.setAccountId("test");// TODO wang xiaohua
		request.setLicense("京NEB810");
		request.setVin("LDCB13R48B2065671");
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.Location, postJson, new INetCallback() {
			@Override
			public void callback(INetCallbackArgs args) {
				Context context = LocationActivity.this;

				OnClickListener retryBtnListener = new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						refresh(null);
					}
				};
				if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
					stopProgressDialog();
					String payload = args.getPayload();
					if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
						DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
					} else {
						try {
							Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
							locationResponse = gson.fromJson(payload, LocationResponse.class);
							if (NetworkCallbackHelper.isErrorResponse(context, locationResponse)) {
								NetworkCallbackHelper.alertBusinessError(context, locationResponse.getErrorCode());
							} else {
								locationMyPlace();
							}
						} catch (Exception e) {
							DialogHelper.alertDialog(LocationActivity.this, R.string.warning, R.string.system_error,
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
		// }
	}

	private void refresh(View view) {
		initReloadTimes();
		startProgressDialog(ENetworkServiceType.Track);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseAMapActivity#reload()
	 */
	@Override
	protected void reload() {

		getLocationInfo();
	}

	/**
	 * 定位，在地图上标出
	 */
	private void locationMyPlace() {
		latitude = Double.valueOf(locationResponse.getLatitude());
		longitude = Double.valueOf(locationResponse.getLongitude());
		mPoint = UtilHelper.NewGeoPoint(latitude, longitude);
		Drawable drawable = getResources().getDrawable(R.drawable.location_current);
		LocationOverlay locationOverlay = new LocationOverlay(drawable, this);
		mMapView.getOverlays().add(locationOverlay);
		OverlayItem overlayItem = new OverlayItem(mPoint, null, null);
		locationOverlay.addOverlay(overlayItem);

		mMapController.animateTo(mPoint);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.BaseMapActivity#dealHandlerRequest(android
	 * .os.Message)
	 */
	@Override
	protected void dealHandlerRequest(Message msg) {
		// TODO Auto-generated method stub
	}

}
