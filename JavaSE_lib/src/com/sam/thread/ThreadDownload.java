package com.sam.thread;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ThreadDownload {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		final URL urls[]= {new URL("http://localhost:8080/WebTest/testDownload.html")};
		String filenames[] = {""};

	
	
	}
	
	public static void dump(InputStream src, OutputStream dest){
		try {
			InputStream input = src;
			OutputStream output = dest;
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
}
