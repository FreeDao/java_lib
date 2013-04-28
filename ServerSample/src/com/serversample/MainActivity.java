package com.serversample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.serversample.net.exception.CustomerException;
import com.serversample.net.http.DefaultHttpConnection;
import com.serversample.net.vo.NetRequest;
import com.serversample.net.vo.NetResponse;
import com.serversample.util.JsonHelper;
import com.serversample.vo.UserLoginResultVO;
import com.serversample.vo.UserLoginVO;

/**
 * 
 * @author Administrator
 *
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //TODO: Here is sample codes to connection with server side
        String url = "Some Website";
        String contextPath = "/somepath/xxx";
        DefaultHttpConnection connection = new DefaultHttpConnection(url);
        
        NetRequest request = new NetRequest();
        try {
        	UserLoginVO loginData = new UserLoginVO();
        	loginData.setPassword("your Password");
        	loginData.setUserName("your User Name");
        	
        	String payload = JsonHelper.doStringify(loginData);
        	request.setPayload(payload);
        	
			NetResponse response = connection.sendRequest(contextPath, request);
			String resp = response.getResp();
			//Here is your response value
			Log.i(this.getClass().getName(), resp);
			
			UserLoginResultVO result = JsonHelper.parseString(resp, UserLoginResultVO.class);
			//TODO: Do more handler things for your result.
			
			
		} catch (CustomerException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
