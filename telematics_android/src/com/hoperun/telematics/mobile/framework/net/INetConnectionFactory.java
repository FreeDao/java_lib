package com.hoperun.telematics.mobile.framework.net;

public interface INetConnectionFactory {
	INetConnection getConnection() throws NetworkException;

}
