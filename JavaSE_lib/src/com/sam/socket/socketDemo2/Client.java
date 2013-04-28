package com.sam.socket.socketDemo2;

import java.net.Socket;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			Socket socket = new Socket("10.20.70.63",8080);
			Thread read = new ReadThread(socket,"Client");
			Thread write = new WriteThread(socket,"Client");
			read.start();
			write.start();
		}catch(Exception e){
			System.out.println(e);
		}
	}

}

