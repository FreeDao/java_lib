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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.PoiSearchResultAdapter;
import com.hoperun.telematics.mobile.framework.BaseAMapActivity;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.IntentParamCache;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.map.seivice.impl.AMapServiceImpl;
import com.hoperun.telematics.mobile.model.BaseResponse;
import com.hoperun.telematics.mobile.model.location.GeoLocation;
import com.hoperun.telematics.mobile.model.poi.Poi;
import com.hoperun.telematics.mobile.model.poi.PoiRequest;
import com.hoperun.telematics.mobile.model.poi.PoiRequest.EPoiRequestType;
import com.hoperun.telematics.mobile.model.poi.PoiResponse;

/**
 * 
 * @author he_chen
 * 
 */
public class PoiSearchResultActivity extends BaseAMapActivity {

	private static String TAG = "PoiSearchResultActivity";

	private TextView mPoiResultTotal;
	private TextView mPoiResultType;
	private Button mShowListMapSwitch;
	private ListView mPoiResultListView;
	private MapView mMapView;
	private EPoiRequestType mType;

	private PoiResponse mPoiResponse;

	private PoiSearchResultAdapter mPoiSearchResultAdapter;

	private List<Poi> mPoiList;

	private MapController mMapController;

	public static String POI_POSITION = "poi_position";

	private boolean mIsLoadingMore = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_poi_search_result_layout);

		initWidget();
	}

	private void initWidget() {
		mPoiResultTotal = (TextView) findViewById(R.id.poi_result_total);
		mPoiResultType = (TextView) findViewById(R.id.poi_result_type);
		mShowListMapSwitch = (Button) findViewById(R.id.show_list_map_switch_btn);
		mPoiResultListView = (ListView) findViewById(R.id.poi_search_listview);
		mMapView = (MapView) findViewById(R.id.poi_mapView);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setEnabled(true);
		mMapView.setClickable(true);

		// GeoLocation currentLocation =
		// CacheManager.getInstance().getCurrentLocation(this);
		// int lat = (int) (Double.valueOf(currentLocation.getLat()) * 1E6);
		// int lng = (int) (Double.valueOf(currentLocation.getLng()) * 1E6);
		//
		// mMapController.setCenter(new GeoPoint(lat, lng));
		mMapController = mMapView.getController();
		mMapController.setZoom(12);

		mShowListMapSwitch.setOnClickListener(btnOnClickListener);
		mPoiResultListView.setOnScrollListener(onScrollListener);
		mPoiResultListView.setOnItemClickListener(onItemClickListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.i(TAG, "this is onStart()");

		IntentParamCache intentCache = IntentParamCache.getInstance();
		mPoiResponse = (PoiResponse) intentCache.getElementByKey(Constants.POI_SEARCH_RESULT);

		if (null != mPoiResponse) {
			initDataOnUI(mPoiResponse);
		}
	}

	/**
	 * 
	 */
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(PoiSearchResultActivity.this, PoiItemDetailActivity.class);
			intent.putExtra(POI_POSITION, arg2);
			IntentParamCache.getInstance().addElement(Constants.POI_INDEX_RESULT, mPoiList.get(arg2));
			IntentParamCache.getInstance().addElement(Constants.POI_TYPE, mType);
			startActivity(intent);
		}
	};

	private OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 当滑动到底部
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					if (null != mPoiResponse) {
						int curIndex = mPoiResponse.getCurIndex();
						int total = mPoiResponse.getTotalSize();
						int pageCount = mPoiResponse.getPoiList().size();
						int totalPage = total % pageCount == 0 ? total / pageCount : (total / pageCount + 1);

						// load more
						if (curIndex != totalPage && !mIsLoadingMore) {
							mIsLoadingMore = true;
							curIndex++;
							loadMoreData(curIndex);
						} else {
							mPoiSearchResultAdapter = null;
							Toast.makeText(PoiSearchResultActivity.this, R.string.all_shown, Toast.LENGTH_LONG).show();
						}
					}
				}
				break;
			}
		}

	};

	/**
	 * loadMoreData
	 * 
	 * @param curIndex
	 */
	private void loadMoreData(int curIndex) {

		LogUtil.i(TAG, "loadMoreData().");

		PoiRequest poiRequest = new PoiRequest();
		poiRequest.setType(mType.getValue());
		poiRequest.setLatitude("39.9022");
		poiRequest.setLongitude("116.3922");
		poiRequest.setRadius(15);
		poiRequest.setIndex(curIndex);
		poiRequest.setMaxSize(1);

		String jsonString = poiRequest.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.Poi, jsonString, new INetCallback() {

			@Override
			public void callback(INetCallbackArgs args) {
				stopProgressDialog();

				Context context = PoiSearchResultActivity.this;

				// OnClickListener retryBtnListener = new OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// }
				// };
				// 检查是否有非业务级的错误
				if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {

					String payload = args.getPayload();
					if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
						DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
					} else {
						try {
							mPoiResponse = (PoiResponse) BaseResponse.parseJsonToObject(payload, PoiResponse.class);

							if (NetworkCallbackHelper.isErrorResponse(context, mPoiResponse)) {
								// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
								NetworkCallbackHelper.alertBusinessError(context, mPoiResponse.getErrorCode());
							} else {
								initDataOnUI(mPoiResponse);
							}
						} catch (Exception e) {
							DialogHelper.alertDialog(PoiSearchResultActivity.this, R.string.warning,
									R.string.system_error, R.string.ok);
						}

					}
				} else {
					String errMsg = args.getErrorMessage();

					LogUtil.e(TAG, "errorMsg: " + errMsg);
					DialogHelper.alertDialog(PoiSearchResultActivity.this, R.string.warning,
							R.string.error_async_return_fault, R.string.ok);
				}
			}

		});
	}

	/**
	 * 
	 * @param poiResponse
	 */
	private void initDataOnUI(PoiResponse poiResponse) {
		if (null == mPoiSearchResultAdapter) {
			mPoiSearchResultAdapter = new PoiSearchResultAdapter(this, poiResponse.getPoiList());
			mPoiResultListView.setAdapter(mPoiSearchResultAdapter);
		} else {
			List<Poi> lastList = mPoiSearchResultAdapter.getmList();
			lastList.addAll(poiResponse.getPoiList());
			mPoiSearchResultAdapter.notifyDataSetChanged();
			mIsLoadingMore = false;
		}

		mPoiList = mPoiSearchResultAdapter.getmList();

		mPoiResultTotal.setText(String.valueOf(mPoiList.size()));

		mType = EPoiRequestType.getEPoiRequestType(mPoiResponse.getType());
		switch (mType) {
		case Parking:
			String parking = getResources().getString(R.string.poi_parking_str);
			setTitleBar(this, parking);
			mPoiResultType.setText(parking);
			break;
		case GasStation:
			String gasstation = getResources().getString(R.string.poi_gasstation_str);
			setTitleBar(this, gasstation);
			mPoiResultType.setText(gasstation);
			break;
		default:
			break;
		}

	}

	private OnClickListener btnOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mShowListMapSwitch.getText().toString().equalsIgnoreCase(getResources().getString(R.string.show_map))) {
				mShowListMapSwitch.setText(getResources().getString(R.string.show_list));
				mMapView.setVisibility(View.VISIBLE);
				mPoiResultListView.setVisibility(View.GONE);

				List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
				for (int i = 0; i < mPoiList.size(); i++) {
					GeoLocation geoLocation = new GeoLocation();
					Poi poi = mPoiList.get(i);
					geoLocation.setLat(Double.valueOf(poi.getLatitude()));
					geoLocation.setLng(Double.valueOf(poi.getLongitude()));
					geoLocation.setName(poi.getName());
					geoLocation.setAddress(poi.getAddress());
					geoLocation.setExtInfo(poi.getExtInfo());
					geoLocations.add(geoLocation);
				}
				mMapController.setCenter(geoLocations.get(0).getGeopoint());
				AMapServiceImpl aMapServiceImpl = AMapServiceImpl.getInstance();
				aMapServiceImpl.cleanMapView(mMapView);
				switch (mType) {
				case Parking:
					aMapServiceImpl.drawPointsOnMap(geoLocations, mMapView, PoiSearchResultActivity.this,
							R.layout.ui_poi_map_popup_layout, R.layout.ui_map_pin_layout);
					break;

				case GasStation:
					aMapServiceImpl.drawPointsOnMap(geoLocations, mMapView, PoiSearchResultActivity.this,
							R.layout.ui_poi_gas_popup_layout, R.layout.ui_map_pin_layout);
					break;
				default:
					break;
				}

			} else {
				mShowListMapSwitch.setText(getResources().getString(R.string.show_map));
				mMapView.setVisibility(View.GONE);
				mPoiResultListView.setVisibility(View.VISIBLE);
			}
		}

	};
}
