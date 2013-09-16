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
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.paging.AbstractArrowBar;
import com.hoperun.telematics.mobile.model.states.VehicleState;

/**
 * 
 * @author fan_leilei
 * 
 */
public class VehicleStateDetailActivity extends DefaultActivity {

	private List<VehicleState> stateList;
	private TextView faultLevelText;
	private TextView faultPositionText;
	private TextView faultDescriptionText;
	private TextView suggestionText;
	private FrameLayout rootLayout;
	private TextView lincenseText;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_vehicle_state_detail_layout);
		stateList = this.getIntent().getExtras().getParcelableArrayList(VehicleStateActivity.STATE_LIST_FLAG);
		int curStateIndex = getIntent().getExtras().getInt(VehicleStateActivity.STATE_INDEX_IN_LIST_FLAG);
		if (stateList == null || curStateIndex == -1) {
			finish();
		}
		initViews();
		setTitleBar(this, getString(R.string.state_detail_title));
		// 滑动切屏
		// IPagingWidget widget = new AbsIndexBar(this, rootLayout,
		// curStateIndex + 1, stateList.size(),
		// R.layout.ui_fling_foot_layout) {
		//
		// @Override
		// public void updateWidget(int curIndex, int maxIndex) {
		// // update the index,just like "第1页/共3页"
		// updateIndexBar(String.format(getString(R.string.show_index),
		// curIndex, maxIndex));
		// }
		// };
		// AbsPagingManager pagesManager = new
		// AbsFlingPagingManager(curStateIndex + 1, stateList.size(),
		// rootLayout, widget) {
		//
		// @Override
		// public void updatePage() {
		// showDetailInfo(curIndex - 1);
		// }
		// };
		// pagesManager.updatePage();

		// 按动切屏
		new AbstractArrowBar(this, rootLayout, curStateIndex + 1, stateList.size(), R.layout.ui_arrow_text_foot_layout) {

			@Override
			protected void showContent(int curIndex) {
				showDetailInfo(curIndex - 1);
			}

			@Override
			public void updateWidget(int curIndex, int maxIndex) {
				updateIndexBar(String.format(getString(R.string.show_index), curIndex, maxIndex));
			}
		};
	}

	private void initViews() {
		faultLevelText = (TextView) findViewById(R.id.faultLevelText);
		faultPositionText = (TextView) findViewById(R.id.faultPositionText);
		faultDescriptionText = (TextView) findViewById(R.id.faultDescriptionText);
		suggestionText = (TextView) findViewById(R.id.suggestionText);
		rootLayout = (FrameLayout) findViewById(R.id.rootLayout);
		lincenseText = (TextView) findViewById(R.id.licenseText);
	}

	/**
	 * show Detail Information in UI
	 */
	private void showDetailInfo(int index) {
		VehicleState stateInfo = stateList.get(index);
		faultLevelText.setText(String.format(getString(R.string.show_fault_level), stateInfo.getFaultLevel()));
		faultPositionText.setText(String.format(getString(R.string.show_fault_position_description),
				stateInfo.getPositionName()));
		faultDescriptionText.setText(String.format(getString(R.string.show_fault_description),
				stateInfo.getFaultLevel()));
		suggestionText.setText(stateInfo.getSuggestion());
		String licenseStr = CacheManager.getInstance().getLicense();
		if (licenseStr != null) {
			lincenseText.setText(licenseStr);
		} else {
			lincenseText.setText(getString(R.string.show_license, getString(R.string.testLicense1)));
		}
	}

}
