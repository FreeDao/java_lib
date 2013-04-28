package com.airbiquity.hap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileCompare {

	private static String MD5 = "MD5";
	
	public static String getFileMD5(File file) throws IOException, NoSuchAlgorithmException {
		if (!file.isFile()){
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in=null;
		byte buffer[] = new byte[1024];
		int len;
		digest = MessageDigest.getInstance(MD5);
		in = new FileInputStream(file);
		while ((len = in.read(buffer, 0, 1024)) != -1) {
			digest.update(buffer, 0, len);
		}
		in.close();
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	public static String getFileMD5(InputStream in) throws NoSuchAlgorithmException, IOException{
		MessageDigest digest = null;
		byte buffer[] = new byte[1024];
		int len;
		digest = MessageDigest.getInstance(MD5);
		while ((len = in.read(buffer, 0, 1024)) != -1) {
			digest.update(buffer, 0, len);
		}
		in.close();
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
