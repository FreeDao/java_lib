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
import java.util.Arrays;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.FriendListAdapter;
import com.hoperun.telematics.mobile.framework.BaseAMapActivity;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.IntentParamCache;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.helper.PinyinComparator;
import com.hoperun.telematics.mobile.helper.TestDataManager;
import com.hoperun.telematics.mobile.map.seivice.impl.AMapServiceImpl;
import com.hoperun.telematics.mobile.model.BaseResponse;
import com.hoperun.telematics.mobile.model.buddy.Friend;
import com.hoperun.telematics.mobile.model.buddy.FriendRequest;
import com.hoperun.telematics.mobile.model.buddy.FriendResponse;
import com.hoperun.telematics.mobile.model.location.GeoLocation;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class FriendActivity extends BaseAMapActivity {

	private String TAG = "FriendActivity";

	private ListView friendListView;
	private List<Friend> mfriendList;
	private FriendResponse friendResponse;
	private Button selectButton;
	private TextView friendTotal;
	private MapView mapView;
	private MapController mapController;
	public static String FRIEND_POSITION = "friend_position";
	public static String INTENT_TRANSFER_ID = "id";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_friend_search_result_layout);

		initView();
	}

	private void initView() {
		setTitleBar(this, getResources().getString(R.string.friend));
		selectButton = (Button) findViewById(R.id.show_map_button_id);
		friendTotal = (TextView) findViewById(R.id.friend_result_total);
		friendListView = (ListView) findViewById(R.id.friend_list_id);
		mapView = (MapView) findViewById(R.id.friend_mapView);
		//mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(12);

		selectButton.setOnClickListener(btnOnClickListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.framework.BaseAMapActivity#onBindServiceFinish (android.content.ComponentName)
	 */
	@Override
	protected void onBindServiceFinish(ComponentName className) {
		startProgressDialog(ENetworkServiceType.Buddy);
		if (className.getClassName().equals(NetworkService.class.getName())) {
			getFriendList();
		}
	}

	/**
	 * 向后台发送请求，获得好友列表
	 */
	private void getFriendList() {
		LogUtil.i(TAG, "getFriendList()");

		if (TestDataManager.IS_TEST_MODE) {
			friendResponse = TestDataManager.getInstance().getFriendResponse();
			initDataView();
		} else {

			FriendRequest request = new FriendRequest();
			request.setAccountId("test");// TODO hechen
			String postJson = request.toJsonStr();
			sendAsyncMessage(ENetworkServiceType.Buddy, postJson, new INetCallback() {

				@Override
				public void callback(INetCallbackArgs args) {
					Context context = FriendActivity.this;

					OnClickListener retryBtnListener = new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							refresh(null);
						}
					};
					if (!NetworkCallbackHelper.haveSystemError(context, args.getStatus())) {
						// stopProgressDialog();
						String payload = args.getPayload();
						LogUtil.i(TAG, "payload: " + payload);
						if (NetworkCallbackHelper.isPayloadNullOrEmpty(payload)) {
							DialogHelper.alertDialog(context, R.string.error_empty_payload, R.string.ok);
						} else {
							try {
								friendResponse = (FriendResponse) BaseResponse.parseJsonToObject(payload,
										FriendResponse.class);

								if (NetworkCallbackHelper.isErrorResponse(context, friendResponse)) {
									NetworkCallbackHelper.alertBusinessError(context, friendResponse.getErrorCode());
								} else {
									initDataView();
								}
							} catch (Exception e) {
								DialogHelper.alertDialog(FriendActivity.this, R.string.warning, R.string.system_error,
										R.string.ok);
							}

						}
					} else {
						// 根据各接口情况选择重试或直接提示
						String errMsg = args.getErrorMessage();
						LogUtil.e(TAG, "error msg: " + errMsg);
						startReload(getResources().getString(R.string.error_async_return_fault), retryBtnListener);
					}
				}

			});
		}
	}

	private void refresh(View view) {
		initReloadTimes();
		startProgressDialog(ENetworkServiceType.Buddy);
		getFriendList();
	}

	private void initDataView() {
		List<Friend> friendList = friendResponse.getFriendList();

		String[] nameList = new String[friendList.size()];
		for (int i = 0; i < friendList.size(); i++) {
			nameList[i] = friendList.get(i).getNickName();
		}
		Arrays.sort(nameList, new PinyinComparator());
		mfriendList = new ArrayList<Friend>();
		for (String name : nameList) {
			for (Friend friend : friendList) {
				if (friend.getNickName() == name) {
					mfriendList.add(friend);
				}
			}
		}

		setData(mfriendList);
	}

	/**
	 * setData
	 * 
	 * @param list
	 */
	private void setData(List<Friend> list) {
		friendTotal.setText(String.valueOf(list.size()));
		FriendListAdapter adapter = new FriendListAdapter(this, list);
		friendListView.setAdapter(adapter);
		friendListView.setOnItemClickListener(listener);

		stopProgressDialog();
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			Intent intent = new Intent();
			intent.putExtra(FRIEND_POSITION, position);
			intent.setClass(getApplicationContext(), FriendDetailActivity.class);
			IntentParamCache.getInstance().addElement(Constants.FRIEND_POSITION, mfriendList.get(position));
			startActivity(intent);
		}
	};

	private View.OnClickListener btnOnClickListener = new View.OnClickListener() {

		public void onClick(View v) {
			if (selectButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.show_map))) {
				selectButton.setText(getResources().getString(R.string.show_list));
				mapView.setVisibility(View.VISIBLE);
				friendListView.setVisibility(View.GONE);
				if (mfriendList.size() != 0) {
					List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
					for (int i = 0; i < mfriendList.size(); i++) {
						GeoLocation geoLocation = new GeoLocation();
						Friend friend = mfriendList.get(i);
						geoLocation.setLat(Double.valueOf(friend.getLatitude()));
						geoLocation.setLng(Double.valueOf(friend.getLongitude()));
						geoLocation.setName(friend.getNickName());
						geoLocation.setAddress(friend.getAddress());
						geoLocation.setNote(friend.getNote());
						geoLocations.add(geoLocation);
					}
					mapController.setCenter(mfriendList.get(0).getGeoPoint());
					AMapServiceImpl aMapServiceImpl = AMapServiceImpl.getInstance();
					aMapServiceImpl.cleanMapView(mapView);
					aMapServiceImpl.drawPointsOnMap(geoLocations, mapView, FriendActivity.this,
							R.layout.ui_friend_popup_layout, R.layout.ui_map_pin_layout);
				}
			} else {
				selectButton.setText(getResources().getString(R.string.show_map));
				mapView.setVisibility(View.GONE);
				friendListView.setVisibility(View.VISIBLE);
			}
		}

	};

}
