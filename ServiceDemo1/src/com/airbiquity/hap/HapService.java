package com.airbiquity.hap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class HapService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("TAG", "----------service 1------------");
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("TAG", "----------service 1-on Start command-----------");
		
		return super.onStartCommand(intent, flags, startId);
	}
	

}
