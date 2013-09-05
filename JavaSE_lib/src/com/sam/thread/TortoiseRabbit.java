package com.sam.thread;

public class TortoiseRabbit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TortoiseThread tortoise = new TortoiseThread();
		RabbitThread rabbit = new RabbitThread();
		Thread tThread = new Thread(tortoise);
		Thread rThread = new Thread(rabbit);
		tThread.start();
		rThread.start();
	}
}

class TortoiseThread  implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int times = 10;
		for (int i = 0;i<times;i++){
			System.out.println("Tortoise run "+ i + " steps.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}


class RabbitThread implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int times = 10;
		for(int i = 0;i<times;i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("rabbit bobobo");
		}
	}
	
}





















