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
		String filename = "Simba01";
		int mode = ObjModel.VERTEX_VT_N;
		//把檔名跟mode打上去就就能直接使用了
		
		File dataFile=new File(System.getProperty("user.dir"),filename+".obj");
		ObjModel obj=new ObjModel(dataFile,mode);
		
		float[] vertices=obj.getVertices();
		float[] textures=obj.getTextures();
		float[] normals=obj.getNormals();
		short[] indices=obj.getIndices();
		ObjData data1=new ObjData();
		data1.setVertices(vertices);
		data1.setTextures(textures);
		data1.setNormals(normals);
		data1.setIndices(indices);
		//System.out.println("textures:"+Arrays.toString(textures));
		//System.out.println("textures:"+textures.length);
		//System.out.println(Arrays.toString(textures));
		//System.out.println(Arrays.toString(normals));
		//System.out.println(Arrays.toString(indices));
		File data=new File(System.getProperty("user.dir"),filename+".data");
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
