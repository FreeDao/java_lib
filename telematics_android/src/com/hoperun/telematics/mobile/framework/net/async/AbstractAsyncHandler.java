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
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;
import com.hoperun.telematics.mobile.framework.net.helper.DefineHelper;
import com.hoperun.telematics.mobile.framework.net.vo.NetRequest;

/**
 * 
 * @author hu_wg
 * 
 */
public abstract class AbstractAsyncHandler implements IAsyncHandler {

	protected long delay;
	protected boolean isAvailable;
	protected Timer timer;
	protected Messenger replyTo;
	protected NetRequest request;
	protected ENetworkServiceType serviceType;
	protected INetCallback savedCallback;
	protected Messenger savedReplyTo;
	protected String resId;

	public AbstractAsyncHandler(ENetworkServiceType serviceType, NetRequest request, Messenger replyTo, long delay,
			boolean isAvailable, Messenger savedReplyTo, INetCallback savedCallback) {
		super();
		this.serviceType = serviceType;
		this.request = request;
		this.replyTo = replyTo;
		this.delay = delay;
		this.isAvailable = isAvailable;
		this.savedReplyTo = savedReplyTo;
		this.savedCallback = savedCallback;
	}

	@Override
	public void updateDelay(long delay) {
		this.delay = delay;
		Log.d(this.getClass().getName(), String.format("Update Delay %s", this.delay));
	}

	@Override
	public boolean isAvailable() {
		return isAvailable;
	}

	@Override
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public INetCallback getSavedCallback() {
		return this.savedCallback;
	}

	@Override
	public Messenger getSavedReplyTo() {
		return this.savedReplyTo;
	}

	@Override
	public void setResId(String resId) {
		this.resId = resId;
	}

	protected void doCallback(Messenger replyTo, INetCallbackArgs args) {
		try {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putSerializable(DefineHelper.MESSAGE_KEY_NET_CALLBACK_RESULTS, args);
			msg.setData(bundle);
			replyTo.send(msg);
		} catch (RemoteException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	protected ECallbackStatus getCallbackStatus(int status) {
		if (status == ECallbackStatus.Success.getValue()) {
			return ECallbackStatus.Success;
		} else if (status == ECallbackStatus.Processing.getValue()) {
			return ECallbackStatus.Processing;
		} else {
			return ECallbackStatus.Failure;
		}
	}

}
