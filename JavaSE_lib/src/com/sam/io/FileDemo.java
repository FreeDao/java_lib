package com.sam.io;

import java.io.File;
import java.io.IOException;

public class FileDemo {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File f =new File("/home/sam/work/a.txt");
		f.createNewFile();
		System.out.println(f.getAbsolutePath());
		System.out.println(f.getName());
		System.out.println(f.getParent());
		System.out.println(f.canExecute());
		System.out.println(f.isDirectory());
		System.out.println(f.length());
		
	}

}
    