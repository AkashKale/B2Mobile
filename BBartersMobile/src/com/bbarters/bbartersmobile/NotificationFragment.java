package com.bbarters.bbartersmobile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class NotificationFragment extends Fragment 
{
	NotificationAdapter adapter;
	String URL=Constants.getUrl();
	int listItem=0;
	
	public NotificationFragment() 
	{
	
	}
	
	
	Integer getIdFromString(String string)
	{
		String integer="";
		
		for(int i=string.length()-1;i>0;i--)
		{
			if(string.charAt(i)=='/')
			{
		      integer=string.substring(i+1,string.length());	
		      break;
			}
		}
		
		return Integer.parseInt(integer);
		
		
	}
	
	
	
	void openProfileFromLink(String string)
	{
		
		String integer="";
		
		for(int i=string.length()-1;i>0;i--)
		{
			if(string.charAt(i)=='/')
			{
		      integer=string.substring(i+1,string.length());	
		      break;
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("name",integer); 
	    
	    String url=URL+"mobile_getIdFromUserName";
	    
	    AQuery aq=new AQuery(getActivity());
	    
		aq.ajax(url,params,String.class, new AjaxCallback<String>() {
		    public void callback(String url, String content, AjaxStatus status) 
		    {
		    	
		    	
		    	Log.e("can you belive this guy", content+"");
	
		    	Intent intent=new Intent();
		    	intent.setClass(getActivity(), ProfilePage.class);
		    	intent.putExtra("id", Integer.parseInt(content));
		    	startActivity(intent);
	            
		    }
	      });
		
		
	}

	String getType(String string)
	{
		String type="";
		
		if(string.toLowerCase().contains("user"))
		{
		    type="user";	
		}
		else if(string.toLowerCase().contains("blogbook"))
		{
			type="blogbook";
		}
		else if(string.toLowerCase().contains("collaboration"))
		{
			type="collaboration";
		}
		else if(string.toLowerCase().contains("quiz"))
		{
			type="quiz";
		}
		else if(string.toLowerCase().contains("article"))
		{
			type="article";
		}
		else if(string.toLowerCase().contains("media"))
		{
			type="media";
		}
		else if(string.toLowerCase().contains("resource"))
		{
			type="resource";
		}
		else if(string.toLowerCase().contains("poll"))
		{
			type="poll";
		}
		else if(string.toLowerCase().contains("recco"))
		{
			type="recco";
		}
		
		return type;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_notification, container,false);
		
	 final ListView list=(ListView)view.findViewById(R.id.notificationListView);
		
	
		
		SharedPreferences auth = this.getActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE);
		
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id",auth.getInt("id", 0)); 
	    params.put("no", listItem);
	    
	    String url=URL+"mobile_getNotification";
	    
	    AQuery aq=new AQuery(getActivity());
	    
		aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() {
		    public void callback(String url, JSONObject content, AjaxStatus status) 
		    {
		    	
		
		    	Log.e("bbarters notification",content.toString());
		    	
		    	
		    	if(content.optString("ok").equals("true"))
		    	{
		    		try
		    		{
			    		
			    		JSONArray data;
			    		JSONArray pic_url;
			    		JSONArray name;
			    		
			    		
			    		ArrayList<String> message=new ArrayList<String>();
			    		ArrayList<String> picUrl=new ArrayList<String>();
			    		ArrayList<String> link=new ArrayList<String>();
			    		
			    		
			    		if(content.optInt("number")!=0)
			    		{
			    			data=new JSONArray(content.optString("data"));
			    			pic_url=new JSONArray(content.optString("pic_url"));
			    			name=new JSONArray(content.optString("names"));
			    			
			    			for(int i=0;i<data.length();i++)
			    			{
			    				JSONObject ob= (JSONObject) data.get(i);
			    				
			    				message.add((String)name.get(i)+" "+ob.optString("message"));
			    				
			    				 link.add(ob.optString("link"));
			    			    
                           
			    				picUrl.add((String)pic_url.get(i));
			    				
			    				
			    			}
			    			listItem+=20;
			    			
			    			adapter=new NotificationAdapter(getActivity(),picUrl,message,link);			    			
			    			 list.setAdapter(adapter);  
			    		}
			    		else
			    		{
			    			Constants.showMsg("No New Notification",getActivity());
			    		}
		    		}
		    		
		    		catch(Exception e)
		    		{
		    			e.printStackTrace();
		    		}
		    		
		    	}
		    	else
		    	{
		    		ActionFragment.showMsg("nothing is here",getActivity());
		    		Log.e("bbarters", content.optString("ok"));
		    	}
		    	
	            
		    }
	      });
		
		list.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
			
		        String type=getType(adapter.links.get(position));
		        
		        
		        
		    if(type.equals("user"))
		    {
		    	openProfileFromLink(adapter.links.get(position));
		    }
		    else 
		    {
		    	showMsg(getType(adapter.links.get(position)));
		    	showMsg(getIdFromString(adapter.links.get(position))+"");
		
		    	Intent intent=new Intent();
		    	intent.setClass(getActivity(), PreviewPage.class);
		    	intent.putExtra("type", getType(adapter.links.get(position)));
		    	intent.putExtra("contentid", getIdFromString(adapter.links.get(position)));
		    	startActivity(intent);		    	
		    }
			  	
			}
		});
		
		return view;
	}
	
	void showMsg(String string)
	{
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
	}

}






class NotificationAdapter extends BaseAdapter
{
	LayoutInflater inflater;
	ArrayList<String> picUrl;
	ArrayList<String> contents;
	ArrayList<String> links;
	Context context;
	String storagePath;
	
	public NotificationAdapter(Context con,ArrayList<String> p,ArrayList<String> c,ArrayList<String> l)
	{
		context=con;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		picUrl=p;
		contents=c;
		links=l;
		storagePath=Constants.getStoragePathUser(con)+"/";		
	}
	
	
	@Override
	public int getCount() 
	{
		
		return contents.size();
	}

	@Override
	public Object getItem(int position) 
	{
		
	return contents.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
	
		return position;
	}

	
	private static class ViewHolder 
	{

		  public TextView content;		   
		  public ImageView image;  	
		 
		  public ViewHolder(TextView c,ImageView i)
		    {
		       content=c; 
		       image=i;		     
		    }
		    
	}
	
	
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) 
	{
		TextView content;
		ImageView image;
		
		
		if(arg1==null)
		{
			arg1=inflater.inflate(R.layout.adapter_notification, arg2,false);

		    content=(TextView)arg1.findViewById(R.id.contentNotification);
		    image=(ImageView)arg1.findViewById(R.id.imageNotification);	
		    arg1.setTag(new ViewHolder(content,image));		
			
		}
		else
		{
			ViewHolder vh = (ViewHolder) arg1.getTag();		    
			content=vh.content;
			image=vh.image;			
		}
		
		content.setText(contents.get(position));			
		
	/*
	  	File file=new File(storagePath+userid.get(position));
		
		if(file.exists())	
		{
			Picasso.with(context).load(file).resize(90, 90).into(image);				
		}
		else
		{
			Bitmap map=Constants.getBitmapFromURL(picUrl.get(position));
			
			Constants.writeBmpToFile(file, map);
			
			Picasso.with(context).load(picUrl.get(position)).resize(90, 90).into(image);
		}
		
	
	*/
		
		Picasso.with(context).load(picUrl.get(position)).resize(90, 90).into(image);
		
		return arg1;
	}
	
	
}
