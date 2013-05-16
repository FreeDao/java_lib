/**
 * �˵�Activity
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
		
		//��ʼ��SharedPreferance
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
		
		//��ʼ��Ϸ��ť��Ӧ
		start_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MenuActivity.this, GameActivity.class);
				MenuActivity.this.startActivityForResult(intent, 0);
			}
		});
		
		//ѡ���ѶȰ�ť��Ӧ
		choice_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				//����ѡ���ѶȶԻ���
				//ѡ���ͼ
				showMapChoiceDialog();
			}
		});
		
		//���а�ť��Ӧ
		arrange_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//�������а�Ի���	
				int first = GameConstants.sp.getInt("first", 0);
				int second = GameConstants.sp.getInt("second", 0);
				int third = GameConstants.sp.getInt("third", 0);
				
				String content = "��һ��: " + first + "\n"
					+ "�ڶ���: " + second + "\n"
					+ "������: " + third;
				
				showMessageDialog("���ٵ����а�", content, "��֪����...Ŭ��ȥ��");
			}
		});
		
		//������ť��Ӧ
		help_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//����������Ϣ
				String help_content = "˧˧or���� ����: \n" +
						"		���ȷǳ���л����֧�֣� \n" +
						"		��Ϸ�Ĳ����ܼ򵥣���Ȼ��ʼ��������Щ����Ϥ \n" +
						"�ⲻ���������⣬����Ϊ������ȷʵ���Ը��õ�ȷ������ \n" +
						"		��ֻ��Ҫ������������ָ�������ϽǺ����½ǣ�����ϣ��\n" +
						"������������ߵ�ʱ����ֻ��Ҫ���������Ͻǵ���ָ\n" +
						"������Ҫ�����һ��������ߵ�ʱ����ֻ��Ҫ���������½ǵ���ָ\n" +
						"		���Ŵ��������ܿ����Ӧ���ֲ�����\n" +
						"������и��õĲ������飬�뷢email����qq��ϵ��\n" +
						"email:onerain88@163.com\n" +
						"qq:171253484\n" +
						"		ף�Ҹ���oneRain����";
				
				showMessageDialog("��ܰ����ʾ", help_content, "�ðɣ��ҽ������ף����");
			}
		});
		
		//���ڰ�ť��Ӧ
		about_btn.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				//����������Ϸ��һЩС��Ϣ������
				String about_content = "		���ȷǳ���л����֧�֣����������ĵ�һ��С��Ϸ�����в���ܶ�©����" +
						"�����и�л���õ��������ѣ�������������������ѵ�ʱ�򲻻������޴�ļ�ǿ��" +
						"���������ʲô��������и��õ���Ϸ���⣬\n" +
						"		�����Ҳ��android���֣���Դ�������Ȥ��������ϵ��\n" +
						"email:onerain88@163.com\n" +
						"qq:171253484\n" +
						"		лл��";
		
				showMessageDialog("����", about_content, "��֪����...");
			}
		});
		
		//�˳���Ϸ��Ӧ
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
		case KeyEvent.KEYCODE_BACK: //���ذ�ť��
			showExitDialog();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	//��ʾѡ���ͼ�Ի���
	private void showMapChoiceDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
		builder.setTitle("ѡ����ͼ���԰�");
		builder.setItems(GameConstants.maps, 
				new DialogInterface.OnClickListener()
				{			
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						int map = 4;
						
						switch(which)
						{
						case 0: //ѡ����"2"�͵�ͼ
							map = 0;
							break;
						case 1: //ѡ����"I"�͵�ͼ
							map = 1;
							break;
						case 2: //ѡ����"U"�͵�ͼ
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
	
	//��ʾ���а�Ի���
	private void showMessageDialog(String title, String content, String conContent)
	{
		//�����Ի���
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(conContent, null);

		builder.create().show();
	}
	
	//�����˳��Ի���
	private void showExitDialog()
	{
		//�����Ի���
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�˳���ʾ");
		builder.setMessage("ȷ���˳���");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
		{	
			public void onClick(DialogInterface dialog, int which) 
			{
				//�˳�����
				finish();
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
	}
	
	//���ݷ���ֵ���ж��Ƿ��ǡ�������һ�Ρ�
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
			//������Ӧ
			break;
		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
