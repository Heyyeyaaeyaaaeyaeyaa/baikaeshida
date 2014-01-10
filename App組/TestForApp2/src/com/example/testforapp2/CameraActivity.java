package com.example.testforapp2;

//import com.example.dragimagedemo.R;

//import com.example.dragimagedemo.ImageViewHelper;




import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraActivity extends Activity {
	private Button buttonBack, buttonCapture;
	private FrameLayout frameLayout;
	private Camera camera;
	private CameraPreview preview;
	private Singleton singleton;
	
	private DisplayMetrics dm; //螢幕
	private GLSurfaceView glSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//在當前的activity中註冊廣播
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);//爲BroadcastReceiver指定一個action，即要監聽的消息名字  
		registerReceiver(mBoradcastReceiver,filter); //動態註冊監聽  靜態的話 在AndroidManifest.xml中定義
		setContentView(R.layout.activity_camera);

		//LOG新增區塊->開發者看完後可刪除此註解
		//===============start===============
		
		glSurfaceView=(GLSurfaceView)findViewById(R.id.glSurfaceView);
		//建立GLSurface的View
		HeyRenderer heyRenderer=new HeyRenderer(glSurfaceView.getContext());
		//新增Renderer給GLSurfaceView用
		glSurfaceView.setZOrderMediaOverlay(true);
		//(不確定)將view擺在最上層
		glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
		//(不確定)已上兩組是設定配色採用RGBA，這樣才能透明
		glSurfaceView.setRenderer(heyRenderer);
		//把做好的Renderer掛到glSurfaceView上面
		glSurfaceView.setOnTouchListener(heyRenderer);
		//glSurfaceView附加觸控監聽的Buff
		
		//glSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		//這裡是指重畫的模式，預設是不斷重畫，上面的設定是除非呼叫glSurfaceView.requestRender()不然不會重畫
		//經測試使用後監聽會失效，但我覺得預設的模式要一直畫，也許有替代方案就留下來了
		
		//================end================
		
		buttonBack = (Button)this.findViewById(R.id.camera_button_back);
		buttonCapture = (Button)this.findViewById(R.id.camera_button_capture);
		frameLayout = (FrameLayout)this.findViewById(R.id.camera_frameLayout);
		preview = new CameraPreview(this);

		dm = new DisplayMetrics();
		frameLayout.addView(preview);
		
		buttonBack.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(CameraActivity.this, ChooseObjActivity.class);
				startActivity(intent);
			}});
		buttonCapture.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*拍照*/
			}});
		getWindowManager().getDefaultDisplay().getMetrics(dm); //取得螢幕資訊
		Toast.makeText(this.getApplicationContext(), "手機螢幕 高度:"+dm.heightPixels+" 寬度:"+dm.widthPixels, Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}
	
	private Camera getCameraInstance() {
		String TAG = "camera99";
    	Camera camera = null;
	    try {
	        camera = Camera.open();
	    } catch (Exception e) {
	    	Log.e(TAG ,"FUCK IT!");
	    }
	    return camera;
	  }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this.getApplicationContext(), "CameraDestroy", Toast.LENGTH_SHORT).show();
		unregisterReceiver(mBoradcastReceiver); //取消監聽
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        if (camera != null) {
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
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
	protected void onResume(){
		super.onResume();
		camera = getCameraInstance();
        preview.setCamera(camera);
	}
}
