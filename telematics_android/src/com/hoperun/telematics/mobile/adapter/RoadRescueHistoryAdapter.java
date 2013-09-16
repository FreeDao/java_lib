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
package com.hoperun.telematics.mobile.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.model.roadrescuehistory.RescueInfo;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RoadRescueHistoryAdapter extends BaseAdapter {
	private static final String TAG = "RoadRescueHistoryAdapter";
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Object> mList;

	public RoadRescueHistoryAdapter(Context context, List<RescueInfo> rescueList) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<Object>();
		// Sort violationList according to date DESC
		Collections.sort(rescueList, new Comparator<RescueInfo>() {
			@Override
			public int compare(RescueInfo a, RescueInfo b) {
				long startTimeA = a.getTimeFrom();
				long startTimeB = b.getTimeFrom();
				if (startTimeA > startTimeB) {
					return -1;
				} else if (startTimeA < startTimeB) {
					return 1;
				}
				return 0;
			}
		});
		/*
		 * Add all violation list data to mList. And we will add related tag for
		 * each group.
		 */
		int size = rescueList.size();
		RescueInfo tempRescueInfo;
		int month = -1;
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < size; i++) {
			tempRescueInfo = rescueList.get(i);
			calendar.setTimeInMillis(tempRescueInfo.getTimeFrom());
			if (month != (calendar.get(Calendar.MONTH) + 1)) {
				month = calendar.get(Calendar.MONTH) + 1;
				mList.add(month);
			}
			mList.add(tempRescueInfo);
		}
		CacheManager.getInstance().setRoadRescueHistoryList(mList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object item = mList.get(position);
		if (item instanceof RescueInfo) {
			Calendar calendar = Calendar.getInstance();
			convertView = mInflater.inflate(R.layout.ui_road_rescue_item_layout, null);
			RescueInfo rescueInfo = (RescueInfo) item;
			calendar.setTimeInMillis(rescueInfo.getTimeFrom());
			
			TextView dayTextView = (TextView) convertView.findViewById(R.id.date_image);
			dayTextView.setText(String.format("%s", calendar.get(Calendar.DAY_OF_MONTH)));
			
			TextView timeTextView = (TextView) convertView.findViewById(R.id.time);
			timeTextView.setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY),
			        calendar.get(Calendar.SECOND)));
			
			TextView addressTextView = (TextView) convertView.findViewById(R.id.address);
			addressTextView.setText(rescueInfo.getAddress());
		} else {
			convertView = mInflater.inflate(R.layout.ui_road_rescue_history_item_tag_layout, null);
			TextView textView = (TextView) convertView.findViewById(R.id.month_textview);
			textView.setText(String.format("%s%s", item.toString(), mContext.getString(R.string.month)));
		}
		return convertView;
	}

}
