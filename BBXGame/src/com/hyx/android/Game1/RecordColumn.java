package com.hyx.android.Game1;

import android.provider.BaseColumns;

//定义数据
public class RecordColumn implements BaseColumns
{
	public RecordColumn()
	{
	}
	//列名
	public static final String NAME = "classname";				//姓名
	public static final String TIME = "time";//移动电话
	public static final String USER = "user";	//家庭电话
			
	//列 索引值
	public static final int _ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int TIME_COLUMN = 2;
	public static final int USER_COLUMN = 3;

	//查询结果
	public static final String[] PROJECTION ={
		_ID,
		NAME,
		TIME,
		USER
	};
}

