package com.sam.abstractClass;

public abstract class Person {
	String name;
	int age;
	String occupation;
	
	public abstract String talk();
	//抽象类可以拥有构造方法，但必须在子类中被调用
	public Person(String name,int age,String occupation){
		this.name = name;
		this.age =age;
		this.occupation = occupation;
		
	}

}
