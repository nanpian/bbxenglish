package com.hyx.android.Game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetUtils {

	public static String getUrl(String url) {

		String result = null;
		try {
			HttpClient client = new DefaultHttpClient();

			HttpGet request = new HttpGet(url);

			System.out.println("url=" + url);

			HttpResponse response = client.execute(request);

			HttpEntity entity = response.getEntity();
			entity = new BufferedHttpEntity(entity);

			result = EntityUtils.toString(entity, "UTF-8");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();

	}

	public static String getDefaultUrl(String url) {

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		StringBuilder result = new StringBuilder();

		try {
			client.getParams().setIntParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();

	}
	
	
    public static boolean getNetworkStatus(Context context)
    {
        ConnectivityManager connectivitymanager = (ConnectivityManager)context.getSystemService("connectivity");
        boolean flag;
        if(connectivitymanager.getActiveNetworkInfo() != null)
            flag = connectivitymanager.getActiveNetworkInfo().isAvailable();
        else
            flag = false;

        return flag;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://221.131.71.118:14330/gpsgateway/services/stations";
		String result = ""; // getUrl(url);
		System.out.println(result);
	}

}
