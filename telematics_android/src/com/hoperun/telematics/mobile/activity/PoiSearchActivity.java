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
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.poisearch.PoiPagedResult;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.NavigationAdapter;
import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationProviderType;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.IntentParamCache;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.model.BaseResponse;
import com.hoperun.telematics.mobile.model.poi.PoiRequest;
import com.hoperun.telematics.mobile.model.poi.PoiRequest.EPoiRequestType;
import com.hoperun.telematics.mobile.model.poi.PoiResponse;

/**
 * 
 * @author he_chen
 * 
 */
public class PoiSearchActivity extends DefaultActivity {

	private static String TAG = "PoiSearchActivity";

	private GridView mNavigationGridview;

	private PoiPagedResult mResult;

	private NavigationAdapter mNavigationAdapter;
	public static double mLat;
	public static double mLng;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_poi_search_layout);

		setTitleBar(this, getResources().getString(R.string.poi_search_str));

		initWidget();

		fullGridView();

		startLocationService();
	}

	/**
	 * initWidget
	 */
	private void initWidget() {
		mNavigationGridview = (GridView) findViewById(R.id.poi_gridview_id);
		mNavigationGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	/**
	 * 
	 */
	private void fullGridView() {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 2; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("navigation_image_id", R.drawable.navigation_item_png_10 + i);
			map.put("navigation_text_id", getResources().getString(R.string.poi_item_text_10 + i));

			list.add(map);
		}

		mNavigationAdapter = new NavigationAdapter(this, list, Constants.POI_FLAG);
		mNavigationGridview.setAdapter(mNavigationAdapter);
		mNavigationGridview.setOnItemClickListener(new ItemClickListener());
	}

	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0,

		View arg1, int position, long arg3) {

			switch (position) {
			case 0:
				LogUtil.i(TAG, "this poi is parking");
				// getCurrentLocation(); //TODO he_chen
				startProgressDialog(ENetworkServiceType.Poi);

				searchPoi(EPoiRequestType.Parking);

				break;
			case 1:
				LogUtil.i(TAG, "this poi is gasstation");

				startProgressDialog(ENetworkServiceType.Poi);
				searchPoi(EPoiRequestType.GasStation);
				break;
			case 2:
				Toast.makeText(PoiSearchActivity.this, "btn : " + position, Toast.LENGTH_SHORT).show();

				break;
			case 3:
				Toast.makeText(PoiSearchActivity.this, "btn : " + position, Toast.LENGTH_SHORT).show();

				break;
			case 4:
				Toast.makeText(PoiSearchActivity.this, "btn : " + position, Toast.LENGTH_SHORT).show();

				break;
			case 5:
				Toast.makeText(PoiSearchActivity.this, "btn : " + position, Toast.LENGTH_SHORT).show();

				break;

			default:

				break;
			}
		}
	}

	/**
	 * searchPoi
	 * 
	 * @param type
	 */
	private void searchPoi(PoiRequest.EPoiRequestType type) {
		PoiRequest poiRequest = new PoiRequest();
		poiRequest.setType(type.getValue());
		poiRequest.setLatitude("39.9022");
		poiRequest.setLongitude("116.3922");
		poiRequest.setRadius(15);
		poiRequest.setIndex(1);
		poiRequest.setMaxSize(1);

		String jsonString = poiRequest.toJsonStr();
		sendAsyncMessage(ENetworkServiceType.Poi, jsonString, new INetCallback() {
			@Override
			public void callback(INetCallbackArgs args) {

				stopProgressDialog();

				Context context = PoiSearchActivity.this;

				// OnClickListener retryBtnListener = new OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// }
				// };
				// 检查是否有非业务级的错误
				if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {

					String payload = args.getPayload();
					// String testString =
					// "{\"poiList\":[{\"address\":\"西单北大街176号\",\"extInfo\":{\"key\":\"feeText\",\"value\":\"07:00-21:00,1.5元/15分钟21:00-07:00.2.5元/0.5小时\"},\"latitude\":\"39.909770734277195\",\"longitude\":\"116.37597679514113\",\"name\":\"中友百货地下停车库\"}],\"type\":1}";
					LogUtil.i(TAG, "payload: " + payload);
					if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
						DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
					} else {
						try {
							PoiResponse response = (PoiResponse) BaseResponse.parseJsonToObject(payload,
									PoiResponse.class);
							if (NetworkCallbackHelper.isErrorResponse(context, response)) {
								// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
								NetworkCallbackHelper.alertBusinessError(context, response.getErrorCode());
							} else {
								IntentParamCache intentCache = IntentParamCache.getInstance();
								intentCache.addElement(Constants.POI_SEARCH_RESULT, response);

								startActivity(new Intent(PoiSearchActivity.this, PoiSearchResultActivity.class));
							}
						} catch (Exception e) {
							DialogHelper.alertDialog(PoiSearchActivity.this, R.string.warning, R.string.system_error,
									R.string.ok);
						}

					}
				} else {
					String errMsg = args.getErrorMessage();
					// if (errMsg == null || errMsg.trim().length() == 0) {
					// errMsg = getString(R.string.error_async_return_fault);
					// }
					LogUtil.e(TAG, "errorMsg: " + errMsg);
					DialogHelper.alertDialog(PoiSearchActivity.this, R.string.warning,
							R.string.error_async_return_fault, R.string.ok);
				}

			}
		});
	}

	/**
	 * searchPoi
	 * 
	 * @param poiParam
	 */
	// private void searchPoi(final String poiParam) {
	// Thread thread = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// PoiSearch poiSearch = new PoiSearch(PoiSearchActivity.this, new PoiSearch.Query(poiParam,
	// PoiTypeDef.All, "010")); // 010 city num
	// try {
	// mResult = poiSearch.searchPOI();
	//
	// if (null != mResult) {
	// mHandler.sendEmptyMessage(Constants.SUCCESS);
	// }
	// } catch (AMapException e) {
	// mHandler.sendEmptyMessage(Constants.FAIL);
	// }
	// }
	// });
	// thread.start();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.DefaultActivity#dealHandlerRequest(android.os.Message)
	 */
	@Override
	public void dealHandlerRequest(Message msg) {
		switch (msg.what) {
		case Constants.SUCCESS:
			try {
				List<PoiItem> poiItems = mResult.getPage(1);
				if (poiItems != null && poiItems.size() > 0) {
					for (PoiItem poiItem : poiItems) {
						poiItem.getTitle();
					}
				}
			} catch (AMapException e) {
				// NotFoundException
			}

			break;
		case Constants.FAIL:
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 */
	private void getCurrentLocation() {
		LogUtil.i("PoiSearchActivity", "getCurrentLocation()");
		getLastLocation(new ILocationListener() {
			@Override
			public void callback(ILocationEventArgs args) {
				Location location = args.getLocation();

				LogUtil.i("getCurrentLocation type", "" + args.getLocationType());

				if (null == location) {
					if (args.getLocationType() == ELocationProviderType.NONE) {
						DialogHelper.alertDialog(PoiSearchActivity.this, R.string.warning, R.string.locate_fail,
								R.string.ok);
					}
				} else {
					mLat = location.getLatitude();
					mLng = location.getLongitude();
					LogUtil.i("getCurrentLocation", "lat, lng: " + mLat + ", " + mLng);
				}

			}
		});

		// registerLocationListener(new ILocationListener() {
		//
		// @Override
		// public void callback(ILocationEventArgs args) {
		// Location location = args.getLocation();
		//
		// LogUtil.i("getCurrentLocation type", "" + args.getLocationType());
		//
		// if (null == location) {
		// if (args.getLocationType() == ELocationProviderType.NONE) {
		// DialogHelper.alertDialog(PoiSearchActivity.this, R.string.warning, R.string.locate_fail,
		// R.string.ok);
		// }
		// } else {
		// mLat = location.getLatitude();
		// mLng = location.getLongitude();
		// LogUtil.i("getCurrentLocation", "lat, lng: " + mLat + ", " + mLng);
		// }
		// }
		// });
	}
}
