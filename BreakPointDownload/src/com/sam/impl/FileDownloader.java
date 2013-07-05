package com.sam.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sam.db.FileService;

import android.content.Context;
import android.util.Log;

public class FileDownloader {
	private static final String TAG = "FileDownloader";
	private Context context;
	private FileService fileService;
	private int downloadSize = 0;	//已下载长度
	private int fileSize = 0;
	private DownloadThread threads[];
	private File saveFile;
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
	private int block;
	private String downloadUrl;
	
	public int getThreadSize(){
		return this.threads.length;
	}
	
	public int getFileSize(){
		return this.fileSize;
	}
	
	protected synchronized void apend(int size){
		downloadSize += size;
	}
	
	public int download(DownloadProgressListener listener) throws Exception{
		RandomAccessFile ranOut = new RandomAccessFile(this.saveFile, "rw");
		if(this.fileSize > 0){
			ranOut.setLength(this.fileSize);
		}
		ranOut.close();
		URL url = new URL(this.downloadUrl);
		if(this.data.size()!= this.threads.length){
			this.data.clear();
		}
		
		return block;
		
	}
	
	public FileDownloader(Context context,String downloadUrl, File fileSaveDir, int threadNum){
		try {
			Log.i(TAG, "----------FileDownloader----------");
			this.context = context;
			this.downloadUrl = downloadUrl;
			URL url = new URL(this.downloadUrl);
			if(!fileSaveDir.exists()){
				fileSaveDir.mkdirs();
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", downloadUrl); 
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			Log.i(TAG, "--getResponseCode---: "+conn.getResponseCode());
			
			if(conn.getResponseCode() == 200){
				this.fileSize = conn.getContentLength();
				if (this.fileSize <= 0) throw new RuntimeException("Unkown file size ");
				
				String filename = getFileName(conn);
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			
			
		}
		
		
	}

//	public static void printResponseHeader(HttpURLConnection http){
//		Map<String , String> header = getH
//	}
	
	
	private String getFileName(HttpURLConnection conn){
		String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/')+1);
		Log.i(TAG, "-----------getFileName--------"+filename);
		if(filename == null || "".equals(filename.trim())){// 获取不到文件名
			for(int i = 0;;i++){
				String mine = conn.getHeaderField(i);
				if(mine == null) break;
				if("content-disposition".equalsIgnoreCase(conn.getHeaderFieldKey(i))){
					
				}
				
			}
			
		}
		
		return null;
	}
	
	
}




















