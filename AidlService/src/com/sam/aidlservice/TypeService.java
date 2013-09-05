package com.sam.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;
import android.widget.Toast;

public class TypeService extends Service {
	String TAG = "TypeService";
	private Handler mHandler = new Handler();
	
	private ITypeServiceListener typeServiceListener;

	private ITypeService.Stub typeService = new ITypeService.Stub() {

		@Override
		public void basicTypes(final int anInt, long aLong, char aChar,
				boolean aBoolean, String aString) throws RemoteException {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					String text = "int : " + anInt;
					Toast.makeText(TypeService.this, text, Toast.LENGTH_LONG)
							.show();
				}
			});
		}

		@Override
		public void objectType(final Dog aDog) throws RemoteException {
			mHandler.post(new Runnable() {
				String text = aDog.toString();

				@Override
				public void run() {
					Toast.makeText(TypeService.this, text, Toast.LENGTH_LONG)
							.show();
				}
			});

		}

		@Override
		public Dog getDogWithType(int type) throws RemoteException {
			switch (type) {
			case 0:
				return new Dog("King", 1, "BLACK");
			case 1:
				return new Dog("Queen", 3, "White");

			default:
				break;
		}
			return new Dog("T: "+type,3,"default");

	}

		@Override
		public void request(ITypeServiceListener lisnterner) throws RemoteException {
			typeServiceListener = lisnterner;
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(5000);
						
						
					} catch (Exception e) {
						// TODO: handle exception
					}finally{
						try {
							typeServiceListener.requestCompleted("Task finished  ....");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			});
			
			
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return typeService;
	}
	
	}