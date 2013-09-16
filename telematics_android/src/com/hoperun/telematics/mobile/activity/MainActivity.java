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

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.hoperun.telematics.mobile.activity.LoginActivity;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.NavigationAdapter;
import com.hoperun.telematics.mobile.helper.Constants;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.LogUtil;
import com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback;

/**
 * 
 * @author he_chen
 * 
 */

public class MainActivity extends DefaultActivity implements OnClickListener, DialogCallback {

	private static String TAG = "MainActivity";

	private GridView mGridView;
	private Button mLogOutBtn;

	private NavigationAdapter mNavigationAdapter;

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_main_navigation_layout);

		super.setTitleBar(this, getResources().getString(R.string.navigation_titleBar_text));
		super.mMainBtn.setVisibility(View.INVISIBLE);
		super.mLineImageView.setVisibility(View.INVISIBLE);

		initWidget();

		fullGridView();
	}

	/**
	 * initWidget
	 */
	private void initWidget() {
		mGridView = (GridView) findViewById(R.id.navigation_gridview_id);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mLogOutBtn = (Button) findViewById(R.id.logoutbtn);
		mLogOutBtn.setOnClickListener(this);
	}

	private void fullGridView() {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 12; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("navigation_image_id", R.drawable.navigation_item_png_10 + i);
			map.put("navigation_text_id", getResources().getString(R.string.navigation_item_text_10 + i));

			list.add(map);
		}

		mNavigationAdapter = new NavigationAdapter(this, list, Constants.NAVIGATION_FLAG);
		mGridView.setAdapter(mNavigationAdapter);
		mGridView.setOnItemClickListener(new ItemClickListener());
	}

	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0,

		View arg1, int position, long arg3) {

			switch (position) {
			case 0:
				// Intent intent0 = new Intent(MainActivity.this, RoadRescueActivity.class);
				// intent0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent0);
				break;
			case 1:
				Intent intent1 = new Intent(MainActivity.this, RemoteControlActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent1);
				break;
			case 2:
				Intent intent2 = new Intent(MainActivity.this, VehicleStateActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent2);
				break;
			case 3:
				Intent intent3 = new Intent(MainActivity.this, PoiSearchActivity.class);
				intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent3);
				break;
			case 4:
				 Intent intent4 = new Intent(MainActivity.this, LocationActivity.class);
				 intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent4);
				break;
			case 5:
				 Intent intent5 = new Intent(MainActivity.this, TrackActivity.class);
				 intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent5);
				break;
			case 6:
				Intent intent6 = new Intent(MainActivity.this, FuelStateActivity.class);
				intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent6);
				break;
			case 7:
				Intent intent7 = new Intent(MainActivity.this, ViolationActivity.class);
				intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent7);
				break;
			case 8:
				Intent intent8 = new Intent(MainActivity.this, MaintenanceHistoryActivity.class);
				intent8.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent8);
				break;
			case 9:
				Intent intent9 = new Intent(MainActivity.this, WeatherActivity.class);
				intent9.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent9);
				break;
			case 10:
				 Intent intent10 = new Intent(MainActivity.this, FriendActivity.class);
				 intent10.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent10);
				break;
			case 11:
				Intent intent11 = new Intent(MainActivity.this, ScoreActivity.class);
				intent11.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent11);
				break;
			default:

				break;
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutbtn:
			// Toast.makeText(MainActivity.this, "logout btn",
			// Toast.LENGTH_SHORT).show();

			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			// TODO he_chen stop service
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// System.exit(0);

			DialogHelper.alertDialog(MainActivity.this, R.string.is_exit_system, R.string.cancel, R.string.ok,
					MainActivity.this);
			LogUtil.i(TAG, "keycode back.");
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback#onCancle ()
	 */
	@Override
	public void onCancle() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.helper.DialogHelper.DialogCallback#onOk()
	 */
	@Override
	public void onOk() {
		CustomApplication.getInstance().exit();
	}
}
