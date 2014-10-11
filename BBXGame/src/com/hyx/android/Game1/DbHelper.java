package com.hyx.android.Game1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME="bbx_db"; 
	private final static int DATABASE_VERSION=5;
	private final static String TABLE_NAME="bbx_record"; 
	public final static String FIELD_ID="_id";
	public final static String FIELD_CLASSNAME="record_ClassName";
	public final static String FIELD_TIME="record_Time";
	public final static String FIELD_USER="record_USER";
	public DbHelper(Context context) { 
		super(context, DATABASE_NAME,null, DATABASE_VERSION); 
	} 
	
	@Override 
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub 
		String sql="Create table "+TABLE_NAME+"("+FIELD_ID+" integer primary key autoincrement," +FIELD_CLASSNAME+" text ," +FIELD_TIME+" int," +FIELD_USER+" text);"; 
		db.execSQL(sql); 
		}
	
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		// TODO Auto-generated method stub
		String sql=" DROP TABLE IF EXISTS "+TABLE_NAME; db.execSQL(sql); onCreate(db); 
		
	} 
	
	public Cursor select(String classname) { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, "record_ClassName=?", new String[]{classname}, null, null, " record_ClassName,record_Time desc");
		return cursor;
	} 
	
	public Cursor select() { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null, " record_ClassName,record_Time desc");
		return cursor;
	} 
	
	public long insert(String classname,int time,String user) 
	{ 
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(FIELD_CLASSNAME, classname); 
		cv.put(FIELD_TIME, time); 
		cv.put(FIELD_USER, user); 
		long row=db.insert(TABLE_NAME, null, cv); 
		return row; 
		}
	
	public void delete(int id) { 
		SQLiteDatabase db=this.getWritableDatabase();
		String where=FIELD_ID+"=?";
		String[] whereValue={Integer.toString(id)}; db.delete(TABLE_NAME, where, whereValue); }
	
	public void update(int id,String classname,int time,String user) {
		SQLiteDatabase db=this.getWritableDatabase(); String where=FIELD_ID+"=?";
		String[] whereValue={Integer.toString(id)}; ContentValues cv=new ContentValues(); 
		cv.put(FIELD_CLASSNAME, classname); 
		cv.put(FIELD_TIME, time); 
		cv.put(FIELD_USER, user); 
		db.update(TABLE_NAME, cv, where, whereValue); 
		}
	
}

