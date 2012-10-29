package com.aerilys.geowarfare.android.tools;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.aerilys.geowarfare.android.activities.CheckinActivity;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class SectorOverlay extends ItemizedOverlay<OverlayItem>
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context context;

	public SectorOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
	}

	public SectorOverlay(Drawable defaultMarker, Context context)
	{
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	@Override
	protected boolean onTap(int index)
	{
		CheckinActivity activity = (CheckinActivity) this.context;
		activity.secteurOverlayFocus();
		return true;
	}

	public void addOverlay(OverlayItem overlay)
	{
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i)
	{
		return mOverlays.get(i);
	}

	@Override
	public int size()
	{
		return mOverlays.size();
	}

	public void clearOverlays()
	{
		mOverlays.clear();
	}

}
