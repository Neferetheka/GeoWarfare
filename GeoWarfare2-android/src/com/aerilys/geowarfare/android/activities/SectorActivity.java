package com.aerilys.geowarfare.android.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.views.adapters.SectorAdapter;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_sectors)
public class SectorActivity extends SherlockActivity
{
	@ViewById
	protected ListView sectorsListview;

	private SectorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void bindDatas()
	{
		setProgressBarIndeterminateVisibility(false);
		getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (adapter == null)
		{
			adapter = new SectorAdapter(this);
			sectorsListview.setAdapter(adapter);
		}
		else
			adapter.notifyDataSetChanged();

	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			this.finish();
			return super.onMenuItemSelected(featureId, item);
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			this.finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
}
