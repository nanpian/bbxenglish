package com.hyx.android.Game1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hyx.android.Game.util.DeviceUuidFactory;
import com.hyx.android.Game.util.RankManager;
import com.hyx.android.Game.util.Utils;
import com.hyx.android.Game1.R;

public class Login extends BaseActivity {
	
	private static String ajaxHost = "http://www.tgbbx.com/api/api1.php";  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        
        final CheckBox login_cb_savepwd = (CheckBox)findViewById(R.id.login_cb_savepwd);        
        final TextView login_edit_account = (TextView)findViewById(R.id.login_edit_account);    
        final TextView login_edit_pwd = (TextView)findViewById(R.id.login_edit_pwd);    
        	
        SharedPreferences sharedata = getSharedPreferences("login", 0);   
		String savepwd = sharedata.getString("savepwd", null);   
		final String account = sharedata.getString("account", null); 		
		final String pwd = sharedata.getString("pwd", null); 	
		
		final String right_account = sharedata.getString("right_account", null); 		
		final String right_pwd = sharedata.getString("right_pwd", null); 	
		final String right_pass = sharedata.getString("right_pass", null); 	
		
		if(account!=null){
			login_edit_account.setText(account);
			RankManager.getInstance().setUser_name(account);
		}
		
		/*
    	if(savepwd!=null){
    		login_cb_savepwd.setChecked("true".equals(savepwd));
    		if("true".equals(savepwd) && pwd!=null){
    			login_edit_pwd.setText(pwd);
    		}
    	}   	
    	*/
    	
    	String thePwd = getLocalMacAddress();
    	System.out.println("thePwd="+thePwd);
    	login_edit_pwd.setText(thePwd);

    	
    	Button btnReg  = (Button)findViewById(R.id.login_btn_reg);
    	btnReg.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub					
			  String account =login_edit_account.getText().toString().trim();
			  String password = login_edit_pwd.getText().toString().trim();
			  if(account.length()==0){
				  new AlertDialog.Builder(Login.this)  
    			  .setTitle("提示")        		
    			  .setIcon(android.R.drawable.ic_dialog_info)        		
    			  .setMessage("用户名不能为空")
    			  .setPositiveButton("确定", new DialogInterface.OnClickListener(){
    				  
    				  public void onClick(DialogInterface dialog, int which) {                     
    					  /**关闭窗口**/
    					
    					 }}).show();
				  
				  return;						  
			  }
				
				
				
				
       		  SharedPreferences.Editor sharedata2 = getSharedPreferences("login", 0).edit();   
       		       		  
       		  HttpClient client = new DefaultHttpClient();
       		  String checkcode= Utils.MD5(account + login_edit_pwd.getText().toString() + "defabc");
       		  System.out.println("BBX-reg:"+ajaxHost+"?action=reg&username="+account+"&pwd="+login_edit_pwd.getText().toString()+"&checkcode="+checkcode);
       		  HttpGet request = new HttpGet(ajaxHost+"?action=reg&username="+account+"&pwd="+login_edit_pwd.getText().toString()+"&checkcode="+checkcode);
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
				      String res = result.toString().replace("var data=", "");					    
					  JSONObject obj = new JSONObject(res);						
				      if("true".equals(obj.getString("success"))){	    	  
				    	  sharedata2.putString("right_account", account); 
				    	  sharedata2.putString("right_pwd", login_edit_pwd.getText().toString());   
				    	  sharedata2.putString("right_pass", "false");   
				    	  sharedata2.commit();  
				    	
				      }else{
				    	 
	        		   }
       		
				      String info = obj.getString("info").toString();
			    	  new AlertDialog.Builder(Login.this)         
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
   
    	
	

        	
        	Button btnLogin = (Button)findViewById(R.id.login_btn_login);
        	btnLogin.setOnClickListener(new Button.OnClickListener()
             {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub		
					
					  String account =login_edit_account.getText().toString();
					
					
					
	        		  SharedPreferences.Editor sharedata2 = getSharedPreferences("login", 0).edit();   
	        		  sharedata2.putString("savepwd", login_cb_savepwd.isChecked() ? "true" : "false");   
	        		  sharedata2.putString("account", account);   
	        		
	        		  
	        		  if(login_cb_savepwd.isChecked())
	        			  sharedata2.putString("pwd", login_edit_pwd.getText().toString());   
	        		  sharedata2.commit(); 
	        		  
	        		  RankManager.getInstance().setUser_name(account);
	        		  
	        		  if(right_account!=null && right_account.equals(account) && right_pwd!=null && right_pwd.equals(login_edit_pwd.getText().toString()) && "true".equals(right_pass)){
	        			
	        			  Intent intent = new Intent();
	                      intent.setClass(Login.this, MyGame.class);
	                      startActivity(intent);
	        		  }else
	        			  
	        		  {
	        		  
	        		  HttpClient client = new DefaultHttpClient();
	        		  String checkcode= Utils.MD5(account+ login_edit_pwd.getText().toString() + "defabc");
	        		  HttpGet request = new HttpGet(ajaxHost+"?action=login&username="+ account +"&pwd="+login_edit_pwd.getText().toString()+"&checkcode="+checkcode);
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
					      String res = result.toString().replace("var data=", "");					    
						  JSONObject obj = new JSONObject(res);						
					      if("true".equals(obj.getString("success")))
						  //if(true)
					      {
					    	  
					    	
					    	  //sharedata2.putString("right_account", login_edit_account.getText().toString()); 
					    	  //sharedata2.putString("right_pwd", login_edit_pwd.getText().toString());  
					          SharedPreferences sharedata3 = getSharedPreferences("login", 0); 
					  		  String right_account = sharedata3.getString("right_account", null); 	
					    	  
					    	  
					    	  //if(login_edit_account.getText().toString().equals(right_account))
					    	  {
						    	  sharedata2.putString("right_pass", "true"); 
						    	  sharedata2.commit();  
						    	  
						    	  Intent intent = new Intent();
			                      intent.setClass(Login.this, MyGame.class);
			                      startActivity(intent);
					    	  }
					    	 /*
					    	  else{
					    		  String info = "请先注册！";
						    	  new AlertDialog.Builder(Login.this)         
			        			  //设置标题        
			        			  .setTitle("提示")
			        			  //**设置icon
			        			  .setIcon(android.R.drawable.ic_dialog_info)
			        			  //**设置内容
			        			  .setMessage(info)
			        			  
			        			  .setPositiveButton("确定", new DialogInterface.OnClickListener(){
			        				  
			        				  public void onClick(DialogInterface dialog, int which) {                     
			        					  //关闭窗口
			        					 // finish();
			        					  //login_edit_pwd.setText("");
			        					 }}).show();	
			        			  
			        			
					    	  }
					      	*/					    	  
					    	 
					      }else{
					    	  String info = obj.getString("info").toString();
					    	  new AlertDialog.Builder(Login.this)         
		        			  /**设置标题**/         
		        			  .setTitle("提示")
		        			  /**设置icon**/
		        			  .setIcon(android.R.drawable.ic_dialog_info)
		        			  /**设置内容**/
		        			  .setMessage(info)
		        			  /*
		        			  .setNegativeButton("取消", new DialogInterface.OnClickListener(){
		        				  
		        			    public void onClick(DialogInterface dialog, int which) {
		        				  // TODO Auto-generated method stub
		        			    	
		        				  
		        			  }})*/
		        			  .setPositiveButton("确定", new DialogInterface.OnClickListener(){
		        				  
		        				  public void onClick(DialogInterface dialog, int which) {                     
		        					  /**关闭窗口**/
		        					 // finish();
		        					  //login_edit_pwd.setText("");
		        					 }}).show();
		        			  }
	        		
					      
					      
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
					/*
	        		  if("hyx".equals(login_edit_account.getText().toString()) && "123456".equals(login_edit_pwd.getText().toString())){
	        			  Intent intent = new Intent();
	                      intent.setClass(Login.this, MyGame.class);
	                      startActivity(intent);
	        		  }else{
	        			  
	        		  } 
	        			  
					*/
	        	  }	       	
					
    				

             });
        
        	/*
       	 if(right_account!=null && right_account.equals(login_edit_account.getText().toString()) && right_pwd!=null && right_pwd.equals(login_edit_pwd.getText().toString()) && "true".equals(right_pass)){
			  Intent intent = new Intent();
            intent.setClass(Login.this, MyGame.class);
            startActivity(intent);          
		  }*/
        	
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//按下键盘上返回按钮 
        if(keyCode == KeyEvent.KEYCODE_BACK){
            SharedPreferences sharedata = getSharedPreferences("login", 0); 
    		final String right_pass = sharedata.getString("right_pass", null); 	
        	if(right_pass!=null && "true".equals(right_pass)){
        		this.finish();
        	}
        	return true;
        }
        
        return super.onKeyDown(keyCode, event);      

    }
    
    
    
    public String getLocalMacAddress() { 
    	/*
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        String mac = info.getMacAddress();
        if(mac==null || mac.length()==0){
        	DeviceUuidFactory fac = new DeviceUuidFactory(this.getBaseContext());        	
        	mac  = fac.getDeviceUuid().toString();
        	System.out.print("fac="+fac);
        }*/
    	DeviceUuidFactory fac = new DeviceUuidFactory(this.getBaseContext());        	
    	String mac  = fac.getDeviceUuid().toString();
        return mac;
    }  
    

}