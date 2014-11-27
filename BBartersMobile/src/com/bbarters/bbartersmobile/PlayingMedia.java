package com.bbarters.bbartersmobile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayingMedia extends Activity 
{
	
String PlayingUrl;
String URL=Constants.getUrl();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playing_media);		
		
		final VideoView videoView=(VideoView)findViewById(R.id.videoView1);
      	final Button downloadBtn=(Button)findViewById(R.id.download);

      	AQuery aq=new AQuery(getApplicationContext());
	    
      	String url=URL+"mobile_showProfile";     		    												   
	    
      	int id=getIntent().getExtras().getInt("id");
        		 
      	Map<String, Object> params = new HashMap<String, Object>();
	    params.put("contentid",id);       		   		    	
      	
	    aq.ajax(url,params,String.class, new AjaxCallback<String>() {
	    public void callback(String url, String content, AjaxStatus status) 
	    {
               PlayingUrl=content;
               videoView.setVideoURI(Uri.parse(PlayingUrl));
	    }
      });

	    
		videoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) 
			{
		           MediaController controller=new MediaController(getApplicationContext());
		           videoView.setMediaController(controller);
			}
		});
		
		
	downloadBtn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{
		   
			DownloadManager mgr = (DownloadManager) 
				    getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
				boolean isDownloading = false;
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterByStatus(
				    DownloadManager.STATUS_PAUSED|
				    DownloadManager.STATUS_PENDING|
				    DownloadManager.STATUS_RUNNING|
				    DownloadManager.STATUS_SUCCESSFUL);
				
				Cursor cur = mgr.query(query);
				int col = cur.getColumnIndex(
				    DownloadManager.COLUMN_LOCAL_FILENAME);
				for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) 
				{
				    isDownloading = isDownloading || ("" == cur.getString(col));		
				}
				
				cur.close();





				// the actual download process

				if (!isDownloading) 
				{
				    Uri source = Uri.parse(PlayingUrl);
				    
				    File file=new File(PlayingUrl);
				    
				    Uri destination = Uri.fromFile(new File(getApplicationContext().getExternalFilesDir(android.os.Environment.DIRECTORY_MOVIES),file.getName()));
				 
				    DownloadManager.Request request = 
				        new DownloadManager.Request(source);
				    request.setTitle(file.getName());
				    request.setDescription("Nothing");
				    request.setDestinationUri(destination);
				    request.setNotificationVisibility(DownloadManager
				        .Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				    request.allowScanningByMediaScanner();
				 
				    long id = mgr.enqueue(request);
				}
				else 
				{
					Constants.showMsg("Already downloaded", getApplicationContext());
				}
		}
	});
		
	}

}
