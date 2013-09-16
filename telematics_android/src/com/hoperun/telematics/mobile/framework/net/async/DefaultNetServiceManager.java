package com.hoperun.telematics.mobile.framework.net.async;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.hoperun.telematics.mobile.framework.mq.IMessageCallback;
import com.hoperun.telematics.mobile.framework.mq.IMessageConsumer;
import com.hoperun.telematics.mobile.framework.mq.IMessageProducer;
import com.hoperun.telematics.mobile.framework.mq.IMessageQueue;
import com.hoperun.telematics.mobile.framework.mq.IMessageQueueManager;
import com.hoperun.telematics.mobile.framework.mq.impl.DefaultConsumer;
import com.hoperun.telematics.mobile.framework.mq.impl.DefaultMessageQueue;
import com.hoperun.telematics.mobile.framework.mq.impl.DefaultMessageQueueManager;
import com.hoperun.telematics.mobile.framework.mq.impl.DefaultProducer;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;

public class DefaultNetServiceManager implements INetServiceManager {
	private static Map<ENetworkServiceType, IAsyncHandler> registeredAsyncRequest = new HashMap<ENetworkServiceType, IAsyncHandler>();
	private static DefaultNetServiceManager instance;

	private IMessageQueueManager<IMessageCallback> messageQueueManager = new DefaultMessageQueueManager<IMessageCallback>();
	private IMessageConsumer consumer;
	private IMessageProducer producer;

	/**
	 * Creates a new instance of DefaultNetServiceManager.
	 */
	private DefaultNetServiceManager() {
		IMessageQueue<IMessageCallback> asyncQueue = new DefaultMessageQueue<IMessageCallback>();
		asyncQueue.setName("Async Queue");
		consumer = new DefaultConsumer(asyncQueue);
		producer = new DefaultProducer(asyncQueue);
		messageQueueManager.init(asyncQueue, producer, consumer);
	}

	public static DefaultNetServiceManager getInstance() {
		if (instance == null) {
			instance = new DefaultNetServiceManager();
		}
		return instance;
	}

	@Override
	public void register(ENetworkServiceType serviceType, IAsyncHandler handler) {
		synchronized (registeredAsyncRequest) {
			if (!registeredAsyncRequest.containsKey(serviceType)) {
				registeredAsyncRequest.put(serviceType, handler);
				Log.i(this.getClass().getName(), String.format("Register Net Service (%s)", serviceType));
				// handler.sendRequest();

				producer.offer(handler);
			} else {
				Log.w(this.getClass().getName(),
						String.format("The (%s) Net Service has been Registered!", serviceType));
			}

		}

	}

	public IAsyncHandler getHandler(ENetworkServiceType serviceType) {
		synchronized (registeredAsyncRequest) {
			return registeredAsyncRequest.get(serviceType);
		}
	}
	
	public void offerHandler(IAsyncHandler handler){
		producer.offer(handler);
	}

	@Override
	public void unregister(ENetworkServiceType serviceType) {
		synchronized (registeredAsyncRequest) {
			if (registeredAsyncRequest.containsKey(serviceType)) {
				IAsyncHandler handler = registeredAsyncRequest.remove(serviceType);
				handler.setAvailable(false);
				handler.cancel();
				Log.i(this.getClass().getName(), String.format("Unregister Notification (%s)", serviceType));
			} else {
				Log.w(this.getClass().getName(),
						String.format("The (%s) Notification has been Unregistered!", serviceType));
			}
		}
	}
}
