package com.aerilys.geowarfare.android.tools;

import android.content.Context;
import android.graphics.Typeface;

public abstract class FontManager
{
	public static Typeface Ubuntu;
	public static Typeface UbuntuBold;
	
	public static void init(Context context)
	{
		Ubuntu = Typeface.createFromAsset(context.getAssets(), "Ubuntu-R.ttf");
		UbuntuBold = Typeface.createFromAsset(context.getAssets(), "Ubuntu-B.ttf");
	}
}
