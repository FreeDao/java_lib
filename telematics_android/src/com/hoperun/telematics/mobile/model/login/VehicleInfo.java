package com.hoperun.telematics.mobile.model.login;

import java.util.Date;

public class VehicleInfo {
	
	private String vin;
	private String license;
	private String oem;
	private String model;
	private int engineCapacity;
	private String transmissionType;
	private String engineNum;
	private Date registrationDate;
	private int maintenMiles;

	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @param vin
	 *            the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license
	 *            the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @return the oem
	 */
	public String getOem() {
		return oem;
	}

	/**
	 * @param oem
	 *            the oem to set
	 */
	public void setOem(String oem) {
		this.oem = oem;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the engineCapacity
	 */
	public int getEngineCapacity() {
		return engineCapacity;
	}

	/**
	 * @param engineCapacity
	 *            the engineCapacity to set
	 */
	public void setEngineCapacity(int engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	/**
	 * @return the transmissionType
	 */
	public String getTransmissionType() {
		return transmissionType;
	}

	/**
	 * @param transmissionType
	 *            the transmissionType to set
	 */
	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	/**
	 * @return the engineNum
	 */
	public String getEngineNum() {
		return engineNum;
	}

	/**
	 * @param engineNum
	 *            the engineNum to set
	 */
	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate
	 *            the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the maintenMiles
	 */
	public int getMaintenMiles() {
		return maintenMiles;
	}

	/**
	 * @param maintenMiles
	 *            the maintenMiles to set
	 */
	public void setMaintenMiles(int maintenMiles) {
		this.maintenMiles = maintenMiles;
	}

}
