package com.airbiquity.hap;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class LongPollingThread extends Thread{
	
	private String getEventUrl = "http://192.168.0.3:8090/hap/api/1.0/getEvent";

	@Override
	public void run() {
		HttpClient client = new DefaultHttpClient();
		while(true){
			try {
				HttpGet request = new HttpGet(getEventUrl);
				HttpResponse response = client.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				System.out.println("StatusCode = "+statusCode);
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				int c = 0;
				byte[] b = new byte[1024];
				StringBuffer sb = new StringBuffer();
				while((c = content.read(b))!=-1){
					sb.append(new String(b,0,c));
				}
				System.out.println(sb.toString());
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				client.getConnectionManager().closeExpiredConnections();
			}
		}
	}
}

