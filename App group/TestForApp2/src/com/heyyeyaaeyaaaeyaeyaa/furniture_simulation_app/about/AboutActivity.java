package com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.about;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.main.MainActivity;
import com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp.R;

public class AboutActivity extends Activity {
	private Button buttonBack;
//	private Singleton singleton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);  
        registerReceiver(mBoradcastReceiver,filter);
		setContentView(R.layout.activity_about);
			
		buttonBack = (Button)this.findViewById(R.id.about_button_back);
		
		buttonBack.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(AboutActivity.this, MainActivity.class);
				startActivity(intent);
			}});
	}

	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(MainActivity.BORADCAST_ACTION_EXIT)){//發?�??�?�閉action?�??�?�播  
                finish();  
            }  
        }  
    }; 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBoradcastReceiver); 
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
				super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
