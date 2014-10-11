package com.hyx.android.Game1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.TextView;

import com.hyx.android.Game.util.DialogUtil;

public class BaseActivity extends Activity {

	public static Context mContext;
	protected ProgressDialog progress;

	protected int MSG_SHOWPROCESS = 1;
	protected int MSG_HIDEPROCESS = 2;
	protected int MSG_NETWORKERROR = 3;
	protected int MSG_NOCONTENT = 4;
	protected int MSG_BINDDATA = 5;
	protected int MSG_UPDATEUI = 6;
	protected int MSG_SHOWUPDATE = 7;
	protected int MSG_SHOWSCORE = 8;

	public Handler _handler = new Handler() {
		@Override
		public void handleMessage(Message paramMessage) {

			if (paramMessage.what == MSG_SHOWPROCESS) {
				showProgress();
			} else if (paramMessage.what == MSG_HIDEPROCESS) {
				hideProgress();
			} else if (paramMessage.what == MSG_NETWORKERROR) {
				handleNetworkSetting();
			} else if (paramMessage.what == MSG_NOCONTENT) {
			
			} else if (paramMessage.what == MSG_BINDDATA) {
				bindDataInThread();
			}		
			else if(paramMessage.what == MSG_UPDATEUI){
				updateUI();
			}
			else if(paramMessage.what == MSG_SHOWUPDATE){
				showUpdate();
			}
		}
	};

	public void bindDataInThread() {
		
	}
	
	protected void updateUI(){
		
	}
	
    protected void showUpdate(){
		
	}
	
	public void showProgress() {
		this.progress.show();
	}

	public void hideProgress() {
		progress.hide();
		this.progress.dismiss();
	}

	public void handleNetworkSetting() {
		//this.progress.dismiss();
		DialogUtil.netSettingDialog(BaseActivity.this);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		//setTheme(android.R.style.Theme_Translucent);
		//requestWindowFeature(1);

		super.onCreate(savedInstanceState);

		mContext = this;
		TextView view = (TextView) findViewById(android.R.id.title);
		view.setGravity(Gravity.CENTER);
		this.progress = new ProgressDialog(this);


	}

	protected void initUI() {


	}

}
