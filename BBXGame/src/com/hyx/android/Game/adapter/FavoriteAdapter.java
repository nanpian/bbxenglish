package com.hyx.android.Game.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.hyx.android.Game.util.FavoriteItem;
import com.hyx.android.Game1.DbFavoriteHelper;
import com.hyx.android.Game1.MyGameActivity;
import com.hyx.android.Game1.R;
import com.hyx.android.Game1.Subject;

public class FavoriteAdapter extends BaseAdapter
{
	LayoutInflater inflater;
    List<FavoriteItem> list;
    Context context;
    DbFavoriteHelper  dbFavoriteHelper;
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
        View view1 = inflater.inflate(R.layout.favoriteitem, null);
        //view1.setBackgroundColor(item_background[i % 2]);
        
        FavoriteItem model = (FavoriteItem)list.get(i);
        

        ((TextView)view1.findViewById(R.id.tvClassname)).setText(String.valueOf(model.getClassName()));
    	((TextView)view1.findViewById(R.id.tvQuestion)).setText(model.getQuestion());  
        ((Button)view1.findViewById(R.id.btnExe)).setTag(String.valueOf(model.getSubject_id()));
        ((Button)view1.findViewById(R.id.btnCancel)).setTag(String.valueOf(model.getSubject_id()));
        ((Button)view1.findViewById(R.id.btnPlay)).setTag(String.valueOf(model.getSubject_id()));
        
        Button btnExel =  (Button)view1.findViewById(R.id.btnExe);
        btnExel.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    int subject_id =  Integer.parseInt(v.getTag().toString());	
		    
		    //((MyGameActivity)context).doSubject(subject_id);
		    Subject subject  = ((MyGameActivity)context).getSubject(subject_id);
		    if(subject!=null)
		    {
//		    	 Toast.makeText(context , subject.getAnswer().replace("1", " "),
//		    				Toast.LENGTH_SHORT).show();
		    	
		    	String strAnswer="";
				String[] aryAnswer = subject.getAnswer().split("\\|");
				for(int i=0;i<aryAnswer.length;i+=2){
					if(strAnswer.length() >0) strAnswer+=" ";
					
					strAnswer+=aryAnswer[i];							
				}
		    	
		    	
		    	new AlertDialog.Builder(((MyGameActivity)context))
				.setTitle("显示答案")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(strAnswer)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {								
							}

						}).show();
		    	
		    }
		}
	});
		
        Button btnCancel =  (Button)view1.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    int subject_id =  Integer.parseInt(v.getTag().toString());
		    dbFavoriteHelper.delete(subject_id);
		    Toast.makeText(context , "取消收藏成功",
    				Toast.LENGTH_SHORT).show();
		    
		    ((MyGameActivity)context).loadFavorite();
		}
	});
		
		
        Button btnPlay =  (Button)view1.findViewById(R.id.btnPlay);
		   int subject_id =  Integer.parseInt(btnPlay.getTag().toString());
		   final Subject subject  = ((MyGameActivity)context).getSubject(subject_id);
		   if(subject.getMp3().length()==0)
		   {
			   btnPlay.setVisibility(View.GONE);
		   }
		   
		   btnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			    if(subject!=null)
			    {
			    	 ((MyGameActivity)context).playMp3(subject);
			    }
			}
		});
		
//	    dbFavoriteHelper.delete(subject_id);
//	    Toast.makeText(getApplicationContext(), "取消收藏成功",
//				Toast.LENGTH_SHORT).show();
        
		
	
		
         view1.setTag(model);
         return view1;
    }

    public FavoriteAdapter(Context context,List<FavoriteItem> list)
    {
    	 super();
    	 dbFavoriteHelper = new DbFavoriteHelper(context);
         this.list = list;
         inflater = LayoutInflater.from(context);
         this.context = context;
    }
}
