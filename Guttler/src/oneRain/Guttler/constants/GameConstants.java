package oneRain.Guttler.constants;

import android.app.Activity;
import android.content.SharedPreferences;
import oneRain.Guttler.logic.Operator;

public class GameConstants 
{
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;
	public static final int RIGHT = 4;
	
	public static final int GREEN_START = 1;
	public static final int RED_START = 2;
	public static final int YELLOW_START = 3;
	
	public static final int MSG_UPDATE = 1;
	public static final int MSG_GAMEOVER = 2;
	public static final int MSG_PAUSE = 3;
	
	public static int SIZE = 20;
	
	public static int UPDATE_RATE = 500;
	
	public static Operator operator = null;
	public static int[][] map;
	public static int curOrientation = 3;
	public static int score = 0;
	public static int eveScore = 10;
	
	//SharedPreferences
	public static SharedPreferences sp;
	
	//��ͼѡ���б�
	public static final String[] maps = {"\"2\"�͵�", "\"I\"�͵�", "\"U\"�͵�"};
	
	//��ͼѡ��
	public static int mapChoice = 4;
}
