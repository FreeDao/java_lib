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
package com.hoperun.telematics.mobile.model.violation;

import java.util.Date;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ViolationInfo {
	private String id;
	private Date date;
	private String summary;
	private boolean isDealed;
	private String content;
	private String location;
	private String legalBasis;
	private int subtractedScore;
	private int fine;

	/**
	 * Creates a new instance of ViolationInfo.
	 */
	public ViolationInfo() {
		super();
	}

	/**
	 * Creates a new instance of ViolationInfo.
	 */
	public ViolationInfo(String id, Date date, String summary, boolean isDealed, String content, String location,
	        String legalBasis, int subtractedScore, int fine) {
		super();
		this.id = id;
		this.date = date;
		this.summary = summary;
		this.isDealed = isDealed;
		this.content = content;
		this.location = location;
		this.legalBasis = legalBasis;
		this.subtractedScore = subtractedScore;
		this.fine = fine;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public boolean isDealed() {
		return isDealed;
	}

	public void setDealed(boolean isDealed) {
		this.isDealed = isDealed;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLegalBasis() {
		return legalBasis;
	}

	public void setLegalBasis(String legalBasis) {
		this.legalBasis = legalBasis;
	}

	public int getSubtractedScore() {
		return subtractedScore;
	}

	public void setSubtractedScore(int subtractedScore) {
		this.subtractedScore = subtractedScore;
	}

	public int getFine() {
		return fine;
	}

	public void setFine(int fine) {
		this.fine = fine;
	}

}
