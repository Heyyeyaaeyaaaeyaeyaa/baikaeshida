package com.example.testforapp2;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseObjActivity extends Activity {
	private Button buttonBack, buttonSelect;
	private Singleton singleton;
	private TextView objListView;
	private static final String TAG = "SDCardActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);//爲BroadcastReceiver指定一個action，即要監聽的消息名字  
		registerReceiver(mBoradcastReceiver,filter); //動態註冊監聽  靜態的話 在AndroidManifest.xml中定義
		setContentView(R.layout.activity_choose_obj);
		
		buttonBack = (Button)this.findViewById(R.id.choose_obj_button_back);
		buttonSelect = (Button)this.findViewById(R.id.choose_obj_button_select);
		objListView = (TextView)this.findViewById(R.id.choose_obj_objlist);
		
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
	
	protected void onResume(){
		super.onResume();
		File sdCardDir = Environment.getExternalStorageDirectory();
		File objDir = new File(sdCardDir, "BAIObj");
        File[] list = objDir.listFiles();
        StringBuilder sb = new StringBuilder();
        this.listChildred(list, sb, "");
        this.objListView.setText(sb.toString());
	}
	private void listChildred(File[] list, StringBuilder sb, String space) {
		// TODO Auto-generated method stub
		 if (list == null) {
	            // 跳過空目錄
	            Log.e(TAG, ">>>>>>>>>>>>>>>>");
	            return;
	        }
	        for (File f : list) {
	            String msg;
	            if (f.isDirectory()) {
	                msg = space + " + " + f.getName();
	                sb.append(msg + "\n");
	                Log.d(TAG, msg);
	                // 往下一層
	                this.listChildred(f.listFiles(), sb, space + "   ");
	            }
	            else {
	                msg = space + " - " + f.getName();
	                sb.append(msg + "\n");
	                Log.d(TAG, msg);
	            }
	       }
	}
}
