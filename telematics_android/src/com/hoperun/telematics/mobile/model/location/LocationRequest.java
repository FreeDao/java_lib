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
package com.hoperun.telematics.mobile.model.location;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class LocationRequest extends BaseRequest {
	private String accountId;
	private String license;
	private String vin;

	/**
	 * Creates a new instance of LocationRequest.
	 */
	public LocationRequest() {
		super();
	}

	public LocationRequest(String accountid, String license, String vin) {
		super();
		this.accountId = accountid;
		this.license = license;
		this.vin = vin;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String parseObjecttoJson(LocationRequest locationRequest) {
		Gson gson = new Gson();
		String str = gson.toJson(locationRequest);
		return str;
	}

}
