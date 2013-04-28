package com.airbiquity.connectionmgr.msp;

public class IdGenerator {
	
	private int index;
	
	private static IdGenerator _INSTANCE = new IdGenerator();
	
	private IdGenerator(){
		index = 1;
	};
	
	public static IdGenerator getInstance(){
		return _INSTANCE;
	}
	
	public synchronized int generateId(){
		if (index < 0) {
			index = 1;
		}
		return index++;
	}

}
