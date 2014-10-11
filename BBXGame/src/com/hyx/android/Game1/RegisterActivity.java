package com.hyx.android.Game1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyx.android.Game.util.DeviceUuidFactory;
import com.hyx.android.Game.util.RankManager;
import com.hyx.android.Game.util.Utils;
import com.hyx.android.Game1.R;

public class RegisterActivity extends BaseActivity {
	
	private static String ajaxHost = "http://www.tgbbx.com/api/api6_2.php";
	private static final String Tag = "RegisterActivity";
	private EditText register_username;
	private EditText register_passwd;
	private String register_phonenum;
	private EditText reregister_passwd;
	private Button register_submit;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.register);
		
		register_username=(EditText)findViewById(R.id.register_username);
		register_passwd=(EditText)findViewById(R.id.register_passwd);
		reregister_passwd=(EditText)findViewById(R.id.reregister_passwd);
		register_submit=(Button)findViewById(R.id.register_submit);
		register_phonenum = getIntent().getStringExtra("mobilenum");
		Log.d(Tag,"the phone num is " + register_phonenum);
		register_username.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(register_username.getText().toString().trim().length()<4){
						Toast.makeText(RegisterActivity.this, "用户名不能小于4个字符", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
		});
		register_passwd.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(register_passwd.getText().toString().trim().length()<6){
						Toast.makeText(RegisterActivity.this, "密码不能小于8个字符", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
		});
		reregister_passwd.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(!reregister_passwd.getText().toString().trim().equals(register_passwd.getText().toString().trim())){
						Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show(); 
					}
				}
			}
			
		});
		register_submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
	       		  HttpClient client = new DefaultHttpClient();
	       		  String checkcode= Utils.MD5(register_username.getText().toString().trim() + register_passwd.getText().toString().trim() + "defabc");
	       		  Log.i(Tag,"BBX-reg:"+ajaxHost+"?action=reg&username="+register_username.getText().toString().trim()+"&pwd="+register_passwd.getText().toString()+"&tel="+register_phonenum+"&checkcode="+checkcode);
	       		  SharedPreferences sharedata = getSharedPreferences("sms_verify", 0);   
    			  String mobilenum = sharedata.getString("mobienum", null);
	       		  HttpGet request = new HttpGet(ajaxHost+"?action=reg&username="+register_username.getText().toString().trim()+"&pwd="+register_passwd.getText().toString()+"&tel="+register_phonenum+"&checkcode="+checkcode);
	       		  try {
						HttpResponse response = client.execute(request);
						  /* response code*/
					       BufferedReader rd = new BufferedReader(
					      new InputStreamReader(response.getEntity().getContent()));
					     String line = "";
					     StringBuilder result=new StringBuilder();
					      while ((line = rd.readLine()) != null) {
					    	  result.append(line);
					     }
					      Log.d(Tag,"The rersult server return is "+result);
					      String res = result.toString().replace("var data=", "");					    
						  JSONObject obj = new JSONObject(res);		
						  SharedPreferences.Editor sharedata2 = getSharedPreferences("register", 0).edit();
					      if("true".equals(obj.getString("success"))){	    	  
					    	  sharedata2.putString("register_username", register_username.getText().toString().trim()); 
					    	  sharedata2.putString("register_passwd", register_passwd.getText().toString());   
					    	  sharedata2.putString("mobile_number", "false");   
					    	  sharedata2.commit();  
					    	  Toast.makeText(RegisterActivity.this, "注册成功,将跳转到登陆界面！", Toast.LENGTH_SHORT).show();
					    	  
					    	  Intent intent2=new Intent(RegisterActivity.this,LoginActivity.class);
							  startActivity(intent2);
							  finish();
					
					      }else{
					    	  Toast.makeText(RegisterActivity.this, "注册失败，请重新注册，或检查网络！", Toast.LENGTH_SHORT).show();
		        		   }
	       		
					      String info = obj.getString("info").toString();
				    	  new AlertDialog.Builder(RegisterActivity.this)         
	        			  /**设置标题**/         
	        			  .setTitle("提示")
	        			  /**设置icon**/
	        			  .setIcon(android.R.drawable.ic_dialog_info)
	        			  /**设置内容**/
	        			  .setMessage(info)
	        		
	        			  .setPositiveButton("确定", new DialogInterface.OnClickListener(){
	        				  
	        				  public void onClick(DialogInterface dialog, int which) {                     
	        					  /**关闭窗口**/
	        					 // finish();
	        					  //login_edit_pwd.setText("");
	        					 }}).show();
					      
					      
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();						
					} 
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		});
	}
	
	private boolean checkEdit(){
		if(register_username.getText().toString().trim().equals("")){
			Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}else if(register_passwd.getText().toString().trim().equals("")){
			Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}else if(!register_passwd.getText().toString().trim().equals(reregister_passwd.getText().toString().trim())){
			Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
		}else{
			return true;
		}
		return false;
	}
        

}