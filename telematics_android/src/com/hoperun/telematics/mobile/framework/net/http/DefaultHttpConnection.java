package com.hoperun.telematics.mobile.framework.net.http;

import com.hoperun.telematics.mobile.framework.net.INetConnection;

public class DefaultHttpConnection extends AbstractHttpConnection implements INetConnection {

	public DefaultHttpConnection(String url) {
		super(url);
	}

}
