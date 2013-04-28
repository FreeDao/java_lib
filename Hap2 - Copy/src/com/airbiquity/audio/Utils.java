package com.airbiquity.audio;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import org.json.JSONArray;
import org.json.JSONException;

public class Utils {
	
	public static byte[] convertShortArrayToByteArray(short[] srcArr, int offset, int length){
		
		ByteBuffer buf = ByteBuffer.allocate(length*2);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		for(int i = offset; i < offset + length;i++){
			buf.putShort(srcArr[i]);
		}
		
		return buf.array();
	}
	
	public static short[] convertByteArrayToShortArray(byte[] srcArr, int offset, int length){
		
		ShortBuffer sb = ShortBuffer.allocate(length/2);
		
		for(int i = offset; i < offset + length;i+=2){
		  sb.put((short)((srcArr[i+1] << 8) + (srcArr[i] & 0xff)));
		}
		
		return sb.array();
	}
	
public static byte[] convertJsonHexArrayToBytes(String content){
		
		byte[] payload = null;
		try {
			JSONArray ja = new JSONArray(content);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(4 * ja.length());
			DataOutputStream dos = new DataOutputStream(bos);

			for (int i = 0; i < ja.length();) {
				String s = ja.getString(i++);
				int v = Integer.valueOf(s, 16);
				dos.writeByte(v);
			}
			payload = bos.toByteArray();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return payload;
	}

	/**
	 * Converts the specified payload to a string containing an array of
	 * hex strings for each byte.
	 * 
	 * @param payload : payload to be converted
	 * @return string representation of the specified payload with format:
	 * "["01","23","45","67","89","AB","CD","EF",...]"
	 */
	public static String convertBytesToJsonHexArray(byte[] payload) {
		JSONArray ja = new JSONArray();
		
		if( payload != null ){
			for (byte b : payload) {
				ja.put(String.format("%02X", b));
			}
		}
		
		return ja.toString();
	}

}
