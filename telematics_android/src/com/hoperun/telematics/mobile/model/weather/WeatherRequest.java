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
package com.hoperun.telematics.mobile.model.weather;

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author fan_leilei
 * 
 */
public class WeatherRequest extends BaseRequest {

	private String longitude;
	private String latitude;

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * Creates a new instance of WeatherRequest.
	 */
	public WeatherRequest() {
	}

	/**
	 * Creates a new instance of WeatherRequest.
	 */
	public WeatherRequest(String longitude, String latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}
}
