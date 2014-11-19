package com.bbarters.bbartersmobile;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewPage extends Activity {

	String URL=Constants.getUrl();

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_page);
	
		ActionBar bar=this.getActionBar();
		bar.setTitle("");
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(R.layout.actionbar);		
		bar.setDisplayHomeAsUpEnabled(true);		
	
	final	ImageView profileImage=(ImageView)bar.getCustomView().findViewById(R.id.authorImage);
	final	TextView  profileName=(TextView)bar.getCustomView().findViewById(R.id.authorName);	
	
	final	ImageView contentImage=(ImageView)findViewById(R.id.contentImage);
	final	TextView  title=(TextView)findViewById(R.id.title);
	final	TextView  typeText=(TextView)findViewById(R.id.type);
	final	TextView  ifc=(TextView)findViewById(R.id.ifc);
	final	TextView  des=(TextView)findViewById(R.id.des);
	final 	Button    read=(Button)findViewById(R.id.read);
	
		String type=getIntent().getExtras().getString("type");
		int contentid=getIntent().getExtras().getInt("contentid");
		
		typeText.setText(type);
		
		
		final AQuery aq = new AQuery(getApplicationContext());

		String url = URL+"mobile_getPreview";

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentid", contentid);
		params.put("type",type);
		
		aq.ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() 
				{

					@Override
					public void callback(String url,final JSONObject content, AjaxStatus status) 
					{
						
						Log.e("preview", content.toString());
						
						if(content.optString("ok").equals("true"))
						{

							String contentPic=Constants.getUrl()+content.optString("content_pic_url");
							String profilePic=Constants.setReplacement(content.optString("author_pic_url"));
							
							
				           LoadActionbar load=new LoadActionbar(profileImage,profilePic,content.optInt("author_id"));
				           load.execute();
				           
							AQuery a=new AQuery(PreviewPage.this.getApplicationContext());
							a.id(contentImage).image(contentPic);
						    
						     profileName.setText(content.optString("author_name"));
						     title.setText(content.optString("title"));
						     ifc.setText("IFC:"+content.optString("ifc")+" | Readers:"+content.optInt("no_readers"));
						     des.setText(content.optString("desc"));
						
						
						     profileName.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
							   
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), ProfilePage.class);
									intent.putExtra("id",content.optInt("author_id"));
									startActivity(intent);
								}
							});
						     
						     profileImage.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) 
								{
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), ProfilePage.class);
									intent.putExtra("id",content.optInt("author_id"));
									startActivity(intent);
								}
							});
						     
						}
						
					
					}
				});
		
		
		
		read.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), ABCReading.class);
				startActivity(intent);
			}
		});
		
	}
	
	
	class LoadActionbar extends AsyncTask<String,String,String>
	{

		ImageView imageView;
		Bitmap bitmap;
		String url;
		int userid;
		
		public LoadActionbar(ImageView iv,String string,int id)
		{
			url=string;
			imageView=iv;
			userid=id;
			
		}
		
		@Override
		protected String doInBackground(String... params) 
		{
			File file=new File(Constants.getStoragePathUser(getApplicationContext())+"/"+userid);
			
			if(file.exists())
			{
				bitmap=BitmapFactory.decodeFile(file.getPath());	
				bitmap=Constants.getRoundedShape(bitmap);
			}
			else
			{
				 bitmap=Constants.getBitmapFromURL(url);
				 Constants.writeBmpToFile(file, bitmap);
	              bitmap=Constants.getRoundedShape(bitmap);
				
			}
			
             
              
			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
            imageView.setImageBitmap(bitmap);
			super.onPostExecute(result);
		}
		
	}
	
}
