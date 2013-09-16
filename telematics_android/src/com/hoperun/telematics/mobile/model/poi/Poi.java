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

import java.util.Map;

import com.amap.mapapi.core.GeoPoint;
import com.hoperun.telematics.mobile.framework.net.helper.UtilHelper;

/**
 * 
 * @author fan_leilei
 * 
 */
public class Poi {

	private String name;
	private String address;
	private String longitude;
	private String latitude;
	private Map<Object, Object> extInfo;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public GeoPoint getGeoPoint() {
		return UtilHelper.NewGeoPoint(Double.valueOf(latitude), Double.valueOf(longitude));
	}

	/**
	 * @return the extInfo
	 */
	public Map<Object, Object> getExtInfo() {
		return extInfo;
	}

	/**
	 * @param extInfo
	 *            the extInfo to set
	 */
	public void setExtInfo(Map<Object, Object> extInfo) {
		this.extInfo = extInfo;
	}

}
