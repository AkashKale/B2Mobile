package com.bbarters.bbartersmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bbarters.bbartersmobile.PreviewPage.LoadActionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ABCReading extends Activity {
String URL=Constants.getUrl();
String type;
int contentId;

	public void initChapterList()
	{
		AQuery aq = new AQuery(getApplicationContext());
		final ListView chapterList=(ListView)findViewById(R.id.chapterList);				
		
		String url = URL+"mobile_getChapterList";

				
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("contentid", contentId);

		aq.ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() 
				{

					@Override
					public void callback(String url,final JSONObject content, AjaxStatus status) 
					{
						
						Log.e("preview", content.toString());
					
						ArrayList<String> chapterName=new ArrayList<String>();						
						ArrayList<Integer> chapterId=new ArrayList<Integer>();
						
						
						
						ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
						chapterList.setAdapter(adapter);
						
						chapterList.setOnItemClickListener(new OnItemClickListener() 
						{
							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) 
							{
							
						 	
							}
						});
					}
				});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abcreading);
		
		DrawerLayout dlayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		WebView webView=(WebView)findViewById(R.id.webView);

		type=getIntent().getExtras().getString("type");
		contentId=getIntent().getExtras().getInt("contentId");
		
		
		
		
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		webView.loadUrl("http://www.bbarters.com");
		
	}


}
