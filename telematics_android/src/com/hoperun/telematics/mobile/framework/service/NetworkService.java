/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hoperun.telematics.mobile.framework.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.hoperun.telematics.mobile.activity.LoginActivity;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.async.DefaultAsyncHandler;
import com.hoperun.telematics.mobile.framework.net.async.DefaultNetServiceManager;
import com.hoperun.telematics.mobile.framework.net.async.DefaultNotificationHandler;
import com.hoperun.telematics.mobile.framework.net.async.IAsyncHandler;
import com.hoperun.telematics.mobile.framework.net.callback.AsyncNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.AsyncResultNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.DefaultNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback.ECallbackStatus;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.callback.NotificationNetCallbackArgs;
import com.hoperun.telematics.mobile.framework.net.helper.DefineHelper;
import com.hoperun.telematics.mobile.framework.net.helper.JsonHelper;
import com.hoperun.telematics.mobile.framework.net.helper.ResourcesHelper;
import com.hoperun.telematics.mobile.framework.net.http.DefaultHttpConnectionFactory;
import com.hoperun.telematics.mobile.framework.net.vo.NetResponse;
import com.hoperun.telematics.mobile.framework.resource.LocalResourceManager;

/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application. The
 * {@link LocalServiceActivities.Controller} and
 * {@link LocalServiceActivities.Binding} classes show how to interact with the
 * service.
 * 
 * <p>
 * Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service. This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */

public class NetworkService extends Service {
	/**
	 * @uml.property name="mNM"
	 * @uml.associationEnd
	 */
	private NotificationManager mNM;

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	/**
	 * @uml.property name="nOTIFICATION"
	 */
	private int NOTIFICATION = R.string.local_service_started;

	// service static define
	// private static String DEFAULT_HOST_URL =
	// "https://mobilegateway.eobu.com/mobilegateway/service";
	private static String DEFAULT_HOST_URL = "http://119.255.194.36:8070/TSP3S/phone/v1";
	private static long DEFAULT_ASYCN_DELAY = 2000;
	private static long DEFAULT_NET_SERVICE_DELAY = 10000;

	public static final int TYPE_REGISTER = 0x0001;

	/**
	 * @uml.property name="mIncomingHandler"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Handler mIncomingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle params = msg.getData();
			String jsonString = (String) params.get(DefineHelper.MESSAGE_KEY_PARAMS);
			EMessageType messageType = (EMessageType) params.get(DefineHelper.MESSAGE_KEY_TYPE);
			ENetworkServiceType serviceType = (ENetworkServiceType) params.get(DefineHelper.MESSAGE_KEY_SERVICE_TYPE);
			INetCallback callback = (INetCallback) msg.obj;

			Log.d(this.getClass().getName(), String.format("(%s), (%s), (%s)", messageType.toString(),
					jsonString == null ? "payload is null" : jsonString, serviceType.toString()));

			Bundle results = new Bundle();

			NetResponse response = null;
			IAsyncHandler handler = null;
			try {
				switch (messageType) {
				case Sync:
					response = DefaultHttpConnectionFactory.getInstance().getConnection()
							.sendRequest(serviceType.getPath(), JsonHelper.packageRequest(jsonString));
					if (callback != null && response != null) {
						results.putSerializable(
								DefineHelper.MESSAGE_KEY_RESULTS,
								new DefaultNetCallbackArgs(response.getPayload(), ECallbackStatus.Success, response
										.getErrorMessage()));
						doCallback(results, msg.replyTo, callback);
					}
					break;
				case Async:
					Log.i(this.getClass().getName(), jsonString);

					handler = new DefaultAsyncHandler(serviceType, JsonHelper.packageRequest(jsonString),
							netCallbackMessager, DEFAULT_ASYCN_DELAY, true, msg.replyTo, callback);
					DefaultNetServiceManager.getInstance().register(serviceType, handler);

					break;
				case CancelAsync:
					DefaultNetServiceManager.getInstance().unregister(serviceType);
					break;
				case RegisterNetService:
					handler = new DefaultNotificationHandler(serviceType, JsonHelper.packageRequest(jsonString),
							netCallbackMessager, DEFAULT_NET_SERVICE_DELAY, true, msg.replyTo, callback);
					DefaultNetServiceManager.getInstance().register(serviceType, handler);

					break;
				case UnregisterNetService:
					DefaultNetServiceManager.getInstance().unregister(serviceType);
					break;
				case PauseNetService:
					DefaultNetServiceManager.getInstance().unregister(serviceType);
					break;
				case ResumeNetService:
					DefaultNetServiceManager.getInstance().unregister(serviceType);
					break;
				default:
					Log.w(this.getClass().getName(), "Do not support handle type!");
				}

			} catch (Exception e) {
				Log.e(this.getClass().getName(), "Send request error!", e);
				results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultNetCallbackArgs(null,
						ECallbackStatus.Failure, e.getMessage()));
				doCallback(results, msg.replyTo, callback);
			}
		}

	};

	/**
	 * @uml.property name="mServiceMessenger"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final Messenger mServiceMessenger = new Messenger(mIncomingHandler);

	/**
	 * @uml.property name="netCallbackHandler"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Handler netCallbackHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			INetCallbackArgs args = (INetCallbackArgs) msg.getData().get(DefineHelper.MESSAGE_KEY_NET_CALLBACK_RESULTS);

			if (args instanceof AsyncResultNetCallbackArgs) {
				AsyncResultNetCallbackArgs asyncResultArgs = (AsyncResultNetCallbackArgs) args;
				Log.d("netCallbackHandler.AsyncResultNetCallbackArgs", asyncResultArgs.getStatus().toString());
				Log.d("netCallbackHandler.AsyncResultNetCallbackArgs",
						asyncResultArgs.getPayload() == null ? "payload is null" : asyncResultArgs.getPayload());

				ENetworkServiceType serviceType = asyncResultArgs.getServiceType();
				IAsyncHandler handler = DefaultNetServiceManager.getInstance().getHandler(serviceType);
				if (handler != null) {
					if (asyncResultArgs.getStatus() == ECallbackStatus.Processing) {
						handler.updateDelay(asyncResultArgs.getInterval());

						DefaultNetServiceManager.getInstance().offerHandler(handler);
					} else if (asyncResultArgs.getStatus() == ECallbackStatus.Success) {
						Bundle results = new Bundle();
						results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS,
								new DefaultNetCallbackArgs(asyncResultArgs.getPayload(), ECallbackStatus.Success,
										asyncResultArgs.getErrorMessage()));
						doCallback(results, handler.getSavedReplyTo(), handler.getSavedCallback());
						DefaultNetServiceManager.getInstance().unregister(serviceType);
					} else {
						//TODO by leilei, when use weather , it always return fault first time
//						handler.updateDelay(asyncResultArgs.getInterval());
//						DefaultNetServiceManager.getInstance().offerHandler(handler);
						//TODO original code
						handler.cancel();
						DefaultNetServiceManager.getInstance().unregister(serviceType);
						Bundle results = new Bundle();
						results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultNetCallbackArgs(null,
								ECallbackStatus.Failure, asyncResultArgs.getErrorMessage()));
						doCallback(results, handler.getSavedReplyTo(), handler.getSavedCallback());
					}
				}
			} else if (args instanceof AsyncNetCallbackArgs) {
				AsyncNetCallbackArgs asyncArgs = (AsyncNetCallbackArgs) args;
				ENetworkServiceType serviceType = asyncArgs.getServiceType();
				IAsyncHandler handler = DefaultNetServiceManager.getInstance().getHandler(serviceType);
				Log.d("netCallbackHandler.AsyncNetCallbackArgs", asyncArgs.getStatus().toString());
				Log.d("netCallbackHandler.AsyncNetCallbackArgs", asyncArgs.getPayload() == null ? "payload is null"
						: asyncArgs.getPayload());
				if (handler != null) {
					if (asyncArgs.getStatus() == ECallbackStatus.Processing
							|| asyncArgs.getStatus() == ECallbackStatus.Success) {
						String resId = asyncArgs.getResId();
						handler.updateDelay(asyncArgs.getInterval());
						handler.setResId(resId);
						// handler.getResult();

						DefaultNetServiceManager.getInstance().offerHandler(handler);
					} else {
						handler.cancel();
						DefaultNetServiceManager.getInstance().unregister(serviceType);
						Bundle results = new Bundle();
						results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultNetCallbackArgs(null,
								ECallbackStatus.Failure, asyncArgs.getErrorMessage()));
						doCallback(results, handler.getSavedReplyTo(), handler.getSavedCallback());
					}
				}
			} else if (args instanceof NotificationNetCallbackArgs) {

				NotificationNetCallbackArgs tempArgs = (NotificationNetCallbackArgs) args;
				Log.d("netCallbackHandler.NotificationNetCallbackArgs", tempArgs.getStatus().toString());
				Log.d("netCallbackHandler.NotificationNetCallbackArgs",
						tempArgs.getPayload() == null ? "payload is null" : tempArgs.getPayload());

				ENetworkServiceType serviceType = tempArgs.getServiceType();
				IAsyncHandler handler = DefaultNetServiceManager.getInstance().getHandler(serviceType);
				if (handler != null) {
					if (tempArgs.getStatus() == ECallbackStatus.Processing) {
						handler.updateDelay(tempArgs.getInterval());
						// handler.getResult();
						DefaultNetServiceManager.getInstance().offerHandler(handler);
					} else if (tempArgs.getStatus() == ECallbackStatus.Success) {
						Bundle results = new Bundle();
						results.putSerializable(
								DefineHelper.MESSAGE_KEY_RESULTS,
								new DefaultNetCallbackArgs(tempArgs.getPayload(), ECallbackStatus.Success, tempArgs
										.getErrorMessage()));
						doCallback(results, handler.getSavedReplyTo(), handler.getSavedCallback());

						handler.updateDelay(tempArgs.getInterval());
						// handler.getResult();

						DefaultNetServiceManager.getInstance().offerHandler(handler);
					} else {
						// Notification error cancel all.
						handler.cancel();
						DefaultNetServiceManager.getInstance().unregister(serviceType);
						Bundle results = new Bundle();
						results.putSerializable(DefineHelper.MESSAGE_KEY_RESULTS, new DefaultNetCallbackArgs(null,
								ECallbackStatus.Failure, tempArgs.getErrorMessage()));
						doCallback(results, handler.getSavedReplyTo(), handler.getSavedCallback());
					}
				}
			}

			super.handleMessage(msg);
		}
	};

	/**
	 * @uml.property name="netCallbackMessager"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Messenger netCallbackMessager = new Messenger(netCallbackHandler);

	public void doCallback(Bundle bundle, Messenger replyTo, INetCallback callback) {

		try {
			Message msg = new Message();
			msg.setData(bundle);
			msg.obj = callback;
			replyTo.send(msg);
		} catch (RemoteException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	@Override
	public void onCreate() {

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();

		// TODO:config for server url
		DefaultHttpConnectionFactory.getInstance().setUrl(DEFAULT_HOST_URL);

		if (ResourcesHelper.getInstance().get(ResourcesHelper.CACHED_KEY_CLIENT_BKS) == null) {
			ResourcesHelper.getInstance().put(ResourcesHelper.CACHED_KEY_CLIENT_BKS,
					getResources().openRawResource(R.raw.client));
			ResourcesHelper.getInstance().put(ResourcesHelper.CACHED_KEY_CLIENT_BKS_PWD, "123456");
		}

		LocalResourceManager.getInstance().init(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(this.getClass().getName(), "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

	}

	@Override
	public IBinder onBind(Intent intent) {
		return mServiceMessenger.getBinder();
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = getText(R.string.local_service_started);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.stat_sample, text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.local_service_label), text, contentIntent);

		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}

	/**
	 * @author hu_wg
	 */
	public enum EMessageType {
		Sync, Async, CancelAsync, RegisterNetService, UnregisterNetService, PauseNetService, ResumeNetService
	}

	/**
	 * @author hu_wg
	 */
	public enum ENotificatinType {
		Message, Warning, Error, Alert
	}

}
