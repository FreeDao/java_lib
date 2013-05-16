
package oneRain.Guttler.ui;

import oneRain.Guttler.R;
import oneRain.Guttler.constants.GameConstants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends Activity 
{
	private ImageView welcomeImageView = null;
	
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        initView();
    }
	
	private void initView()
    {
    	welcomeImageView = (ImageView)findViewById(R.id.welcome);
    	
    	AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
    	animation.setDuration(3000);
    	welcomeImageView.startAnimation(animation);
    	animation.setAnimationListener(new AnimationListener()
    	{
			public void onAnimationEnd(Animation animation) 
			{				
				Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
			}

			public void onAnimationRepeat(Animation animation) 
			{
				
			}

			public void onAnimationStart(Animation animation) 
			{
				
			}
    	});
    }
}
