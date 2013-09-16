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
package com.hoperun.telematics.mobile.framework.mq.impl;

import android.util.Log;

import com.hoperun.telematics.mobile.framework.mq.IMessageCallback;
import com.hoperun.telematics.mobile.framework.mq.IMessageProducer;
import com.hoperun.telematics.mobile.framework.mq.IMessageQueue;

/**
 * 
 * @author hu_wg
 * 
 */
public class DefaultProducer implements IMessageProducer {

	private IMessageQueue<IMessageCallback> queue;

	public DefaultProducer(IMessageQueue<IMessageCallback> queue) {
		this.queue = queue;
	}

	@Override
	public void offer(IMessageCallback handler) {
		synchronized (queue) {
			queue.offer(handler);
			queue.notifyAll();
			Log.d(this.getClass().getName(), String.format("offer size (%s)", queue.size()));
		}
	}

}