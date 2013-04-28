package com.sam.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		缓冲区
		byte buffer[] = new byte[1024];
		try {
			DatagramSocket dsocket = new DatagramSocket(8080);
			System.out.println("the client is waitting for the server sent messages....");
			while(true){
				DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
				try {
					dsocket.receive(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
