package com.sam.countservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CountServiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count_service);
		startService(new Intent("com.sam.countService"));
	}


}
