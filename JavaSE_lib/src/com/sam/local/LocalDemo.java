package com.sam.local;

import java.util.Locale;

public class LocalDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Locale locale = Locale.getDefault();
		Locale locales[] = Locale.getAvailableLocales();
		
		System.out.println("Country is:"+locale.getCountry());
		System.out.println("DisplayLanguage is:"+locale.getDisplayLanguage(locale));
		System.out.println("language is:"+locale.getLanguage());
		
		for(Locale locale2 : locales){
			System.out.println(locale2);
		}
	}

}
