package com.bbarters.bbartersmobile;

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
	protected void onCreate(Bundle savedInstanceState) {
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
	    
      	Bundle bundle=getIntent().getExtras();
		 
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
			        
			    	Display display=new Display(dpURL,ivDispImg,coverPic,cp);  	
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
         
         public Display(String ur,ImageView i,ImageView cp,String cpstring)   
         {
        	 
        	 coverPicString=cpstring.replaceAll("\\\\", "");
        	 coverPic=cp;
        	 url=ur.replaceAll("\\\\","");
        	 iv=i;
         }
		
		@Override
		protected String doInBackground(String... params) 
		{
			image=getBitmapFromURL(url);
			image=getResizedBitmap(image,230,200);
			image=getRoundedShape(image);
			image=imageGlow(image);  	
			
			
	    	coverImage=getBitmapFromURL(coverPicString);
	    	coverImage=toGrayscale(coverImage);
	    	return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			
		   iv.setBackgroundDrawable(new BitmapDrawable(getResources(),image));	
		   coverPic.setBackgroundDrawable(new BitmapDrawable(getResources(),coverImage));
		   
		   
		   super.onPostExecute(result);
		}
		
		
		
		
		
		public Bitmap getRoundedShape(Bitmap scaleBitmapImage) 
		{
		    int targetWidth = 150;
		    int targetHeight = 150;
		    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
		                        targetHeight,Bitmap.Config.ARGB_8888);

		    Canvas canvas = new Canvas(targetBitmap);
		    Path path = new Path();
		    path.addCircle(((float) targetWidth - 1) / 2,
		        ((float) targetHeight - 1) / 2,
		        (Math.min(((float) targetWidth), 
		        ((float) targetHeight)) / 2),
		        Path.Direction.CCW);

		    canvas.clipPath(path);
		    Bitmap sourceBitmap = scaleBitmapImage;
		    canvas.drawBitmap(sourceBitmap, 
		        new Rect(0, 0, sourceBitmap.getWidth(),
		        sourceBitmap.getHeight()), 
		        new Rect(0, 0, targetWidth, targetHeight), null);
		    return targetBitmap;
		}
		
		
		
		
		
		public Bitmap getBitmapFromURL(String src) 
		{
		    try 
		    {
		        Log.e("src",src);
		        URL url = new URL(src);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
		        Log.e("Bitmap","returned");
		        return myBitmap;
		    } 
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		        Log.e("Exception",e.getMessage());
		        return null;
		    }
		}
		

		
		
		
		public  Bitmap imageGlow(Bitmap src)
		{
		int r=0;
		int g=0;
		int b=0;
		// An added margin to the initial image
		try
		{
		    int margin = 16;
		    int halfMargin = margin / 2;
		    // the glow radius
		    int glowRadius = 12;

		    // the glow color
		    int glowColor = Color.rgb(r, g, b);

		    // The original image to use

		    // extract the alpha from the source image
		    Bitmap alpha = src.extractAlpha();

		    // The output bitmap (with the icon + glow)
		    Bitmap bmp =  Bitmap.createBitmap(src.getWidth() + margin, src.getHeight() + margin, Bitmap.Config.ARGB_8888);

		    // The canvas to paint on the image
		    Canvas canvas = new Canvas(bmp);

		    Paint paint = new Paint();
		    paint.setColor(glowColor);

		    // outer glow
		    paint.setMaskFilter(new BlurMaskFilter(glowRadius, Blur.OUTER));//For Inner glow set Blur.INNER
		    canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);

		    // original icon
		    canvas.drawBitmap(src, halfMargin, halfMargin, null);


		return bmp;
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			return src;
		}


		}
		
		
		
		
		
		public Bitmap toGrayscale(Bitmap bmpOriginal)
		{        
		    int width, height;
		    height = bmpOriginal.getHeight();
		    width = bmpOriginal.getWidth();    

		    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		    Canvas c = new Canvas(bmpGrayscale);
		    Paint paint = new Paint();
		    ColorMatrix cm = new ColorMatrix();
		    cm.setSaturation(0);
		    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		    paint.setColorFilter(f);
		    c.drawBitmap(bmpOriginal, 0, 0, paint);
		    return bmpGrayscale;
		}
		
		
		
		public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
			 
			int width = bm.getWidth();
			 
			int height = bm.getHeight();
			 
			float scaleWidth = ((float) newWidth) / width;
			 
			float scaleHeight = ((float) newHeight) / height;
			 
			// CREATE A MATRIX FOR THE MANIPULATION
			 
			Matrix matrix = new Matrix();
			 
			// RESIZE THE BIT MAP
			 
			matrix.postScale(scaleWidth, scaleHeight);
			 
			// RECREATE THE NEW BITMAP
			 
			Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
			 
			return resizedBitmap;
			 
			}
	}
}
