package com.sam.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WriterThread extends Thread{
	Socket socket = null;
	String clientName = null;
	
	public WriterThread(Socket socket,String clientName){
		this.socket = socket;
		this.clientName = clientName;
	}
	
	public void run(){

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			String result = null;
			result = reader.readLine();
			while(!(result == "bye")){
				pw.println("I am client"+result);
				// 刷新输出流，make the server get the char immediately
				pw.flush();
				result = reader.readLine();
			}
			pw.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
