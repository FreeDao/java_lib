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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseAMapActivity;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.map.seivice.impl.AMapServiceImpl;
import com.hoperun.telematics.mobile.model.location.GeoLocation;
import com.hoperun.telematics.mobile.model.track.TrackInfo;
import com.hoperun.telematics.mobile.model.track.TrackRequest;
import com.hoperun.telematics.mobile.model.track.TrackResponse;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class TrackActivity extends BaseAMapActivity {
	private TrackResponse trackResponse;
	private List<TrackInfo> trackList;
	private MapView mapView;
	private MapController mapController;
	public static final int GET_TRACK_SUCCESS = 2001;
	private String TAG = "TrackActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_track_map_layout);

		initView();
		setTitleBar(this, getResources().getString(R.string.track));

	}

	private void drawOnMap() {
		trackList = trackResponse.getTrackList();
		if (trackList.size() != 0) {
			List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
			List<GeoPoint> pointList = new ArrayList<GeoPoint>();
			for (TrackInfo track : trackList) {
				GeoLocation location = new GeoLocation();
				location.setTime(track.getTime().toString());
				location.setAddress(track.getAddress());
				location.setLat(Double.valueOf(track.getLatitude()));
				location.setLng(Double.valueOf(track.getLongitude()));
				geoLocations.add(location);
				pointList.add(track.getGeoPoint());
			}
			//show all points on mapview
			mapController.setFitView(pointList);
			AMapServiceImpl aMapServiceImpl = AMapServiceImpl.getInstance();
			aMapServiceImpl.drawPointsOnMap(geoLocations, mapView, this, R.layout.ui_track_pop_layout,
					R.layout.ui_track_pin_layout);
		}
	}

	private void initView() {
		mapView = (MapView) findViewById(R.id.track_mapView_id);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(11);

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
		if (className.getClassName().equals(NetworkService.class.getName()))
			getTrackList();
	}

	private void getTrackList() {
		// if (TestDataManager.IS_TEST_MODE) {
		// trackResponse = TestDataManager.getInstance().getTrackResponse();
		// } else {
		startProgressDialog(ENetworkServiceType.Track);
		TrackRequest request = new TrackRequest();
		String accountid = CacheManager.getInstance().getAccountId();
		LogUtil.d(TAG, "ACCOUNTID:" + accountid);
		request.setAccountId("test");// TODO wang xiaohua
		request.setTimeFrom(Timestamp.valueOf("2012-03-04 12:34:12.0"));
		request.setTimeTo(Timestamp.valueOf("2012-03-06 12:34:12.0"));
		String postJson = request.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.Track, postJson, new INetCallback() {
			@Override
			public void callback(INetCallbackArgs args) {
				
				Context context = TrackActivity.this;

				OnClickListener retryBtnListener = new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						refresh(null);
					}
				};
				if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
					stopProgressDialog();
					String payload = args.getPayload();
					LogUtil.d(TAG, payload);
					if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
						DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
					} else {
						try {
							Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
							trackResponse = gson.fromJson(payload, TrackResponse.class);
							if (NetworkCallbackHelper.isErrorResponse(context, trackResponse)) {
								NetworkCallbackHelper.alertBusinessError(context, trackResponse.getErrorCode());
							} else {
								drawOnMap();
							}
						} catch (Exception e) {
							DialogHelper.alertDialog(TrackActivity.this, R.string.warning, R.string.system_error,
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
		getTrackList();
	}

	@Override
	protected void dealHandlerRequest(Message msg) {
		super.dealHandlerRequest(msg);
		switch (msg.what) {
		case GET_TRACK_SUCCESS:
			drawOnMap();
			break;

		default:
			break;
		}
	}

}
