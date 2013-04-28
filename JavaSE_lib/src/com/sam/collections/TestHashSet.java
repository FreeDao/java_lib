/********
 * 
 * this program shows shows how to use set,
 * notice that:the element cannot be duplicated
 * 			the element was not sorted
 * 			if you want it ordered ,you can use LinkedHashSet
 * LinkedHashSet:保持被插入的顺序
 * TreeSet:排序
 * *****/

package com.sam.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestHashSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> set = new HashSet<String>();
		//add string to set
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
		
		Iterator<String> iterator = set.iterator();
		
		while(iterator.hasNext()){
			System.out.print(iterator.next()+" ");
		}
		
		System.out.println();
		for(Object e:set){
			System.out.print(e+" ");
		}
		
	}

}
