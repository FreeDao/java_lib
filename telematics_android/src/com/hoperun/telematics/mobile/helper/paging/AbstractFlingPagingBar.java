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
package com.hoperun.telematics.mobile.helper.paging;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

/**
 * 
 * @author fan_leilei
 * 
 */
public abstract class AbstractFlingPagingBar extends AbstractIndexBar implements OnTouchListener, OnGestureListener{

	private static final int FLING_MIN_DISTANCE = 120;// 移动最小距离
	private static final int FLING_MIN_VELOCITY = 200;// 移动最小速度
	
	// 构建手势探测器
	private GestureDetector gesture = new GestureDetector(this);
	/**
	 * Creates a new instance of AbstractFlingPagingBar.
	 */
	public AbstractFlingPagingBar(Context context, FrameLayout layout, int curIndex, int maxIndex, int layoutId,View flingView) {
		super(context, layout, curIndex, maxIndex, layoutId);
		flingView.setLongClickable(true);
		flingView.setOnTouchListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.
	 * MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			showNext();
		}
		// 显示上一个
		if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			showLast();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gesture.onTouchEvent(event);
	}
	
	private void showNext() {
		if (curIndex == maxIndex ) {
			return;
		}
		curIndex++;
		updateWidget(curIndex,maxIndex);
		showContent(curIndex);
	}

	private void showLast() {
		if (curIndex == 1) {
			return;
		}
		curIndex--;
		updateWidget(curIndex,maxIndex);
		showContent(curIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.
	 * MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.GestureDetector.OnGestureListener#onShowPress(android.view
	 * .MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.
	 * view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.GestureDetector.OnGestureListener#onScroll(android.view.
	 * MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.GestureDetector.OnGestureListener#onLongPress(android.view
	 * .MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
	}

}
