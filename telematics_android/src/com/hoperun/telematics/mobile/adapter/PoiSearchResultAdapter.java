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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.model.poi.Poi;

/**
 * 
 * @author he_chen
 * 
 */
public class PoiSearchResultAdapter extends BaseAdapter {

	private Context mContext;
	private List<Poi> mList;

	public PoiSearchResultAdapter(Context context, List<Poi> list) {
		this.mContext = context;
		this.mList = list;

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
		View layout = inflater.inflate(R.layout.ui_poi_list_item_layout, null);

		TextView poiSequenceChar = (TextView) layout.findViewById(R.id.poi_sequence_char);
		TextView poiItemName = (TextView) layout.findViewById(R.id.poi_item_name);
		TextView poiItemAddress = (TextView) layout.findViewById(R.id.poi_item_address);
		TextView poiDistance = (TextView) layout.findViewById(R.id.poi_distance);

		Poi poi = mList.get(position);
		poiSequenceChar.setText(String.valueOf((char) (position + 65)));
		poiItemName.setText(poi.getName());
		poiItemAddress.setText(poi.getAddress());
		poiDistance.setText(String.valueOf(poi.getExtInfo().get("distance"))); // TODO hechen

		return layout;
	}

	/**
	 * @return the mList
	 */
	public List<Poi> getmList() {
		return mList;
	}

}
