package com.sam.collections;

import java.util.HashSet;
import java.util.Set;

public class TestMethodInCollection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> set1 = new HashSet<String>();
		set1.add("hp");
		set1.add("sam");
		set1.add("dell");
		set1.add("lenove");
		set1.add("tongfang");
		set1.add("hoes");
		set1.add("samsiung");
		set1.add("hp");
		System.out.println("set1 ===="+set1);
		System.out.println("the size of set1 is : "+set1.size());
		
		set1.remove("hp");
		System.out.println("set1 ===="+set1);
		System.out.println("the size of set1 is : "+set1.size());
		
		System.out.println("set1 has hp =="+set1.contains("hp"));
		set1.removeAll(set1);
		System.out.println(set1);
	}

}
