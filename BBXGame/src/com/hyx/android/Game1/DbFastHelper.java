package com.hyx.android.Game1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbFastHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME="bbx6_db"; 
	private final static int DATABASE_VERSION=6;
	private final static String TABLE_NAME="bbx_fast"; 
	public final static String FIELD_ID="_id";	
	public final static String FIELD_SORTID="sort_id";
	public final static String FIELD_USETIMES="user_times";
	public DbFastHelper(Context context) { 
		super(context, DATABASE_NAME,null, DATABASE_VERSION); 
	} 
	
	@Override 
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub 
		String sql="Create table "+TABLE_NAME+"("+FIELD_ID+" integer primary key autoincrement," +FIELD_SORTID+" int," +FIELD_USETIMES+" int);"; 
		db.execSQL(sql); 
	}
	
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		// TODO Auto-generated method stub
		String sql=" DROP TABLE IF EXISTS "+TABLE_NAME; db.execSQL(sql); onCreate(db); 
		
	} 

	
	public Cursor select() { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null, " _id desc");
		return cursor;
	} 
	
	public Cursor select(int sort_id) { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, FIELD_SORTID + "=?", new String[]{String.valueOf(sort_id)}, null, null, " _id desc");
		return cursor;
	} 
	
	public void update(int sort_id,int user_times) {
		SQLiteDatabase db=this.getWritableDatabase(); 
		String where=FIELD_SORTID+"=?";
		String[] whereValue={Integer.toString(sort_id)}; ContentValues cv=new ContentValues(); 
	
		cv.put(FIELD_USETIMES, user_times); 		

		db.update(TABLE_NAME, cv, where, whereValue); 
	}
	
	public long insert(int sort_id,int user_times) 
	{ 
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();	
		cv.put(FIELD_SORTID, sort_id); 		
		cv.put(FIELD_USETIMES, user_times); 	
		long row=db.insert(TABLE_NAME, null, cv); 
		return row; 
		}
	
	public void delete(int id) { 
		SQLiteDatabase db=this.getWritableDatabase();
		String where=FIELD_SORTID+"=?";
		String[] whereValue={Integer.toString(id)}; db.delete(TABLE_NAME, where, whereValue); }
	
}

