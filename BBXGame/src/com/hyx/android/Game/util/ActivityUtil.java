package com.hyx.android.Game.util;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtil {
	public static void noTitleBar(Activity activity){
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	}
	
	public static void noNotificationBar(Activity activity){
		final Window win = activity.getWindow(); 
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
