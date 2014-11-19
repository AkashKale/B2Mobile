package com.bbarters.bbartersmobile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ProfilePage extends Activity 
{

String URL=Constants.getUrl();

	@Override
	protected void onPause() 
	{
		super.onPause();
	
	overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_page);
		
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		
		final TextView tvName=(TextView)findViewById(R.id.name);
      	final ImageView ivDispImg=(ImageView)findViewById(R.id.image);      	 
      	final ImageView coverPic=(ImageView)findViewById(R.id.profile_background);      	 
      	final TextView settings=(TextView)findViewById(R.id.settings);
      	
      	ActionBar action=this.getActionBar();
      	
      	action.hide();
      	
      	AQuery aq=new AQuery(getApplicationContext());
	    
      	String url=URL+"mobile_showProfile";     		    												   
	    
      	final Bundle bundle=getIntent().getExtras();
		 
      	Map<String, Object> params = new HashMap<String, Object>();
	    params.put("id",bundle.getInt("id"));       		   		    	
      	
		 SharedPreferences auth = getSharedPreferences("Auth", Context.MODE_PRIVATE);			          
     	
	    if(bundle.getInt("id")!=auth.getInt("id", 0))
	    {
	    	settings.setVisibility(TextView.INVISIBLE);
	    }
	    
	    
	    aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() {
	    public void callback(String url, JSONObject content, AjaxStatus status) 
	    {
	    	if(content!=null)
	    	{
	    		Log.e("bbarter", content.toString());
	    		
	    		if(content.optString("ok").equals("true"))
		    	  {
		    		
	    			String strName=content.optString("first_name");
			    	strName=strName+" "+content.optString("last_name");
			    	String dpURL=content.optString("profile_pic").replaceAll("b2.com", Constants.getDomainName());
			    	String cp=content.optString("cover_pic").replaceAll("b2.com", Constants.getDomainName());
			   
			    	tvName.setText(strName);
			        
			    	Display display=new Display(dpURL,ivDispImg,coverPic,cp,bundle.getInt("id"));  	
		    	    display.execute();
		    	  }
		    	  else
		    	  {
		    		  Log.e("bb", content.optString("ok"));
		    	  }  
	    	}
	    	else
	    	{
	    		showMsg("nothing is here");
	    	}
            
	    }
      });
		//dispImg.setImageBitmap(getRoundedShape(BitmapDrawable.));

	}


	void showMsg(String string)
	{
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
	}
	

	
	
	
	
	class Display extends AsyncTask<String,String,String>
	{
         String url;
         Bitmap image;
         ImageView iv;
         ImageView coverPic;
         String coverPicString;
         Bitmap coverImage;
         int userid;
         
         public Display(String ur,ImageView i,ImageView cp,String cpstring,int id)   
         {
        	 
        	 coverPicString=cpstring.replaceAll("\\\\", "");
        	 coverPic=cp;
        	 url=ur.replaceAll("\\\\","");
        	 iv=i;
        	 userid=id;
         }
		
		@Override
		protected String doInBackground(String... params) 
		{
			
			File userFile=new File(Constants.getStoragePathUser(getApplicationContext())+"/"+userid);
			File coverFile=new File(Constants.getStoragePathCover(getApplicationContext())+"/"+userid);
			
			if(userFile.exists())
			{
				image=BitmapFactory.decodeFile(userFile.getPath());
				image=Constants.getResizedBitmap(image,230,200);
				image=Constants.getRoundedShape(image);
				image=Constants.imageGlow(image);  
			}
			else
			{
				image=Constants.getBitmapFromURL(url);
				Constants.writeBmpToFile(userFile,image);
				image=Constants.getResizedBitmap(image,230,200);
				image=Constants.getRoundedShape(image);
				image=Constants.imageGlow(image);  
				
			}
			
			
			if(coverFile.exists())
			{
				coverImage=BitmapFactory.decodeFile(coverFile.getPath());
			}
			else
			{	
				coverImage=Constants.getBitmapFromURL(coverPicString);   
				coverImage=Constants.toGrayscale(coverImage);
		    	
				Constants.writeBmpToFile(coverFile, coverImage);
			}
	    
	    	
	    	return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			
		   iv.setBackgroundDrawable(new BitmapDrawable(getResources(),image));	
		   coverPic.setBackgroundDrawable(new BitmapDrawable(getResources(),coverImage));
		   
		   
		   super.onPostExecute(result);
		}
		
		
		
		
		
		
	}
}
