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
package com.hoperun.telematics.mobile.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 
 * @author chen_guigui
 * 
 */
public class ShaderView extends View implements Runnable {
	private static final String TAG = "ShaderView";
	private static final int MAX_TIME = 18;
	private static final float STEP_DISTANCE = 30f;
	private static final int DEFAULT_TEXTSIZE = 32;
	private static final int ANIMATION_COMPLETE = 1;
	private static final int DEFAULT_DELAY_TIME_PER_STEP = 200; // millisecond
	// ------ judgement of post invalidate
	public static final int POST_INVALIDATE_JUDGEMENT_TIMES = 1;
	public static final int POST_INVALIDATE_JUDGEMENT_FLAG = 2;
	// used to constructed shader
	public static final int[] DEFAULT_SHADER_COLORS = new int[] { Color.argb(255, 120, 120, 120),
	        Color.argb(255, 120, 120, 120), Color.WHITE };
	public static final float[] DEFAULT_SHADER_COLOR_POSITIONS = new float[] { 0f, 0.7f, 1f };
	// ------
	private float mDistance = 0f;
	private Paint mPaint = new Paint();
	private Shader mShader;
	private Matrix mMatrix = new Matrix();
	private boolean mDrawFlag = true;
	private String mContentString;
	private AnimationComplete mAnimationComplete;
	private int mTime = 0;
	private int mJudgement = POST_INVALIDATE_JUDGEMENT_TIMES;

	/**
	 * Creates a new instance of OpeningAnimationView.
	 */
	public ShaderView(Context context) {
		this(context, null, 0);
	}

	/**
	 * Creates a new instance of OpeningAnimationView.
	 */
	public ShaderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Creates a new instance of OpeningAnimationView.
	 */
	public ShaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(DEFAULT_TEXTSIZE);
		mShader = new LinearGradient(0, 0, 200, 0, DEFAULT_SHADER_COLORS, DEFAULT_SHADER_COLOR_POSITIONS,
		        TileMode.MIRROR);
		mPaint.setShader(mShader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		Rect rect = new Rect();
		Log.i(TAG, "width is " + width + " height is " + height);
		if (mContentString != null && mContentString.length() > 0) {
			float[] textWidths = new float[mContentString.length()];
			mPaint.getTextWidths(mContentString, textWidths);
			mPaint.getTextBounds(mContentString, 0, mContentString.length() - 1, rect);
			float sumWidth = 0f;
			for (int i = 0; i < textWidths.length; i++) {
				sumWidth += textWidths[i];
			}
			canvas.drawText(mContentString, (width - sumWidth) / 2, (height + rect.height()) / 2, mPaint);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (continuePostInvalidate()) {
			try {
				Thread.sleep(DEFAULT_DELAY_TIME_PER_STEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mDistance += STEP_DISTANCE;
			mMatrix.setTranslate(mDistance, 0);
			mShader.setLocalMatrix(mMatrix);
			mPaint.setShader(mShader);
			postInvalidate();
		}
		Message msg = mHandler.obtainMessage();
		msg.what = ANIMATION_COMPLETE;
		mHandler.sendMessage(msg);
	}

	/**
	 * Whether continue post invalidate according to mJudgement.
	 * 
	 * @return
	 */
	private boolean continuePostInvalidate() {
		boolean result = false;
		switch (mJudgement) {
		case POST_INVALIDATE_JUDGEMENT_TIMES:
			if (mTime < MAX_TIME) {
				result = true;
				mTime++;
			}
			break;
		case POST_INVALIDATE_JUDGEMENT_FLAG:
			result = mDrawFlag;
			break;
		default:
			break;
		}
		return result;
	}

	public void setTextSize(int size) {
		mPaint.setTextSize(size);
	}

	public int getJudgement() {
		return mJudgement;
	}

	public void setJudgement(int mJudgement) {
		this.mJudgement = mJudgement;
	}

	public boolean getDrawFlag() {
		return mDrawFlag;
	}

	public void setDrawFlag(boolean mDrawFlag) {
		this.mDrawFlag = mDrawFlag;
	}

	public String getContentString() {
		return mContentString;
	}

	public void setContentString(String mContentString) {
		this.mContentString = mContentString;
	}

	public interface AnimationComplete {
		public void complete();
	}

	public void setAnimationComplete(AnimationComplete animationComplete) {
		mAnimationComplete = animationComplete;
	}

	private Handler mHandler = new Handler() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ANIMATION_COMPLETE:
				mAnimationComplete.complete();
				break;
			default:
				break;
			}
		}
	};

}
