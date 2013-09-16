package com.hoperun.telematics.mobile.framework.net.http;

import com.hoperun.telematics.mobile.framework.net.INetConnectionFactory;

public interface IHttpConnectionFactory extends INetConnectionFactory {
	public void setUrl(String url);
}
