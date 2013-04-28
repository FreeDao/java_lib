package com.sam.countservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CountService extends Service{
	String TAG = "CountService";
	private boolean threadDisable;
	private int count;
	

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "count service was onBind");
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "count service was oncreate");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "count service was onDestroy");
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "count service was onStartCommand");
		while(!threadDisable){
			try {
				Thread.sleep(1000);
				
			} catch (Exception e) {
			}
			
			count++;
			Log.i(TAG, " count== "+count);
		}
		
		
		return START_STICKY;
	}
	

}
