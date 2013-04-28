package com.sam.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	private Button startButton;
	private Button endButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (Button) findViewById(R.id.button1);
		endButton = (Button) findViewById(R.id.button2);
		
		startButton.setOnClickListener(new StartButtonListener());
		endButton.setOnClickListener(new EndButtonListener());

	}
	
	class StartButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			handler.post(updateThread);
		}}
	
	class EndButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			handler.removeCallbacks(updateThread);
		}}
	
	Handler handler = new Handler();
	Runnable updateThread = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("sam  -----update thread");
			handler.postDelayed(updateThread, 1000);
		}
		
	};

}
















