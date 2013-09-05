package com.sam.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author sam
 *This is a common class 
 */
public class CommonIO {
	public static void dump(InputStream src,OutputStream dest) throws IOException{
		InputStream input = null; 
		OutputStream output = null;
		try{
			 input = src; 
			 output = dest;
			 byte data[] = new byte[1024];	// 每次读取1024 bytes;
			 int length = -1;
			 while((length = input.read(data))!=-1){	// read读取一个字符 readLine读取一行 
				 output.write(data, 0, length);
			 }
		}finally{
			src.close();
			dest.close();
		}
	}

}
