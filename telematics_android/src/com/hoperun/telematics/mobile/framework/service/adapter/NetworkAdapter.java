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

import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.helper.DefineHelper;
import com.hoperun.telematics.mobile.framework.service.NetworkService.EMessageType;

/**
 * 
 * @author hu_wg
 * 
 */
public class NetworkAdapter {

	public NetworkAdapter() {

	}

	public void sendMessage(IBinder boundService, ENetworkServiceType serviceType, EMessageType messageType,
			String jsonString, INetCallback callback) {
		Bundle bundle = new Bundle();
		bundle.putString(DefineHelper.MESSAGE_KEY_PARAMS, jsonString);
		bundle.putSerializable(DefineHelper.MESSAGE_KEY_SERVICE_TYPE, serviceType);
		bundle.putSerializable(DefineHelper.MESSAGE_KEY_TYPE, messageType);
		sendMessage(boundService, bundle, callback);
	}

	public void sendMessage(IBinder boundService, Bundle bundle, INetCallback callback) {
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
			if (msg.obj != null && msg.obj instanceof INetCallback) {
				INetCallback callback = (INetCallback) msg.obj;
				INetCallbackArgs callbackArgs = (INetCallbackArgs) msg.getData().get(DefineHelper.MESSAGE_KEY_RESULTS);
				callback.callback(callbackArgs);
			}
			super.handleMessage(msg);
		}
	};

	private Messenger clientMessager = new Messenger(incomingHandler);
}
