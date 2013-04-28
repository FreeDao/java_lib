package com.sam.socket.socketDemo2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread extends Thread{
	Socket socket = null;
	String clientName = null;
	public WriteThread(Socket socket, String clientName) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.clientName = clientName;
	}
	public void run(){
		try{
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			String readLine = sin.readLine();
			while(!readLine.equals("bye")){
				os.println(readLine);
				os.flush();//refresh'
				readLine = sin.readLine();
			}
			os.close();
			socket.close();
			
		}catch(Exception e){
			System.out.println("transmission error");
		}
	}
}
