package com.sam.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			ServerSocket server = null;
			try{
				server = new ServerSocket(8080);
			}catch(Exception e){
				System.out.println("cannot listen to port 8080:====="+e);
			}
			Socket socket = null;
			try{
				socket = server.accept();
			}catch(Exception e){
				System.out.println(e);
			}
			String line = "";
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(line != null && !line.equals("bye")){
				line = is.readLine();
				System.out.println("I am server,the client says :"+ line);
			}
			// close the socket 输入流
			is.close();
			//close socket
			socket.close();
			// close server socket
			server.close();
			
		}catch(Exception e){
			System.out.println(e);
		}

	}

}
