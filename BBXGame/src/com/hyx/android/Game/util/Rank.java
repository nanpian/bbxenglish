package com.hyx.android.Game.util;

import java.util.ArrayList;
import java.util.List;

public class Rank {
	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	private long rank;
	private String error;
	 public List<ScoreItem> getScoreItems() {
		return scoreItems;
	}

	public void setScoreItems(List<ScoreItem> scoreItems) {
		this.scoreItems = scoreItems;
	}

	List<ScoreItem> scoreItems = new ArrayList<ScoreItem>();
	
	public Rank(){
		rank=0;
		error="";
	}
}
