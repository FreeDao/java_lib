package com.sam.exception;

public class TryCatchDemo {
	public static void main(String args[]){
		int a[] = {1,1,2,3};
		try {
			int b =5/0; 
			for (int i =0; i < a.length; i++) {
				System.out.println("a=="+ a[i]);
				}
//			int b =5/0; 
		} 
		catch (ArithmeticException e) {
			System.out.println("ArithmeticException...b==");
			} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException...");
			} catch (Exception e) {
			System.out.println("error,but i did it");
			} finally {
			System.out.println("mainfinally...");
			}
		int c= method1(); 
		System.out.println("end...c=="+c);
	}
	public static int method1(){
		try{
			int d =5/0;
		}catch (ArithmeticException e) {
			System.out.println("ArithmeticException...d==");
			return 3;
			} catch (Exception e) {
			return 4;
			} finally {
			System.out.println("method1finally...");
			return 5;
			} 
	}
	
}
