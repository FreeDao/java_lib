package com.sam.enumDemo;

enum Signal{
	GREEN,YELLOW,RED
}

public class TrafficLight {
	Signal color = Signal.GREEN;
	public void change(){
		switch (color){
		case RED:
			System.out.println("color is red.");
			break;
		case GREEN:
			System.out.println("color is green.");
			break;
		}
	}
}
