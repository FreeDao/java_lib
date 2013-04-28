package com.sam.thread;

import java.util.ArrayList;

public class SynArrayList {
	public static void main(String[] args) {
		System.out.println("test");
		final ArrayList list = new ArrayList();
		Thread t1 = new Thread(){
			public void run(){
				list.add(1);
			}
		};
		
		Thread t2 = new Thread(){
			public void run(){
				
				list.add(2);
			}
		};
		
		t1.start();
		t2.start();
		
		
	}

}
