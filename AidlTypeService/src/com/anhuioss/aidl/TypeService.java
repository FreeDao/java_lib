package com.anhuioss.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class TypeService extends Service {
	
	private Handler mHandler = new Handler();
	
	private ITypeServiceListener typeServiceListener;
	
	private ITypeService.Stub typeService = new ITypeService.Stub() {
		
		@Override
		public void basicTypes(final int anInt, final long aLong, final char aChar, final boolean aBoolean,
				final float aFloat, final double aDouble, final String aString)
				throws RemoteException {
			
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					String text = "Int:" + anInt
			        + "\nLong:" + aLong
			        + "\nChar:" + aChar
			        + "\nBoolean:" + aBoolean
			        + "\nFloat:" + aFloat
			        + "\nDouble:" + aDouble
			        + "\nString:" + aString;
					Toast.makeText(TypeService.this, text, Toast.LENGTH_SHORT).show();
				}
			});
			
		}

		@Override
		public void objectType(final Dog aDog) throws RemoteException {
			
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(TypeService.this, aDog.toString(), Toast.LENGTH_SHORT).show();
				}
			});
			
		}

		@Override
		public Dog getDogWithType(int type) throws RemoteException {
			switch (type) {
			case 0:
				return new Dog("King", 1, "BLACK");
			case 1:
				return new Dog("Queen", 2, "BLACK");
			default:
				break;
			}
			return new Dog("T" + type, type, "GRAY");
		}

		@Override
		public void request(ITypeServiceListener lintener)
				throws RemoteException {
			
			typeServiceListener = lintener;
			
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						try {
							typeServiceListener.requestCompleted("任务完成，返回结果！=^_^=");
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return typeService;
	}

}
