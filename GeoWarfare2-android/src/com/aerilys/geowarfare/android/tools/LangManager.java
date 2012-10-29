package com.aerilys.geowarfare.android.tools;

import android.content.Context;

public abstract class LangManager
{
	public static String getString(Context context, int id)
	{
		return context.getResources().getString(id);
	}
}
