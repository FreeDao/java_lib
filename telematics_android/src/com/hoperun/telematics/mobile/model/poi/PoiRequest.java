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
package com.hoperun.telematics.mobile.model.poi;

import com.google.gson.Gson;
import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * 
 * @author fan_leilei
 * 
 */
public class PoiRequest extends BaseRequest {

	public enum EPoiRequestType {
		Parking(1), GasStation(2);
		private int value;

		private EPoiRequestType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static EPoiRequestType getEPoiRequestType(int value) {
			return value == Parking.getValue() ? Parking : GasStation;
		}

	}

	private int type;
	private String longitude;
	private String latitude;
	private int radius;
	private int index;
	private int maxSize;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

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
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(int radius) {
		this.radius = radius;
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

	/**
	 * @param type
	 * @param longitude
	 * @param latitude
	 * @param radius
	 */
	public PoiRequest(int type, String longitude, String latitude, int radius, int index, int maxSize) {
		super();
		this.type = type;
		this.longitude = longitude;
		this.latitude = latitude;
		this.radius = radius;
		this.index = index;
		this.maxSize = maxSize;
	}

	/**
	 * 
	 */
	public PoiRequest() {
	}

	/**
	 * @Title: toJsonStr
	 * @Description: change to json format string
	 * @param @return
	 */
	@Override
	public String toJsonStr() {
		Gson gson = new Gson();
		return gson.toJson(this, PoiRequest.class);
	}
}
