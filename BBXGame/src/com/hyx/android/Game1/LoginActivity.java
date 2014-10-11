package com.hyx.android.Game1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hyx.android.Game.util.DeviceUuidFactory;
import com.hyx.android.Game.util.RankManager;
import com.hyx.android.Game.util.Utils;
import com.hyx.android.Game1.R;

public class LoginActivity extends BaseActivity {

	private static String ajaxHost = "http://www.tgbbx.com/api/api6_2.php";
	private String right_account;
	private String right_pwd;
	private String right_pass;
	private static final String Tag = "RegisterActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		final CheckBox login_cb_savepwd = (CheckBox) findViewById(R.id.login_cb_savepwd);
		final TextView login_edit_account = (TextView) findViewById(R.id.login_edit_account);
		final TextView login_edit_pwd = (TextView) findViewById(R.id.login_edit_pwd);

		SharedPreferences sharedata = getSharedPreferences("login", 0);
		String savepwd = sharedata.getString("savepwd", null);
		String account = sharedata.getString("account", null);
		String pwd = sharedata.getString("pwd", null);

		right_account = sharedata.getString("register", null);
		right_pwd = sharedata.getString("register_username", null);
		right_pass = sharedata.getString("register_passwd", null);

		if (account != null) {
			login_edit_account.setText(account);
			login_cb_savepwd.setChecked("true".equals(savepwd));
			login_edit_pwd.setText(pwd);
			RankManager.getInstance().setUser_name(account);
		}

		Button btnReg = (Button) findViewById(R.id.login_btn_reg_activity);
		btnReg.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentreg = new Intent();
				intentreg.setClass(LoginActivity.this, SmsActivity.class);
				startActivity(intentreg);
			}
		});

		Button btnLogin = (Button) findViewById(R.id.login_btn_login);
		btnLogin.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String account = login_edit_account.getText().toString();
				SharedPreferences.Editor sharedata2 = getSharedPreferences(
						"login", 0).edit();
				sharedata2.putString("savepwd",
						login_cb_savepwd.isChecked() ? "true" : "false");
				sharedata2.putString("account", account);
				if (login_cb_savepwd.isChecked())
					sharedata2.putString("pwd", login_edit_pwd.getText()
							.toString());
				sharedata2.commit();

				RankManager.getInstance().setUser_name(account);

				/*
				 * if(right_account!=null && right_account.equals(account) &&
				 * right_pwd!=null &&
				 * right_pwd.equals(login_edit_pwd.getText().toString()) &&
				 * "true".equals(right_pass)){ SharedPreferences.Editor
				 * sharedata3 = getSharedPreferences("register", 0).edit();
				 * sharedata3.putString("register_username", account);
				 * sharedata3.putString("register_userpwd",
				 * login_edit_pwd.getText().toString()); sharedata3.commit();
				 * Intent intent = new Intent();
				 * intent.setClass(LoginActivity.this, MyGameActivity.class);
				 * startActivity(intent); }else
				 * 
				 * {
				 */

				HttpClient client = new DefaultHttpClient();
				String checkcode = Utils.MD5(account
						+ login_edit_pwd.getText().toString() + "defabc");
				SharedPreferences sharedata = getSharedPreferences(
						"sms_verify", 0);
				String mobilenum = sharedata.getString("mobienum", null);
				SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMddHHmmss");
				String logindate=dateformat1.format(new Date());
				SharedPreferences.Editor sharedatat = getSharedPreferences(
						"login_time", 0).edit();
				sharedatat.putString("logindate", logindate);
				sharedatat.commit();


				HttpGet request = new HttpGet(ajaxHost
						+ "?action=login&username=" + account + "&pwd="
						+ login_edit_pwd.getText().toString() + "&tel="
						+ mobilenum + "&logindate=" + logindate + "&checkcode="
						+ checkcode);
				Log.d(Tag, ajaxHost + "?action=login&username=" + account
						+ "&pwd=" + login_edit_pwd.getText().toString()
						+ "&tel=" + mobilenum + "&logindate" + logindate
						+ "&checkcode=" + checkcode);
				try {
					HttpResponse response = client.execute(request);
					/* response code */
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					String line = "";
					StringBuilder result = new StringBuilder();
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Log.d(Tag, "The result server return is " + result);
					String res = result.toString().replace("var data=", "");
					JSONObject obj = new JSONObject(res);
					if ("true".equals(obj.getString("success"))) {
						SharedPreferences.Editor sharedata3 = getSharedPreferences(
								"register", 0).edit();
						sharedata3.putString("register_username",
								login_edit_account.getText().toString());
						sharedata3.putString("register_userpwd", login_edit_pwd
								.getText().toString());
						sharedata3.commit();
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this,
								MyGameActivity.class);
						startActivity(intent);
					} else {
						String info = obj.getString("info").toString();
						new AlertDialog.Builder(LoginActivity.this)
								// 设置标题
								.setTitle("提示")
								// **设置icon
								.setIcon(android.R.drawable.ic_dialog_info)
								// **设置内容
								.setMessage(info)

								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).show();

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
				 * } /* if("hyx".equals(login_edit_account.getText().toString())
				 * && "123456".equals(login_edit_pwd.getText().toString())){
				 * Intent intent = new Intent(); intent.setClass(Login.this,
				 * MyGame.class); startActivity(intent); }else{
				 * 
				 * }
				 */
			}

		});

		/*
		 * if(right_account!=null &&
		 * right_account.equals(login_edit_account.getText().toString()) &&
		 * right_pwd!=null &&
		 * right_pwd.equals(login_edit_pwd.getText().toString()) &&
		 * "true".equals(right_pass)){ Intent intent = new Intent();
		 * intent.setClass(Login.this, MyGame.class); startActivity(intent); }
		 */

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	public String getLocalMacAddress() {
		/*
		 * WifiManager wifi = (WifiManager)
		 * getSystemService(Context.WIFI_SERVICE); WifiInfo info =
		 * wifi.getConnectionInfo(); String mac = info.getMacAddress();
		 * if(mac==null || mac.length()==0){ DeviceUuidFactory fac = new
		 * DeviceUuidFactory(this.getBaseContext()); mac =
		 * fac.getDeviceUuid().toString(); System.out.print("fac="+fac); }
		 */
		DeviceUuidFactory fac = new DeviceUuidFactory(this.getBaseContext());
		String mac = fac.getDeviceUuid().toString();
		return mac;
	}

}