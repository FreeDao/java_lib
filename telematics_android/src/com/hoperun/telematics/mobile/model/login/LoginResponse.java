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
package com.hoperun.telematics.mobile.model.login;

import java.util.List;

import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class LoginResponse extends BaseResponse {

	private UserInfo userInfo;
	private VehicleInfo vehicleInfo;
	private List<ServiceInfo> serviceList;
	private List<InsuranceInfo> insuranceList;

	/**
	 * @return the userInfo
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo
	 *            the userInfo to set
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * @return the vehicleInfo
	 */
	public VehicleInfo getVehicleInfo() {
		return vehicleInfo;
	}

	/**
	 * @param vehicleInfo
	 *            the vehicleInfo to set
	 */
	public void setVehicleInfo(VehicleInfo vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}

	/**
	 * @return the serviceList
	 */
	public List<ServiceInfo> getServiceList() {
		return serviceList;
	}

	/**
	 * @param serviceList
	 *            the serviceList to set
	 */
	public void setServiceList(List<ServiceInfo> serviceList) {
		this.serviceList = serviceList;
	}

	/**
	 * @return the insuranceList
	 */
	public List<InsuranceInfo> getInsuranceList() {
		return insuranceList;
	}

	/**
	 * @param insuranceList
	 *            the insuranceList to set
	 */
	public void setInsuranceList(List<InsuranceInfo> insuranceList) {
		this.insuranceList = insuranceList;
	}

}
