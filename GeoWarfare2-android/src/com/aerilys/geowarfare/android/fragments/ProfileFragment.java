package com.aerilys.geowarfare.android.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.ImageThreadLoader;
import com.aerilys.geowarfare.android.tools.ImageThreadLoader.ImageLoadedListener;

public class ProfileFragment extends GeoFragment
{
	private boolean isLoad = false;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		isLoad = false;
		return inflater.inflate(R.layout.fragment_profile, container, false);
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
		if (isLoad && getView() != null)
		{
			((TextView) getView().findViewById(R.id.profileLogin)).setText(DataContainer.getPlayerI().getLogin());
			((TextView) getView().findViewById(R.id.profileBiographie)).setText(DataContainer.getPlayerI()
					.getBiography());

			Bitmap cachedImage = null;
			final ImageView imageView = (ImageView) getView().findViewById(R.id.profileAvatar);
			try
			{
				cachedImage = imageLoader.loadImage(DataContainer.getPlayerI().getAvatar(), new ImageLoadedListener()
				{
					public void imageLoaded(Bitmap imageBitmap)
					{
						imageView.setImageBitmap(imageBitmap);
					}
				});

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			if (cachedImage != null)
			{
				imageView.setImageBitmap(cachedImage);
			}
		}
	}
}
