package com.aerilys.geowarfare.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aerilys.geowarfare.android.R;

public class AllyFragment extends GeoFragment
{
	private boolean isLoad = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		isLoad = false;
		return inflater.inflate(R.layout.fragment_ally, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		isLoad = true;
	}

	@Override
	public void datasLoaded()
	{
		if (isLoad)
		{

		}
	}

}
