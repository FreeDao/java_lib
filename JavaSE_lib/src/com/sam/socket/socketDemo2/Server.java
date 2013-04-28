
package com.sam.socket.socketDemo2;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		Socket socket = null;
		try{
			serverSocket =new ServerSocket(8080);
			System.out.println("Server is starting......");
			socket = serverSocket.accept();
			System.out.println("accept a connection......");
			Thread read = new ReadThread(socket,"server");
			Thread write = new WriteThread(socket,"server");
			read.start();
			write.start();
			
		}catch(Exception e){
			System.out.println(e);
			System.exit(-1);
		}
	}
}
