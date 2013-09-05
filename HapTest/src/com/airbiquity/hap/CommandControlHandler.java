package com.airbiquity.hap;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class CommandControlHandler{
	
	private String commandControlUrl = "http://192.168.0.3:8080/hap/api/1.0/commandControl/";
	
	private String appName ;
	private int statusCode;
	private String contentType;
	
	public CommandControlHandler(String appName){
		this.appName = appName;
		commandControlUrl = commandControlUrl.concat(appName);
	}
	
	public String sendCommand(byte[] payload,String contentType){
		
		String result = "";;

		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(commandControlUrl);
			StringEntity reqEntity = new StringEntity(new String(payload));
			request.setEntity(reqEntity);
			request.setHeader("Content-Type", contentType);
			
			HttpResponse response = client.execute(request);
			setStatusCode(response.getStatusLine().getStatusCode());
			
			HttpEntity entity = response.getEntity();
			setContentType(entity.getContentType().getValue());
			
			InputStream content = entity.getContent();
			int c = 0;
			byte[] b = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while((c = content.read(b))!=-1){
				sb.append(new String(b,0,c));
			}
			result = sb.toString();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.getConnectionManager().closeExpiredConnections();
		}
		
		return result;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
	
}