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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author HopeRun
 * 
 */
public final class SharedPreferencesUtil {

	public static Context context;

	private static String PREFERENCE_NAME = "data_save";

	public static final String BACKEND_HOST_KEY = "host";

	public static final String BACKEND_HOST = "xx.xx.xx.xx";

	private static SharedPreferences prefs;
	private static Editor editor;

	private static SharedPreferencesUtil instance;

	private SharedPreferencesUtil() {
		prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
	}

	public static SharedPreferencesUtil getInstance() {
		if (null == instance) {
			instance = new SharedPreferencesUtil();
		}
		return instance;
	}

	/**
	 * get back-end host
	 * 
	 * @return
	 */
	public String getBackendHost() {
		String port = getString(BACKEND_HOST_KEY);
		if (null != port && !"".equals(port)) {
			return port;
		}
		return BACKEND_HOST;
	}

	public void saveBackendHost(String host) {
		putString(BACKEND_HOST_KEY, host);
	}

	public String getString(String key) {

		return prefs.getString(key, null);
	}

	public int getInt(String key) {
		return prefs.getInt(key, -1);
	}

	public void putString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void putBoolean(String key, boolean value) {
		if (context == null) {
			return;
		}
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		return prefs.getBoolean(key, false);
	}

	public void putInt(String key, int value) {
		if (context == null) {
			return;
		}
		editor.putInt(key, value);
		editor.commit();
	}

	public void putLong(String key, long value) {
		if (context == null) {
			return;
		}
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key) {
		int result = -1;

		if (context == null) {
			return result;
		}
		return prefs.getLong(key, result);
	}

}