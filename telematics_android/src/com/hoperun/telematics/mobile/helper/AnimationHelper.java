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
package com.hoperun.telematics.mobile.helper;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * 
 * @author fan_leilei
 * 
 */
public class AnimationHelper {

	public static void setFlickerAnimation(View v) {
		final Animation animation = new AlphaAnimation(1, 0); // Change alpha
		animation.setDuration(500); // duration - half a second
		animation.setInterpolator(new LinearInterpolator()); // do not alter
		                                                     // animation
		                                                     // rate
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation
		                                              // infinitely
		animation.setRepeatMode(Animation.REVERSE); //
		v.setAnimation(animation);
	}

	public static void startOnceRotateAnimation(View v, float from, float to, long duration,
	        Animation.AnimationListener listener) {
		RotateAnimation animation = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		// 设置动画执行过程用的时间,单位毫秒
		animation.setDuration(duration);
		animation.setRepeatCount(0); // Repeat animation
		animation.setFillAfter(true);
		animation.setAnimationListener(listener);
		// animation.setRepeatMode(Animation.ABSOLUTE); //
		// 将动画加入动画集合中
		v.startAnimation(animation);
		// animation.setInterpolator(new LinearInterpolator()); // do not alter
	}

	public static void startOnceRotateAnimation(View v, float from, float to, long duration) {
		RotateAnimation animation = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		// 设置动画执行过程用的时间,单位毫秒
		animation.setDuration(duration);
		animation.setRepeatCount(0); // Repeat animation
		animation.setFillAfter(true);
		LinearInterpolator lir = new LinearInterpolator();
		animation.setInterpolator(lir);
		// animation.setRepeatMode(Animation.ABSOLUTE); //
		// 将动画加入动画集合中
		v.startAnimation(animation);
	}

	public static void startRepeatSelfRotateAnimation(View v, long duration, Animation.AnimationListener listener) {
		RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(duration);
		animation.setRepeatCount(-1); // Repeat animation
		animation.setFillAfter(true);
		animation.setRepeatMode(Animation.INFINITE); //
		LinearInterpolator lir = new LinearInterpolator();
		animation.setInterpolator(lir);
		if (listener != null) {
			animation.setAnimationListener(listener);
		}
		v.startAnimation(animation);
	}
}
