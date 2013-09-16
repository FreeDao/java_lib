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

import java.util.Date;

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ViolationRequest extends BaseRequest {
	private String vin;
	private String license;
	private Date dateFrom;
	private Date dateTo;
	private int index;
	private int maxSize;

	/**
	 * Creates a new instance of ViolationRequest.
	 */
	public ViolationRequest() {
		super();
	}

	/**
	 * Creates a new instance of ViolationRequest.
	 */
	public ViolationRequest(String vin, String license, Date dateFrom, Date dateTo, int index, int maxSize) {
		super();
		this.vin = vin;
		this.license = license;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.index = index;
		this.maxSize = maxSize;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
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
