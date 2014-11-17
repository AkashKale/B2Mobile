package com.bbarters.bbartersmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class ReadingFragment extends Fragment {

	public ReadingFragment() 
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{	View view=inflater.inflate(R.layout.fragment_reading, container, false);

		return view;
	}

}
