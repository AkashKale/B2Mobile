package com.bbarters.bbartersmobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ExploreFragment extends Fragment {

	public ExploreFragment() 
	{
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{ 
		View view=inflater.inflate(R.layout.fragment_explore, container, false);
	
		
		return view;
	}


}
