package com.bbarters.bbartersmobile;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {

	private Context mContext;
	ArrayList<Integer> reads;
	ArrayList<String> title;
	ArrayList<String> picUrl;
	ArrayList<String> by;
	
	LayoutInflater inflater;
	
	
	
    public CustomGrid(Context c, ArrayList<String>picUrl, ArrayList<Integer>reads, ArrayList<String>title, ArrayList<String>by ) 
    {
        mContext = c;
    	this.reads=reads;
    	this.title=title;
    	this.picUrl=picUrl;
    	this.by=by;
    	
        inflater = (LayoutInflater) mContext
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    }
	@Override
	public int getCount() 
	{
		return title.size();
	}

	
	
	@Override
	public Object getItem(int position) 
	{
		
		return title.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}

	@Override
	public View getView(int position, View grid, ViewGroup parent) 
	{
		
	          if (grid == null) 
	          {	           
	            grid = inflater.inflate(R.layout.staggered_grid_item,parent, false);	                    
	          }
	         
	            ImageView ivPic = (ImageView)grid.findViewById(R.id.grid_pic);
	            TextView tvReads=(TextView)grid.findViewById(R.id.grid_reads);
	            TextView tvTitle=(TextView)grid.findViewById(R.id.grid_title);
	            TextView tvBy=(TextView)grid.findViewById(R.id.grid_by);
	            
	            Picasso.with(mContext).load(picUrl.get(position)).resize(150,150).into(ivPic);
	            
	            tvReads.setText(Integer.toString(reads.get(position)));
	            tvTitle.setText(title.get(position));
	            tvBy.setText(by.get(position));
	            
	            Log.e("bbarters my reading", picUrl.get(position));
	   
	            return grid;
	}

}
