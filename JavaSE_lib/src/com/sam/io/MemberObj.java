package com.sam.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MemberObj implements Serializable{
	private static String number;
	private String name;
	private int age;
	public MemberObj(String number,String name,int age){
		this.number = number;
		this.name = name;
		this.age = age;
	}
	
	public void save(){
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(number));
			output.writeObject(this);
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static MemberObj load(){
		MemberObj member = null;
		
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(number));
			member = (MemberObj)input.readObject();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return member;
	}
	
	@Override
	public String toString() {
		String result = "number: "+number+" \n name : "+name+"\n age : "+age;
		return result;
	}

}
