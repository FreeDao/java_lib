package com.android.lumaqqMe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import edu.tsinghua.lumaqq.qq.QQClient;
import edu.tsinghua.lumaqq.qq.Util;
import edu.tsinghua.lumaqq.qq.beans.QQUser;
import edu.tsinghua.lumaqq.qq.net.PortGateFactory;

public class lumaqqMe extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViews();
        setListeners();
    }
    
    private Button button_test;
    private void findViews()
    {
    	button_test = (Button) findViewById(R.id.submit);
    }
    
    private void setListeners() {
    	button_test.setOnClickListener(testLumaQQ);
    }
    
    private OnClickListener testLumaQQ = new OnClickListener(){
    	@Override
		public void onClick(View v) {
    		QQClient client = new QQClient();
            client.setConnectionPoolFactory(new PortGateFactory());
            QQUser user = new QQUser(756427684, "1748!?");
            // 设置参数
            user.setUdp(true);            
            client.setUser(user);
            client.setLoginServer("sz.tencent.com");

            try {
                // 登录
                client.login();
                // 等待登录完成
                Thread.sleep(10000);
                // 发送消息
                /* 在sendMessage中填入你自己想发的消息和对方的QQ号 */
                client.im_Send(1269305100, Util.getBytes("Hello"));
                // 等待消息发送完成
                Thread.sleep(6000);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            // 登出
            client.logout();
            // 释放，不再想使用此client实例时必须调用此方法
            client.release();
    	}
    };
}