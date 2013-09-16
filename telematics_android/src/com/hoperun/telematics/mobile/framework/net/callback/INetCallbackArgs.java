package com.hoperun.telematics.mobile.framework.net.callback;

import java.io.Serializable;

import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;

public interface INetCallbackArgs extends Serializable {
	String getPayload();

	ECallbackStatus getStatus();

	String getErrorMessage();
}
