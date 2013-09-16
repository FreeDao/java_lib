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
package com.hoperun.telematics.mobile.framework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.hoperun.telematics.mobile.framework.location.DefaultLocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs.ELocationStatus;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.helper.DefineHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class LocationService extends Service {

	private static final int CHECK_INTERVAL = 1000 * 30;

	private int listenedLocationTypes = ELocationProviderType.NONE.getValue();
	private ELocationProviderType currentProviderType = ELocationProviderType.NONE;
	private ELocationStatus locationStatus = ELocationStatus.WAIT_SETTING;
	private Location currentLocation;
	private Map<Messenger, ILocationListener> cachedListeners = new HashMap<Messenger, ILocationListener>();

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterAllListener();
	}

	public ELocationProviderType getLocationType() {
		return currentProviderType;
	}

	private void startLocationListener() {
		Log.d(this.getClass().getName(), "startLocationListener");
		if (!hasLocationType(ELocationProviderType.GPS)) {
			registerListener(LocationManager.GPS_PROVIDER, gpsListener);
		}
		if (hasLocationType(ELocationProviderType.GPS)) {
			currentProviderType = ELocationProviderType.GPS;
		} else {
			if (!hasLocationType(ELocationProviderType.NETWORK)) {
				registerListener(LocationManager.NETWORK_PROVIDER, networkListener);
			}
			if (hasLocationType(ELocationProviderType.NETWORK)) {
				currentProviderType = ELocationProviderType.NETWORK;
			}
		}
		Log.d(this.getClass().getName(), new Integer(listenedLocationTypes).toString());
		Log.d(this.getClass().getName(), currentProviderType.toString());
	}

	private void stopLocationListener() {
		synchronized (cachedListeners) {
			if (cachedListeners.size() == 0) {
				unregisterAllListener();
			}
		}
	}

	private void unregisterAllListener() {
		if (hasLocationType(ELocationProviderType.GPS)) {
			unregisterListener(LocationManager.GPS_PROVIDER, gpsListener);
		}
		if (hasLocationType(ELocationProviderType.NETWORK)) {
			unregisterListener(LocationManager.NETWORK_PROVIDER, networkListener);
		}
	}

	private LocationListener gpsListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(this.getClass().getName(), "gpsListener.onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i(this.getClass().getName(), "gpsListener.onProviderEnabled");
			unregisterListener(LocationManager.NETWORK_PROVIDER, networkListener);
			currentProviderType = ELocationProviderType.GPS;
			listenedLocationTypes = listenedLocationTypes
					| ELocationProviderType.getEnumByProviderType(LocationManager.GPS_PROVIDER).getValue();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(this.getClass().getName(), "gpsListener.onProviderDisabled");
			if (!hasLocationType(ELocationProviderType.NETWORK)) {
				registerListener(LocationManager.NETWORK_PROVIDER, networkListener);
				currentProviderType = ELocationProviderType.NETWORK;
			}
			listenedLocationTypes = listenedLocationTypes
					& ~ELocationProviderType.getEnumByProviderType(LocationManager.GPS_PROVIDER).getValue();
		}

		@Override
		public void onLocationChanged(final Location location) {
			handleLocation(location);
		}
	};

	private LocationListener networkListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(this.getClass().getName(), "networkListener.onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i(this.getClass().getName(), "networkListener.onProviderEnabled");
			listenedLocationTypes = listenedLocationTypes
					| ELocationProviderType.getEnumByProviderType(LocationManager.NETWORK_PROVIDER).getValue();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(this.getClass().getName(), "networkListener.onProviderDisabled");
			listenedLocationTypes = listenedLocationTypes
					& ~ELocationProviderType.getEnumByProviderType(LocationManager.NETWORK_PROVIDER).getValue();
		}

		@Override
		public void onLocationChanged(final Location location) {
			handleLocation(location);
		}
	};

	private void handleLocation(Location location) {
		Log.i(this.getClass().getName(), "gpsListener.onLocationChanged");

		// Called when a new location is found by the location provider.
		Log.v("GPSTEST", "Got New Location of provider:" + location.getProvider());
		if (currentLocation != null) {
			if (isBetterLocation(location, currentLocation)) {
				Log.v("GPSTEST", "It's a better location");
				currentLocation = location;
				handleLocationChagned(location);
			} else {
				Log.v("GPSTEST", "Not very good!");
			}
		} else {
			Log.v("GPSTEST", "It's first location");
			currentLocation = location;
			handleLocationChagned(location);
		}

	}

	private void handleLocationChagned(Location location) {
		// 纬度
		Log.v("GPSTEST", "Latitude:" + location.getLatitude());
		// 经度
		Log.v("GPSTEST", "Longitude:" + location.getLongitude());
		// 精确度
		Log.v("GPSTEST", "Accuracy:" + location.getAccuracy());
		// Location还有其它属性，请自行探索
		List<Object[]> paramsList = new ArrayList<Object[]>();
		synchronized (cachedListeners) {
			for (Map.Entry<Messenger, ILocationListener> entry : cachedListeners.entrySet()) {

				Bundle results = new Bundle();
				results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultLocationEventArgs(currentLocation,
						currentProviderType, locationStatus));
				Object[] params = new Object[3];
				params[0] = results;
				params[1] = entry.getKey();
				params[2] = entry.getValue();
				paramsList.add(params);
			}
		}

		for (Object[] params : paramsList) {
			doCallback((Bundle) params[0], (Messenger) params[1], (ILocationListener) params[2]);
		}
	}

	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location,
		// use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must
			// be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	private void registerListener(String providerType, LocationListener listener) {
		Log.i(this.getClass().getName(), providerType);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(providerType)) {
			locationManager.requestLocationUpdates(providerType, 2000, 0, listener);
			listenedLocationTypes = listenedLocationTypes
					| ELocationProviderType.getEnumByProviderType(providerType).getValue();
		}
	}

	private void unregisterListener(String providerType, LocationListener listener) {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			locationManager.removeUpdates(listener);
			listenedLocationTypes = listenedLocationTypes
					& ~ELocationProviderType.getEnumByProviderType(providerType).getValue();
		}
	}

	private Location getLastLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location result = null;
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			result = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			listenedLocationTypes = listenedLocationTypes
					| ELocationProviderType.getEnumByProviderType(LocationManager.GPS_PROVIDER).getValue();
			currentProviderType = ELocationProviderType.GPS;
		} else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			listenedLocationTypes = listenedLocationTypes
					| ELocationProviderType.getEnumByProviderType(LocationManager.NETWORK_PROVIDER).getValue();
			currentProviderType = ELocationProviderType.NETWORK;
		}
		
		return result;
	}

	private Handler incomingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle args = msg.getData();
			ELocationServiceType handleType = (ELocationServiceType) args
					.getSerializable(DefineHelper.MESSAGE_KEY_SERVICE_TYPE);
			ILocationListener callback = (ILocationListener) msg.obj;
			Bundle results = new Bundle();
			switch (handleType) {
			case GetLastLocation:
				Location lastLocation = getLastLocation();
				if (currentLocation != null) {
					if (isBetterLocation(lastLocation, currentLocation)) {
						currentLocation = lastLocation;
					}
				} else {
					currentLocation = lastLocation;
				}

				results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultLocationEventArgs(currentLocation,
						currentProviderType, locationStatus));
				doCallback(results, msg.replyTo, callback);
				break;
			case GetProviderType:
				results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultLocationEventArgs(currentLocation,
						currentProviderType, locationStatus));
				doCallback(results, msg.replyTo, callback);
				break;
			case RegisterLocationChangedListener:
				startLocationListener();
				synchronized (cachedListeners) {
					cachedListeners.put(msg.replyTo, callback);
				}
				break;
			case UnregisterLocationChangedListener:
				synchronized (cachedListeners) {
					if (cachedListeners.containsKey(msg.replyTo)) {
						cachedListeners.remove(msg.replyTo);
					}
				}
				stopLocationListener();
				break;
			default:
				Log.e(this.getClass().getName(), String.format("Do not support handle type (%s)", handleType));
				break;
			}
		}
	};

	private void doCallback(Bundle bundle, Messenger replyTo, ILocationListener callback) {

		try {
			Message msg = new Message();
			msg.setData(bundle);
			msg.obj = callback;
			replyTo.send(msg);
		} catch (RemoteException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	private Messenger messenger = new Messenger(incomingHandler);

	private boolean hasLocationType(ELocationProviderType type) {
		return ((listenedLocationTypes >> type.getValue() - 1) & 1) == 1;
	}

	public enum ELocationServiceType {
		GetLastLocation, RegisterLocationChangedListener, UnregisterLocationChangedListener, GetProviderType
	}

	public enum ELocationProviderType {
		GPS(1), NETWORK(2), NONE(0);

		private int value;

		private ELocationProviderType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static ELocationProviderType getEnumByValue(int value) {
			ELocationProviderType result = ELocationProviderType.NONE;
			if (value == 1) {
				result = ELocationProviderType.GPS;
			} else if (value == 2) {
				result = ELocationProviderType.NETWORK;
			}
			return result;
		}

		public static ELocationProviderType getEnumByProviderType(String providerType) {
			ELocationProviderType result = ELocationProviderType.NONE;
			if (providerType.equals(LocationManager.GPS_PROVIDER)) {
				result = ELocationProviderType.GPS;
			} else if (providerType.equals(LocationManager.NETWORK_PROVIDER)) {
				result = ELocationProviderType.NETWORK;
			}
			return result;
		}
	}
}
