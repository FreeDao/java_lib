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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

/**
 * 
 * @author fan_leilei
 * 
 */
public class DateUtil {

	private static final String TAG = DateUtil.class.getName();

	public static Date getDate(String strDate)  {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return date;
	}

	/**
	 * get the Chinese format time
	 * 
	 * @param date
	 * @return
	 */
	public static String getZhDate(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("MM月dd日");
		return f.format(date);
	}

	/**
	 * get the common format time
	 * 
	 * @param date
	 * @return
	 */
	public static String getCommonFormat(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(date);
	}

	/**
	 * get the Chinese format time
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static String getZhDate(String strDate) {
		Date date = getDate(strDate);
		if(date == null){
			return "";
		}else{
			return getZhDate(date);
		}
		
	}

	/**
	 * get the Chinese format time
	 * 
	 * @param date
	 * @return
	 */
	public static String getZhTime(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
		return f.format(date);
	}

	/**
	 * According your parameters, return the date.
	 * 
	 * @param year
	 * 
	 * @param month
	 *            based on zero, zero means January
	 * @param day
	 *            day of month
	 * @return
	 */
	public static Date getDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.getTime();
	}
}
