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

import com.hoperun.telematics.mobile.framework.mq.IMessageConsumer;
import com.hoperun.telematics.mobile.framework.mq.IMessageProducer;
import com.hoperun.telematics.mobile.framework.mq.IMessageQueue;
import com.hoperun.telematics.mobile.framework.mq.IMessageQueueManager;

/**
 * 
 * @author hu_wg
 * 
 */
public class DefaultMessageQueueManager<E> implements IMessageQueueManager<E> {

	private IMessageQueue<E> queue;
	private IMessageProducer producer;
	private IMessageConsumer consumer;

	@Override
	public void init(IMessageQueue<E> queue, IMessageProducer producer, IMessageConsumer consumer) {
		this.queue = queue;
		this.producer = producer;
		this.consumer = consumer;
		new Thread(consumer).start();
	}

	
}
