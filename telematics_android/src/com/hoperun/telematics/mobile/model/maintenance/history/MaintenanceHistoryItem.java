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
package com.hoperun.telematics.mobile.model.maintenance.history;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintenanceHistoryItem implements Parcelable{

	private String date;
	private long mileage;
	private String content;
	private String address;
	private String note;

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the mileage
	 */
	public long getMileage() {
		return mileage;
	}

	/**
	 * @param mileage
	 *            the mileage to set
	 */
	public void setMileage(long mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Creates a new instance of MaintenanceHistoryItem.
	 */
	public MaintenanceHistoryItem(String date, long mileage, String content, String address, String note) {
		super();
		this.date = date;
		this.mileage = mileage;
		this.content = content;
		this.address = address;
		this.note = note;
	}

	/**
	 * Creates a new instance of MaintenanceHistoryItem.
	 */
	public MaintenanceHistoryItem() {
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
		parcel.writeString(date);
		parcel.writeLong(mileage);
		parcel.writeString(content);
		parcel.writeString(address);
		parcel.writeString(note);

	}

	public static final Parcelable.Creator<MaintenanceHistoryItem> CREATOR = new Creator<MaintenanceHistoryItem>() {
		@Override
		public MaintenanceHistoryItem createFromParcel(Parcel source) {
			MaintenanceHistoryItem item = new MaintenanceHistoryItem();
			item.date = source.readString();
			item.mileage = source.readLong();
			item.content = source.readString();
			item.address = source.readString();
			item.note = source.readString();
			return item;
		}

		@Override
		public MaintenanceHistoryItem[] newArray(int size) {
			return new MaintenanceHistoryItem[size];
		}
	};
}
