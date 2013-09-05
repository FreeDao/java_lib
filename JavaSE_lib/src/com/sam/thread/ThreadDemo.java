/************
 * @author yang_jun2
 * Thread implements runnable interface
 * *********/
package com.sam.thread;

public class ThreadDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tortoise tortoise = new Tortoise(10);
		Thread tThread = new Thread(tortoise);                                                                                                                                                                                           
		tThread.start();
	}

}


class Tortoise implements Runnable{
	private int totalStep;
	private int step;
	
	public Tortoise(int totalStep){
		this.totalStep = totalStep;
	}

	@Override
	public void run() {
		while(step < totalStep){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			step++;
			System.out.println("The tortoise run "+step+" steps.");
		}
	}
}
       