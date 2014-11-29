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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
	 AQuery aq;
	
	 int contentid;
	 int authid;
	String type;
	int authorid;
	int quiztime;
	
	@Override
	protected void onDestroy() 
	{
aq.ajaxCancel();
aq=null;
		super.onDestroy();
	}


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
	
		type=getIntent().getExtras().getString("type");
		contentid=getIntent().getExtras().getInt("contentid");
		
		 SharedPreferences auth = getSharedPreferences("Auth",Context.MODE_PRIVATE);
		authid=auth.getInt("id",0);
			
		typeText.setText(type);
		
		
		aq = new AQuery(getApplicationContext());

		String url = URL+"mobile_getPreview";

		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authid", auth.getInt("id",0));
		params.put("contentid", contentid);
		params.put("type",type);
		
		
		
		//authid
		//authorid
		//cost
		//type
		//contentid
		
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
				            authorid=content.optInt("author_id");			
							
				           LoadActionbar load=new LoadActionbar(profileImage,profilePic,content.optInt("author_id"));
				           load.execute();
				           
							AQuery a=new AQuery(PreviewPage.this.getApplicationContext());
							a.id(contentImage).image(contentPic);
							
						    quiztime=content.optInt("time");
						    
						     profileName.setText(content.optString("author_name"));
						     title.setText(content.optString("title"));
						     ifc.setText("IFC:"+content.optString("ifc")+" | Readers:"+content.optInt("no_readers"));
						     
						     if(!type.equals("media"))
						     des.setText(content.optString("desc"));
						     else
						     des.setText("");
						
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
				
				
				
				AQuery aq=new AQuery(getApplicationContext());
	

				String url = URL+"mobile_Checkifc";
				
				Map<String, Object> params = new HashMap<String, Object>();
				
				params.put("authid", authid);
				params.put("contentid", contentid);
				params.put("type",type);
				
				
				aq.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() 
						{

							@Override
							public void callback(String url,final JSONObject content, AjaxStatus status) 
							{
								
								Log.e("preview", content.toString());
								
								//cost
								//ok =free(read) true dialog   
								//userifc
								
								Constants.showMsg(content.optString("ok"), getApplicationContext());
								
								
								if(content.optString("ok").equals("free"))
								{
									if(type.equals("blogbook")||type.equals("collaboration")||type.equals("article"))
									{
										Intent intent=new Intent();
										intent.setClass(getApplicationContext(), ABCReading.class);
										intent.putExtra("contentId",contentid );
										intent.putExtra("type", type);
										startActivity(intent);
										
									}
									else if(type.equals("media"))
									{
										
									}
								}
								else if(content.optString("ok").equals("true"))
								{
									
									int userifc=content.optInt("userifc");
									final int cost=content.optInt("cost");
									
									AlertDialog.Builder build=new AlertDialog.Builder(PreviewPage.this);
									build.setMessage("you have :"+userifc+"\n cost :"+cost+"\n you will have :"+(userifc-cost));
									build.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(final DialogInterface dialog, int which) {
									

											AQuery aqq=new AQuery(getApplicationContext());
											

											String url = URL+"mobile_reduceIfc";
											
											Map<String, Object> params = new HashMap<String, Object>();
											
											params.put("authid", authid);
											params.put("contentid", contentid);
											params.put("type",type);
											params.put("cost", cost);
											params.put("authorid",authorid );
											
										Log.e("bbarters purchasing", "authid:"+authid+" contentid:"+contentid+" type:"+type+" cost:"+cost+" authorid:"+authorid);
											
											aqq.ajax(url, params, String.class,
													new AjaxCallback<String>() 
													{

														@Override
														public void callback(String url,final String content, AjaxStatus status) 
														{
															
															Log.e("purchase",content);
										
															
															if(content.equals("success"))
															{
																if(type.equals("blogbook")||type.equals("collaboration")||type.equals("article"))
																{

																	Intent intent=new Intent();
																	intent.setClass(getApplicationContext(), ABCReading.class);
																	intent.putExtra("contentId",contentid );
																	intent.putExtra("type", type);
																	startActivity(intent);
																	
																}
																else if(type.equals("media"))
																{
																	
																}
																else if(type.equals("quiz"))
																{
																	Intent intent=new Intent();
																	intent.setClass(getApplicationContext(), QuizActivity.class);
																	intent.putExtra("contentid",contentid );
																	intent.putExtra("time", quiztime);
																	startActivity(intent);
																}
															}
															else
															{
																Constants.showMsg(content, getApplicationContext());	
															}
															
															dialog.dismiss();
															

														}
													});
											
										}
									});
									
									build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											
											dialog.dismiss();
										}
									});
				                
									build.show();
									
								}
								else 
								{
									Constants.showMsg(content.optString("ok"), getApplicationContext());
								}
	
							}
						});
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
