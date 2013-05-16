/**
 * 蛇类，没有用数组或者链表的原因就是以后可能会对蛇做更精确地设计，比如眼镜蛇，黑曼巴。。。。
 */
package oneRain.Guttler.model;

import java.util.List;

public class Guttler 
{
	
	//贪吃蛇的信息：可变长度的身体，蛇头，长度
	private List<Block> body;
	private Block head;
	private int length;
	
	
	//构造方法
	public Guttler(Block head, List<Block> body, int length) 
	{
		this.head = head;
		this.body = body;
		this.length = length;
	}
	
	//Set,Get身体某一点
	public Block getBlock(int i)
	{
		return this.getBody().get(i);
	}
	
	public void setBlock(int i, Block block)
	{
		this.getBody().get(i).setX(block.getX());
		this.getBody().get(i).setY(block.getY());
	}
	
	//Setter,Getter方法
	public List<Block> getBody() 
	{
		return this.body;
	}

	public void setBody(List<Block> body)
	{
		this.body = body;
	}

	public Block getHead() 
	{
		return head;
	}

	public void setHead(Block head) 
	{
		this.head = head;
	}

	public int getLength() 
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}
	
}
