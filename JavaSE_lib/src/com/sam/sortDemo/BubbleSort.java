/********
 * 
 * Bubble sort
 * 
 * **/
package com.sam.sortDemo;

public class BubbleSort {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int list[] = {2,1,2,4,4,44,134,1,314,1,1,34,31,3,134,3,1,12,213};
		for(int k = 1;k<list.length;k++){
			for(int i = 0;i<list.length - k;i++){
				if(list[i] > list[i+1]){
					int temp = list[i];
					list[i] = list[i+1];
					list[i+1] = temp;
				}
			}
		}
		for (int j = 0;j < list.length;j++){
			System.out.print(list[j]+" ");
		}
		
	}

}
