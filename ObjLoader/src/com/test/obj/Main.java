package com.test.obj;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File dataFile=new File(System.getProperty("user.dir"),"Simba01.obj");
		ObjModel obj=new ObjModel(dataFile);
		float[] vertices=obj.getVertices();
		float[] textures=obj.getTextures();
		float[] normals=obj.getNormals();
		short[] indices=obj.getIndices();
		ObjData data1=new ObjData();
		data1.setVertices(vertices);
		data1.setTextures(textures);
		data1.setNormals(normals);
		data1.setIndices(indices);
		System.out.println("textures:"+Arrays.toString(textures));
		//System.out.println(Arrays.toString(textures));
		//System.out.println(Arrays.toString(normals));
		//System.out.println(Arrays.toString(indices));
		File data=new File(System.getProperty("user.dir"),"Simba01.data");
		try
		{
			ObjectOutputStream objOut=null;
			objOut=new ObjectOutputStream(new BufferedOutputStream(new  FileOutputStream(data)));
			objOut.writeObject(data1);
			objOut.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
