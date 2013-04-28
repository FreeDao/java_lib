package com.sam.template;

public abstract class Template {
	public abstract void print();
	public void update(){
		System.out.println("start to print...");
		for(int i = 0;i<5;i++) print();
	}
}
