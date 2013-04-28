import java.io.UnsupportedEncodingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;


public class AppTokenGenerator {
	
	private final static String UTF8 = "UTF-8";
	private final static String HMAC_SHA256 = "HmacSHA256";
	private final static String PARAM_SEPERATOR = ";";
	private final static String CONCATANATE_SEPERATOR = "&";
	
	private final static String PRE_KEY_VERSION = "KeyVersion=";
	private final static String PRE_TYPE = "Type=";
	private final static String PRE_TIME = "Time=";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		try {
			String keyType = "ANDROID";
			String keyVersion = "1";
			String keyValue = "pxTq5W+49MjQDE0krNBntpCQycKyyE+Ccxjb7io3I8A=";
			long time = 1367032327063l;
			
			String appToken = getAppToken(keyType,keyVersion,keyValue.getBytes(UTF8),time);
			
			System.out.println(appToken);
			System.out.println("S2V5VmVyc2lvbj0xO1R5cGU9QU5EUk9JRDtUaW1lPTEzNjcwMzIzMjcwNjMmN2FiOGNjMDUxYzFmMjk1ZGNiNDI2ZDE5OTJlMzkxYmViODUwYjk0ZTAwMGE4ZjJjYzU2ZDkwNzRmYjY4NTVmZQ==");
			System.out.println(appToken.equals("S2V5VmVyc2lvbj0xO1R5cGU9QU5EUk9JRDtUaW1lPTEzNjcwMzIzMjcwNjMmN2FiOGNjMDUxYzFmMjk1ZGNiNDI2ZDE5OTJlMzkxYmViODUwYjk0ZTAwMGE4ZjJjYzU2ZDkwNzRmYjY4NTVmZQ=="));
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	
	
	}
	
	
	public static String getAppToken(String keyType, String keyVersion, byte[] keyValue, long time) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(keyValue, HMAC_SHA256);
			String keyVersionParam = PRE_KEY_VERSION + keyVersion;
			String keyTypeParam = PRE_TYPE + keyType;
			
			// long time = System.currentTimeMillis();
			// TODO : here can be more efficient
			// time = time - ( 8 * 60 * 60 * 1000); // Seattle time GMT -8
			// time += this.timeDifference;

			String timeParam = PRE_TIME + time;
			
			// step 1.a
			String parameters = keyVersionParam + PARAM_SEPERATOR + keyTypeParam + PARAM_SEPERATOR + timeParam;
			// Log.d(TAG, "PARAMETERS = " + parameters);

			// step 1.b
			Mac mac = Mac.getInstance(HMAC_SHA256);
			mac.init(keySpec);
			byte[] sha256 = mac.doFinal(parameters.getBytes(UTF8));

			// step 2
			//char[] hexToken = bytesToHex(sha256);
			char[] hexToken = Hex.encodeHex(sha256);
			String hexTokenStr = new String(hexToken);
			// Log.d(TAG, "APP_HEX_TOKEN = " + hexTokenStr);
			
			// step 3
			String appTokenParams = parameters + CONCATANATE_SEPERATOR + hexTokenStr;
			// Log.d(TAG, "APP_TOKEN_PARAMS = " + appTokenParams);
			
			// step 4
			//String appToken = Base64.encodeToString( appTokenParams.getBytes(SecurityConstants.UTF_8));
			String appToken = Base64.encodeBase64String(appTokenParams.getBytes(UTF8));
			// Log.d(TAG, "APP_TOKEN = " + appToken);

			return appToken;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
