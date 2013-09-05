package com.sam.math;

import java.util.Random;

public class RandomDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random r = new Random();
		Random r1 = new Random(7);
		
		System.out.println("r: "+r.nextInt(13)+" r1: "+r1.nextInt(13));
	}

}
