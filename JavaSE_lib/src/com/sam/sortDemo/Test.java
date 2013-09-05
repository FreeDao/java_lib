package com.sam.sortDemo;

public class Test {
	static void sort(int i) throws InterruptedException{
		if(i>0){
			i--;
			System.out.println("before: "+i);
			sort(i);
			Thread.sleep(1000);
			System.out.println("a line "+i);
//			sort(i);
//			System.out.println("second line "+i);
		}
		
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		sort(5);
	}

}
