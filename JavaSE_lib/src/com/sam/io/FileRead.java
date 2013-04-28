package com.sam.io;

import java.io.FileInputStream;

public class FileRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream("/home/sam/work/file_read.txt"); //以字节为单位
			byte data[] = new byte[1024];
			int i = 0;
			int n = fis.read();
			while(n != -1){
				data[i] = (byte)n;
				i++;
				n = fis.read();
			}
			String s = new String(data,0,i);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
