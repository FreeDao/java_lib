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
package com.hoperun.telematics.mobile.framework;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.map.MapActivity;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.activity.CustomApplication;
import com.hoperun.telematics.mobile.activity.MainActivity;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;
import com.hoperun.telematics.mobile.framework.service.LocationService;
import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationServiceType;
import com.hoperun.telematics.mobile.framework.service.NetworkService;
import com.hoperun.telematics.mobile.framework.service.NetworkService.EMessageType;
import com.hoperun.telematics.mobile.framework.service.adapter.LocationAdapter;
import com.hoperun.telematics.mobile.framework.service.adapter.NetworkAdapter;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class BaseAMapActivity extends MapActivity {

	private String TAG = "BaseAMapActivity";

	protected ProgressDialog progressDialog;

	private Map<String, IBinder> cachedBoundServices = new HashMap<String, IBinder>();
	private Map<String, Object> cachedAdapters = new HashMap<String, Object>();

	private static final int MAX_RELOAD_TIMES = 3;
	private int reLoadTimes = MAX_RELOAD_TIMES;

	protected boolean canReload() {
		if (reLoadTimes > 0) {
			reLoadTimes--;
			return true;
		} else {
			return false;
		}
	}

	protected void initReloadTimes(){
		reLoadTimes = MAX_RELOAD_TIMES;
	}
	
	protected void reload() {

	}

	protected void startReload(String errMsg, DialogInterface.OnClickListener retryBtnListener) {
		if (canReload()) {
			reload();
		} else {
			stopProgressDialog();
			DialogHelper.alertDialog(this, errMsg, retryBtnListener);
		}
	}

	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			dealHandlerRequest(msg);
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		startNetworkService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnbindAllService();
	}

	protected void onBindServiceFinish(ComponentName className) {

	}

	protected void onUnbindServiceFinish(ComponentName className) {

	}

	protected final boolean isNetworkAvailable() {
		return cachedBoundServices.containsKey(NetworkService.class.getName());
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d(this.getClass().getName(), className.getClassName());
			cachedBoundServices.put(className.getClassName(), service);
			onBindServiceFinish(className);
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			if (cachedBoundServices.containsKey(className.getClassName())) {
				cachedBoundServices.remove(className.getClassName());
			}
			Log.d(this.getClass().getName(), className.getClassName());
			onUnbindServiceFinish(className);
		}
	};

	protected void startNetworkService() {
		if (!isBound(NetworkService.class)) {
			cachedAdapters.put(NetworkService.class.getName(), new NetworkAdapter());
			doBindService(NetworkService.class);
		}
	}

	protected void startLocationService() {
		if (!isBound(LocationService.class)) {
			cachedAdapters.put(LocationService.class.getName(), new LocationAdapter());
			doBindService(LocationService.class);
		}
	}

	private void doBindService(Class<?> serviceClass) {
		bindService(new Intent(BaseAMapActivity.this, serviceClass), serviceConnection, Context.BIND_AUTO_CREATE);
		Log.i(this.getClass().getName(), "Bind network service " + serviceClass.getName());
	}

	private void doUnbindAllService() {
		unbindService(serviceConnection);
	}

	protected void getLastLocation(ILocationListener listener) {
		LocationAdapter adapter = (LocationAdapter) getAdapter(LocationService.class);
		IBinder boundService = getBoundService(LocationService.class);
		adapter.sendMessage(boundService, ELocationServiceType.GetLastLocation, listener);
	}

	protected void registerLocationListener(ILocationListener listener) {
		LocationAdapter adapter = (LocationAdapter) getAdapter(LocationService.class);
		IBinder boundService = getBoundService(LocationService.class);
		adapter.sendMessage(boundService, ELocationServiceType.RegisterLocationChangedListener, listener);
	}

	protected void unregisterLocationListener() {
		LocationAdapter adapter = (LocationAdapter) getAdapter(LocationService.class);
		IBinder boundService = getBoundService(LocationService.class);
		adapter.sendMessage(boundService, ELocationServiceType.UnregisterLocationChangedListener, null);
	}

	protected void getProviderType(ILocationListener listener) {
		LocationAdapter adapter = (LocationAdapter) getAdapter(LocationService.class);
		IBinder boundService = getBoundService(LocationService.class);
		adapter.sendMessage(boundService, ELocationServiceType.GetProviderType, listener);
	}

	protected void sendSyncMessage(ENetworkServiceType serviceType, String jsonString, INetCallback callback) {
		if (!UtilHelper.checkNetWorkAlert(this)) {
			stopProgressDialog();
			return;
		}
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.Sync, jsonString, callback);
	}

	protected void sendAsyncMessage(ENetworkServiceType serviceType, String jsonString, INetCallback callback) {
		if (!UtilHelper.checkNetWorkAlert(this)) {
			stopProgressDialog();
			return;
		}
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.Async, jsonString, callback);
	}

	protected void cancelAsyncMessage(ENetworkServiceType serviceType) {
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.CancelAsync, null, null);
	}

	protected void registerNetService(ENetworkServiceType serviceType, INetCallback callback) {
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.RegisterNetService, null, callback);
	}

	protected void registerNetService(ENetworkServiceType serviceType, String jsonString, INetCallback callback) {
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.RegisterNetService, jsonString, callback);
	}

	protected void pauseNetService(ENetworkServiceType serviceType) {
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.PauseNetService, null, null);
	}

	protected void resumeNetService(ENetworkServiceType serviceType) {
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.ResumeNetService, null, null);
	}

	protected void unregisterNetService(ENetworkServiceType serviceType) {
		NetworkAdapter adapter = (NetworkAdapter) getAdapter(NetworkService.class);
		IBinder boundService = getBoundService(NetworkService.class);
		adapter.sendMessage(boundService, serviceType, EMessageType.UnregisterNetService, null, null);
	}

	private Object getAdapter(Class<?> serviceClass) {
		return cachedAdapters.get(serviceClass.getName());
	}

	private IBinder getBoundService(Class<?> serviceClass) {
		return cachedBoundServices.get(serviceClass.getName());
	}

	private boolean isBound(Class<?> serviceClass) {
		return cachedBoundServices.get(serviceClass.getName()) != null;
	}

	/**
	 * 
	 * setTitleBar:please describe
	 * 
	 */
	public void setTitleBar(Activity activity, String title) {
		FrameLayout mTitleBarLayout = (FrameLayout) findViewById(R.id.title_bar_layout);
		TextView titleBarText = (TextView) mTitleBarLayout.findViewById(R.id.title_bar_text);
		titleBarText.setText(title);

		mTitleBarLayout.findViewById(R.id.main_btn).setOnClickListener(onClickListener);
		mTitleBarLayout.findViewById(R.id.userInfo_btn).setOnClickListener(onClickListener);

		CustomApplication.getInstance().addActivity(activity);
	}

	/**
	 * onClickListener
	 */
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.main_btn:
				intent = new Intent(BaseAMapActivity.this, MainActivity.class);
				startActivity(intent);

				break;
			case R.id.userInfo_btn:
				Toast.makeText(BaseAMapActivity.this, "UserInfo Button", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * startProgressDialog
	 */
	public void startProgressDialog(final ENetworkServiceType serviceType) {
		Resources stringResaurce = getResources();
		String progressBody = stringResaurce.getString(R.string.progress_body);
		String progressHeader = stringResaurce.getString(R.string.progress_header);
		progressDialog = ProgressDialog.show(this, progressHeader, progressBody, true, true);
		progressDialog.setOnCancelListener(new ProgressDialog.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				onProgressDialogCancel(serviceType);
			}

		});

	}

	public void startProgressDialog() {
		Resources stringResaurce = getResources();
		String progressBody = stringResaurce.getString(R.string.progress_body);
		String progressHeader = stringResaurce.getString(R.string.progress_header);
		progressDialog = ProgressDialog.show(this, progressHeader, progressBody, true, true);
		progressDialog.setOnCancelListener(new ProgressDialog.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				onProgressDialogCancel();
			}

		});

	}

	protected void onProgressDialogCancel() {
	}

	protected void onProgressDialogCancel(ENetworkServiceType serviceType) {
		LogUtil.i(TAG, "the request is cancle.");
		cancelAsyncMessage(serviceType);
	}

	/**
	 * stopProgressDialog
	 */
	public void stopProgressDialog() {
		if (null != progressDialog && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}

	protected void dealHandlerRequest(Message msg) {

	}
}