package com.sam.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderThread extends Thread{
	Socket socket = null;
	String clientName = null;
	
	public ReaderThread(Socket socket , String clientName){
		this.socket = socket;
		this.clientName = clientName;
	}
	
	public void run(){
		try {
			String line = "";
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(line != null  && line != "88"){
				line =  is.readLine();
				System.out.println(line);
			}
			is.close();
			socket.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}
}
