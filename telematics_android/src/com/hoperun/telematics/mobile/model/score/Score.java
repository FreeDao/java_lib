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
package com.hoperun.telematics.mobile.model.score;

/**
 * 
 * @author chen_guigui
 * 
 */
public class Score {
	private String id;
	private String name;
	private int cost;
	private String resourceId;

	/**
	 * Creates a new instance of Score.
	 */
	public Score() {
		super();
	}

	/**
	 * Creates a new instance of Score.
	 */
	public Score(String id, String name, int cost, String resourceId) {
		super();
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.resourceId = resourceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
