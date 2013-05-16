/**
 * 自定义的View控件，显示并更新游戏画面
 */
package oneRain.Guttler.view;

import oneRain.Guttler.R;
import oneRain.Guttler.constants.GameConstants;
import oneRain.Guttler.logic.Operator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class GameView extends View 
{	
	//上下文信息
	private Context c;
	
	//屏幕信息
	private int width, height;
	
	//设置屏幕x,y轴的方块数量
	private int xCount;
	private int yCount;
	
	//设置屏幕x,y轴偏移量
	private int xOffset;
	private int yOffset;
	
	//从资源文件获取Bitmap
	private Bitmap[] pics;
	
	//构造方法
	public GameView(Context context) 
	{
		super(context);
		this.c = context;
	}
	
	public GameView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);	
		this.c = context;
	}
	
	//在游戏屏幕尺寸变化的情况下调用，可用作初始化
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{	
		super.onSizeChanged(w, h, oldw, oldh);
		
		initData(w, h);
	}
	
	/**
	 * 初始化地图二维数组，运算类对象
	 * @param w
	 * @param h
	 */
	public void initData(int w, int h)
	{
		width = w;
		height = h;
		
		
		xCount = (int)Math.floor(w / GameConstants.SIZE);
		yCount = (int)Math.floor(h / GameConstants.SIZE);
		
		xOffset = (w - GameConstants.SIZE * xCount) / 2;
		yOffset = (h - GameConstants.SIZE * yCount) / 2;
		
		//初始化地图大小
		GameConstants.map = new int[yCount][xCount];
		
		GameConstants.operator = new Operator(4, xCount, yCount);
		GameConstants.map = GameConstants.operator.update(GameConstants.curOrientation);
		
		initGame();
	}
	
	/**
	 * 初始化游戏中显示图片资源
	 */
	public void initGame()
	{
		Resources r = getContext().getResources();
		
		pics = new Bitmap[4];
		
		loadPic(GameConstants.GREEN_START, r.getDrawable(R.drawable.greenstar));
		loadPic(GameConstants.RED_START, r.getDrawable(R.drawable.redstar));
		loadPic(GameConstants.YELLOW_START, r.getDrawable(R.drawable.yellowstar));
	}
	
	/**
	 * 初始化图片资源数组
	 * @param key
	 * @param drawable
	 */
	public void loadPic(int key, Drawable drawable)
	{
		Bitmap bitmap = Bitmap.createBitmap(GameConstants.SIZE, GameConstants.SIZE, 
				Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		
		drawable.setBounds(0, 0, GameConstants.SIZE, GameConstants.SIZE);
		drawable.draw(canvas);
		
		pics[key] = bitmap;
	}
	
	
	/**
	 * 响应触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		//判断：如果当前是上下移动，则响应左右；如果当前是左右移动，则响应上下
		if((GameConstants.curOrientation%2) == 1) //上下移动
		{
			//判断是左还是右
			if(x < width/2)
			{
				//更新向左
				GameConstants.curOrientation = GameConstants.LEFT;
			}
			else
			{
				//更新向右
				GameConstants.curOrientation = GameConstants.RIGHT;
			}
		}
		else //左右移动
		{
			//判断是上还是下
			if(y < height/2)
			{
				//更新向上
				GameConstants.curOrientation = GameConstants.UP;
			}
			else
			{
				GameConstants.curOrientation = GameConstants.DOWN;
			}
		}
		
		return super.onTouchEvent(event);
	}

	/**
	 * 绘制方法
	 */
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		
		Paint paint = new Paint();

		//绘制View，根据所在坐标点的值而挥之不同的图片
		if(GameConstants.map == null) //判断死亡的状态下。。。
		{
			
		}
		else //绘制
		{
			for(int x=0; x<yCount; x++)
				for(int y=0; y<xCount; y++)
				{
					//有内容
					if(GameConstants.map[x][y] > 0)
					{
						canvas.drawBitmap(pics[GameConstants.map[x][y]], 
								xOffset + y * GameConstants.SIZE,
								yOffset + x * GameConstants.SIZE, 
								paint);
					}
				}
		}
	}
}
