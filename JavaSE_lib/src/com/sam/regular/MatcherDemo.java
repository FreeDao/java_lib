package com.sam.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String regex ="http://[[0-9]{1,3}\\.]{3}[0-9]{1,3}:[0-9]{1,4}/mip_adk/.+";
		String input ="http://165.156.545.6:8080/mip_adk/as";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		int index = 0;
//		public boolean  find() 查找输入子串中与该模式匹配的下一个字符串
		while(matcher.find()){
			index++;
			System.out.println(index+" Matcher :");
			System.out.println("Start at : "+matcher.start());
			System.out.println("end at : "+matcher.end());
		}
		
	}

}
