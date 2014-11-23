package com.bbarters.bbartersmobile;

import java.util.ArrayList;

import com.origamilabs.library.views.StaggeredGridView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {

	private Context mContext;
	ArrayList<Integer> reads;
	ArrayList<Integer> category;
	ArrayList<String> title;
	ArrayList<String> picUrl;
	ArrayList<String> by;
    public CustomGrid(Context c, ArrayList<String>picUrl, ArrayList<Integer>reads, ArrayList<String>title, ArrayList<Integer>category, ArrayList<String>by ) {
        mContext = c;
    	this.reads=reads;
    	this.category=category;
    	this.title=title;
    	this.picUrl=picUrl;
    	this.by=by;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return title.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return title.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View grid, ViewGroup parent) {
		// TODO Auto-generated method stub
	    
	      LayoutInflater inflater = (LayoutInflater) mContext
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	          if (grid == null) 
	          {
	           
	            grid = inflater.inflate(R.layout.staggered_grid_item,parent, false);	           
	           
	            }
	         
	          ImageView ivPic = (ImageView)grid.findViewById(R.id.grid_pic);
	            TextView tvReads=(TextView)grid.findViewById(R.id.grid_reads);
	            TextView tvTitle=(TextView)grid.findViewById(R.id.grid_title);
	            TextView tvCategory=(TextView)grid.findViewById(R.id.grid_category);
	            TextView tvBy=(TextView)grid.findViewById(R.id.grid_by);
	            Picasso.with(mContext).load(picUrl.get(position)).resize(90, 90).into(ivPic);
	            tvReads.setText(reads.get(position));
	            tvTitle.setText(title.get(position));
	            tvCategory.setText(category.get(position));
	            tvBy.setText(by.get(position));
	            
	      return grid;
	}

}
