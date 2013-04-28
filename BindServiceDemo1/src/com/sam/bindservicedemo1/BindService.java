package com.sam.bindservicedemo1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BindService extends Service{
	private static final String TAG = "BindService";
	
	public void MyMethod(){
		 Log.i(TAG, "BindService-->MyMethod()");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	
	 public class MyBinder extends Binder{
	        
	        public BindService getService(){
	            return BindService.this;
	        }
	    }
	 private MyBinder myBinder = new MyBinder();
}
