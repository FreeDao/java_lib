/*****************************************************************************
 *
 *                      HOPERUN PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to HopeRun
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from HopeRun.
 *
 *            Copyright (c) 2012 by HopeRun.  All rights reserved.
 *
 *****************************************************************************/
package com.hoperun.telematics.mobile.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoperun.telematics.mobile.R;
import com.hoperun.telematics.mobile.framework.net.ENetworkServiceType;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallback;
import com.hoperun.telematics.mobile.framework.net.callback.INetCallbackArgs;
import com.hoperun.telematics.mobile.helper.CacheManager;
import com.hoperun.telematics.mobile.helper.DialogHelper;
import com.hoperun.telematics.mobile.helper.NetworkCallbackHelper;
import com.hoperun.telematics.mobile.model.remotecontrol.RemoteControlRequest;
import com.hoperun.telematics.mobile.model.remotecontrol.RemoteControlResponse;

/**
 * 
 * @author chen_guigui
 * 
 */
public class RemoteControlActivity extends DefaultActivity {
	private static final String TAG = "RemoteControlActivity";
	private static final int COMMAND_LOCK = 1;
	private static final int COMMAND_UNLOCK = 2;
	private static final int COMMAND_FORCE_UNLOCK = 3;
	private static final int COMMAND_LOCATE = 4;
	private static final int COMMAND_START = 5;
	private static final int COMMAND_CANCEL = 6;
	private static final String DESCRIPTION_LOCK = "lock";
	private static final String DESCRIPTION_UNLOCK = "unlock";
	private static final String DESCRIPTION_FORCE_UNLOCK = "forceUnlock";
	private static final String DESCRIPTION_LOCATE = "locate";
	private static final String DESCRIPTION_START = "start";
	private static final String DESCRIPTION_CANCEL = "cancel";
	private static final int TIME_INTERVAL = 250; // millisecond
	private static final int DELAY_TIME_INTERVAL = 200; // millisecond
	private static final int HANDLER_MESSAGE_HIDDEN = 1;
	private static final int HANDLER_MESSAGE_UPDATE = 2;
	private RelativeLayout mUnlockLayout;
	private RelativeLayout mLockLayout;
	private RelativeLayout mStartLayout;
	private RelativeLayout mLocateLayout;
	private RelativeLayout mProgressLayout;
	private TextView mUnlock;
	private TextView mLock;
	private TextView mStart;
	private TextView mLocate;
	private ImageView mUnlockImage;
	private ImageView mLockImage;
	private ImageView mStartImage;
	private ImageView mLocateImage;
	private boolean mShowProgress = false;
	private Button mCancel;
	private int mProgressBackgroundIndex = 0;
	private int[] mProgressBackgroundArray = new int[] { R.drawable.remote_round_one, R.drawable.remote_round_two,
	        R.drawable.remote_round_three, R.drawable.remote_round_four, R.drawable.remote_round_five,
	        R.drawable.remote_round_six, R.drawable.remote_round_seven, R.drawable.remote_round_eight };
	private int mCommand;
	private String mDescription;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_remote_control_layout);
		setTitleBar(this, getResources().getString(R.string.remote_control));

		mUnlockLayout = (RelativeLayout) findViewById(R.id.unlock_layout);
		mLockLayout = (RelativeLayout) findViewById(R.id.lock_layout);
		mStartLayout = (RelativeLayout) findViewById(R.id.start_layout);
		mLocateLayout = (RelativeLayout) findViewById(R.id.locate_layout);

		mProgressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
		mCancel = (Button) findViewById(R.id.cancel);

		mUnlock = (TextView) findViewById(R.id.unlock);
		mLock = (TextView) findViewById(R.id.lock);
		mStart = (TextView) findViewById(R.id.start);
		mLocate = (TextView) findViewById(R.id.locate);

		mUnlockImage = (ImageView) findViewById(R.id.unlock_image);
		mLockImage = (ImageView) findViewById(R.id.lock_image);
		mStartImage = (ImageView) findViewById(R.id.start_image);
		mLocateImage = (ImageView) findViewById(R.id.locate_image);

		mUnlockLayout.setOnClickListener(mRemoteControlListener);
		mLockLayout.setOnClickListener(mRemoteControlListener);
		mStartLayout.setOnClickListener(mRemoteControlListener);
		mLocateLayout.setOnClickListener(mRemoteControlListener);

		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mShowProgress = false;
				cancelAsyncMessage(ENetworkServiceType.RemoteControl);
			}
		});
	}

	private View.OnClickListener mRemoteControlListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.unlock_layout:
				mCommand = COMMAND_UNLOCK;
				mDescription = DESCRIPTION_UNLOCK;
				mUnlockLayout.setBackgroundResource(R.drawable.remote_control_pressed);
				mUnlockImage.setImageResource(R.drawable.unlock_pressed);
				mUnlock.setTextColor(R.color.white);
				break;
			case R.id.lock_layout:
				mCommand = COMMAND_LOCK;
				mDescription = DESCRIPTION_LOCK;
				mLockLayout.setBackgroundResource(R.drawable.remote_control_pressed);
				mLockImage.setImageResource(R.drawable.lock_pressed);
				mLock.setTextColor(R.color.white);
				break;
			case R.id.start_layout:
				mCommand = COMMAND_START;
				mDescription = DESCRIPTION_START;
				mStartLayout.setBackgroundResource(R.drawable.remote_control_pressed);
				mStartImage.setImageResource(R.drawable.start_pressed);
				mStart.setTextColor(R.color.white);
				break;
			case R.id.locate_layout:
				mCommand = COMMAND_LOCATE;
				mDescription = DESCRIPTION_LOCATE;
				mLocateLayout.setBackgroundResource(R.drawable.remote_control_pressed);
				mLocateImage.setImageResource(R.drawable.locate_pressed);
				mLocate.setTextColor(R.color.white);
				break;
			}
			doRemoteControl();
		}
	};

	/**
	 * Execute remote control command and send request to server.
	 * 
	 * @param command
	 */
	private void executeRemoteControl(int commandId, String description) {
		RemoteControlRequest remoteControlRequest = new RemoteControlRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		remoteControlRequest.setVin("1G1BL52P7TR115520");
		remoteControlRequest.setLicense("京NEB810");
		remoteControlRequest.setCommandId(commandId);
		Log.i(TAG, "The send command is " + commandId);
		remoteControlRequest.setDescription(description);
		Gson gson = new Gson();
		String requestStr = gson.toJson(remoteControlRequest);

		// send remote control command
		sendAsyncMessage(ENetworkServiceType.RemoteControl, requestStr, new INetCallback() {
			@Override
			public void callback(INetCallbackArgs args) {
				// The retry listener
				DialogInterface.OnClickListener onRetryClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doRemoteControl();
					}
				};
				// ------ Judge the business error
				if (!NetworkCallbackHelper.haveSystemError(RemoteControlActivity.this, args.getStatus())) {
					mShowProgress = false;
					String payload = args.getPayload();
					if (payload == null || payload.trim().length() == 0) {
						DialogHelper.alertDialog(RemoteControlActivity.this, R.string.error_empty_payload, R.string.ok);
					} else {
						// 在 payload 不为空和空字符串的情况下，根据model的不同情况解析json为model
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
						RemoteControlResponse response = gson.fromJson(payload, RemoteControlResponse.class);
						if (NetworkCallbackHelper.isErrorResponse(RemoteControlActivity.this, response)) {
							// 当返回的信息为异常提示信息的时候，判断异常类型并弹出提示对话框
							NetworkCallbackHelper.alertBusinessError(RemoteControlActivity.this,
							        response.getErrorCode());
						} else {
							// 当返回的信息为正常的数据时，则按业务逻辑正常处理和显示数据
							handleResponse(response);
						}
					}
				} else {
					// 根据各接口情况选择提示信息
					String errMsg = args.getErrorMessage();
					if (errMsg == null || errMsg.trim().length() == 0) {
						errMsg = getString(R.string.error_async_return_fault);
					}
					// 根据实际情况选择弹出的警告对话框类型或直接重试
					startReload(errMsg, onRetryClickListener);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hoperun.telematics.mobile.activity.DefaultActivity#reload()
	 */
	@Override
	protected void reload() {
		executeRemoteControl(mCommand, mDescription);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hoperun.telematics.mobile.framework.BaseActivity#stopProgressDialog()
	 */
	@Override
	public void stopProgressDialog() {
		mShowProgress = false;
	}

	public void doRemoteControl() {
		setAllLayoutEnable(false);
		startProgress();
		executeRemoteControl(mCommand, mDescription);
	}

	private void handleResponse(RemoteControlResponse remoteControlResponse) {
		int command = remoteControlResponse.getStateId();
		if (command == mCommand) {
			DialogHelper.alertDialog(this, R.string.remote_control, R.string.remote_control_success, R.string.ok);
		} else {
			DialogHelper.alertDialog(this, R.string.remote_control, R.string.remote_control_failed, R.string.ok);
		}
		Log.i(TAG, "The received command is " + command);
	}

	private void startProgress() {
		mShowProgress = true;
		mProgressLayout.setVisibility(View.VISIBLE);
		new Thread(mProgressRunnable).start();
	}

	private Runnable mProgressRunnable = new Runnable() {
		@Override
		public void run() {
			mProgressBackgroundIndex = 0;
			while (mShowProgress) {
				try {
					Message msg = mHandler.obtainMessage();
					msg.what = HANDLER_MESSAGE_UPDATE;
					mHandler.sendMessage(msg);
					Thread.sleep(TIME_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mProgressBackgroundIndex = (mProgressBackgroundIndex + 1) % mProgressBackgroundArray.length;
			}
			Message msg = mHandler.obtainMessage();
			msg.what = HANDLER_MESSAGE_HIDDEN;
			mHandler.sendMessage(msg);
		}
	};

	private void setAllLayoutEnable(boolean enabled) {
		mUnlockLayout.setEnabled(enabled);
		mLockLayout.setEnabled(enabled);
		mStartLayout.setEnabled(enabled);
		mLocateLayout.setEnabled(enabled);
	}

	private void setAllNormal() {
		mUnlockLayout.setBackgroundResource(R.drawable.remote_control_normal);
		mLockLayout.setBackgroundResource(R.drawable.remote_control_normal);
		mStartLayout.setBackgroundResource(R.drawable.remote_control_normal);
		mLocateLayout.setBackgroundResource(R.drawable.remote_control_normal);
		mUnlockImage.setImageResource(R.drawable.unlock_normal);
		mLockImage.setImageResource(R.drawable.lock_normal);
		mStartImage.setImageResource(R.drawable.start_normal);
		mLocateImage.setImageResource(R.drawable.locate_normal);
		mUnlock.setTextColor(R.color.black);
		mLock.setTextColor(R.color.black);
		mStart.setTextColor(R.color.black);
		mLocate.setTextColor(R.color.black);
	}

	private Handler mHandler = new Handler() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_MESSAGE_UPDATE:
				mProgressLayout.setBackgroundResource(mProgressBackgroundArray[mProgressBackgroundIndex]);
				break;
			case HANDLER_MESSAGE_HIDDEN:
				mProgressLayout.setVisibility(View.GONE);
				mProgressLayout.setBackgroundResource(mProgressBackgroundArray[0]);
				setAllLayoutEnable(true);
				setAllNormal();
				break;
			}
		}
	};
}
