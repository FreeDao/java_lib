package com.sam.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadDemo {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args){
		
		try {
			
			URL url = new URL("http://www.baidu.com");
			InputStream src = url.openStream();	// openStream is short for openConnection , getInputStream
			File fileDest = new File("/home/sam/work/fileDest");
			OutputStream dest = new FileOutputStream(fileDest);
			CommonIO.dump(src,dest);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
