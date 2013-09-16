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
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.model.violation.ViolationInfo;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ViolationListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Object> mList;
	/*
	 * Note: the month is based on one, one means January, twelve means
	 * December. So -1 don't mean any month and it's invalid value.
	 */
	private int mMonth = -1;

	public ViolationListAdapter(Context context, List<ViolationInfo> violationList) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mList = new ArrayList<Object>();
		// Sort violationList according to date DESC
		Collections.sort(violationList, new Comparator<ViolationInfo>() {
			@Override
			public int compare(ViolationInfo a, ViolationInfo b) {
				long timeA = a.getDate().getTime();
				long timeB = b.getDate().getTime();
				if (timeA > timeB) {
					return -1;
				} else if (timeA < timeB) {
					return 1;
				}
				return 0;
			}
		});
		/*
		 * Add all violation list data to mList. And we will add related tag for
		 * each group.
		 */
		int size = violationList.size();
		ViolationInfo tempViolationInfo;
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < size; i++) {
			tempViolationInfo = violationList.get(i);
			calendar.setTime(tempViolationInfo.getDate());
			if (mMonth != (calendar.get(Calendar.MONTH) + 1)) {
				mMonth = calendar.get(Calendar.MONTH) + 1;
				mList.add(mMonth);
			}
			mList.add(tempViolationInfo);
		}
		CacheManager.getInstance().setViolationList(mList);

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
		Object item = null;
		if (mList.size() > position) {
			item = mList.get(position);
		}
		return item;
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
		if (item instanceof ViolationInfo) {
			Calendar calendar = Calendar.getInstance();
			convertView = mInflater.inflate(R.layout.ui_violation_item_layout, null);
			ViolationInfo violationInfo = (ViolationInfo) item;
			calendar.setTime(violationInfo.getDate());
			TextView dayTextView = (TextView) convertView.findViewById(R.id.date_image);
			TextView timeTextView = (TextView) convertView.findViewById(R.id.time);
			dayTextView.setText(String.format("%s", calendar.get(Calendar.DAY_OF_MONTH)));
			timeTextView.setText(String.format("%2s:%2s", calendar.get(Calendar.HOUR_OF_DAY),
			        calendar.get(Calendar.MINUTE)));
			TextView stateTextView = (TextView) convertView.findViewById(R.id.state);
			ImageView stateImageView = (ImageView) convertView.findViewById(R.id.state_image);
			if (violationInfo.isDealed()) {
				stateTextView.setText(R.string.is_dealed);
				stateImageView.setImageResource(R.drawable.violation_treated);
			} else {
				stateTextView.setText(R.string.not_dealed);
				stateImageView.setImageResource(R.drawable.violation_not_treated);
			}
			TextView addressTextView = (TextView) convertView.findViewById(R.id.address);
			addressTextView.setText(violationInfo.getLocation());

		} else {
			convertView = mInflater.inflate(R.layout.ui_violation_item_tag_layout, null);
			TextView textView = (TextView) convertView.findViewById(R.id.month_textview);
			textView.setText(String.format("%s%s", item.toString(), mContext.getString(R.string.month)));
		}
		return convertView;
	}

	/**
	 * Add extra violation info list.
	 * 
	 * @param list
	 */
	public void addExtraList(List<ViolationInfo> list) {
		/*
		 * Add all violation list data to mList. And we will add related tag for
		 * each group.
		 */
		int size = list.size();
		ViolationInfo tempViolationInfo;
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < size; i++) {
			tempViolationInfo = list.get(i);
			calendar.setTime(tempViolationInfo.getDate());
			if (mMonth != (calendar.get(Calendar.MONTH) + 1)) {
				mMonth = calendar.get(Calendar.MONTH) + 1;
				mList.add(mMonth);
			}
			mList.add(tempViolationInfo);
		}
		CacheManager.getInstance().setViolationList(mList);
	}

}
