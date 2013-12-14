package com.android.opgl.test;

import com.example.testforapp2.R;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class Run extends Activity
{
	private MyGLSurfaceView glSurface;
	//private GLSurfaceView glSurface2;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		Toast.makeText(getApplicationContext(), "setContentView", 1000).show();
		//glSurface=new GLSurfaceView(this);
		glSurface=(MyGLSurfaceView)this.findViewById(R.id.myGLSurfaceView);
		Toast.makeText(getApplicationContext(), "glSurface findViewById", 1000).show();
		Test03 test03=new Test03(glSurface.getContext());
		//glSurface.setZOrderOnTop(true);
		glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		glSurface.setRenderer(test03);
		glSurface.setOnTouchListener(test03);
		//glSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		//addContentView(glSurface, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//setContentView(glSurface);
		
	}
}