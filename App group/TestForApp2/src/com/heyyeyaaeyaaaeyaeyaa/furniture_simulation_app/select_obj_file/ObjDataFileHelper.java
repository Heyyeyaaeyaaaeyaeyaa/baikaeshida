package com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.select_obj_file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.shared.Singleton;

import android.util.Log;

public class ObjDataFileHelper {
	private Singleton singleton = Singleton.getSharedInstance();
	private int objDataFileCount = 0;
	private float objInitScale;
	private File objHeaderFile = null;
	private String[] objDataFileNameList = null;
	
	public ObjDataFileHelper(){
		getHeaderFile();
		getHeaderFileInfo();
	}
	
	private void getHeaderFileInfo() {
		try {
			Scanner sc = new Scanner( objHeaderFile );
			objDataFileCount = sc.nextInt();
			objDataFileNameList = new String[objDataFileCount];
			sc.nextLine();
			for( int i = 0; i < objDataFileCount; i++)
			{
				objDataFileNameList[i] = sc.nextLine();
			}
			objInitScale = sc.nextFloat();
			sc.close();
		} catch (FileNotFoundException e) {
			Log.e("Log","X"+e);
		}
		
	}

	private void getHeaderFile() {
		String objImageFileName = singleton.getChoosenObjImageFileName(); 
		String[] temp = objImageFileName.split("[.]");
		String objHeaderFileName = temp[0] + ".hdr";
		String path = singleton.getObjLoadPath() + "/ObjHeader";
		objHeaderFile = new File( path + "/" + objHeaderFileName );
	}

	public int getObjDataFileCount() {
		return objDataFileCount;
	}
	public String[] getObjDataFileNameList() {
		return objDataFileNameList;
	}
	public float getObjInitScale() {
		return objInitScale;
	}
	
}
