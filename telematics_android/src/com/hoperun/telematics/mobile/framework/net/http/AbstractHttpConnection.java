package com.hoperun.telematics.mobile.framework.net.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.hoperun.telematics.mobile.framework.net.INetConnection;
import com.hoperun.telematics.mobile.framework.net.NetworkException;
import com.hoperun.telematics.mobile.framework.net.helper.JsonHelper;
import com.hoperun.telematics.mobile.framework.net.helper.KeyStoreHelper;
import com.hoperun.telematics.mobile.framework.net.vo.NetRequest;
import com.hoperun.telematics.mobile.framework.net.vo.NetResponse;
import com.hoperun.telematics.mobile.framework.resource.LocalResourceManager;

public abstract class AbstractHttpConnection implements INetConnection {

	protected String url = "http://119.255.194.36:9010/TSP3S/phone/v1";
	protected int timeout;
	private static String HTTP_HEAD_CONTENT_TYPE = "Content-Type";
	private static String HTTP_HEAD_CONTENT_TYPE_VALUE = "application/json; charset=utf-8";

	public AbstractHttpConnection(String url) {
		this.url = url;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public NetResponse sendRequest(String contextPath, NetRequest request) throws NetworkException {
		HttpClient client = getHttpClient();

		Log.d(this.getClass().getName(), url + contextPath);

		HttpPost post = getHttpPost(url + contextPath, request);
		NetResponse result = null;
		try {
			if (url.startsWith("https")) {
				SSLSocketFactory socketFactory = new SSLSocketFactory(KeyStoreHelper.getInstance().getKeyStore());
				Scheme sch = new Scheme("https", socketFactory, 443);
				client.getConnectionManager().getSchemeRegistry().register(sch);
			}

			HttpResponse res = client.execute(post);
			String responseContextString = EntityUtils.toString(res.getEntity(), "UTF-8");
			Log.d(this.getClass().getName(), "HTTP Post Response: " + responseContextString);
			result = JsonHelper.parseResponse(responseContextString);
			// res.getEntity().getContent().close();
		} catch (Exception e) {
			throw new NetworkException("DefaultHttpConnection send sycn request error!", e);
		}
		return result;
	}

	@Override
	public void loadResource(String contextPath, NetRequest request) throws NetworkException {

		HttpClient client = getHttpClient();

		Log.d(this.getClass().getName(), url + contextPath);

		HttpPost post = getHttpPost(url + contextPath, request);
		try {
			SSLSocketFactory socketFactory = new SSLSocketFactory(KeyStoreHelper.getInstance().getKeyStore());
			Scheme sch = new Scheme("https", socketFactory, 443);
			client.getConnectionManager().getSchemeRegistry().register(sch);

			HttpResponse res = client.execute(post);
			LocalResourceManager.getInstance().set(request.getResId(), res.getEntity().getContent());
			// res.getEntity().getContent().close();
		} catch (Exception e) {
			throw new NetworkException("DefaultHttpConnection send sycn request error!", e);
		}
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

		String contextString = JsonHelper.parseRequest(request);

		Log.d(this.getClass().getName(), "HTTP Post Request: " + contextString);
		HttpEntity entity = new ByteArrayEntity(contextString.getBytes());
		post.setEntity(entity);

		return post;
	}

	public static void main(String[] args) {

	}
}
