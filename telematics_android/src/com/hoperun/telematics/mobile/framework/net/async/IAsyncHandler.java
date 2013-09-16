package com.hoperun.telematics.mobile.framework.net.async;

import android.os.Messenger;

import com.hoperun.telematics.mobile.framework.mq.IMessageCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;

public interface IAsyncHandler extends IMessageCallback {

	public void updateDelay(long delay);

	public boolean isAvailable();

	public void setAvailable(boolean isAvailable);

	public void sendRequest();

	public void getResult();

	public void setResId(String resId);

	public void cancel();

	public INetCallback getSavedCallback();

	public Messenger getSavedReplyTo();
}
