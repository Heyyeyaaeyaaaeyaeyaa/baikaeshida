package com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp.R;

public class MainActivity extends Activity {
	private Button buttonStart, buttonObjSite, buttonAbout, buttonExit;
	private Singleton singleton = Singleton.getSharedInstance();
	private final String DEFAULT_PATH = "/storage/emulated/0/Pictures/MyCameraApp";
	private DBHelper dirDBHelper;
	public final static String BORADCAST_ACTION_EXIT = "com.wangzhj.exit";//關閉活動廣播action名稱 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
        filter.addAction(BORADCAST_ACTION_EXIT);//爲BroadcastReceiver�?���?個action?�即要監聽�?���?名�?  
        registerReceiver(mBoradcastReceiver,filter); //動�?註冊監聽  靜�?�?�� 在AndroidManifest.xml中定義
		
		setContentView(R.layout.activity_main);

		dirDBHelper = new DBHelper(this);
		loadSQLiteData();
		
		buttonStart = (Button)this.findViewById(R.id.main_button_start);
		buttonObjSite = (Button)this.findViewById(R.id.main_button_obj_site);
		buttonAbout = (Button)this.findViewById(R.id.main_button_about);
		buttonExit = (Button)this.findViewById(R.id.main_button_exit);
		
		buttonStart.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ChooseObjActivity.class);
				startActivity(intent);
			}});
		buttonObjSite.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ObjSiteActivity.class);
				startActivity(intent);
			}});
		buttonAbout.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AboutActivity.class);
				startActivity(intent);
			}});
		buttonExit.setOnClickListener(new Button.OnClickListener(){
		    @Override
		    public void onClick(View v) {
		        // TODO Auto-generated method stub
		        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		        dialog.setTitle("Exit");
		        dialog.setMessage("Are you sure to exit the system?");
		        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {   
                        Intent intent = new Intent();  
                        intent.setAction(BORADCAST_ACTION_EXIT);  
                        sendBroadcast(intent);//發�???出系統廣播  每�?�接收器都�?��到 調動finish()關閉activity  
                        finish();  
                    }  
                });
		        dialog.setNegativeButton("No", null); 
		        dialog.show();
		    }});
	}
	
	public void onBackPressed() {    
        new AlertDialog.Builder(this)  
        .setTitle("Exit")  
        .setMessage("Are you sure to exit the system?")  
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {   
                                        @Override   
                                        public void onClick(DialogInterface dialog, int which) {   
                                            Intent intent = new Intent();  
                                            intent.setAction(BORADCAST_ACTION_EXIT);  
                                            sendBroadcast(intent);//發�???出系統廣播  每�?�接收器都�?��到 調動finish()關閉activity
                                            finish();  
                                        }  
                                    })  
        .setNegativeButton("No", null)  
        .show();  
    }
	
	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(BORADCAST_ACTION_EXIT)){//發来關閉action�?��播  
                finish();  
            }  
        }  
    };  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		dirDBHelper.close();
		unregisterReceiver(mBoradcastReceiver); //取消監聽  
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	private void loadSQLiteData(){
		String path = findDirectory();
		if(path==null)
			singleton.setObjLoadPath(DEFAULT_PATH);
		else
			singleton.setObjLoadPath(path);
	}
	@SuppressWarnings("deprecation")
	private String findDirectory(){
		SQLiteDatabase db = dirDBHelper.getReadableDatabase();
        Cursor cursor = db.query(dirDBHelper.getTableName(), null,null,null, null, null, null);
        startManagingCursor(cursor);
        if(cursor.getCount()==0){
        	return null;	
        }
        else{
        	 cursor.moveToFirst();
        	 return cursor.getString(1);
        }
	}
}
