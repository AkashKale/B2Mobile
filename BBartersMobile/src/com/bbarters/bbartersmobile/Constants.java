package com.bbarters.bbartersmobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
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
import android.util.Log;
import android.widget.Toast;

public class Constants 
{
	//192.168.60.1
	
	static String DOMAIN="bbarters.com";
	
	static String URL="http://"+DOMAIN+"/";

	static String REPLACEMENT="b2.com";

	
     public static String getUrl()
     {
    	 return URL;
     }
     
     public static String getDomainName()
     {
    	 return DOMAIN;
     }
     
     public static String getReplacement()
     {
    	 return REPLACEMENT;
     }
     
     
     public static String setReplacement(String toReplace)
     {
    	 String name= toReplace.replaceFirst(Constants.getReplacement(), Constants.getDomainName());	 
        
    	 return name;
     
     }
     
     public static void showMsg(String string,Context context)
     {
    	Toast.makeText(context, string, Toast.LENGTH_SHORT).show(); 
     }
     
     
     public static String getStoragePathUser(Context con)
     {
    	 return con.getExternalCacheDir().getPath()+"/user";
     }
     
     public static String getStoragePathCover(Context con)
     {
    	 return con.getExternalCacheDir().getPath()+"/cover";
     }
     
     
     
		public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) 
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
		
		
		
		
		
		public static  Bitmap getBitmapFromURL(String src) 
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
		

		
		
		
		public static  Bitmap imageGlow(Bitmap src)
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
		
		
		
		
		
		public static Bitmap toGrayscale(Bitmap bmpOriginal)
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
		
		
		
		public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
			 
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
		
		
		
		public static Bitmap cropBitmap(Bitmap srcBmp)
		{
			Bitmap dstBmp;
			
			if (srcBmp.getWidth() >= srcBmp.getHeight())
			{

				  dstBmp = Bitmap.createBitmap(
				     srcBmp, 
				     srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
				     0,
				     srcBmp.getHeight(), 
				     srcBmp.getHeight()
				     );

				}else{

				  dstBmp = Bitmap.createBitmap(
				     srcBmp,
				     0, 
				     srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
				     srcBmp.getWidth(),
				     srcBmp.getWidth() 
				     );
				}
			
			
			return dstBmp;
		}
		
		public static void writeBmpToFile(File filename,Bitmap bmp)
		{
			FileOutputStream out = null;
			try 
			{
			    out = new FileOutputStream(filename);
			    bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			    // PNG is a lossless format, the compression factor (100) is ignored
			} 
			
			catch (Exception e) 
			{
			    e.printStackTrace();
			} 
			
			finally 
			{
			 
				try
				{
			        if (out != null) 
			        {
			            out.close();
			        }
			    } 
				
				catch (IOException e)
				{
			        e.printStackTrace();
			    }
			}
		}
		
		
		
		public static String getType(String string)
		{
		     String type="";
		    
		     if(string.toLowerCase().contains("book"))
		     {
		    	 type="blogbook";
		     }
		     else if(string.toLowerCase().contains("collaboration"))
		     {
		    	 type="collaboration";
		     }
		     else if(string.toLowerCase().contains("article"))
		     {
		    	 type="article";
		     }
		     else if(string.toLowerCase().contains("media"))
		     {
		    	 type="media";
		     }
		     else if(string.toLowerCase().contains("poll"))
		     {
		    	 type="poll";
		     }
		     else if(string.toLowerCase().contains("quiz"))
		     {
		    	 type="quiz";
		     }
		     else if(string.toLowerCase().contains("resource"))
		     {
		    	 type="resource";
		     }
		    	 return type;
		     
		}
		
}
