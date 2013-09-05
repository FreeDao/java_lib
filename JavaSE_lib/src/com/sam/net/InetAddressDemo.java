

/*****************************
 * get the basic imformation of your PC and net 
 * 
 * 
 */


package com.sam.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {

	/**
	 * @param args
	 */
	public static void main(String[] para) throws UnknownHostException{
		InetAddress IP0 = InetAddress.getLocalHost();
		String name = IP0.getCanonicalHostName();
		byte[] address = IP0.getAddress();
		String hostAddress = IP0.getHostAddress();
		String ip = IP0.toString();
		System.out.println("CanonicalHostName is : "+name+" hostAddress is:"+hostAddress+
				" address is:"+address+" IP is:"+ip );
		
	}

}
