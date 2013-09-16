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

import android.widget.ListView;

import com.hoperun.telematics.mobile.adapter.AbsHaveListAdapter;

/**
 * 内含列表型分页管理器，增加了获取新列表信息的方法
 * @author fan_leilei
 * 
 */
public abstract class AbstractListPagingManager<T> extends AbstractPagingManager{

	protected ListView listView;
	
	public AbstractListPagingManager(ListView listView){
		this.listView = listView;
	}
	
	/***
	 * initialize the list view 
	 * @param adapter
	 * @param newList
	 * @param maxIndex
	 */
	public void initListView(AbsHaveListAdapter<T> adapter,int maxIndex) {
		this.maxIndex = maxIndex;
		listView.setAdapter(adapter);
	}

}