package com.hyx.android.Game1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbFavoriteHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME="bbx7_db"; 
	private final static int DATABASE_VERSION=7;
	private final static String TABLE_NAME="bbx_favorite"; 
	public final static String DEVICE_ID="_id";
	public final static String DEVICE_CLASSNAME="className";
	public final static String DEVICE_SORTID="sort_id";
	public final static String DEVICE_QUESTION="question";
	public final static String DEVICE_SUBJECTID="subject_id";
	public DbFavoriteHelper(Context context) { 
		super(context, DATABASE_NAME,null, DATABASE_VERSION); 
	} 
	
	@Override 
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub 
		String sql = "Create table " + TABLE_NAME 
				+ "(" + DEVICE_ID + " integer primary key autoincrement," 
				+ DEVICE_CLASSNAME + " text ," 
				+ DEVICE_SORTID + " int," 
				+ DEVICE_QUESTION + " text," 
				+ DEVICE_SUBJECTID + " int);";
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
	
	public Cursor select(int subject_id) { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, DEVICE_SUBJECTID + "=?", new String[]{String.valueOf(subject_id)}, null, null, " _id desc");
		return cursor;
	} 
	
	public long insert(String classname,int sort_id,String question,int subjectid) 
	{ 
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(DEVICE_CLASSNAME, classname); 
		cv.put(DEVICE_SORTID, sort_id); 
		cv.put(DEVICE_QUESTION, question); 		
		cv.put(DEVICE_SUBJECTID, subjectid); 		
		long row=db.insert(TABLE_NAME, null, cv); 
		return row; 
		}
	
	public void delete(int id) { 
		SQLiteDatabase db=this.getWritableDatabase();
		String where=DEVICE_SUBJECTID+"=?";
		String[] whereValue={Integer.toString(id)}; db.delete(TABLE_NAME, where, whereValue); }
	
}

