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
package com.hoperun.telematics.mobile.model.maintenance.history;

import java.util.List;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceHistoryResponse extends BaseResponse {

	/**
	 * minimum value is 1
	 */
	private int curIndex;
	private int totalSize;
	private List<MaintenanceHistoryItem> historyList;

	/**
	 * @return the curIndex
	 */
	public int getCurIndex() {
		return curIndex;
	}

	/**
	 * @param curIndex
	 *            the curIndex to set
	 */
	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize
	 *            the totalSize to set
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the historyList
	 */
	public List<MaintenanceHistoryItem> getHistoryList() {
		return historyList;
	}

	/**
	 * @param historyList
	 *            the historyList to set
	 */
	public void setHistoryList(List<MaintenanceHistoryItem> historyList) {
		this.historyList = historyList;
	}

	/**
	 * Creates a new instance of MaintenanceHistoryResponse.
	 */
	public MaintenanceHistoryResponse(int curIndex, int totalSize, List<MaintenanceHistoryItem> historyList) {
		super();
		this.curIndex = curIndex;
		this.totalSize = totalSize;
		this.historyList = historyList;
	}

	/**
	 * Creates a new instance of MaintenanceHistoryResponse.
	 */
	public MaintenanceHistoryResponse() {
		super();
	}

	/**
	 * @param jsonStr
	 * @return
	 */
	public static MaintenanceHistoryResponse toObject(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, MaintenanceHistoryResponse.class);
	}
}
