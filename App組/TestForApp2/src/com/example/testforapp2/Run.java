package com.example.testforapp2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Run extends Activity
{
	private GLSurfaceView glSurface;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		glSurface=new GLSurfaceView(this);
		Test03 test03=new Test03(glSurface.getContext());
		glSurface.setRenderer(test03);
		glSurface.setOnTouchListener(test03);
		setContentView(glSurface);
	}
}