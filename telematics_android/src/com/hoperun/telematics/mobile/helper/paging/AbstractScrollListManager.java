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

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.adapter.AbsHaveListAdapter;

/**
 * 滚动监听型分页管理器，当滚到底部的时候更新
 * @author fan_leilei
 * 
 */
public abstract class AbstractScrollListManager<T> extends AbstractListPagingManager<T>{
	
	private Context context;

	public AbstractScrollListManager( ListView listView) {
		super(listView);
		this.listView.setOnScrollListener(scrollListener);
	}

	/**
	 * 列表滚动监听
	 */
	private OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 当滑动到底部
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					// load more
					if (curIndex != maxIndex && !isLoadingMore) {
						curIndex++;
						isLoadingMore = true;
						getNewInfo(curIndex);
					} else {
						// when the last item is shown
						
					}
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	};

	public void updatePage(AbsHaveListAdapter<T> adapter, List<T> newList) {
		List<T> lastList = adapter.getList();
		lastList.addAll(newList);
		adapter.notifyDataSetChanged();
		isLoadingMore = false;
		Log.i("AbstractListScrollManager", curIndex + "/" + maxIndex);
	}
}
