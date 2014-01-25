package com.example.testforapp2;

import android.util.Log;



public class ObjSizeManager {


	private double fix = 0.01*3.14;
	private HeyRenderer heyRenderer;
	
	public ObjSizeManager(HeyRenderer heyRenderer){
		this.heyRenderer = heyRenderer;
	}
	
	private double getMagnification(double focusLength, double distance){
		double magnification;
		double v;

		Log.e("Log","distance:"+distance);
		v = 1/(1/focusLength - 1/distance);
		magnification = distance/v*fix;
		
		return magnification;
	}

	public void changeObjSizeByFoot(float focusLength, int foot) {
		double distance = getDistanceByFoot(foot);
		double magnification = getMagnification(focusLength,distance);
		heyRenderer.changeObjSize(magnification);
	}
	private double getDistanceByFoot(int foot) {
		double distance = foot * 500;//50cm
		if(distance==0)
			return 0;
		Log.e("Log","foot:"+foot);
		return distance;
	}

	public void changeObjSizeByDistance(float focusLength,float angle, float height) {
		double distance = getDistanceByAgrle(angle,height);
		if(distance!=-1){
			double magnification = getMagnification(focusLength,distance);
			heyRenderer.changeObjSize(magnification);
		}else{
			Log.e("Log","-1");
		}
	}
	private double getDistanceByAgrle(float angle, float height) {
		double distance;
		if(Math.cos(angle/180*Math.PI)!=0)
			distance = height/Math.cos(angle/180*Math.PI);
		else
			distance = -1;
		return distance;
	}

	public void initSize(){
		heyRenderer.initSize();
	}
}
