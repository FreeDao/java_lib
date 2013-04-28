package com.airbiquity.connectionmgr.msp;

import android.util.Base64;
import android.util.Log;

import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.audio.Utils;
import com.airbiquity.hap.A;

public class PanLongPollingWorker extends Thread {

	private final static String TAG = "PanLongPollingWorker";

	private boolean isRunning = true;

	@Override
	public void run() {

		while(isRunning){

		     try {
		    	// Blocks until we get an async message from nomadic app.
				ApplicationMessage msg = A.a().qLongPolling.take();

				if ( (null != msg) && msg.isValid() )
				{

					//TODO if content type is base64, we need to encoding it and set the transfer encoding content
					String transferContent = msg.contentType;
					String result = new String(msg.payload) ;
					int id = IdGenerator.getInstance().generateId();

					if(msg.contentType.indexOf("image/") != -1){
						transferContent = "base64";
						//result = "'"+Base64.encodeToString(msg.payload, Base64.DEFAULT)+"'";
						//result = Base64.encodeToString(msg.payload, Base64.DEFAULT);
						
						String path = "hap://"+ id;
						//PanAppManager.imageMap.put(path, result);
						PanAppManager.imageMap.put(path, msg.payload);
						notifyAppImage(id,msg.appName,transferContent,path);
						
					}else if(msg.contentType.equalsIgnoreCase("application/octet-stream")){
						transferContent = "base64";
						//result = "'"+Base64.encodeToString(msg.payload, Base64.DEFAULT)+"'";
						//result = Base64.encodeToString(msg.payload, Base64.DEFAULT);
						result = Base64.encodeToString(msg.payload, Base64.NO_WRAP);
						
						PanAppManager.messageMap.put(id, result);
						notifyAppMessage(id,msg.appName,transferContent,msg.contentType);
					}else{
						
						PanAppManager.messageMap.put(id, result);
						notifyAppMessage(id,msg.appName,transferContent,msg.contentType);
						
					}
					//result = result.replaceAll("(\\r|\\n)", "");//we will get the result from getAppMessage in javascript interface.

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void notifyAppMessage(final int id, final String appName, final String transferEncoding, final String contentType){
		
		String url = "javascript:notifyAppMessage(" + id + ",'" + appName + "','" + transferEncoding + "','" + contentType + "')";
		Log.d(TAG, url);
		A.a().loadUrlOnUiThread(url);
		
	}
	
	private void notifyAppImage(final int id, final String appName, final String contentType, final String path ){
		String url = "javascript:notifyAppImage(" + id + ",'" +appName + "','" + contentType + "','" + path + "')";
		Log.d(TAG, url);
		A.a().loadUrlOnUiThread(url);
	}

	public void setRunning(boolean isRunning){
		this.interrupt();
		this.isRunning = isRunning;
	}

	public boolean isRunning(){
		return this.isRunning;
	}

}
