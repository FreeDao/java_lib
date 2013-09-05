package com.sam.collections;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestLinkedHashSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> set = new LinkedHashSet<String>();
		set.add("china");
		set.add("london");
		set.add("nanjing");
		set.add("hoperun");
		set.add("hoperun");
		set.add("hoperun");
		set.add("ton");
		set.add("omer");
		set.add("flex");
		set.add("sam");
		System.out.println(set);// but I donot know why it is unordered
		
		

}}