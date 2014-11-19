package com.bbarters.bbartersmobile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity 
{
	private UiLifecycleHelper uihelper; // facebook
	boolean showLogin = false;
	boolean onetime=true;

	void showMsg(String string) 
	{
		/*SnackBar mSnackBar = new SnackBar(this);
		mSnackBar.show("This library is awesome!");
		*/
		
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
	}

	private boolean checkInternet() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return true;
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			onSessionStateChange(session, state, exception);
		}
	};

	void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("facebook", "Logged in...");
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {

					if (user != null) 
					{
						if(onetime==true)				
						{
							onetime=false;
							AQuery aq = new AQuery(getApplicationContext());

							String url = Constants.getUrl()+"mobile_fblogin";

							Map<String, Object> params = new HashMap<String, Object>();
							params.put("fbid", "100006110950344");

							aq.ajax(url, params, JSONObject.class,
									new AjaxCallback<JSONObject>() {

										@Override
										public void callback(String url,
												JSONObject content,
												AjaxStatus status) {

											if (content != null) {

												if (content.optString("ok").equals(
														"true")) {

													int userid = content
															.optInt("id");
													String fname = content
															.optString("first_name");
													String lastname = content
															.optString("last_name");

													SharedPreferences auth = getSharedPreferences(
															"Auth",
															Context.MODE_PRIVATE);

													Editor edi = auth.edit();
													edi.putInt("id", userid);
													edi.putString("first_name",
															fname);
													edi.putString("last_name",
															lastname);
													edi.commit();

													Intent intent = new Intent();
													intent.setClass(
															getApplicationContext(),
															Home.class);
													startActivity(intent);

												} else {
													showMsg(content.optString("ok"));
													Log.e("error bbarters",
															content.optString("ok"));
												}

											} else {
												showMsg("emm something went wrong in the server side");
												Log.e("error server mainactivity",
														status.getError());
											}

										}
									});

							Session session = Session.getActiveSession();
							if (session != null) {

								if (!session.isClosed()) {
									session.closeAndClearTokenInformation();
								}
							} else {

								session = new Session(Login.this);
								Session.setActiveSession(session);

								session.closeAndClearTokenInformation();

							}
						}
						
						

					} 
					else 
					
					{
						showMsg("its null");
					}
				}
			}).executeAsync();

		} else if (state.isClosed()) {
			Log.i("facebook", "Logged out...");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		uihelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		uihelper.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uihelper.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		onetime=true;

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uihelper.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uihelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (showLogin) {

				final LinearLayout socialLayout = (LinearLayout) findViewById(R.id.socialLogin);

				socialLayout.animate().alpha(0).translationY(180)
						.setDuration(400);

				final Button socialBtn = (Button) findViewById(R.id.socialBtn);

				socialBtn.setEnabled(true);
				socialBtn.animate().alpha(1).setDuration(700);

				socialLayout.postDelayed(new Runnable() {

					@Override
					public void run() {
						socialLayout.setVisibility(LinearLayout.INVISIBLE);
						showLogin = false;

					}

				}, 800);

			} else {
				finish();
			}

		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		
		String StoragePathUser=Constants.getStoragePathUser(getApplicationContext());
		File file=new File(StoragePathUser);
		if(!file.exists())
		{
			file.mkdirs();
		}
		
		
		String StoragePathCover=Constants.getStoragePathCover(getApplicationContext());
		File file2=new File(StoragePathCover);
		if(!file2.exists())
		{
			file2.mkdirs();
		}
		
		uihelper = new UiLifecycleHelper(this, callback);
		uihelper.onCreate(savedInstanceState);

		final Button btn = (Button) findViewById(R.id.login);

		final LoginButton fbbtn = (LoginButton) findViewById(R.id.fblogin);
		ArrayList<String> permission = new ArrayList<String>();
		permission.add("email");
		permission.add("public_profile");
		permission.add("user_friends");
		fbbtn.setPublishPermissions(permission);
	

		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		final TextView bbText = (TextView) findViewById(R.id.bbText);

		final Button tlogin = (Button) findViewById(R.id.tlogin);
		final Button glogin = (Button) findViewById(R.id.glogin);
		final ProgressBar pbar = (ProgressBar) findViewById(R.id.pBar);
		final Button socialBtn = (Button) findViewById(R.id.socialBtn);
		final LinearLayout socialLayout = (LinearLayout) findViewById(R.id.socialLogin);
        final Progress progress=new Progress(this);
        
		username.setAlpha(0);
		password.setAlpha(0);
		btn.setAlpha(0);
		bbText.setTranslationY(150);
		pbar.setTranslationY(150);
		socialBtn.setAlpha(0);
		socialLayout.setTranslationY(180);
		socialLayout.setAlpha(0);

		socialBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showLogin = true;
				socialLayout.setVisibility(LinearLayout.VISIBLE);
				socialLayout.animate().alpha(1).translationY(0)
						.setDuration(700);

				socialBtn.setEnabled(false);
				socialBtn.animate().alpha(0).setDuration(700);

			}
		});

		bbText.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (checkInternet()) {
					bbText.animate().translationY(0).setDuration(1000);
					pbar.animate().alpha(0).setDuration(500);

					bbText.postDelayed(new Runnable() {

						@Override
						public void run() {
							username.animate().alpha(1).setDuration(500);
							password.animate().alpha(1).setDuration(500);
							btn.animate().alpha(1).setDuration(500);
							socialBtn.animate().alpha(1).setDuration(500);
						}
					}, 1100);
				} else {
					showMsg("No Internet Connection");
					pbar.animate().alpha(0).setDuration(500);

				}

			}
		}, 1000);

		/*
		 * try { PackageInfo info = getPackageManager().getPackageInfo(
		 * "com.bbarters.bbartersmobile", PackageManager.GET_SIGNATURES); for
		 * (Signature signature : info.signatures) { MessageDigest md =
		 * MessageDigest.getInstance("SHA"); md.update(signature.toByteArray());
		 * Log.d("KeyHash:", Base64.encodeToString(md.digest(),
		 * Base64.DEFAULT)); } } catch (Exception e) { e.printStackTrace(); }
		 */

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (username.getText().toString().isEmpty()
						|| password.getText().toString().isEmpty()) 
				{
					showMsg("fill in the fields");
					//testing without net
					Intent intent = new Intent();
					intent.setClass(
							getApplicationContext(),
							Home.class);
					startActivity(intent);
					//ends
				} 
				else 
				{
					progress.start("Login","Authenticating");
					
					AQuery aq = new AQuery(getApplicationContext());

					String url = Constants.getUrl()+"mobile_login";

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("uname", username.getText().toString());
					params.put("pwd", password.getText().toString());

					aq.ajax(url, params, JSONObject.class,
							new AjaxCallback<JSONObject>() {

								@Override
								public void callback(String url,
										JSONObject content, AjaxStatus status) {

								
									
									progress.stop();
									
									if (content != null) 
									{

										if (content.optString("ok").equals(
												"true")) {

											int userid = content.optInt("id");
											String fname = content
													.optString("first_name");
											String lastname = content
													.optString("last_name");

											SharedPreferences auth = getSharedPreferences(
													"Auth",
													Context.MODE_PRIVATE);

											Editor edi = auth.edit();
											edi.putInt("id", userid);
											edi.putString("first_name", fname);
											edi.putString("last_name", lastname);
											edi.commit();

											Intent intent = new Intent();
											intent.setClass(
													getApplicationContext(),
													Home.class);
											startActivity(intent);

										} else {
											showMsg(content.optString("ok"));
										}

									}
									else 
									{
										showMsg("emm something went wrong in the server side");
										Log.e("error server mainactivity",
												status.getError());
									}
									
									Log.e("bbarters login", content.optString("ok"));
									

								}
							});
				}

			}
		});

	}
	


}




class Progress
{
	ProgressDialog p;
	Context conte;
	public Progress(Context co)
	{
		conte=co;
	}
	public void start(String title,String msg)
	{
	  p=new ProgressDialog(conte);
	  p.setTitle(title);
	  p.setMessage(msg);
	  p.setCancelable(false);
	  p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	  p.show();
	  
	}
	public void stop()
	{
		p.dismiss();
	}
}