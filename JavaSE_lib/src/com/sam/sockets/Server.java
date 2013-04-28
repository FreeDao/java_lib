package com.sam.sockets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class Server{
	public static void main(String args[]){
		try {
			ServerSocket server = new ServerSocket(8080);
			Socket socket = server.accept();
			Thread readerThread = new ReaderThread(socket, "server");
			Thread witerThread = new WriterThread(socket, "server");
			readerThread.start();
			witerThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}