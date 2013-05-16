/**
 * 主要的逻辑运算类，判断移动，吃食物，死亡
 */
package oneRain.Guttler.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oneRain.Guttler.constants.GameConstants;
import oneRain.Guttler.model.Block;
import oneRain.Guttler.model.Food;
import oneRain.Guttler.model.Guttler;


public class Operator 
{
	//贪吃蛇对象
	private Guttler guttler = null;
	
	//食物对象
	private Food food = null;
	
	//地图二维数组
	private int[][] map = null;
	
	private int xCount, yCount;
	
	private boolean foodFlag = false;

	//构造方法：参数部分可以对蛇的大小及位置做修改
	public Operator(int snakeLength, int xCount, int yCount) 
	{
		this.xCount = xCount;
		this.yCount = yCount;
		
		//初始化地图
		initMap(xCount, yCount);
		
		//初始化贪吃蛇
		initSnake(snakeLength);
	}
	
	//初始化地图
	public void initMap(int xCount, int yCount)
	{
		map = new int[yCount][xCount];
		food = new Food();
	}
	
	//初始化蛇
	public void initSnake(int snakeLength)
	{
		Block head = new Block(10, 1);
		
		List<Block> body = new ArrayList<Block>();
		for(int i=0; i<snakeLength; i++)
		{
			body.add(new Block(9-i, 1));
		}
		
		guttler = new Guttler(head, body, snakeLength);
	}
	
	//以时间为变化更新地图
	public int[][] update(int orientation)
	{	
		//每一层更新都是一次重绘
		clearTile();
		buildWall();
		
		if(!foodFlag)
		{
			updateFood();
			foodFlag = true;
		}
		
		buildFood();
		
		Block b = new Block();
		
		int x = 0;
		int y = 0;
		//主要分为移动和吃苹果两种情况
		switch(orientation)
		{	
		case GameConstants.UP:
			x = guttler.getHead().getX()-1;
			y = guttler.getHead().getY();
			break;
		case GameConstants.LEFT:
			x = guttler.getHead().getX();
			y = guttler.getHead().getY()-1;
			break;
		case GameConstants.DOWN:
			x = guttler.getHead().getX()+1;
			y = guttler.getHead().getY();
			break;
		case GameConstants.RIGHT:
			x = guttler.getHead().getX();
			y = guttler.getHead().getY()+1;
			break;
		}
		
		b.setX(x);
		b.setY(y);
	
		//判断是移动还是吃食物还是死亡
		if((map[x][y] == GameConstants.GREEN_START) ||
				(map[x][y] == GameConstants.YELLOW_START))
		{
			//死亡
			return null;
		}
		else if(map[x][y] == 0)
		{
			//移动
			return move(b);
		}
		else //吃食物
		{
			return eat(b);
		}
	}
	
	//生成食物
	private void buildFood() 
	{
		if(foodFlag)
		{
			setTile(GameConstants.RED_START, 
					food.getFood().getX(), food.getFood().getY());
		}
	}

	//初始化食物
	public void updateFood()
	{
		int x, y;
		
		//获取随机数
		do
		{
			Random random = new Random();
			
			x = Math.abs(random.nextInt()) % (yCount-1);
			y = Math.abs(random.nextInt()) % (xCount-1);
			
			//防止第一行或者第一列（左边和上边的墙）
			if(x == 0)
			{
				x = 1;
			}
			if(y == 0)
			{
				y = 1;
			}
			
		}while(map[x][y] != 0);
		
		setTile(GameConstants.RED_START, x, y);
		food.setFood(new Block(x, y));
	}
	
	//吃食物的情况
	public int[][] eat(Block block)
	{
		foodFlag = false;
		
		map = move(block);
		
		Block tail = guttler.getBody().get(guttler.getBody().size()-1);
		guttler.getBody().add(tail);
		
		setTile(GameConstants.YELLOW_START, tail.getX(), tail.getY());
		
		//根据吃食物的多少来修改刷新的频率，和积分
		//延迟变短 
		GameConstants.UPDATE_RATE = (int) (GameConstants.UPDATE_RATE * 0.95);
		//得分增加
		GameConstants.score += GameConstants.eveScore;
		GameConstants.eveScore = (int) (GameConstants.eveScore * 1.1);
		
		return map;
	}
	
	//移动的情况
	public int[][] move(Block block)
	{
		//为身体赋值
		for(int i=guttler.getBody().size()-1; i>0; i--)
		{
			//依次赋值
			guttler.getBody().set(i, guttler.getBody().get(i-1));
		}
		//为身体第一个节点赋值赋值
		guttler.getBody().set(0, guttler.getHead());
		//为头赋值
		guttler.setHead(block);
		
		//为蛇头赋值
		setTile(GameConstants.RED_START, 
				guttler.getHead().getX(), guttler.getHead().getY());
		
		//为蛇的身体赋值
		for(int i=0; i<guttler.getBody().size(); i++)
		{
			Block b = guttler.getBody().get(i);
			
			setTile(GameConstants.YELLOW_START, b.getX(), b.getY());
		}
		
		return map;
	}
	
	//清空操作
	public void clearTile()
	{
		for(int x=0; x<yCount; x++)
			for(int y=0; y<xCount; y++)
			{
				setTile(0, x, y);
			}
	}
	
	//设置地图显示内容
	public void setTile(int picIndex, int x, int y)
	{
		map[x][y] = picIndex;
	}
	
	//根据不同的选择绘制不同的墙
	public void buildWall()
	{
		switch(GameConstants.mapChoice)
		{
		case 0:
			build2Wall(); //绘制"2"型墙
			break;
		case 1:
			buildIWall(); //绘制"I"型墙
			break;
		case 2:
			buildUWall(); //绘制"U"型墙
			break;
		default:
			buildNormalWall(); //绘制普通墙
			break;
		}
		
	}
	
	//绘制2型的墙
	public void build2Wall()
	{
		buildNormalWall();
		
		int i, j;
		
		for(j=xCount/4; j<xCount*3/4; j++)
			setTile(GameConstants.GREEN_START, yCount/6, j);
		for(i=yCount/6; i<yCount/2; i++)
			setTile(GameConstants.GREEN_START, i, xCount*3/4);
		for(j=xCount/4; j<xCount*3/4+1; j++)
			setTile(GameConstants.GREEN_START, yCount/2, j);
		for(i=yCount/2; i<yCount*5/6; i++)
			setTile(GameConstants.GREEN_START, i, xCount/4);
		for(j=xCount/4; j<xCount*3/4; j++)
			setTile(GameConstants.GREEN_START, yCount*5/6, j);
	}
	
	//绘制I型的墙
	public void buildIWall()
	{
		buildNormalWall();
		
		int i, j;
		
		for(j=xCount*2/5; j<xCount*3/5+1; j++)
			setTile(GameConstants.GREEN_START, yCount/4, j);
		for(i=yCount/4; i<yCount*3/4; i++)
			setTile(GameConstants.GREEN_START, i, xCount/2);
		for(j=xCount*2/5; j<xCount*3/5+1; j++)
			setTile(GameConstants.GREEN_START, yCount*3/4, j);
	}
	
	//绘制U型墙
	public void buildUWall()
	{
		buildNormalWall();
		
		int i, j;
		
		for(i=yCount/4; i<yCount*3/4; i++)
			setTile(GameConstants.GREEN_START, i, xCount/4);
		for(j=xCount/4; j<xCount*3/4+1; j++)
			setTile(GameConstants.GREEN_START, yCount*3/4, j);
		for(i=yCount/4; i<yCount*3/4; i++)
			setTile(GameConstants.GREEN_START, i, xCount*3/4);
	}
	
	//绘制普通墙
	public void buildNormalWall()
	{
		//左右两边
		for(int x=0; x<yCount; x++)
		{
			setTile(GameConstants.GREEN_START, x, 0);
			setTile(GameConstants.GREEN_START, x, xCount-1);
		}
		//上下两边
		for(int y=0; y<xCount; y++)
		{
			setTile(GameConstants.GREEN_START, 0, y);
			setTile(GameConstants.GREEN_START, yCount-1, y);
		}
	}
}
