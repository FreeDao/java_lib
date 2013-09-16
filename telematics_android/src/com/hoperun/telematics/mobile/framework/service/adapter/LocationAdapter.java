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
package com.hoperun.telematics.mobile.framework.service.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.helper.DefineHelper;
import com.hoperun.telematics.mobile.framework.service.LocationService.ELocationServiceType;

/**
 * 
 * @author hu_wg
 * 
 */
public class LocationAdapter {

	public LocationAdapter() {

	}

	public void sendMessage(IBinder boundService, ELocationServiceType serviceType, ILocationListener callback) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(DefineHelper.MESSAGE_KEY_SERVICE_TYPE, serviceType);
		sendMessage(boundService, bundle, callback);
	}

	public void sendMessage(IBinder boundService, Bundle bundle, ILocationListener callback) {
		try {
			Message msg = new Message();
			msg.setData(bundle);
			msg.replyTo = clientMessager;
			if (callback != null) {
				msg.obj = callback;
			}
			new Messenger(boundService).send(msg);
		} catch (RemoteException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	private Handler incomingHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null && msg.obj instanceof ILocationListener) {
				ILocationListener callback = (ILocationListener) msg.obj;
				ILocationEventArgs callbackArgs = (ILocationEventArgs) msg.getData().get(
						DefineHelper.MESSAGE_KEY_RESULTS);
				callback.callback(callbackArgs);
			}
			super.handleMessage(msg);
		}
	};

	private Messenger clientMessager = new Messenger(incomingHandler);
}
