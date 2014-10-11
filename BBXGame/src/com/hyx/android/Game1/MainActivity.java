package com.hyx.android.Game1;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
	
	 private static final int LOAD_DISPLAY_TIME = 1500;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash);
			new Handler().postDelayed(new Runnable() {
		        public void run() {
		        	 Intent intent = new Intent();
                     intent.setClass(getApplicationContext(), LoginActivity.class);
                     startActivity(intent);
                     finish();
   	
		        }
        }, LOAD_DISPLAY_TIME); 
	        
	 }

}
