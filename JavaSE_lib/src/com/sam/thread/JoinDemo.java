package com.sam.thread;

public class JoinDemo {
	public static void main(String[] args) {
		  System.out.println("Main thread start");
		Thread threadB = new Thread(){
			public void run(){
				System.out.println("B thread started....");
				for(int i = 0; i<12; i++){
					System.out.println("Thread B is running ...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Thread B will end soon");
			}
		};
		
		threadB.start();
		//可以将注释部分取消试试，join（），先执行B线程，在执行原主线程
		
//		try {
//			threadB.join(2000);    //加入2秒，2秒后仍未完成的那个则不理他了
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		
		
		System.out.println("Main thread will end ...");
	}

}
