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
package com.hoperun.telematics.mobile.model.states;

import java.util.List;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class StatesResponse extends BaseResponse {

	private List<VehicleState> stateList;

	/**
	 * @return the stateList
	 */
	public List<VehicleState> getStateList() {
		return stateList;
	}

	/**
	 * @param stateList
	 *            the stateList to set
	 */
	public void setStateList(List<VehicleState> stateList) {
		this.stateList = stateList;
	}

	/**
	 * Creates a new instance of StatesResponse.
	 */
	public StatesResponse(List<VehicleState> stateList) {
		super();
		this.stateList = stateList;
	}

	/**
	 * Creates a new instance of StatesResponse.
	 */
	public StatesResponse() {
		super();
	}

	public static StatesResponse toObject(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, StatesResponse.class);
	}
}
