package com.example.testforapp2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {
	
	private TextView textView;
	private Button buttonBack;
	private Button buttonCapture;
	private Button buttonFocus;
	private FrameLayout frameLayout;
	private BroadcastReceiver mBoradcastReceiver;
	private Camera camera;
	private CameraPreview preview;
	private Singleton singleton;
	private HeyRenderer heyRenderer;
	private DisplayMetrics dm;
	private GLSurfaceView glSurfaceView;
	private int defaultPictureExpectedSize = 1080;
	private int defaultPictureWidth = 1280;
	private int defaultPictureHeight = 960;
	private String LOGTAG = "Log";
	private String DAVIDTAG = "David";
	private float focalLength;
	private int foot;
	private double magnification;
	private int rotation;
	private final int ROTATION_VERTICAL = 0;
	private final int ROTATION_LEFT_HORIZONTAL = 1;
	private final int ROTATION_RIGHT_HORIZONTAL = 2;
	private final int ROTATION_UPSIDE_DOWN = 3;
	private final int TYPE_DISTANCE_OPTION = 0;
	private ObjSizeManager osm;
	private SensorEventListener listener;
	private SensorManager sensorMgr;
	
	private boolean takePictureClick = false;
	private AngleManager angleMgr;
	private float angle = 0;
	private float angleX = 0;
	private float angleY = 0;
	private float angleZ = 0;
	private float newAngle = 0;
	
	private float[] accelerometer_values;
	private float[] magnitude_values;
	private float[] angle_rotation; //旋轉角度 有正負號 使用前請先自行觀測數據變化
	private float[] rMtrix; //rotation matrix
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_camera);
		initBroadcast();		
		initGlSurfaceView();
		
		textView = (TextView)this.findViewById(R.id.camera_text_title);
		buttonBack = (Button)this.findViewById(R.id.camera_button_back);
		buttonCapture = (Button)this.findViewById(R.id.camera_button_capture);
		buttonFocus = (Button)this.findViewById(R.id.camera_button_focus);
		frameLayout = (FrameLayout)this.findViewById(R.id.camera_frameLayout);
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); //取得螢幕資訊
		Toast.makeText(this.getApplicationContext(), "手機螢幕 高度:"+dm.heightPixels+" 寬度:"+dm.widthPixels, Toast.LENGTH_SHORT).show();
		
		preview = new CameraPreview(this, dm);
		preview.setLayoutParams(new RelativeLayout.LayoutParams(defaultPictureHeight, defaultPictureWidth));
		frameLayout.addView(preview);
		sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
		angleMgr = new AngleManager();
		focalLength = 0;
		foot = 1;
		rotation = 0;
		angle_rotation = new float[3]; 
		rMtrix = new float[9]; 
		
		buttonBack.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent();
				intent.setClass(CameraActivity.this, ChooseObjActivity.class);
				startActivity(intent);
			}
		});
		buttonCapture.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				takePictureClick = true;
				autoFocus();
			}
		});
		buttonFocus.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				autoFocus();
			}
		});
		
		listener = new SensorEventListener() { 
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
			@Override
			public void onSensorChanged(SensorEvent event) {
				
				
				
				
				
				
				//以下是取旋轉角度
				//http://epaper.gotop.com.tw/pdf/AEL011000.pdf
				//詳細請看方向感測器章節的說明
				switch (event.sensor.getType()) { 
					case Sensor.TYPE_ACCELEROMETER: 
						accelerometer_values = (float[]) event.values.clone(); 
						break; 
					case Sensor.TYPE_MAGNETIC_FIELD: 
						magnitude_values = (float[]) event.values.clone(); 
						break; 
					default: 
						break; 
				} 
				
				if (magnitude_values != null && accelerometer_values != null) { 
					
					//float[] R = new float[9]; 
					//float[] angle_rotation = new float[3]; 
					
					SensorManager.getRotationMatrix(rMtrix, null, accelerometer_values, magnitude_values); 
					SensorManager.getOrientation(rMtrix, angle_rotation); 
					
					StringBuilder sensorInfo = new StringBuilder(); 
					
					for (int i = 0; i < angle_rotation.length; i++) {
						angle_rotation[i] = (float) (angle_rotation[i]*180/Math.PI); // 轉換到 度數		
						BigDecimal bd = new BigDecimal(angle_rotation[i]); 
						angle_rotation[i] = bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); //4捨5入到小數點第1位的方法
						sensorInfo.append(" - 旋轉角度 [" + i + "] = " + angle_rotation[i] + "\n");
					}
					textView.setText(sensorInfo);
					
					
					
					
					
					
					if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
					
						float value[] = event.values;
						if(angle_rotation[1] < 0 && angle_rotation[1] > -90){
							newAngle = Math.abs(angle_rotation[1]);
						}

						if(value[2] < 0)
							newAngle = 90;
						else if(value[1] < 0)
							newAngle = 0;	
					}
		
				} 
				//以上是取旋轉角度
					
				Sensor sensor = event.sensor;
				float[] values = event.values;
				int rotationState = (values[2] >= 0) ? ROTATION_VERTICAL : ROTATION_UPSIDE_DOWN;
				
				if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
	
					if(values[1] > 9 && rotation != ROTATION_VERTICAL){ //直立
						rotation = ROTATION_VERTICAL;
						Toast.makeText(getApplicationContext(), "直立", Toast.LENGTH_SHORT).show();
					}
					else if(values[0] > 9 && rotation != ROTATION_LEFT_HORIZONTAL){
						rotation = ROTATION_LEFT_HORIZONTAL;
						Toast.makeText(getApplicationContext(), "左旋轉 橫立", Toast.LENGTH_SHORT).show();
					}
					else if(values[0] < -9 && rotation != ROTATION_RIGHT_HORIZONTAL){
						rotation = ROTATION_RIGHT_HORIZONTAL;
						Toast.makeText(getApplicationContext(), "右旋轉 橫立", Toast.LENGTH_SHORT).show();
					}
					else if(values[1] < -9 && rotation != ROTATION_UPSIDE_DOWN){
						rotation = ROTATION_UPSIDE_DOWN;
						Toast.makeText(getApplicationContext(), "倒立", Toast.LENGTH_SHORT).show();
					}
					//取手機俯角 的角度
					
					angleMgr.setAngleRotation(angle_rotation);
					angleMgr.setValue(values);
					angleMgr.setOrientation(rotation);
					

					float newAngleX = angle_rotation[1];
					float deltaAngle = Math.abs(angleX-newAngleX);
					float newAngleY = angle_rotation[2];
						  deltaAngle += Math.abs(angleY-newAngleY);
					float newAngleZ = angle_rotation[0];
						  //deltaAngle += Math.abs(angleZ-newAngleZ);
					angleX = newAngleX;
					angleY = newAngleY;
					angleZ = newAngleZ;
					//boolean condition1 = (angleX>65||angleX<-65)&&deltaAngle>40;
					//boolean condition2 = (angleX<=65 && angleX>=-65)&&deltaAngle>4;
					if(deltaAngle>2){
						Log.e("Log",deltaAngle+"");
						float height = 500;
						focalLength = camera.getParameters().getFocalLength();
						if(rotation==ROTATION_LEFT_HORIZONTAL||rotation==ROTATION_RIGHT_HORIZONTAL)
							osm.changeObjSizeByDistance(focalLength,angleY, height);
						else
							osm.changeObjSizeByDistance(focalLength,angleX, height);
						osm.rotateObj(angleX,angleY,angleZ,rotationState);
					}
					
				}
				
				
				
			}
		};
		
		
		
		
	}
	private void initGlSurfaceView()
	{
		glSurfaceView=(GLSurfaceView)findViewById(R.id.glSurfaceView);
		heyRenderer=new HeyRenderer(glSurfaceView.getContext(),defaultPictureWidth,defaultPictureHeight);
		glSurfaceView.setZOrderMediaOverlay(true);
		glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
		glSurfaceView.setRenderer(heyRenderer);
		glSurfaceView.setOnTouchListener(heyRenderer);
		glSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(defaultPictureHeight, defaultPictureWidth));
        osm = new ObjSizeManager(heyRenderer);
	}
 	private void initBroadcast()
	{
		mBoradcastReceiver = new BroadcastReceiver(){  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            if(intent.getAction().equals(MainActivity.BORADCAST_ACTION_EXIT)){
	                finish();  
	            }  
	        }  
	    }; 
		IntentFilter filter = new IntentFilter();  
		filter.addAction(MainActivity.BORADCAST_ACTION_EXIT);
		registerReceiver(mBoradcastReceiver,filter); 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.camera, menu);
		menu.add(0, 0, 0, "Object Distance");
		menu.add(0, 1, 1, "Option 1");
		menu.add(0, 2, 2, "Option 2");
		menu.add(0, 3, 3, "Option 3");
		return true;
	}
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	  // TODO Auto-generated method stub

	  switch(item.getItemId()){
	  case TYPE_DISTANCE_OPTION:
		  	LayoutInflater inflater = LayoutInflater.from(CameraActivity.this);
			final View v = inflater.inflate(R.layout.camera_setting_foot, null);
			new AlertDialog.Builder(CameraActivity.this)
		    .setTitle("請輸入多少步的距離")
		    .setView(v)
		    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {                               
		        	EditText editText = (EditText) (v.findViewById(R.id.camera_setting_foot_input));
		        	if(!editText.getText().toString().equals(""))
		        	{
		        		osm.initSize();
		        		foot = Integer.parseInt(editText.getText().toString());
		        		Toast.makeText(getApplicationContext(), "foot="+foot, Toast.LENGTH_SHORT).show();
		        		
				        focalLength = camera.getParameters().getFocalLength();
				        Toast.makeText(getApplicationContext(), "focalLength="+focalLength, Toast.LENGTH_SHORT).show();
						osm.changeObjSizeByFoot(focalLength,foot);
		        	}
		        }
		    })
		    .show();
	   break;
	  case (1):
	   break;
	  case (2):
	   break;
	  case (3):
	   break;
	  }
	  return true;
	 }
	 
	private Camera getCameraInstance() {
    	Camera camera = null;
	    try {
	        camera = Camera.open();
	    } catch (Exception e) {
	    	Log.e(LOGTAG ,"FUCK IT!");
	    }
	    return camera;
	  }
	
	/** Create a file Uri for saving an image or video */
	@SuppressWarnings("unused")
	private Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type){
		if(Environment.getExternalStorageState() == null) 
			Log.d("SDcard", "doesn't exist");
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory( 
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());//取得日期時間	    
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "AppCameraTest_IMG_"+ timeStamp + ".jpg");    
	    return mediaFile;
	}
	
	private void autoFocus(){
		Camera.Parameters parameters = camera.getParameters();
		parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
		camera.setParameters(parameters);
		AutoFocusCallback autoFacusCallback = new AutoFocusCallback()
		{
		    @Override
		    public void onAutoFocus(boolean success, Camera camera)
		    {
		        Log.i("onAutoFocus", "onAutoFocus:" + success);
		        if(takePictureClick == true){
			        takePictureClick = false;
			        takePicture();
		        }
		    }
		};
		camera.autoFocus(autoFacusCallback);
	}
	
	private void takePicture(){
		final PictureCallback picture = new PictureCallback() 
		{
			@Override
			public void onPictureTaken(byte[] data, Camera camera) 
			{
				File pictureFile = getOutputMediaFile(1);
				if (pictureFile == null)
				{
					Log.d(DAVIDTAG, "Error creating media file");
					return;
				}		
				try 
				{
			        glSurfaceView.queueEvent(new Runnable() {
			             public void run() {
			                   heyRenderer.snedBitmapResquest();
			             }
			         });
			        while(!heyRenderer.getResponseFlag());
			        heyRenderer.setResponseFlag(false);
			        Bitmap glBitmap = heyRenderer.getBitmap();
					Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
					Matrix mtx = new Matrix();
					mtx.postRotate(270);
					glBitmap = Bitmap.createBitmap(glBitmap, 0, 0, glBitmap.getWidth(), glBitmap.getHeight(), mtx, true);
					bmp = combineImages(bmp, glBitmap);
					FileOutputStream fos = new FileOutputStream(pictureFile);
					switch(rotation){
						case ROTATION_VERTICAL:
							mtx.postRotate(180);
							break;
						case ROTATION_LEFT_HORIZONTAL:
							mtx.postRotate(90);
							break;
						case ROTATION_RIGHT_HORIZONTAL:
							mtx.postRotate(270);
							break;
						case ROTATION_UPSIDE_DOWN:
							mtx.postRotate(0);
							break;
					}
					bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mtx, true);
					bmp.compress(CompressFormat.PNG, 100, fos);
					fos.flush();
					fos.close();
					glBitmap.recycle();
					bmp.recycle();
				} 
				catch (FileNotFoundException e)
				{
					Log.d(DAVIDTAG, "File not found: " + e.getMessage());
				} 
				catch (IOException e) 
				{
					Log.d(DAVIDTAG, "Error accessing file: " + e.getMessage());
				}
				camera.startPreview();
			}
		};
		        camera.takePicture(null, null, picture);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(this.getApplicationContext(), "CameraDestroy", Toast.LENGTH_SHORT).show();
		unregisterReceiver(mBoradcastReceiver); //取消監聽
	}
	@Override
	protected void onPause() {
		super.onPause();
        if (camera != null) {
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        
		 sensorMgr.unregisterListener(listener); 
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	protected void onResume(){
		super.onResume();
		camera = getCameraInstance();
		findAndSetPictureSupportSize(camera,defaultPictureExpectedSize);
        preview.setCamera(camera);

		sensorMgr.registerListener(listener, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI); 
		sensorMgr.registerListener(listener, sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI); 
		
		
		/*if(!(sensorMgr.registerListener(listener, sensorMgr 
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_UI) && 
				sensorMgr.registerListener(listener, sensorMgr 
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
				SensorManager.SENSOR_DELAY_UI))){ 
				Log.w("OrientationEx", "sensor not found!"); 
				sensorMgr.unregisterListener(listener); 
		}*/
	}
	/**combine photo and image to a bitmap */
	public Bitmap combineImages(Bitmap cameraBitmap, Bitmap glImageBitmap) 
	{
		Bitmap combinedBitmap = null; 
	    int width, height = 0; 
	    if(cameraBitmap.getWidth() > glImageBitmap.getWidth()) 
	    { 
	      width = cameraBitmap.getWidth(); 
	      height = cameraBitmap.getHeight();
	    } else 
	    { 
	      width = cameraBitmap.getWidth(); 
	      height = cameraBitmap.getHeight();
	    } 
	    combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
	    Canvas comboImage = new Canvas(combinedBitmap); 
	    comboImage.drawBitmap(cameraBitmap, 0f, 0f, null); 
	    comboImage.drawBitmap(glImageBitmap, 0f, 0f, null); 
        return combinedBitmap;
    }
	/**Choose photo size */
	public void findAndSetPictureSupportSize(Camera cam, int maxWidth) 
	{
	    Camera.Parameters params = cam.getParameters();
	    List<Size> sizes = params.getSupportedPictureSizes();
	    if ( sizes != null && sizes.size() > 0) 
	    {
	    	double targetRatio = (double)4 / 3;
	        Size setSize = sizes.get(0);
	        for(Size size : sizes)
	        {
	        	double ratio = (double) size.width / size.height;
	            if(Math.min(size.width, size.height) <= maxWidth && Math.abs(ratio - targetRatio) < 0.1) 
	            {
	                setSize = size;
	                break;
	            }
	        }
	        params.setPictureSize(setSize.width, setSize.height);
	        cam.setParameters(params);
	    }
	}
}
