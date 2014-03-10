package com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder holder;
	private Camera camera;
	private final String TAG = "Preview";
//	private DisplayMetrics dm;
//	private float focalLength;
	
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, DisplayMetrics dm) {
		super(context);
		// TODO Auto-generated constructor stub
		//this.camera = camera;
		this.holder = this.getHolder();
		this.holder.addCallback(this);
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		this.dm = dm;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			if(camera!=null)
				camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IOException caused by setPreviewDisplay()", e);
		}
		camera.setDisplayOrientation(90);//????½e????½????½????½????½????½????½???½????½????½???½????½???½??½90????½??
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(camera!=null)
			camera.stopPreview();
	}
	public void setCamera(Camera camera){
		this.camera = camera;
		
	}
}
