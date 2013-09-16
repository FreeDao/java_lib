package com.hoperun.telematics.mobile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.BaseActivity;
import com.hoperun.telematics.mobile.framework.location.ILocationEventArgs;
import com.hoperun.telematics.mobile.framework.location.ILocationListener;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;

public class AndroidClientActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(button1OnClickListener);
		button = (Button) findViewById(R.id.button2);
		button.setOnClickListener(button5OnClickListener);
		button = (Button) findViewById(R.id.button3);
		button.setOnClickListener(button6OnClickListener);
		button = (Button) findViewById(R.id.button4);
		button.setOnClickListener(button7OnClickListener);
		
		startLocationService();
	}

	private OnClickListener button1OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String jsonString = "{\"UUID\":\"sss\", \"token\":\"ssss\",\"timestamp\":1111111,\"payload\":\"ssdasdasdasd\"}";
			sendSyncMessage(ENetworkServiceType.Login, jsonString, new INetCallback() {
				@Override
				public void callback(INetCallbackArgs args) {
					if (args.getStatus() == ECallbackStatus.Success) {
						Log.i(this.getClass().getName(), args.getPayload().toString());

					} else {
						Log.e(this.getClass().getName(), args.getErrorMessage());
					}
				}
			});
		}
	};

	private OnClickListener button2OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String jsonString = "{\"UUID\":\"sss\", \"token\":\"ssss\",\"timestamp\":1111111,\"payload\":\"ssdasdasdasd\"}";
			sendAsyncMessage(ENetworkServiceType.TestAsync, jsonString, new INetCallback() {
				@Override
				public void callback(INetCallbackArgs args) {
					if (args.getStatus() == ECallbackStatus.Success) {
						Log.i(this.getClass().getName(), args.getPayload().toString());

					} else {
						Log.e(this.getClass().getName(), args.getErrorMessage());
					}
				}
			});
		}
	};

	private OnClickListener button3OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			registerNetService(ENetworkServiceType.Notification, new INetCallback() {
				@Override
				public void callback(INetCallbackArgs args) {
					if (args.getStatus() == ECallbackStatus.Success) {
						Log.i(this.getClass().getName(), args.getPayload().toString());

					} else {
						Log.e(this.getClass().getName(), args.getErrorMessage());
					}
				}
			});
		}
	};

	private OnClickListener button4OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			unregisterNetService(ENetworkServiceType.Notification);
		}
	};
	private OnClickListener button5OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getLastLocation(new ILocationListener() {

				@Override
				public void callback(ILocationEventArgs args) {
					Log.d(this.getClass().getName(), args.getLocation().toString());
					args.getLocationType();
					args.getLocationStatus();
				}
			});
		}
	};
	private OnClickListener button6OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			registerLocationListener(new ILocationListener() {

				@Override
				public void callback(ILocationEventArgs args) {
					Log.d(this.getClass().getName(), args.getLocation().toString());
				}
			});
		}
	};
	private OnClickListener button7OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			unregisterLocationListener();
		}
	};
}