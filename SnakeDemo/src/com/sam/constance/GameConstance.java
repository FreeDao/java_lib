package com.sam.constance;

import java.lang.ref.Reference;

import android.content.SharedPreferences;

import com.sam.logic.Operator;

public class GameConstance {
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;
	
	public static final int GREEN_START = 1;
	public static final int RED_START = 2;
	public static final int YELLOW_START = 3;
	
	public static final int MSG_UPDATE = 1;
	public static final int MSG_GAMEOVER = 1;
	public static final int MSG_PAUSE = 1;
	
	public static final int SIZE = 20;	
	public static int UPDATE_RATE = 500;	
	
	public static Operator operator = null;
	public static int map[][];
	public static int curOrientation = 3;
	public static int score = 0;
	public static int eveScore = 10;
	
	public static SharedPreferences sp;
	
	public static final String maps[] = {"\"2\"","\"I\""};// TODO
	
	public static int mapChoice = 4;
	
}