package com.aerilys.geowarfare.android.tools;

public abstract class HTMLHelper
{
	public static String convertFromHTML(String chaine)
	{
		return chaine.replace("&eacute", "�").replace("&egrave", "�").replace("&agrave", "�").replace("&ecirc", "�")
				.replace("&ocirc", "�").replace("&ucirc", "�").replace("&ugrave", "�").replace("&ccedil", "�")
				.replace("&acirc", "�");
	}

	public static String convertFromHTML(String chaine, boolean isSpecial)
	{
		return chaine.replace("&eacute;", "�").replace("&egrave;", "�").replace("&agrave;", "�")
				.replace("&ecirc;", "�").replace("&ocirc;", "�").replace("&ucirc;", "�").replace("&ugrave;", "�")
				.replace("&ccedil;", "�").replace("&acirc;", "�");
	}
}
