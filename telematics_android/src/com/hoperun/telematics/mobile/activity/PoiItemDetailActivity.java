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

import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.amap.mapapi.route.Route;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseAMapActivity;
import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;
import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationProviderType;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback;
import com.hoperun.telematics.mobile.helper.IntentParamCache;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.map.seivice.impl.AMapServiceImpl;
import com.hoperun.telematics.mobile.model.poi.Poi;
import com.hoperun.telematics.mobile.model.poi.PoiRequest.EPoiRequestType;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class PoiItemDetailActivity extends BaseAMapActivity implements OnClickListener {
	private MyLocationOverlay locationOverlay;
	private TextView title;
	private TextView address;
	private TextView distance;
	private TextView price;
	private LinearLayout decriptionLayout;
	private TextView gasDiscountdescText;
	private TextView gasDescription;
	private TextView gasDiscountdesc;
	private Button getRoute;
	private MapView mapView;
	private MapController mapController;
	private ImageView image;
	private double latitude;
	private double longitude;
	private GeoPoint point;
	private Poi poi;
	private int index;
	private AMapServiceImpl aMapServiceImpl;
	private String TAG = "PoiItemDetailActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.ui_poi_item_detail_layout);

		Intent intent = getIntent();
		index = intent.getIntExtra(PoiSearchResultActivity.POI_POSITION, 0);
		initView();
		startLocationService();
		getRoute.setOnClickListener(this);
		image.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		initViewData();

	}

	/**
	 * init view
	 */
	private void initView() {
		title = (TextView) findViewById(R.id.gas_title_address_id);
		address = (TextView) findViewById(R.id.gas_address_id);
		distance = (TextView) findViewById(R.id.gas_distance_id);
		price = (TextView) findViewById(R.id.gas_price_id);
		gasDescription = (TextView) findViewById(R.id.gas_favorable_id);
		decriptionLayout = (LinearLayout) findViewById(R.id.gas_favorable);
		gasDiscountdescText = (TextView) findViewById(R.id.gas_discountdesc);
		gasDiscountdesc = (TextView) findViewById(R.id.gas_discountdesc_id);
		getRoute = (Button) findViewById(R.id.gas_get_direction_btn);
		image = (ImageView) findViewById(R.id.map_direction_image);
		image.setVisibility(View.GONE);
		mapView = (MapView) findViewById(R.id.gas_mapView_id);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(12);

	}

	private void initViewData() {
		IntentParamCache intentCache = IntentParamCache.getInstance();
		poi = (Poi) intentCache.getElementByKey(Constants.POI_INDEX_RESULT);

		title.setText(poi.getName());
		address.setText(poi.getAddress());
		latitude = Double.valueOf(poi.getLatitude());
		longitude = Double.valueOf(poi.getLongitude());

		point = UtilHelper.NewGeoPoint(latitude, longitude);
		aMapServiceImpl = AMapServiceImpl.getInstance();
		aMapServiceImpl.drawPointOnMap(getApplicationContext(), mapView, point, index, R.layout.ui_map_pin_layout,
				null, null);
		mapController.setCenter(point);

		EPoiRequestType e = (EPoiRequestType) IntentParamCache.getInstance().getElementByKey(Constants.POI_TYPE);
		Map<Object, Object> map = poi.getExtInfo();
		price.setText((String) map.get("price"));
		switch (e) {
		case Parking:
			String parking = getResources().getString(R.string.poi_parking_str);
			setTitleBar(this, parking);
			decriptionLayout.setVisibility(View.GONE);
			gasDiscountdescText.setText(getResources().getString(R.string.park_saturation));
			gasDiscountdesc.setText((String) map.get("saturation"));
			break;
		case GasStation:
			String gasstation = getResources().getString(R.string.poi_gasstation_str);
			gasDescription.setText((String) map.get("description"));
			gasDiscountdescText.setText(getResources().getString(R.string.gas_discountdesc));
			gasDiscountdesc.setText((String) map.get("discountdesc"));
			setTitleBar(this, gasstation);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gas_get_direction_btn:
			final ProgressDialog pd = new ProgressDialog(PoiItemDetailActivity.this);
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					final GeoPoint mPoint = UtilHelper.NewGeoPoint(32.6666, 116.3426);
					if (aMapServiceImpl.drawRouteSuccess(PoiItemDetailActivity.this, PoiItemDetailActivity.this,
							mapView, mPoint, poi.getGeoPoint(), Route.DrivingLeastDistance)) {
						pd.dismiss();
					} else {
						pd.dismiss();
						mHandler.sendEmptyMessage(Constants.GET_ROUTE_FAIL);
					}
				}
			});
			DialogHelper.progressDialog(pd, PoiItemDetailActivity.this, ProgressDialog.STYLE_SPINNER, getResources()
					.getString(R.string.get_route));
			thread.start();
			break;

		case R.id.map_direction_image:
			// autoGetLocation();
			// locateCurrentPlace();
			break;
		}

	}

	private void locateCurrentPlace() {
		getLastLocation(new ILocationListener() {

			@Override
			public void callback(ILocationEventArgs args) {
				Location location = args.getLocation();
				if (location == null) {
					if (args.getLocationType() == ELocationProviderType.NONE) {
						DialogHelper.alertDialog(PoiItemDetailActivity.this, R.string.gps_msg, R.string.gps_cancel,
								R.string.gps_ok, new DialogCallback() {

									@Override
									public void onOk() {
										// TODO Auto-generated method stub

									}

									@Override
									public void onCancle() {
										// TODO Auto-generated method stub

									}
								});
					} else {
						DialogHelper.alertDialog(PoiItemDetailActivity.this, R.string.gps_msg, R.string.gps_ok);
					}
				} else {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					LogUtil.d(TAG, "经度：" + longitude + "," + "纬度：" + latitude);
				}

			}
		});
	}

	/**
	 * 自动定位
	 */
	public void autoGetLocation() {
		// locationOverlay = new MyLocationOverlay(this, mapView);
		// mapView.getOverlays().add(locationOverlay);
		// // 实现初次定位使定位结果居中显示
		// locationOverlay.runOnFirstFix(new Runnable() {
		// @Override
		// public void run() {
		// GeoPoint geoPoint = locationOverlay.getMyLocation();
		// mapController.animateTo(geoPoint);
		// }
		// });
		// locationOverlay.enableMyLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseAMapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView = null;
		System.gc();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.HrAMapActivity#dealHandlerRequest(android
	 * .os.Message)
	 */
	@Override
	protected void dealHandlerRequest(Message msg) {
		switch (msg.what) {
		case Constants.GET_ROUTE_FAIL:
			DialogHelper.showToastText(PoiItemDetailActivity.this, getResources().getString(R.string.get_route_fail));
			break;
		case Constants.GET_ROUTE_SUCCESS:
			mapView.invalidate();
			break;
		}
	}
}
