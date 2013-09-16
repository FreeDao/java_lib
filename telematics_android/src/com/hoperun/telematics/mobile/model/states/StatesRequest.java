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

import com.hoperun.telematics.mobile.model.BaseRequest;


/**
 * 
 * @author fan_leilei
 * 
 */
public class StatesRequest extends BaseRequest{

	private String vin;
	private String license;
	/**
	 * Creates a new instance of StatesRequest.
	 */
	public StatesRequest(String vin, String license) {
		super();
		this.vin = vin;
		this.license = license;
	}
	/**
	 * Creates a new instance of StatesRequest.
	 */
	public StatesRequest() {
		super();
	}
	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}
	/**
	 * @param vin the vin to set
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
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}
	
	
}
