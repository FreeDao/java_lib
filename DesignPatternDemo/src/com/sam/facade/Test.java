package com.sam.facade;

import javax.management.loading.PrivateClassLoader;

class CPU{
	public void start(){
		System.out.println("cpu start to work.");
	}
	
	public void working(){
		System.out.println("cpu is working.");
	}
}
class Memory{
	public void load(){
		System.out.println("memory is loading.");
	}
	
}

class Computer{
	private CPU cpu;
	private Memory memory;
	public Computer(){
		this.cpu = new CPU();
		this.memory = new Memory();
	}
	public void startComputer(){
		cpu.start();
		cpu.working();
		memory.load();
	}
	
}



public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Computer facade =  new Computer();
		facade.startComputer();
	}

}
