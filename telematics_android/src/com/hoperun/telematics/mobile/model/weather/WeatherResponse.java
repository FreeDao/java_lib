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

import java.util.List;

import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class WeatherResponse extends BaseResponse {

	private List<Weather> weatherList;
	private String currentTemperature;

	/**
	 * @return the weatherList
	 */
	public List<Weather> getWeatherList() {
		return weatherList;
	}

	/**
	 * @param weatherList
	 *            the weatherList to set
	 */
	public void setWeatherList(List<Weather> weatherList) {
		this.weatherList = weatherList;
	}

	/**
	 * @return the currentTemperature
	 */
	public String getCurrentTemperature() {
		return currentTemperature;
	}

	/**
	 * @param currentTemperature
	 *            the currentTemperature to set
	 */
	public void setCurrentTemperature(String currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	/**
	 * Creates a new instance of WeatherResponse.
	 */
	public WeatherResponse(List<Weather> weatherList, String currentTemperature) {
		super();
		this.weatherList = weatherList;
		this.currentTemperature = currentTemperature;
	}

	/**
	 * Creates a new instance of WeatherResponse.
	 */
	public WeatherResponse() {
	}
}
