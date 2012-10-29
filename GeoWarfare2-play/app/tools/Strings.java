package tools;

public abstract class Strings
{
	public static boolean isNullOrEmpty(String string)
	{
		return string == null || string.trim().length() == 0;
	}

	public static String ucfirst(String chaine)
	{
		return chaine.substring(0, 1).toUpperCase() + chaine.substring(1).toLowerCase();
	}
}