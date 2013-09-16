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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author he_chen
 * 
 */
public class IntentParamCache {
	private Map<String, Object> data;
	private static IntentParamCache cache;

	private IntentParamCache() {
		data = new HashMap<String, Object>();
	}

	public static IntentParamCache getInstance() {
		if (cache == null) {
			cache = new IntentParamCache();
		}
		return cache;
	}

	public void addElement(String key, Object value) {
		data.put(key, value);
	}

	public synchronized Object getElementByKey(String key) {
		if (!data.containsKey(key)) {
			return null;
		}
		return data.remove(key);
	}

}
