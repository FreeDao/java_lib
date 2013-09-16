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

/**
 * 
 * @author fan_leilei
 * 
 */
public class Weather {

	public static final int WASH_STATE_LOW = 1;
	public static final int WASH_STATE_MIDDLE = 2;
	public static final int WASH_STATE_HIGH = 3;
	
	private String condition;
	private String highTemperature;
	private String lowTemperature;
	private String date;
	private String week;
	private String wind;
	private String humidness;
	private int washState;

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return the highTemperature
	 */
	public String getHighTemperature() {
		return highTemperature;
	}

	/**
	 * @param highTemperature
	 *            the highTemperature to set
	 */
	public void setHighTemperature(String highTemperature) {
		this.highTemperature = highTemperature;
	}

	/**
	 * @return the lowTemperature
	 */
	public String getLowTemperature() {
		return lowTemperature;
	}

	/**
	 * @param lowTemperature
	 *            the lowTemperature to set
	 */
	public void setLowTemperature(String lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the week
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}

	/**
	 * @return the wind
	 */
	public String getWind() {
		return wind;
	}

	/**
	 * @param wind
	 *            the wind to set
	 */
	public void setWind(String wind) {
		this.wind = wind;
	}

	/**
	 * @return the humidness
	 */
	public String getHumidness() {
		return humidness;
	}

	/**
	 * @param humidness
	 *            the humidness to set
	 */
	public void setHumidness(String humidness) {
		this.humidness = humidness;
	}

	/**
	 * @return the washState
	 */
	public int getWashState() {
		return washState;
	}

	/**
	 * @param washState
	 *            the washState to set
	 */
	public void setWashState(int washState) {
		this.washState = washState;
	}

	/**
	 * Creates a new instance of Weather.
	 */
	public Weather(String condition, String highTemperature, String lowTemperature, String date, String week,
			String wind, String humidness, int washState) {
		super();
		this.condition = condition;
		this.highTemperature = highTemperature;
		this.lowTemperature = lowTemperature;
		this.date = date;
		this.week = week;
		this.wind = wind;
		this.humidness = humidness;
		this.washState = washState;
	}

	/**
	 * Creates a new instance of Weather.
	 */
	public Weather() {
	}

}
