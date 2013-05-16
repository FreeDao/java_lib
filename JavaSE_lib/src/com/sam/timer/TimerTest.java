package com.sam.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new MyTask(),0,2000);
		while(true){
			try {
				int ch = System.in.read();
				if(ch-'c'==0){
				timer.cancel();//使用这个方法退出任务
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
class MyTask extends TimerTask{

	@Override
	public void run() {
		System.out.println("hehe");
	}
	
}