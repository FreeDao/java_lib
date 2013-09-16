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
package com.hoperun.telematics.mobile.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseActivity;
import com.hoperun.telematics.mobile.framework.service.LocationService;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.model.location.GeoLocation;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public abstract class LocationManagerActivity extends BaseActivity {
//	private boolean isBound;
//	private Messenger mService = null;
//	private Location location;
//	private IBinder boundService;
//	public double currentlocation[];
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (!isBound) {
//			doBindService();
//		}
//	}
//
//	private Handler IncomingHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case LocationService.OPEN_GPS_REQUSET:
//				showDialog();
//				break;
//			case LocationService.GET_LOCATION_SUCCESS:
//				location = (Location) msg.obj;
//				currentlocation = new double[2];
//				currentlocation[0] = location.getLatitude();
//				currentlocation[1] = location.getLongitude();
//				updateLocation(location);
//
//				break;
//			default:
//				super.handleMessage(msg);
//			}
//		}
//	};
//
//	/**
//	 * @param mLocation
//	 */
//	private void updateLocation(Location location) {
//		double latitude = location.getLatitude();
//		double longitude = location.getLongitude();
//		CacheManager cacheManager = CacheManager.getInstance();
//		cacheManager.updateGeoLocation(latitude, longitude);
//
//	}
//
//	private void showDialog() {
//		DialogHelper.alertDialog(this, R.string.gps_msg, R.string.gps_cancel, R.string.gps_ok, new mCallback());
//	}
//
//	private class mCallback implements DialogCallback {
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see
//		 * com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback#
//		 * onOk()
//		 */
//		@Override
//		public void onOk() {
//			Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see
//		 * com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback#
//		 * onCancle()
//		 */
//		@Override
//		public void onCancle() {
//
//		}
//
//	}
//
//	final Messenger mMessenger = new Messenger(IncomingHandler);
//
//	private void doBindService() {
//		isBound = bindService(new Intent(LocationManagerActivity.this, LocationService.class),
//				serviceConnection, Context.BIND_AUTO_CREATE);
//		Log.i(this.getClass().getName(), "Bind base service " + isBound);
//	}
//
//	private ServiceConnection serviceConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			boundService = null;
//			onUnbindServiceFinish();
//		}
//
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			boundService = service;
//			mService = new Messenger(service);
//			Message msg = new Message();
//			msg.replyTo = mMessenger;
//			msg.what = LocationService.BIND_SUCCESS;
//			try {
//				mService.send(msg);
//			} catch (RemoteException e) {
//			}
//			onBindServiceFinish();
//			LogUtil.i("LocationManagerActivity", "...............onServiceConnected");
//		}
//	};
//
//	private void doUnbindService() {
//		if (isBound) {
//			unbindService(serviceConnection);
//			isBound = false;
//		}
//	}
//
//	/**
//	 * 定位、update 地址信息
//	 */
//	public GeoLocation getLocation() {
//		return CacheManager.getInstance().getGeoLocation();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		doUnbindService();
//	}
//
//	@Override
//	public boolean isBound() {
//		return isBound;
//	}

}
