package com.kpfunusa;

import com.kpfunusa.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;

public class SplashScreen extends Activity {
	// Splash screen timer
	public static int height;
	public static int width;
	
	private static int SPLASH_TIME_OUT = 1;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.splash_screen_layout);

		if(android.os.Build.VERSION.SDK_INT >= 13){
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}else{
			Display display = getWindowManager().getDefaultDisplay(); 
			width = display.getWidth();  // deprecated
			height = display.getHeight();
			
		}
		
		
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				
				Intent in = getIntent();
				String tab = in.getStringExtra("tab");
				
				
				Intent i = new Intent(SplashScreen.this, ActivityLogin.class);
				i.putExtra("tab", tab);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}
}