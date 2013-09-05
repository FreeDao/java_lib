package com.sam.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyDemo {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//这个程序可以由命令行自变量 指定读取的 文档来源 src 和 写出的目的地 dest.  可惜我不会用
		
//		/home/sam/work/mtu.sh /home/sam/work/dest.txt

		CommonIO.dump(new FileInputStream(new File("/home/sam/work/mtu.sh")), new FileOutputStream(new File("/home/sam/work/dest.txt")));
	}

}
