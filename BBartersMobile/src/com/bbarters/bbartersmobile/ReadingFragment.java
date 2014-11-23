package com.bbarters.bbartersmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.origamilabs.library.views.StaggeredGridView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class ReadingFragment extends Fragment {
	String URL=Constants.getUrl();
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
	    	//auth.getInt("id", 0)
		    params.put("id",17);       		   		    	
	       	aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() 
      		{
	    		public void callback(String url, JSONObject data, AjaxStatus status) 
	    		{
	    			if(data.optString("ok").equals("true"))
	    			{
	    				Log.e("bbarters", data.optString("title").toString());
	    				try
	        		   	{
	    					//gjkhgj
	    					ArrayList<Integer> uid=new ArrayList<Integer>();
	    					ArrayList<Integer> reads=new ArrayList<Integer>();
	    					ArrayList<Integer> category=new ArrayList<Integer>();
	    					ArrayList<String> title=new ArrayList<String>();
	    					ArrayList<String> picUrl=new ArrayList<String>();
	    					ArrayList<String> by=new ArrayList<String>();
	    					JSONArray titleArray;
	    					JSONArray byArray;
	    					JSONArray picArray;
	    					String[] itemsReads = data.optString("reads").replaceAll("\\[", "").replaceAll("\\]", "").split(",");
	    					String[] itemsCat = data.optString("category").replaceAll("\\[", "").replaceAll("\\]", "").split(",");
	    					int[] readsArray = new int[itemsReads.length];
	    					int[] categoryArray = new int[itemsCat.length];

	    					for (int i = 0; i < itemsReads.length; i++) {
	    					    try {
	    					        readsArray[i] = Integer.parseInt(itemsReads[i]);
	    					        categoryArray[i]=Integer.parseInt(itemsCat[i]);
	    					    } catch (NumberFormatException nfe) {};
	    					}
	    					titleArray=new JSONArray(data.optString("title"));  		        	 
	    					byArray=new JSONArray(data.optString("by"));	    					
	    					picArray=new JSONArray(data.optString("pic"));
		        	        for(int i=0;i<titleArray.length();i++)
		        	        {
		  		        		reads.add(readsArray[i]);
		  		        		title.add(titleArray.get(i).toString());
		  		        		category.add(categoryArray[i]);
		  		        		JSONObject ob=(JSONObject)byArray.get(i);
		  		        		by.add(ob.optString("first_name")+ob.optString("last_name"));
		  		        		picUrl.add((String)picArray.get(i));
		        	        }
		        	        
		        	        CustomGrid adapter = new CustomGrid(ReadingFragment.this.getActivity(), picUrl,reads,title,category,by);
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
					public void onItemClick(StaggeredGridView parent,
							View view, int position, long id) {
					
						
					}
	            });
		return view;
	}

}
