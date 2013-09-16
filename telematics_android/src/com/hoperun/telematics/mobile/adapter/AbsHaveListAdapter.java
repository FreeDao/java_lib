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

import android.widget.BaseAdapter;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbsHaveListAdapter<T> extends BaseAdapter {

	/**
	 * @param historyList
	 */
	public abstract void setList(List<T> list) ;
	
	public abstract List<T> getList();
}
