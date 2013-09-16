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
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hoperun.telematics.mobile.R;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbstractArrowBar extends AbstractIndexBar {
	
	protected TextView forwardBtn;
	protected TextView nextBtn;
	protected int layoutId = R.layout.ui_arrow_foot_layout;

	/**
	 * Creates a new instance of AbsArrowBar.
	 */
	public AbstractArrowBar(Context context, FrameLayout layout,int curIndex,int maxIndex,int layoutId) {
		super(context, layout,curIndex,maxIndex,layoutId);
		checkArrowButton(curIndex,maxIndex);
	}

	@Override
	protected void initViews(Context context,FrameLayout layout,int layoutId){
		super.initViews(context,layout,layoutId);
		forwardBtn = (TextView)indexBar.findViewById(R.id.forwardBtn);
		forwardBtn.setOnClickListener(forwardBtnLis);
		nextBtn = (TextView) indexBar.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(nextBtnLis);
	}
	
	private Button.OnClickListener forwardBtnLis = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(curIndex>1){
				curIndex -- ;
				checkArrowButton(curIndex,maxIndex);
				updateWidget(curIndex,maxIndex);
				showContent(curIndex);
			}
		}
	};

	private Button.OnClickListener nextBtnLis = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(curIndex<maxIndex){
				curIndex ++ ;
				checkArrowButton(curIndex,maxIndex);
				updateWidget(curIndex,maxIndex);
				showContent(curIndex);
			}
		}
	};

	/**
	 * 检查当前页码，设置按钮的显示状态
	 * @param curIndex
	 * @param maxIndex
	 */
	protected void checkArrowButton(int curIndex,int maxIndex){
		if (curIndex == 1) {
			forwardBtn.setVisibility(View.INVISIBLE);
		} else {
			forwardBtn.setVisibility(View.VISIBLE);
		}
		if (curIndex == maxIndex ) {
			nextBtn.setVisibility(View.INVISIBLE);
		} else {
			nextBtn.setVisibility(View.VISIBLE);
		}
	}
}
