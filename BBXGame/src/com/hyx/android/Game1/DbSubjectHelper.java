package com.hyx.android.Game1;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbSubjectHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME="bbx2_db"; 
	private final static int DATABASE_VERSION=5;
	private final static String TABLE_NAME="bbx_subject"; 
	public final static String FIELD_ID="_id";
	public final static String FIELD_SORTID="sort_id";
	public final static String FIELD_QUESTION="question";
	public final static String FIELD_ISTEXT="is_text";
	public final static String FIELD_MP3="mp3";
	public final static String FIELD_PIC="pic";
	public final static String FIELD_ANSWER="answer";
	public final static String FIELD_IS_SELECT="is_select";
	public final static String FIELD_SELECT_ANSWER="select_answer";
	public final static String FIELD_YES_ANSWER="yes_answer";
	public final static String FIELD_SUBJECT_ID="subject_id";
	
	public DbSubjectHelper(Context context) { 
		super(context, DATABASE_NAME,null, DATABASE_VERSION); 
	} 
	
	@Override 
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub 
		String sql="Create table "+TABLE_NAME+"("+FIELD_ID+" integer primary key autoincrement," +FIELD_SORTID+" integer ," +FIELD_QUESTION+" text," +FIELD_ISTEXT+" integer ," +FIELD_MP3+" text ," +FIELD_PIC+" text ," +FIELD_ANSWER+" text ," +FIELD_IS_SELECT+" integer," +FIELD_SELECT_ANSWER+" text," +FIELD_YES_ANSWER+" text," + FIELD_SUBJECT_ID+" integer);"; 
		db.execSQL(sql); 
		}
	
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		// TODO Auto-generated method stub
		String sql=" DROP TABLE IF EXISTS "+TABLE_NAME; db.execSQL(sql); onCreate(db); 
		
	} 
	
	public Cursor select(int sort_id) { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, "sort_id=?", new String[]{String.valueOf(sort_id)}, null, null, " _id asc ");
		return cursor;
	} 
	
	public Cursor selectBySubjectId(int subject_id) { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null,  FIELD_SUBJECT_ID + "=?", new String[]{String.valueOf(subject_id)}, null, null, " _id asc ");
		return cursor;
	} 
	
	public Cursor select() { 
		SQLiteDatabase db=this.getReadableDatabase(); 
		Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null, " _id asc");
		return cursor;
	} 
	
	public long insert(Subject model) 
	{ 
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(FIELD_SORTID, model.getSort_id()); 
		cv.put(FIELD_QUESTION, model.getQuestion()); 
		cv.put(FIELD_ISTEXT, model.getIs_text()); 
		cv.put(FIELD_MP3, model.getMp3()); 
		cv.put(FIELD_PIC, model.getPic()); 
		cv.put(FIELD_ANSWER, model.getAnswer());
		cv.put(FIELD_IS_SELECT, model.getIs_select());
		cv.put(FIELD_SELECT_ANSWER, model.getSelect_answer());
		cv.put(FIELD_YES_ANSWER, model.getYes_answer());
		cv.put(FIELD_SUBJECT_ID, model.getId());
		long row=db.insert(TABLE_NAME, null, cv); 
		return row; 
		}
	
	public void delete(int id) { 
		SQLiteDatabase db=this.getWritableDatabase();
		String where=FIELD_ID+"=?";
		String[] whereValue={Integer.toString(id)}; db.delete(TABLE_NAME, where, whereValue); }
	
	public void update(int id,Subject model) {
		SQLiteDatabase db=this.getWritableDatabase(); String where=FIELD_ID+"=?";
		String[] whereValue={Integer.toString(id)}; ContentValues cv=new ContentValues(); 
		cv.put(FIELD_SORTID, model.getSort_id()); 
		cv.put(FIELD_QUESTION, model.getQuestion()); 
		cv.put(FIELD_ISTEXT, model.getIs_text()); 
		cv.put(FIELD_MP3, model.getMp3()); 
		cv.put(FIELD_PIC, model.getPic()); 
		cv.put(FIELD_ANSWER, model.getAnswer());
		cv.put(FIELD_IS_SELECT, model.getIs_select());
		cv.put(FIELD_SELECT_ANSWER, model.getSelect_answer());
		cv.put(FIELD_YES_ANSWER, model.getYes_answer());
		cv.put(FIELD_SUBJECT_ID, model.getId());
		db.update(TABLE_NAME, cv, where, whereValue); 
		}
	
	public List<Subject> getSubjects(int sort_id){
		List<Subject> subjects = new ArrayList<Subject>();		
		Cursor cursor = select(sort_id);
		while(cursor.moveToNext()){
			Subject model = new Subject();
			model.setSort_id(cursor.getInt(1));
			model.setQuestion(cursor.getString(2));
			model.setIs_text(cursor.getString(3));
			model.setMp3(cursor.getString(4));
			model.setPic(cursor.getString(5));
			model.setAnswer(cursor.getString(6));
			model.setIs_select(cursor.getInt(7));
			model.setSelect_answer(cursor.getString(8));
			model.setYes_answer(cursor.getString(9));
			model.setId(cursor.getInt(10));
			subjects.add(model);
		}
		return subjects;
	}
	
	public List<Subject> getSubjectsBySubjectId(int subject_id){
		List<Subject> subjects = new ArrayList<Subject>();		
		Cursor cursor = selectBySubjectId(subject_id);
		while(cursor.moveToNext()){
			Subject model = new Subject();
			model.setSort_id(cursor.getInt(1));
			model.setQuestion(cursor.getString(2));
			model.setIs_text(cursor.getString(3));
			model.setMp3(cursor.getString(4));
			model.setPic(cursor.getString(5));
			model.setAnswer(cursor.getString(6));
			model.setIs_select(cursor.getInt(7));
			model.setSelect_answer(cursor.getString(8));
			model.setYes_answer(cursor.getString(9));
			model.setId(cursor.getInt(10));
			subjects.add(model);
		}
		return subjects;
	}
	
}

