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
package com.hoperun.telematics.mobile.framework.net.async;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Messenger;
import android.util.Log;

import com.hoperun.telematics.mobile.framework.mq.IMessageCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.NotificationNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.helper.JsonHelper;
import com.hoperun.telematics.mobile.framework.net.http.DefaultHttpConnectionFactory;
import com.hoperun.telematics.mobile.framework.net.vo.NetRequest;
import com.hoperun.telematics.mobile.framework.net.vo.NetResponse;

/**
 * 
 * @author hu_wg
 * 
 */
public class DefaultNotificationHandler extends AbstractAsyncHandler implements INotificationHandler {

	public DefaultNotificationHandler(ENetworkServiceType serviceType, NetRequest request, Messenger replyTo, long delay,
			boolean isAvailable, Messenger savedReplyTo, INetCallback savedCallback) {
		super(serviceType, request, replyTo, delay, isAvailable, savedReplyTo, savedCallback);
	}

	@Override
	public void sendRequest() {

		try {
			NetResponse response = DefaultHttpConnectionFactory.getInstance().getConnection()
					.sendRequest(serviceType.getPath(), request);
			INetCallbackArgs args = new NotificationNetCallbackArgs(null, getCallbackStatus(response.getStatus()),
					response.getErrorMessage(), response.getInterval(), serviceType);
			doCallback(replyTo, args);

		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Send async request error!", e);
			INetCallbackArgs args = new NotificationNetCallbackArgs(null, ECallbackStatus.Failure, e.getMessage(), 0,
					serviceType);
			doCallback(replyTo, args);
			isAvailable = false;
		}
	}

	@Override
	public void getResult() {

		try {
			TimerTask task = new NotificationTimerTask();
			timer = new Timer();
			Log.d(this.getClass().getName(), String.format("GetResult Task Delay %s", this.delay));
			timer.schedule(task, delay);
		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Send async result request error!", e);

			INetCallbackArgs args = new NotificationNetCallbackArgs(null, ECallbackStatus.Failure, e.getMessage(), 0,
					serviceType);
			doCallback(replyTo, args);
			isAvailable = false;
		}

	}

	@Override
	public void cancel() {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	protected class NotificationTimerTask extends TimerTask {

		public NotificationTimerTask() {
		}

		@Override
		public void run() {

			try {
				if (isAvailable) {
					NetResponse response = DefaultHttpConnectionFactory.getInstance().getConnection()
							.sendRequest(serviceType.getPath(), JsonHelper.packageRequest(null));
					INetCallbackArgs args = new NotificationNetCallbackArgs(response.getPayload(),
							getCallbackStatus(response.getStatus()), response.getErrorMessage(),
							response.getInterval(), serviceType);
					doCallback(replyTo, args);
				} else {
					cancel();
				}

			} catch (Exception e) {
				Log.e(this.getClass().getName(), "Send async result request error!", e);

				INetCallbackArgs args = new NotificationNetCallbackArgs(null, ECallbackStatus.Failure, e.getMessage(),
						0, serviceType);
				doCallback(replyTo, args);
				isAvailable = false;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.mq.IMessageCallback#callback(
	 * com.hoperun.telematics.mobile.framework.mq.IMessageCallbackArgs)
	 */
	@Override
	public void callback(IMessageCallbackArgs args) {
		getResult();
	}
}
