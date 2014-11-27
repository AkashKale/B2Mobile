package com.bbarters.bbartersmobile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.etsy.android.grid.StaggeredGridView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ExploreFragment extends Fragment {

	String URL=Constants.getUrl();
	
	public ExploreFragment() 
	{
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{ 
		View view=inflater.inflate(R.layout.fragment_explore, container, false);
	
		StaggeredGridView gridview=(StaggeredGridView)view.findViewById(R.id.staggeredGridView1);
		
	
		
		AQuery aq=new AQuery(this.getActivity());
		
    	String url=URL+"mobile_explore";
    	Map<String, Object> params = new HashMap<String, Object>();
    	SharedPreferences auth = this.getActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE);
    	params.put("id",auth.getInt("id",0));       		   		    	
    
	    aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() 
  		{
    		public void callback(String url, JSONObject data, AjaxStatus status) 
    		{

    		}
    	});		

		
		return view;
	}

	
	
	class GridAdapter extends BaseAdapter
	{
	  Context context;
	  ArrayList<String> picUrl;
	  ArrayList<String> title;
	  ArrayList<String> author;
	  ArrayList<Integer> contentId;
	  
	  
		
		public GridAdapter()
		{
			
		}

		@Override
		public int getCount() {
		
			return 0;
		}

		@Override
		public Object getItem(int position) 
		{
			return null;
		}

		@Override
		public long getItemId(int position) 
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			return null;
		}
		
		
	}
	

}
