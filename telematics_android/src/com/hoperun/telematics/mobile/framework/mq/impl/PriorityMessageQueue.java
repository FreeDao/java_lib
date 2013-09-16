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

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 
 * @author hu_wg
 * 
 */
@Deprecated
public class PriorityMessageQueue<E> {// implements IMessageQueue<E> {

	private Queue<E> queue = null;

	public PriorityMessageQueue() {
		queue = new PriorityQueue<E>();
	}

	public PriorityMessageQueue(Comparator<E> comparator) {
		queue = new PriorityQueue<E>(11, comparator);
	}

//	@Override
//	public boolean offer(E e) {
//		return queue.offer(e);
//	}
//
//	@Override
//	public E poll() {
//		return queue.poll();
//	}
//
//	@Override
//	public int size() {
//		return queue.size();
//	}

}
