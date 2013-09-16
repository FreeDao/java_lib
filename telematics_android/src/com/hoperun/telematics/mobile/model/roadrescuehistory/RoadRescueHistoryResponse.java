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
package com.hoperun.telematics.mobile.model.roadrescuehistory;

import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueHistoryResponse extends BaseResponse {
	private int curIndex;
	private int totalSize;
	private List<RescueInfo> rescueList;

	/**
	 * Creates a new instance of RoadRescueHistoryResponse.
	 */
	public RoadRescueHistoryResponse() {
		super();
	}

	/**
	 * Creates a new instance of RoadRescueHistoryResponse.
	 */
	public RoadRescueHistoryResponse(int curIndex, int totalSize, List<RescueInfo> rescueList) {
		super();
		this.curIndex = curIndex;
		this.totalSize = totalSize;
		this.rescueList = rescueList;
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

	public List<RescueInfo> getRescueList() {
		return rescueList;
	}

	public void setRescueList(List<RescueInfo> rescueList) {
		this.rescueList = rescueList;
	}

	/**
	 * Parse string to RoadRescueHistoryResponse object
	 * 
	 * @param json
	 * @return RoadRescueHistoryResponse or null
	 */
	public static RoadRescueHistoryResponse parseJsonToObject(String json) {
		RoadRescueHistoryResponse roadRescueHistoryResponse = null;
		try {
			Gson gson = new Gson();
			roadRescueHistoryResponse = gson.fromJson(json, RoadRescueHistoryResponse.class);
		} catch (Exception e) {
			Log.e("RemoteControlResponse", e.getMessage(), e);
		}
		return roadRescueHistoryResponse;
	}
}
