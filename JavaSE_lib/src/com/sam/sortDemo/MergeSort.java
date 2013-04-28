/**********
 * 归并排序
 * @author yang_jun,sam。
 * 比较难以理解，其4561，2352
 * 分为45，61，
 * 再4，5进行排序，1，6，再1456
 * 
 * 
 * 
 * ********/
package com.sam.sortDemo;

public class MergeSort {
	public static void mergeSort(int list[]){
		if(list.length >1){
			int firstHarf[] = new int [list.length/2];
			System.arraycopy(list, 0, firstHarf, 0, list.length/2);
			mergeSort(firstHarf);
			System.out.println("merge firsthalf");
			int secondHalf[] = new int [list.length - list.length/2];
			//被复制的数组，从第几个元素开始复制，要复制到的数组，从第几个元素开始粘贴，一共需要复制的元素个数
			System.arraycopy(list, list.length/2, secondHalf, 0, list.length - list.length/2);
			mergeSort(secondHalf);
			System.out.println("merge secondhalf");
			int temp[] = merge(firstHarf,secondHalf);
			System.arraycopy(temp, 0, list, 0, temp.length);
			System.out.println("temp copied");
		}
	}
	private static int [] merge(int list1[],int list2[]){
		int temp[]= new int[list1.length+list2.length];
		int current1= 0;//current index in list1
		int current2= 0;//current index in list2
		int current3= 0;//current index in temp
		while(current1<list1.length && current2<list2.length){
			if(list1[current1]<list2[current2]){//list1中仍有为移动的元素，将其移动到temp中。
				temp[current3++] = list1[current1++];
			}else{
				temp[current3++] = list2[current2++];
			}
		}
		while(current1<list1.length){
			temp[current3++] = list1[current1++];
		}
		while(current2<list2.length){
			temp[current3++] = list2[current2++];
		}
		return temp;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int list[] = {4,3,1,6,7,5,8,9,};
		mergeSort(list);
		for(int i=0;i<list.length;i++)
			System.out.print(list[i]+" ");
	}
}
