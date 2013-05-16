package com.sam.snake;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SnakeActivity extends Activity {
	private ImageView welcomeImageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snake);
		initView();
	}

	private void initView() {
		welcomeImageView = (ImageView) findViewById(R.id.welcome);
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(5000);	// duration time
		welcomeImageView.startAnimation(animation);	// startAnimation
		
		animation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(SnakeActivity.this,MenuActivity.class);
				SnakeActivity.this.startActivity(intent);
				SnakeActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snake, menu);
		return true;
	}

}
