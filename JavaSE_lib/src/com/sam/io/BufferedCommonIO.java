package com.sam.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BufferedCommonIO {
	public static void dump(InputStream src,OutputStream dest){
		try {
		InputStream input = new BufferedInputStream(src);
		OutputStream output = new BufferedOutputStream(dest);	
		byte data[] = new byte[1024];
		int length = -1;
		 while((length = input.read(data))!=-1){	// read读取一个字符 readLine读取一行 
			 output.write(data, 0, length);
		 }
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				src.close();
				dest.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
	}

}
