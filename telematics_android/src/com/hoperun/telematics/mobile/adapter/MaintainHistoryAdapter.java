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

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.DateUtil;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryItem;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintainHistoryAdapter extends AbsGroupListAdapter<MaintenanceHistoryItem> {

	private int dayOfMonth;

	/**
	 * Creates a new instance of MaintainHistoryAdapter.
	 */
	public MaintainHistoryAdapter(Context context, List<MaintenanceHistoryItem> list) {
		super(context, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.adapter.AbstractGroupListAdapter#
	 * getSortComparator()
	 */
	@Override
	protected Comparator<MaintenanceHistoryItem> getSortComparator() {
		return new Comparator<MaintenanceHistoryItem>() {

			@Override
			public int compare(MaintenanceHistoryItem o1, MaintenanceHistoryItem o2) {
				long timeA = 0L;
				long timeB = 0L;
				String dateStr1 = o1.getDate();
				String dateStr2 = o2.getDate();
				timeA = DateUtil.getDate(dateStr1).getTime();
				timeB = DateUtil.getDate(dateStr2).getTime();
				if (timeA > timeB) {
					return -1;
				} else if (timeA < timeB) {
					return 1;
				}
				return 0;
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.adapter.AbstractGroupListAdapter#getTitleItem
	 * (android.view.LayoutInflater)
	 */
	@Override
	protected View getTitleItem(LayoutInflater mInflater, int position) {
		View convertView = mInflater.inflate(R.layout.ui_maintain_history_item_layout, null);
		TextView dateText = (TextView) convertView.findViewById(R.id.dateText);
		dateText.setText(this.curTitle);
		showCommonInfo(convertView, position);
		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.adapter.AbstractGroupListAdapter#getNoTitleItem
	 * (android.view.LayoutInflater)
	 */
	@Override
	protected View getNoTitleItem(LayoutInflater mInflater, int position) {
		View convertView = mInflater.inflate(R.layout.ui_maintain_history_item_layout, null);
		// set date line gone
		View dateTextLine = convertView.findViewById(R.id.dateLine);
		dateTextLine.setVisibility(View.GONE);
		showCommonInfo(convertView, position);
		return convertView;
	}

	private void showCommonInfo(View convertView, int position) {
		TextView dateOfMonthText = (TextView) convertView.findViewById(R.id.dateOfMonthText);
		dateOfMonthText.setText(String.format("%s", dayOfMonth));
		MaintenanceHistoryItem item = list.get(position);
		TextView maintainDateText = (TextView) convertView.findViewById(R.id.maintainDateText);
		TextView mainTainMileText = (TextView) convertView.findViewById(R.id.mainTainMileText);
		TextView mainTainContentText = (TextView) convertView.findViewById(R.id.mainTainContentText);
		// = item.getDate()
		maintainDateText.setText(String.format(context.getString(R.string.show_maintain_date), item.getDate()));
		mainTainMileText.setText(String.format(context.getString(R.string.show_maintain_mile), item.getMileage()));
		mainTainContentText
				.setText(String.format(context.getString(R.string.show_maintain_content), item.getContent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.adapter.AbstractGroupListAdapter#
	 * getDistinguishStr(int)
	 */
	@Override
	protected String getTitleStr(int position) {
		String dateStr = list.get(position).getDate();
		Date date;
		date = DateUtil.getDate(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int newItemMonth = calendar.get(Calendar.MONTH) + 1;
		int newItemYear = calendar.get(Calendar.YEAR);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		return String.format(context.getString(R.string.show_date), newItemYear, newItemMonth);
	}

}
