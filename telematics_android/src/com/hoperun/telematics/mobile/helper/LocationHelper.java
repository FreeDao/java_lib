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
package com.hoperun.telematics.mobile.helper;

import java.util.List;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.model.location.GeoLocation;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class LocationHelper {
	private LocationManager locationManager;
	private String TAG = "LocationHelper";
	private static LocationHelper instance;

	private LocationHelper() {
	}

	public static LocationHelper getInstance() {
		if (instance == null) {
			instance = new LocationHelper();
		}
		return instance;
	}

	public void init(Context context) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		String provider = null;
		List<String> providers = locationManager.getAllProviders();
		if (providers.size() != 0) {
			if (providers.contains(LocationManager.GPS_PROVIDER)
					&& locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
				provider = LocationManager.GPS_PROVIDER;
			}

			if (providers.contains(LocationManager.NETWORK_PROVIDER)
					&& locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
				provider = LocationManager.NETWORK_PROVIDER;
			}
			if (provider != null) {
				Location location = locationManager.getLastKnownLocation(provider);
				updateWithNewLocation(location);
				locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
				locationManager.removeUpdates(locationListener);
			} else {
				// TODO exception
				provider = null;
			}
		}
	}

	/**
	 * 定位、update 地址信息
	 */
	public GeoLocation getLocation() {
		return CacheManager.getInstance().getGeoLocation();
	}

	/**
	 * @param mLocation
	 */
	private void updateWithNewLocation(Location location) {
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			CacheManager cacheManager = CacheManager.getInstance();
			cacheManager.updateGeoLocation(latitude, longitude);
		}
	}

	/**
	 * listener 监听location
	 */
	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				Log.d(TAG, "经度" + location.getLongitude());
				Log.d(TAG, "纬度" + location.getLatitude());
			}
			updateWithNewLocation(location);
		}
	};

	/**
	 * 当不再需要定位时，取消监听
	 */
	public void removeUpdate() {
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	/**
	 * @param address
	 * @return GeoPoint
	 */
	public GeoPoint getPosfromAddress(Context context, String address) {
		Geocoder geocoder = new Geocoder((Activity) context);
		GeoPoint geoPoint = null;
		try {
			List<Address> addList = geocoder.getFromLocationName(address, 2);
			Address add = addList.get(0);
			double lat = add.getLatitude() * 1E6;
			double lng = add.getLongitude() * 1E6;
			geoPoint = new GeoPoint((int) lat, (int) lng);
		} catch (AMapException e) {
			LogUtil.e(TAG, e.getMessage(), e);
		}
		return geoPoint;
	}

	/**
	 * @param point
	 * @return address
	 */
	public String getAddressfromPoint(Context context, GeoPoint point) {
		String address = null;
		Geocoder geocoder = new Geocoder((Activity) context);
		double lat = point.getLatitudeE6() / 1E6;
		double lng = point.getLongitudeE6() / 1E6;
		try {
			List<Address> addList = geocoder.getFromLocation(lat, lng, 5);
			Address add = addList.get(0);
			String admin = add.getAdminArea();
			String locality = add.getLocality();
			String featurename = add.getFeatureName();
			address = String.format("%s,%s,%s", admin, locality, featurename);
		} catch (AMapException e) {
			LogUtil.e(TAG, e.getMessage(), e);
		}
		return address;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public String getCityName(Context context, String location) {
		String province = context.getString(R.string.province);
		String city = context.getString(R.string.city);
		String cityStr = location.split(city, 2)[0];
		String[] tempArray = cityStr.split(province, 2);
		if (tempArray.length < 2) {
			return cityStr;
		} else {
			return tempArray[1];
		}

	}
	
	/**
	 * 
	 * @param latStr as 38.11
	 * @param longStr as 108.22
	 * @return
	 */
	public static GeoPoint parse2Geo(String latStr,String longStr){
		double lat = Double.parseDouble(latStr) * 1E6;
		double lng = Double.parseDouble(longStr) * 1E6;
		GeoPoint geoPoint = new GeoPoint((int) lat, (int) lng);
		return geoPoint;
	}
}
