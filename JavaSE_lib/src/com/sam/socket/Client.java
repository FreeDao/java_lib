package com.sam.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			//send request to loclahost at port 8080
			Socket socket = new Socket("127.0.0.1",8080);
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
			
		}catch(Exception e){
			System.out.println(e);
		}
		

	}

}
