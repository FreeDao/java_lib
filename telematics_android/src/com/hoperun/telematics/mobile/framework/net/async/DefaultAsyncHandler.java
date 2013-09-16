package com.hoperun.telematics.mobile.framework.net.async;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Messenger;
import android.util.Log;

import com.hoperun.telematics.mobile.framework.mq.IMessageCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.AsyncNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.AsyncResultNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.helper.JsonHelper;
import com.hoperun.telematics.mobile.framework.net.http.DefaultHttpConnectionFactory;
import com.hoperun.telematics.mobile.framework.net.vo.NetRequest;
import com.hoperun.telematics.mobile.framework.net.vo.NetResponse;

public class DefaultAsyncHandler extends AbstractAsyncHandler implements IAsyncHandler {

	public DefaultAsyncHandler(ENetworkServiceType serviceType, NetRequest request, Messenger replyTo, long delay,
			boolean isAvailable, Messenger savedReplyTo, INetCallback savedCallback) {
		super(serviceType, request, replyTo, delay, isAvailable, savedReplyTo, savedCallback);
	}

	@Override
	public void sendRequest() {
		try {
			NetResponse response = DefaultHttpConnectionFactory.getInstance().getConnection()
					.sendRequest(serviceType.getPath(), request);
			INetCallbackArgs args = new AsyncNetCallbackArgs(null, getCallbackStatus(response.getStatus()),
					response.getErrorMessage(), response.getInterval(), response.getResId(), serviceType);
			doCallback(replyTo, args);

		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Send async request error!", e);
			INetCallbackArgs args = new AsyncNetCallbackArgs(null, ECallbackStatus.Failure, e.getMessage(), 0, null,
					serviceType);
			doCallback(replyTo, args);
			isAvailable = false;
		}
	}

	@Override
	public void getResult() {

		try {
			TimerTask task = new AsyncTimerTask(resId);
			timer = new Timer();
			timer.schedule(task, delay);
		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Send async result request error!", e);

			INetCallbackArgs args = new AsyncResultNetCallbackArgs(null, ECallbackStatus.Failure, e.getMessage(), 0,
					null, serviceType);
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

	protected class AsyncTimerTask extends TimerTask {

		private String resId;

		public AsyncTimerTask(String resId) {
			this.resId = resId;
		}

		@Override
		public void run() {

			try {
				if (isAvailable) {
					NetResponse response = DefaultHttpConnectionFactory.getInstance().getConnection()
							.sendRequest(serviceType.getResultPath(), JsonHelper.packageResultRequest(resId));
					INetCallbackArgs args = new AsyncResultNetCallbackArgs(response.getPayload(),
							getCallbackStatus(response.getStatus()), response.getErrorMessage(),
							response.getInterval(), resId, serviceType);
					doCallback(replyTo, args);
				} else {
					cancel();
				}

			} catch (Exception e) {
				Log.e(this.getClass().getName(), "Send async result request error!", e);

				INetCallbackArgs args = new AsyncResultNetCallbackArgs(null, ECallbackStatus.Failure, e.getMessage(),
						0, null, serviceType);
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
		if (resId == null) {
			sendRequest();
		} else {
			getResult();
		}
	}
}
