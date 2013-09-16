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
package com.hoperun.telematics.mobile.model.track;

import java.sql.Timestamp;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class TrackRequest extends BaseRequest{
	private String accountId;
	private Timestamp timeFrom;
	private Timestamp timeTo;
	
	/**
	 * Creates a new instance of TrackRequest.
	 */
	public TrackRequest() {
		super();
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Timestamp getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Timestamp timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Timestamp getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Timestamp timeTo) {
		this.timeTo = timeTo;
	}
	
	public String parseObjecttoJson(TrackRequest trackRequest){
		Gson gson = new Gson();
		String str = gson.toJson(trackRequest);
		return str;
	}
}
