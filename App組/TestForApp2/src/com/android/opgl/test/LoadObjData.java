package com.android.opgl.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import com.test.obj.ObjData;
import android.content.Context;
import android.util.Log;


public class LoadObjData
{
	private ObjData objData;
	public LoadObjData(Context context,String fileName)
	{
		ObjectInputStream objIn=null;
		objData=null;
		try
		{
			objIn=new ObjectInputStream(new BufferedInputStream(context.getAssets().open(fileName)));
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
