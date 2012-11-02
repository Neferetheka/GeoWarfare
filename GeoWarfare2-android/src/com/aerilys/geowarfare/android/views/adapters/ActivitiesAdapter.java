package com.aerilys.geowarfare.android.views.adapters;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.GeoEvent;
import com.aerilys.geowarfare.android.models.GeoEvent.GeoEventType;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.RoutineManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitiesAdapter extends BaseAdapter
{
	LayoutInflater inflater;

	public ActivitiesAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return DataContainer.getPlayerI().getListGeoEvents().size();
	}

	@Override
	public GeoEvent getItem(int position)
	{
		return DataContainer.getPlayerI().getListGeoEvents().get(position);
	}

	@Override
	public long getItemId(int id)
	{
		return id;
	}

	private class ViewHolder
	{
		ImageView activitiesImage;
		TextView activitiesTitle;
		TextView activitiesContent;
		TextView activitiesDate;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
 
		holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.listview_activities, null);

		holder.activitiesImage = (ImageView) convertView.findViewById(R.id.activitiesImage);
		holder.activitiesTitle = (TextView) convertView.findViewById(R.id.activitiesTitle);
		holder.activitiesContent = (TextView) convertView.findViewById(R.id.activitiesContent);
		holder.activitiesDate = (TextView) convertView.findViewById(R.id.activitiesDate);

		convertView.setTag(holder);
		
		GeoEvent event = getItem(position);

		holder.activitiesTitle.setText(RoutineManager.getTextFromRoutine(inflater.getContext(), event.getTitle()));
		holder.activitiesContent.setText(RoutineManager.getTextFromRoutine(inflater.getContext(), event.getContent()));
		holder.activitiesDate.setText(event.getDatePublication());

		if (event.getGeoEventType() == GeoEventType.Battle)
			holder.activitiesImage.setImageResource(R.drawable.noun_rocket);
		else if (event.getGeoEventType() == GeoEventType.Success)
			holder.activitiesImage.setImageResource(R.drawable.noun_award);
		else
			holder.activitiesImage.setImageResource(R.drawable.question);

		return convertView;
	}

}
