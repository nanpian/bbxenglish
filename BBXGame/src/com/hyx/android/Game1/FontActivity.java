package com.hyx.android.Game1;

import com.hyx.android.Game.util.AppPreferences;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FontActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce1);
        /*LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflater.inflate(
				R.introduce1, null).findViewById(
				R.id.layIntroduce);*/
		
		//Log.i("test","getApplicationContext()="+getApplicationContext());
		 final AppPreferences appPrefs = new AppPreferences(getApplicationContext()); 
		 final EditText txtFontSize = (EditText) findViewById(R.id.txtFontSize);
		 //Log.i("test","appPrefs.getClassFontSize()="+appPrefs.getClassFontSize());
		 //if(appPrefs.getClassFontSize()!=null)
		 txtFontSize.setText(appPrefs.getClassFontSize());

		 
		 final EditText txtSubjectFontSize = (EditText) findViewById(R.id.txtSubjectFontSize);
		 txtSubjectFontSize.setText(appPrefs.getSubjectFontSize());
		 
		 final EditText txtAnswerFontSize = (EditText) findViewById(R.id.txtAnswerFontSize);
		 txtAnswerFontSize.setText(appPrefs.getAnswerFontSize());
		 
		 final EditText txtPicHeight = (EditText) findViewById(R.id.txtPicHeight);
		 txtPicHeight.setText(appPrefs.getPicHeight());
		 
		 final EditText audioTime = (EditText) findViewById(R.id.audiotime);
		 audioTime.setText(String.valueOf(appPrefs.getAudioTime()));
		 
		 Button btnSetup = (Button)  findViewById(R.id.btnSetup);
		 btnSetup.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 appPrefs.saveClassFontSize(txtFontSize.getText().toString());
				 appPrefs.saveSubjectFontSize(txtSubjectFontSize.getText().toString());
				 appPrefs.saveAnswerFontSize(txtAnswerFontSize.getText().toString());
				 
				 appPrefs.savePicHeight(txtPicHeight.getText().toString());
				 appPrefs.saveAudioTime(Integer.valueOf(audioTime.getText().toString()));
			 }
		 });
		 
		TextView txtMore = (TextView)  findViewById(R.id.txtMore);
		//创建一个 SpannableString对象  
		Spannable  sp = new SpannableString("点击访问全部内容");   
        //设置超链接  
        sp.setSpan(new URLSpan("http://ebbx.taobao.com"), 0, 8,   
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   
        //设置高亮样式一  
        sp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //sp.setSpan(new BackgroundColorSpan(Color.BLUE), 0 ,5 ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   
        //SpannableString对象设置给TextView  
        txtMore.setText(sp);   
        //设置TextView可点击  
        txtMore.setMovementMethod(LinkMovementMethod.getInstance());   
	        
        
		 final Button btnAudioIntro = (Button)  findViewById(R.id.btnAudioIntro);
		 btnAudioIntro.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				//mMPlayer.playIntroduceVoice();
				 if(btnAudioIntro.getText().toString().equals(getString(R.string.cancelvoice))){
					 btnAudioIntro.setText(getString(R.string.recovervoice));
					 appPrefs.saveIsCancelVoice(true);
				 }else{
					 btnAudioIntro.setText(getString(R.string.cancelvoice));
					 appPrefs.saveIsCancelVoice(false);
				 }
			 }
		 });
		 
		 
		// lv.setTextColor(Color.WHITE);
    }
}
