package com.bbarters.bbartersmobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ABCReading extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abcreading);
		
		DrawerLayout dlayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		WebView webView=(WebView)findViewById(R.id.webView);	
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		webView.loadUrl("http://www.bbarters.com");
		
	}


}
