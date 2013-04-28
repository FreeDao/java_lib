package com.airbiquity.hap;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HapHttp {
	
	private String resContent;
	private int statusCode;
	private String contentType;
	
	
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
	
	
	public String sendHttpPostRequest(String url,String reqContent,String contentType) {

		HttpClient client = new DefaultHttpClient();

		try {
			HttpPost request = new HttpPost(url);
			StringEntity str = new StringEntity(reqContent);
			request.setEntity(str);
			request.setHeader("Content-Type", contentType);
			HttpResponse response = client.execute(request);
			setStatusCode(response.getStatusLine().getStatusCode());

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				Header contentTypeHeader = entity.getContentType();
				if (contentTypeHeader != null) {
					setContentType(entity.getContentType().getValue());
				}

				InputStream content = entity.getContent();
				int c = 0;
				byte[] b = new byte[1024];
				StringBuffer sb = new StringBuffer();
				while ((c = content.read(b)) != -1) {
					sb.append(new String(b, 0, c));
				}

				resContent = sb.toString();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resContent;
	}
	
	
	public String sendHttpGetRequest(String url) {

		HttpClient client = new DefaultHttpClient();

		try {
			HttpGet request = new HttpGet(url);

			HttpResponse response = client.execute(request);
			setStatusCode(response.getStatusLine().getStatusCode());

			HttpEntity entity = response.getEntity();
			setContentType(entity.getContentType().getValue());

			InputStream content = entity.getContent();
			int c = 0;
			byte[] b = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while ((c = content.read(b)) != -1) {
				sb.append(new String(b, 0, c));
			}

			resContent = sb.toString();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resContent;
	}


}
