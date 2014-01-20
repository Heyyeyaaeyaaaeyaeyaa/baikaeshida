package com.test.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ObjModel
{
	private float[] vertices;
	private float[] textures;
	private float[] normals;
	private short[] indices;
	public static final int VERTEX_ONLY=0;
	public static final int VERTEX_VT=1;
	public static final int VERTEX_N=2;
	public static final int VERTEX_VT_N=3;
	public ObjModel(File data,int mode)
	{
		this.loadData(data,mode);
	}
	public void loadData(File data,int mode)
	{
		InputStream input;
		BufferedReader reader;
		try
		{
			ArrayList<String> verticeLines = new ArrayList<String>();
			ArrayList<String> textureLines = new ArrayList<String>(); 
			ArrayList<String> normalLines = new ArrayList<String>();
			ArrayList<String> verticeIndiceLines = new ArrayList<String>();
			ArrayList<String> textureIndiceLines = new ArrayList<String>();
			ArrayList<String> normalIndiceLines = new ArrayList<String>();
			input=new FileInputStream(data);
			reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while((line = reader.readLine()) != null) 
			{
				//如果是注解或空白繼續跳過
				if(line.startsWith("//") ||line.startsWith("#")|| line.trim().equals("")) 
				{
					continue;
				}
				String SPACE=" ";
				String SLASH="/";
				StringTokenizer st=new StringTokenizer(line,SPACE);
				String lineType = st.nextToken();
				if(lineType.equals("v"))
				{//頂點座標
					verticeLines.add(st.nextToken());
					verticeLines.add(st.nextToken());
					verticeLines.add(st.nextToken());
				}
				else if(lineType.equals("vt"))
				{//頂點材質座標
					textureLines.add(st.nextToken());
					textureLines.add(st.nextToken());
					//System.out.println("TXXXXXXXXXXXXXX");
				}
				else if(lineType.equals("vn"))
				{//頂點法向量
					normalLines.add(st.nextToken());
					normalLines.add(st.nextToken());
					normalLines.add(st.nextToken());
				}
				else if(lineType.equals("f"))
				{//三角形對應，頂點、材質、法向量
					//三組座標
					int tokenCount = st.countTokens();
					String v;
					StringTokenizer st_temp;
					
					switch(mode){
					case VERTEX_ONLY:
						
						for(int i=0;i<tokenCount;i++){
							String str = st.nextToken();
							//System.out.println(str);
							verticeIndiceLines.add(str);
						}
						break;
					case VERTEX_VT:
						
						for(int i=0;i<tokenCount;i++){
							v = st.nextToken();
							st_temp = new StringTokenizer(v,SLASH);
							
							verticeIndiceLines.add(st_temp.nextToken());
							textureIndiceLines.add(st_temp.nextToken());
						}
						break;
					case VERTEX_N:
						for(int i=0;i<tokenCount;i++){
							v = st.nextToken();
							st_temp = new StringTokenizer(v,SLASH);
							
							verticeIndiceLines.add(st_temp.nextToken());
							normalIndiceLines.add(st_temp.nextToken());
						}
						break;
					case VERTEX_VT_N:
						for(int i=0;i<tokenCount;i++){
							v = st.nextToken();
							st_temp = new StringTokenizer(v,SLASH);
							
							verticeIndiceLines.add(st_temp.nextToken());
							textureIndiceLines.add(st_temp.nextToken());
							normalIndiceLines.add(st_temp.nextToken());
						}
						break;
					default:
						break;
				}
	
				}
			}
			//System.out.println("textureLines:"+textureLines.toString());
			//System.out.println("textureIndiceLines:"+textureIndiceLines.toString());
			//頂點座標:3個值為一個頂點
			int indicesSize=verticeIndiceLines.size();
			indices=new short[indicesSize];
			int verticeSize=indicesSize*3;
			vertices=new float[verticeSize];
			//材質座標:除3找出幾個頂點，乘2有兩個座標
			int textureSize=indicesSize*2;
			textures=new float[textureSize];
			//法向量:每個頂點都有一組法向量，乘3有3個方向向量
			int normalSize=indicesSize*3;
			normals =new float[normalSize];
			for(int i=0;i<verticeIndiceLines.size();i++)
			{
				//頂點
				indices[i]=(short)i;
				int indice=Integer.valueOf(verticeIndiceLines.get(i))-1;
				//頂點座標
				vertices[i*3]=Float.valueOf(verticeLines.get(indice*3));
				vertices[i*3+1]=Float.valueOf(verticeLines.get(indice*3+1));
				vertices[i*3+2]=Float.valueOf(verticeLines.get(indice*3+2));
				
				
				//材質點
				if(!textureIndiceLines.isEmpty()){
				int textureIndice=Integer.valueOf(textureIndiceLines.get(i))-1;
				textures[i*2]=Float.valueOf(textureLines.get(textureIndice*2));
				textures[i*2+1]=Float.valueOf(textureLines.get(textureIndice*2+1));
				}
				//法向量座標
				if(!normalIndiceLines.isEmpty()){
				int normalIndice=Integer.valueOf(normalIndiceLines.get(i))-1;
				normals[i*3]=Float.valueOf(normalLines.get(normalIndice*3));
				normals[i*3+1]=Float.valueOf(normalLines.get(normalIndice*3+1));
				normals[i*3+2]=Float.valueOf(normalLines.get(normalIndice*3+2));    
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public float[] getVertices()
	{
		return vertices;
	}
	public float[] getTextures()
	{
		return textures;
	}
	public float[] getNormals()
	{
		return normals;
	}
	public short[] getIndices()
	{
		return indices;
	}
	
}
