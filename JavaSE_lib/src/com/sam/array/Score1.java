package com.sam.array;

import java.util.Arrays;

public class Score1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] scores = new int[10];
		for(int score : scores){
			System.out.print(" "+score);	// default 0
		}
		System.out.println();
		Arrays.fill(scores, 60);
		for(int score : scores){
			System.out.print(" \t"+score);	//60
		}
		//****************fen ge xian***********************//
		int scores1[] = {1,2,3,4,5,6,7,8,9,0};
		int scores2[] = scores1;	// they are the same object
		
		scores2[0] = 999;
		
		System.out.println("scores1[0]= "+scores1[0]);
		System.out.println("scores2[0]= "+scores2[0]);
		
		
		int cords[][] = new int [2][6];	// cords.length == 2,cords[0].length == 6;
		System.out.println("cords 's length = "+cords.length);
		
		//遍历二维数组
		for(int [] row : cords){
			for(int value : row){
				
			}
		}
		
		
		
		
		
	}

}
