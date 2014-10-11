package com.hyx.android.Game.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.hyx.android.Game1.MyViewGroup;
import com.hyx.android.Game1.R;
import com.hyx.android.Game1.SubSubjectClass;
import com.hyx.android.Game1.Subject;
import com.hyx.android.Game1.SubjectClass;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Utils {

	public static  ArrayList<SubjectClass>  getAllClass(XmlPullParser parser){
        ArrayList<SubjectClass> subjects = new ArrayList<SubjectClass>(); 
        
        SubjectClass subject = null;
        int eventType;
		try {
			eventType = parser.getEventType();
			while(XmlPullParser.END_DOCUMENT!=eventType){  
	            String nodeName = parser.getName();  
	              
	            switch (eventType) {  
	            case XmlPullParser.START_TAG:  
	                if(nodeName.equals("class")){  
	                	subject = new  SubjectClass();  
	                	subject.setName(parser.getAttributeValue(0));
	                	subject.setId(Integer.parseInt(parser.getAttributeValue(1)));
	                	subject.subclass = new ArrayList<SubSubjectClass>(); 
	                	//for(int i=0; i<parser.getAttributeCount();i++){
	                	//	System.out.println("getAttributeName=" + parser.getAttributeName(i));
	                	//	System.out.println("getAttributeValue=" + parser.getAttributeValue(i));
	                	//}
	                	//subject.setName(parser.getProperty("name").toString());
	                }  	                
	                if(nodeName.equals("subclass")){ 
	                	String name = parser.getAttributeValue(0);
	                	int id = Integer.parseInt(parser.getAttributeValue(1));
						subject.subclass.add(new SubSubjectClass(id,name));			
	                }  
	                /*
	                if(nodeName.equals("id")){  
	                	subject.setId(Integer.parseInt(parser.nextText()));  
	                } 
	                */ 
	                break;  
	              
	            case XmlPullParser.END_TAG:  
	                if(nodeName.equals("class")&&subject!=null){  
	                	subjects.add(subject);  
	                }  
	                  
	                break;  
	            default:  
	                break;  
	            }  

	            eventType = parser.next();  
	            Log.i("PullActivity", eventType+"");  
	        }  
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return subjects;
        
	}
	
	public static HashMap<Integer,List<Subject>>  getSubjects(int firstClassId,XmlPullParser parser){
		HashMap<Integer,List<Subject>> allSubjects = new HashMap<Integer,List<Subject>>();
       
        Subject subject = null;
        int eventType;
		try {
			eventType = parser.getEventType();
			while(XmlPullParser.END_DOCUMENT!=eventType){  
	            String nodeName = parser.getName();  
	              
	            switch (eventType) {  
	            case XmlPullParser.START_TAG:  
	                if(nodeName.equals("subject")){  
	                	subject = new  Subject();  
	                	
	                	subject.setId(Integer.parseInt(parser.getAttributeValue(0)));
	                	subject.setQuestion(parser.getAttributeValue(1));
	                	subject.setIs_text(parser.getAttributeValue(2));
	                	subject.setPic(parser.getAttributeValue(3));
	                	subject.setMp3(parser.getAttributeValue(4));
	                	int sort_id = Integer.parseInt(parser.getAttributeValue(5));
	                	subject.setSort_id(sort_id);
	                	subject.setAnswer(parser.getAttributeValue(6));
	                	subject.setIs_select(Integer.parseInt(parser.getAttributeValue(7)));
	                	subject.setSelect_answer(parser.getAttributeValue(8));
	                	subject.setYes_answer(parser.getAttributeValue(9));
	                	
	                	List<Subject> subjects = null;
	                	if(allSubjects.containsKey(sort_id)){
	                		subjects = allSubjects.get(sort_id);
	                		subjects.add(subject);	                	
	                	}else{
	                		subjects = new ArrayList<Subject>();
	                		subjects.add(subject);
	                		allSubjects.put(sort_id, subjects);
	                	}
	                	
	                	if(subject.getMp3().length()>0){
	                		Log.d("dewei", "downlaodAudio class id :" +firstClassId +"class id"+subject.getClass().getName() + subject.getId());
	                	//	ClassManager.downloadAudio(firstClassId,subject.getMp3());
	                	}
	                	
	                	if(subject.getPic().length()>0){
	                		ClassManager.downloadPic(firstClassId,subject.getPic());
	                	}
	                	
	                }  	               
	                break;  
	              
	            case XmlPullParser.END_TAG:  
	                //if(nodeName.equals("subject")&&subject!=null){  
	                //	subjects.add(subject);  
	                //}  	                  
	                break;  
	            default:  
	                break;  
	            }  
	            // �ֶ��Ĵ�����һ���¼�  
	            eventType = parser.next();  
	            Log.i("PullActivity", eventType+"");  
	        }  
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        return allSubjects;
	}
	
	public static Bitmap createImage(int alpha,String mstrTitle){
		
		//if(mbmpTest==null){
			int w = mstrTitle.length()*12,h = 21;		
			Bitmap mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);	
			Canvas canvasTemp = new Canvas(mbmpTest);
			canvasTemp.drawColor(Color.TRANSPARENT);
			Paint p = new Paint();
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName,Typeface.BOLD);
			p.setColor(getColor());
			p.setTypeface(font);
			p.setTextSize(20);
			p.setAntiAlias(true);	
			p.setAlpha(alpha);
			canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
			canvasTemp.drawText(mstrTitle,0,h-3,p);
			return mbmpTest;		
		//}
		//else{
		//	changeAlpha(p);
		//	return mbmpTest;
		//}
	}
	
	public static Bitmap createTopic(String mstrTitle,int color){
		
		//if(mbmpTest==null){
			int w = mstrTitle.length()*18,h = 21;
			Bitmap mbmpTest = Bitmap.createBitmap(w,h, Config.ARGB_8888);		
			Canvas canvasTemp = new Canvas(mbmpTest);
			canvasTemp.drawColor(Color.TRANSPARENT);
			Paint p = new Paint();
			String familyName = "黑体";
			Typeface font = Typeface.create(familyName,Typeface.BOLD);
			p.setColor(color);
			p.setTypeface(font);
			p.setTextSize(18);
			p.setAntiAlias(true);		
			canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
			canvasTemp.drawText(mstrTitle,0,h-3,p);
			return mbmpTest;		
		//}
		//else{
		//	changeAlpha(p);
		//	return mbmpTest;
		//}
	}
	
	public static void changeAlpha(Paint p){
		int value  = p.getAlpha();
		if(value<100)value+=10;
		p.setAlpha(value);
	}
	
	public static int getColor(){
		int color = Color.rgb(0, 128, 0);
		//Random random = new Random(10);//指定种子�?00
		int value  = (int)(Math.random()*10);
		switch(value){
			case 1:			
				color =Color.rgb(0, 128, 255); 
				break;
			case 2:
				color= Color.rgb(0, 128, 192); 
				break;
			case 3:
				color =  Color.rgb(0, 64, 128);
				break;
			case 4:
				color = Color.rgb(255, 51, 102);
				break;
			case 5:
				color = Color.rgb(0, 240, 120);
				break;
			case 6:
				color = Color.rgb(0, 128, 64);
				break;
			case 7:
				color = Color.rgb(255, 102, 51);
				break;
			case 8:
				color = Color.rgb(111, 111, 255);
				break;
			case 9:
				color = Color.rgb(255, 255, 51);
				break;
		}
		return color;
	}
	
	//打乱数组
	public static List<String> changeArray(String[] ary){
		List<String> retVal = new ArrayList<String>();
		Random rand = new Random();
	
		boolean[]  bool = new boolean[ary.length];
		 int randInt = 0;
         for(int i = 0; i < ary.length ; i++) {
              do {
                  randInt  =rand.nextInt(ary.length);                  
              }while(bool[randInt]);
             bool[randInt] = true;  
             ary[randInt] = ary[randInt].trim();
             if(!"".equals(ary[randInt]) && !"|".equals(ary[randInt])){
            	 retVal.add(ary[randInt]);
             }          
         }  
		return retVal;
	}
	
	//打乱数组
		public static List<String> changeArray(List<String> ary){
			List<String> retVal = new ArrayList<String>();
			Random rand = new Random();
		
			boolean[]  bool = new boolean[ary.size()];
			 int randInt = 0;
	         for(int i = 0; i < ary.size() ; i++) {
	              do {
	                  randInt  =rand.nextInt(ary.size());                  
	              }while(bool[randInt]);
	             bool[randInt] = true;  
	             ary.set(randInt,ary.get(randInt).trim());
	             if(!"".equals(ary.get(randInt)) && !"|".equals(ary.get(randInt))){
	            	 retVal.add(ary.get(randInt));
	             }          
	         }  
			return retVal;
		}
	

		public static List<String> getArray(String[] ary){
			List<String> retVal = new ArrayList<String>();			
		
			
	         for(int i = 0; i < ary.length ; i++) {
	            
	             if(!"".equals(ary[i]) && !"|".equals(ary[i])){
	            	 retVal.add(ary[i]);
	             }          
	         }  
			return retVal;
		}
		
		
	
				public static List<String>  addToArray(List<String> arySource,List<String> aryObject,int count){
					List<String> retVal = new ArrayList<String>();			
				
					 int m=0;
			         for(int i = aryObject.size(); i < arySource.size() ; i++) {			            
			            
			        	 retVal.add(arySource.get(i));
			             m++;
			             if(m==count) break;
			         }  
			      
					return retVal;
				}
		
	
	/*
	public static View createOptionView(Context context,String option,boolean isMulti){		
		TextView tvOption = new TextView(context);			
		tvOption.setText(option);	
		if(isMulti){
			
		}
		return tvOption;
	}*/

	
	public static TextView CreateOptionView(boolean isMulti,Context context,String strOption,int fontSize){
		//System.out.println("fontSize="+fontSize);
		TextView tv = new TextView(context);
		if(isMulti){
			tv.setGravity(Gravity.LEFT);
			tv.setSingleLine(true);
		}
        tv.setTextColor(Color.BLUE);  
        tv.setText(strOption);  
        tv.setTextSize(fontSize);  
        tv.setShadowLayer(0, 3, 3, Color.YELLOW);
        tv.setPadding(9,0,9,250);
        return tv;
	}
	
	public static ImageView CreateImageView(Context context,AssetManager am,String picname){
		ImageView iv = new ImageView(context);      
		Bitmap bm = getImageFromAssetsFile(am,picname);
		iv.setTag(picname);
		iv.setImageBitmap(bm);			
		iv.setPadding(10, 10,10, 10);
        return iv;
	}
	
	/*
	public static TextView ConfigOptionView(boolean isMulti,TextView tv,String strOption){
		 tv.setGravity(Gravity.LEFT);  
         tv.setTextColor(Color.WHITE);  
         tv.setText(strOption);  
         tv.setTextSize(24);
         tv.setShadowLayer(45, 2, 2, Color.BLACK);
         tv.setPadding(10, 10,10, 10);
         return tv;
	}*/
	
	public static boolean checkFinish(boolean isMulti,ViewGroup layoutOption){
		boolean retVal=true;		
		if(isMulti){
			for(int i=0;i<layoutOption.getChildCount();i++){
				TextView ev = (TextView) layoutOption.getChildAt(i);
				if(ev.getVisibility()==View.VISIBLE){
					retVal=false;
					break;
				}
			}			
		}
		return retVal;
	}
	
	public static boolean checkFinish(boolean isMulti,LinearLayout layoutOption,LinearLayout layoutOption2,LinearLayout layoutOption3,LinearLayout layoutOption4){
		boolean retVal=true;		
		if(isMulti){
			for(int i=0;i<layoutOption.getChildCount();i++){
				TextView ev = (TextView) layoutOption.getChildAt(i);
				if(ev.getVisibility()==View.VISIBLE){
					retVal=false;
					break;
				}
			}	
			
			for(int i=0;i<layoutOption2.getChildCount();i++){
				TextView ev = (TextView) layoutOption2.getChildAt(i);
				if(ev.getVisibility()==View.VISIBLE){
					retVal=false;
					break;
				}
			}	
			
			for(int i=0;i<layoutOption3.getChildCount();i++){
				TextView ev = (TextView) layoutOption3.getChildAt(i);
				if(ev.getVisibility()==View.VISIBLE){
					retVal=false;
					break;
				}
			}	
			
			for(int i=0;i<layoutOption4.getChildCount();i++){
				TextView ev = (TextView) layoutOption4.getChildAt(i);
				if(ev.getVisibility()==View.VISIBLE){
					retVal=false;
					break;
				}
			}
		}
		return retVal;
	}
	
	public static boolean checkAnswer(boolean isMulti,LinearLayout layoutOption,LinearLayout layoutOption2,LinearLayout layoutOption3,LinearLayout layoutOption4,Subject model){
		boolean retVal=false;
		String answer="";
		if(isMulti){
			for(int i=0;i<layoutOption.getChildCount();i++){
				TextView ev = (TextView) layoutOption.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption2.getChildCount();i++){
				TextView ev = (TextView) layoutOption2.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption3.getChildCount();i++){
				TextView ev = (TextView) layoutOption3.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption4.getChildCount();i++){
				TextView ev = (TextView) layoutOption4.getChildAt(i);
				answer+=ev.getText().toString();
			}
			if(answer.equals(model.getAnswer().replace("|", "")))
				retVal=true;
		}
		return retVal;
	}
	
	public static boolean checkIsRight(boolean isMulti,LinearLayout layoutOption,LinearLayout layoutOption2,LinearLayout layoutOption3,LinearLayout layoutOption4,Subject model,String text){
		boolean retVal=false;
		String answer="";
		if(isMulti){
			for(int i=0;i<layoutOption.getChildCount();i++){
				TextView ev = (TextView) layoutOption.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption2.getChildCount();i++){
				TextView ev = (TextView) layoutOption2.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption3.getChildCount();i++){
				TextView ev = (TextView) layoutOption3.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption4.getChildCount();i++){
				TextView ev = (TextView) layoutOption4.getChildAt(i);
				answer+=ev.getText().toString();
			}
			if(model.getAnswer().replace("|", "").indexOf(answer+ text)==0)
				retVal=true;
		}
		return retVal;
	}
	
	
	public static boolean checkIsRight(boolean isMulti,LinearLayout layoutOption,LinearLayout layoutOption2,LinearLayout layoutOption3,LinearLayout layoutOption4,List<String> answers,String text){
		boolean retVal=false;
		String answer="";
		if(isMulti && answers.size()>1){
			for(int i=0;i<layoutOption.getChildCount();i++){
				TextView ev = (TextView) layoutOption.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption2.getChildCount();i++){
				TextView ev = (TextView) layoutOption2.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption3.getChildCount();i++){
				TextView ev = (TextView) layoutOption3.getChildAt(i);
				answer+=ev.getText().toString();
			}
			for(int i=0;i<layoutOption4.getChildCount();i++){
				TextView ev = (TextView) layoutOption4.getChildAt(i);
				answer+=ev.getText().toString();
			}
			if((answers.get(answers.size()-2).replace("|", "")+" ").indexOf(answer + text +" ")==0)
				retVal=true;
		}
		return retVal;
	}
	
	
	/*
	public static boolean checkAnswer(boolean isMulti,ViewGroup layoutOption,Subject model){
		boolean retVal=false;
		String answer="";
		if(isMulti){
			for(int i=0;i<layoutOption.getChildCount();i++){
				TextView ev = (TextView) layoutOption.getChildAt(i);
				answer+=ev.getText().toString();
			}
			if(answer.equals(model.getAnswer().replace("|", "")))
				retVal=true;
		}
		return retVal;
	}
	*/
      /*
	  * 从Assets中读取图片  
	  */  
	  public static Bitmap getImageFromAssetsFile(AssetManager am,String fileName)  
	  {  
	      Bitmap image = null;  	  
	      try  
	      {  
	    	  //if(!new File("images/"+fileName).exists())return image;
	          InputStream is = am.open("pic/"+fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  	  
	      return image;  	  
	  }  	  

	  //MD5加密，32位   
	  public static String MD5(String str){   
	  MessageDigest md5 = null;   
	  try{   
	  md5 = MessageDigest.getInstance("MD5");   
	  }catch(Exception e){   
	  e.printStackTrace();   
	  return "";   
	  }   
	  char[] charArray = str.toCharArray();   
	  byte[] byteArray = new byte[charArray.length];   
	  for(int i = 0; i < charArray.length; i++){   
	  byteArray[i] = (byte)charArray[i];   
	  }   
	  byte[] md5Bytes = md5.digest(byteArray);   
	  StringBuffer hexValue = new StringBuffer();   
	   for( int i = 0; i < md5Bytes.length; i++)   
	  {   
	  int val = ((int)md5Bytes[i])&0xff;   
	  if(val < 16)   
	   {   
	  hexValue.append("0");   
	  }   
	   hexValue.append(Integer.toHexString(val));   
	   }   
	   return hexValue.toString();   
	  }   
	  // 可逆的加密算法   
	  public static String encryptmd5(String str) {   
	  char[] a = str.toCharArray();   
	   for (int i = 0; i < a.length; i++)   
	  {   
	   a[i] = (char) (a[i] ^ 'l');   
	  }   
	   String s = new String(a);   
	  return s;   
	   }   
	  
	  
	  public static String GetNowDate(){
		    String temp_str="";
		    Date dt = new Date();
		    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    temp_str=sdf.format(dt);
		    return temp_str;
		}
	  
	  
	    public static boolean getNetworkStatus(Context context)
	    {
	        ConnectivityManager connectivitymanager = (ConnectivityManager)context.getSystemService("connectivity");
	        boolean flag;
	        if(connectivitymanager.getActiveNetworkInfo() != null)
	            flag = connectivitymanager.getActiveNetworkInfo().isAvailable();
	        else
	            flag = false;
	        
	        //System.out.println("networkstatus="+flag);
	        return flag;
	    }
	    
}
