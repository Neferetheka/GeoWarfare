package com.aerilys.geowarfare.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Player;
import com.aerilys.geowarfare.android.tools.DataContainer;

public class ArmyFragment extends GeoFragment
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
		return inflater.inflate(R.layout.fragment_army, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		isLoad = true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		datasLoaded();
	}

	@Override
	public void datasLoaded()
	{
		if (isLoad)
		{
			try
			{
				
				TextView unitsLabel = (TextView) getView().findViewById(R.id.unitsLabel);
				unitsLabel.setText(getString(R.string.units) + " : " + DataContainer.getPlayerI().units);

				((TextView) getView().findViewById(R.id.productionLabel)).setText(getString(R.string.production)
						+ " : " + DataContainer.getPlayerI().production);
				
				((TextView)getView().findViewById(R.id.factionLabel)).setText(
						Player.getFactionNameFromIndex(getActivity(), DataContainer.getPlayerI().faction));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
