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
import com.hoperun.telematics.mobile.framework.mq.IMessageCallbackArgs;
import com.hoperun.telematics.mobile.framework.mq.IMessageConsumer;
import com.hoperun.telematics.mobile.framework.mq.IMessageQueue;

/**
 * 
 * @author hu_wg
 * 
 */
public class DefaultConsumer implements IMessageConsumer {

	private IMessageQueue<IMessageCallback> queue;

	public DefaultConsumer(IMessageQueue<IMessageCallback> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			IMessageCallback handler = null;
			synchronized (queue) {

				Log.d(this.getClass().getName(), String.format("poll before size (%s)", queue.size()));
				handler = queue.poll();
				Log.d(this.getClass().getName(), String.format("poll after size (%s)", queue.size()));
				if (handler == null) {
					try {
						Log.d(this.getClass().getName(), String.format("wait before size (%s)", queue.size()));
						queue.wait();
						Log.d(this.getClass().getName(), String.format("wait after size (%s)", queue.size()));
						continue;
					} catch (InterruptedException ex) {

					}
				}
			}
			if (handler != null) {
				IMessageCallbackArgs args = null;
				handler.callback(args);
			}
		}
	}

}