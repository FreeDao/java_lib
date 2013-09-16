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

/**
 * 
 * @author he_chen
 * 
 */
public final class LogUtil {
	public static int LOG_LEVEL = 5;

	private LogUtil() {

	}

	public static void v(String tag, String msg) {
		if (LOG_LEVEL >= 5) {
			android.util.Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (LOG_LEVEL >= 5) {
			android.util.Log.v(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL >= 4) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (LOG_LEVEL >= 4) {
			android.util.Log.d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL >= 3) {
			android.util.Log.i(tag, msg);
		}
	}

	static void i(String tag, String msg, Throwable tr) {
		if (LOG_LEVEL >= 3) {
			android.util.Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL >= 2) {
			android.util.Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (LOG_LEVEL >= 2) {
			android.util.Log.w(tag, msg, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL >= 1) {
			android.util.Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (LOG_LEVEL >= 1) {
			android.util.Log.e(tag, msg, tr);
		}
	}
}
