package com.example.testforapp2;

import android.hardware.SensorEvent;

/*
 * ㄏノよ猭
 * newン
 * SensorEventListenerい
 * onSensorChanged(SensorEvent event)
 * set value[] and set orientation
 * 礛㊣computeAngle() ウ穦肚à*/
public class AngleManager {
	
	private float[] value;
	private int orientation; //も诀よ
	private float gOfZeroPointOneAngle; //à–ど0.1   どぶ硉
	private float gOfAngle[]; //硉 0 ~ 9.8 ぇ丁  だΘ901计 ㄤだà
	private float angle; //à
	
	public AngleManager(){
		
		gOfZeroPointOneAngle = (float) (9.8/900);	
		gOfAngle = new float[901];
		gOfAngle[0] = 0;
		
		for(int i=1; i<901; i++)
			gOfAngle[i] = gOfAngle[i-1] + gOfZeroPointOneAngle;
	}
	
	public float computeAngle(){
		
		if(orientation == 0)
			computeAngleOfOrientation0();
		else if(orientation == 1)
			computeAngleOfOrientation1();
		else if(orientation == 2)
			computeAngleOfOrientation2();
		else 
			angle = 0;
		return angle;
		
	}
	
	private void computeAngleOfOrientation0(){
		
		int i=0;
		while(i<900){	
			if( gOfAngle[i] <= value[1] && value[1] < gOfAngle[i+1]){ //程穦 89.9 ~ 90.0
				angle = (float) (i*(0.1));
				break;
			}
			else
				i++;
		}
		//耞箂翴
		if(value[2] < 0)
			angle = 90;
		else if(value[1] < 0)
			angle = 0;
	}
	
	private void computeAngleOfOrientation1(){
		
		int i=0;
		while(i<900){	
			if( gOfAngle[i] <= value[0] && value[0] < gOfAngle[i+1]){ //程穦 89.9 ~ 90.0
				angle = (float) (i*(0.1));
				break;
			}
			else
				i++;
		}
		
		if(value[2] < 0)
			angle = 90;
		else if(value[0] < 0)
			angle = 0;
	}
	
	private void computeAngleOfOrientation2(){

		int i=0;
		while(i<900){	
			if( gOfAngle[i] <= Math.abs(value[0]) && Math.abs(value[0]) < gOfAngle[i+1]){ //程穦 89.9 ~ 90.0
				angle = (float) (i*(0.1));
				break;
			}
			else
				i++;
		}
		
		if(value[2] < 0)
			angle = 90;
		else if(value[0] > 0)
			angle = 0;
	}
	
	public void setValue(float[] value){
		this.value = new float[3];
		this.value[0] = value[0];
		this.value[1] = value[1];
		this.value[2] = value[2];
	}
	
	public void setOrientation(int orientation){
		this.orientation = orientation;
	}
}
