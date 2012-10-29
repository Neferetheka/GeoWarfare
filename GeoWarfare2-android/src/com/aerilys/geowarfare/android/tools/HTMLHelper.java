package com.aerilys.geowarfare.android.tools;

public abstract class HTMLHelper
{
	public static String convertFromHTML(String chaine)
	{
		return chaine.replace("&eacute", "é").replace("&egrave", "è").replace("&agrave", "à").replace("&ecirc", "ê")
				.replace("&ocirc", "ô").replace("&ucirc", "û").replace("&ugrave", "ù").replace("&ccedil", "ç")
				.replace("&acirc", "â");
	}

	public static String convertFromHTML(String chaine, boolean isSpecial)
	{
		return chaine.replace("&eacute;", "é").replace("&egrave;", "è").replace("&agrave;", "à")
				.replace("&ecirc;", "ê").replace("&ocirc;", "ô").replace("&ucirc;", "û").replace("&ugrave;", "ù")
				.replace("&ccedil;", "ç").replace("&acirc;", "â");
	}
}
