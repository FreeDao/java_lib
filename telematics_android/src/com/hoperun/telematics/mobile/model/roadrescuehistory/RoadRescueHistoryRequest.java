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

import java.util.Date;

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueHistoryRequest extends BaseRequest{
	private String accountId;
	private Date dateFrom;
	private Date dateTo;
	private int index;
	private int maxSize;

	/**
	 * Creates a new instance of RoadRescueHistoryRequest.
	 */
	public RoadRescueHistoryRequest() {
		super();
	}

	/**
	 * Creates a new instance of RoadRescueHistoryRequest.
	 */
	public RoadRescueHistoryRequest(String accountId, Date dateFrom, Date dateTo, int index, int maxSize) {
		super();
		this.accountId = accountId;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.index = index;
		this.maxSize = maxSize;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

}
