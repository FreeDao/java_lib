package com.hoperun.telematics.mobile.framework.net.async;

import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;

public interface INetServiceManager {

	void register(ENetworkServiceType serviceType, IAsyncHandler handler);

	public void unregister(ENetworkServiceType serviceType);

}
