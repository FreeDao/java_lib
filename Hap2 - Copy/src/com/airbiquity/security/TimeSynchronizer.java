/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *            
 *                   @author 
 *                  
 *
 *****************************************************************************/
package com.airbiquity.security;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.airbiquity.util_net.HttpUtil;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;
import com.airbiquity.util_net.UrlMaker;



public class TimeSynchronizer {
	
	private static final String TAG = TimeSynchronizer.class.getName();
	
	private static TimeSynchronizer INSTANCE = null;
	
	private TimeSynchronizer(){}

	/**
	 * Singleton Instance of the class
	 * @return
	 */
	public synchronized static TimeSynchronizer getInstance(){
		if ( null == INSTANCE ){
			INSTANCE  =  new TimeSynchronizer();
		}
		return INSTANCE;
	}
	
	/**
	 * Fires a request to backend server and returns the time as a long value.
	 * </br>
	 * It is the backend server System time in milliseconds 
	 * @return
	 */
	public long getBackendTime(){
		
		
		long l = 0l;
		URL url = UrlMaker.getUrlForTimeSync();
        NetReq req = new NetReq( url, "");
        Log.d(TAG, "time sync req = " + req.toString());
        NetResp resp = HttpUtil.com( req );
        
        if(resp.code == HttpURLConnection.HTTP_OK){
        	try {
				JSONObject timeJson = new JSONObject(new String(resp.data));
				String time = timeJson.optString("time");
				l = Long.parseLong(time);
			} catch (JSONException e) {
				e.printStackTrace();
				l = 0;
			}catch (NumberFormatException e) {
				e.printStackTrace();
				l = 0;
			}
        }
        
        return l;
	} 
}
