package com.hyx.android.Game1;

import java.io.File;
import java.io.IOException;

import com.hyx.android.Game.util.AudioRecorder2Mp3Util;
import com.pocketdigi.utils.FLameUtils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;

public class AudioRecorder {
	
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".raw"; //".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
//	private MediaRecorder recorder = null;
//	private int output_formats = MediaRecorder.OutputFormat.RAW_AMR;  //MediaRecorder.OutputFormat.MPEG_4;
	private String rawPath="",mp3Path="";
	//获取类的实例
	//ExtAudioRecorder extAudioRecorder = null;
	AudioRecorder2Mp3Util util = null;
		
	public AudioRecorder(Context context)
	{

	}
	
	public String getFilename(int index){
		System.out.println("index="+index);
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);
		
		if(!file.exists()){
			file.mkdirs();
		}
		
		return (file.getAbsolutePath() + "/" + "record" + String.valueOf(index) + AUDIO_RECORDER_FILE_EXT_MP4);
	}
	
	public void clearFile(int maxIndex){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);
		
		if(!file.exists()){
			file.mkdirs();
		}
		
		for(int i=1;i<=maxIndex;i++){
			String path =  file.getAbsolutePath() + "/";
			File file2 = new File(path,"record" + String.valueOf(i) + AUDIO_RECORDER_FILE_EXT_MP4);
			System.out.println("file2.exists()="+file2.exists());
			if(file2.exists())
				file2.delete();
		}
	}
	
	
	public void startRecording(int index){
		//if(true) return;

		
		
//		recorder = new MediaRecorder();
		
//		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//		recorder.setOutputFormat(output_formats);
//		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		rawPath = getFilename(index);
		mp3Path = rawPath.replace(".raw", ".mp3");
		
//		recorder.setOutputFile(getFilename(index));		
//		recorder.setOnErrorListener(errorListener);
//		recorder.setOnInfoListener(infoListener);
		
		if (util == null) {
			util = new AudioRecorder2Mp3Util(null,rawPath,mp3Path);
		}
		
		try {
//			recorder.prepare();
//			recorder.start();
			
			util.startRecording();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void stopRecording(){
		//if(true) return;

		
//		if(null != recorder){
//			recorder.stop();
//			recorder.reset();
//			recorder.release();
//			
//			recorder = null;			
//		}
		if(util==null) return;
		
		util.stopRecordingAndConvertFile();
		util.cleanFile(AudioRecorder2Mp3Util.RAW);
		// 如果要关闭可以
		util.close();
		util = null;
		
		
	}
	
//	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
//		@Override
//		public void onError(MediaRecorder mr, int what, int extra) {
//			AppLog.logString("Error: " + what + ", " + extra);
//		}
//	};
//	
//	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
//		@Override
//		public void onInfo(MediaRecorder mr, int what, int extra) {
//			AppLog.logString("Warning: " + what + ", " + extra);
//		}
//	};

	public void free()
	{

	}	
	
	
	




}
