package com.sam.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class StandardIn {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		File src = new File("/home/sam/work/dest.txt");
		FileInputStream in = (new FileInputStream(src));
		System.setIn(in);
		try {
			Scanner scanner = new Scanner(System.in);
			while(scanner.hasNextLine()){
				System.out.println(scanner.nextLine());
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
