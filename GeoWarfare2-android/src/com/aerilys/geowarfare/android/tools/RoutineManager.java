package com.aerilys.geowarfare.android.tools;

import android.content.Context;
import android.util.Log;

public abstract class RoutineManager
{
	public static String getTextFromRoutine(Context context, String routine)
	{
		if (routine.contains("|"))
		{
			String[] results = routine.split("\\|");
			
			String result= getStringByName(context, results[0]).replace("SECTORNAME", results[1]);
			if(results.length == 3)
				result = result.replace("ENEMYNAME", results[2]);
			
			return result;
		}
		else
		{
			return getStringByName(context, routine);
		}
	}
	
	private static String getStringByName(Context context, String name)
	{
		try
		{
			return  context.getResources().getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
		}
		catch (Exception e)
		{
			Log.d("GeoDebug", "Ressource not found: "+name);
			e.printStackTrace();
			return name;
		}
	}
}
