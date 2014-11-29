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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class ReadingFragment extends Fragment 
{
	String URL=Constants.getUrl();
	ArrayList<Integer> contentId;
    ArrayList<String> picUrl;
    
	public ReadingFragment() 
	{
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{	
			View view=inflater.inflate(R.layout.fragment_reading, container, false);
			final StaggeredGridView gridView=(StaggeredGridView)view.findViewById(R.id.staggeredGridView1);
			AQuery aq=new AQuery(this.getActivity());
			
	    	String url=URL+"mobile_myreadings";
	    	Map<String, Object> params = new HashMap<String, Object>();
	    	SharedPreferences auth = this.getActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE);
		    params.put("id",auth.getInt("id",0));       		   		    	
		    
	       	aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() 
      		{
	    		public void callback(String url, JSONObject data, AjaxStatus status) 
	    		{
	    			if(data.optString("ok").equals("true"))
	    			{
	    				
	    				try
	        		   	{
	    					//gjkhgj
	    					contentId=new ArrayList<Integer>();    					
	    					ArrayList<Integer> reads=new ArrayList<Integer>();
	    					ArrayList<String> title=new ArrayList<String>();
	    			        picUrl=new ArrayList<String>();
	    					ArrayList<String> by=new ArrayList<String>();
	    					
	    					JSONArray titleArray;
	    					JSONArray byArray;
	    					JSONArray picArray;
	    					
	    					String[] itemsReads = data.optString("reads").replaceAll("\\[", "").replaceAll("\\]", "").split(",");

	    					String[] cId = data.optString("contentid").replaceAll("\\[", "").replaceAll("\\]", "").split(",");

	    					
	    					titleArray=new JSONArray(data.optString("title"));  		        	 
	    					byArray=new JSONArray(data.optString("by"));	    					
	    					picArray=new JSONArray(data.optString("pic"));
		        	        
	    					for(int i=0;i<titleArray.length();i++)
		        	        {
		  		        		reads.add(Integer.parseInt(itemsReads[i]));
		  		        		title.add(titleArray.get(i).toString());
		  		        		JSONObject ob=(JSONObject)byArray.get(i);
		  		        		by.add(ob.optString("first_name")+ob.optString("last_name"));
		  		        		picUrl.add(URL+((String)picArray.get(i)));
		        	            contentId.add(Integer.parseInt(cId[i]));
		        	        }
		        	        
		        	        CustomGrid adapter = new CustomGrid(ReadingFragment.this.getActivity(), picUrl,reads,title,by);
		        		    gridView.setAdapter(adapter);
	        		   	}
	    				catch(Exception e)
	    				{
	        			   e.printStackTrace();
	    				}
	    			}
	    			else
		        	 {
		        		 Log.e("bbarters", data.optString("ok"));
		        	 }
	    		}
	    	});		
	    
	    gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
	             
					

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
				
						Log.e("bbarters reading fragment onclick", picUrl.get(position) +"   "+contentId.get(position));
						Intent intent=new Intent();
						intent.setClass(getActivity(), ABCReading.class);
						intent.putExtra("type",Constants.getType(picUrl.get(position)));
						intent.putExtra("contentId",contentId.get(position));
						startActivity(intent);
					
					}
	            });
		return view;
	}

}
