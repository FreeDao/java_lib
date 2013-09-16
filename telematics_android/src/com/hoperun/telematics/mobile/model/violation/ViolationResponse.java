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
package com.hoperun.telematics.mobile.model.violation;

import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ViolationResponse extends BaseResponse {
	private int curIndex;
	private int totalSize;
	private List<ViolationInfo> violationList;

	/**
	 * Creates a new instance of ViolationResponse.
	 */
	public ViolationResponse() {
		super();
	}

	/**
	 * Creates a new instance of ViolationResponse.
	 */
	public ViolationResponse(int curIndex, int totalSize, List<ViolationInfo> violationList) {
		super();
		this.curIndex = curIndex;
		this.totalSize = totalSize;
		this.violationList = violationList;
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public List<ViolationInfo> getViolationList() {
		return violationList;
	}

	public void setViolationList(List<ViolationInfo> violationList) {
		this.violationList = violationList;
	}

	/**
	 * 
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static ViolationResponse parseJsonToObject(String json) {
		ViolationResponse violationResponse = null;
		try {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			violationResponse = gson.fromJson(json, ViolationResponse.class);
		} catch (Exception e) {
			Log.e("ViolationResponse", e.getMessage(), e);
		}
		return violationResponse;
	}

}
