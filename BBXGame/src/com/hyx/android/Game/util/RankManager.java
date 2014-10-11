package com.hyx.android.Game.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RankManager {

	static RankManager instance;
	public static RankManager getInstance(){
		if(instance==null)
			instance = new RankManager();
		return instance;
	}
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	private String user_name;
	
	public int getSort_id() {
		return sort_id;
	}

	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}

	private int sort_id;
	
	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	private int seconds;

    public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	private boolean valid = true;
   
	
	public String getRankUrl(String user_name,int sort_id,int score_number,String checkcode){
		return IConst.BaseAPI+"upload.php?user_name="+ user_name +"&sort_id="+ sort_id +"&score_number="+ score_number +"&checkcode=" + checkcode;
	}
	
	public Rank getRank(String url){	
		 Rank rank = new Rank();		
	     String result = NetUtils.getDefaultUrl(url);
	     System.out.println("result="+result);
         try {        	 
        	 if (result != null) {
        		 if(result.startsWith("\ufeff"))
        			 result = result.substring(1);        		
				JSONObject json = new JSONObject(result);	
				
				if(json.has("rank"))
					rank.setRank(json.getLong("rank"));
				
				if(json.has("error"))
					rank.setError(json.getString("error"));
				
				
				if(json.has("scores")){
					JSONArray ary = json.getJSONArray("scores");
					rank.getScoreItems().clear();
					for(int i=0;i<ary.length();i++){
						JSONObject obj = (JSONObject)ary.get(i);
						ScoreItem item = new ScoreItem();
						item.setNum(obj.getInt("num"));
						item.setUsername(obj.getString("user_name"));
						item.setScore(obj.getInt("score"));
						rank.getScoreItems().add(item);
					}
				}
        	 }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return rank;
	}
	
}
