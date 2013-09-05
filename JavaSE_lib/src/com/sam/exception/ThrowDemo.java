package com.sam.exception;

public class ThrowDemo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int a[]={1,2,3};
		
		try{
			for (int i =0; i < a.length; i++) {
				System.out.println("a=="+ a[i]);
				}
			int b = 5/0;
			
			
		}catch(Exception e){
			throw new Exception("eeeeeeeeeeeeeeeee :"+e.getMessage());
		}
		finally{
			System.out.println("finally");
		}
		System.out.println("end");
	}    

}
