package com.aerilys.geowarfare.android.views.adapters;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Sector;
import com.aerilys.geowarfare.android.tools.DataContainer;
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

public class SectorAdapter extends BaseAdapter
{
	LayoutInflater inflater;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();

	public SectorAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return DataContainer.getPlayerI().getListSectors().size();
	}

	public Sector getItem(int index)
	{
		return DataContainer.getPlayerI().getListSectors().get(index);
	}

	public long getItemId(int arg0)
	{
		return arg0;
	}

	private class ViewHolder
	{
		ImageView sectorImage;
		TextView sectorName;
		TextView sectorUnits;
		TextView sectorDescription;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;

		holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.listview_sector, null);

		holder.sectorImage = (ImageView) convertView.findViewById(R.id.sectorListviewImage);
		holder.sectorName = (TextView) convertView.findViewById(R.id.sectorListviewName);
		holder.sectorUnits = (TextView) convertView.findViewById(R.id.sectorListviewUnits);
		holder.sectorDescription = (TextView) convertView.findViewById(R.id.sectorListviewDescription);

		convertView.setTag(holder);

		Sector currentSector = getItem(position);
		
		holder.sectorName.setText(currentSector.getName());
		holder.sectorUnits.setText(currentSector.getUnits() + "");

		String descriptionKey = "NOMI" + ((int)Math.floor(currentSector.influence %100 / 25) + 1) + "D"
				+ ((int)Math.floor(currentSector.development/25) + 1);
 
		try
		{
			holder.sectorDescription.setText(inflater.getContext().getResources()
					.getIdentifier(descriptionKey, "string", inflater.getContext().getPackageName()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (getItem(position).getImage().length() > 5)
		{
			Bitmap cachedImage = null;
			final ImageView imageView = holder.sectorImage;

			final String key = "cache_" + getItem(position).getCategory();

			cachedImage = DataContainer.getInstance().getBitmapCache().get(key);

			if (cachedImage == null)
			{
				try
				{

					cachedImage = imageLoader.loadImage(getItem(position).getImage(), new ImageLoadedListener()
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
			holder.sectorImage.setImageDrawable(inflater.getContext().getResources().getDrawable(R.drawable.question));

		return convertView;
	}
}
