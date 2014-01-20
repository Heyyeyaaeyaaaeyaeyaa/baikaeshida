package com.example.testforapp2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button buttonStart, buttonObjSite, buttonAbout, buttonExit;
	private Singleton singleton;
	public final static String BORADCAST_ACTION_EXIT = "com.wangzhj.exit";//關閉活動廣播action名稱 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
        filter.addAction(BORADCAST_ACTION_EXIT);//爲BroadcastReceiver指定一個action，即要監聽的消息名字  
        registerReceiver(mBoradcastReceiver,filter); //動態註冊監聽  靜態的話 在AndroidManifest.xml中定義
		
		setContentView(R.layout.activity_main);
		Toast.makeText(this.getApplicationContext(), "MainCreate", 1000).show();
		
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
                        sendBroadcast(intent);//發送退出系統廣播  每個接收器都會收到 調動finish()關閉activity  
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
                                            sendBroadcast(intent);//發送退出系統廣播  每個接收器都會收到 調動finish()關閉activity
                                            finish();  
                                        }  
                                    })  
        .setNegativeButton("No", null)  
        .show();  
    }
	
	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(BORADCAST_ACTION_EXIT)){//發来關閉action的廣播  
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
		Toast.makeText(this.getApplicationContext(), "MainDestroy", 1000).show();
		unregisterReceiver(mBoradcastReceiver); //取消監聽  
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(this.getApplicationContext(), "MainPause", 1000).show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Toast.makeText(this.getApplicationContext(), "MainStart", 1000).show();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Toast.makeText(this.getApplicationContext(), "MainStop", 1000).show();
	}

}
