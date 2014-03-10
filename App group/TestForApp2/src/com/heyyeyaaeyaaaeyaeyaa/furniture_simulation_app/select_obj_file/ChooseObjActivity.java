package com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.select_obj_file;

import java.io.File;
import java.io.FilenameFilter;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher.ViewFactory;

import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.camera.CameraActivity;
import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.main.MainActivity;
import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.shared.Singleton;
import com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp.R;

public class ChooseObjActivity extends Activity implements ViewFactory{
	private Button buttonBack, buttonSelect;
	private final int IMAGE_ITEM_SIZE = 300;
	private LinearLayout HSVLayout;
	private Singleton singleton = Singleton.getSharedInstance();
	private ImageSwitcher imageSwitcher;
	
	private Integer[] imgs = {R.drawable.img1 , R.drawable.img2 , R.drawable.img3 ,
			R.drawable.img4 , R.drawable.img5};
	
	private final String IMAGE_DIR = "/ObjPreview";
	private Uri[] mUris;
	private String[] mFiles = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);
		registerReceiver(mBoradcastReceiver,filter); 
		setContentView(R.layout.activity_choose_obj);
		
		HSVLayout = (LinearLayout)findViewById(R.id.HSVLayout);

        imageSwitcher = (ImageSwitcher)findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(this);
        
		setImageItem();
        
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
				
				Intent intent = new Intent();
				intent.setClass(ChooseObjActivity.this, CameraActivity.class);
				startActivity(intent);
			}});
	}

	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(MainActivity.BORADCAST_ACTION_EXIT)){
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
	
	public void setImageItem() {
		if((mUris = getImage())!=null) {
			for(int i=0;i<mUris.length;i++){
				ImageView imageView = new ImageView(this);  
				imageView.setImageURI(mUris[i]);
				imageView.setId(i);
				imageView.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_ITEM_SIZE,IMAGE_ITEM_SIZE,1.0f));
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setBackgroundColor(0xFF000000);
				imageView.setOnClickListener(new ImageView.OnClickListener(){

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
	        			singleton.setChoosenObjImageFileName(mUris[view.getId()].getLastPathSegment());
						imageSwitcher.setImageURI(mUris[view.getId()]);
					}
					
				});
				HSVLayout.addView(imageView);
			}
			
		}else
			for(int i=0;i<imgs.length;i++){
				ImageView imageView = new ImageView(this);  
				imageView.setImageResource(imgs[i]);
				imageView.setId(i);
				imageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1.0f));
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setBackgroundColor(0xFF000000);
				imageView.setOnClickListener(new ImageView.OnClickListener(){

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						imageSwitcher.setImageResource(imgs[view.getId()]);
					}
					
				});
				HSVLayout.addView(imageView);
			}
	}
	
	public Uri[] getImage(){
		//File images = Environment.getExternalStorageDirectory();
		File images = new File(singleton.getObjLoadPath()+IMAGE_DIR);
		File[] imagelist = images.listFiles(new FilenameFilter(){
			public boolean accept(File dir , String name){
				return(name.endsWith(".jpg")||name.endsWith(".png"));
			}
		});
		
		if(imagelist==null)
			return null;
		
		mFiles = new String[imagelist.length];
		for(int i=0;i<imagelist.length;i++)
			mFiles[i] = imagelist[i].getAbsolutePath();
		
		mUris = new Uri[mFiles.length];
		for(int i=0;i<mFiles.length;i++)
			mUris[i] = Uri.parse(mFiles[i]);
		
		return mUris;
	}
	@Override
	public View makeView() {
        ImageView i = new ImageView(ChooseObjActivity.this);  
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);  
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,  
                LayoutParams.MATCH_PARENT));  
        return i;  
	}
}
