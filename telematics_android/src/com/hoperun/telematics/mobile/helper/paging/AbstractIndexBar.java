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
package com.hoperun.telematics.mobile.helper.paging;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbstractIndexBar extends AbstractPagingWidget {

	protected TextView indexText;
	protected View indexBar;
	protected int layoutId;

	public AbstractIndexBar(Context context, FrameLayout layout, int curIndex, int maxIndex, int layoutId) {
		super(context, layout, curIndex, maxIndex, layoutId);
	}

	protected void updateIndexBar(String text) {
		indexText.setText(text);
	}

	// R.layout.ui_fling_foot_layout
	@Override
	protected void initViews(Context context, FrameLayout layout, int layoutId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
		indexBar = inflater.inflate(layoutId, null);
		indexBar.setLayoutParams(params);
		indexText = (TextView) indexBar.findViewById(R.id.indexText);
		layout.addView(indexBar);
	}

	@Override
	protected void initViewsValue() {
		updateWidget(curIndex, maxIndex);
		showContent(curIndex);
	}
}
