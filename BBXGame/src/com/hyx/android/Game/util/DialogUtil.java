package com.hyx.android.Game.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.hyx.android.Game1.R;

public class DialogUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static boolean netSettingDialogShow=false;
	
	  public static void baseDialog(Context paramContext, int paramIntMsg)
	  {
	    if (!((Activity)paramContext).isFinishing())
	    	  new AlertDialog.Builder(paramContext).setIcon(R.drawable.icon).setTitle("信息提示").setMessage(paramIntMsg).setPositiveButton("确定", null).show();
	  }
	
	public static void baseDialog(Context paramContext, int paramIntTitle, int paramIntMsg, int paramIntIcon, int paramIntBtn1, int paramIntBtn2, DialogInterface.OnClickListener paramOnClickListener1, DialogInterface.OnClickListener paramOnClickListener2)
	  {
	    if (!((Activity)paramContext).isFinishing())
	    {
	      AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
	      if (paramIntTitle != -1)
	        localBuilder.setTitle(paramIntTitle);
	      if (paramIntMsg != -1)
	        localBuilder.setMessage(paramIntMsg);
	      if (paramIntIcon != -1)
	        localBuilder.setIcon(paramIntIcon);
	      if (paramIntBtn1 != -1)
	        localBuilder.setPositiveButton(paramIntBtn1, paramOnClickListener1);
	      if (paramIntBtn2 != -1)
	        localBuilder.setNegativeButton(paramIntBtn2, paramOnClickListener2);
	      localBuilder.create().show();
	    }
	  }
	  public static void netSettingDialog(final Context paramContext)
	  {
		  if(netSettingDialogShow) return;
		  if(!((Activity)paramContext).isFinishing()){
			  
			  AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		 
		        localBuilder.setTitle(R.string.app_name);
		 
		        localBuilder.setMessage(R.string.networdk_not_available);
		 
		        localBuilder.setIcon(R.drawable.icon);
	
		        localBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {  

	    	        	   Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");	    	         
	    	               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	    	               paramContext.startActivity(intent);  	 
	    	               netSettingDialogShow=false;
	    	           }  
	    	       });
		   
		        localBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {  
	    	                dialog.cancel();  
	    	                netSettingDialogShow=false;
	    	          }  
	    	       });
		      localBuilder.create().show();
		      netSettingDialogShow=true;	      

			  
		  }		  

	  }
	  
	  public static void errorDialog(Context paramContext, int paramInt, DialogInterface.OnClickListener paramOnClickListener)
	  {
	    if (!((Activity)paramContext).isFinishing())
	      new AlertDialog.Builder(paramContext).setIcon(R.drawable.icon).setTitle("信息提示").setMessage(paramInt).setPositiveButton("确定", paramOnClickListener).show();
	  }


}
