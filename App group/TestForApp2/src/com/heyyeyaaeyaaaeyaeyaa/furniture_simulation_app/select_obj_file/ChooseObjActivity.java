package com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.select_obj_file;

import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.camera.CameraActivity;
import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.main.MainActivity;
import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.shared.Singleton;
import com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

@SuppressWarnings("deprecation")
public class ChooseObjActivity extends Activity {
	private Button buttonBack, buttonSelect;
	private ImageAdapter imgAdapter = null;
	private Gallery gallery = null;
	private Singleton singleton = Singleton.getSharedInstance();
	private ImageSwitcher imageSwitcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);//爲BroadcastReceiver?�??�?�?�?個action??�即要監聽?�??�?�?�?名�?  
		registerReceiver(mBoradcastReceiver,filter); //動�?註冊監聽  靜�??�??�?� 在AndroidManifest.xml中定義
		setContentView(R.layout.activity_choose_obj);
		
		gallery = (Gallery) findViewById(R.id.gallery);  
        imgAdapter = new ImageAdapter(this);  
        imageSwitcher = (ImageSwitcher)findViewById(R.id.imageSwitcher);
        
        imageSwitcher.setFactory(new ViewFactory() {  
            @Override  
            public View makeView() {  
                // TODO Auto-generated method stub  
                ImageView i = new ImageView(ChooseObjActivity.this);  
                // 把圖?�??�?�比例擴大/縮小到View?�??�?�度??�置中顯示  
                i.setScaleType(ImageView.ScaleType.FIT_CENTER);  
                i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,  
                        LayoutParams.MATCH_PARENT));  
                return i;  
            }  
        });
        
        // 設置點擊圖片?�??�?�聽事件??��?要用手點擊才觸發??�滑動時不觸發?? 
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
                Toast.makeText(ChooseObjActivity.this, "點擊圖片 " + (position + 1), Toast.LENGTH_SHORT).show();  
            }  
        });          
        
        // 設置選中圖片?�??�?�聽事件??�當圖片滑到螢幕正中??��?��為自動選中??  
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
            @Override  
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   
                if(imgAdapter.getImage() != null){
        			imageSwitcher.setImageURI(imgAdapter.getImage()[position % imgAdapter.getImage().length]);
        			singleton.setChoosenObjImageFileName(imgAdapter.getImage()[position % imgAdapter.getImage().length].getLastPathSegment());
                    Toast.makeText(ChooseObjActivity.this, "選中圖片 " + imgAdapter.getImage()[position % imgAdapter.getImage().length].getLastPathSegment(), Toast.LENGTH_SHORT).show(); 
                }else
        			imageSwitcher.setImageResource(imgAdapter.getImgs()[position % imgAdapter.getImgs().length]);
            }  
      
            @Override  
            public void onNothingSelected(AdapterView<?> arg0) {  
                  
            }  
        });        
        
        gallery.setUnselectedAlpha(0.3f);                   // 設置未選中圖片?�???�?�?度  
        gallery.setSpacing(40); 
		
        gallery.setAdapter(imgAdapter);                     // 設置圖片?�??�?  
        gallery.setGravity(Gravity.CENTER_HORIZONTAL);      // 設置水平置中顯示  
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
				
				/*選?�??�?�要使用?�??�?�具OBJ??�要把他告訴給下�?個CameraActivity知?�?*/
				
				Intent intent = new Intent();
				intent.setClass(ChooseObjActivity.this, CameraActivity.class);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_obj, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
