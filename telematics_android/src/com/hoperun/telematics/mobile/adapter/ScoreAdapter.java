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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.model.score.Score;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ScoreAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Score> mList;
	
	/**
     * Creates a new instance of ScoreAdapter.
     */
    public ScoreAdapter(Context context, List<Score> list) {
	    super();
	    this.mContext = context;
	    this.mList = list;
	    Log.i(this.getClass().getName(), "mList size is " + mList.size());
		mInflater = LayoutInflater.from(context);
    }

	/* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
	    return mList.size();
    }

	/* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
	    return mList.get(position);
    }

	/* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
	    return position;
    }

	/* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(convertView == null) {
    		convertView = mInflater.inflate(R.layout.ui_score_item_layout, null);
    	}
    	TextView numberTextView = (TextView) convertView.findViewById(R.id.number);
    	TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
    	TextView scoreTextView = (TextView) convertView.findViewById(R.id.score);
    	
    	numberTextView.setText(mList.get(position).getId());
    	nameTextView.setText(mList.get(position).getName());
    	scoreTextView.setText(String.format("%s", mList.get(position).getCost()));
	    return convertView;
    }

}
