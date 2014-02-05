package com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.camera.glsurface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import android.content.Context;
import android.util.Log;

import com.heyyeyaaeyaaaeyaeyaa.furniture_simulation_app.shared.Singleton;
import com.test.obj.ObjData;

public class ObjDataLoader
{
	private Singleton singleton = Singleton.getSharedInstance();
	private ObjData objData;
	private File dataFile;
	public ObjDataLoader(Context context,String fileName)
	{
		ObjectInputStream objIn=null;
		objData=null;
		dataFile = new File(singleton.getObjLoadPath() + "/ObjData/" + fileName);
		try
		{
			objIn=new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)));
			objData=(ObjData)objIn.readObject();
			objIn.close();
		}
		catch (IOException e)
		{
			Log.v("TEST","IOException:"+e);
		} 
		catch (ClassNotFoundException e)
		{
			Log.v("TEST","ClassNotFoundException:"+e);
		}
	}
	public ObjData getObjData()
	{	
		return objData;
	}
	
}
