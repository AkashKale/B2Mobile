package com.bbarters.bbartersmobile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Home extends FragmentActivity 
{
	ActionBar actionBar;
	ViewPager viewPager;
	DrawerLayout drawer;
	ListView drawerList;
	private String[] drawerListViewItems;
	private ActionBarDrawerToggle drawerToggle;
	
	String URL=Constants.getUrl();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getActionBar().setHomeButtonEnabled(true);
		drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
		drawerList=(ListView)findViewById(R.id.left_drawer);
		viewPager=(ViewPager)findViewById(R.id.pager);
		viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		
			
			
			@Override
			public void onPageSelected(int arg0) {
				actionBar.setSelectedNavigationItem(arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	
		TabListener listener=	new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
		         viewPager.setCurrentItem(tab.getPosition());			
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
			}
		};
		
		actionBar=getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("");
		
		ActionBar.Tab tab1=actionBar.newTab();
		tab1.setText("T1");
		tab1.setTabListener(listener);
		 
	
		ActionBar.Tab tab2=actionBar.newTab();
		tab2.setText("T2");
		tab2.setTabListener(listener);
		
		
		ActionBar.Tab tab3=actionBar.newTab();
		tab3.setText("T3");
		tab3.setTabListener(listener);
		
		ActionBar.Tab tab4=actionBar.newTab();
		tab4.setText("T4");
		tab4.setTabListener(listener);
		
		
		
	actionBar.addTab(tab1);	
	actionBar.addTab(tab2);
	actionBar.addTab(tab3);
	actionBar.addTab(tab4);
		
	
	//sliding drawer code
	drawerListViewItems = getResources().getStringArray(R.array.items);
    // get ListView defined in activity_main.xml
    drawerList = (ListView) findViewById(R.id.left_drawer);

    // Set the adapter for the list view
    drawerList.setAdapter(new ArrayAdapter<String>(this,
            R.layout.drawer_listview_item, drawerListViewItems));
	drawerToggle = new ActionBarDrawerToggle(
            this,                  /* host Activity */
            drawer,         /* DrawerLayout object */
            R.drawable.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
            0,  /* "open drawer" description */
            0  /* "close drawer" description */
            );

    // Set actionBarDrawerToggle as the DrawerListener
    drawer.setDrawerListener(drawerToggle);

    getActionBar().setDisplayHomeAsUpEnabled(true); 

    // just styling option add shadow the right edge of the drawer

    
    drawerList.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
	
			      if(position==1)
			      {
			    	  
			      }
			      else if(position==2)
			      {
			    	  
			      }
			      else if(position==3)
			      {
			    	  
			      }
			      else if(position==4)
			      {
			    	  
			      }
			      else if(position==5)
			      {
			    	  
			      }
			      else if(position==6)
			      {
			    	  
			      }
		}
	});

}

@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
}

    
	//drawer code ends
	
	void showMsg(String string)
	{
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
	    switch (item.getItemId()) {
	        case R.id.logout:
	            logout();
	            return true;
	        case R.id.profile:
	            viewProfile();
	            return true;
	        case R.id.ifcMgr:
	            ifcManager();
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() 
	{
		logout();
	}
	
	private void viewProfile() 
	{
		SharedPreferences auth = getSharedPreferences("Auth", Context.MODE_PRIVATE);
	    
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), ProfilePage.class);
		intent.putExtra("id", auth.getInt("id",0));
		startActivity(intent);
	}

	private void logout() 
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Are you sure?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		{
		           public void onClick(DialogInterface dialog, int id) 
		           {
		               // User clicked OK button
		        	   	AQuery aq=new AQuery(getApplicationContext());
		   			
		   		    	String url=URL+"mobile_logout";     		    												   
		   		                                                    
		   		    
		   		    	Map<String, Object> params = new HashMap<String, Object>();
		   		    	SharedPreferences auth = getSharedPreferences("Auth", Context.MODE_PRIVATE);
		   		    	params.put("id",auth.getInt("id", 0));       		   		    	
		   	       		aq.ajax(url,params,String.class, new AjaxCallback<String>() 
		   	       				{
		   	       			
		   	       		@Override
		   		        public void callback(String url, String str, AjaxStatus status) 
		   		        {
		   		        	if(str.equals("success"))
		   		        	{
		   		        		showMsg("Log out successful");
		   		        	}
		   		        }
		           });
		   	       		

	   		       finish();
		          }
		       });
		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		        	  dialog.dismiss();   
		           }
		       });

			builder.show();
	}

	private void ifcManager() {
		// TODO Auto-generated method stub
		Intent intent =new Intent();
	    intent.setClass(getApplicationContext(), IFCManager.class);
	    startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_bar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
}


class Adapter extends FragmentStatePagerAdapter
{

	public Adapter(FragmentManager fm) 
	{
		super(fm);

	}

	@Override
	public Fragment getItem(int arg0) 
	{

		Fragment fragment=null;
		
		 if(arg0==0)
		 {
			 fragment=new ActionFragment();
		 }
		 else if(arg0==1) 
		 {
			 fragment=new NotificationFragment();
		 }
		 else if(arg0==2)
		 {
			 fragment=new ReadingFragment();
		 }
		 else if(arg0==3)
		 {
			 fragment=new ExploreFragment();
		 }
		 
		return fragment;
	}

	@Override
	public int getCount() 
	{
		return 4;
	}
	
	
	
	
 
}