package com.example.testforapp2;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;

public class ChooseObjActivity extends Activity {
	private Button buttonBack, buttonSelect;
	private ImageAdapter imgAdapter = null;
	private Gallery gallery = null;
	private Singleton singleton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);//爲BroadcastReceiver指定一個action，即要監聽的消息名字  
		registerReceiver(mBoradcastReceiver,filter); //動態註冊監聽  靜態的話 在AndroidManifest.xml中定義
		setContentView(R.layout.activity_choose_obj);
		
		gallery = (Gallery) findViewById(R.id.gallery);  
        imgAdapter = new ImageAdapter(this);  
        
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
                Toast.makeText(ChooseObjActivity.this, "点击图片 " + (position + 1), 100).show();  
            }  
        });          // 设置点击图片的监听事件（需要用手点击才触发，滑动时不触发）  
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
            @Override  
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  
                Toast.makeText(ChooseObjActivity.this, "选中图片 " + (position + 1), 20).show();  
            }  
      
            @Override  
            public void onNothingSelected(AdapterView<?> arg0) {  
                  
            }  
        });        // 设置选中图片的监听事件（当图片滑到屏幕正中，则视为自动选中）  
        gallery.setUnselectedAlpha(0.3f);                   // 设置未选中图片的透明度  
        gallery.setSpacing(40); 
		
        gallery.setAdapter(imgAdapter);                     // 设置图片资源  
        gallery.setGravity(Gravity.CENTER_HORIZONTAL);      // 设置水平居中显示  
        gallery.setSelection(imgAdapter.imgs.length * 100);
        
		buttonBack = (Button)this.findViewById(R.id.choose_obj_button_back);
		buttonSelect = (Button)this.findViewById(R.id.choose_obj_button_select);
		
		buttonBack.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseObjActivity.this, MainActivity.class);
				startActivity(intent);
			}});
		buttonSelect.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*選擇好要使用的家具OBJ，要把他告訴給嚇一個CameraActivity知道*/
				
				Intent intent = new Intent();
				intent.setClass(ChooseObjActivity.this, CameraActivity.class);
				startActivity(intent);
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
		getMenuInflater().inflate(R.menu.choose_obj, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this.getApplicationContext(), "chooseObjDestroy", 1000).show();
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
