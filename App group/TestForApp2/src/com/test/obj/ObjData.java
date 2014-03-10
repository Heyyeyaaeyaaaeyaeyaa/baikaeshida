package com.test.obj;

import java.io.Serializable;

public class ObjData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6244395282907135167L;
	private float[] vertices;
	private float[] textures;
	private float[] normals;
	private short[] indices;
	public ObjData()
	{
	}
	public float[] getVertices()
	{
		return vertices;
	}
	public void setVertices(float[] vertices)
	{
		this.vertices = vertices;
	}
	public float[] getTextures()
	{
		return textures;
	}
	public void setTextures(float[] textures)
	{
		this.textures = textures;
	}
	public float[] getNormals()
	{
		return normals;
	}
	public void setNormals(float[] normals)
	{
		this.normals = normals;
	}
	public short[] getIndices()
	{
		return indices;
	}
	public void setIndices(short[] indices)
	{
		this.indices = indices;
	}
	
}
