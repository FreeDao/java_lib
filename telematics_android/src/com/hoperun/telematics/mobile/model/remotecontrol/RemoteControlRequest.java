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
package com.hoperun.telematics.mobile.model.remotecontrol;

import com.hoperun.telematics.mobile.model.BaseRequest;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RemoteControlRequest extends BaseRequest {
	private String vin;
	private String license;
	private int commandId;
	private String description;

	/**
	 * Creates a new instance of RemoteControlRequest.
	 */
	public RemoteControlRequest() {
		super();
	}

	/**
	 * Creates a new instance of RemoteControlRequest.
	 */
	public RemoteControlRequest(String vin, String license, int commandId, String description) {
		super();
		this.vin = vin;
		this.license = license;
		this.commandId = commandId;
		this.description = description;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
