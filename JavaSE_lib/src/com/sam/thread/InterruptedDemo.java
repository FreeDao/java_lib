package com.sam.thread;

public class InterruptedDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread thread =  new Thread(){
			public void run(){
				try {
					Thread.sleep(50000);
				} catch (Exception e) {
					System.out.println("I am wake");
				}
			}
		};
		thread.start();
		thread.interrupt();
	}

}
