/*******
 * 快速排序，选择一个主元，快速分为两部分，
 * 第一部分小于，第二部分大于，然后递归的调用他们
 * 快速排序的空间效率高于归并排序
 * 
 * ********/
package com.sam.sortDemo;

public class QuickSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int list[] = { 1, 2, 3, 1, 2, 321, 12, 12, 321, 12, 121, 321, 332, 12,
				12, 12, 12, 12, 123 };
		quickSort(list);
		for(int i= 0;i<list.length;i++)
			System.out.print(list[i]+" ");
		
	}

	private static void quickSort(int[] list) {
		quickSort(list, 0, list.length-1);
		
		
	}

	private static void quickSort(int[] list, int first, int last) {
		if(last>first){
			int pivotIndex = partition(list,first,last);
			quickSort(list, first, pivotIndex-1);
			quickSort(list,pivotIndex+1,last);
		}
	}

	private static int partition(int list[], int first, int last) {
		int pivot = list[first];//choose the first element as the pivot
		int low = first+1;
		int high = last;
		while(high>low){
			while(low<=high&& list[low]<=pivot)
				low++;
			
			while(low<=high && list[high]>pivot)
				high--;
			if(high > low){
				int temp = list[high];
				list[high] = list[low];
				list[low] = temp;
			}
		}
		while(high>first && list[high]>=pivot)
			high--;
		if(pivot > list[high]){
			list[first] = list[high];
			list[high] = pivot;
			return high;
		}else{
			return first;
		}
	}

}














