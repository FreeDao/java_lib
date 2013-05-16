package com.sam.snake;

import com.sam.constance.GameConstance;
import com.sam.view.GameView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class GameActivity extends Activity {
	private LinearLayout layout = null;
	private GameView gameView = null;
	MyHandler handler = new MyHandler();
	private boolean isPaused = false;
	
   	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initData();
		initView();
		
	}
   	
   	public void initData(){
   		GameConstance.curOrientation = GameConstance.DOWN;
   		GameConstance.map = null;
   		GameConstance.operator = null;
   		GameConstance.UPDATE_RATE = 500;
   		GameConstance.score = 0;
   		GameConstance.eveScore = 10;
//   		GameConstance.mapChoice = getIntent.GameConstance("map",4);
   		
   	}
   	
   	public void initView(){
   		layout = (LinearLayout)findViewById(R.id.layout);
   		gameView = new GameView(this);
   		gameView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
   		layout.addView(gameView);	// add gameView to layout
   		
   	}

   	UpThread ut = new UpThread();
   	
   public 	class UpThread extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
   		
   	}
   	
   	public void update(){
   		 //TODO
   	}
   	
	public class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				
				break;

			default:
				break;
			}
		
		
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	
	
}
