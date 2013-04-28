/**
 * 
 */
package com.sam.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author yang_jun2
 *
 */
public class MapMessage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> messages = new HashMap<String, String>();
		messages.put("a", "message from a.");
		messages.put("b", "message from b.");
		messages.put("c", "message from c.");
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("get message from whom: ");
		String message = messages.get(scanner.nextLine());
		System.out.println(message);
		System.out.println(messages);
		
	}

}
