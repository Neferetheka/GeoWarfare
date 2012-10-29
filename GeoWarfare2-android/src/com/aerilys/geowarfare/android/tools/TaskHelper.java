package com.aerilys.geowarfare.android.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public abstract class TaskHelper
{
	/*
	 * Permet de lancer une tâche de partage
	 */
	public static void shareTask(Activity activity, String title, String content)
	{
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, content
				+ " - via GeoWarfare pour Android");
		activity.startActivity(Intent.createChooser(intent, title));
	}

	/*
	 * Permet de lancer le navigateur vers une URI précise
	 */
	public static void browserTask(Activity activity, String lien)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lien));
		activity.startActivity(intent);	
	}
	
	/*
	 * Permit to launch market at app page
	 */
	public static void marketplaceTask(Context context)
	{
		Uri uri = Uri.parse("market://details?id="+context.getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
}
