package com.hoperun.telematics.mobile.model.login;

public class InsuranceInfo {
	private String company;
	private String insuranceNum;
	private String phoneNum;
	private String type;
	private String amount;

	public InsuranceInfo() {

	}

	public InsuranceInfo(String company, String insuranceNum, String phoneNum,
			String type, String amount) {
		this.company = company;
		this.insuranceNum = insuranceNum;
		this.phoneNum = phoneNum;
		this.type = type;
		this.amount = amount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getInsuranceNum() {
		return insuranceNum;
	}

	public void setInsuranceNum(String insuranceNum) {
		this.insuranceNum = insuranceNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
