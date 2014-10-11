package com.hyx.android.Game1;

import com.hyx.android.Game1.ui.CheckBoxPreference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class settings extends PreferenceActivity{
	   private static CheckBoxPreference autoplay;
	private SharedPreferences shp;

	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			TextView view = (TextView) findViewById(android.R.id.title);
			view.setGravity(Gravity.CENTER);
	        addPreferencesFromResource(R.layout.settings);
	        autoplay = (CheckBoxPreference) findPreference("autoplay");  
	        shp = PreferenceManager.getDefaultSharedPreferences(this);  

	        autoplay.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
	        {

				@Override
				public boolean onPreferenceChange(Preference preference, Object objValue) {
					 Log.i("dewei222", "onPreferenceChange----->"+String.valueOf(preference.getKey()));  
					// TODO Auto-generated method stub
					if (preference == autoplay){                  //点击了    "打开wifi"  
						 Log.i("dewei222", "Wifi CB, and isCheckd = " + String.valueOf(objValue));
						 boolean tempValue =false;
						 if (String.valueOf(objValue).equalsIgnoreCase("true")) {
							 tempValue = true;
						 }
						 Editor editor=shp.edit();
						 editor.putBoolean("autoplay", tempValue);
						 editor.commit();
                         return true;
			        }
					return true;
				}
	        	
	        });
	    }

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        shp = PreferenceManager.getDefaultSharedPreferences(this);  
        boolean autoplay_checked = shp.getBoolean("autoplay", false);  
       Log.d("dewei222","autplay checked is " + autoplay_checked);
       autoplay.setChecked(autoplay_checked);
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
 
}
