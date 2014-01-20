package com.example.testforapp2;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ObjSiteActivity extends Activity {
	private EditText TextInputObjSite;
	private Button buttonBack, buttonApply;
	private Singleton singleton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);//爲BroadcastReceiver指定一個action，即要監聽的消息名字  
		registerReceiver(mBoradcastReceiver,filter); //動態註冊監聽  靜態的話 在AndroidManifest.xml中定義
		setContentView(R.layout.activity_obj_site);
		
		TextInputObjSite = (EditText)this.findViewById(R.id.obj_site_editText_input_site);
		buttonBack = (Button)this.findViewById(R.id.obj_site_button_back);
		buttonApply = (Button)this.findViewById(R.id.obj_site_button_apply);
		
		buttonBack.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ObjSiteActivity.this, MainActivity.class);
				startActivity(intent);
			}});
		buttonApply.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String site = TextInputObjSite.getText().toString();
				/*把輸入的site丟進singleton裡面*/
			}});
	}

	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(MainActivity.BORADCAST_ACTION_EXIT)){//發來關閉action的廣播  
                finish();  
            }  
        }  
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.obj_site, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this.getApplicationContext(), "ObjSiteDestroy", 1000).show();
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
		//decide close activity

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
