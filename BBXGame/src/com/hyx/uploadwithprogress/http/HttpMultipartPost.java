package com.hyx.uploadwithprogress.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hyx.uploadwithprogress.http.CustomMultipartEntity.ProgressListener;

public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;
	//private String filePath;
	private List<String> filePaths;
	private String username;
	private String classname;
	private String subclassname;
	private int classid;
	private int subclassid;
	private ProgressDialog pd;
	private long totalSize;

	public HttpMultipartPost(Context context, List<String> filePaths,String username,String classname,String subclassname,int classid,int subclassid) {
		this.context = context;
		this.filePaths = filePaths;
		this.username = username;
		this.classname = classname;
		this.subclassname = subclassname;
		this.classid = classid;
		this.subclassid = subclassid;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在上传录音文件...");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		try {
			classname = URLEncoder.encode(classname,"GBK");
			subclassname = URLEncoder.encode(subclassname,"GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		HttpPost httpPost = new HttpPost("http://hxu0010785.chinaw3.com/upload/up.php?username="+ username +"&classname="+classname+"&subclassname="+subclassname+"&classid="+classid+"&subclassid="+subclassid);

		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					});

			// We use FileBody to transfer an image
			for(String filePath:filePaths){
				File file = new File(filePath);
				if(file.exists())
					multipartContent.addPart("user_upload_file[]", new FileBody(file));
			}
		
			totalSize = multipartContent.getContentLength();

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("serverResponse: " + serverResponse);
		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("result: " + result);
		pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		System.out.println("cancle");
	}

}
