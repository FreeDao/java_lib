package com.sam.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionDemo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		URL url = new URL("http://www.baidu.com");
		URLConnection uc = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		String result = null;
		while ((result = br.readLine()) != null)

		{
			System.out.println(result);
		}
	}

}
