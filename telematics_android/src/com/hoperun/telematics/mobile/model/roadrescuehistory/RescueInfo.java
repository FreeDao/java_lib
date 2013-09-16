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
package com.hoperun.telematics.mobile.model.roadrescuehistory;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RescueInfo {
	private long timeFrom;
	private long timeTo;
	private String address;
	private String longitude;
	private String latitude;
	private String rescuerName;
	private int rate;
	private String note;

	/**
	 * Creates a new instance of RescueInfo.
	 */
	public RescueInfo() {
		super();
	}

	/**
	 * Creates a new instance of RescueInfo.
	 */
	public RescueInfo(long timeFrom, long timeTo, String address, String longitude, String latitude,
	        String rescuerName, int rate, String note) {
		super();
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.rescuerName = rescuerName;
		this.rate = rate;
		this.note = note;
	}

	public long getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(long timeFrom) {
		this.timeFrom = timeFrom;
	}

	public long getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(long timeTo) {
		this.timeTo = timeTo;
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

	public String getRescuerName() {
		return rescuerName;
	}

	public void setRescuerName(String rescuerName) {
		this.rescuerName = rescuerName;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
