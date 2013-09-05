package com.serversample.util;

import com.google.gson.Gson;

/**
 * 
 * @author Administrator
 *
 */
public class JsonHelper {

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String doStringify(Object obj) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		return jsonString;
	}

	/**
	 * 
	 * @param jsonString
	 * @param objClass
	 * @return
	 */
	public static <T extends Object> T parseString(String jsonString, Class<T> objClass) {
		Gson gson = new Gson();
		T response = gson.fromJson(jsonString, objClass);
		return response;
	}
}
