package com.sam.io;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sun.org.mozilla.javascript.internal.Context;

public class FileUtil {

	/**
	 * 
	 * @param src
	 * @param dest
	 */
	public static void copy(File src, File dest) {
		
		try {
			String name = src.getName();
			File target = new File(dest, name);
			if (src.isDirectory()) {
				mkdir(target);
				File[] listFiles = src.listFiles();
				if (listFiles != null) {
					for (File file : listFiles) {
						copy(file, target);
					}
				}
			} else {
				createNewFile(target);
				FileInputStream fis = new FileInputStream(src);
				FileOutputStream fos = new FileOutputStream(target);

				byte[] b = new byte[1024];
				int c = 0;
				while ((c = fis.read(b)) != -1) {
					fos.write(b, 0, c);
				}
				fos.flush();
				fos.close();
				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean mkdir(File dir) {
		boolean ret = false;
		if (!dir.exists()) {
			ret = dir.mkdirs();
		}
		return ret;
	}

	public static boolean createNewFile(File file) throws IOException {
		boolean ret = false;
		if (!file.exists()) {
			ret = file.createNewFile();
		}
		return ret;
	}

	
	public static void main(String args[]){
		System.out.println("--------------");
		File src = new File("/home/sam/work/git/androidhap/InfinitiInTouch/assets");
		File dest = new File("file:///data/data/com.infiniti.intouch/file");
		copy(src,dest);
		
	}
	
	
}