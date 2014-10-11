package com.hyx.android.Game.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class ClassManager {
	
	public static String getAudioPath(String filename){
		String   dirPath = Environment.getExternalStorageDirectory()+"/BBX/audio/";
		return dirPath+filename;
	}
	
	public static String getPicPath(String filename){
		String   dirPath = Environment.getExternalStorageDirectory()+"/BBX/pic/";
		return dirPath+filename;
	}
	
	public static Bitmap getImageFromFile(String fileName)  
	  {  
	      Bitmap image = null;  	  
	      try  
	      {  
	    	  File file = new File(getPicPath(fileName));
	    	  //if(!new File("images/"+fileName).exists())return image;
	    	  InputStream inputStream = new FileInputStream(file);
	          image = BitmapFactory.decodeStream(inputStream);  
	          inputStream.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  	  
	      return image;  	  
	  }  
	
	public static XmlPullParser downloadClass(int classId) {
		
		XmlPullParser xmlParser = null;
		try {
			String urlPath ="http://www.tgbbx.com/resources/allclass.xml";
			String dirName = Environment.getExternalStorageDirectory()+"/BBX/";
			if(classId>0){
				
				urlPath ="http://www.tgbbx.com/resources/res"+ classId +"/allclass.xml";
				dirName = Environment.getExternalStorageDirectory()+"/BBX/res"+classId+"/";
				File f = new File(dirName);
				if(!f.exists()){
					f.mkdir();
				}				
			}
			
			
			File f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
			String newFilename= dirName + "allclass.xml";
			File file = new File(newFilename);
//			if(file.exists())
//			{
//			    file.delete();
//			}
			
			downloadFile(urlPath,newFilename);
			
			xmlParser = Xml.newPullParser();
			
		
			
			//得到文件流，并设置编码方式
			InputStream inputStream = new FileInputStream(file);
			xmlParser.setInput(inputStream,"utf-8"); 
			
			
			
			return xmlParser;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
 
    public static void downzipFile(String urlPath, String newFilename) {
		try {
			
			// 构造URL
			URL url = new URL(urlPath);
			// 打开连接
			URLConnection con = url.openConnection();
			// 获得文件的长度
			// int contentLength = con.getContentLength();
			// System.out.println("长度 :"+contentLength);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(newFilename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadFile(String urlPath, String newFilename) {
		try {
			
			// 构造URL
			URL url = new URL(urlPath);
			// 打开连接
			URLConnection con = url.openConnection();
			// 获得文件的长度
			// int contentLength = con.getContentLength();
			// System.out.println("长度 :"+contentLength);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(newFilename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static XmlPullParser downloadSubjects(int secondClassId,int classid) {
		
		XmlPullParser xmlParser = null;
		try {
			String urlPath ="http://www.tgbbx.com/resources/res"+ secondClassId +"/subjects/"+classid+".xml";
			Log.d("dewei","downloadsubjects classid is " + urlPath);
			String dirName = Environment.getExternalStorageDirectory()+"/BBX/";
			File f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
		    dirName = Environment.getExternalStorageDirectory()+"/BBX/Subjects/";
			 f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
			String newFilename= dirName + classid + ".xml";
			File file = new File(newFilename);
			if(!file.exists()) {
				
				downloadFile(urlPath,newFilename);
			}
			xmlParser = Xml.newPullParser();
			//得到文件流，并设置编码方式
			InputStream inputStream = new FileInputStream(file);
			xmlParser.setInput(inputStream,"utf-8"); 			
			return xmlParser;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	

	public static void downloadAudio(int secondClassId,String filename) {

		try {
			String urlPath ="http://www.tgbbx.com/resources/res"+ secondClassId +"/audio/"+filename;
			String dirName = Environment.getExternalStorageDirectory()+"/BBX/";
			File f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
		    dirName = Environment.getExternalStorageDirectory()+"/BBX/audio/";
			 f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
			String newFilename= dirName + filename;
			//downloadFile(urlPath,newFilename);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
	}
	
	public static void downloadPic(int secondClassId,String filename) {

		try {
			String urlPath ="http://www.tgbbx.com/resources/res"+ secondClassId +"/pic/"+filename;
			String dirName = Environment.getExternalStorageDirectory()+"/BBX/";
			File f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
		    dirName = Environment.getExternalStorageDirectory()+"/BBX/pic/";
			 f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
			String newFilename= dirName + filename;
			//downloadFile(urlPath,newFilename);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
	}
	


}
