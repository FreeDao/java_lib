package com.sam.local;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double value = 15686679.426883;
		
		Locale usLocale = new Locale("en","US");
		Locale cnLocale = new Locale("zh","CN");
		
		NumberFormat dnf = NumberFormat.getNumberInstance();
		
	}

}
