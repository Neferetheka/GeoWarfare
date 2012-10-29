package com.aerilys.geowarfare.android.tools;

public abstract class Strings
{
	public static boolean isNullOrEmpty(String string)
	{
		return string == null || string.trim().length() == 0;
	}

	public static String ucfirst(String string)
	{
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
	
	public static String pluralize(String string, int count)
	{
		return (count > 1) ? string+"s" : string;
	}

	public static String mergeArray(String[] strings, String separator)
	{
		String result = "";
		for (String string : strings)
		{
			result += string + separator;
		}
		result = result.substring(0, result.length() - 2);
		return result;
	}
}
