package com.sam.acceleramote;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class AccelerateActivity extends Activity {
	private SimulationView mSimulationView;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private PowerManager mPowerManager;
	private WindowManager mWindowManager;
	private Display mDisplay;
	private WakeLock mWakeLock;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accelerate);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mPowerManager = (PowerManager)getSystemService(POWER_SERVICE);
		mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		
		
		
	}

	
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mSensorManager.unregisterListener(this);
	}





	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
	}





	class SimulationView extends View implements SensorEventListener{

		public SimulationView(Context context) {
			super(context);
		}
		
		

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
