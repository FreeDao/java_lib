package com.sam.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
// This file used by testMember.java
public class Member {
	private String number;
	private String name;
	private int age;
	static File file = new File("/home/sam/work/test.txt");
	public Member(String number,String name,int age){
		this.number = number;
		this.name = name;
		this.age = age;
	}
	public void save(){
		try {
			
			DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
			output.writeUTF(number);
			output.writeUTF(name);
			output.writeInt(age);
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static Member load(String number){
		Member member = null;
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(number));
			member = new Member(input.readUTF(),input.readUTF(),input.readInt());
			input.close();
			
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
