/**
 * 之所以做食物类是想以后可能会做不同的食物，不同类型不同状态的食物，与方块类相同
 */
package oneRain.Guttler.model;

//对食物专门定义一个类的原因是方便以后对食物类扩展，比如不同的食物得到不同的分数
public class Food 
{
	private Block food;

	public Food() 
	{
		super();
	}

	public Food(Block food) 
	{
		this.food = food;
	}
	
	//Setter, Getter方法
	public Block getFood() 
	{
		return food;
	}

	public void setFood(Block food) 
	{
		this.food = food;
	}
}
