package com.hyx.android.Game1;

import android.provider.BaseColumns;

//��������
public class RecordColumn implements BaseColumns
{
	public RecordColumn()
	{
	}
	//����
	public static final String NAME = "classname";				//����
	public static final String TIME = "time";//�ƶ��绰
	public static final String USER = "user";	//��ͥ�绰
			
	//�� ����ֵ
	public static final int _ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int TIME_COLUMN = 2;
	public static final int USER_COLUMN = 3;

	//��ѯ���
	public static final String[] PROJECTION ={
		_ID,
		NAME,
		TIME,
		USER
	};
}

