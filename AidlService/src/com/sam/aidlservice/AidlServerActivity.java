package com.sam.aidlservice;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AidlServerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aidl_server);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aidl_server, menu);
		return true;
	}

}
