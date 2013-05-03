package com.sam.handlerthreaddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class HandlerThreadActivity extends Activity {
	private MyHandler mHandler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handler_thread);
		System.out.println("----------Main thread id = "+Thread.currentThread().getId());
		
		// 生成一个HandlerThread 对象，使用Looper 处理消息队列
		HandlerThread thread = new HandlerThread("My thread");
		thread.start();
		//将一个线程绑定到Handler上，该Handler 可以处理线程的队列消息
		mHandler = new MyHandler(thread.getLooper());
		
		// 从handler 中获取消息对象
		Message message = mHandler.obtainMessage();
		
		message.sendToTarget();
	}


}


class MyHandler extends Handler{
	public MyHandler(Looper looper){
		super(looper);
		System.out.println("-----construct for MyHandler------");
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		System.out.println("------message.what---"+msg.what);
		super.handleMessage(msg);
		System.out.println("MyHandler id----"+Thread.currentThread().getId());
	}
	
	
	
}