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
package com.hoperun.telematics.mobile.model.maintenance.order;

import java.util.List;

import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceOrderResponse extends BaseResponse {

	/**
	 * minimum value is 1
	 */
	private int curIndex;
	private int totalSize;
	private List<MaintenanceOrderItem> orderList;

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
	public List<MaintenanceOrderItem> getHistoryList() {
		return orderList;
	}


	/**
	 * Creates a new instance of MaintenanceHistoryResponse.
	 */
	public MaintenanceOrderResponse() {
		super();
	}

	/**
	 * @return the orderList
	 */
	public List<MaintenanceOrderItem> getOrderList() {
		return orderList;
	}

	/**
	 * @param orderList the orderList to set
	 */
	public void setOrderList(List<MaintenanceOrderItem> orderList) {
		this.orderList = orderList;
	}

	/**
	 * Creates a new instance of MaintenanceOrderResponse.
	 */
	public MaintenanceOrderResponse(int curIndex, int totalSize, List<MaintenanceOrderItem> orderList) {
		super();
		this.curIndex = curIndex;
		this.totalSize = totalSize;
		this.orderList = orderList;
	}

}
