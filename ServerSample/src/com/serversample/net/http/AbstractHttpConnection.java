package com.serversample.net.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.serversample.net.exception.CustomerException;
import com.serversample.net.vo.NetRequest;
import com.serversample.net.vo.NetResponse;
import com.serversample.util.JsonHelper;

/**
 * 
 * @author Administrator
 *
 */
public abstract class AbstractHttpConnection {

	protected String url;
	protected int timeout;

	private static String HTTP_HEAD_CONTENT_TYPE = "Content-Type";
	private static String HTTP_HEAD_CONTENT_TYPE_VALUE = "application/json; charset=utf-8";

	public AbstractHttpConnection(String url) {
		this.url = url;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 
	 * @param contextPath
	 * @param request
	 * @return
	 * @throws CustomerException
	 */
	public NetResponse sendRequest(String contextPath, NetRequest request) throws CustomerException{
		Log.d(this.getClass().getName(), url + contextPath);
		
		HttpClient client = getHttpClient();
		HttpPost post = getHttpPost(url + contextPath, request);
		NetResponse result = null;
		try{
			HttpResponse res = client.execute(post);
			String responseContextString = EntityUtils.toString(res.getEntity(), "UTF-8");
			Log.d(this.getClass().getName(), "HTTP Post Response: " + responseContextString);
			result = JsonHelper.parseString(responseContextString, NetResponse.class);
		}
		catch(Exception e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			throw new CustomerException(e.getMessage());
		}

		return result;
	}

	protected HttpClient getHttpClient() {
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		ConnManagerParams.setTimeout(httpParams, timeout);

		return client;
	}

	protected HttpPost getHttpPost(String url, NetRequest request) {
		HttpPost post = new HttpPost(url);
		post.setHeader(HTTP_HEAD_CONTENT_TYPE, HTTP_HEAD_CONTENT_TYPE_VALUE);

		String contextString = JsonHelper.doStringify(request);

		Log.d(this.getClass().getName(), "HTTP Post Request: " + contextString);
		HttpEntity entity = new ByteArrayEntity(contextString.getBytes());
		post.setEntity(entity);
		return post;
	}

	public static void main(String[] args) {

	}
}
