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

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.route.Route;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseAMapActivity;
import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.IntentParamCache;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.map.seivice.impl.AMapServiceImpl;
import com.hoperun.telematics.mobile.model.buddy.Friend;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class FriendDetailActivity extends BaseAMapActivity implements OnClickListener {
	private TextView nickname;
	private TextView note;
	private TextView address;
	private Button getRoute;
	private ImageView image;
	private MapView mapView;
	private MapController mapController;
	private Friend friend;
	private int index;
	private AMapServiceImpl aMapServiceImpl;
	private String TAG = "FriendDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_friend_detail_layout);

		initView();
		setTitleBar(this, getResources().getString(R.string.friend));
		Intent intent = getIntent();
		index = intent.getIntExtra(FriendActivity.FRIEND_POSITION, 0);
		friend = (Friend) IntentParamCache.getInstance().getElementByKey(Constants.FRIEND_POSITION);

		startLocationService();

		getRoute.setOnClickListener(this);
		image.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		nickname.setText(friend.getNickName());
		address.setText(friend.getAddress());
		note.setText(friend.getNote());

		aMapServiceImpl = new AMapServiceImpl();
		aMapServiceImpl.cleanMapView(mapView);
		aMapServiceImpl.drawPointOnMap(getApplicationContext(), mapView, friend.getGeoPoint(), index,
				R.layout.ui_map_pin_layout, null, null);
		mapController.setCenter(friend.getGeoPoint());

	}

	/**
	 * 
	 */
	private void initView() {
		nickname = (TextView) findViewById(R.id.friend_item_nickname);
		note = (TextView) findViewById(R.id.friend_item_note);
		address = (TextView) findViewById(R.id.friend_item_address);
		image = (ImageView) findViewById(R.id.friend_map_direction_image);
		// image.setVisibility(View.GONE);
		getRoute = (Button) findViewById(R.id.friend_get_direction_btn);
		mapView = (MapView) findViewById(R.id.gas_mapView_id);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(12);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_get_direction_btn:
			final ProgressDialog pd = new ProgressDialog(FriendDetailActivity.this);
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					final GeoPoint mPoint = UtilHelper.NewGeoPoint(32.6666, 116.3426);
					if (aMapServiceImpl.drawRouteSuccess(FriendDetailActivity.this, FriendDetailActivity.this, mapView,
							mPoint, friend.getGeoPoint(), Route.DrivingDefault)) {
						pd.dismiss();
						mHandler.sendEmptyMessage(Constants.GET_ROUTE_SUCCESS);
					} else {
						pd.dismiss();
						mHandler.sendEmptyMessage(Constants.GET_ROUTE_FAIL);
					}
				}
			});
			DialogHelper.progressDialog(pd, FriendDetailActivity.this, ProgressDialog.STYLE_SPINNER, getResources()
					.getString(R.string.get_route));
			thread.start();
			break;

		case R.id.friend_map_direction_image:
			// autoGetLocation();
			locateCurrentPlace();
			break;
		}

	}

	private void locateCurrentPlace() {
		// getLastLocation(new ILocationListener() {
		//
		// @Override
		// public void callback(ILocationEventArgs args) {
		// Location location = args.getLocation();
		// if (location == null) {
		// if (args.getLocationType() == ELocationProviderType.NONE) {
		// DialogHelper.alertDialog(FriendDetailActivity.this, R.string.gps_msg,
		// R.string.gps_ok);
		// } else {
		// DialogHelper.alertDialog(FriendDetailActivity.this,
		// R.string.gps_locate_fail, R.string.gps_ok);
		// }
		// } else {
		// double latitude = location.getLatitude();
		// double longitude = location.getLongitude();
		// LogUtil.d(TAG, "经度：" + longitude + "," + "纬度：" + latitude);
		// Geocoder coder = new Geocoder(FriendDetailActivity.this);
		// // 纠偏
		// List<Address> addlst;
		// try {
		// addlst = coder.getFromRawGpsLocation(latitude, longitude, 3);
		//
		// if (addlst != null) {
		// Address add = addlst.get(0);
		// double lat = add.getLatitude();
		// double lng = add.getLongitude();
		// LogUtil.d(TAG, "经度：" + longitude + "," + "纬度：" + latitude);
		// GeoPoint point = UtilHelper.NewGeoPoint(lat, lng);
		// Drawable drawable =
		// getResources().getDrawable(R.drawable.location_current);
		// LocationOverlay locationOverlay = new LocationOverlay(drawable,
		// FriendDetailActivity.this);
		// mapView.getOverlays().add(locationOverlay);
		// OverlayItem overlayItem = new OverlayItem(point, null, null);
		// locationOverlay.addOverlay(overlayItem);
		// mapController.animateTo(point);
		// }} catch (AMapException e) {
		// LogUtil.d(TAG,e.getMessage(),e);
		// }
		// }
		//
		// }
		// });

		registerLocationListener(new ILocationListener() {

			@Override
			public void callback(ILocationEventArgs args) {
				LogUtil.d(TAG, "registerlocationlistener callback start");
				Location location = args.getLocation();

				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				Toast.makeText(FriendDetailActivity.this, latitude + "," + longitude, Toast.LENGTH_SHORT).show();
				LogUtil.d(TAG, latitude + "..." + longitude);

			}
		});
	}

	@Override
	protected void dealHandlerRequest(Message msg) {
		super.dealHandlerRequest(msg);
		switch (msg.what) {
		case Constants.GET_ROUTE_FAIL:
			DialogHelper.showToastText(FriendDetailActivity.this, getResources().getString(R.string.get_route_fail));
			break;
		case Constants.GET_ROUTE_SUCCESS:
			mapView.invalidate();
			break;
		default:
			break;
		}
	}

}
