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
import android.os.IBinder;
import android.util.Log;

import com.hoperun.telematics.mobile.R;
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
import com.hoperun.telematics.mobile.helper.LogUtil;

public abstract class BaseActivity extends Activity {
	
	private String TAG = "BaseActivity";

	private Map<String, IBinder> cachedBoundServices = new HashMap<String, IBinder>();
	private Map<String, Object> cachedAdapters = new HashMap<String, Object>();

	protected ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	private void startNetworkService() {
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
		bindService(new Intent(BaseActivity.this, serviceClass), serviceConnection, Context.BIND_AUTO_CREATE);
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

	/**
	 * startProgressDialog
	 */
	protected void startProgressDialog() {
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
	
	/**
	 * startProgressDialog
	 */
	protected void startProgressDialog(final ENetworkServiceType serviceType) {
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

	protected void onProgressDialogCancel() {

	}
	
	protected void onProgressDialogCancel(final ENetworkServiceType serviceType) {
		LogUtil.i(TAG, "the request is cancle.");
		cancelAsyncMessage(serviceType);
	}

	/**
	 * stopProgressDialog
	 */
	protected void stopProgressDialog() {
		if (null != progressDialog && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
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
}