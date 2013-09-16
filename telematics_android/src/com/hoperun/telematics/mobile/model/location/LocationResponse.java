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

import java.sql.Timestamp;

import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class LocationResponse extends BaseResponse{
	private Timestamp time;
	private String address;
	private String longitude;
	private String latitude;
	
	/**
	 * Creates a new instance of LocationResponse.
	 */
	public LocationResponse(Timestamp time,String address,String latitude,String longitude) {
		super();
		this.time=time;
		this.address=address;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

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
}
