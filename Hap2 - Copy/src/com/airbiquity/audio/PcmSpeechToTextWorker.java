package com.airbiquity.audio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;

import com.airbiquity.connectionmgr.msp.MspAudioMgr;
import com.airbiquity.hap.A;
import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.UrlMaker;

public class PcmSpeechToTextWorker implements Runnable {
	
	private final static String TAG = "PcmSpeechToTextWorker";
	
	//Parameters
	private final static String AUDIO_CONTENT_TYPE = URLEncoder.encode("audio/x-wav;codec=pcm;bit=16;rate=16000");
	private String language;
	private boolean isMultipleResults = false;
	
	
	// Header
	private final static String CONTENT_TYPE = "binary/octet-stream";
	
	private Thread currentThread;
	private final Object mutex = new Object();
	private boolean isProcessing = false;
	
	public final LinkedBlockingQueue<RawData> list = new LinkedBlockingQueue<RawData>();
	
	private MspAudioMgr mAudioMgr = null;
	private int vrId;

	
	/**
	 * Constructor with multiple result false.
	 * @param language
	 */
	public PcmSpeechToTextWorker(String language){
		this.language = language;
	}
	
	/**
	 * Constructor with multiple result parameters
	 * @param language
	 * @param isMultipleResults
	 */
	public PcmSpeechToTextWorker(String language, boolean isMultipleResults){
		this.language = language;
		this.isMultipleResults = isMultipleResults;
	}

	

	@Override
	public void run() {
		currentThread = Thread.currentThread();
		
		HttpURLConnection connection = null;
		DataOutputStream dataOutputStreamInstance = null;
		DataInputStream dataInputStreamInstance = null;

		try {


			URL mURL = UrlMaker.getUrlForSpeechToText(this.language, AUDIO_CONTENT_TYPE, this.isMultipleResults);
			
			Log.d(TAG, "speech to text url = "+mURL.toString());
			connection = (HttpURLConnection) mURL.openConnection();
			connection.setDoOutput(true);
			connection.setChunkedStreamingMode(0);

			connection.setRequestProperty(HttpHeader.NAME_MIP_ID, A.getMipId());
			connection.setRequestProperty(HttpHeader.NAME_AUTH_TOKEN, A.getAuthToken());
			connection.setRequestProperty(HttpHeader.NAME_CONTENT_TYPE, CONTENT_TYPE);

			dataOutputStreamInstance = new DataOutputStream( new BufferedOutputStream( connection.getOutputStream()));
			
			//TODO VR is ready here if we need this
			
			
			//Start Stop
	        synchronized( mutex )
	        {
	            while( !isProcessing )
	            {
	                try
	                {
	                    mutex.wait();
	                }
	                catch( InterruptedException e )
	                {
	                    return;
	                }
	            }
	        }

			boolean isVoiceDetected = false;
			SimpleVAD vadImpl = new SimpleVAD();
			
			while (isProcessing) {
				
				Timer timer = new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						currentThread.interrupt();
					}
					
				}, 3000);
				
				RawData raw = list.take();
				
				timer.cancel();
				
				boolean updateVadStatus = false;
				
				if (raw != null) {
					updateVadStatus = vadImpl.update(raw.pcm, raw.size);
					Log.d("TAG", "is voice detected = " + updateVadStatus);
					if (updateVadStatus) {
						isVoiceDetected = true;
					}
					if (isVoiceDetected && !updateVadStatus) {
						isProcessing = false;
					}
					// TODO be careful of here
					byte[] buf = Utils.convertShortArrayToByteArray(raw.pcm, 0, raw.size);
					dataOutputStreamInstance.write(buf, 0, buf.length);
				}
				
			}
			
			//send  off board vr stop request to HU
			mAudioMgr.sendOffBoardVrRequest(2);
			
			dataOutputStreamInstance.flush();
			
			dataInputStreamInstance =new DataInputStream(new BufferedInputStream(connection.getInputStream()));
			byte[] b = new byte[1024];
			int c = 0;
			StringBuffer sb = new StringBuffer();
			while ((c = dataInputStreamInstance.read(b)) != -1) {
				sb.append(new String(b, 0, c));
			}
			
			//TODO send it to javascript call endVr(id, text)
			Log.e("TAG", "VR Response = " + sb.toString());
			Log.e("TAG", "javascript:vrEnd("+vrId+",'"+sb.toString()+"')");
			A.a().loadUrlOnUiThread("javascript:vrEnd("+vrId+",'"+sb.toString()+"')");
			
			

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			//send  off board vr stop request to HU
			mAudioMgr.sendOffBoardVrRequest(2);
			try {
				dataInputStreamInstance = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
				byte[] b = new byte[1024];
				int c = 0;
				StringBuffer sb = new StringBuffer();
				while ((c = dataInputStreamInstance.read(b)) != -1) {
					sb.append(new String(b, 0, c));
				}
				// TODO send it to javascript call endVr(id, text)
				Log.e("TAG", "VR Response = " + sb.toString());
				Log.e("TAG", "javascript:vrEnd(" + sb.toString() + "," + vrId + ")");
				A.a().loadUrlOnUiThread( "javascript:vrEnd(" + sb.toString() + "," + vrId + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}finally{
			
			Log.d("TAG", "finally");
			
			try {
				// while this should be close correctly
				// if( dataOutputStreamInstance != null ){
				// dataOutputStreamInstance.close();
				// dataOutputStreamInstance = null;
				// }
				
				if( dataInputStreamInstance != null ){
					dataInputStreamInstance.close();
					dataInputStreamInstance = null;
				}
				
				if( connection != null ){
					connection.disconnect();
					connection = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
	

    public boolean setProcessing( boolean isProcessing )
    {
    	boolean ret = false;
        synchronized( mutex )
        {
            this.isProcessing = isProcessing;
            if( this.isProcessing )
            {
                mutex.notify();
            }else{
            	if( currentThread != null){
            		currentThread.interrupt();
            	}
            }
            ret = true;
        }
        return ret;
    }


    public boolean isProcessing()
    {
        synchronized( mutex )
        {
            return isProcessing;
        }
    }
	
	
	
	public void putData(short[] buf, int size )
	{
		RawData data = new RawData();
		data.size = size;
		System.arraycopy(buf, 0, data.pcm, 0, size);
		list.add(data);
	}
	
	class RawData {
		int size;
		short[] pcm = new short[4096];
	}
	
	public void setAudioMgr(MspAudioMgr audioMgr)
	{
		this.mAudioMgr = audioMgr;
	}

	public void setVrId(int id) 
	{
		this.vrId = id;
	}
    
}
