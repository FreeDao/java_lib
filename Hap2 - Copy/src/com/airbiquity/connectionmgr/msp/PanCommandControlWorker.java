package com.airbiquity.connectionmgr.msp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.airbiquity.application.manager.ApplicationManager;
import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.audio.Utils;
import com.airbiquity.hap.A;
import com.airbiquity.hap.IHapCallback;

public class PanCommandControlWorker extends Thread {

	private final static String TAG = "PanCommandControlWorker";

	private final static long WAIT = 20000;//wait 20 seconds for call back

	private int seqNum;
	private String appName;
	private String contentType;
	private String transferEncoding;
	private String content;

    //hard code application list
    private Map<String,String> appMapping = new HashMap<String,String>();
    {
    	appMapping.put("com.clearchannel.iHeartRadio", "com.clearchannel.iheartradio.connect");
    	appMapping.put("Pandora", "com.pandora.android");
    }


	public PanCommandControlWorker(int seqNum, String appName, String contentType, String transferEncoding, String content)
	{
		this.appName = appName;
		this.seqNum = seqNum;
		this.contentType = contentType;
		this.transferEncoding = transferEncoding;
		this.content = content;
	}






	@Override
	public void run() {

		try {

			ApplicationManager appManager = ApplicationManager.getInstance();
			IHapCallback c = appManager.getCallback(appName);

			if (null == c) {
				// No callback, try to start up application here.
				Log.d(TAG, "No callback for " + appName);
				//Trying to launch application with package name
				String packageName = appMapping.get(appName);
				Intent intent = A.a().getPackageManager().getLaunchIntentForPackage(packageName);
	            A.a().startActivity( intent );
			}

			long time = 0;

			while ( c == null ) {
				if(time <= WAIT){
					Thread.sleep(100);
					time += 100;
					c = appManager.getCallback(appName);
				}else{
					//TODO send error here
					return ;
				}
			}

			final LinkedBlockingQueue<ApplicationMessage> q = new LinkedBlockingQueue<ApplicationMessage>();
			// Create a queue for the response to this request.
			A.a().mapQueues.put(seqNum, q);
			Log.d(TAG, "put queue into map "+ seqNum);
			// Send message to the nomadic app.
			byte[] payload = content.getBytes();

			// decode the content if necessary
			if (null != transferEncoding) {
				if ( transferEncoding.equalsIgnoreCase("application/hex-array") ) {
					payload = convertJsonHexArrayToBytes(content);
				} else if ( transferEncoding.equalsIgnoreCase("base64") ) {
					payload = Base64.decode(content.getBytes(), Base64.DEFAULT);
				}
			}

			if(payload != null ){
				c.onHapCommandReceived(seqNum, payload, contentType);
			}
			// Get blocked until we have a response from the nomadic app.

			ApplicationMessage msg = q.poll(10, TimeUnit.SECONDS);
			A.a().mapQueues.remove(seqNum);
			Log.d(TAG, "remove queue from map "+ seqNum);

			if ( msg != null && msg.isValid()) {
				// TODO encoding content with transfer encoding

				String transferContent =  msg.contentType;
				String result = new String(msg.payload);
				
				int id = IdGenerator.getInstance().generateId();

				if( msg.contentType.indexOf("image/") != -1 ){
					transferContent = "base64";
					//result = "'"+Base64.encodeToString(msg.payload, Base64.DEFAULT)+"'";
					//result = Base64.encodeToString(msg.payload, Base64.DEFAULT);//we don't need quote here, because we will not send this image to javascript
					
					//create a fake path here
					String path = "hap://"+ msg.sequenceNumber;
					//PanAppManager.imageMap.put(path, result);
					PanAppManager.imageMap.put(path, msg.payload);
					notifyAppImage(msg.sequenceNumber,msg.appName,transferContent,path);
					
				}else if(msg.contentType.equalsIgnoreCase("application/octet-stream")){
					
					transferContent = "base64";
					//result = "'"+Base64.encodeToString(msg.payload, Base64.DEFAULT)+"'";
					//result = Base64.encodeToString(msg.payload, Base64.DEFAULT);//we don't need quote here, because we will send this by javascript interface
					result = Base64.encodeToString(msg.payload, Base64.NO_WRAP);
					
					PanAppManager.messageMap.put(id, result);
					notifyAppMessage(msg.sequenceNumber,msg.appName,transferContent,msg.contentType);
					
				}else{
					
					PanAppManager.messageMap.put(msg.sequenceNumber, result);
					notifyAppMessage(msg.sequenceNumber,msg.appName,transferContent,msg.contentType);
				}

			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
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

	private byte[] convertJsonHexArrayToBytes(String content){

		byte[] payload = null;
		try {
			JSONArray ja = new JSONArray(content);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(4 * ja.length());
			DataOutputStream dos = new DataOutputStream(bos);

			for (int i = 0; i < ja.length();) {
				String s = ja.getString(i++);
				int v = Integer.valueOf(s, 16);
				dos.writeByte(v);
			}
			payload = bos.toByteArray();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return payload;
	}

}
