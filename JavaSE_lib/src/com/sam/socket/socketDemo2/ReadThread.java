package com.sam.socket.socketDemo2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
	Socket socket = null;
	String clientName = null;

	public ReadThread(Socket socket, String clientName) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.clientName = clientName;
	}
	public void run(){
		try{
			String line = "";
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			line = is.readLine();
			while(line!=null && !line.equals("bye")){
				System.out.println(clientName+" says :"+line);
				line = is.readLine();
			}
			is.close();
			socket.close();
			
		}catch(Exception e ){
			System.out.println("error cannot get socket connection.");
			System.out.println(e);
		}
	}

}
