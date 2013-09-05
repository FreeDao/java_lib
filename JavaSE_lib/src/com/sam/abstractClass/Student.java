package com.sam.abstractClass;

public class Student extends Person{

	public Student(String name, int age, String occupation) {
		//需要明确调用抽象类中的构造方法。
		super(name, age, occupation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String talk() {
		// TODO Auto-generated method stub
		return "I am a student..";
	}

}
