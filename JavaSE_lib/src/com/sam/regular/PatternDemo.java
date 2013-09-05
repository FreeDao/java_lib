package com.sam.regular;

import java.util.regex.Pattern;

public class PatternDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String regex = "http://[0-9]{1,3}\\.{3}[0-9]{1,3}:[0-9]{1,4}/mip_adk/.+";
		String input = "http://165.156.545.6:8080/mip_adk/as";
//		Pattern对象表示一个已编译的正则表达式
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		String items[] = p.split(input);
		for(String s :items){
			System.out.println(s);
		}
//		p = Pattern.compile(regex);
//		items = p.split(input);
//		for(String s :items){
//			System.out.println(s);
//		}
	}

}
