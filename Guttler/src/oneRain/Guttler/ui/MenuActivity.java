/**
 * 菜单Activity
 */
package oneRain.Guttler.ui;

import oneRain.Guttler.R;
import oneRain.Guttler.constants.GameConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity
{
	private Button start_btn = null;
	private Button choice_btn = null;
	private Button arrange_btn = null;
	private Button help_btn = null;
	private Button about_btn = null;
	private Button exit_btn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		initView();
		
		//初始化SharedPreferance
		GameConstants.sp = getPreferences(Activity.MODE_PRIVATE);
	}
	
	public void initView()
	{
		start_btn = (Button)findViewById(R.id.start_btn);
		choice_btn = (Button)findViewById(R.id.choice_btn);
		arrange_btn = (Button)findViewById(R.id.arrange_btn);
		help_btn = (Button)findViewById(R.id.help_btn);
		about_btn = (Button)findViewById(R.id.about_btn);
		exit_btn = (Button)findViewById(R.id.exit_btn);
		
		//开始游戏按钮响应
		start_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MenuActivity.this, GameActivity.class);
				MenuActivity.this.startActivityForResult(intent, 0);
			}
		});
		
		//选择难度按钮响应
		choice_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				//弹出选择难度对话框
				//选择地图
				showMapChoiceDialog();
			}
		});
		
		//排行榜按钮响应
		arrange_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//弹出排行榜对话框	
				int first = GameConstants.sp.getInt("first", 0);
				int second = GameConstants.sp.getInt("second", 0);
				int third = GameConstants.sp.getInt("third", 0);
				
				String content = "第一名: " + first + "\n"
					+ "第二名: " + second + "\n"
					+ "第三名: " + third;
				
				showMessageDialog("光荣的排行榜", content, "我知道了...努力去了");
			}
		});
		
		//帮助按钮响应
		help_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//弹出帮助信息
				String help_content = "帅帅or靓靓 的您: \n" +
						"		首先非常感谢您的支持！ \n" +
						"		游戏的操作很简单，当然开始的您会有些不熟悉 \n" +
						"这不是您的问题，是因为触摸屏确实难以更好的确定方向 \n" +
						"		您只需要把您的两个手指放在左上角和右下角，当你希望\n" +
						"蛇向左和向上走的时候，您只需要按下您左上角的手指\n" +
						"当您想要蛇向右或者向下走的时候，您只需要按下您右下角的手指\n" +
						"		相信聪明的您很快会适应这种操作的\n" +
						"如果您有更好的操作体验，请发email或者qq联系我\n" +
						"email:onerain88@163.com\n" +
						"qq:171253484\n" +
						"		祝幸福！oneRain敬上";
				
				showMessageDialog("温馨的提示", help_content, "好吧，我接受你的祝福！");
			}
		});
		
		//关于按钮响应
		about_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//关于我做游戏的一些小信息。。。
				String about_content = "		首先非常感谢您的支持，这是我做的第一个小游戏，其中不免很多漏洞！" +
						"过程中感谢兔兔的陪伴与刁难，让我练就了在面对困难的时候不会手足无措的坚强！" +
						"如果发现有什么不足或者有更好的游戏创意，\n" +
						"		如果您也是android新手，对源代码感兴趣，请您联系我\n" +
						"email:onerain88@163.com\n" +
						"qq:171253484\n" +
						"		谢谢！";
		
				showMessageDialog("关于", about_content, "我知道了...");
			}
		});
		
		//退出游戏响应
		exit_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				showExitDialog();
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_BACK: //返回按钮下
			showExitDialog();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	//显示选择地图对话框
	private void showMapChoiceDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
		builder.setTitle("选个地图试试吧");
		builder.setItems(GameConstants.maps, 
				new DialogInterface.OnClickListener()
				{			
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						int map = 4;
						
						switch(which)
						{
						case 0: //选择了"2"型地图
							map = 0;
							break;
						case 1: //选择了"I"型地图
							map = 1;
							break;
						case 2: //选择了"U"型地图
							map = 2;
							break;
						}
						
						Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						intent.putExtra("map", map);
						MenuActivity.this.startActivityForResult(intent, 0);
					}
				});
		builder.create().show();
	}
	
	//显示排行榜对话框
	private void showMessageDialog(String title, String content, String conContent)
	{
		//弹出对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(conContent, null);

		builder.create().show();
	}
	
	//生成退出对话框
	private void showExitDialog()
	{
		//弹出对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("退出提示");
		builder.setMessage("确定退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{	
			public void onClick(DialogInterface dialog, int which) 
			{
				//退出程序
				finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	
	//根据返回值来判断是否是”重新玩一次“
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		int result = data.getIntExtra("result", 4);
		System.out.println(resultCode + result);
		switch(result)
		{
		case 0:
			System.out.println("onActivityResul");
			Intent intent = new Intent(MenuActivity.this, GameActivity.class);
			intent.putExtra("map", GameConstants.mapChoice);
			MenuActivity.this.startActivityForResult(intent, 0);
			break;
		case 1:
			//返回响应
			break;
		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
