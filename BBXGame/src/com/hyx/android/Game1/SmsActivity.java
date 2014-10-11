package com.hyx.android.Game1;

import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class SmsActivity  extends Activity {
	
	boolean Verified = false;
	private static final int LOAD_DISPLAY_TIME = 1500;
	// 鐭俊娉ㄥ唽锛岄殢鏈轰骇鐢熷ご鍍�
	private static final String[] AVATARS = {
		"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
		"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
		"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
	    
		SMSSDK.initSDK(this, "32c5bd4bff80", "49d96efe10de44ec0ed017c179637e35");
		new Handler().postDelayed(new Runnable() {
			        public void run() {
			        	
			    		SharedPreferences preferences=getSharedPreferences("sms_verify", Context.MODE_PRIVATE);
			    		boolean verified=preferences.getBoolean("verified", false );
			    		if(!verified) {
			    			RegisterPage registerPage = new RegisterPage();
			    			registerPage.setRegisterCallback(new EventHandler() {
			    			        public void afterEvent(int event, int result, Object data) {
			    			                if (result == SMSSDK.RESULT_COMPLETE) {
			    			                        @SuppressWarnings("unchecked")
			    			                        HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
			    			                        String country = (String) phoneMap.get("country");
			    			                        String phone = (String) phoneMap.get("phone");               
			    			                        // registerUser(country, phone);
			    			                        SharedPreferences preferences=getSharedPreferences("sms_verify",Context.MODE_PRIVATE);
			    			                        Editor editor=preferences.edit();
			    			                        editor.putBoolean("verified", true);
			    			                        editor.putString("mobienum", phone);
			    			                        editor.commit();
			    			                        Intent intent = new Intent();
			    					    			intent.putExtra("mobilenum", phone);
			    			                        intent.setClass(getApplicationContext(), RegisterActivity.class);
			    			                        startActivity(intent);
			    			                        finish();
			    			                       //Toast toast = Toast.makeText(getApplicationContext(), "success", 1000);
			    			                       //toast.show();
			    			                }
			    			        }
			    			});
			    			registerPage.show(getApplicationContext());
			    		} else {
			    			SharedPreferences sharedata = getSharedPreferences("sms_verify", 0);   
			    			String mobilenum = sharedata.getString("mobienum", null);
			    			Intent intent = new Intent();
			    			intent.putExtra("mobilenum", mobilenum);
			                intent.setClass(getApplicationContext(), RegisterActivity.class);
			                startActivity(intent);
			                finish();
			    		}
                      }
	        }, 0); 

		
	}
     
	
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		String avatar = AVATARS[id % 12];
		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
	}
	
}
