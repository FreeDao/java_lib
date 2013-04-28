package com.sam.collections;

import java.util.ArrayList;
import java.util.List;


public class TestArrayLinkedList {

	public static void main(String[] args) {
		List<Integer> arraylist =  new ArrayList<Integer>();
		
		arraylist.add(1);//autoboxed to new Integer(1);自动装箱
		arraylist.add(3);
		arraylist.add(4);
		arraylist.add(2);
		arraylist.add(1);
		arraylist.add(5);
		arraylist.add(3);
		System.out.println(arraylist);
	}

}
