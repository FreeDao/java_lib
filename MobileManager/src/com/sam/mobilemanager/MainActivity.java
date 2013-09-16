package com.sam.mobilemanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//The main page of this app, button click functions
public class MainActivity extends Activity implements OnClickListener {
	private Button buttonInfo;
	private Button buttonMgr;
	private Button buttonDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonInfo = (Button) findViewById(R.id.buttonMobileInfo);
		buttonMgr = (Button) findViewById(R.id.buttonSoftMgr);
		buttonDevice = (Button) findViewById(R.id.buttonDeviceInfo);
		
		buttonInfo.setOnClickListener(this);
		buttonMgr.setOnClickListener(this);
		buttonDevice.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonMobileInfo:
			Intent intent = new Intent(this,MobileInfo.class);
			startActivity(intent);
			break;
		case R.id.buttonSoftMgr:
			Intent intent2 = new Intent(this,SoftwareManage.class);
			startActivity(intent2);
			break;
			
		case R.id.buttonDeviceInfo:
			Intent intent3 = new Intent(this,DeViceInfo.class);
			startActivity(intent3);
			break;

		default:
			break;
		}

	}

}
