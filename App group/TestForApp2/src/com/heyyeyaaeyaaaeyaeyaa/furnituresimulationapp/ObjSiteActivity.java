package com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ObjSiteActivity extends Activity {
	private EditText TextInputObjSite;
	private Button buttonBack, buttonApply,buttonSource;
	private Singleton singleton = Singleton.getSharedInstance();
	private final String DEFAULT_PATH = "/storage/emulated/0/Pictures/MyCameraApp";
	private DBHelper dirDBHelper;
	private String currentDir;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在當前的activity中註冊廣播
		dirDBHelper = new DBHelper(this);
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);//爲BroadcastReceiver�?���?個action?�即要監聽�?���?名�?  
		registerReceiver(mBoradcastReceiver,filter); //動�?註冊監聽  靜�?�?�� 在AndroidManifest.xml中定義
		setContentView(R.layout.activity_obj_site);
		
		TextInputObjSite = (EditText)this.findViewById(R.id.obj_site_editText_input_site);
		buttonBack = (Button)this.findViewById(R.id.obj_site_button_back);
		buttonApply = (Button)this.findViewById(R.id.obj_site_button_apply);
		buttonSource = (Button)this.findViewById(R.id.source_button);
		currentDir=findDirectory();
		TextInputObjSite.setText(currentDir);
		
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
				currentDir = TextInputObjSite.getText().toString();
                updateDir(currentDir);
                singleton.setObjLoadPath(currentDir);
				/*把輸入的site丟�?�singleton裡面*/
			}});
		
		buttonSource.setOnClickListener(new Button.OnClickListener(){
            private String m_chosenDir = "";
            private boolean m_newFolderEnabled = true;

            @Override
            public void onClick(View v) 
            {
                // Create DirectoryChooserDialog and register a callback 
                DirectoryChooserDialog directoryChooserDialog = 
                new DirectoryChooserDialog(ObjSiteActivity.this, 
                    new DirectoryChooserDialog.ChosenDirectoryListener() 
                {
                    @Override
                    public void onChosenDir(String chosenDir) 
                    {
                        m_chosenDir = chosenDir;
                        TextInputObjSite.setText(chosenDir);
                        updateDir(m_chosenDir);
                        singleton.setObjLoadPath(m_chosenDir);
                    }
                }); 
                // Toggle new folder button enabling
                directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.

         
                directoryChooserDialog.chooseDirectory(m_chosenDir);
                m_newFolderEnabled = ! m_newFolderEnabled;
            }
		});
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        
        // 有選�?���?
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
            if(intent.getAction().equals(MainActivity.BORADCAST_ACTION_EXIT)){//發�?��閉action�?��播  
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
		dirDBHelper.close();
		Toast.makeText(this.getApplicationContext(), "ObjSiteDestroy", Toast.LENGTH_SHORT).show();
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
	private void insertDir(String chosenDir){
		SQLiteDatabase db = dirDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dirDBHelper.getFieldName2(), chosenDir);
        db.insert(dirDBHelper.getTableName(), null, values);
	}
	
	private void updateDir(String chosenDir){

		SQLiteDatabase db = dirDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dirDBHelper.getFieldName2(), chosenDir);

        db.update(dirDBHelper.getTableName(), values, "_id=1", null);
	}
	
	@SuppressWarnings("deprecation")
	private String findDirectory(){
		SQLiteDatabase db = dirDBHelper.getReadableDatabase();
        Cursor cursor = db.query(dirDBHelper.getTableName(), null,null,null, null, null, null);
        startManagingCursor(cursor);
        if(cursor.getCount()==0){
        	insertDir(DEFAULT_PATH);
        	return DEFAULT_PATH;
        }else{
        	 cursor.moveToFirst();
        	 return cursor.getString(1);
        }
	}
}
