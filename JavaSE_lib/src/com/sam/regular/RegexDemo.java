/**************
 * this program hasnot finished
 * 
 * 
 * */
package com.sam.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDemo {
	//verify phone number
	public static boolean verifyPhoneNum(String input){
		boolean ck = false;
		String regex = "\\d\\d-\\d\\d";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if(matcher.matches()){
			ck = true;
		}
		return ck;
	}
}
