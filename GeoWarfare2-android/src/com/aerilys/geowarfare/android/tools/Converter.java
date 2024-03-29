package com.aerilys.geowarfare.android.tools;

import com.google.gson.Gson;

public abstract class Converter
{
	public static Gson gson = new Gson();
	
	public static int ctI(String chaine)
	{
		try
		{
			return parseInt(chaine);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/* Permit to convert String to int Code from
	 * https://github.com/nasa/World-Wind
	 * -Java/blob/master/WorldWind/src/org/codehaus/jackson/io/NumberInput.java
	 * NASA Code \o/ "Houston, we've got a problem" */
	public final static int parseInt(String chaine)
	{
		char[] digitChars = chaine.toCharArray();
		int offset = 0, len = digitChars.length;
		int num = digitChars[offset] - '0';
		len += offset;
		// This looks ugly, but appears the fastest way:
		if (++offset < len)
		{
			num = (num * 10) + (digitChars[offset] - '0');
			if (++offset < len)
			{
				num = (num * 10) + (digitChars[offset] - '0');
				if (++offset < len)
				{
					num = (num * 10) + (digitChars[offset] - '0');
					if (++offset < len)
					{
						num = (num * 10) + (digitChars[offset] - '0');
						if (++offset < len)
						{
							num = (num * 10) + (digitChars[offset] - '0');
							if (++offset < len)
							{
								num = (num * 10) + (digitChars[offset] - '0');
								if (++offset < len)
								{
									num = (num * 10) + (digitChars[offset] - '0');
									if (++offset < len)
									{
										num = (num * 10) + (digitChars[offset] - '0');
									}
								}
							}
						}
					}
				}
			}
		}
		return num;
	}
	
	public static boolean convertStringToBool(String textContent)
	{
		if (textContent.equals("true"))
			return true;
		else
			return false;
	}
}
