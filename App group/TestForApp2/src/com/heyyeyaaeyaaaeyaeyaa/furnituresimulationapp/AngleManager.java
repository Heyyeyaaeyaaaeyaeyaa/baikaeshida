package com.heyyeyaaeyaaaeyaeyaa.furnituresimulationapp;



public class AngleManager {
	
	private float[] value;
	private float[] angle_rotation;
	private int orientation; //¤â¾?¤è¦V

	private float angle; //¨¤«×
	
	public AngleManager(){
		value = new float[3];
		angle_rotation = new float[3];
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
		
		if(angle_rotation[1] < 0 && angle_rotation[1] > -90)
			angle = Math.abs(angle_rotation[1]);

		if(value[2] < 0)
			angle = 90;
		else if(value[1] < 0)
			angle = 0;	

	}
	
	private void computeAngleOfOrientation1(){
		
		if(angle_rotation[2] < 0 && angle_rotation[2] > -90)
			angle = Math.abs(angle_rotation[2]);

		if(value[2] < 0)
			angle = 90;
		else if(value[0] < 0)
			angle = 0;
	}
	
	private void computeAngleOfOrientation2(){

		if(angle_rotation[2] > 0 && angle_rotation[2] < 90)
			angle = Math.abs(angle_rotation[2]);

		if(value[2] < 0)
			angle = 90;
		else if(value[0] > 0)
			angle = 0;
	}
	
	@SuppressWarnings("unused")
	private void computeAngleOfRotationDirection(){
		
	}
	
	public void setValue(float[] value){
		this.value[0] = value[0];
		this.value[1] = value[1];
		this.value[2] = value[2];
	}
	
	public void setOrientation(int orientation){
		this.orientation = orientation;
	}
	
	public void setAngleRotation(float[] angle_rotation){
		this.angle_rotation[0] = angle_rotation[0];
		this.angle_rotation[1] = angle_rotation[1];
		this.angle_rotation[2] = angle_rotation[2];
	}
}
