package com.android.opgl.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testforapp2.R;
import com.test.obj.ObjData;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Test03 implements Renderer, OnTouchListener
{
	private Shape[] shapes;
	private Context context;
	
	private String testFilename = "Simba01.data";
	private int shapeCount = 2;
	private int testTexture = R.raw.simba1;
	private String[] dataFileName;
	
	/* Rotation values */
	private float xrot;					//X Rotation
	private float yrot;					//Y Rotation
	private float xmove;
	private float ymove;
	private float zmove;
	private int lock = 0;
	
	/* 
	 * The initial light values for ambient and diffuse
	 * as well as the light position ( NEW ) 
	 */
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};//0.5f, 0.5f, 0.5f, 1.0f
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};//1.0f, 1.0f, 1.0f, 1.0f
	private float[] lightPosition = {0.0f, -100.0f, 0.0f, 1.0f};//0.0f, 0.0f, 2.0f, 1.0f
		
	/* The buffers for our light values ( NEW ) */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;
	
	/* Variables and factor for the input handler */
	private float oldX;
    private float oldY;
    float oldDist=0,newDist=0;
	private final float TOUCH_SCALE = 0.2f;			//Proved to be good for normal rotation
	public Test03(Context context)
	{
		this.context=context;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);
		
		shapes = new Shape[shapeCount];
		dataFileName = new String[shapeCount];
		dataFileName[0] = testFilename;
		dataFileName[1] = "Simba02.data";
		for(int i=0;i<shapeCount;i++){
			LoadObjData loadObjData=new LoadObjData(context,dataFileName[i]);
			ObjData objData=loadObjData.getObjData();
			shapes[i]=new Shape(objData.getVertices(),objData.getTextures(),objData.getNormals(),objData.getIndices());
		}
	}
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//And there'll be light!
		//Setup The Ambient Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);		//Setup The Diffuse Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);	//Position The Light ( NEW )
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHTING);
		
		shapes[0].loadTexture(gl, context,testTexture);
		shapes[1].loadTexture(gl, context,R.raw.simba2);
		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}
	
	public void onDrawFrame(GL10 gl)
	{
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();				//Reset The Current Modelview Matrix
		//Drawing
		gl.glTranslatef(0.0f+xmove, 0.0f-ymove, -7.0f);	//Move down 1.0 Unit And Into The Screen 7.0
		gl.glScalef(0.02f-zmove, 0.02f-zmove, 0.02f-zmove);
		//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		for(int i=0;i<shapeCount;i++){
			shapes[i].draw(gl);
		}
		
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if(height == 0) //Prevent A Divide By Zero By
		{ 						
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix

	}
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		float x = event.getX(0);
        float y = event.getY(0);
        int pointerCount = event.getPointerCount();
        if(pointerCount==2)
        	newDist = spacing(event);
        switch(event.getAction() ) 
        {
        	case MotionEvent.ACTION_DOWN:
        		break;
        	case MotionEvent.ACTION_MOVE:
        		float dx = x - oldX;
    	        float dy = y - oldY;
        		switch(pointerCount)
        		{
        			case 1:
        				if(lock<1){
        					lock = 0;
        					xrot += dy * TOUCH_SCALE;
        					yrot += dx * TOUCH_SCALE;
        				}
        				break;
        			case 2:    
        				if(lock<2){
        					lock = 1;
        					zmove -=(newDist-oldDist)*0.0001;
        					zmove = Math.min(0.02f, zmove);
        				}
        				break;
        			case 3:
        				if(lock<3){
        					lock = 2;
        					xmove += dx * TOUCH_SCALE * 0.016;
        					ymove += dy * TOUCH_SCALE * 0.016;
        				}
        				break;
        		}
        		break;
        	case MotionEvent.ACTION_UP:
        		lock = 0;
        		break;
        }
        if(event.getPointerCount()==2)
        	oldDist = spacing(event);
        oldX = x;
        oldY = y;
		return true;
	}
	private float spacing(MotionEvent event) { 
		float x = event.getX(0) - event.getX(1); 
		float y = event.getY(0) - event.getY(1); 
		return FloatMath.sqrt(x * x + y * y); 
	}
	
}
