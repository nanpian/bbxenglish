package com.hyx.android.Game1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyViewGroup extends ViewGroup {
    private final static String TAG = "MyViewGroup";
    
    private final static int VIEW_MARGIN=2;
    //private LayoutInflater mInflater = null;
    //private TextView mText = null;
    //private View mView;

    public MyViewGroup(Context context) {
        //super(context);
        this(context, null, 0);
    }
    
    public MyViewGroup(Context context, AttributeSet attr)
    {
        //super(context, attr);     
        this(context, attr, 0);
    }
    
    public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);			
		//mInflater = LayoutInflater.from(context);	
		//mView = mInflater.inflate(R.layout.multioption, null);	
		//addView(mView);
		
		//mText = new TextView(context);
		//mText.setText("≤‚ ‘LayoutInflater");
		//addView(mText);
	}
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "widthMeasureSpec = "+widthMeasureSpec+" heightMeasureSpec"+heightMeasureSpec);
        
   
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            child.setVisibility(View.VISIBLE);
            // measure
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);   
          
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        Log.d(TAG, "changed = "+arg0+" left = "+arg1+" top = "+arg2+" right = "+arg3+" botom = "+arg4);
        final int count = getChildCount();
        int row=0;// which row lay you view relative to parent
        int lengthX=arg1;    // right position of child relative to parent
        int lengthY=arg2;    // bottom position of child relative to parent
        for(int i=0;i<count;i++){
            
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX+=width+VIEW_MARGIN;
            lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+arg2;
            //if it can't drawing on a same line , skip to next line
            if(lengthX>arg3){
                lengthX=width+VIEW_MARGIN+arg1;
                row++;
                lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+arg2;
                
            }
            
            child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
        }

    }

}