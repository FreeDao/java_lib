/**
 * 用来标记每一个坐标点上的小方块，主要记录方块在二维数组中的坐标
 */
package oneRain.Guttler.model;

public class Block 
{
	//定义图片块得x,y轴坐标
	private int x;
	private int y;
	
	
	public Block() 
	{
		super();
	}

	//构造方法
	public Block(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}

	//Setter,Getter方法
	public int getX()
	{
		return x;
	}


	public void setX(int x) 
	{
		this.x = x;
	}


	public int getY()
	{
		return y;
	}


	public void setY(int y) 
	{
		this.y = y;
	}
	
	
}
