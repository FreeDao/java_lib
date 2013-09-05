package com.sam.sfspcopy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.airbiquity.android.sfsp.service.WebviewService;

public class AppHostMain extends Activity implements OnInitListener{ // TTS 作用
	private static final String TAG = "AppHostMain";
	private TextToSpeech mTts;
	private static final int REQ_TTS_STATUS_CHECK = 0;
	private WebView mWebview;
	private JavaScriptInterface mJsInterface;
	WebviewService webviewService;
	private static final int VERSION_MAJOR = 0;
	private static final int VERSION_MINOR = 0;
	private static final int VERSION_BUILD = 0;
	public static final boolean isLock = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AqLog.getInstance().info("ICS_VERSION: " + AppHostMain.getIcsVersion() );
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.activity_app_host_main);
		mWebview = (WebView) findViewById(R.id.webView1);
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		// Disable long click event for text copy
		mWebview.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		//设置webview 属性
		mJsInterface = new JavaScriptInterface(getApplicationContext());
		mWebview.addJavascriptInterface(mJsInterface, "Android");
		mWebview.setWebViewClient(new WebViewClient());
		mWebview.loadUrl("file:///android_asset/index.html");
		
		//绑定服务
		Intent serviceIntent = new Intent(this, WebviewService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
		// 启动Message 传递
        try {
			CMessageDispatcher.startup(getApplicationContext(), "com.airbiquity.android.choreofleetmessaging.NetServerBroker", null);
		} catch (AlreadyStartedException e) {
			Log.e(TAG, "CMessageDispatcher exception: ", e);
		}
     // Check to be sure that TTS exists and is okay to use
     		Intent checkIntent = new Intent();
     		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
     		try {
     			startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
     		} catch (ActivityNotFoundException ex) {
     			ex.printStackTrace();
     			Log.d(this.getClass().getName(), ex.getMessage());
     		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, requestCode, data);
		if (requestCode == REQ_TTS_STATUS_CHECK) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
				// TTS is up and running
				mTts = new TextToSpeech(this, this);
				Log.v(TAG, "Pico is installed okay");
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
				// missing data, install it
				Log.v(TAG, "Need language stuff: " + resultCode);
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
			default:
				Log.e(TAG, "Got a failure. TTS apparently not available");
				break;
			}
		} else {
			// Got something else
		}
	}
	
	
	
	
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {

		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "--- Start webview ---");
		
		Intent intent = new Intent();
        intent.setAction(DaemonService.ACTION_DAEMON);
        intent.putExtra("cmd", DaemonService.CMD_USB_CONNECT);
        sendBroadcast(intent);// 发送广播
		
		if (isLock) {
			Log.d(TAG, "--- Start daemon service ---");
			startService(new Intent(this, DaemonService.class));
		} else {
			Log.d(TAG, "--- Stop daemon service ---");
			stopService(new Intent(this, DaemonService.class));
		}
	}
    
    @Override
    public void onStop() {
    	Log.d(TAG, "--- Stop webview ---");
    	
    	if (isLock) { // send broadcast to DaemonService for restart current activity
	    	Intent intent = new Intent();
	        intent.setAction(DaemonService.ACTION_DAEMON);
	        intent.putExtra("cmd", DaemonService.CMD_APP_GUARD);
	        sendBroadcast(intent);
    	}
    	
    	super.onStop();
    }

	@Override
	public void onDestroy() {	
		Log.d(TAG, "--- Destory webview ---");
		
		unbindService(mConnection);
		
		Intent intent = new Intent();
        intent.setAction(DaemonService.ACTION_DAEMON);
        intent.putExtra("cmd", DaemonService.CMD_APP_RESTART);
        sendBroadcast(intent);
		
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isLock) return true; // disable the system key event, such as "back"
		
		return super.onKeyDown(keyCode, event);
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {  
		@Override
        public void onServiceConnected(ComponentName className, IBinder localBinder) {  
			webviewService = ((LocalBinder) localBinder).getService(); 
			webviewService.setHandler(mHandler);
        }  
        
		@Override
		public void onServiceDisconnected(ComponentName arg0) {  
			webviewService = null;  
        }
    };
	
	
	// new handler 处理消息
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();

			String notifyType;
			String notifyContent = "";
			try { // For special character issue
				notifyContent = URLEncoder.encode(data.getString("json"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				
			}
			notifyContent = notifyContent.replaceAll("\\+", "%20");
			Log.d(TAG, "Notification type: "+ msg.what +", Notification content: " + notifyContent);

			switch (msg.what) {
				case IcsDirectorInterface.MSG_RESET:
					notifyType = "NOTIFY_TYPE_RESET";
					mWebview.loadUrl("javascript:util.notify('" + notifyType
							+ "', '" + notifyContent + "')");
					break;
				case IcsDirectorInterface.MSG_HANDLE_ALERT:
					//mWebview.loadUrl("");
					break;
				case IcsDirectorInterface.MSG_HANDLE_TEXT_MESSAGE:
					notifyType = "NOTIFY_TYPE_TEXT_MESSAGE";
					mWebview.loadUrl("javascript:util.notify('" + notifyType
							+ "', '" + notifyContent + "')");
					break;
				case IcsDirectorInterface.MSG_HANDLE_OBU_STATUS:
					notifyType = "NOTIFY_TYPE_OBU_STATUS";
					mWebview.loadUrl("javascript:util.notify('" + notifyType
							+ "', '" + notifyContent + "')");
					break;
				default:
					break;
			}
		}

	};
	
	
	
	public class JavaScriptInterface {
		private Context m_context;

		public JavaScriptInterface(Context c) {
			m_context = c;
		}

		/**
		 * Show short toast
		 * 
		 * @param String toast
		 */
		public void showToast(String toast) {
			Toast.makeText(m_context, toast, Toast.LENGTH_SHORT).show();
		}

		/**
		 * Show toast
		 * 
		 * @param String toast
		 * @param int duration
		 */
		public void showToast(String toast, int duration) {
			Toast.makeText(m_context, toast, duration).show();
		}

		/**
		 * Show tips
		 * @param String msg
		 */
		public void showTips(String msg) {
			AlertDialog.Builder builder = new Builder(AppHostMain.this);

			builder.setMessage(msg);
			builder.setTitle(m_context.getString(R.string.title_tips));

			builder.setNegativeButton(m_context.getString(R.string.ok), new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

		/**
		 * Show alert
		 * 
		 * @param String msg
		 * @param Strinh callback
		 */
		public void showAlert(String msg, final String callback) {
			AlertDialog.Builder builder = new Builder(AppHostMain.this);

			builder.setMessage(msg);
			builder.setTitle(m_context.getString(R.string.title_warn));

			builder.setPositiveButton(m_context.getString(R.string.ok), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (callback != null && !callback.isEmpty()) {
						mWebview.loadUrl("javascript:" + callback);
					}
				}
			});

			builder.setNegativeButton(m_context.getString(R.string.cancel), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

		/**
		 * Record log
		 * 
		 * @param String text
		 */
		public void log(String text) {
			Log.d(TAG, text);
		}

		// ------------ functions for HMI begin ------------

		/**
		 * Login with PIN
		 * 
		 * @param String request: {"PIN":"1234455"}
		 * @return String
		 */
		public String login(String request) {
			//showTips(mWebview.getWidth() + ", " + mWebview.getHeight());
			return webviewService.login(request);
		}

		/**
		 * Send tts command
		 * 
		 * @param text
		 */
		public void setTtsText(String text) {
			if (mTts == null) return;
			
			Log.v(TAG, "-----Text to speach called successed" + text);
			try {
				mTts.speak(text.toString(), TextToSpeech.QUEUE_ADD, null);
			} catch (Exception e) {
				e.printStackTrace();
				Log.v(TAG, "Text to speach called failed");
			}
		}
		
		/**
		 * Send message
		 * @param String request: {
				    "user_id": 1,
				    "send_to": "abc",
				    "subject": "abc",
				    "message": "abc"
				}
		 * @return String
		 */
		public String sendMessage(String request) {
			return webviewService.sendMessage(request);
		}

		/**
		 * Send message
		 * @param String request: {
				    "user_id": 1,
				    "message_id": "abc"
				}
		 * @return String
		 */
		public String deleteMessage(String request) {
			return webviewService.deleteMessage(request);
		}

		/**
		 * Set message status
		 * @param String request: {
				    "user_id": 1,
				    "message_id": "abc"
				}
		 * @return String
		 */
		public String setMessageStatus(String request) {
			Log.d(TAG, "----AppHostMain----setMessageStatus: " + request );
			return webviewService.setMessageStatus(request);
		}

		/**
		 * Get message
		 * @param String request: {"user_id" : ****}
		 * @return String
		 */
		public String getMessages(String request) {
			return webviewService.getMessages(request);
		}
		
		/**
		 * Get system setting
		 * @param String request: {"user_id" : ****}
		 * @return String
		 */
		public String getSetting(String request) {
			return webviewService.getSetting(request);
		}
		
		/**
		 * Set system setting
		 * @param String request
		 * @return String
		 */
		public String setSetting(String request) {
			return webviewService.setSetting(request);
		}
		
		/**
		 * User access EULA
		 * @param String request: {"user_id" : ****}
		 * @return String
		 */
		public String acceptEula(String request) {
			return webviewService.acceptEula(request);
		}
		
		/**
		 * Open third party navigation application
		 * @param String request
		 * @return String
		 */
		public void openNavigation(String request) {
			webviewService.openNavigation(request);
		}
		
		/**
		 * Cancel sounds notification if exists
		 */
		public void cancelSoundNotification() {
			webviewService.cancelSoundNotification();
		}

		// ------------ functions for HMI end ------------
	}
	
	public static String getIcsVersion()
	{
		String s = AqUtils.rightString("00" + Integer.toString( AppHostMain.VERSION_MAJOR ), 2) 
				 + AqUtils.rightString("00" + Integer.toString( AppHostMain.VERSION_MINOR ), 2) 
		         + AqUtils.rightString("0000" + Integer.toString( AppHostMain.VERSION_BUILD ), 4);
		return s;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
