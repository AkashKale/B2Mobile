package com.bbarters.bbartersmobile;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
		
	final	ImageView profileImage=(ImageView)findViewById(R.id.profileImage);
	final	TextView  profileName=(TextView)findViewById(R.id.profileName);
	final	ImageView contentImage=(ImageView)findViewById(R.id.contentImage);
	final	TextView  title=(TextView)findViewById(R.id.title);
	final	TextView  typeText=(TextView)findViewById(R.id.type);
	final	TextView  ifc=(TextView)findViewById(R.id.ifc);
	final	TextView  des=(TextView)findViewById(R.id.des);
	final 	Button    read=(Button)findViewById(R.id.read);
	
		String type=getIntent().getExtras().getString("type");
		int contentid=getIntent().getExtras().getInt("contentid");
		
		
		
		AQuery aq = new AQuery(getApplicationContext());

		String url = URL+"mobile_getPreview";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentid", contentid);
		params.put("type",type);
		
		aq.ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() 
				{

					@Override
					public void callback(String url,JSONObject content, AjaxStatus status) 
					{

					     Log.e("bbarters previewage", content.toString());

					}
				});
	}

	
}
