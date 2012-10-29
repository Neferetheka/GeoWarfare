package com.aerilys.geowarfare.android.tools;

import com.aerilys.geowarfare.android.R;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Toast;

public abstract class UIHelper
{
	/* Permet de faire une toast notification */
	public static void toast(Context context, String message, boolean islong)
	{
		Toast.makeText(context, message, (islong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	/* Permet de lancer une status bar notification */
	@SuppressWarnings("deprecation")
	public static void statusBarNotification(Context context, String title, String message, Intent intent, int number)
	{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, message, when);

		CharSequence contentTitle = title;
		CharSequence contentText = message;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
		if (number != 0)
			notification.number = number;

		mNotificationManager.notify(0, notification);
	}

	/* Permet d'afficher une boite de dialogue classique */
	public static void alertDialog(Context context, String message, String titre)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(titre);
		dialog.setMessage(message);
		dialog.show();
	}

	/* Permet d'afficher une boite de dialogue classique avec un titre par
	 * défaut */
	public static void alertDialog(Context context, String message)
	{
		alertDialog(context, message, "Message");
	}

	/* Permet d'ouvrir une boite de dialogue contenant un webView */
	/*public static void openWebDialog(final Activity context, String title, final String url)
	{
		final Dialog dialog = new Dialog(context);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setContentView(R.layout.customwebviewdialog);
		dialog.setTitle(title);
		WebView webView = new WebView(context);
		webView.loadUrl(url);
		((LinearLayout) dialog.findViewById(R.id.guideLinearLayout)).addView(webView);
		((Button) dialog.findViewById(R.id.guideContinueButton)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog.dismiss();

				if (url.contains("introduction"))
				{
					Main.changeHelpState(context, false);
					TabManager.navigate(context, GuideActivity.class);
				}
			}
		});

		((Button) dialog.findViewById(R.id.guideDisableButton)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Main.changeHelpState(context, false);
				dialog.dismiss();
			}
		});

		dialog.show();
	}*/

	/* Permet de vérifier si le device est une tablette ou non */
	public static boolean isTablet(Context context)
	{
		try
		{
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			float screenWidth = dm.widthPixels / dm.xdpi;
			float screenHeight = dm.heightPixels / dm.ydpi;
			double size = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));
			return size >= 6;
		}
		catch (Throwable t)
		{
			return false;
		}
	}

	public static void toastConnexion(Context context)
	{
		UIHelper.toast(context, context.getString(R.string.no_network), true);
	}

	public static void toastError(Context context, Exception e)
	{
		UIHelper.toast(context, "Une erreur est survenue. On essayera de corriger ça au plus vite !", true);
		e.printStackTrace();
	}
}
