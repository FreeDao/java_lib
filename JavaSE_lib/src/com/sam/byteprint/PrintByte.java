package com.sam.byteprint;

public class PrintByte {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int m =-1;
		
		for (int i = 1;i<=8;i++){
			int n=m&1;
			System.out.print(n+"\t");
			m = m >> 1;
		}
		
		
	}

}
