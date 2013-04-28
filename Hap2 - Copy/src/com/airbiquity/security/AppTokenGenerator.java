
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
 *                   @author 
 *
 *****************************************************************************/
package com.airbiquity.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

/**
 * Application Token Generator class for providing the Application token for
 * </br>
 * MimicTethering Authentication and Client Gateway request 
 * <font color="RED">"App-Token"</font> Http header value
 *
 */
public class AppTokenGenerator {
	
	private static final String TAG = AppTokenGenerator.class.getName();
	
	// Security
	private String securityPreSharedKey = null;
	private String securityVersionValue = null;
	private String securityType  = null; 
	
	private static AppTokenGenerator INSTANCE = null;
	
	private long timeDifference = 0l;
	
	private AppTokenGenerator(){
	};
	
	/**
	 * Call time sync explicitly to synchronize the time.
	 * <br>
	 * Do this at the very beginning of the Application start
	 */
	public void timeSync(){
		
		long choreoTime = TimeSynchronizer.getInstance().getBackendTime();
		long systemTime = System.currentTimeMillis();
		
		this.timeDifference = choreoTime - systemTime;
	}
	
	/**
	 * Singleton instance of the class
	 * 
	 * @return
	 */
	public synchronized static AppTokenGenerator getInstance(){
		if(null == INSTANCE){
			INSTANCE = new AppTokenGenerator();
		}
		return INSTANCE;
	}
	
	/**
	 * Get Application Token String for Choreo Proxy Server
	 * 
	 * @return The application token string
	 */
	public String getProxyAppToken() {
		String retVal = getAppToken(securityType, securityVersionValue, Base64.decode(securityPreSharedKey.getBytes(), Base64.DEFAULT));
		retVal = retVal.replace("\n", "");
		return retVal;
	}
	
	/**
	 * Get Application Token String for Https requests.
	 * @return
	 */
	public String getHttpsAppToken(){
		String retVal = getAppToken(SecurityConstants.HTTPS_TYPE, securityVersionValue,  Base64.decode(SecurityConstants.HTTPS_PRE_SHARED_KEY, Base64.DEFAULT));
		Log.e("TAG", "app token = "+retVal);
		retVal = retVal.replace("\n", "");
		return retVal;
	}

	private String getAppToken(String keyType, String keyVersion, byte[] keyValue) {
		try {
			
			Log.e("TAG", "app keyType = "+keyType);
			Log.e("TAG", "app keyVersion = "+keyVersion);
			Log.e("TAG", "app keyValue = "+new String(keyValue));
			
			SecretKeySpec keySpec = new SecretKeySpec(keyValue, SecurityConstants.HMAC_SHA256);
			String keyVersionParam = SecurityConstants.PRE_KEY_VERSION + keyVersion;
			String keyTypeParam = SecurityConstants.PRE_TYPE + keyType;
			long time = System.currentTimeMillis();
			
			

			// TODO : here can be more efficient
			// time = time - ( 8 * 60 * 60 * 1000); // Seattle time GMT -8
			time += this.timeDifference;

			String timeParam = SecurityConstants.PRE_TIME + time;
			
			Log.e("TAG", "app keyVersionParam = "+keyVersionParam);
			Log.e("TAG", "app keyTypeParam = "+keyTypeParam);
			Log.e("TAG", "app timeParam = "+timeParam);

			String parameters = keyVersionParam + SecurityConstants.PARAM_SEPERATOR + keyTypeParam + SecurityConstants.PARAM_SEPERATOR + timeParam;

			
			Log.e("TAG", "app parameters = "+parameters);
			Mac mac = Mac.getInstance(SecurityConstants.HMAC_SHA256);
			mac.init(keySpec);
			byte[] sha256 = mac.doFinal(parameters.getBytes(SecurityConstants.UTF_8));

//			char[] hexToken = Hex.encodeHex(sha256);
			char[] hexToken = bytesToHex(sha256);
			String hexTokenStr = new String(hexToken);
			String appTokenParams = parameters + SecurityConstants.CONCATANATE_SEPERATOR + hexTokenStr;
			String appToken = Base64.encodeToString( appTokenParams.getBytes(SecurityConstants.UTF_8), Base64.DEFAULT);

			Log.d(TAG, "appToken"+appToken);
			
			return appToken;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setSecurityPreSharedKey(String securityPreSharedKey) {
		this.securityPreSharedKey = securityPreSharedKey;
	}

	public void setSecurityVersionValue(String securityVersionValue) {
		this.securityVersionValue = securityVersionValue;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}
	/**
	 * put this method in to util
	 * convert byte array to hex char array
	 * @param bytes
	 * @return
	 */
	public char[] bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return hexChars;
	}
}

