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
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.Constants;

/**
 * 
 * @author he_chen
 * 
 */
public class NavigationAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private String flag;

	public NavigationAdapter(Context context, ArrayList<HashMap<String, Object>> list, String flag) {
		this.mContext = context;
		this.mList = list;
		this.flag = flag;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return (null != mList ? mList.size() : 0);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return (null != mList ? mList.get(position) : null);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ui_main_navigation_item, null);
		// RelativeLayout gridItem = (RelativeLayout) layout
		// .findViewById(R.id.navigation_item_layout_id);

		// gridItem.setBackgroundDrawable(mContext.getResources().getDrawable(
		// R.drawable.navigation_item_png_10 + position));

		ImageView itemImage = (ImageView) layout.findViewById(R.id.navigation_image_id);
		TextView itemText = (TextView) layout.findViewById(R.id.navigation_text_id);

		if (flag.equals(Constants.POI_FLAG)) {
			itemImage.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.poi_item_png_10 + position));
			itemText.setText(mContext.getResources().getString(R.string.poi_item_text_10 + position));
		} else {
			itemImage.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.navigation_item_png_10 + position));
			itemText.setText(mContext.getResources().getString(R.string.navigation_item_text_10 + position));
		}

		return layout;
	}

}
