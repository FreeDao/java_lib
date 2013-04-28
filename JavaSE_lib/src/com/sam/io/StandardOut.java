package com.sam.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class StandardOut {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("/home/sam/work/dest.txt");
			PrintStream printStream = new PrintStream(new FileOutputStream(file));
			System.setOut(printStream);	// ****When we setOut ,standardoutputStream will be changed
			printStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("hello world");
	}

}
