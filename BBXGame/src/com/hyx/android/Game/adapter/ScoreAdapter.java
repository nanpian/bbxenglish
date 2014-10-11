package com.hyx.android.Game.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyx.android.Game.util.ScoreItem;
import com.hyx.android.Game1.R;

public class ScoreAdapter extends BaseAdapter
{
	LayoutInflater inflater;
    List<ScoreItem> list;
//    String keywords;


    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int i)
    {
        return list.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = inflater.inflate(R.layout.scoreitem, null);
        //view1.setBackgroundColor(item_background[i % 2]);
        
        ScoreItem model = (ScoreItem)list.get(i);
        
        //LinearLayout llScoreItem =  (LinearLayout)view1.findViewById(R.id.llScoreItem);
        ((TextView)view1.findViewById(R.id.tvNum)).setText(String.valueOf(model.getNum()));
    	((TextView)view1.findViewById(R.id.tvUsername)).setText(model.getUsername());
        ((TextView)view1.findViewById(R.id.tvScore)).setText(String.valueOf(model.getScore()));
        
         view1.setTag(model);
         return view1;
    }

    public ScoreAdapter(Context context,List<ScoreItem> list)
    {
    	 super();
         this.list = list;
//         this.keywords = keywords;
         inflater = LayoutInflater.from(context);
    }
}
