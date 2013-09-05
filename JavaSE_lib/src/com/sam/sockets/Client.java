package com.sam.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Client{
	public static void main(String args[]){
		try {
			Socket socket = new Socket("10.20.70.63",8080);
			Thread read = new ReaderThread(socket, "Client");
			Thread write = new WriterThread(socket,"Client");
			read.start();
			write.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
