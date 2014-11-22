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
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class ActionFragment extends Fragment 
{
	String URL=Constants.getUrl();	
	ActionAdapter adapter=null;
    
	int listItem=0;
	
	public ActionFragment() 
	{
		
	}

 
	public void getActionDataAjax(final View view)
	{
		AQuery aq=new AQuery(this.getActivity());
		
	    String url=URL+"mobile_getaction";     		    												   
	    

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("no",listItem);
		
	    aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() {

	        @Override
	        public void callback(String url, JSONObject data, AjaxStatus status) 
	        {	               
	    
	        	    getActionData(data,view);
	        	    listItem+=30;
	        	
	        }
	    });		
	}
	
	public void getActionData(JSONObject data,View view)
	{
		
        final ListView listView=(ListView)view.findViewById(R.id.actionListView);
		
		final ProgressBar loading=(ProgressBar)view.findViewById(R.id.actionLoading);
		
		 if(data.optString("ok").equals("true"))
    	 {
    		 
	        ArrayList<Integer> uid=new ArrayList<Integer>();
        	ArrayList<Integer> cid=new ArrayList<Integer>();
        	ArrayList<String> des=new ArrayList<String>();
        	ArrayList<String> name=new ArrayList<String>();
        	ArrayList<String> title=new ArrayList<String>();
        	ArrayList<String> picUrl=new ArrayList<String>();
        	ArrayList<String> type=new ArrayList<String>();
        	ArrayList<String> time=new ArrayList<String>();
        	
        		 
        		   try
        		   { 
        			  JSONArray action;
  		        	  JSONArray author;	 
  		        	  JSONArray content;
                      JSONArray pic;
                      
  		        	  action=data.getJSONArray("action");
  		        	  author=new JSONArray(data.optString("author"));  		        	 
  		        	  content=new JSONArray(data.optString("content"));
  		        	  pic=new JSONArray(data.optString("pic"));
  		        	  
  		        	  for(int i=0;i<action.length();i++)
  		        	  {
  		        		  JSONObject ob=(JSONObject) action.get(i);
  		        		  String tp=ob.optString("type");
  		        	              		  
  		        		  time.add(ob.optString("created_at"));
  		        		  
  		        		  if(tp.equals("BB new"))        			  
  		        		  {
  		        			  des.add("Posted a new Blogbook");
  		        			type.add("blogbook");
	  		        		  
  		        		  }
  		        		  else if(tp.equals("BB new chapter"))
  		        			{
  		        			  des.add("Posted a new Chapter in Blogbook");
  		        			type.add("blogbook");
  		        			}
  		        		  else if(tp.equals("C new"))
  		        		  {
  		        			  des.add("Posted a new Collaboration");
  		        		  type.add("collaboration");
  		        		  }
  		        		  else if(tp.equals("C new chapter"))
  		        		  {
  		        			  des.add("Posted a new Chapter in Collaboration");
  		        		  type.add("collaboration");
  		        		  }
  		        		  else if(tp.equals("C req"))
  		        		  {
  		        			  des.add("now Collaborating to Collaboration");
  		        		  type.add("collaboration");
  		        		  }
  		        		  else if(tp.equals("M new"))
  		        		  {
  		        			  des.add("Uploaded a new Media");
  		        		  type.add("media");
  		        		  }
  		        		  else if(tp.equals("R new"))
  		        		  {
  		        			  des.add("Uploaded a new Resource");
  		        		  type.add("resource");
  		        		  }
  		        	     else if(tp.equals("E new"))
  		        	     {
  		        	    	 des.add("is Hosting a new Event");
  		        	     type.add("event");
  		        	     }
  		        	      else if(tp.equals("P new"))		
  		        	      {
  		        	    	  des.add("Posted a new Poll");
  		        	          type.add("poll");
  		        	      }
  		        	      else if(tp.equals("Q new"))
  		        	      {
  		        	    	  des.add("Posted a new Quiz");
  		        	          type.add("quiz");
  		        	      }
  		        	      else if(tp.equals("Diary new"))
  		        	      {
  		        	    	  des.add("made a new Post in Diary");
  		        	    	  type.add("diary");
  		        	      }
  		        	      else if(tp.equals("Recco new"))
  		        	      {
  		        	    	  des.add("Recommends");
  		        	    	  type.add("recco");
  		        	      }
  		        	      else if(tp.equals("A new"))
  		        	      {
  		        	    	  des.add("Posted a new Article");
  		        	    	  type.add("article");
  		        	      }
  		        	      else if(tp.equals("Q score"))
  		        	      {
  		        	    	  des.add("Earned "+ob.optInt("user2id")+" by taking the Quiz ");
  		        	    	  type.add("quiz");
  		        	      }
  		    
  		        		  
  		        		  uid.add(ob.optInt("user1id"));
  		        		  cid.add(ob.optInt("contentid"));
  		        		  
  		        		  ob=(JSONObject)author.get(i);
  		        		  name.add(ob.optString("first_name")+" "+ob.optString("last_name"));
  		        		
  		        		  ob=(JSONObject)content.get(i);
  		        		  
  		        		  if(tp.equals("P new"))
  		        	      title.add(ob.optString("question"));
  		        		  else
  		        		  title.add(ob.optString("title"));		  		    
  		        		  
  		        		  
  		        		  picUrl.add(((String)pic.get(i)).replaceAll("b2.com", Constants.getDomainName()));
  		        	  
  		        	  }
        		   }
        		   catch(Exception e)
        		   {
        			   e.printStackTrace();
        		   }
            	 
        		   
                   loading.animate().alpha(0).setDuration(300);
                   
                   if(adapter==null)
                   {

                       adapter=new ActionAdapter(uid,cid,des,name,title,picUrl,type,time,ActionFragment.this.getActivity(),view);
    		        	 
    		        	
    		        	 SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
    		        	 animationAdapter.setAbsListView(listView);
    		        	 listView.setAdapter(animationAdapter);       
                   }
                   else
                   {
                	   adapter.addData(uid,cid,des,name,title,picUrl,type,time);
                	   adapter.notifyDataSetChanged();
                	   
                   }  
        	 
        	 
    	 }
    	 else
    	 {
    		 Log.e("bbarters", data.optString("ok"));
    	 }

		 data=null;
		
	}
	
	public static void showMsg(String string,Context con)
	{
		Toast.makeText(con, string,Toast.LENGTH_SHORT).show();
	}
	
  
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{	
		if(adapter!=null)
		{
			if(adapter.getAllUserId()!=null)
			{
		 	outState.putIntegerArrayList("userId", adapter.getAllUserId());
	      	outState.putIntegerArrayList("contentId", adapter.getAllContentId());
	      	outState.putStringArrayList("description", adapter.getAllDescription());
	      	outState.putStringArrayList("names", adapter.getAllNames());
	      	outState.putStringArrayList("picUrl", adapter.getAllPicUrl());
	      	outState.putStringArrayList("times", adapter.getAllTimes());
	      	outState.putStringArrayList("title", adapter.getAllTitle());
	      	outState.putStringArrayList("type", adapter.getAllType());
			}	
		}
	     
	      	super.onSaveInstanceState(outState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_action, container, false);
	
final ListView listView=(ListView)view.findViewById(R.id.actionListView);
		
		final ProgressBar loading=(ProgressBar)view.findViewById(R.id.actionLoading);
			                   
	   if(savedInstanceState!=null)
	   {
		   	ArrayList<Integer> uid;
        	ArrayList<Integer> cid;
        	ArrayList<String> des;
        	ArrayList<String> name;
        	ArrayList<String> title;
        	ArrayList<String> picUrl;
        	ArrayList<String> type;
        	ArrayList<String> time;
     
        	uid=savedInstanceState.getIntegerArrayList("userId");
        	cid=savedInstanceState.getIntegerArrayList("contentId");
        	des=savedInstanceState.getStringArrayList("description");
        	name=savedInstanceState.getStringArrayList("names");
        	title=savedInstanceState.getStringArrayList("title");
        	picUrl=savedInstanceState.getStringArrayList("picUrl");
           type=savedInstanceState.getStringArrayList("type");
           time=savedInstanceState.getStringArrayList("times");
           
           loading.setAlpha(0);
           
	        		   adapter=new ActionAdapter(uid,cid,des,name,title,picUrl,type,time,ActionFragment.this.getActivity(),view);
		        	 
		        	
		        	 listView.setAdapter(adapter);
		
	   }
	   else
	   {

			getActionDataAjax(view);	

	   }
		   

		
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
				arrayAdapter.add("Open Profile");
				
				builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
					  if(which==0)
					  {
						  
						  showMsg(adapter.getUserId(position)+"",getActivity());
						  Intent intent=new Intent();
						  intent.setClass(getActivity(), ProfilePage.class);
						  intent.putExtra("id", adapter.getUserId(position));
						  startActivity(intent);
						  
					  }
					}
				});
			
				builder.show();
				return false;
			}
		});
	
		listView.setDividerHeight(3);
		return view; 
	}

		
	


class ActionAdapter extends BaseAdapter
{
	
	ArrayList<Integer> userid;
	ArrayList<String> des;
	ArrayList<Integer> contentid;
	LayoutInflater inflater;
	ArrayList<String> names;
	ArrayList<String> title;
	ArrayList<String> picUrl;
	ArrayList<String> type;
	ArrayList<String> times;
	

	
	Context context;
	
	String StoragePath;
	View view;
	
	public ArrayList<String> getAllTimes()
	{
		return times;
	}
	
	public ArrayList<String> getAllType()
	{
		return type;
	}
	
	public ArrayList<String> getAllPicUrl()
	{
		return picUrl;
	}
	
	public ArrayList<String> getAllNames()
	{
		return names;
	}
	public ArrayList<String> getAllTitle()
	{
		return title;
	}
	
	public ArrayList<String> getAllDescription()
	{
	   return des;	
	}
	
	public ArrayList<Integer> getAllContentId()
	{
       return contentid;
	}
	public ArrayList<Integer> getAllUserId()
	{
	   return userid;	
	}
	
	
	public void addData(ArrayList<Integer> uid,ArrayList<Integer> cid,ArrayList<String> t,ArrayList<String> f,ArrayList<String> ti,ArrayList<String> pic,ArrayList<String> typ,ArrayList<String> tim)
	{
	    userid.addAll(uid);
	    contentid.addAll(cid);
	    des.addAll(t);
	    names.addAll(f);
	    title.addAll(ti);
	    picUrl.addAll(pic);
	    type.addAll(typ);
	    times.addAll(tim);
	}
	
	public ActionAdapter(ArrayList<Integer> uid,ArrayList<Integer> cid,ArrayList<String> t,ArrayList<String> f,ArrayList<String> ti,ArrayList<String> pic,ArrayList<String> typ,ArrayList<String> tim,Context con,View v)
	 {
		 userid=uid;
		 contentid=cid;
		 des=t;
		 names=f;
		 title=ti;
		 inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 picUrl=pic;
		 type=typ;
		 times=tim;
		 
		 context=con;
			
		view=v;
		
		StoragePath=Constants.getStoragePathUser(con);	
		
		
	 }
	 
	@Override
	public int getCount() 
	{
		return userid.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return contentid.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	
	private  class ViewHolder 
	{

		  public TextView name;		   
		  public ImageView image;  	
		  public TextView content;  
		  public TextView time;  
		  
		  public ViewHolder(TextView n,TextView c,TextView t,ImageView i)
		    {
		       content=c;
		       name=n;
		       image=i;
		       time=t;
		    }
		    
	}
		
	int getContentId(int position)
	{
		return contentid.get(position);
	}
	
	int getUserId(int position)
	{
		return userid.get(position);
	}
	
	String getType(int position)
	{
		return type.get(position);
	}
	
	
	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) 
	{
		TextView content;
		ImageView image;
		TextView name;
		TextView time;
	
		View.OnClickListener profileListener=new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {

	             Intent intent=new Intent();
	             intent.setClass(context, ProfilePage.class);
	             intent.putExtra("id",userid.get(position));
	             context.startActivity(intent);
			}
		};
		
		
		View.OnClickListener previewListener=new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Constants.showMsg(type.get(position)+contentid.get(position), context);
				
	             Intent intent=new Intent();
	             intent.setClass(context, PreviewPage.class);
	             intent.putExtra("type",type.get(position));
	             intent.putExtra("contentid",contentid.get(position) );
	             context.startActivity(intent);
	             
			}
		};
			
		if(arg1==null)
		{
			arg1=inflater.inflate(R.layout.adapter_action, arg2,false);

		    content=(TextView)arg1.findViewById(R.id.content);
		    image=(ImageView)arg1.findViewById(R.id.image);
			 name=(TextView)arg1.findViewById(R.id.name);
			 time=(TextView)arg1.findViewById(R.id.time);
	
			 
			 arg1.setTag(new ViewHolder(name,content,time,image));		
			
		}
		else
		{
			ViewHolder vh = (ViewHolder) arg1.getTag();		    
			content=vh.content;
			image=vh.image;
			name=vh.name;
			time=vh.time;
		}
		
		image.setOnClickListener(profileListener);
        name.setOnClickListener(profileListener); 
        
		content.setOnClickListener(previewListener);
		
		name.setText(names.get(position));
		time.setText(times.get(position));
		
		content.setText(des.get(position)+"\n"+title.get(position));
		
		
	   File file=new File(StoragePath,userid.get(position)+"");
		
		if(file.exists())
		{
			Picasso.with(context).load(file).resize(90, 90).into(image);
		}
		else
		{
			Picasso.with(context).load(picUrl.get(position)).resize(90, 90).into(image);
			WriteToStorage storage=new WriteToStorage(file,picUrl.get(position));
			storage.execute();
		}
		
		
		if(position==names.size()-1)
		{
			
			Constants.showMsg("last list",context);
			getActionDataAjax(view);
		}
		
		return arg1;

	}
	

	
}


class WriteToStorage extends AsyncTask<String,String,String>
{

File file;

Bitmap bitmap;

String url;

public WriteToStorage(File f,String u)
{
file=f;
url=u;

}

	@Override
	protected String doInBackground(String... params) 
	{
		bitmap=Constants.getBitmapFromURL(url);
		Constants.writeBmpToFile(file, bitmap);
		
		return null;
	}
	

	
	@Override
	protected void onPostExecute(String result) 
	{
		
		super.onPostExecute(result);
	}
	
	
}
	
	
	
}



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
