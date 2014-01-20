package com.example.testforapp2;

//import com.example.dragimagedemo.R;

//import com.example.dragimagedemo.ImageViewHelper;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
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
	private HeyRenderer heyRenderer;
	
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
		heyRenderer=new HeyRenderer(glSurfaceView.getContext(),glSurfaceView.getHeight(),glSurfaceView.getWidth());
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
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); //取得螢幕資訊
		Toast.makeText(this.getApplicationContext(), "手機螢幕 高度:"+dm.heightPixels+" 寬度:"+dm.widthPixels, Toast.LENGTH_SHORT).show();
		
		preview = new CameraPreview(this, dm);
		frameLayout.addView(preview);
		
		buttonBack.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(CameraActivity.this, ChooseObjActivity.class);
				startActivity(intent);
			}});

		/*===============以下拍照按鈕*/
		buttonCapture.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//新增一個PictureCallback
				PictureCallback picture = new PictureCallback() {
				    @Override
				    public void onPictureTaken(byte[] data, Camera camera) {
				        File pictureFile = getOutputMediaFile(1); //MEDIA_TYPE_IMAGE = 1,  MEDIA_TYPE_VIDEO = 2
				        if (pictureFile == null){
				            Log.d("David", "Error creating media file");
				            return;
				        }
						
				        try {
				        	Bitmap glBitmap = heyRenderer.getBitmap();
				        	Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
				        	Matrix mtx = new Matrix();
				        	mtx.postRotate(270);
				        	glBitmap = Bitmap.createBitmap(glBitmap, 0, 0, 1080, 1920, mtx, true);
				        	bmp = combineImages(bmp, glBitmap);
				            FileOutputStream fos = new FileOutputStream(pictureFile);
				            bmp.compress(CompressFormat.PNG, 100, fos);
				            fos.flush();
				            fos.close();

				        } catch (FileNotFoundException e) {
				            Log.d("David", "File not found: " + e.getMessage());
				        } catch (IOException e) {
				            Log.d("David", "Error accessing file: " + e.getMessage());
				        }
				        camera.startPreview(); //拍完照以後刷新畫面 不加這行 會讓畫面一直停在拍照的那張圖
				    }
				};
		        // get an image from the camera
				//拍照 把PictureCallback丟進去
				camera.takePicture(null, null, picture);	 
		}});
		/*===============以上拍照按鈕*/
		
		
		
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
		String TAG = "Log";
    	Camera camera = null;
	    try {
	        camera = Camera.open();
	    } catch (Exception e) {
	    	Log.e(TAG ,"FUCK IT!");
	    }
	    return camera;
	  }
	//=====以下是儲存照片用
	/** Create a file Uri for saving an image or video */
	//這個目前沒用到
	private Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		if(Environment.getExternalStorageState() == null) //查看外部儲存空間是否存在
			Log.d("SDcard", "doesn't exist");
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory( //在手機Picture內建資料夾上 新建一個資料夾MyCameraApp
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    
	    //System.out.println(mediaStorageDir.getPath());
	    //但是不曉得為什麼他還是儲存在手機上 不是sd card上
	    // 這是我在LogCat上追蹤到的儲存路徑 : storage/emulated/0/Pictures/MyCameraApp

	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());//取得日期時間
	    
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "AppCameraTest_IMG_"+ timeStamp + ".jpg");

	    
	    return mediaFile;
	}
	//=====以上是儲存照片用
	
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
		setPictureSize(camera,1080);
        preview.setCamera(camera);
        Camera.Size cs = camera.getParameters().getPictureSize();
        Log.e("Log",cs.height+""+cs.width);
	}
	public Bitmap combineImages(Bitmap c, Bitmap s) {

		Bitmap cs = null; 
		 
	    int width, height = 0; 
	     
	    if(c.getWidth() > s.getWidth()) { 
	      width = c.getWidth(); 
	      height = c.getHeight();
	    } else { 
	      width = c.getWidth(); 
	      height = c.getHeight();
	    } 
	 
	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
	 
	    Canvas comboImage = new Canvas(cs); 
	 
	    comboImage.drawBitmap(c, 0f, 0f, null); 
	    comboImage.drawBitmap(s, 0f, 0f, null); 
        return cs;
    }
	public void setPictureSize(Camera cam, int maxWidth) {
	    //端末がサポートするサイズ一覧取得
	    Camera.Parameters params = cam.getParameters();
	    List<Size> sizes = params.getSupportedPictureSizes();
	    if ( sizes != null && sizes.size() > 0) {
	        //撮影サイズを設定する
	    	double targetRatio = (double)4 / 3;
	        Size setSize = sizes.get(0);
	        for(Size size : sizes){
	        	double ratio = (double) size.width / size.height;
	            if(Math.min(size.width, size.height) <= maxWidth && Math.abs(ratio - targetRatio) < 0.1) {
	                setSize = size;
	                break;
	            }
	        }
	        params.setPictureSize(setSize.width, setSize.height);
	        cam.setParameters(params);
	    }
	}
}
