package com.aerilys.geowarfare.android.tools;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache extends LruCache<String, Bitmap>
{
	public BitmapCache(int maxSize)
	{
		super(maxSize);
	}
}
