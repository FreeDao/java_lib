package com.hoperun.telematics.mobile.framework.net.http;

import com.hoperun.telematics.mobile.framework.net.INetConnection;
import com.hoperun.telematics.mobile.framework.net.NetworkException;

public class DefaultHttpConnectionFactory implements IHttpConnectionFactory {

	private String url;

	private static DefaultHttpConnectionFactory instance;

	private DefaultHttpConnectionFactory() {

	}

	public static IHttpConnectionFactory getInstance() {
		if (instance == null) {
			instance = new DefaultHttpConnectionFactory();
		}
		return instance;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public INetConnection getConnection() throws NetworkException {
		return new DefaultHttpConnection(url);
	}

}
