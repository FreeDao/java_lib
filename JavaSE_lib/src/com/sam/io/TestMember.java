package com.sam.io;

public class TestMember {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Member members[] = {new Member("12","sam",23),new Member("13","hello",24),new Member("14","oop",25)};
		
		for(Member member: members){
			member.save();
			
		}
		System.out.println(Member.load("1222"));
		System.out.println(Member.load("13"));
		System.out.println(Member.load("14"));
		
	}

}
