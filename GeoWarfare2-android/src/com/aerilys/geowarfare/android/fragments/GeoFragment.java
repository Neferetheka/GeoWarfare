package com.aerilys.geowarfare.android.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.activities.CheckinActivity_;

public abstract class GeoFragment extends SherlockFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		MenuItem itemCheckin = menu.add(getActivity().getString(R.string.checkin));
		itemCheckin.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		itemCheckin.setIcon(R.drawable.ic_checkin);
		
		Intent checkinIntent = new Intent(getActivity(), CheckinActivity_.class);
		
		itemCheckin.setIntent(checkinIntent);

		menu.add(getActivity().getString(R.string.settings)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(getActivity().getString(R.string.guide)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{	
		return super.onOptionsItemSelected(item);
	}
	
	public abstract void datasLoaded();
}
