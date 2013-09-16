package com.hoperun.telematics.mobile.framework.net;

import com.hoperun.telematics.mobile.framework.net.vo.NetRequest;
import com.hoperun.telematics.mobile.framework.net.vo.NetResponse;

public interface INetConnection {

	NetResponse sendRequest(String contextPath, NetRequest request) throws NetworkException;
	
	void loadResource(String contextPath, NetRequest request) throws NetworkException;

	void setTimeout(int timeout);

	public enum ENotification {
		Message, Warning, Error, Alert
	}

}
