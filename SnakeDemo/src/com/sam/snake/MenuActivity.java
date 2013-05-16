package com.sam.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {
	private String TAG = "MenuActivity";
	private Button start_btn = null;
	private Button choice_btn = null;
	private Button arrange_btn = null;
	private Button help_btn = null;
	private Button about_btn = null;
	private Button exit_btn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		initView();
		
		
	}
	
	private void initView(){
		start_btn = (Button)findViewById(R.id.start_btn);
		choice_btn = (Button)findViewById(R.id.choice_btn);
		arrange_btn = (Button)findViewById(R.id.arrange_btn);
		help_btn = (Button)findViewById(R.id.help_btn);
		about_btn = (Button)findViewById(R.id.about_btn);
		exit_btn = (Button)findViewById(R.id.exit_btn);
		
		start_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, GameActivity.class);
				MenuActivity.this.startActivityForResult(intent, 0);
			}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int result = data.getIntExtra("result", 4);
		Log.i(TAG, "------result = "+result);
		//TODO
		
		
	}
	
	
	
}
