/******
 * get the basic imformation of a/an URL
 * 
 * */

package com.sam.net;

import java.net.MalformedURLException;
import java.net.URL;

public class URLDemo {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		URL url = new URL("http://www.baidu.com");
		URL tuto = new URL(url,"tutorial.intro.html#DOWNLOADING");
		System.out.println("protocal="+tuto.getProtocol());
		System.out.println("host="+tuto.getHost());
		System.out.println("port="+tuto.getPort());
		System.out.println("Authority="+tuto.getAuthority());
		System.out.println("Path="+tuto.getPath());
		System.out.println("UserInfo="+tuto.getUserInfo());
		System.out.println("DefaultPort="+tuto.getDefaultPort());
		

	}

}
