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
import android.widget.FrameLayout;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbstractPagingWidget {

	protected int curIndex;
	protected int maxIndex;
	
	public AbstractPagingWidget(Context context,FrameLayout layout,int curIndex,int maxIndex,int layoutId){
		this.curIndex = curIndex;
		this.maxIndex = maxIndex;
		initViews(context,layout,layoutId);
		initViewsValue();
	}
	/**
	 * update the widget，curIndex‘s minimum is 1
	 */
	public abstract void updateWidget(int curIndex,int maxIndex);
	protected abstract void initViews(Context context,FrameLayout layout,int layoutId);
	protected abstract void initViewsValue();
	/**
	 * 
	 */
	protected abstract void showContent(int curIndex);
}
