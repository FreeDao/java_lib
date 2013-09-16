package com.hoperun.telematics.mobile.framework.net.helper;

import java.util.HashMap;
import java.util.Map;

public class ResourcesHelper {

	public static String CACHED_KEY_CLIENT_BKS = "CACHED_KEY_CLIENT_BKS";
	public static String CACHED_KEY_CLIENT_BKS_PWD = "CACHED_KEY_CLIENT_BKS_PWD";
	private static ResourcesHelper instance;

	private Map<String, Object> cached = new HashMap<String, Object>();

	private ResourcesHelper() {

	}

	public static ResourcesHelper getInstance() {
		if (instance == null) {
			instance = new ResourcesHelper();
		}
		return instance;
	}

	public void put(String key, Object value) {
		cached.put(key, value);
	}

	public Object get(String key) {
		return cached.get(key);
	}

}
