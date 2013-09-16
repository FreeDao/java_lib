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
package com.hoperun.telematics.mobile.helper;

import java.util.Comparator;

/**
 * 
 * @author wang_xiaohua
 * 
 */
public class PinyinComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		String str1 = PinyinSortHelper.getPingYin((String) o1);
		String str2 = PinyinSortHelper.getPingYin((String) o2);
		return str1.compareTo(str2);
	}

}
