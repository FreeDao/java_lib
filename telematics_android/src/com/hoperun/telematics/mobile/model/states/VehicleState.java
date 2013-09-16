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
package com.hoperun.telematics.mobile.model.states;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author fan_leilei
 * 
 */
public class VehicleState implements Parcelable {

	private String faultId;
	private String positionId;
	private String positionName;
	private String description;
	private int faultLevel;
	private String suggestion;

	public enum FaultPositionId {
		// 分别代表空调，轮胎,车灯，发动机
		AirCondition("P001"), Tire("P002"),Light ("P003"), Engine("P1234");
		private String positionId;
		public static String[] positionIdArray = { AirCondition.getPositionId(), Tire.getPositionId(),
			Light.getPositionId(), Engine.getPositionId() };

		private FaultPositionId(String positionId) {
			this.positionId = positionId;
		}

		private String getPositionId() {
			return this.positionId;
		}
	}

	/**
	 * @return the faultId
	 */
	public String getFaultId() {
		return faultId;
	}

	/**
	 * @param faultId
	 *            the faultId to set
	 */
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}

	/**
	 * @return the positionId
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId
	 *            the positionId to set
	 */
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	/**
	 * @return the positionName
	 */
	public String getPositionName() {
		return positionName;
	}

	/**
	 * @param positionName
	 *            the positionName to set
	 */
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the faultLevel
	 */
	public int getFaultLevel() {
		return faultLevel;
	}

	/**
	 * @param faultLevel
	 *            the faultLevel to set
	 */
	public void setFaultLevel(int faultLevel) {
		this.faultLevel = faultLevel;
	}

	/**
	 * @return the suggestion
	 */
	public String getSuggestion() {
		return suggestion;
	}

	/**
	 * @param suggestion
	 *            the suggestion to set
	 */
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	/**
	 * Creates a new instance of VehicleState.
	 */
	public VehicleState(String faultId, String positionId, String positionName, String description, int faultLevel,
			String suggestion) {
		super();
		this.faultId = faultId;
		this.positionId = positionId;
		this.positionName = positionName;
		this.description = description;
		this.faultLevel = faultLevel;
		this.suggestion = suggestion;
	}

	/**
	 * Creates a new instance of VehicleState.
	 */
	public VehicleState() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(faultId);
		parcel.writeString(positionId);
		parcel.writeString(positionName);
		parcel.writeString(description);
		parcel.writeInt(faultLevel);
		parcel.writeString(suggestion);

	}

	public static final Parcelable.Creator<VehicleState> CREATOR = new Creator<VehicleState>() {
		@Override
		public VehicleState createFromParcel(Parcel source) {
			VehicleState mVehicleState = new VehicleState();
			mVehicleState.faultId = source.readString();
			mVehicleState.positionId = source.readString();
			mVehicleState.positionName = source.readString();
			mVehicleState.description = source.readString();
			mVehicleState.faultLevel = source.readInt();
			mVehicleState.suggestion = source.readString();
			return mVehicleState;
		}

		@Override
		public VehicleState[] newArray(int size) {
			return new VehicleState[size];
		}
	};

}
