package com.hyx.android.Game.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class MemberManager {

	
	public static String getUsername(Activity activity){
		SharedPreferences sharedata = activity.getSharedPreferences(
				"login", 0);
		final String account = sharedata
				.getString("account", null);
		
		return account;
		
	}
	
	public static boolean checkValid(String username){
		if(username==null || "".equals(username.trim())){
			return false;
		}
		return true;
	}
	
}
