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

import com.hoperun.telematics.mobile.helper.Constants;

/**
 * 分页管理器
 * @author fan_leilei
 * 
 */
public abstract class AbstractPagingManager {
	
	/**
	 * 是否正在加载新列表
	 */
	protected boolean isLoadingMore = false;
	
	/**
	 * minimum index is 1,maxIndex = maxNum%perNum == 0? maxNum/perNum :
	 * maxNum/perNum+1
	 */
	protected int maxIndex;
	/**
	 * 页码，最小为1
	 */
	protected int curIndex = 1;

	public AbstractPagingManager(){
		
	}
	
	/**
	 * 更新页面内容
	 * @param adapter
	 * @param newList
	 */
	public abstract void updatePage();
	
	protected abstract void onShownLastPage();
	
	public int getCurIndex(){
		return curIndex;
	}
	
	/**
	 * 发送页面更新请求
	 * @param index
	 */
	protected abstract void getNewInfo(int index);
	
	/**
	 * 进行第一次请求
	 */
	/**
	 * start the first loading 
	 */
	public void startFirstLoading(){
		isLoadingMore = true;
		curIndex = Constants.MIN_INDEX_OF_LIST_IN_PAGE;
		getNewInfo(curIndex);
	}
}
