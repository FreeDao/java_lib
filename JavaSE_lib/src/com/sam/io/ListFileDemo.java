package com.sam.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ListFileDemo {
	private static File f = null;
	private static File f1 = null;

	public static void main(String[] args) throws IOException {
		// f = new File("/home/sam/work/io");
		// f.mkdirs();
		// printAllFiles();

		String f1Path = "/home/sam/work/io";
		f1 = new File(f1Path);
		deleteAll(f1);
	}

	private static void deleteAll(File f1) {
		System.out.println("f1.exists(): "+f1.exists());
		if (f1.exists()) {
			boolean isF1Directory = f1.isDirectory();
			boolean isF1File = f1.isFile();
			if (isF1Directory) {
				File files[] = f1.listFiles();
				for (File file : files) {
					if (file.isFile()) {
						file.delete();
					}
					if (file.isDirectory() && file.listFiles() == null) {
						System.out.println("empty Files");
						file.delete();
					} else {
						String subFilePath = file.getAbsolutePath();
						File subFile = new File(subFilePath);
						deleteAll(subFile);
					}
					file.delete();
				}
			}
			f1.delete();
		} else {
			f1.mkdirs();
			System.out.println("f1.get."+f1.getAbsolutePath());
			System.out.println("This file do not exists.");
		}

	}

	private static void printAllFiles() {
		if (f.isDirectory()) {
			File[] listFile = f.listFiles(); // 不能遍历子目录
			for (int i = 0; i < listFile.length; i++) {
				System.out.println(listFile[i]);
			}

			System.out.println("\n");
		} else {
			System.out.println("/home/sam/work/io is not a directory.");
		}
		System.out.println("------------fen ge xian-----------");
	}

}
