/**
 * 
 */
package com.sam.snakedemo;

import java.util.ArrayList;
import java.util.Random;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author yang_jun2
 *
 */
public class SnakeView extends TileView{
	private static final String TAG = "SnakeView";
	private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    private int mDirection = NORTH;
    private int mNextDirection = NORTH;
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;
    private static final int RED_STAR = 1;
    private static final int YELLOW_STAR = 2;
    private static final int GREEN_STAR = 3;
    private long mScore = 0;
    private long mMoveDelay = 600;
    private long mLastMove;
    
	private TextView mStatusText;
	 private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
	    private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();

	    /**
	     * Everyone needs a little randomness in their life
	     */
	    private static final Random RNG = new Random();

	    /**
	     * Create a simple handler that we can use to cause animation to happen.  We
	     * set ourselves as a target and we can use the sleep()
	     * function to cause an update/invalidate to occur at a later date.
	     */
	    private RefreshHandler mRedrawHandler = new RefreshHandler();
	    class RefreshHandler extends Handler {

	        @Override
	        public void handleMessage(Message msg) {
	            SnakeView.this.update();
	            SnakeView.this.invalidate();
	        }

	        public void sleep(long delayMillis) {
	        	this.removeMessages(0);
	            sendMessageDelayed(obtainMessage(0), delayMillis);
	        }
	    };
	/**
	 * @param context
	 * @param attrs
	 */
	public SnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSnakeView();
	}
	
	 public SnakeView(Context context, AttributeSet attrs, int defStyle) {
	    	super(context, attrs, defStyle);
	    	initSnakeView();
	    }
	 private void initSnakeView() {
	        setFocusable(true);

	        Resources r = this.getContext().getResources();
	        
	        resetTiles(4);
	        loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
	        loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
	        loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
	    	
	    }
	 
	 
	 private void initNewGame() {
	        mSnakeTrail.clear();
	        mAppleList.clear();

	        // For now we're just going to load up a short default eastbound snake
	        // that's just turned north

	        
	        mSnakeTrail.add(new Coordinate(7, 7));
	        mSnakeTrail.add(new Coordinate(6, 7));
	        mSnakeTrail.add(new Coordinate(5, 7));
	        mSnakeTrail.add(new Coordinate(4, 7));
	        mSnakeTrail.add(new Coordinate(3, 7));
	        mSnakeTrail.add(new Coordinate(2, 7));
	        mNextDirection = NORTH;

	        // Two apples to start with
	        addRandomApple();
	        addRandomApple();

	        mMoveDelay = 600;
	        mScore = 0;
	    }
	  private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
	        int count = cvec.size();
	        int[] rawArray = new int[count * 2];
	        for (int index = 0; index < count; index++) {
	            Coordinate c = cvec.get(index);
	            rawArray[2 * index] = c.x;
	            rawArray[2 * index + 1] = c.y;
	        }
	        return rawArray;
	    }
	  public Bundle saveState() {
	        Bundle map = new Bundle();

	        map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
	        map.putInt("mDirection", Integer.valueOf(mDirection));
	        map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
	        map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
	        map.putLong("mScore", Long.valueOf(mScore));
	        map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

	        return map;
	    }
	  
	  private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
	        ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

	        int coordCount = rawArray.length;
	        for (int index = 0; index < coordCount; index += 2) {
	            Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
	            coordArrayList.add(c);
	        }
	        return coordArrayList;
	    }
	  
	  public void restoreState(Bundle icicle) {
	        setMode(PAUSE);

	        mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
	        mDirection = icicle.getInt("mDirection");
	        mNextDirection = icicle.getInt("mNextDirection");
	        mMoveDelay = icicle.getLong("mMoveDelay");
	        mScore = icicle.getLong("mScore");
	        mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
	    }
	  
	  public boolean onKeyDown(int keyCode, KeyEvent msg) {

	        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
	            if (mMode == READY | mMode == LOSE) {
	                /*
	                 * At the beginning of the game, or the end of a previous one,
	                 * we should start a new game.
	                 */
	                initNewGame();
	                setMode(RUNNING);
	                update();
	                return (true);
	            }

	            if (mMode == PAUSE) {
	                /*
	                 * If the game is merely paused, we should just continue where
	                 * we left off.
	                 */
	                setMode(RUNNING);
	                update();
	                return (true);
	            }

	            if (mDirection != SOUTH) {
	                mNextDirection = NORTH;
	            }
	            return (true);
	        }

	        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
	            if (mDirection != NORTH) {
	                mNextDirection = SOUTH;
	            }
	            return (true);
	        }

	        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
	            if (mDirection != EAST) {
	                mNextDirection = WEST;
	            }
	            return (true);
	        }

	        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
	            if (mDirection != WEST) {
	                mNextDirection = EAST;
	            }
	            return (true);
	        }

	        return super.onKeyDown(keyCode, msg);
	    }


	  public void setTextView(TextView newView) {
	        mStatusText = newView;
	    }
	  public void setMode(int newMode) {
	        int oldMode = mMode;
	        mMode = newMode;

	        if (newMode == RUNNING & oldMode != RUNNING) {
	            mStatusText.setVisibility(View.INVISIBLE);
	            update();
	            return;
	        }

	        Resources res = getContext().getResources();
	        CharSequence str = "";
	        if (newMode == PAUSE) {
	            str = res.getText(R.string.mode_pause);
	        }
	        if (newMode == READY) {
	            str = res.getText(R.string.mode_ready);
	        }
	        if (newMode == LOSE) {
	            str = res.getString(R.string.mode_lose_prefix) + mScore
	                  + res.getString(R.string.mode_lose_suffix);
	        }

	        mStatusText.setText(str);
	        mStatusText.setVisibility(View.VISIBLE);
	    }
	   private void addRandomApple() {
	        Coordinate newCoord = null;
	        boolean found = false;
	        while (!found) {
	            // Choose a new location for our apple
	            int newX = 1 + RNG.nextInt(mXTileCount - 2);
	            int newY = 1 + RNG.nextInt(mYTileCount - 2);
	            newCoord = new Coordinate(newX, newY);

	            // Make sure it's not already under the snake
	            boolean collision = false;
	            int snakelength = mSnakeTrail.size();
	            for (int index = 0; index < snakelength; index++) {
	                if (mSnakeTrail.get(index).equals(newCoord)) {
	                    collision = true;
	                }
	            }
	            // if we're here and there's been no collision, then we have
	            // a good location for an apple. Otherwise, we'll circle back
	            // and try again
	            found = !collision;
	        }
	        if (newCoord == null) {
	            Log.e(TAG, "Somehow ended up with a null newCoord!");
	        }
	        mAppleList.add(newCoord);
	    }
	   
	   public void update() {
	        if (mMode == RUNNING) {
	            long now = System.currentTimeMillis();

	            if (now - mLastMove > mMoveDelay) {
	                clearTiles();
	                updateWalls();
	                updateSnake();
	                updateApples();
	                mLastMove = now;
	            }
	            mRedrawHandler.sleep(mMoveDelay);
	        }

	    }
	   private void updateWalls() {
	        for (int x = 0; x < mXTileCount; x++) {
	            setTile(GREEN_STAR, x, 0);
	            setTile(GREEN_STAR, x, mYTileCount - 1);
	        }
	        for (int y = 1; y < mYTileCount - 1; y++) {
	            setTile(GREEN_STAR, 0, y);
	            setTile(GREEN_STAR, mXTileCount - 1, y);
	        }
	    } private void updateApples() {
	        for (Coordinate c : mAppleList) {
	            setTile(YELLOW_STAR, c.x, c.y);
	        }
	    }

	    private void updateSnake() {
	        boolean growSnake = false;

	        // grab the snake by the head
	        Coordinate head = mSnakeTrail.get(0);
	        Coordinate newHead = new Coordinate(1, 1);

	        mDirection = mNextDirection;

	        switch (mDirection) {
	        case EAST: {
	            newHead = new Coordinate(head.x + 1, head.y);
	            break;
	        }
	        case WEST: {
	            newHead = new Coordinate(head.x - 1, head.y);
	            break;
	        }
	        case NORTH: {
	            newHead = new Coordinate(head.x, head.y - 1);
	            break;
	        }
	        case SOUTH: {
	            newHead = new Coordinate(head.x, head.y + 1);
	            break;
	        }
	        }

	        // Collision detection
	        // For now we have a 1-square wall around the entire arena
	        if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > mXTileCount - 2)
	                || (newHead.y > mYTileCount - 2)) {
	            setMode(LOSE);
	            return;

	        }

	        // Look for collisions with itself
	        int snakelength = mSnakeTrail.size();
	        for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
	            Coordinate c = mSnakeTrail.get(snakeindex);
	            if (c.equals(newHead)) {
	                setMode(LOSE);
	                return;
	            }
	        }

	        // Look for apples
	        int applecount = mAppleList.size();
	        for (int appleindex = 0; appleindex < applecount; appleindex++) {
	            Coordinate c = mAppleList.get(appleindex);
	            if (c.equals(newHead)) {
	                mAppleList.remove(c);
	                addRandomApple();
	                
	                mScore++;
	                mMoveDelay *= 0.9;

	                growSnake = true;
	            }
	        }

	        // push a new head onto the ArrayList and pull off the tail
	        mSnakeTrail.add(0, newHead);
	        // except if we want the snake to grow
	        if (!growSnake) {
	            mSnakeTrail.remove(mSnakeTrail.size() - 1);
	        }

	        int index = 0;
	        for (Coordinate c : mSnakeTrail) {
	            if (index == 0) {
	                setTile(YELLOW_STAR, c.x, c.y);
	            } else {
	                setTile(RED_STAR, c.x, c.y);
	            }
	            index++;
	        }

	    }

	    /**
	     * Simple class containing two integer values and a comparison function.
	     * There's probably something I should use instead, but this was quick and
	     * easy to build.
	     * 
	     */
	    private class Coordinate {
	        public int x;
	        public int y;

	        public Coordinate(int newX, int newY) {
	            x = newX;
	            y = newY;
	        }

	        public boolean equals(Coordinate other) {
	            if (x == other.x && y == other.y) {
	                return true;
	            }
	            return false;
	        }

	        @Override
	        public String toString() {
	            return "Coordinate: [" + x + "," + y + "]";
	        }
	    }
	    

}










