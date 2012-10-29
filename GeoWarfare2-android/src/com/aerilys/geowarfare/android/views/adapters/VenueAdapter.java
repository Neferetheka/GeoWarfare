package com.aerilys.geowarfare.android.views.adapters;

import java.util.List;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.api.foursquare.Venue;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.FontManager;
import com.aerilys.geowarfare.android.tools.ImageThreadLoader;
import com.aerilys.geowarfare.android.tools.ImageThreadLoader.ImageLoadedListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VenueAdapter extends BaseAdapter
{
	public List<Venue> listeVenues;
	LayoutInflater inflater;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();

	public VenueAdapter(Context context, List<Venue> listeVenues)
	{
		inflater = LayoutInflater.from(context);
		this.listeVenues = listeVenues;
	}

	public int getCount()
	{
		return listeVenues.size();
	}

	public Venue getItem(int index)
	{
		return listeVenues.get(index);
	}

	public long getItemId(int arg0)
	{
		return arg0;
	}

	private class ViewHolder
	{
		ImageView VenuesImage;
		TextView VenuesNom;
		TextView VenuesAddress;
		TextView VenuesDistance;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_venue, null);

			holder.VenuesImage = (ImageView) convertView.findViewById(R.id.venueListImage);
			holder.VenuesNom = (TextView) convertView.findViewById(R.id.venueListName);
			holder.VenuesAddress = (TextView) convertView.findViewById(R.id.venueListAdress);
			holder.VenuesDistance = (TextView) convertView.findViewById(R.id.venueListDistance);

			holder.VenuesNom.setTypeface(FontManager.Ubuntu);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.VenuesNom.setText(listeVenues.get(position).Name);
		holder.VenuesAddress.setText(listeVenues.get(position).Adress);
		holder.VenuesDistance.setText(listeVenues.get(position).Distance + "m");

		if (getItem(position).Image.length() > 5)
		{
			Bitmap cachedImage = null;
			final ImageView imageView = holder.VenuesImage;

			final String key = "cache_" + getItem(position).Category;

			cachedImage = DataContainer.getInstance().getBitmapCache().get(key);

			if (cachedImage == null)
			{
				try
				{

					cachedImage = imageLoader.loadImage(getItem(position).Image, new ImageLoadedListener()
					{
						public void imageLoaded(Bitmap imageBitmap)
						{
							imageView.setImageBitmap(imageBitmap);
							if (DataContainer.getInstance().getBitmapCache().get(key) == null)
								DataContainer.getInstance().getBitmapCache().put(key, imageBitmap);
							notifyDataSetChanged();
						}
					});

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			if (cachedImage != null)
			{
				imageView.setImageBitmap(cachedImage);
			}
		}
		else
			holder.VenuesImage.setImageDrawable(inflater.getContext().getResources().getDrawable(R.drawable.question));

		return convertView;
	}
}
