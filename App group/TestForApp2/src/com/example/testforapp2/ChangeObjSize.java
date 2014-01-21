package com.example.testforapp2;



public class ChangeObjSize {


	private float focalLength;  //焦距
	private int foot;  //腳步數
	private float u, v, magnification;   //物距 像距 縮小倍數
	
	public ChangeObjSize(float focalLength, int foot){
		this.focalLength = focalLength;
		this.foot = foot;
	}

	public void setFocalLength(float focalLength){
		this.focalLength = focalLength;
	}
	
	public void setFoot(int foot){
		this.foot = foot;
	}
	
	public float getMagnification(){
		u = foot * 5000;//50cm
		v = 1/(1/focalLength - 1/u);
		magnification = v/u;
		
		return magnification;
	}
}
