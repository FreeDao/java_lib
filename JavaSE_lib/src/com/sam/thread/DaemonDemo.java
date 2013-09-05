package com.sam.thread;

public class DaemonDemo {
	public static void main(String[] args) {
		Thread thread = new Thread(){
			public void run (){
				while(true){
					System.out.println("Orz");
				}
			}
		};
		//取消注释试试
//		thread.setDaemon(true);
		thread.start();
		
	}

}
