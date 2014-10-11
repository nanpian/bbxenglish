package com.hyx.android.Game.util;

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

public class DownloadManager {
	
	
	public static String getResult(String url){
		String retVal="";
		HttpGet request = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);
			/* response code */
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			StringBuilder result = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			retVal = result.toString();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retVal;
	}

	public static boolean checkDownload(Activity activity, String username) {

		
		String checkcode = Utils.MD5(username + IConst.CheckKey);
		String url= IConst.UrlCheckDownload + "&username="
				+ username + "&pwd=" + "&checkcode=" + checkcode + "&t="
				+ Math.random();	
		
		String result = getResult(url);
		String res = result.replace("var data=", "");
		JSONObject obj;
		try {
			obj = new JSONObject(res);
			
			if ("true".equals(obj.getString("success"))) {
				return true;

			} else {
				String info = obj.getString("info").toString();
				new AlertDialog.Builder(activity)
				/** 设置标题 **/
				.setTitle("提示")
				/** 设置icon **/
				.setIcon(android.R.drawable.ic_dialog_info)
				/** 设置内容 **/
				.setMessage(info)

				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						/** 关闭窗口 **/
				
					}
				}).show();
				
				return false;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return false;

	}

}
