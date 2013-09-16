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

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceOrderItem {

	private String date;
	private String address;
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * Creates a new instance of MaintenanceOrder.
	 */
	public MaintenanceOrderItem(String date, String address) {
		super();
		this.date = date;
		this.address = address;
	}
	/**
	 * Creates a new instance of MaintenanceOrder.
	 */
	public MaintenanceOrderItem() {
		super();
	}
	
}
