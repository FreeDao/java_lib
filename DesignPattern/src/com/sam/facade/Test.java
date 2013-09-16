package com.sam.facade;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Motorbike bike = new Motorbike();
		Car car = new MotorbikeAdapter(bike);
		car.startMove();
		car.moveTo();
		car.stop();
		
	}

}
