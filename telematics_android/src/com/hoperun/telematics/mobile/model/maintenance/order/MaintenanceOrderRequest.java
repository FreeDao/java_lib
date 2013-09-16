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

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceOrderRequest extends BaseRequest {
	private String vin;
	private String license;
	private String dateFrom;
	private String dateTo;
	private String index;
	private String maxSize;

	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @param vin
	 *            the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license
	 *            the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @return the dateFrom
	 */
	public String getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom
	 *            the dateFrom to set
	 */
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public String getDateTo() {
		return dateTo;
	}

	/**
	 * @param dateTo
	 *            the dateTo to set
	 */
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the maxSize
	 */
	public String getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize
	 *            the maxSize to set
	 */
	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * Creates a new instance of MaintainHistoryRequest.
	 */
	public MaintenanceOrderRequest(String vin, String license, String dateFrom, String dateTo, String index,
			String maxSize) {
		super();
		this.vin = vin;
		this.license = license;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.index = index;
		this.maxSize = maxSize;
	}

	/**
	 * Creates a new instance of MaintainHistoryRequest.
	 */
	public MaintenanceOrderRequest() {
		super();
	}

}
