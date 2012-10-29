package com.aerilys.geowarfare.android.fragments;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.views.adapters.ActivitiesAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ActivitiesFragment extends GeoFragment
{
	private boolean isLoad = false;
	private ListView activitiesListview;
	ActivitiesAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		isLoad = false;
		return inflater.inflate(R.layout.fragment_activities, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		isLoad = true;
		activitiesListview = (ListView) getView().findViewById(R.id.activitiesListview);
	}

	@Override
	public void datasLoaded()
	{
		if (isLoad && getView() != null)
		{
			adapter = new ActivitiesAdapter(getActivity());
			activitiesListview.setAdapter(adapter);
		}
	}
}
