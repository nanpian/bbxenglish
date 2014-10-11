package com.hyx.android.Game1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hyx.android.Game.adapter.FavoriteAdapter;
import com.hyx.android.Game.adapter.ScoreAdapter;
import com.hyx.android.Game.util.AppPreferences;
import com.hyx.android.Game.util.ClassManager;
import com.hyx.android.Game.util.DownLoaderTask;
import com.hyx.android.Game.util.DownloadManager;
import com.hyx.android.Game.util.FavoriteItem;
import com.hyx.android.Game.util.IConst;
import com.hyx.android.Game.util.MemberManager;
import com.hyx.android.Game.util.Rank;
import com.hyx.android.Game.util.RankManager;
import com.hyx.android.Game.util.Utils;
import com.hyx.uploadwithprogress.http.HttpMultipartPost;
import com.iflytek.cloud.ui.RecognizerDialog;

public class MyGameActivity extends BaseActivity {
	

	private static final int Class_ID = Menu.FIRST;
	private static final int EXIT_ID = Menu.FIRST + 1;
	private static int ClickNum = 0;

	Timer timer = null;
	int totalSeconds = 0;

	public static MyGameActivity instance;
	

   AppPreferences appPrefs ; 
   String classFontSize;
   String subjectFontSize;
   String answerFontSize;
   String picHeight;
   
   //TextView tvPre ;
   Button tvNext ;

	ListView lv;
	List<SubjectClass> mapFirstClass = new ArrayList<SubjectClass>();
	public static List<SubjectClass> mapSecondClass = new ArrayList<SubjectClass>();
	List<SubSubjectClass> subclasses = new ArrayList<SubSubjectClass>();
	HashMap<Integer, List<Subject>> mapAllSubject = null;
	List<Subject> curSubjects = null;
	int firstClassId=0;

	LayoutInflater inflater;
	LinearLayout lin;
	LinearLayout bottom;
	SimpleAdapter listItemAdapter;

	int subjectIndex = 0;	
	String classname;
	private static int audioTime ;
	int classid;
	String nextclassname;
	String subclassname;

	MusicPlayer mMPlayer = null;
	AudioRecorder mRecorder=null;
	
	Button btnClass;
	Button btnFavorite;
	Button btnRecordAudio;
	Button btnTestRecord;
	Button btnIntroduce;

	DbHelper dbHelper;
	DbTestHelper dbTestHelper;
	DbSubjectHelper dbSubjectHelper;
	DbFavoriteHelper dbFavoriteHelper;
	DbFastHelper  dbFastHelper;
	
	Button btnPlayAllSource;
	Button btnNextStep;
	Button btnPlayAllRecord;
	Button btnViewRanking;
	Button btnScore;
	Button btnRedo;
	TextView tvCurRank;
	LinearLayout llScore;
	ListView lvScore;
	Rank rank = null;
	boolean isNeedRecordAudio = false;
	
	int posAnswer = 0;
	boolean recalc = false;
	
	int screenWidth;
	public static Context mContext;
	
	Subject curSubject = null;
	
	//识别窗口
	private RecognizerDialog iatDialog;
	// Tip
	private Toast mToast;
	
	LinearLayout layoutOption = null,layoutOption2 = null,layoutOption3 = null,layoutOption4 = null;
	Button tvShowAnswer;
	Button btnShowQuestion;
	TextView tvQuestion;
	
	
	List<String> curSubjectArray = new ArrayList<String>();
	List<String> curSubjectRealArray = new ArrayList<String>();
	Map<String ,Integer> mapClassId = new HashMap<String,Integer>();
	
	private static Handler handler = new Handler();
	private static String urlPath;
	private static String dirName;
	
    Runnable runui = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			loadThirdClass(MyGameActivity.this.subclassname);
		}   	
    };
    
	Runnable runnableCheck = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SharedPreferences sharedata = getSharedPreferences("register", 0);
			String right_account = sharedata.getString("register_username",
					null);
			String right_pwd = sharedata.getString("register_userpwd", null);
			SharedPreferences sharedata2 = getSharedPreferences("sms_verify", 0);
			String mobilenum = sharedata2.getString("mobienum", null);
			String checkcode = Utils.MD5(right_account + right_pwd + "defabc");
			SharedPreferences sharedatat = getSharedPreferences(
					"login_time", 0);
			String logindate = sharedatat.getString("logindate", null);
			String ajaxHost = "http://www.tgbbx.com/api/api6_2.php";
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(ajaxHost
					+ "?action=checkReLogin&username=" + right_account
					+ "&pwd=" + right_pwd + "&tel=" + mobilenum + "&logindate="
					+ logindate + "&checkcode=" + checkcode);
			Log.d("checktest", ajaxHost + "?action=checkReLogin&username="
					+ right_account + "&pwd=" + right_pwd + "&tel=" + mobilenum
					+ "&logindate=" + logindate + "&checkcode=" + checkcode);
			try {
				HttpResponse response = client.execute(request);
				/* response code */
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				StringBuilder result = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				Log.d("checktest", "The result server return is " + result);
				String res = result.toString();
				JSONObject obj = new JSONObject(res);
				if ("true".equals(obj.getString("success"))) {
					handler.postDelayed(runnableCheck, 30000);
				} else {
					handler.removeCallbacks(runnableCheck);
					String info = obj.getString("info").toString();
					new AlertDialog.Builder(MyGameActivity.this)
							// 设置标题
							.setTitle("提示")
							// **设置icon
							.setIcon(android.R.drawable.ic_dialog_info)
							.setCancelable(false)
							// **设置内容
							.setMessage("用户在其他地方已登陆")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									}).show();
					// finish();

				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};
    
     Runnable runnableauto =new Runnable(){
		   @Override
		public void run() {
			if (MusicPlayer.mIsPlayFinished) {
				RankManager.getInstance().setValid(false);
				boolean ret = ChangeSubject(subjectIndex, false, true);
				if (nextSubjectIndex > curSubjects.size()) {
					if (tvNext != null) {
						tvNext.setVisibility(View.INVISIBLE);
					}
				} else {
					if (tvNext != null) {
						tvNext.setVisibility(View.VISIBLE);
					}
				}
				if (!ret)
					return;
				audioTime = appPrefs.getAudioTime() * 1000;
				Log.d("dewetest", "auto time is " + audioTime);
				handler.postDelayed(this, audioTime);
			} else return;
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		//setTheme(android.R.style.Theme_Translucent);
		super.onCreate(savedInstanceState);
		mContext = this;
        setContentView(R.layout.main);
		
		

		try {
			
			
			appPrefs = new AppPreferences(getApplicationContext()); 
		
			classFontSize = appPrefs.getClassFontSize();
			subjectFontSize = appPrefs.getSubjectFontSize();
			answerFontSize = appPrefs.getAnswerFontSize();
			
			picHeight = appPrefs.getPicHeight();
		    audioTime = appPrefs.getAudioTime()*1000;

			instance = this;
			mMPlayer = new MusicPlayer(this);
			mRecorder = new AudioRecorder(this);
			dbHelper = new DbHelper(this);
			dbTestHelper = new DbTestHelper(this);
			dbSubjectHelper = new DbSubjectHelper(this);
			dbFavoriteHelper = new DbFavoriteHelper(this);
			dbFastHelper = new DbFastHelper(this);

			/*
			 * Resources res = getResources(); Drawable drawable =
			 * res.getDrawable(R.drawable.bkcolor);
			 * this.getWindow().setBackgroundDrawable(drawable);
			 */
			// 加载分类
//			Resources r = getResources();
//			XmlPullParser xParser = r.getXml(R.xml.allclass);			
			
			
			
		
			
//			if (!Utils.getNetworkStatus(mContext)) {	
//				((XmlResourceParser) xParser).close();
//			}

			inflater = LayoutInflater.from(this);
			lin = (LinearLayout) findViewById(R.id.top);

			bottom = (LinearLayout) findViewById(R.id.bottom);

			btnClass = (Button) findViewById(R.id.btnClass);
			btnFavorite = (Button) findViewById(R.id.btnFavorite);
			btnTestRecord = (Button) findViewById(R.id.btnTestRecord);
			btnIntroduce = (Button) findViewById(R.id.btnIntroduce);

			
			//加载一级分类
			loadFirstClass();

			// 简介按钮事件处理
			btnIntroduce.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MyGameActivity.this, settings.class);
					startActivity(intent);
				}
			});

			btnFavorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 btnFavorite.setTextColor(getResources().getColor(R.color.grayz));
					 btnTestRecord.setTextColor(getResources().getColor(R.color.ivory));
					 btnIntroduce.setTextColor(getResources().getColor(R.color.ivory));
					 btnClass.setTextColor(getResources().getColor(R.color.ivory));
					loadFavorite();
					
				}
			});

			btnTestRecord.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 picHeight = appPrefs.getPicHeight();
					 btnFavorite.setTextColor(getResources().getColor(R.color.ivory));
					 btnTestRecord.setTextColor(getResources().getColor(R.color.grayz));
					 btnIntroduce.setTextColor(getResources().getColor(R.color.ivory));
					 btnClass.setTextColor(getResources().getColor(R.color.ivory));
					loadTestRecord();
				}
			});

			// 分类按钮事件处理
			btnClass.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					classFontSize = appPrefs.getClassFontSize();
					answerFontSize = appPrefs.getAnswerFontSize();
					picHeight = appPrefs.getPicHeight();
					btnFavorite.setTextColor(getResources().getColor(R.color.ivory));
					btnTestRecord.setTextColor(getResources().getColor(R.color.ivory));
					btnIntroduce.setTextColor(getResources().getColor(R.color.ivory));
					btnClass.setTextColor(getResources().getColor(R.color.grayz));
					
					loadFirstClass();
					//loadClass(inflater, lin, listItemAdapter, 1);					

				}
			});
			
			
			
//			//用户登录
//			SpeechUser.getUser().login(MyGameActivity.this, null, null
//					, "appid=" + getString(R.string.app_id), listener);
			
			mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
			

		} catch (Exception e) {
			Log.e("ERROR", e.toString());
		}
	}

	private void showToolbar(int visible) {
		btnFavorite.setVisibility(visible);
		btnTestRecord.setVisibility(visible);
		btnClass.setVisibility(visible);
		btnIntroduce.setVisibility(visible);
		bottom.setVisibility(visible);
		if (visible == View.INVISIBLE) {
			btnFavorite.setVisibility(View.GONE);
			btnTestRecord.setVisibility(View.GONE);
			btnClass.setVisibility(View.GONE);
			btnIntroduce.setVisibility(View.GONE);
			bottom.setVisibility(View.GONE);
		}
	}

	public void loadFavorite() {
		if (timer != null)
			timer.cancel();
		
		List<FavoriteItem> favoriteList = new ArrayList<FavoriteItem>();
		Cursor cursor = dbFavoriteHelper.select();
		while (cursor.moveToNext()) {

			FavoriteItem model = new FavoriteItem();
			model.setClassName(cursor.getString(1));
			model.setSort_id(cursor.getInt(2));
			model.setQuestion(cursor.getString(3));
			model.setSubject_id(cursor.getInt(4));
			favoriteList.add(model);
			
		}

		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.favoritelist, null).findViewById(R.id.llFavorite);
		lin.removeAllViews();
		lin.addView(layout);

		ListView lv = (ListView) layout.findViewById(R.id.lvFavorite);	
		
		ListAdapter adapter = new FavoriteAdapter(	MyGameActivity.this, favoriteList) ;
		lv.setAdapter(adapter);		
		View v = this.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		ViewGroup.LayoutParams params = lv.getLayoutParams();
		params.height = v.getHeight();
		lv.setLayoutParams(params);

	}
	/*
	private void loadRecord() {
		if (timer != null)
			timer.cancel();
		ArrayList<HashMap<String, Object>> listRecordData = new ArrayList<HashMap<String, Object>>();
		Cursor cursor = dbHelper.select();
		while (cursor.moveToNext()) {

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(DbHelper.FIELD_CLASSNAME, cursor.getString(1));
			map.put(DbHelper.FIELD_TIME, cursor.getString(2));
			map.put(DbHelper.FIELD_USER, cursor.getString(3));
			listRecordData.add(map);
		}

		// 父分类适配器
		final SimpleAdapter listRecordAdapter = new SimpleAdapter(this,
				listRecordData, R.layout.recorditem, new String[] {
						DbHelper.FIELD_CLASSNAME, DbHelper.FIELD_TIME,
						DbHelper.FIELD_USER }, new int[] { R.id.tvClassname,
						R.id.tvTime, R.id.tvUser });

		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.recordlist, null).findViewById(R.id.llRecord);
		lin.removeAllViews();
		lin.addView(layout);

		ListView lv = (ListView) layout.findViewById(R.id.lvRecord);
		lv.setAdapter(listRecordAdapter);

		int totalHeight = 0;
		for (int i = 0; i < listRecordAdapter.getCount(); i++) {
			View listItem = listRecordAdapter.getView(i, null, lv);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = lv.getLayoutParams();
		params.height = totalHeight
				+ (lv.getDividerHeight() * (listRecordAdapter.getCount() - 1));
		// ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
		lv.setLayoutParams(params);

	}
	*/

	private void loadTestRecord() {
		if (timer != null)
			timer.cancel();
		ArrayList<HashMap<String, Object>> listRecordData = new ArrayList<HashMap<String, Object>>();
		Cursor cursor = dbTestHelper.select();
		while (cursor.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(DbTestHelper.FIELD_CLASSNAME, cursor.getString(1));
			map.put(DbTestHelper.FIELD_TIME, cursor.getString(2));
			map.put(DbTestHelper.FIELD_DATE, cursor.getString(4));
			listRecordData.add(map);
		}

		// 父分类适配器
		final SimpleAdapter listRecordAdapter = new SimpleAdapter(this,
				listRecordData, R.layout.testrecorditem, new String[] {
						DbTestHelper.FIELD_CLASSNAME, DbTestHelper.FIELD_TIME,
						DbTestHelper.FIELD_DATE }, new int[] {
						R.id.tvClassname, R.id.tvTime, R.id.tvDate });

		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.testrecordlist, null).findViewById(R.id.llRecord);
		lin.removeAllViews();
		lin.addView(layout);

		ListView lv = (ListView) layout.findViewById(R.id.lvRecord);
		lv.setAdapter(listRecordAdapter);

		int totalHeight = 0;
		for (int i = 0; i < listRecordAdapter.getCount(); i++) {
			View listItem = listRecordAdapter.getView(i, null, lv);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = lv.getLayoutParams();
		params.height = totalHeight
				+ (lv.getDividerHeight() * (listRecordAdapter.getCount() - 1));
		// ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
		lv.setLayoutParams(params);
	}
	
	
	
	private void loadFirstClass(){
		
		if (Utils.getNetworkStatus(mContext)) {	
			XmlPullParser xParser = ClassManager.downloadClass(0);
			this.mapFirstClass = Utils.getAllClass(xParser);
		}else{
			return;
		}
	
		// 存储数据的数组列表
		ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

		int rows = (mapFirstClass.size() % 2 == 0) ? mapFirstClass.size() / 2
				: mapFirstClass.size() / 2 + 1;
		for (int i = 0; i < rows; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("friend_image", R.drawable.icon);
			map.put("classname_1", mapFirstClass.get(i * 2).name);				
			mapClassId.put(mapFirstClass.get(i * 2).name, mapFirstClass.get(i * 2).id);
			
			if (i * 2 + 1 < mapFirstClass.size()) {
				map.put("classname_2", mapFirstClass.get(i * 2 + 1).name);
				mapClassId.put(mapFirstClass.get(i * 2+1).name, mapFirstClass.get(i * 2+1).id);
				
			} else {
				// map.put("classname_2", "");
			}

			listData.add(map);
		}
		// 父分类适配器

		listItemAdapter = new SimpleAdapter(this, listData,
				R.layout.classitem, new String[] { "classname_1",
						"classname_2" }, new int[] { R.id.classname_1,
						R.id.classname_2 });

		loadClass(inflater, lin, listItemAdapter, 1);
		
	}

	// 加载分类
	private void loadClass(LayoutInflater inflater, LinearLayout lin,SimpleAdapter listItemAdapter, final int level) {

		try {
			if (timer != null)
				timer.cancel();

			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.classlist, null).findViewById(R.id.llClass);

			lv = (ListView) layout.getChildAt(0);
			lv.setAdapter(listItemAdapter);

			lin.removeAllViews();
			lin.addView(layout);

			int totalHeight = 0;

			List<String> lstClassname=new ArrayList<String>();
			
			for (int i = 0; i < listItemAdapter.getCount(); i++) {

				View listItem = listItemAdapter.getView(i, null, lv);

				listItem.measure(0, 0);

				totalHeight += listItem.getMeasuredHeight();
			}
			
			
			/*List<Subject> subjects = dbSubjectHelper.getSubjects(classid);
			if (subjects.size() == 0) {
				loadThread = true;
				//初始化
				final ProgressDialog dialog = ProgressDialog.show(MyGameActivity.this, "", "正在初始化...", true);
				
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
//						Resources r = getResources();
//						XmlPullParser xParser = r.getXml(R.xml.subjects);
						
						if (Utils.getNetworkStatus(mContext)) {	
							XmlPullParser xParser = ClassManager.downloadSubjects(classid);
							if(xParser!=null)
								mapAllSubject = Utils.getSubjects(xParser);
						}						
						
						
//						if (!Utils.getNetworkStatus(mContext)) {	
//							((XmlResourceParser) xParser).close();
//						}
						
						for (int cls : mapAllSubject.keySet()) {
							List<Subject> lstSubject = mapAllSubject.get(cls);
							for (Subject model : lstSubject) {
								// 插入题目至数据库
								// System.out.println("insert subject id="+model.getId());
								dbSubjectHelper.insert(model);
							}
						}
						dialog.dismiss();
						
						
						if (mapAllSubject.containsKey(classid))
							curSubjects = mapAllSubject.get(classid);
						else
							curSubjects = dbSubjectHelper.getSubjects(classid);

						mRecorder.clearFile(curSubjects.size()+1);
						//System.out.println("curSubjects.size()=" + curSubjects.size());

						if (curSubjects.size() > 0) {
							subjectIndex = 0;
							boolean ret = MoveNext(subjectIndex,false);
							if (!ret)
								return;
						}
						
						
					}
				};
				new Thread(runnable).start();
			}
			*/
			
			ViewGroup.LayoutParams params = lv.getLayoutParams();
			params.height = totalHeight
					+ (lv.getDividerHeight() * (listItemAdapter.getCount() - 1));
			// ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
			lv.setLayoutParams(params);

			// 分类增加事件处理
			lv.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
				@Override
				public void onChildViewAdded(View parent, View child) {
					// TODO Auto-generated method stub

					Button btn1 = (Button) child.findViewById(R.id.classname_1);
					if (btn1.getText().toString().equals("")) {
						btn1.setVisibility(View.INVISIBLE);
					}
					btn1.setTextSize(Integer.parseInt(classFontSize));
					btn1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Button btn = (Button) v;
							String classname = btn.getText().toString();
						
							// System.out.println("btn.getText="+btn.getText());
							if (level == 1) {
								Log.d("dewei", "level 1 class name = " +classname);
								subclassname = classname;
								loadSecondClass(classname);
							} 
							else if (level == 2) {
								Log.d("dewei", "level 2 class name = " +classname);
								subclassname = classname;
								loadThirdClass(classname);
							} else if (level == 3) {
								Log.d("dewei", "level 3 class name = " +classname);
								loadSubjects(classname);
							}
						}
					});

					Button btn2 = (Button) child.findViewById(R.id.classname_2);
					if (btn2.getText().toString().equals("")) {
						btn2.setVisibility(View.INVISIBLE);
					}
					btn2.setTextSize(Integer.parseInt(classFontSize));
					
					btn2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Button btn = (Button) v;
							String classname = btn.getText().toString();
							// System.out.println("btn.getText="+btn.getText());
						
							if (level == 1) {
								subclassname = classname;
								loadSecondClass(classname);
							} else if (level == 2) {
								subclassname = classname;
								loadThirdClass(classname);
							} else if (level == 3) {
								loadSubjects(classname);
							}
						}
					});
				}

				@Override
				public void onChildViewRemoved(View parent, View child) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {
			Log.e("ERROR", e.toString());
		}

	}
	
	@Override
	protected  void updateUI(){
		loadSubData();
		super.updateUI();
	}
	
	/**
	 * 初始化所有子分类
	 */
    private boolean initAllSubClass(){
    	boolean needInitSubject=false;
    	boolean needInitResource = true;
    	ProgressDialog dialog = ProgressDialog.show(MyGameActivity.this, "", "正在初始化...", true);
    	dialog.show();
    	String dirName = Environment.getExternalStorageDirectory()+"/BBX/audio/";
		/*if(!DownloadManager.checkDownload(MyGameActivity.this, MemberManager.getUsername(MyGameActivity.this))){			
			 return false;
		}*/
		Log.d("dewei","important!the subclassname is "+subclassname);
		for(SubSubjectClass subClass:subclasses){
			//Log.d("dewei","initAllSubClass : " +subClass.getId()+subClass.getName());
			List<Subject> subjects = dbSubjectHelper.getSubjects(subClass.getId());
			if (subjects.size() == 0) {	
				Log.d("dewei","need to init");
				needInitSubject  = true;
			} else {
				needInitResource = false;
			}

		}
		Log.d("dewei","init end");
		dialog.dismiss();
		if(needInitSubject&&needInitResource) {
			// _handler.sendEmptyMessage(MSG_UPDATEUI);
			loadAndUnzip();
		} else if (needInitSubject&&!needInitResource) {
			loadhandler.sendEmptyMessage(2);
		} else if (!needInitSubject&&needInitResource) {
			loadAndUnzip();
		} else {
			 return false;
		}
		return false;
    }
    
	private long getOriginalSize(ZipFile zip) {	
		Enumeration<ZipEntry> entries =  (Enumeration<ZipEntry>) zip.entries();
		long originalSize = 0l;
		while(entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if(entry.getSize()>=0) {
				originalSize+=entry.getSize();
			}
		}
		return originalSize;
	}
    public  Handler loadhandler = new Handler() {
    	public void handleMessage(Message msg) {    	//初始化
    		final ProgressDialog dialog = ProgressDialog.show(MyGameActivity.this, "", "初始化题目数据库...", true);
    		Runnable runnable = new Runnable() {
    			@Override
    			public void run() {
    				
    				for(SubSubjectClass subClass:subclasses){
    					List<Subject> subjects = dbSubjectHelper.getSubjects(subClass.getId());
    					if (subjects.size() == 0) {
    					
    							HashMap<Integer,List<Subject>> mapAllSubject = new HashMap<Integer,List<Subject>>();
    							if (Utils.getNetworkStatus(mContext)) {	
    								XmlPullParser xParser = ClassManager.downloadSubjects(firstClassId,subClass.getId());
    								if(xParser!=null)
    									mapAllSubject = Utils.getSubjects(firstClassId,xParser);
    							}	
    							
    	
    							for (int cls : mapAllSubject.keySet()) {
    								List<Subject> lstSubject = mapAllSubject.get(cls);
    								for (Subject model : lstSubject) {
    									// 插入题目至数据库
    									dbSubjectHelper.insert(model);
    								}
    							}
    						
    						
    					}
    				}

    			
    				dialog.dismiss();	
    				
    				
    				
    				_handler.sendEmptyMessage(MSG_UPDATEUI);
    				
    				
    			}
    		};
    		new Thread(runnable).start();}
    };
    private void loadAndUnzip() {
    	int subClassId = 0 ;
    	if (Utils.getNetworkStatus(mContext)) {	
			for (SubjectClass sub : mapSecondClass) {
				if (MyGameActivity.this.subclassname.equals(sub.getName())) {
					Log.d("dewei","important!the subclassname is "+subclassname);
					subClassId = sub.getId();
					break;
				}
			}
			urlPath ="http://www.tgbbx.com/resources/res"+ firstClassId +"/res"+subClassId+".zip";
			Log.d("dewei2",urlPath);
			Log.d("dewei2","test this case");
			dirName = Environment.getExternalStorageDirectory()+"/BBX/";
			File f = new File(dirName);
			if(!f.exists())
			{
			    f.mkdir();
			}
			File f3 = new File(dirName+"temp.zip");
			if(f3.exists()){
				f3.delete();
			}
			Log.d("dewei2","remove temp zip");
			DownLoaderTask downloadtask = new DownLoaderTask(urlPath, dirName+"temp.zip", MyGameActivity.this);
			downloadtask.execute();
			if(f3.exists()) {
				f3.delete();
			}
			
    	}
    }


    private void loadSubData(){
    	ArrayList<HashMap<String, Object>> listSubData = new ArrayList<HashMap<String, Object>>();
    	int rows = (subclasses.size() % 2 == 0) ? subclasses.size() / 2
				: subclasses.size() / 2 + 1;

		for (int i = 0; i < rows; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("friend_image", R.drawable.icon);
			map.put("classname_1", subclasses.get(i * 2).name);
			if (i * 2 + 1 < subclasses.size()) {
				map.put("classname_2", subclasses.get(i * 2 + 1).name);
			} else {
				// map.put("classname_2", "");
			}

			listSubData.add(map);
		}

		final LayoutInflater inflater = LayoutInflater.from(this);
		final LinearLayout lin = (LinearLayout) findViewById(R.id.top);

		// 父分类适配器
		/*
		 * final SimpleAdapter listSubItemAdapter = new SimpleAdapter(this,
		 * listSubData, R.layout.classitem, new String[] { "classname_1",
		 * "classname_2", "classname_3" }, new int[] { R.id.classname_1,
		 * R.id.classname_2, R.id.classname_3 });
		 */

		final SimpleAdapter listSubItemAdapter = new SimpleAdapter(this,
				listSubData, R.layout.classitem, new String[] { "classname_1",
						"classname_2" }, new int[] { R.id.classname_1,
						R.id.classname_2 });

		loadClass(inflater, lin, listSubItemAdapter, 3);

		timer = null;
    	
    }
    
	// 加载二级分类
	private void loadSecondClass(String classname) {
		
		if(!mapClassId.containsKey(classname)) return;
		
		int classid = mapClassId.get(classname);
		
		firstClassId = classid;
		
		if (Utils.getNetworkStatus(mContext)) {	
			XmlPullParser xParser = ClassManager.downloadClass(classid);
			this.mapSecondClass = Utils.getAllClass(xParser);
		}else{
			return;
		}
		
		// 存储数据的数组列表
		ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

		int rows = (mapSecondClass.size() % 2 == 0) ? mapSecondClass.size() / 2
				: mapSecondClass.size() / 2 + 1;
		for (int i = 0; i < rows; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("friend_image", R.drawable.icon);
			map.put("classname_1", mapSecondClass.get(i * 2).name);				
			mapClassId.put(mapSecondClass.get(i * 2).name, mapSecondClass.get(i * 2).id);
			
			if (i * 2 + 1 < mapSecondClass.size()) {
				map.put("classname_2", mapSecondClass.get(i * 2 + 1).name);
				mapClassId.put(mapSecondClass.get(i * 2+1).name, mapSecondClass.get(i * 2+1).id);
				
			} else {
				// map.put("classname_2", "");
			}

			listData.add(map);
		}
		// 父分类适配器

		listItemAdapter = new SimpleAdapter(this, listData,
				R.layout.classitem, new String[] { "classname_1",
						"classname_2" }, new int[] { R.id.classname_1,
						R.id.classname_2 });

		loadClass(inflater, lin, listItemAdapter, 2);
	}

	// 加载子分类
	private void loadThirdClass(String classname) {
		
		this.subclasses = null;
		for (SubjectClass sub : this.mapSecondClass) {
			if (classname.equals(sub.getName())) {
				subclasses = sub.getSubclass();				
				break;
			}
		}
		if (subclasses == null)
			return;
		
		//初始化所有子类
		if(!initAllSubClass()){
			loadSubData();	
		}
	}

	// 加载题目
	private void loadSubjects(String classname) {

		int classid = 0;
		
		int index=0;
		
		for (SubSubjectClass sub : subclasses) {
			if (classname.equals(sub.getName())) {
				classid = sub.getId();
				
				if(index+1 < subclasses.size()){
					SubSubjectClass nextClass  = subclasses.get(index + 1);					
					nextclassname = nextClass.getName();
				}
				else{
					SubSubjectClass nextClass  = subclasses.get(0);		
					nextclassname = nextClass.getName();
				}
				
				break;
			}
			index++;
		}
		this.classname = classname;
		this.classid = classid;
		loadSubjects(classid);
		
	}

	boolean loadThread = false;
	// 加载题目
	private void loadSubjects(final int classid) {
		RankManager.getInstance().setSort_id(classid);
		RankManager.getInstance().setValid(true);
		Log.d("dewei","load subject start");
		
		subjectFontSize = appPrefs.getSubjectFontSize();
		loadThread = false;
		if (this.mapAllSubject == null) 
			mapAllSubject = new HashMap<Integer, List<Subject>>();
		/*if (this.mapAllSubject == null) {
			mapAllSubject = new HashMap<Integer, List<Subject>>();*/

			List<Subject> subjects = dbSubjectHelper.getSubjects(classid);
			if (subjects.size() == 0) {
				Log.d("dewei","load thread start");
				loadThread = true;
				//初始化
				final ProgressDialog dialog = ProgressDialog.show(MyGameActivity.this, "", "正在下载题目...", true);
				dialog.show();
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
//						Resources r = getResources();
//						XmlPullParser xParser = r.getXml(R.xml.subjects);
						
						if (Utils.getNetworkStatus(mContext)) {	
							XmlPullParser xParser = ClassManager.downloadSubjects(firstClassId,classid);
							if(xParser!=null)
								mapAllSubject = Utils.getSubjects(firstClassId,xParser);
						}						
						
						
//						if (!Utils.getNetworkStatus(mContext)) {	
//							((XmlResourceParser) xParser).close();
//						}
						
						for (int cls : mapAllSubject.keySet()) {
							List<Subject> lstSubject = mapAllSubject.get(cls);
							for (Subject model : lstSubject) {
								// 插入题目至数据库
								// System.out.println("insert subject id="+model.getId());
								dbSubjectHelper.insert(model);
							}
						}
						dialog.dismiss();
						
						
						if (mapAllSubject.containsKey(classid))
							curSubjects = mapAllSubject.get(classid);
						else
							curSubjects = dbSubjectHelper.getSubjects(classid);

						mRecorder.clearFile(curSubjects.size()+1);
						//System.out.println("curSubjects.size()=" + curSubjects.size());

						if (curSubjects.size() > 0) {
							subjectIndex = 0;
							boolean ret = MoveNext(subjectIndex,false);
							if (!ret)
								return;
						}
						
						
					}
				};
				new Thread(runnable).start();

				
				
				
				
			} else {
				mapAllSubject.put(classid, subjects);
				if (mapAllSubject.containsKey(classid))
					this.curSubjects = mapAllSubject.get(classid);
				else
					this.curSubjects = dbSubjectHelper.getSubjects(classid);

				mRecorder.clearFile(curSubjects.size()+1);
				//System.out.println("curSubjects.size()=" + curSubjects.size());

				if (curSubjects.size() > 0) {
					subjectIndex = 0;
					boolean ret = MoveNext(subjectIndex,false);
					if (!ret)
						return;
				}
			}
		/*}
		
		else{
			
			if (mapAllSubject.containsKey(classid))
				this.curSubjects = mapAllSubject.get(classid);
			else
				this.curSubjects = dbSubjectHelper.getSubjects(classid);

			//System.out.println("curSubjects.size()=" + curSubjects.size());
			mRecorder.clearFile(curSubjects.size()+1);

			if (curSubjects.size() > 0) {
				subjectIndex = 0;
				boolean ret = MoveNext(subjectIndex,false);
				if (!ret)
					return;
			}
			
		}*/


	}

	boolean show = false;

	
	Handler mHandler = new Handler() { 
	        @Override 
	        public void handleMessage(Message msg) { 	        	
	        	showToolbar(View.INVISIBLE);	
	        	
	        	int index = Integer.parseInt(msg.obj.toString()); 
	        	ChangeSubject(index,false,true);
	     } 
	};

	private boolean MoveNext(int index,final Boolean isFavorite) {
		try {
			mMPlayer.StopVoice();
			mRecorder.stopRecording();

			final int nextSubjectIndex = index + 1;
			if (nextSubjectIndex > curSubjects.size()) {
				timer.cancel();


					showToolbar(View.VISIBLE);											
					
					
					if(isFavorite){
						loadFavorite();
					}else{

						SharedPreferences sharedata = getSharedPreferences(
								"login", 0);
						final String account = sharedata
								.getString("account", null);
						if (account != null) {
							Cursor cursor = dbHelper
									.select();// classname
							if (cursor.moveToNext()) {
								if (cursor.getInt(2) > totalSeconds) {
									dbHelper.update(cursor
											.getInt(0),
											classname,
											totalSeconds,
											account);
								}
							} else {
								dbHelper.insert(classname,
										totalSeconds, account);
							}

							dbTestHelper.insert(classname,
									totalSeconds, account,
									Utils.GetNowDate());
							
							RankManager.getInstance().setSeconds(totalSeconds);
						}
						
						ChangeResult();
					}
					
		
				return true;
			} else {
				
				if(!loadThread){
		            SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);  
		            boolean autoplay_checked = shp.getBoolean("autoplay", false);  
		           Log.d("dewei222","autplay checked is " + autoplay_checked);
		            if(!autoplay_checked) { 
		            	 Log.d("dewei222","autoplay is started!");
		            	 ChangeSubject(index,false,true);
		            	handler.postDelayed(runnableauto, 10000);
		            } else {
					showToolbar(View.INVISIBLE);
					ChangeSubject(index,false,true);
		            }
				}else{
					//showToolbar(View.INVISIBLE);
					Message msg = new Message(); 		      
			        msg.obj =index; 
			        mHandler.sendMessage(msg);
			        loadThread = false;		
				}
				return false;
			}

		} catch (Exception e) {
			// Log.e("ERROR",e.toString());
			System.out.println(e.toString());
			return false;
		}
	}
	
	


	float allWidth = 0;
	float allAnswerWidth = 0;
	
	

	

	private TextView AddAnswer(String answer,LinearLayout layoutAnswer,LinearLayout layoutAnswer2,LinearLayout layoutAnswer3,LinearLayout layoutAnswer4){
		
		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, 0);
		
		// 处理点击答案

		
		
		TextView tvAnswer = Utils.CreateOptionView(true,
				instance,answer,Integer.parseInt(answerFontSize));
		Paint paint = tvAnswer.getPaint();
		float textWidth = paint.measureText(answer);
		float splitWidth = paint.measureText(" ") * 2;	

		if (recalc) {
			int width = 0;
			if (layoutAnswer4.getChildCount() > 0) {
				posAnswer = 3;
				for (int i = 0; i < layoutAnswer4
						.getChildCount(); i++) {
					TextView tv = (TextView) layoutAnswer4
							.getChildAt(i);
					Paint pai = tv.getPaint();
					width += pai.measureText(tv.getText()
							.toString()) + splitWidth;
				}
			} else if (layoutAnswer3.getChildCount() > 0) {
				posAnswer = 2;
				for (int i = 0; i < layoutAnswer3
						.getChildCount(); i++) {
					TextView tv = (TextView) layoutAnswer3
							.getChildAt(i);
					Paint pai = tv.getPaint();
					width += pai.measureText(tv.getText()
							.toString()) + splitWidth;
				}
			} else if (layoutAnswer2.getChildCount() > 0) {
				posAnswer = 1;
				for (int i = 0; i < layoutAnswer2
						.getChildCount(); i++) {
					TextView tv = (TextView) layoutAnswer2
							.getChildAt(i);
					Paint pai = tv.getPaint();
					width += pai.measureText(tv.getText()
							.toString()) + splitWidth;
				}
			} else {
				posAnswer = 0;
				for (int i = 0; i < layoutAnswer
						.getChildCount(); i++) {
					TextView tv = (TextView) layoutAnswer
							.getChildAt(i);
					Paint pai = tv.getPaint();
					width += pai.measureText(tv.getText()
							.toString()) + splitWidth;
				}
			}

			recalc = false;

			allAnswerWidth = width + textWidth + splitWidth;
		} else {
			allAnswerWidth += textWidth + splitWidth;
		}
		
		if (allAnswerWidth > screenWidth) {
			posAnswer++;
			allAnswerWidth = textWidth + splitWidth;
		}

		if (posAnswer >= 3) {
			layoutAnswer4
					.addView(
							tvAnswer,
							layoutParams);
		} else if (posAnswer == 2) {
			layoutAnswer3
					.addView(
							tvAnswer,
							layoutParams);
		} else if (posAnswer == 1) {
			layoutAnswer2
					.addView(
							tvAnswer,
							layoutParams);
		} else {
			// allAnswerWidth -= screenWidth;
			layoutAnswer
					.addView(
							tvAnswer,
							layoutParams);

		}
		return tvAnswer;
	}
	
	private boolean AnswerClick(View v,final LinearLayout layoutAnswer,final LinearLayout layoutAnswer2,final LinearLayout layoutAnswer3,final LinearLayout layoutAnswer4,final LinearLayout layoutOption,final LinearLayout layoutOption2,final LinearLayout layoutOption3,final LinearLayout layoutOption4){
		
		// 处理答案返回
		if (!"touched".equals(v.getTag())) {
			v.setTag("touched");
		} else {
			return false;
		}

		recalc = false;
		TextView evAnswer = (TextView) v;
		String answer = evAnswer.getText().toString();
		//Paint paint = evAnswer.getPaint();
		evAnswer.setVisibility(View.INVISIBLE);
		
		//取得所有列表值
		List<String> aryAnswer = new ArrayList<String>();									

		boolean found = false;
		for (int i = 0; i < layoutOption
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutOption
					.getChildAt(i);
			if (ev.getVisibility() == View.INVISIBLE && answer.equals(ev.getText().toString())) {
				ev.setVisibility(View.VISIBLE);
				ev.setTag("");
				//aryAnswer.add(ev.getText().toString());
				found = true;
				break;
			}
		}

        if(!found)
		for (int i = 0; i < layoutOption2
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutOption2
					.getChildAt(i);
			if (ev.getVisibility() == View.INVISIBLE && answer.equals(ev.getText().toString())) {
				ev.setVisibility(View.VISIBLE);
				ev.setTag("");
				found = true;
				break;
				//aryAnswer.add(ev.getText().toString());
			}
		}

        if(!found)
		for (int i = 0; i < layoutOption3
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutOption3
					.getChildAt(i);
			if (ev.getVisibility() == View.INVISIBLE && answer.equals(ev.getText().toString())) {
				ev.setVisibility(View.VISIBLE);
				ev.setTag("");	
				found=true;
				break;
				//aryAnswer.add(ev.getText().toString());
			}
		}
		//layoutAnswer3.removeAllViews();
        if(!found)
		for (int i = 0; i < layoutOption4
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutOption4
					.getChildAt(i);
			if (ev.getVisibility() == View.INVISIBLE && answer.equals(ev.getText().toString())) {
				ev.setVisibility(View.VISIBLE);
				ev.setTag("");
				found=true;
				break;
			}
		}
        

        for (int i = 0; i < layoutAnswer
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutAnswer
					.getChildAt(i);
			if (ev.getVisibility() == View.VISIBLE) {				
				aryAnswer.add(ev.getText().toString());
			}
		}
		layoutAnswer.removeAllViews();	
        for (int i = 0; i < layoutAnswer2
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutAnswer2
					.getChildAt(i);
			if (ev.getVisibility() == View.VISIBLE) {				
				aryAnswer.add(ev.getText().toString());
			}
		}
		layoutAnswer2.removeAllViews();	
        for (int i = 0; i < layoutAnswer3
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutAnswer3
					.getChildAt(i);
			if (ev.getVisibility() == View.VISIBLE) {				
				aryAnswer.add(ev.getText().toString());
			}
		}
		layoutAnswer3.removeAllViews();		
        for (int i = 0; i < layoutAnswer4
				.getChildCount(); i++) {
			TextView ev = (TextView) layoutAnswer4
					.getChildAt(i);
			if (ev.getVisibility() == View.VISIBLE) {				
				aryAnswer.add(ev.getText().toString());
			}
		}
		layoutAnswer4.removeAllViews();
		

		
		
		allAnswerWidth =0;
		posAnswer = 0;
		
		//加载答案
		for(String str:aryAnswer){
		    TextView tvAnswer =	AddAnswer(str,layoutAnswer,layoutAnswer2,layoutAnswer3,layoutAnswer4);
			tvAnswer.setOnTouchListener(new TextView.OnTouchListener() {
				@Override
				public boolean onTouch(View v,
						android.view.MotionEvent arg1) {									
					return AnswerClick(v,layoutAnswer,layoutAnswer2,layoutAnswer3,layoutAnswer4,layoutOption,layoutOption2,layoutOption3,layoutOption4);
				}
			});
		}
		return true;
	}
	
	public void doSubject(int subject_id) {
//		Cursor cursor = dbFavoriteHelper
//				.select(subject_id);		
		this.curSubjects = dbSubjectHelper.getSubjectsBySubjectId(subject_id);
		subjectIndex=0;
		ChangeSubject(0,true,true);
		
	}
	
	
	public Subject getSubject(int subject_id) {
	
		this.curSubjects = dbSubjectHelper.getSubjectsBySubjectId(subject_id);
		if(curSubjects.size()>0)
			return curSubjects.get(0);
		else
			return null;		
	}
	
	public void playMp3(Subject model){
		final AssetManager am = getResources().getAssets();		
		final String mp3 = model.getMp3().substring(
				model.getMp3().lastIndexOf("/") + 1);
		mMPlayer.StopVoice();
		//mMPlayer.playFaviteVoice(am, mp3);
		mMPlayer.playFavoriteVoice(ClassManager.getAudioPath(mp3));
	}

	private int nextSubjectIndex ;
    Subject model ;
	private Button autoPlay;
	private static boolean playaudiofinished = true;
	// 切换题目
	private boolean ChangeSubject(int index,final boolean favorite,boolean isNew) {
		nextSubjectIndex  = subjectIndex;
		if(isNew){
			
			subjectIndex++;
			nextSubjectIndex = subjectIndex ;// index + 1;
			
			if(index==curSubjects.size()){
				ChangeResult();
				return false;
			}
			
			if(index>=curSubjects.size()) return false;
			curSubject= curSubjects.get(index);
			
			model = curSubjects.get(index);
			
			
		}
		
	
	
	
		lin.removeAllViews();

		LinearLayout layoutTop = (LinearLayout) inflater.inflate(
				R.layout.subjecttop, null).findViewById(R.id.lltop);
		lin.addView(layoutTop);

		TextView tvTopClassname = (TextView) layoutTop
				.findViewById(R.id.tvTopClassname);
		tvTopClassname.setText(this.classname);

		
		LinearLayout layoutSubject = (LinearLayout) inflater.inflate(
				R.layout.subject, null).findViewById(R.id.llSubject);
		
		
		tvNext = (Button) layoutSubject.findViewById(R.id.tvNext);
		tvNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RankManager.getInstance().setValid(false);			
				boolean ret = ChangeSubject(subjectIndex,false,true);
				if(nextSubjectIndex>curSubjects.size()){
					if(tvNext!=null){
						tvNext.setVisibility(View.INVISIBLE);
					}			
				}else{
					if(tvNext!=null){
						tvNext.setVisibility(View.VISIBLE);
					}
				}						
				if (!ret)
					return;
			    } 
		});
		
		
/*		if(tvPre!=null){
			if(nextSubjectIndex>1)
				tvPre.setVisibility(View.VISIBLE);						
		}*/
		//System.out.println("nextSubjectIndex"+nextSubjectIndex+",curSubjects.size()-1"+(curSubjects.size()-1));
		Button btnReturn = (Button) layoutTop.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curSubject.is_select==3){
					 if(btnRecordAudio.getText().toString().equals("停止")){					
							mRecorder.stopRecording();
							btnRecordAudio.setText(R.string.record);								
							stopRecord();
					 }
				}
				
				if (timer != null)
					timer.cancel();
				handler.postDelayed(runui, 0);
				showToolbar(View.VISIBLE);

				
			}
		});	
			

		autoPlay = (Button) layoutTop.findViewById(R.id.autoplay);
		autoPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				   ClickNum = ClickNum +1;
				if(ClickNum%2==1){
					autoPlay.setText("停止");
					handler.postDelayed(runnableauto, 1000);
				} else {
					handler.removeCallbacks(runnableauto);
					autoPlay.setText("自动播放");
				}
			}
		});	

       
		tvQuestion = (TextView) layoutSubject
				.findViewById(R.id.tvQuestion);
		tvQuestion.setText(model.getQuestion());
		Log.d("deweitest","答案是"+model.getQuestion());
		
		if(isNew){
			tvQuestion.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.in_rightleft));
		}

		
		try{
			tvQuestion.setTextSize(Integer.parseInt(subjectFontSize));
		}catch(Exception e){};

		
		
		ImageView ivPic = null;

		// LinearLayout llOption = (LinearLayout) layout.getChildAt(1);
		// TextView tvOptionTmpl = (TextView) layout.getChildAt(1);
		final AssetManager am = getResources().getAssets();
		if (model.getPic() != null && !"".equals(model.getPic())) {
			ivPic = new ImageView(this); // (ImageView) layout.getChildAt(1);
			//ivPic.setMaxHeight(400);
			try{
				ivPic.setMaxHeight(Integer.parseInt(picHeight));
			}catch(Exception e){}
			
			ivPic.setMaxWidth(720);
			ivPic.setAdjustViewBounds(true);
			ivPic.setPadding(2, 2, 2, 2);
			String pic = model.getPic().substring(
					model.getPic().lastIndexOf("/") + 1);
			Bitmap bm = ClassManager.getImageFromFile(pic);  //Utils.getImageFromAssetsFile(am, pic);
			if (bm != null) {
				ivPic.setImageBitmap(bm);
				ivPic.setVisibility(View.VISIBLE);
				
				if(isNew)
					ivPic.setAnimation(AnimationUtils.loadAnimation(this,
							R.anim.in_rightleft));
			}
		} 
		
		

		//final LinearLayout 
		layoutOption = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);

	    layoutOption2 = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);

		layoutOption3 = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);

		layoutOption4 = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);

		final LinearLayout layoutAnswer = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);		
		//layoutAnswer.setMinimumHeight(Integer.parseInt(answerFontSize)*2);
		layoutAnswer.setMinimumHeight(0);

		final LinearLayout layoutAnswer2 = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);
		//layoutAnswer2.setMinimumHeight(Integer.parseInt(answerFontSize)*2);
		layoutAnswer2.setMinimumHeight(0);

		final LinearLayout layoutAnswer3 = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);
		//layoutAnswer3.setMinimumHeight(Integer.parseInt(answerFontSize)*2);
		layoutAnswer3.setMinimumHeight(0);

		final LinearLayout layoutAnswer4 = (LinearLayout) inflater.inflate(
				R.layout.multioption, null).findViewById(R.id.llOption);
		//layoutAnswer4.setMinimumHeight(Integer.parseInt(answerFontSize)*2);
		layoutAnswer4.setMinimumHeight(0);

		LinearLayout layoutSingleOption = (LinearLayout) inflater.inflate(
				R.layout.singleoption, null).findViewById(R.id.llSingleOption);

		LinearLayout layoutImageOption = (LinearLayout) inflater.inflate(
				R.layout.imageoption, null).findViewById(R.id.llImage);

		LinearLayout layoutImageOption2 = (LinearLayout) inflater.inflate(
				R.layout.imageoption, null).findViewById(R.id.llImage);

		if (ivPic != null)
			lin.addView(ivPic);
		lin.addView(layoutSubject);
		// if(model.is_select==0){
		
		btnRecordAudio =  (Button) layoutSubject.findViewById(R.id.btnRecord);
		// }else{

		// }
		
		
		LinearLayout layoutShowAnswer = (LinearLayout) inflater.inflate(
				R.layout.showanswer, null).findViewById(R.id.llShowAnswer);
		

		tvShowAnswer = (Button) layoutShowAnswer
				.findViewById(R.id.tvShowAnswer);
		final TextView tvAnswer = (TextView) layoutShowAnswer
				.findViewById(R.id.tvAnswer);
		final ImageView ivAnswer = (ImageView) layoutShowAnswer
				.findViewById(R.id.ivAnswer);
		

		Button btnPlay =  (Button) layoutSubject.findViewById(R.id.btnPlay);
		
		btnShowQuestion =  (Button) layoutSubject.findViewById(R.id.btnShowQuestion);
		btnShowQuestion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnShowQuestion.setVisibility(View.GONE);
				tvQuestion.setVisibility(View.VISIBLE);
			
				
			}
		});
		
		
		
		
		{
			// 处理多选  语音录入题
			allWidth = 0;
			allAnswerWidth = 0;
			
			//录音题目
			if(model.is_select == 3){
				isNeedRecordAudio = true;
				//btnPlay.setVisibility(View.VISIBLE);
				btnRecordAudio.setVisibility(View.VISIBLE);
			}
			else{ //if(model.is_select == 2){
				isNeedRecordAudio = false;
				btnRecordAudio.setVisibility(View.GONE);
				//
			}
			btnPlay.setVisibility(View.GONE);
			
			btnShowQuestion.setVisibility(View.GONE);
			tvQuestion.setVisibility(View.VISIBLE);
			//如果是听力题目，不要显示答案
			if(model.is_select==1   || model.is_select==2 || model.is_select==4) {
				layoutOption4.setVisibility(View.INVISIBLE);
				layoutOption3.setVisibility(View.INVISIBLE);
				layoutOption2.setVisibility(View.INVISIBLE);
				layoutOption.setVisibility(View.INVISIBLE);
				if(model.is_select==1){
					btnShowQuestion.setVisibility(View.VISIBLE);
					tvQuestion.setVisibility(View.GONE);
				}
				
				if(model.is_select==4)
					tvShowAnswer.setVisibility(View.GONE);
			}
			else// if(model.is_select==2)
			{
				layoutOption4.setVisibility(View.INVISIBLE);
				layoutOption3.setVisibility(View.INVISIBLE);
				layoutOption2.setVisibility(View.INVISIBLE);
				layoutOption.setVisibility(View.INVISIBLE);
				
				//layoutSubject.removeView(btnRecordAudio);
						
				//layoutOption.addView(btnRecordAudio);
				//layoutOption.setMinimumHeight(150);	
				//btnRecordAudio.setTextSize(40);
				//btnRecordAudio.setLayoutParams(new LayoutParams(100,LinearLayout.LayoutParams.WRAP_CONTENT));
				tvShowAnswer.setVisibility(View.GONE);
				
				if(model.getMp3().length()==0){
					startRecord();			
				}
				//return false;
			}
			
			if(model.is_select==2){
			     layoutOption4.setVisibility(View.VISIBLE);
			      layoutOption3.setVisibility(View.VISIBLE);
			      layoutOption2.setVisibility(View.VISIBLE);
			       layoutOption.setVisibility(View.VISIBLE);
			}

			// 宽度计算数组
			int[] widths = new int[4];
			for (int i = 0; i < widths.length; i++) {
				widths[i] = 0;
			}
			int pos = 0;

			int[] widthsAnswer = new int[4];
			for (int i = 0; i < widthsAnswer.length; i++) {
				widthsAnswer[i] = 0;
			}
			posAnswer = 0;
			recalc = false;

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenWidth = dm.widthPixels;

			
		
			
			if(isNew){
				String[] ary = model.answer.split("\\|");
				curSubjectArray = Utils.getArray(ary);
				curSubjectRealArray.clear();
			}			
			
			
			List<String> lstSubject = Utils.addToArray(curSubjectArray, curSubjectRealArray,2);   //Utils.changeArray(ary);
			curSubjectRealArray.addAll(lstSubject);
			
			layoutOption.removeAllViews();
			
			List<String> subjects = Utils.changeArray(lstSubject);		
			
			final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					screenWidth/2 , LinearLayout.LayoutParams.WRAP_CONTENT);//screenWidth/2   LinearLayout.LayoutParams.WRAP_CONTENT 
			layoutParams.setMargins(0, 0, 0, 0);
			layoutParams.gravity =  Gravity.CENTER_HORIZONTAL;

			//if(model.is_select!=2)
			for (String strOption : subjects) {

				final TextView tv = Utils.CreateOptionView(true, this, strOption,Integer.parseInt(answerFontSize));
				layoutOption.addView(tv,layoutParams);
				
				tv.setGravity(Gravity.CENTER);
                //点击答案
				tv.setOnTouchListener(new TextView.OnTouchListener() {
					@Override
					public boolean onTouch(View v, android.view.MotionEvent arg1) {
						// TODO Auto-generated method stub
						try {
							

							
							//如果声音还未播放完成，点击答案不做处理
							if(!MusicPlayer.mIsPlayFinished) return false;
							
							
							// 处理点击答案
							TextView ev = (TextView) v;
							TextView tvAnswer = Utils.CreateOptionView(true,
									instance, ev.getText().toString(),Integer.parseInt(answerFontSize));
							//点击正常答案后，才允许点击
							boolean isRight = Utils.checkIsRight(true,
									layoutAnswer, layoutAnswer2, layoutAnswer3,
									layoutAnswer4, curSubjectRealArray , tv.getText().toString() );
							
							//if(!isRight) return false;
							
							
							
							if (!"touched".equals(v.getTag())) {
								v.setTag("touched");
							} else {
								return false;
							}
							
							if(!isRight){
								mMPlayer.playWrongVoice();
								curSubjectRealArray = new ArrayList<String>();
								ChangeSubject(subjectIndex,false,false);
								return false;
								
							}
							
							Paint paint = ev.getPaint();
							float textWidth = paint.measureText(ev.getText()
									.toString());
							float splitWidth = paint.measureText(" ") * 2;
							
							

							if (recalc) {
								int width = 0;
								if (layoutAnswer4.getChildCount() > 0) {
									posAnswer = 3;
									for (int i = 0; i < layoutAnswer4
											.getChildCount(); i++) {
										TextView tv = (TextView) layoutAnswer4
												.getChildAt(i);
										Paint pai = tv.getPaint();
										width += pai.measureText(tv.getText()
												.toString()) + splitWidth;
									}
								} else if (layoutAnswer3.getChildCount() > 0) {
									posAnswer = 2;
									for (int i = 0; i < layoutAnswer3
											.getChildCount(); i++) {
										TextView tv = (TextView) layoutAnswer3
												.getChildAt(i);
										Paint pai = tv.getPaint();
										width += pai.measureText(tv.getText()
												.toString()) + splitWidth;
									}
								} else if (layoutAnswer2.getChildCount() > 0) {
									posAnswer = 1;
									for (int i = 0; i < layoutAnswer2
											.getChildCount(); i++) {
										TextView tv = (TextView) layoutAnswer2
												.getChildAt(i);
										Paint pai = tv.getPaint();
										width += pai.measureText(tv.getText()
												.toString()) + splitWidth;
									}
								} else {
									posAnswer = 0;
									for (int i = 0; i < layoutAnswer
											.getChildCount(); i++) {
										TextView tv = (TextView) layoutAnswer
												.getChildAt(i);
										Paint pai = tv.getPaint();
										width += pai.measureText(tv.getText()
												.toString()) + splitWidth;
									}
								}

								recalc = false;

								allAnswerWidth = width + textWidth + splitWidth;
							} else {
								allAnswerWidth += textWidth + splitWidth;
							}
							
							if (allAnswerWidth > screenWidth) {
								posAnswer++;
								allAnswerWidth = textWidth + splitWidth;
							}
							
					

							if (posAnswer >= 3) {
								layoutAnswer4
										.addView(
												tvAnswer,
												layoutParams);
							} else if (posAnswer == 2) {
								layoutAnswer3
										.addView(
												tvAnswer,
												layoutParams);
							} else if (posAnswer == 1) {
								layoutAnswer2
										.addView(
												tvAnswer,
												layoutParams);
							} else {
								
								layoutAnswer
										.addView(
												tvAnswer,
												layoutParams);
								
							}

							v.setVisibility(View.INVISIBLE);
							
							//PK题目，隐藏点击的答案
							if(curSubject.is_select==4){
								tvAnswer.setVisibility(View.INVISIBLE);
							}
							
							/*boolean finish = Utils.checkFinish(true, layoutOption,
									layoutOption2, layoutOption3, layoutOption4);*/
							boolean finish = ( curSubjectArray.size() == curSubjectRealArray.size() ); 
							if (finish) {
								// allAnswerWidth = 0;
								// allWidth = 0;
//								boolean success = Utils.checkAnswer(true,
//										layoutAnswer, layoutAnswer2, layoutAnswer3,
//										layoutAnswer4, model);
								boolean success = true;
								if (success) {
									mMPlayer.playRightVoice();
									System.out.println("start MoveNext");
									MoveNext(nextSubjectIndex,favorite);
									System.out.println("end MoveNext");
									//boolean ret = MoveNext(nextSubjectIndex);
									//if (!ret)
									//	return false;
								} else {
									mMPlayer.playWrongVoice();
								}
							}
							else{
								ChangeSubject(subjectIndex,false,false);
							}

							tvAnswer.setOnTouchListener(new TextView.OnTouchListener() {
								@Override
								public boolean onTouch(View v,
										android.view.MotionEvent arg1) {									
									return AnswerClick(v,layoutAnswer,layoutAnswer2,layoutAnswer3,layoutAnswer4,layoutOption,layoutOption2,layoutOption3,layoutOption4);
								}
							});
							
							

						} catch (Exception e) {
							System.out.println(e.toString());
						}
						
						return true;

					}
				});
			
				

				Paint paint = tv.getPaint();
				float textWidth = paint.measureText(strOption);
				float splitWidth = paint.measureText(" ") * 2;
				allWidth += textWidth + splitWidth;
				if (allWidth > screenWidth) {
					pos++;
					allWidth = textWidth + splitWidth;
				}
				if (pos >= 3) {
					// allWidth -=screenWidth;
					layoutOption.removeView(tv);
					tv.setVisibility(View.VISIBLE);
					layoutOption4.addView(tv,layoutParams);

				} else if (pos == 2) {
					// allWidth -=screenWidth;
					layoutOption.removeView(tv);
					tv.setVisibility(View.VISIBLE);
					layoutOption3.addView(tv, layoutParams);

				} else if (pos == 1) {
					// allWidth -=screenWidth;
					layoutOption.removeView(tv);
					tv.setVisibility(View.VISIBLE);
					layoutOption2.addView(tv, layoutParams);

				}
			}
			
			
			if(!isNeedRecordAudio){
				lin.addView(layoutAnswer);
				if (layoutOption2.getChildCount() > 0)
					lin.addView(layoutAnswer2);
				if (layoutOption3.getChildCount() > 0)
					lin.addView(layoutAnswer3);
				if (layoutOption4.getChildCount() > 0)
					lin.addView(layoutAnswer4);
	
				//layoutOption.setAnimation(AnimationUtils.loadAnimation(this,
				//		R.anim.in_rightleft));
				lin.addView(layoutOption);
				if (layoutOption2.getChildCount() > 0)
					lin.addView(layoutOption2);
				if (layoutOption3.getChildCount() > 0)
					lin.addView(layoutOption3);
				if (layoutOption4.getChildCount() > 0)
					lin.addView(layoutOption4);
			}
			else{
				//layoutOption.setAnimation(AnimationUtils.loadAnimation(this,
				//		R.anim.in_rightleft));
				lin.addView(layoutOption);
			}
			
			//调整显示高度
			//System.out.println("layoutOption.getHeight()="+layoutOption.getHeight()+",layoutAnswer.getHeight()="+layoutAnswer.getHeight()+",lin.getHeight()="+lin.getHeight());
			//layoutAnswer.layout(layoutAnswer.getLeft(), layoutAnswer.getTop(),layoutAnswer.getRight(), layoutAnswer.getTop()+layoutOption.getHeight());
			//layoutAnswer2.layout(layoutAnswer2.getLeft(), layoutAnswer2.getTop(),layoutAnswer2.getRight(), layoutAnswer2.getTop()+layoutOption2.getHeight());

		}
		//显示答案
		lin.addView(layoutShowAnswer);

		if (timer == null) {
			final Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						totalSeconds++;
						//TextView tvTopSeconds = (TextView) findViewById(R.id.tvTopSeconds);
						//tvTopSeconds.setText(String.valueOf(totalSeconds));					

						
						break;
					}
					super.handleMessage(msg);
				}
			};

			TimerTask task = new TimerTask() {
				public void run() {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
			};

			totalSeconds = 0;
			timer = new Timer(true);
			timer.schedule(task, 1000, 1000); // 延时1000ms后执行，1000ms执行一次

		}

		
		tvShowAnswer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button tv = (Button) v;
				if ("显示答案:".equals(tv.getText())) {
					tv.setText(R.string.hideanswer);
					if (model.is_select == 0) {
						String answer = model.getYes_answer().substring(
								model.getYes_answer().lastIndexOf("/") + 1);
						if (answer.endsWith(".jpg") || answer.endsWith(".png")
								|| answer.endsWith(".gif")) {
							// ImageView iv = Utils.CreateImageView(MyGameActivity.this,
							// getResources().getAssets(), answer);
							Bitmap bm = ClassManager.getImageFromFile(answer) ;// Utils.getImageFromAssetsFile(am, answer);
							ivAnswer.setImageBitmap(bm);
							ivAnswer.setVisibility(View.VISIBLE);
							tvAnswer.setVisibility(View.INVISIBLE);
						} else {
							tvAnswer.setText(answer);
							tvAnswer.setVisibility(View.VISIBLE);
							ivAnswer.setVisibility(View.INVISIBLE);
						}
					} else {
						String strAnswer="";
						String[] aryAnswer = model.answer.split("\\|");
						for(int i=0;i<aryAnswer.length;i+=2){
							if(strAnswer.length() >0) strAnswer+=" ";
							
							strAnswer+=aryAnswer[i];							
						}
						tvAnswer.setText(strAnswer);//model.answer.replace("|", " ")
						tvAnswer.setVisibility(View.VISIBLE);
						ivAnswer.setVisibility(View.INVISIBLE);
					}
				} else {
					tv.setText(R.string.showanswer);
					tvAnswer.setVisibility(View.INVISIBLE);
					ivAnswer.setVisibility(View.INVISIBLE);
				}
				tvAnswer.setTextSize(Integer.parseInt(subjectFontSize));
			}
		});
       
		ImageView ivLaba = (ImageView) layoutSubject.findViewById(R.id.ivLaba);
		if (model.getMp3() != null && !"".equals(model.getMp3())) {
			final String mp3 = model.getMp3().substring(
					model.getMp3().lastIndexOf("/") + 1);
			
			if(isNew)
				if(ClassManager.getAudioPath(mp3)!=null) {
					Log.d("dewei222","The path is " + ClassManager.getAudioPath(mp3));
					File tmpfile =new File(ClassManager.getAudioPath(mp3));
					if (tmpfile.exists()) {
						Log.d("dewei22","play this music"+mp3);
				        mMPlayer.playVoice(ClassManager.getAudioPath(mp3));
					} else {
						Toast.makeText(this, "没找到音频资源", Toast.LENGTH_SHORT);
					}
				}
				//mMPlayer.playVoice(am, mp3);

			ivLaba.setOnTouchListener(new ImageView.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, android.view.MotionEvent arg1) {
					// TODO Auto-generated method stub
					if(curSubject!=null && curSubject.is_select!=4){
						mMPlayer.StopVoice();
						//mMPlayer.playVoice(am, mp3);
						mMPlayer.playVoice(ClassManager.getAudioPath(mp3));
					}
					return false;
				}

			});
			ivLaba.setVisibility(View.VISIBLE);

		} else {
			ivLaba.setVisibility(View.GONE);
			//MP3为空时，如果是录音提，自动开始录音
			//stopRecord();
			
		}
		
		
		final Button tvAddFavorite = (Button) layoutShowAnswer
				.findViewById(R.id.tvAddFavorite);
		
		tvAddFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Button tv = (Button) v;
				
				if(curSubject !=null ){
				//if(curSubjects.size()>subjectIndex){
					//Subject subject = curSubjects.get(subjectIndex);
					Subject subject = curSubject;
					
					Cursor cursor = dbFavoriteHelper.select(subject.id);
					if (cursor.moveToNext()) {
						new AlertDialog.Builder(MyGameActivity.this)
	    				.setTitle("提示")
	    				.setIcon(android.R.drawable.ic_dialog_info)
	    				.setMessage("该题已加入收藏。")
	    				.setPositiveButton("确定",
	    						new DialogInterface.OnClickListener() {

	    							@Override
	    							public void onClick(
	    									DialogInterface dialog,
	    									int which) {								
	    							}

	    						}).show();
					} else {
						dbFavoriteHelper.insert(classname,
								subject.sort_id,subject.question,subject.id);
						/*Drawable drawable =
								 getResources().getDrawable(R.drawable.);*/
						tvAddFavorite.setBackgroundColor(Color.RED);
						
					/*	new AlertDialog.Builder(MyGameActivity.this)
	    				.setTitle("提示")
	    				.setIcon(android.R.drawable.ic_dialog_info)
	    				.setMessage("加入收藏成功。")
	    				.setPositiveButton("确定",
	    						new DialogInterface.OnClickListener() {

	    							@Override
	    							public void onClick(
	    									DialogInterface dialog,
	    									int which) {								
	    							}

	    						}).show();*/
					}
				}
				
				
			}
		});
		
	

		if(favorite){
//			tvPre.setVisibility(View.INVISIBLE);
			tvNext.setVisibility(View.INVISIBLE);
			btnReturn.setVisibility(View.INVISIBLE);
			tvAddFavorite.setVisibility(View.INVISIBLE);
		}
		else{
//			tvPre.setVisibility(View.VISIBLE);
			tvNext.setVisibility(View.VISIBLE);
			btnReturn.setVisibility(View.VISIBLE);
			tvAddFavorite.setVisibility(View.VISIBLE);
		}

		
	     if(!isNew){
	    	 afterPlayAudio();
	     }
		
		
		btnRecordAudio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Button btnRecorder  =(Button)v;
			
				if(btnRecordAudio.getText().toString().equals("录音")){
					//System.out.println("btnRecord.getText()="+btnRecordAudio.getText());
					btnRecordAudio.setText(R.string.stoprecord);
					mRecorder.startRecording(subjectIndex);
					
//					showIatDialog();
				}else if(btnRecordAudio.getText().toString().equals("停止")){					
					mRecorder.stopRecording();
					btnRecordAudio.setText(R.string.record);
				
					//btnRecorder.setVisibility(View.INVISIBLE);
					btnRecordAudio.setVisibility(View.VISIBLE);
					stopRecord();
					
					if(isNeedRecordAudio){						
						MoveNext(subjectIndex,false);
					}
					
				}	
		
			}

		});	
		
		
		
		btnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Button btnPlay  =(Button)v;	
				
				if(mRecorder.getFilename(subjectIndex)!=null){
					String recordFilename = mRecorder.getFilename(subjectIndex).replace(".raw", ".mp3");
					mMPlayer.playVoice(recordFilename);
				}
				
				//btnRecord.setText(R.string.record);
				//btnRecord.setVisibility(View.VISIBLE);				
							
			}

		});	
		

		return true;
	}
	
	private void ChangeResult(){
		lin.removeAllViews();
		LinearLayout layoutResult = (LinearLayout) inflater.inflate(
				R.layout.result, null).findViewById(R.id.layResult);
		lin.addView(layoutResult);
		btnPlayAllRecord = (Button)layoutResult.findViewById(R.id.btnPlayAllRecord);
		btnPlayAllRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				playAllRecord();
			}
		});	
		
		btnPlayAllSource = (Button)layoutResult.findViewById(R.id.btnPlayAllSource);
		btnPlayAllSource.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				playAllMp3();
			}
		});	
		
		btnNextStep = (Button)layoutResult.findViewById(R.id.btnNextStep);
		btnNextStep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				
				if (timer != null){
					timer.cancel();
					timer = null;
				}
				
				loadSubjects(nextclassname);
				
			}
		});	
		
		btnScore = (Button)layoutResult.findViewById(R.id.btnScore);
		btnScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				
				List<String> lst = new ArrayList<String>();
				for(int i=0;i<=curSubjects.size()-1;i++){
					//Subject subject = curSubjects.get(i);
					String filename = mRecorder.getFilename(i+1).replace(".raw", ".mp3");
					lst.add(filename);
				}
			
				if (lst.size()>0) {
					HttpMultipartPost post = new HttpMultipartPost(mContext, lst,RankManager.getInstance().getUser_name(),classname,subclassname,classid, curSubject.sort_id);
					post.execute();
				} else {
					Toast.makeText(mContext, "录音文件不存在", Toast.LENGTH_LONG).show();
				}
				
				
//				new AlertDialog.Builder(MyGameActivity.this)
//				.setTitle("提示")
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setMessage("您的得分为90分。")
//				.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(
//									DialogInterface dialog,
//									int which) {								
//							}
//
//						}).show();
			}
		});	
		
		btnViewRanking = (Button)layoutResult.findViewById(R.id.btnViewRanking);
		btnViewRanking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				if(!RankManager.getInstance().isValid()){
					new AlertDialog.Builder(MyGameActivity.this)
    				.setTitle("提示")
    				.setIcon(android.R.drawable.ic_dialog_info)
    				.setMessage("点击下一题后无法查看排名。")
    				.setPositiveButton("确定",
    						new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(
    									DialogInterface dialog,
    									int which) {								
    							}

    						}).show();
					return;					
				}
				
				udpateRankInThread();
				//_handler.sendEmptyMessage(MSG_SHOWUPDATE);
				
			}
		});	
		
		//重做一次
		btnRedo = (Button)layoutResult.findViewById(R.id.btnRedo);
		btnRedo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				
				if (timer != null){
					timer.cancel();
					timer = null;
				}
				
				loadSubjects(classname);
			}
		});	
		
	   Button btnResultReturn = (Button)layoutResult.findViewById(R.id.btnResultReturn);
	   btnResultReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				MyGameActivity.this.loadThirdClass(MyGameActivity.this.subclassname);
			}
		});	
	   
	   tvCurRank =  (TextView)layoutResult.findViewById(R.id.tvCurRank);
	   llScore =  (LinearLayout)layoutResult.findViewById(R.id.llScore);
	   lvScore =  (ListView)layoutResult.findViewById(R.id.lvScore);	   
	   
	   
	   //udpateRankInThread();
	   int nFast = totalSeconds;
	   Cursor cursor = dbFastHelper.select(RankManager.getInstance().getSort_id());
		if (cursor.moveToNext()) {
			int fast = cursor.getInt(2);
			if(fast<nFast)
				nFast =fast ;	
			else{
				dbFastHelper.update(RankManager.getInstance().getSort_id(), nFast);
			}
		} 
		else{
			dbFastHelper.insert(RankManager.getInstance().getSort_id(),totalSeconds);
		}   
	   
	
		/*
	   final Builder builder = new AlertDialog.Builder(this)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage(RankManager.getInstance().isValid() ? "用时 "+totalSeconds +" 秒，" + "最快 "+ nFast  +" 秒" :"点击下一题后 无法记录时间 。" )
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {								
					}

				});
		builder.show();
		*/
		
		if (Utils.getNetworkStatus(mContext)) {	
			if(!RankManager.getInstance().isValid()){
				new AlertDialog.Builder(MyGameActivity.this)
				.setTitle("提示")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("点击下一题后无法查看排名。")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {								
							}

						}).show();
				return;					
			}
			
			udpateRankInThread();
		}
		else{
			tvCurRank.setText("用时:" + totalSeconds + "秒");
		}
	}
	
    public void udpateRankInThread(){
		this.progress.setTitle("提示");
		this.progress.setMessage("正在查询排名...");
    	progress.show();
    	Runnable runnable = new Runnable() {
			@Override
			public void run() {
				udpateRank();
				progress.dismiss();
			}
    	};
    	new Thread(runnable).start();
    	
    }
    public void udpateRank(){
    	if (Utils.getNetworkStatus(mContext)) {	
    		
    		Looper.prepare();
    		String checkcode = Utils.MD5(RankManager.getInstance().getUser_name()+RankManager.getInstance().getSort_id()+RankManager.getInstance().getSeconds()+IConst.InterfaceKey);
    		String rankUrl = RankManager.getInstance().getRankUrl(RankManager.getInstance().getUser_name(), 
    				RankManager.getInstance().getSort_id(), 
    				RankManager.getInstance().getSeconds(),
    				 checkcode);
    		System.out.println("rankUrl="+rankUrl);
    		rank = RankManager.getInstance().getRank(rankUrl);
    		if(rank!=null){   			
    			
    			/*if(rank.getRank()>0){    
    				
    				
    				final Builder builder = new AlertDialog.Builder(this)
    				.setTitle("提示")
    				.setIcon(android.R.drawable.ic_dialog_info)
    				.setMessage((rank.getRank()==1) ? "新的最快纪录 "+totalSeconds +"秒" : "最快用时 "+ (rank.getScoreItems().size()>0 ? rank.getScoreItems().get(0).getScore() :"")   +" 秒,您用时 "+totalSeconds+" 秒")
    				.setPositiveButton("确定",
    						new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(
    									DialogInterface dialog,
    									int which) {								
    							}

    						});
    				builder.show();
    			}
    			else*/
    			if(rank.getError().length()>0){
    				final Builder builder = new AlertDialog.Builder(this)
    				.setTitle("提示")
    				.setIcon(android.R.drawable.ic_dialog_info)
    				.setMessage("发生错误:"+rank.getError())
    				.setPositiveButton("确定",
    						new DialogInterface.OnClickListener() {

    							@Override
    							public void onClick(
    									DialogInterface dialog,
    									int which) {								
    							}

    						});
    				builder.show();
    			}
    			
    			
    			MyGameActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						lvScore.setAdapter(new ScoreAdapter(
								MyGameActivity.this, rank.getScoreItems()
								));
						
						_handler.sendEmptyMessage(MSG_HIDEPROCESS);
						_handler.sendEmptyMessage(MSG_SHOWUPDATE);
					}
				});
    		
    			
    		}
    		
    		Looper.loop();
    		
    	}else {
			_handler.sendEmptyMessage(MSG_NETWORKERROR);
		}    
    }
    
    @Override
    protected void showUpdate(){
    	if(rank!=null && rank.getRank()>0){
    		tvCurRank.setText("您的排名:"+rank.getRank()+",用时:" + totalSeconds + "秒");
    	}
    	llScore.setVisibility(View.VISIBLE);
		lvScore.setVisibility(View.VISIBLE);
	}
	
	private void playAllRecord(){		
		List<String> lst = new ArrayList<String>();
		for(int i=0;i<=this.curSubjects.size()-1;i++){
			Subject subject = this.curSubjects.get(i);
			String filename = mRecorder.getFilename(i+1).replace(".raw", ".mp3");	
			lst.add(filename);
		}
		mMPlayer.playVoiceList(lst,0);
	}
	
	private void playAllMp3(){		
		final AssetManager am = getResources().getAssets();
		
		List<String> lst = new ArrayList<String>();
		for(int i=0;i<=this.curSubjects.size()-1;i++){
			Subject model = this.curSubjects.get(i);
			String filename = model.getMp3();
			if (model.getMp3() != null && !"".equals(model.getMp3())) {
				filename = filename.substring(filename.lastIndexOf("/") + 1);			
				lst.add(filename);
			}
		}
		mMPlayer.playVoiceList(am,lst,0);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// 题目分类
		menu.add(0, Class_ID, 0, R.string.subjectclass).setShortcut('3', 'a')
				.setIcon(R.drawable.subjectclass);

		/*
		 * Intent intent = new Intent(null, getIntent().getData());
		 * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		 * menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new
		 * ComponentName(this, MyGameActivity.class), null, intent, 0, null);
		 */
		// 退出程序
		menu.add(0, EXIT_ID, 0, R.string.exit).setShortcut('4', 'd')
				.setIcon(R.drawable.exit);
		return true;
	}

	// 处理菜单操作
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Class_ID:
			// 题目分类
			// startActivity(new Intent(Intent.ACTION_INSERT, getIntent()
			// .getData()));
			if (timer != null)
				timer.cancel();
			// loadClass(inflater, lin, listItemAdapter,1);
			showToolbar(View.VISIBLE);
			btnClass.performClick();
			return true;
		case EXIT_ID:
			// 退出程序
			if (timer != null)
				timer.cancel();
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void freeResources() {
		mMPlayer.free();
		// instance.freeResources();
	}

	public void onStop() {
		
		if(mToast!=null)
			mToast.cancel();

		if (null != iatDialog) {
			iatDialog.cancel();
		}
		
		super.onStop();
		//freeResources();
		if (timer != null)
			timer.cancel();
	}

	
	public void afterPlayAudio(){
		playaudiofinished = true;
		if(curSubject.is_select == 1 || curSubject.is_select == 4){
			layoutOption4.setVisibility(View.VISIBLE);
			layoutOption3.setVisibility(View.VISIBLE);
			layoutOption2.setVisibility(View.VISIBLE);
			layoutOption.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void startRecord(){
		if(isNeedRecordAudio){		
			
			btnRecordAudio.setText(R.string.stoprecord);
			//mRecorder.stopRecording();
			mRecorder.startRecording(subjectIndex);
			
			//显示语音听写Dialog.
			//showIatDialog();
		}
	}
	
	public void stopRecord(){
		if(isNeedRecordAudio){
			mRecorder.stopRecording();
			
			btnRecordAudio.setText(R.string.record);
			btnRecordAudio.setVisibility(View.VISIBLE);
		}
	}
	
	
	
	
	

	
	private void showTip(String str)
	{
		if(!TextUtils.isEmpty(str))
		{
			mToast.setText(str);
			mToast.show();
		}
	}
	
	
	
    /** 
     * 当Activity执行onPause()时让WebView执行pause 
     */  
    @Override  
    protected void onPause() {  
        super.onPause();  
        try {  
//            if (mWebView != null) {  
//                mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);  
//                isOnPause = true;  
//            }  
        	handler.removeCallbacks(runnableauto);
        	handler.removeCallbacks(runnableCheck);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * 当Activity执行onResume()时让WebView执行resume 
     */  
    @Override  
    protected void onResume() {  
        super.onResume();  

        try {  
        	handler.postDelayed(runnableCheck, 30000);
//            if (isOnPause) {  
//                if (mWebView != null) {  
//                    mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);  
//                }  
//                isOnPause = false;  
//            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	
	
}
