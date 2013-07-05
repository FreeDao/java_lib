package com.sam.db;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FileService {
	private DBOpenHelper openHelper;
	public FileService(Context context){
		openHelper = new DBOpenHelper(context);
	}
	
	//获取每条线程已经下载的文件长度
	public Map<Integer,Integer> getData(String path){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select threadid, downlength from SmartFileDownlog where downpath=?", new String[]{path});
		Map<Integer,Integer> data = new HashMap<Integer, Integer>();
		
		while(cursor.moveToNext()){
			data.put(cursor.getInt(0), cursor.getInt(1));
		}
		
		cursor.close();
		db.close();
		return data;
		
	}
	
	
	//保存每条线程已经下载的文件长度
	
	public void save(String path,Map<Integer, Integer> map){//int threadid, int position
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			db.execSQL("insert into SmartFileDownlog(downpath, threadid, downlength) values(?,?,?)",new Object[]{path, entry.getKey(),entry.getValue()});	
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	public void update(String path ,Map<Integer,Integer>map){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			db.execSQL("update SmartFileDownlog set downlength=? where downpath=? and threadid=?",new Object[]{path, entry.getKey(),entry.getValue()});	
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from SmartFileDownlog where downpath=?", new Object[]{path});
		db.close();
	}
	
	
	
	
	
}










