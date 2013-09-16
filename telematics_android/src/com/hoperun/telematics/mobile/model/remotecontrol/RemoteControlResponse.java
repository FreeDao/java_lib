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
package com.hoperun.telematics.mobile.model.remotecontrol;

import android.util.Log;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RemoteControlResponse extends BaseResponse {
	private int stateId;
	private String description;

	/**
	 * Creates a new instance of RemoteControlResponse.
	 */
	public RemoteControlResponse() {
		super();
	}

	/**
	 * Creates a new instance of RemoteControlResponse.
	 */
	public RemoteControlResponse(int stateId, String description) {
		super();
		this.stateId = stateId;
		this.description = description;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static RemoteControlResponse parseJsonToObject(String json) {
		RemoteControlResponse remoteControlResponse = null;
		try {
			Gson gson = new Gson();
			remoteControlResponse = gson.fromJson(json, RemoteControlResponse.class);
		} catch (Exception e) {
			Log.e("RemoteControlResponse", e.getMessage(), e);
		}
		return remoteControlResponse;
	}

}
