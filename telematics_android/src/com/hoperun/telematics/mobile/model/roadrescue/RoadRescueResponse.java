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
package com.hoperun.telematics.mobile.model.roadrescue;

import android.util.Log;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueResponse extends BaseResponse {
	private String name;
	private String phoneNum;
	private String dymamicMessage;

	/**
	 * Creates a new instance of RoadRescueResponse.
	 */
	public RoadRescueResponse() {
		super();
	}

	/**
	 * Creates a new instance of RoadRescueResponse.
	 */
	public RoadRescueResponse(String name, String phoneNum, String dymamicMessage) {
		super();
		this.name = name;
		this.phoneNum = phoneNum;
		this.dymamicMessage = dymamicMessage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getDymamicMessage() {
		return dymamicMessage;
	}

	public void setDymamicMessage(String dymamicMessage) {
		this.dymamicMessage = dymamicMessage;
	}

	public static RoadRescueResponse parseJsonToObject(String json) {
		RoadRescueResponse roadRescueResponse = null;
		try {
			Gson gson = new Gson();
			roadRescueResponse = gson.fromJson(json, RoadRescueResponse.class);
		} catch (Exception e) {
			Log.e("RemoteControlResponse", e.getMessage(), e);
		}
		return roadRescueResponse;
	}

}
