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
package com.hoperun.telematics.mobile.model.roadrescue;

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueRequest extends BaseRequest {
	private String latitude;
	private String longitude;

	/**
	 * Creates a new instance of RoadRescueRequest.
	 */
	public RoadRescueRequest() {
		super();
	}

	/**
	 * Creates a new instance of RoadRescueRequest.
	 */
	public RoadRescueRequest(String latitude, String longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
