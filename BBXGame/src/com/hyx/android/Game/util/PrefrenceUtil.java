package com.hyx.android.Game.util;

import android.content.Context;

public class PrefrenceUtil {
	
	public static int getValue(Context context, String node, String key ,int defaultValue){
		return context.getSharedPreferences(node, Context.MODE_PRIVATE).getInt(key, defaultValue);
	}
	
	
	public static String getValue(Context context, String node, String key ,String defaultValue){
		return context.getSharedPreferences(node, Context.MODE_PRIVATE).getString(key, defaultValue);
	}
	
	public static boolean getValue(Context context, String node, String key ,boolean defaultValue){
		return context.getSharedPreferences(node, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
	}
}
