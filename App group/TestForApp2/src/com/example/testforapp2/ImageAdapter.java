package com.example.testforapp2;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private Singleton singleton = Singleton.getSharedInstance();
	private final String IMAGE_DIR = "/ObjPreview";
	
	public Integer[] imgs = {R.drawable.img1 , R.drawable.img2 , R.drawable.img3 ,
			R.drawable.img4 , R.drawable.img5};
	
	public ImageAdapter(Context c){
		mContext = c;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgs[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageview = new ImageView(mContext);  
		//imageview.setImageResource(imgs[position % imgs.length]);
		if((mUrls = getImage())!=null)
			imageview.setImageURI(mUrls[position % mUrls.length]);
		else
			imageview.setImageResource(imgs[0]);
		
        imageview.setLayoutParams(new Gallery.LayoutParams(300, 300));
        imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageview.setBackgroundColor(Color.alpha(1));
        return imageview;
	}
	
	Uri[] mUrls;
	String[] mFiles = null;
	
	public Uri[] getImage(){
		//File images = Environment.getExternalStorageDirectory();
		File images = new File(singleton.getObjLoadPath()+IMAGE_DIR);
		File[] imagelist = images.listFiles(new FilenameFilter(){
			public boolean accept(File dir , String name){
				return(name.endsWith(".jpg")||name.endsWith(".png"));
			}
		});
		
		if(imagelist==null)
			return null;
		
		mFiles = new String[imagelist.length];
		for(int i=0;i<imagelist.length;i++)
			mFiles[i] = imagelist[i].getAbsolutePath();
		
		mUrls = new Uri[mFiles.length];
		for(int i=0;i<mFiles.length;i++)
			mUrls[i] = Uri.parse(mFiles[i]);
		
		return mUrls;
	}

}
