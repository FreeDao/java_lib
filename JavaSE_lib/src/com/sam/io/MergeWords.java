package com.sam.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MergeWords {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileManager a = new FileManager("D:/a.txt",new char[]{'\n'});
		FileManager b = new FileManager("D:/b.txt",new char[]{'\n'});
		
		FileWriter c =  new FileWriter("D:/c.txt");
		String aWord = null;
		String bWord = null;
		
		while((aWord = a.nextWord())!=null){
			c.write(aWord+"*");
			bWord = b.nextWord();
			if(bWord != null){
				c.write(bWord+"$");
			}
		}

			while((bWord = b.nextWord())!=null){
				c.write(bWord+"\n");
			}
			c.close();
	}

}

class FileManager{
	String words[] = null;
	int pos = 0;
	
	public FileManager(String filename,char seperators[]) throws IOException{
		File f = new File(filename);
		FileReader reader = new FileReader(f);
		System.out.println("sam  f.length:"+f.length());
		char buf[] = new char[(int)f.length()];
		int len = reader.read(buf);
		String results = new String(buf,0,len);//截取字符串
		String regex = null;
		if(seperators.length>1){
			regex = ""+seperators[0]+"|"+seperators[1];
		}else{
			regex = ""+seperators[0];
		}
		System.out.println("regex==="+regex);
		words = results.split("");
		System.out.println(words[1]);
	}
	
	public String nextWord(){
		if(pos == words.length){
			return null;
		}
		return words[pos++];
	}
	
	
}
