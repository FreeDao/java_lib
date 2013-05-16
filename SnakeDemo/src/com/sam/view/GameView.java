package com.sam.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.sam.snake.GameActivity;

public class GameView extends View{
	private String TAG = "GameView";
	private Context c;
	private int width;
	private int height;
	
	private int xCount;
	private int yCount;
	
	private int xOffSet;
	private int yOffSet;
	
	private Bitmap[] pics;
	

	public GameView(Context context) {
		super(context);
		this.c = context;
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i(TAG, "------onsize changed");
		initData(w,h);
		
	}
	
	private void initData(int w, int h){
		width = w;
		height =h;
		
		
		
		
	}
	
}















