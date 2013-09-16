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

import java.util.LinkedList;
import java.util.Queue;

import com.hoperun.telematics.mobile.framework.mq.IMessageQueue;

/**
 * 
 * @author hu_wg
 * 
 */
public class DefaultMessageQueue<E> implements IMessageQueue<E> {

	private Queue<E> queue = new LinkedList<E>();
	private String name;

	@Override
	public boolean offer(E e) {

		return queue.offer(e);
	}

	@Override
	public E poll() {
		return queue.poll();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
