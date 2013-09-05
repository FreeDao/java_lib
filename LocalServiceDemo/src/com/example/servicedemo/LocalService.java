package com.example.servicedemo;

import java.io.FileDescriptor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class LocalService extends Service {
	private static final String TAG = "LocalService";
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"sam LocalService onbind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "sam LocalService onCreate"); 
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "sam LocalService onDestroy"); 
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "sam LocalService onStart"); 
		super.onStart(intent, startId);
	}
	
	

}
