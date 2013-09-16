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
package com.hoperun.telematics.mobile.model.fuel;

import com.hoperun.telematics.mobile.model.BaseResponse;

/**
 * 
 * @author fan_leilei
 * 
 */
public class FuelResponse extends BaseResponse {

	private int maxFuel;
	private int remainingFuel;
	private float consumption;
	private float remainingMileage;

	/**
	 * @return the maxFuel
	 */
	public int getMaxFuel() {
		return maxFuel;
	}

	/**
	 * @param maxFuel
	 *            the maxFuel to set
	 */
	public void setMaxFuel(int maxFuel) {
		this.maxFuel = maxFuel;
	}

	/**
	 * @return the remainingFuel
	 */
	public int getRemainingFuel() {
		return remainingFuel;
	}

	/**
	 * @param remainingFuel
	 *            the remainingFuel to set
	 */
	public void setRemainingFuel(int remainingFuel) {
		this.remainingFuel = remainingFuel;
	}

	/**
	 * @return the consumption
	 */
	public float getConsumption() {
		return consumption;
	}

	/**
	 * @param consumption
	 *            the consumption to set
	 */
	public void setConsumption(float consumption) {
		this.consumption = consumption;
	}

	/**
	 * @return the remainingMileage
	 */
	public float getRemainingMileage() {
		return remainingMileage;
	}

	/**
	 * @param remainingMileage
	 *            the remainingMileage to set
	 */
	public void setRemainingMileage(float remainingMileage) {
		this.remainingMileage = remainingMileage;
	}

	/**
	 * Creates a new instance of FuelResponse.
	 */
	public FuelResponse(int maxFuel, int remainingFuel, float consumption, float remainingMileage) {
		super();
		this.maxFuel = maxFuel;
		this.remainingFuel = remainingFuel;
		this.consumption = consumption;
		this.remainingMileage = remainingMileage;
	}

	/**
	 * Creates a new instance of FuelResponse.
	 */
	public FuelResponse() {
		super();
	}
}
