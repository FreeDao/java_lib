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
import com.hoperun.telematics.mobile.model.maintenance.order.MaintenanceOrderItem;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintainOrderAdapter extends AbsGroupListAdapter<MaintenanceOrderItem> {

	private int dayOfMonth;

	/**
	 * Creates a new instance of MaintainHistoryAdapter.
	 */
	public MaintainOrderAdapter(Context context, List<MaintenanceOrderItem> list) {
		super(context, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.adapter.AbstractGroupListAdapter#
	 * getSortComparator()
	 */
	@Override
	protected Comparator<MaintenanceOrderItem> getSortComparator() {
		return new Comparator<MaintenanceOrderItem>() {

			@Override
			public int compare(MaintenanceOrderItem o1, MaintenanceOrderItem o2) {
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
		View convertView = mInflater.inflate(R.layout.ui_maintain_order_item_layout, null);
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
		View convertView = mInflater.inflate(R.layout.ui_maintain_order_item_layout, null);
		View dateTextLine = convertView.findViewById(R.id.dateLine);
		dateTextLine.setVisibility(View.GONE);
		showCommonInfo(convertView, position);
		
		return convertView;
	}

	private void showCommonInfo(View convertView, int position){
		TextView dateOfMonthText = (TextView) convertView.findViewById(R.id.dateOfMonthText);
		dateOfMonthText.setText(String.format("%s", dayOfMonth));
		MaintenanceOrderItem item = list.get(position);
		TextView mainTainAddressText = (TextView) convertView.findViewById(R.id.mainTainAddressText);
		mainTainAddressText.setText(item.getAddress());
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
