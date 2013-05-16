package oneRain.Guttler.ui;

import oneRain.Guttler.R;
import oneRain.Guttler.constants.GameConstants;
import oneRain.Guttler.view.GameView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class GameActivity extends Activity 
{
	private LinearLayout layout = null;
	private GameView gameView = null;
	
	MyHandler handler = new MyHandler();
	
	//���ֲ���
//	private MediaPlayer mp = null;
	
	//�Ƿ���ͣ
	private boolean isPaused = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        initMusic();
        initData();
        initView();
        ut.start();
    }
    
//    public void initMusic()
//    {
//    	mp = MediaPlayer.create(this, R.raw.becauselove);
//    	
//    	mp.start();
//    }
    
    public void initData()
    {
    	GameConstants.curOrientation = GameConstants.DOWN;
    	GameConstants.map = null;
    	GameConstants.operator = null;
    	GameConstants.UPDATE_RATE = 500;
    	GameConstants.score = 0;
    	GameConstants.eveScore = 10;
    	
    	GameConstants.mapChoice = getIntent().getIntExtra("map", 4);
    }
    
    @Override
	protected void onResume() 
    {
		super.onResume();
	}

	public void initView()
    {
    	layout = (LinearLayout)findViewById(R.id.layout);
    	gameView = new GameView(this);
    	gameView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
    			LayoutParams.FILL_PARENT));
    	layout.addView(gameView);
    }
    
    @Override
	protected void onPause()
    {
		super.onPause();
	}

	@Override
	protected void onStop() 
	{
//		GameConstants.mapChoice = 4;
		
		super.onStop();
	}

	@Override
	protected void onDestroy() 
    {
		super.onDestroy();
		
		ut.setFlag(false);
		
//		mp.stop();
	}
	
	private UpThread ut = new UpThread();
	
	//��������߳�
	public class UpThread extends Thread
	{
		private boolean flag = true;
		
		public void setFlag(boolean flag)
		{
			this.flag = flag;
		}
		
		@Override
		public void run() 
		{	
			while(flag)
			{				
				try 
				{
					Thread.sleep(GameConstants.UPDATE_RATE);
				}
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
				//�ж��Ƿ���ͣ
				if(!isPaused) //û��ͣ��ʱ��
				{
					update();
				}
				else //��ͣ��ʱ��
				{
					Message msg = new Message();
					msg.what = GameConstants.MSG_PAUSE;
					handler.sendMessage(msg);
				}
			}
		}
	}
	
	
	//�������
	public void update()
	{
		GameConstants.map = GameConstants.operator.update(GameConstants.curOrientation);
		
		if(GameConstants.map == null) //����
		{
			ut.setFlag(false);
			
			Message msg = new Message();
			msg.what = GameConstants.MSG_GAMEOVER;
			handler.sendMessage(msg);
		}
		else //û��
		{
			Message msg = new Message();
			msg.what = GameConstants.MSG_UPDATE;
			handler.sendMessage(msg);
		}		
	}
	
	//��Ӧ������Ϣ������UI
	public class MyHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{			
			switch(msg.what)
			{
			case GameConstants.MSG_UPDATE:
				gameView.invalidate();
				break;
			case GameConstants.MSG_GAMEOVER:
				//��Ϸ����
				//������û�и������а�
				int first = GameConstants.sp.getInt("first", 0);
				int second = GameConstants.sp.getInt("second", 0);
				int third = GameConstants.sp.getInt("third", 0);
				
				String congratulation = "";
				if(GameConstants.score < third) //δ�ϰ�
				{
					
				}
				else if(GameConstants.score > first) //�ȵ�һ��ţx
				{
					SharedPreferences.Editor editor = GameConstants.sp.edit();
					int t = GameConstants.sp.getInt("second", 0);
					editor.putInt("third", t);
					int s = GameConstants.sp.getInt("first", 0);
					editor.putInt("second", s);
					editor.putInt("first", GameConstants.score);
					editor.commit();
					congratulation = "��һ��С�ľ����˵�һ�ļ�¼...";
				}
				else if(GameConstants.score > second) //���˰񣬵���û��һ�࣬�ȵڶ���
				{
					SharedPreferences.Editor editor = GameConstants.sp.edit();
					int t = GameConstants.sp.getInt("second", 0);
					editor.putInt("third", t);
					editor.putInt("second", GameConstants.score);
					editor.commit();
					congratulation = "��һ��С�ľ����˵ڶ��ļ�¼...";
				}
				else if(GameConstants.score > third) //���˰񣬵���û��һ�࣬û�ڶ��࣬�ȵ����
				{
					SharedPreferences.Editor editor = GameConstants.sp.edit();
					editor.putInt("third", GameConstants.score);
					editor.commit();
					congratulation = "��һ��С�ľ����˵���ļ�¼...";
				}
				
				//�����Ի���
				AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
				builder.setTitle("Game Over");
				builder.setMessage("��ķ�����: " + GameConstants.score + "\n"
						+ congratulation);
				builder.setPositiveButton("����", new DialogInterface.OnClickListener()
				{	
					public void onClick(DialogInterface dialog, int which) 
					{
//						//�˳�����
						Intent i = getIntent();
						i.putExtra("result", 1);
						GameActivity.this.setResult(0, i);
						finish();
					}
				});
				builder.setNegativeButton("����һ��", new DialogInterface.OnClickListener()
				{	
					public void onClick(DialogInterface dialog, int which) 
					{				
						Intent i = getIntent();
						i.putExtra("result", 0);
						GameActivity.this.setResult(0, i);
						finish();
					}
				});
				builder.create().show();
				
//				gameView.invalidate();
				break;
			case GameConstants.MSG_PAUSE:
				//����GameView�ػ�
				gameView.invalidate();
				break;
			}
		}
	}
	
	/**
	 * �������
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_BACK: //���ذ�ť��
			isPaused = true;
			
			//�����Ի���
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("��ʾ");
			builder.setMessage("��ͣ��...\n");
			builder.setPositiveButton("������һҳ", new DialogInterface.OnClickListener()
			{	
				public void onClick(DialogInterface dialog, int which) 
				{					
					Intent i = getIntent();
					i.putExtra("result", 1);
					GameActivity.this.setResult(0, i);
					finish();
				}
			});
			builder.setNegativeButton("ȡ����ͣ", new DialogInterface.OnClickListener()
			{	
				public void onClick(DialogInterface dialog, int which) 
				{
					isPaused = false;
				}
			});
			builder.create().show();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}