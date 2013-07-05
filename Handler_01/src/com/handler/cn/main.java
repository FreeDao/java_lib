package com.handler.cn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class main extends Activity {
    
	private Button myButton01;
	private Button myButton02;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
    myButton01=(Button) findViewById(R.id.Button01);
    myButton02=(Button) findViewById(R.id.Button02);
    
    //����
    myButton01.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
		myHandler.sendEmptyMessage(50);	
		}});
    myButton02.setOnClickListener(new Button.OnClickListener(){

		public void onClick(View v) {
		Message myMessage=new Message();
		myMessage.what=1;
		Bundle myBundle=new Bundle();
		myBundle.putString("name", "sam");
		myBundle.putString("number", "1234567890");
		myMessage.setData(myBundle);
		myHandler.sendMessage(myMessage);
		}});
    
    }
    //����Handler���ʵ��
    Handler  myHandler=new Handler(){
    	
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		switch(msg.what){
    		
    		case 50:
    			Toast.makeText(main.this, "message:"+msg.what, Toast.LENGTH_LONG).show();
    			break;
    		case 1:
    			Bundle myBundle1=msg.getData();
    			String myName=myBundle1.getString("name");
    			String myNumber=myBundle1.getString("number");
    			Toast.makeText(main.this, "name:"+myName+" ,number"+myNumber+" type:"+msg.what, Toast.LENGTH_LONG).show();
    			break;
    		
    		
    		}
    	}
    };
    
    
    
}