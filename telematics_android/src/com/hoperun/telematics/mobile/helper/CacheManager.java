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
package com.hoperun.telematics.mobile.helper;

import java.util.Date;
import java.util.List;

import com.hoperun.telematics.mobile.model.buddy.Friend;
import com.hoperun.telematics.mobile.model.location.GeoLocation;
import com.hoperun.telematics.mobile.model.login.InsuranceInfo;
import com.hoperun.telematics.mobile.model.login.LoginResponse;
import com.hoperun.telematics.mobile.model.login.ServiceInfo;
import com.hoperun.telematics.mobile.model.login.UserInfo;
import com.hoperun.telematics.mobile.model.login.VehicleInfo;

/**
 * 
 * @author he_chen
 * 
 */
public class CacheManager {

	/************** user info *********************/
	private String accountId;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	private String city;
	private String huId;
	private Date registrationDate;
	private int score;
	private String locale;
	private String timeZone;

	/************** vehicle info *********************/
	private String vin;
	private String license;
	private String oem;
	private String model;
	private int engineCapacity;
	private String transmissionType;
	private String engineNum;
	private Date vehicleRegistrationDate;
	private int maintenMiles;

	private List<ServiceInfo> serviceList;
	private List<InsuranceInfo> insuranceList;
	private List<Object> roadRescueHistoryList;
	private List<Object> violationList;

	private List<Friend> friendList;
	private GeoLocation geoLocation;

	private static CacheManager instance;

	private CacheManager() {

	}

	public static CacheManager getInstance() {
		if (instance == null) {
			instance = new CacheManager();
		}
		return instance;
	}

	/**
	 * @param loginResponse
	 *            the loginResponse to set
	 */
	public void updateLoginResponse(LoginResponse loginResponse) {
		UserInfo userInfo = loginResponse.getUserInfo();
		if (null != userInfo) {
			this.accountId = userInfo.getAccountId();
			this.firstName = userInfo.getFirstName();
			this.lastName = userInfo.getLastName();
			this.phoneNumber = userInfo.getPhoneNumber();
			this.emailAddress = userInfo.getEmailAddress();
			this.city = userInfo.getCity();
			this.huId = userInfo.getHuId();
			this.registrationDate = userInfo.getRegistrationDate();
			this.score = userInfo.getScore();
			this.locale = userInfo.getTimeZone();
		}

		VehicleInfo vehicleInfo = loginResponse.getVehicleInfo();
		if (null != vehicleInfo) {
			this.vin = vehicleInfo.getVin();
			this.license = vehicleInfo.getLicense();
			this.oem = vehicleInfo.getOem();
			this.model = vehicleInfo.getModel();
			this.engineCapacity = vehicleInfo.getEngineCapacity();
			this.transmissionType = vehicleInfo.getTransmissionType();
			this.engineNum = vehicleInfo.getEngineNum();
			this.vehicleRegistrationDate = vehicleInfo.getRegistrationDate();
			this.maintenMiles = vehicleInfo.getMaintenMiles();
		}

		this.serviceList = (null != loginResponse.getServiceList() ? loginResponse.getServiceList() : null);
		this.insuranceList = (null != loginResponse.getInsuranceList() ? loginResponse.getInsuranceList() : null);
	}

	public String getAccountId() {
		return accountId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getCity() {
		return city;
	}

	public String getHuId() {
		return huId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public int getScore() {
		return score;
	}

	public String getLocale() {
		return locale;
	}

	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @return the oem
	 */
	public String getOem() {
		return oem;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return the engineCapacity
	 */
	public int getEngineCapacity() {
		return engineCapacity;
	}

	/**
	 * @return the transmissionType
	 */
	public String getTransmissionType() {
		return transmissionType;
	}

	/**
	 * @return the engineNum
	 */
	public String getEngineNum() {
		return engineNum;
	}

	/**
	 * @return the vehicleRegistrationDate
	 */
	public Date getVehicleRegistrationDate() {
		return vehicleRegistrationDate;
	}

	/**
	 * @return the maintenMiles
	 */
	public int getMaintenMiles() {
		return maintenMiles;
	}

	/**
	 * @return the serviceList
	 */
	public List<ServiceInfo> getServiceList() {
		return serviceList;
	}

	/**
	 * @return the insuranceList
	 */
	public List<InsuranceInfo> getInsuranceList() {
		return insuranceList;
	}

	/**
	 * @return friendlist
	 */
	public List<Friend> getFriendList() {
		return friendList;
	}

	/***************** TODO fanleilei, method for test *******************************************/
	public String getTestVin() {
		return "1G1BL52P7TR115520";
	}

	public String getTestLicense() {
		return "苏A NB110";
	}

	public List<Object> getRoadRescueHistoryList() {
		return roadRescueHistoryList;
	}

	public void setRoadRescueHistoryList(List<Object> roadRescueHistoryList) {
		this.roadRescueHistoryList = roadRescueHistoryList;
	}

	public List<Object> getViolationList() {
		return violationList;
	}

	public void setViolationList(List<Object> violationList) {
		this.violationList = violationList;
	}

	public void updateGeoLocation(double lat, double lng) {
		geoLocation = new GeoLocation();
		geoLocation.setLat(lat);
		geoLocation.setLng(lng);
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}
}
