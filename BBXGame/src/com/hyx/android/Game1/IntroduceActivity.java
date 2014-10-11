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

public class IntroduceActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduce2);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.introduce2, null).findViewById(
				R.id.layIntroduce);
		
		
		 
		TextView txtMore = (TextView)  layout.findViewById(R.id.txtMore);
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
	        
       
		 
		 
		// lv.setTextColor(Color.WHITE);
    }
}
