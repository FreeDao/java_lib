package com.airbiquity.hap;

import android.content.Context;
import android.util.AttributeSet;
//import android.util.Log;
import android.widget.VideoView;

/** 
 * Custom VideoView that handles orientation changes correctly. 
 */
public class MyVideoView extends VideoView
{
    //private static final String TAG = "MyVideoView";    
	private int mForceHeight = 0;
	private int mForceWidth = 0;

	public MyVideoView(Context context)
	{
		super(context);
	}

	public MyVideoView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public MyVideoView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void setDimensions(int w, int h)
	{
		this.mForceHeight = h;
		this.mForceWidth = w;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(mForceWidth, mForceHeight);
	}
}
