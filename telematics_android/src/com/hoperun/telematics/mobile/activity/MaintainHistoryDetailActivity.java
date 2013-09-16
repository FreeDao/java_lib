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
package com.hoperun.telematics.mobile.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.paging.AbstractPagingManager;
import com.hoperun.telematics.mobile.helper.paging.AbstractFlingPagingBar;
import com.hoperun.telematics.mobile.model.maintenance.history.MaintenanceHistoryItem;

/**
 * 
 * @author fan_leilei
 * 
 */
public class MaintainHistoryDetailActivity extends DefaultActivity {

	private List<MaintenanceHistoryItem> historyList;
	private AbstractPagingManager pagesManager;
	private TextView maintainDateText;
	private TextView maintainMileText;
	private TextView maintainContentText;
	private TextView maintainAddressText;
	private TextView maintainNoteText;
	private FrameLayout rootLayout;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_maintain_history_detail_layout);
		historyList = this.getIntent().getExtras().getParcelableArrayList(MaintenanceHistoryActivity.HISTORY_LIST_FLAG);
		final int curStateIndex = getIntent().getExtras().getInt(MaintenanceHistoryActivity.HISTORY_INDEX_IN_LIST_FLAG);
		initViews();
		setTitleBar(this, getString(R.string.detail_title));
		new AbstractFlingPagingBar(this, rootLayout, curStateIndex + 1, historyList.size(),
				R.layout.ui_fling_foot_layout, rootLayout) {

			@Override
			public void updateWidget(int curIndex, int maxIndex) {
				// update the index,just like "第1页/共3页"
				updateIndexBar(String.format(getString(R.string.show_index), curIndex, maxIndex));
			}

			@Override
			protected void showContent(int curIndex) {
				showDetailInfo(curIndex - 1);
			}
		};
	}

	private void initViews() {
		maintainDateText = (TextView) findViewById(R.id.maintainDateText);
		maintainMileText = (TextView) findViewById(R.id.maintainMileText);
		maintainContentText = (TextView) findViewById(R.id.maintainContentText);
		maintainAddressText = (TextView) findViewById(R.id.maintainAddressText);
		maintainNoteText = (TextView) findViewById(R.id.maintainNoteText);
		rootLayout = (FrameLayout) findViewById(R.id.rootLayout);
	}

	/**
	 * show Detail Information in UI
	 */
	private void showDetailInfo(int index) {
		MaintenanceHistoryItem historyInfo = historyList.get(index);
		maintainDateText.setText(String.format(getString(R.string.maintain_time), historyInfo.getDate()));
		maintainMileText.setText(String.format(getString(R.string.maintain_mile), historyInfo.getMileage()));
		maintainContentText.setText(String.format(getString(R.string.maintain_content), historyInfo.getContent()));
		maintainAddressText.setText(historyInfo.getAddress());
		maintainNoteText.setText(historyInfo.getNote());
	}

}
