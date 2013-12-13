package com.android.opgl.test;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

public class Run extends Activity
{
	private GLSurfaceView glSurface;
	//private GLSurfaceView glSurface2;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		glSurface=new GLSurfaceView(this);
		Test03 test03=new Test03(glSurface.getContext());
		glSurface.setZOrderOnTop(true);
		glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glSurface.getHolder().setFormat(PixelFormat.RGBA_8888);
		glSurface.setRenderer(test03);
		glSurface.setOnTouchListener(test03);
		//glSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		addContentView(glSurface, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//setContentView(glSurface);
	}
}