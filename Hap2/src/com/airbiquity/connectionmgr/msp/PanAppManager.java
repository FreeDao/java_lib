/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *
 *****************************************************************************/
package com.airbiquity.connectionmgr.msp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import com.airbiquity.application.manager.HandsetProfileManager;
import com.airbiquity.application.model.HandsetProfile;
import com.airbiquity.audio.PcmSpeechToTextWorker;
import com.airbiquity.audio.SpxTextToSpeechPlayer;
import com.airbiquity.audio.Utils;
import com.airbiquity.connectionmgr.util.MspByteUtil;
import com.airbiquity.connectionmgr.util.MyZip;
import com.airbiquity.hap.A;
import com.airbiquity.mcs.common.AbstractMcsLayer;
import com.airbiquity.mcs.common.McsLayerListener;
import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.HttpUtil;
import com.airbiquity.util_net.JsonMaker;
import com.airbiquity.util_net.NetReq;
import com.airbiquity.util_net.NetResp;

/**
 * This layer sits on top of MspLayer and talks to Panasonic HU Apps that are
 * implemented in JavaScript and run in WebView inside the Agent.
 */
//@SuppressLint("SetJavaScriptEnabled")
public class PanAppManager extends AbstractMcsLayer implements McsLayerListener {
	private static final String TAG = "PanAppManager";
	private MspLayer lowerLayer; // Lower layer.

	// private JsToJava js2java;
	private static final String PREFS_JS = "refs_js"; // Preferences for
														// Javascript.
	private final SharedPreferences prefs; // Shared Preferences for reading.
	private final Editor prefsEd; // Shared Preferences for writing.
	private WebView mWebView;

	private boolean audioPermited;

	private boolean ttsAvailable;
	private boolean ttsOnBoard;

	private MspAudioMgr mAudioMgr;
	private ExecutorService threadPool;
	private PanLongPollingWorker mPanLongPollingWorker = null;
	private SpxTextToSpeechPlayer ttsWorker;
	private ArrayList<SpxTextToSpeechPlayer> ttsPlayers = new ArrayList<SpxTextToSpeechPlayer>();
	private PcmSpeechToTextWorker sttWorker;
	private ArrayList<PcmSpeechToTextWorker> sttWorkers = new ArrayList<PcmSpeechToTextWorker>();

	/** Handler to run JS on UI thread. */
	private Handler h;

	public static ConcurrentHashMap<String,byte[]> imageMap = new ConcurrentHashMap<String,byte[]>(); 
	
	public static ConcurrentHashMap<Integer,String> messageMap = new ConcurrentHashMap<Integer,String>(); 

	/**
	 * Constructor. (Must be called on UI thread)
	 */
	public PanAppManager() {
		h = new Handler();
		A.a().webView.getSettings().setJavaScriptEnabled(true);
		prefs = A.getContext().getSharedPreferences(PREFS_JS,
				Context.MODE_PRIVATE);
		prefsEd = prefs.edit();
		mWebView = A.a().webView;
		A.a().webView.addJavascriptInterface(this, "android");
		A.a().webView.setWebChromeClient(new ChromeClient());

		mAudioMgr = new MspAudioMgr();

	}

	// /////////////////////////// +++++++++++++++++++++++++++++++++++++

	/**
	 * Permanently saves a String value. (The value will persist until the app
	 * is uninstalled)
	 * 
	 * @param key
	 *            : The name of the preference to modify.
	 * @param value
	 *            : The new value for the preference.
	 */
	public void putString(String key, String value) {
		Log.i(key, value);

		prefsEd.putString(key, value);
		prefsEd.commit();
	}

	/**
	 * Retrieve a previously saved String value.
	 * 
	 * @param key
	 *            : The name of the preference to retrieve.
	 * @param defValue
	 *            : Value to return if this preference does not exist.
	 * @return Returns the value if it exists, or defValue otherwise.
	 */
	public String getString(String key, String defValue) {
		return prefs.getString(key, defValue);
	}

	/**
	 * Get MIP Profile.
	 * 
	 * @return MIP Profile as a JSON string.
	 * @deprecated Use getHandsetProfile
	 */
	public String getMipProfile() {
		return getHandsetProfile();
	}

	/**
	 * Get Handset Profile.
	 * 
	 * @return Handset Profile in JSON format
	 * TODO: define JSON format
	 */
	public String getHandsetProfile() {
		HandsetProfile hp = HandsetProfileManager.getHandsetProfile();
		return hp.toJson().toString();
	}
	
	/**
	 * Check if this is the 1st time we show a screen after connection to HU is
	 * made.
	 * 
	 * @return true if so.
	 */
	public boolean isFirstStart() {
		boolean res = A.a().isPanHuJustConnected;
		A.a().isPanHuJustConnected = false;
		return res;
	}

	/**
	 * Returns last known location.
	 * 
	 * @return handset location in JSON format
	 * TODO: define JSON format
	 */
	public String getLocation() {
		return JsonMaker.getLocation();
	}

	/**
	 * Starts performing Text to Speech (TTS) on the specified text, and
	 * buffering the audio.
	 * 
	 * @param text
	 *            : text to perform TTS on.
	 * @param language
	 *            : language of the specified text.
	 * @see Panasonic Message Specification MIP_TTS.request message definition
	 *      for acceptable "language" values.
	 * @return ID of this TTS request or -1 on error.
	 */
	public int processTts(String text, int language) {
		if (null == lowerLayer)
			return -1;

		// if( !ttsAvailable ) // If TTS availability is not confirmed yet -
		// {
		// // send request to HU.
		// byte[] payload = new byte[] { 1, (byte) language, 1, 0 };
		// MspMsg msgResp = new MspMsg(payload, MspConst.TTS_REQUEST, (short)
		// 0);
		// lowerLayer.writeData(msgResp);
		// }

		// TODO: get IDs for other supported languages.
		String[] langs = { "en_US", "fr_CA" };
		if (language >= langs.length) {
			Log.e(TAG, "Invalid language code: " + language);
			// TODO send tts request to HU(on board)
			return -1;
		}

		String lang = langs[language];

		// TODO stop stream audio, check vr too.
		// if( ttsWorker != null && ttsWorker.isPlaying()){
		mAudioMgr.sendTtsRequet(1, language, 1);
		// }

		// TODO we need to take care of onBoard tts here
		ttsWorker = new SpxTextToSpeechPlayer(text, lang);
		ttsPlayers.add(ttsWorker);
		ttsWorker.setTtsId(ttsPlayers.indexOf(ttsWorker));
		ttsWorker.setAudioMgr(mAudioMgr);
		// threadPool.execute(ttsWorker);

		return ttsPlayers.indexOf(ttsWorker);
	}

	/**
	 * Play or resume TTS playback that was previously paused by calling
	 * ttsPause(ttsId)
	 * 
	 * @param ttsId
	 *            : ID of TTS request.
	 * @return true on OK.
	 */
	public boolean playTts(int ttsId) {
		if (null == lowerLayer)
			return false;

		// if( !ttsAvailable )
		// return false;
		//
		// if(audioPermited)
		// {
		// byte[] payload = new byte[] { 1, 0 };
		// MspMsg msgResp = new MspMsg( payload, MspConst.AUDIO_INDICATION,
		// (short) 0 );
		// lowerLayer.writeData( msgResp );
		// }

		mAudioMgr.sendStreamAudioRequest(1);

		// TODO we need to handle audio is not permited here
		// SpxTextToSpeechPlayer ttsPlayer = ttsPlayers.get(ttsId);
		// if(!ttsPlayer.isPlaying()){
		// return ttsPlayer.setPlaying(true);
		// }else{
		// return false;
		// }
		ttsWorker = ttsPlayers.get(ttsId);

		if (ttsWorker != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Pauses TTS playback. Can be resumed later by calling playTts(ttsId)
	 * 
	 * @param ttsId
	 *            : ID of TTS request.
	 * @return TBD.
	 */
	public boolean pauseTts(int ttsId) {
		SpxTextToSpeechPlayer ttsPlayer = ttsPlayers.get(ttsId);
		return (!ttsPlayer.isPausing()) ? ttsPlayer.setPausing(true) : false;
	}

	/**
	 * Pauses TTS playback. Can be resumed later by calling playTts(ttsId)
	 * 
	 * @param ttsId
	 *            : ID of TTS request.
	 * @return TBD.
	 */
	public boolean resumeTts(int ttsId) {
		SpxTextToSpeechPlayer ttsPlayer = ttsPlayers.get(ttsId);
		return (ttsPlayer.isPausing()) ? ttsPlayer.setPausing(false) : false;
	}

	/**
	 * Stops Text to Speech (TTS) for the specified ID and clears the buffer.
	 * 
	 * @param ttsId
	 *            : ID of the TTS to stop.
	 * @return true if the TTS was successfully stopped, otherwise false.
	 */
	public boolean stopTts(int ttsId) {
		if (ttsPlayers.size() > 0) {
			mAudioMgr.sendStreamAudioRequest(2);
			SpxTextToSpeechPlayer ttsPlayer = ttsPlayers.get(ttsId);
			ttsPlayer.setPlaying(false);
			return ttsPlayers.remove(ttsPlayer);
		} else {
			return false;
		}
	}

	/**
	 * send command control message to nomadic application.
	 * 
	 * @param appName
	 * @param contentType
	 * @param contentTransferEncoding
	 * @param content
	 * @return
	 */
	public int sendAppRequest(final String appName, final String contentType,
			final String contentTransferEncoding, final String content) {

		int seqNum = IdGenerator.getInstance().generateId();
		PanCommandControlWorker commandControlWoker = new PanCommandControlWorker(seqNum, appName, contentType, contentTransferEncoding, content);
		threadPool.execute(commandControlWoker);

		return seqNum;
	}
	
	/**
	 * Retrieves a MIP Enabled Handset Application message from the Native Layer.
	 * @param id  ID of the MIP Enabled Handset Application message to retrieve
	 * @return  Content of the MIP Enabled Handset Application message if it exists, otherwise null.
	 */
	public String getAppMessage(final int id){
		return messageMap.remove(id);
	}


	/**
	 * 
	 * @param language
	 * @return
	 */
	public int startVR(int language) {

		// TODO: get IDs for other supported languages.
		String[] langs = { "en_US", "fr_CA" };
		if (language >= langs.length) {
			Log.e(TAG, "Invalid language code: " + language);
			return -1;
		}
		String lang = langs[language];
		
		//TODO if stream audio is playing, send stream audio stop
		//TODO if on board tts is playing, send on-board tts end
		//TODO if off board tts is playing, send off board tts end
		

		sttWorker = new PcmSpeechToTextWorker(lang);
		sttWorkers.add(sttWorker);
		sttWorker.setVrId(sttWorkers.indexOf(sttWorker));
		sttWorker.setAudioMgr(mAudioMgr);
		
		//send off board vr request to HU
		mAudioMgr.sendOffBoardVrRequest(1);

		return sttWorkers.indexOf(sttWorker);
	}

	/**
	 * 
	 * @param vrId
	 * @return
	 */
	public boolean stopVr(int vrId) {
		// TODO OFF board VR end
		mAudioMgr.sendOffBoardVrRequest(2);
		PcmSpeechToTextWorker sttWorker = sttWorkers.get(vrId);
		
		return sttWorker.setProcessing(false);
	}

	/**
	 * Sends a command to the head unit to control the streaming of audio.
	 * 
	 * @param operation 0x01 = start, 0x02 = end
	 * @return true if the command was successfully sent, otherwise false.
	 */
	public boolean sendStreamAudioRequest(int operation) {
		if (null == lowerLayer)
			return false;

		byte[] payload = new byte[] { (byte)operation, 0 };
		MspMsg msgResp = new MspMsg(payload, MspConst.AUDIO_INDICATION, (short) 0);
		lowerLayer.writeData(msgResp);
		return true;
	}
	
	/**
	 * Temp function for testing
	 */
	public void log(String response) {
		Log.i("JS_LOG", response);
	}

	/**
	 * Temp function for testing
	 */
	public void bin(String response) {
		Log.i("JS_BINARY", "binary length: " + response.length());
	}

	/**
	 * Handle response from javascript side
	 */
	public void response(String response) {
		mWebView.loadUrl("javascript:display('Java side response length: "
				+ response.length() + "');");
		Log.i("JS_RESPONSE", response);
	}

	// /////////////////////////// -------------------------------------


	/**
	 * Load WebView URL on UI thread.
	 * 
	 * @param url
	 *            : URL to load.
	 */
	void loadUrlOnUiThread(final String url) {
		Runnable r = new Runnable() {
			public void run() {
				A.a().webView.loadUrl(url);
			}
		};
		h.post(r);
	}

	/**
	 * Send response to HU providing image for the requested ID.
	 * 
	 * @param idApp
	 *            : App ID.
	 * @param idImg
	 *            : Image ID.
	 * @param sizeX
	 *            : image width.
	 * @param sizeY
	 *            : image height.
	 * @param urlImage
	 *            : Image URL.
	 * @deprecated use sendImageToHeadUnit()
	 */
	public void updateImage(int idApp, int idImg, int sizeX, int sizeY, String urlImage) {
		// Log.i("sendImage_DATA",
		// "App ID: "+idApp+"; Img ID: "+idImg+"; URL: "+urlImage+"; Width: "+sizeX+"; Height: "+sizeY);

		if (null == lowerLayer)
			return;

		byte[] payload = new byte[0];

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);

			byte[] bufImgOrig;
			
			
			if (urlImage.startsWith("http")) {
				URL url = new URL(urlImage);
				NetReq req = new NetReq(url, null, HttpHeader.CONTENT_BIN);
				NetResp resp = HttpUtil.com(req);
				if (200 != resp.code){
					//throw new IllegalArgumentException("RespCode=" + resp.code);
					//TODO return error image
					InputStream is = A.a().getAssets().open("images/error.png");
					Bitmap map = BitmapFactory.decodeStream(is);
					bufImgOrig = bitmapToBytes(map);
				}else{
					bufImgOrig = resp.data;
				}
			}else if(urlImage.indexOf("file:///android_asset/") != -1){
				//load resource from assets
				InputStream is = A.a().getAssets().open(urlImage.substring(22));
				Bitmap map = BitmapFactory.decodeStream(is);
				bufImgOrig = bitmapToBytes(map);
				
			}else if(urlImage.startsWith("hap://")){
				//bufImgOrig = Base64.decode(imageMap.get(urlImage),Base64.DEFAULT);
				bufImgOrig = imageMap.get(urlImage);
				
			} else{
				bufImgOrig = Base64.decode(urlImage, Base64.DEFAULT);
			}

			byte[] bufImgResized = resizeImg(bufImgOrig, 0, bufImgOrig.length, sizeX, sizeY);
			dos.writeShort(idApp);
			dos.writeShort(idImg);
			dos.writeInt(bufImgResized.length);
			dos.write(bufImgResized);
			payload = bos.toByteArray();
			dos.close();
			bos.close();
			// Log.d( TAG,
			// "Image sent to HU sz="+msg.payload.length+" pid="+Thread.currentThread().getId()
			// );
		} catch (Exception e) {
			Log.e(TAG, "URL:" + urlImage, e);
		}

		MspMsg msg = new MspMsg(payload, MspConst.DISPLAY_IMAGE_DATA_RSP,
				(short) 0);
		lowerLayer.writeData(msg);
	}

	/**
	 * Sends the specified image to the head unit, resizing prior to sending if necessary.
	 * 
	 */
	private boolean sendImageToHeadUnit(String params) {
		// TODO: implement me
		return false;
	}
	
	/**
	 * Covert a bitmap to byte array.
	 * @param bitmap bitmap that load from android
	 * @return the byte array of the image
	 */
	private byte[] bitmapToBytes(Bitmap bitmap) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				baos.flush();
				baos.close();
				baos.toByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  baos.toByteArray();
	}

	/**
	 * Resize given image, convert to PNG, and return as byte array.
	 * 
	 * @param bufIn
	 * @param offset
	 * @param length
	 * @param sizeX
	 * @param sizeY
	 * @return resized image in PNG format in byte array.
	 * @throws IOException
	 */
	private byte[] resizeImg(byte[] bufIn, int offset, int length, int sizeX,
			int sizeY) throws IOException {
		Bitmap bmpIn = BitmapFactory.decodeByteArray(bufIn, offset, length);
		Bitmap bmpOut = Bitmap.createScaledBitmap(bmpIn, sizeX, sizeY, true);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bufIn.length);
		bmpOut.compress(Bitmap.CompressFormat.PNG, 90, bos);
		byte[] bufOut = bos.toByteArray();
		bos.close();
		// Log.d( TAG,
		// "Image resized: "+bmpIn.getWidth()+"x"+bmpIn.getHeight()+" -> "+sizeX+"x"+sizeY+
		// " = "+bmpOut.getWidth()+"x"+bmpOut.getHeight() );
		return bufOut;
	}

	/**
	 * Send response to HU providing text for requested IDs.
	 * 
	 * @param idApp
	 *            : App ID.
	 * @param pairs
	 *            : JSON object containing pairs: ID (key) and Text (value).
	 */
	public void updateText(int idApp, String pairs) {
		if (null == lowerLayer)
			return;
		try {
			JSONObject ja = new JSONObject(pairs);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(
					4 * ja.length());
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeShort(idApp);
			Iterator<?> keys = ja.keys();
			final int textQntt = ja.length();
			dos.writeShort(textQntt);

			while (keys.hasNext()) {
				String key = (String) keys.next();
				String text = (String) ja.get(key); // TODO: we get cast
													// exception here.
				short id = Short.valueOf(key);
				dos.writeShort(id);
				byte[] binText = text.getBytes("UTF-8");
				dos.writeShort(binText.length);
				dos.write(binText);
			}
			byte[] payload = bos.toByteArray();
			dos.close();
			bos.close();
			MspMsg msg = new MspMsg(payload, MspConst.DISPLAY_TEXT_DATA_RSP,
					(short) 0);
			lowerLayer.writeData(msg);
		} catch (Exception e) {
			Log.e(TAG, " ", e);
		}
	}

	/**
	 * Sends screen update message to HU.
	 * 
	 * @param content
	 *            : base 64 encoded content of the screen.
	 */
	public void setHupScreen(String content) {
		// Log.d( TAG, "sendHupScreenUpdate: "+content.length() );
		if (null == lowerLayer)
			return;
		try {
			JSONArray ja = new JSONArray(content);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(
					4 * ja.length());
			DataOutputStream dos = new DataOutputStream(bos);

			for (int i = 0; i < ja.length();) {
				String s = ja.getString(i++);
				int v = Integer.valueOf(s, 16);
				dos.writeByte(v);
			}
			byte[] payload = bos.toByteArray();
			dos.close();
			bos.close();
			MspMsg msg = new MspMsg(payload,
					MspConst.DISPLAY_CHANGE_INDICATION, (short) 0);
			lowerLayer.writeData(msg);
		} catch (Exception e) {
			Log.e(TAG, " ", e);
		}
	}

	/**
	 * Sends the system screen message to the HU.
	 * 
	 * @param screenId : ID of the system screen to be displayed/erased.
	 * @param operation : operation to perform on the system screen.
	 */
	public boolean setSystemScreen(int screenId, int operation) {
		if (null == lowerLayer) {
			return false;
		}

		// construct message
		byte[] payload = new byte[] { (byte)screenId, (byte)operation };
		MspMsg msgResp = new MspMsg(payload, MspConst.DISPLAY_SYSTEM_SCREEN_INDICATION, (short) 0);

		// send message to head unit
		lowerLayer.writeData(msgResp);

		return true;
	}
	
	/**
	 * Handler for all messages from the head unit.
	 * @param msg : message from the head unit to be handled
	 */
	public void handleMessage(MspMsg msg) {
		short msgDef = msg.msgType;

		switch (msgDef) {
		case MspConst.DISPLAY_CHANGE_END_INDICATION:
			Log.d(TAG, "Handle Msg : DISPLAY_CHANGE_END_INDICATION");
			onDisplayChangeEnd(msg);
			break;
		case MspConst.EXIT_APPLICATION_MODE:
			Log.d(TAG, "Handle Msg : EXIT_APPLICATION_MODE");
			onExitAppMode(msg);
			break;
		case MspConst.HANDSFREE_DIAL_RSP:
			Log.d(TAG, "Handle Msg : HANDSFREE_DIAL_RSP");
			onHandsfreeDialResponse(msg);
			break;
		case MspConst.HANDSFREE_INTERRUPT_INDICATION:
			Log.d(TAG, "Handle Msg : HANDSFREE_INTERRUPT_INDICATION");
			onHandsfreeInterrupt(msg);
			break;
		case MspConst.HARD_KEY_OPERATION_INDICATION:
			Log.d(TAG, "Handle Msg : HARD_KEY_OPERATION_INDICATION");
			onClickBtnHard(msg);
			break;
		case MspConst.OFFBOARD_VR_RSP:
			Log.d(TAG, "Handle Msg : OFFBOARD_VR");
			onOffBoardVrResponse(msg);
			break;
		case MspConst.RESUME_APP:
			Log.d(TAG, "Handle Msg : RESUME_APP");
			onResumeApplication(msg);
			break;
		case MspConst.SET_LANGUAGE:
			Log.d(TAG, "Handle Msg : SET_LANGUAGE");
			onSetLanguage(msg);
			break;
		case MspConst.SOFT_KEY_OPERATION_INDICATION:
			Log.d(TAG, "Handle Msg : SOFT_KEY_OPERATION_INDICATION");
			onClickBtnSoft(msg);
			break;
		case MspConst.AUDIO_PERMIT_INDICATION:
			Log.d(TAG, "Handle Msg : STREAM_AUDIO_PERMITTED_INDICATION");
			onAudioPermit(msg);
			break;
		case MspConst.TTS_RESPONSE:
			Log.d(TAG, "Handle Msg : TTS_CONTROL_RSP");
			onTtsRsp(msg);
			break;
		case MspConst.DISPLAY_IMAGE_DATA:
			Log.d(TAG, "Handle Msg : DISPLAY_IMAGE_DATA");
			onGetImage(msg);
			break;
		case MspConst.DISPLAY_TEXT_DATA:
			Log.d(TAG, "Handle Msg : DISPLAY_TEXT_DATA");
			onGetTexts(msg);
			break;
		case MspConst.AUDIO_DATA_TX:
			Log.d(TAG, "Handle Msg : AUDIO_DATA_TX");
			onAudioDataReceive(msg);
			break;
		case MspConst.HEAD_UNIT_INFORMATION_INDICATION:
			Log.d(TAG, "Handle Msg : HEAD_UNIT_INFORMATION_INDICATION");
			onHeadUnitInformation(msg);
			break;
		case MspConst.TEMPLATE_UPDATE_RSP:
			Log.d(TAG, "Handle Msg : TEMPLATE_UPDATE_RSP");
			onTemplateUpdateResponse(msg);
			break;
		case MspConst.RESUME_AUDIO_APP:
			Log.d(TAG, "Handle Msg : RESUME_AUDIO_APP");
			onResumeAudioApplication(msg);
			break;
		default:
			Log.e(TAG,
					"Handle Msg : Unsupported message: "
							+ String.format("%04X", msgDef)); 
		}
	}


	/**
	 * handle audio data, basically for VR
	 * 
	 * @param msg
	 */
	private void onAudioDataReceive(MspMsg msg) {

		//TODO big endian or little endian?
		ByteBuffer bb = ByteBuffer.wrap(msg.payload).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer sb = bb.asShortBuffer();

		short[] s = Utils.convertByteArrayToShortArray(msg.payload,0,msg.payload.length);
		sttWorker.putData(s, s.length);
	}
	

	/**
	 * Handle off-board VR response.
	 * 
	 * @param msg : Response message from head unit.
	 */
	private void onOffBoardVrResponse(MspMsg msg) {
		
		// TODO check off board state
		// 0x00:VR request is available
		// 0x01:VR request is not available
		byte[] res = msg.payload;
		if(res[0] == 0){
			threadPool.execute(sttWorker);
			sttWorker.setProcessing(true);
		}
	}

	/**
	 * Process request from HU to set Language.
	 * 
	 * @param msgReq
	 *            : Request message.
	 */
	private void onSetLanguage(MspMsg msgReq) {
	
		final int LANGUAGE_ID_INDEX = 0;

		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "setLanguage" );
            reqJson.put( "languageId", (int) msgReq.payload[LANGUAGE_ID_INDEX]);
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }

		// send response to head unit
		if (null == lowerLayer)
			return;
		byte[] payload = new byte[] { 2, 0, 0, 0 };
		MspMsg msgResp = new MspMsg(payload, MspConst.SET_LANGUAGE_RSP,
				msgReq.tranId);
		lowerLayer.writeData(msgResp);
	}

	/**
	 * Handle HMI Hard Key Operation message from head unit.
	 * 
	 * @param msg
	 *            : binary message that contains all the parameters.
	 */
	private void onClickBtnHard(MspMsg msg) {
		// extract payload
		byte[] p = msg.payload;
		final int idScreen = MspByteUtil.getShort(p, 0);
		final int idKey = p[2] & 0xff;
		final int idListItem = p[3] & 0xff;
		
		// notify JavaScript
		String url = "javascript:hardKeyClick(" + idScreen + "," + idKey + ","
				+ idListItem + ")";
		loadUrlOnUiThread(url);
	}

	/**
	 * Handle HMI Operation message from head unit.
	 * 
	 * @param msg
	 *            : binary message that contains all the parameters.
	 */
	private void onClickBtnSoft(MspMsg msg) {
		// extract payload
		byte[] p = msg.payload;
		final int idScreen = MspByteUtil.getShort(p, 0);
		final int idKey = p[2] & 0xff;
		final int idListItem = p[3] & 0xff;
		
		// notify JavaScript
		String url = "javascript:softKeyClick(" + idScreen + "," + idKey + ","
				+ idListItem + ")";
		Log.i("SOFT_KEY_EVENT", "SOFT_KEY_CLICK: Screen ID: " + idScreen
				+ "; Key ID: " + idKey + "; List item ID: " + idListItem);

		loadUrlOnUiThread(url);
	}

	/**
	 * Process Audio Permit response from HU.
	 * TODO: move TTS handling logic to seprate class.
	 */
	private void onAudioPermit(MspMsg msg) {
		// extract payload
		byte[] p = msg.payload;
		audioPermited = 1 == p[0];
		Log.d(TAG, "audioPermited = " + audioPermited);

		// notify JavaScript
		loadUrlOnUiThread("javascript:streamingAudioStateChange(" + audioPermited + ")");

		// TODO: need to cleanup and play well with internet radio
		if (null != ttsWorker) {
			ttsWorker.setPlaying(true);
		}
	}

	/**
	 * Process TTS response from HU.
	 * TODO: move TTS handling logic to seprate class.
	 */
	private void onTtsRsp(MspMsg msg) {
		byte[] p = msg.payload;
		ttsAvailable = 0 == p[0];
		ttsOnBoard = 0 == p[1];

		Log.d(TAG, "ttsAvaliable = " + ttsAvailable);
		Log.d(TAG, "ttsOnBoard = " + ttsOnBoard);

		if (null != ttsWorker) {
			threadPool.execute(ttsWorker);
		}
	}

	/**
	 * Convert binary Image-Request message to JSON and forward it to JS.
	 * 
	 * @param msg
	 *            : binary message that contains ID of the image HU needs.
	 */
	private void onGetImage(MspMsg msg) {
		byte[] p = msg.payload;
		final int idApp = MspByteUtil.getShort(p, 0);
		final int idImg = MspByteUtil.getShort(p, 2);
		String url = "javascript:getImage(" + idApp + "," + idImg + ")";
		loadUrlOnUiThread(url);
		// Log.d( TAG, url+" pid="+Thread.currentThread().getId() );
	}

	/**
	 * Convert binary Text-Request message to JSON and forward it to JS.
	 * 
	 * @param msg
	 *            : binary message that contains IDs of the text fields for
	 *            which HU needs texts.
	 */
	private void onGetTexts(MspMsg msg) {
		// extract payload
		byte[] p = msg.payload;
		final int idApp = MspByteUtil.getShort(p, 0);
		final int idsQntt = MspByteUtil.getShort(p, 2);
		final int msg_sz = 4 + 2 * idsQntt;
		if (msg_sz != p.length) {
			Log.e(TAG, "Bad size " + msg_sz + "!=" + p.length);
			return;
		}
		
		// notify JavaScript
		StringBuilder sb = new StringBuilder("javascript:getText(");
		sb.append(idApp);
		sb.append(",'[");
		for (int i = 4; i < msg_sz; i += 2)
			sb.append(MspByteUtil.getShort(p, i) + ",");
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]')");
		String url = sb.toString();
		// Log.d( TAG, url );

		loadUrlOnUiThread(url);
	}

	// TODO: refactor sending of notifications to JavaScript
	// TODO: migrate older notifications to new API

	/**
	 * Handle MIP Exit Request.
	 * 
	 * @param msg
	 *            : binary message that contains all the parameters.
	 */
	private void onExitAppMode(MspMsg msg) {

		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "exit" );
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }

		// send response to head unit
		if (null == lowerLayer)
			return;
		byte[] msgRsp = MspSegment.makeEmptyMsg(msg.tranId,
				MspConst.EXIT_APPLICATION_MODE_RSP);
		lowerLayer.writeData(msgRsp, msgRsp.length);
	}

	/**
	 * Handle Head Unit Information indication message.
	 * 
	 * @param msg
	 * 				: binary message that contains all the parameters.
	 */
	private void onHeadUnitInformation(MspMsg msg) {

		final int METER_STATUS_INDEX = 19;

		// extract payload
		// TODO: extract head unit type
		// TODO: extract head unit serial number
		// TODO: extract template info
		final int meterStatus = (int)msg.payload[METER_STATUS_INDEX];

		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "headUnitInfo" );
            reqJson.put( "headUnitType", "DA2.2" );
            reqJson.put( "headUnitSerialNumber", "012345678901" );
            reqJson.put( "templateInfo", "" ); // TODO: array [A,B,C,...]
            reqJson.put( "isMeterAvailable", (1 == meterStatus) ? true : false );
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }

		// TODO: do whatever we're doing (activation check?) when non-DA head units connect
	}

	/**
	 * Handle Display Change End Indication message from head unit.
	 * @param msg : message from head unit
	 */
	private void onDisplayChangeEnd(MspMsg msg) {

		final int STATUS_INDEX = 0;

		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "hupScreenUpdated" );
            reqJson.put( "status", (int) msg.payload[STATUS_INDEX]);
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }
	}

	/**
	 * Handle Template Update Response message from head unit.
	 * @param msg : message from head unit
	 */
	private void onTemplateUpdateResponse(MspMsg msg) {

		final int STATUS_INDEX = 0;

		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "templateUpdated" );
            reqJson.put( "status", (int) msg.payload[STATUS_INDEX]);
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }
	}

	/**
	 * Handle Handsfree Dial Response message from head unit.
	 * @param msg : message from head unit
	 */
	private void onHandsfreeDialResponse(MspMsg msg) {
		final int STATUS_INDEX = 0;

		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "dialResponse" );
            reqJson.put( "status", (int) msg.payload[STATUS_INDEX]);
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }
	}

	/**
	 * Handle Handsfree Interrupt message from head unit.
	 * @param msg : message from head unit
	 */
	private void onHandsfreeInterrupt(MspMsg msg) {
		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "telephoneInterrupt" );
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }
	}
	
	/**
	 * Handle Resume Application Request message from head unit.
	 * @param msg : message from head unit
	 */
	private void onResumeApplication(MspMsg msg) {
		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "resumeApplication" );
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }
	}

	/**
	 * Handle Resume Audio Request message from head unit.
	 * @param msg : message from head unit
	 */
	private void onResumeAudioApplication(MspMsg msg) {
		// notify JavaScript
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "name", "resumeAudioApplication" );
            loadUrlOnUiThread("javascript:onNotification(" + reqJson.toString() + ")");
        }
        catch( JSONException e )
        {
            Log.e( TAG, "Exception sending notification to JavaScript", e );
        }
	}
	

	// -------------------------------------------------------------------------


	@Override
	public void onConnectionClosed() {
		// Do nothing
		Log.e(TAG, "Connection closed");

		mPanLongPollingWorker.setRunning(false);
		threadPool.shutdown();
		imageMap.clear();
		messageMap.clear();
	}

	// Process data from the lower layer.
	@Override
	public void onDataReceived() {
		MspMsg request = lowerLayer.readData();
		if (null == request)
			return;

		handleMessage(request);
	}

	/**
	 * Set the lower layer and start listening to it. If there is a lower layer
	 * set already - unset it first.
	 * 
	 * @param lower_layer
	 *            : the lower layer (aka data layer, aka transport layer)
	 */
	public void setLowerLayer(MspLayer lower_layer) {
		if (lowerLayer != null)
			lowerLayer.removeListener(this);

		lowerLayer = lower_layer;
		if (lowerLayer != null)
			lowerLayer.addListener(this);

		Log.e(TAG, "Connection established");

		mAudioMgr.setLayer(lower_layer);

		threadPool = Executors.newFixedThreadPool(10);
		mPanLongPollingWorker = new PanLongPollingWorker();
		threadPool.execute(mPanLongPollingWorker);
	}

	// There is no upper layer. This method should never get called.
	@Override
	public int readData(byte[] buffer, int size) {
		throw new UnsupportedOperationException("Upper layer not supported");
	}

	// There is no upper layer. This method should never get called.
	@Override
	public void writeData(byte[] buffer, int size) {
		throw new UnsupportedOperationException("Upper layer not supported");
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
