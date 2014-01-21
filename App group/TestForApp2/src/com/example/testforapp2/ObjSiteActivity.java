package com.example.testforapp2;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	private Button buttonBack, buttonApply,buttonSource;
	private Singleton singleton = Singleton.getSharedInstance();
	
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
		buttonSource = (Button)this.findViewById(R.id.source_button);
		
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
				singleton.setSite(TextInputObjSite.getText().toString());
				/*把輸入的site丟進singleton裡面*/
			}});
		
		buttonSource.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			    intent.setType("file/*");
			    startActivityForResult(intent,0);
			}});
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        
        // 有選擇檔案
        if ( resultCode == RESULT_OK )
        {
            // 取得檔案的 Uri
            Uri uri = data.getData();
            if( uri != null )
            {
                // 利用 Uri 顯示 ImageView 圖片
            	TextInputObjSite.setText( uri.getPath() );
            }
        }
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
