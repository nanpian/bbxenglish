package com.hyx.android.Game.util;
import android.app.Activity; 
import android.content.Context; 
import android.content.SharedPreferences; 
import android.content.SharedPreferences.Editor; 


public class AppPreferences {
    private static final String APP_SHARED_PREFS = "com.hyx.BBXGame_preferences"; //  Name of the file -.xml 
    private SharedPreferences appSharedPrefs; 
    private Editor prefsEditor; 

    public AppPreferences(Context context) 
    { 
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE); 
        this.prefsEditor = appSharedPrefs.edit(); 
    } 

    public String getPicHeight() { 
        return appSharedPrefs.getString("pic_height", "100"); 
    } 
    
    public int getAudioTime() {
    	return appSharedPrefs.getInt("audiotime", 5);
    }
    
    public String getClassFontSize() { 
        return appSharedPrefs.getString("class_fontsize", "20"); 
    } 
    
    public String getSubjectFontSize() { 
        return appSharedPrefs.getString("subject_fontsize", "23"); 
    } 
    
    public String getAnswerFontSize() { 
        return appSharedPrefs.getString("answer_fontsize", "35"); 
    } 
    
    public boolean getIsCancelVoice() { 
        return appSharedPrefs.getBoolean("iscancelvoice", false); 
    } 

    public void savePicHeight(String text) { 
        prefsEditor.putString("pic_height", text); 
        prefsEditor.commit(); 
    }
    
    public void saveClassFontSize(String text) { 
        prefsEditor.putString("class_fontsize", text); 
        prefsEditor.commit(); 
    }
    
    public void saveSubjectFontSize(String text) { 
        prefsEditor.putString("subject_fontsize", text); 
        prefsEditor.commit(); 
    }
    
    public void saveAnswerFontSize(String text) { 
        prefsEditor.putString("answer_fontsize", text); 
        prefsEditor.commit(); 
    }
    
    public void saveIsCancelVoice(boolean iscancelvoice) { 
    	prefsEditor.putBoolean("iscancelvoice", iscancelvoice);
    	prefsEditor.commit();
    }
    
    public void saveAudioTime(int audioTime) {
    	prefsEditor.putInt("audiotime", audioTime);
    	prefsEditor.commit();
    }
}
