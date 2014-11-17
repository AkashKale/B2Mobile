package com.example.googlesignin;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	

	 private PlusClient mPlusClient;  
	 private int REQUEST_CODE_RESOLVE_ERR=301;
	 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==REQUEST_CODE_RESOLVE_ERR&&resultCode==RESULT_OK)
		{
		   mPlusClient.disconnect();
		   mPlusClient.connect();
		}
		
	}
	
	void showMsg(String string)
	{
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
mPlusClient=new PlusClient.Builder(getApplicationContext(), new ConnectionCallbacks() {
	
	@Override
	public void onDisconnected() {
		
		
	}
	
	@Override
	public void onConnected(Bundle arg0) 
	{
	 // this method will be called after the login is complete 
		// now we can access the users profile information here
		
		Person person=mPlusClient.getCurrentPerson();
		showMsg("username:-"+person.getDisplayName());
		showMsg("id"+person.getId());
		showMsg("gender"+person.getGender());
		
		
	}
}, new OnConnectionFailedListener() {
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {

		 // TODO Auto-generated method stub
        if (result.hasResolution()) {
   
            try {
                result.startResolutionForResult(MainActivity.this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.disconnect();
                mPlusClient.connect();
            }
        }			
	}
}).build();


	SignInButton btn=(SignInButton)findViewById(R.id.googlebtn);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				   if (!mPlusClient.isConnected()) {

		                mPlusClient.connect();
	                  showMsg("signed in google+");
		                
		            } else if (mPlusClient.isConnected()) 
		            {
		                {
		                    mPlusClient.clearDefaultAccount();
		                    mPlusClient.disconnect();
		                    showMsg("signed  out of google+");
		            	    
		                }
		            }
				
			}
		});
	}

}
