/**
 * 
 */
package com.sam.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yang_jun2
 *
 */
public class FindRegular {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String regex = "http://\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}:[0-9]{1,4}/mip_adk/\\w+";
//		String input = "http://165.156.545.6:800/mip_adk/as";
		String regex = "http://\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}:[0-9]{1,4}/mip_adk/\\w+";
		String input = "http://12.156.56.56:8080/mip_adk/sasa";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		System.out.println(matcher.find());
		
	}

}
