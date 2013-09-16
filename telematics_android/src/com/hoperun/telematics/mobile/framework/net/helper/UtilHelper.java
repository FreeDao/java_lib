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
package com.hoperun.telematics.mobile.framework.net.helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amap.mapapi.core.GeoPoint;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.helper.DialogHelper;

/**
 * 
 * @author he_chen
 * 
 */
public class UtilHelper {

	/**
	 * intTOChar
	 * 
	 * 0 == A, 1 == B ...
	 * 
	 * @param param
	 * @return String
	 */
	public static String intTOChar(int param) {
		if (param < 0) {
			return "";
		}
		return String.valueOf((char) (param + 65));
	}

	/**
	 * new geopoint
	 * 
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static GeoPoint NewGeoPoint(double lat, double lng) {
		return new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
	}

	/**
	 * showNotification
	 * 
	 * @param context
	 * @param clas
	 * @param alter
	 * @param title
	 * @param message
	 */
	public static void showNotification(Context context, Class<? extends Activity> clas, String alter, String title,
			String message) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.icon = R.drawable.icon;
		notification.tickerText = alter;

		Intent intent = new Intent(context, clas);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, message, contentIntent);

		nm.notify(R.string.app_name, notification);
	}

	/**
	 * haveInternet
	 * 
	 * @param context
	 * @return
	 */
	public static boolean haveInternet(Context context) {
		NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (null == info || !info.isConnected()) {
			return false;
		}
		return true;
	}

	/**
	 * checkNetWork
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetWorkAlert(Context context) {
		if (!haveInternet(context)) {
			DialogHelper.alertDialog(context, R.string.warning, R.string.network_fail, R.string.ok);
			return false;
		}
		return true;
	}
}
