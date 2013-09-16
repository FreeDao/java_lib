package com.sam.facade;


public class MotorbikeAdapter implements Car{
	private Motorbike bike;
	public MotorbikeAdapter(Motorbike bike){
		this.bike = bike;
	}
	
	@Override
	public void startMove() {
		bike.startMove();
	}

	@Override
	public void moveTo() {
		bike.moveTo();
	}

	@Override
	public void stop() {
		bike.stop();
	}

}
