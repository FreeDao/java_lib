package com.sam.thread;

class Producer implements Runnable {

	private Clerk clerk;

	public Producer(Clerk clerk) {
		this.clerk = clerk;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Producer start to produce...");

		try {

			for (int product = 0; product < 10; product++) {
				Thread.sleep((int) (Math.random() * 1000));
				clerk.setProduct(product);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
/************************** 分割线 ****************************/
	class Consumer implements Runnable{
		private Clerk clerk;
		public Consumer(Clerk clerk){
			this.clerk = clerk;
		}

	@Override
	public void run() {
		System.out.println("Consumer start to consume");
		try {
			for(int i = 0;i<10 ; i++){
				Thread.sleep((int) (Math.random()*3000));
				int product = clerk.getProduct();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	}


/************************** 分割线 ****************************/
class Clerk {
	private int product = -1;

	public synchronized void setProduct(int product) {
		while (this.product != -1) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		this.product = product;
		System.out.println("producer set "+ this.product);
		notify();
	}

	public synchronized int getProduct() {
		// TODO Auto-generated method stub
		while(this.product == -1){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int p = this.product;
		System.out.println("consumer get "+this.product);
		this.product = -1;
		notify();
		return p;
	}
}

/************************** 分割线 ****************************/
public class ProducerConsumerDemo {
	public static void main(String args[]) {
		Clerk clerk = new Clerk();
		new Thread(new Producer(clerk)).start();
		new Thread(new Consumer(clerk)).start();
	}
}

