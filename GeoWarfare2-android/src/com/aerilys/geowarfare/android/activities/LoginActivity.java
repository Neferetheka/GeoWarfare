package com.aerilys.geowarfare.android.activities;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Player;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.NetworkHelper;
import com.aerilys.geowarfare.android.tools.TabManager;
import com.aerilys.geowarfare.android.tools.UIHelper;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity
{
	private boolean isLoading;
	private boolean isFirstStart = false;

	@ViewById
	public EditText loginEdit;
	@ViewById
	public EditText passwordEdit;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void bindDatas()
	{
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

		String login = prefs.getString("login", "");
		loginEdit.setText(login);

		String password = prefs.getString("password", "");
		passwordEdit.setText(password);
	}

	public void loginClick(View v)
	{
		if(!NetworkHelper.isConnected(this))
		{
			UIHelper.toastConnexion(this);
			return;
		}
		
		if (checkFields() && !isLoading)
		{
			isLoading = true;

			if (isFirstStart)
				;// splashscreen.setVisibility(View.VISIBLE);
			else
			{
				dialog = new ProgressDialog(this);
				dialog = ProgressDialog.show(this, getString(R.string.connexion), getString(R.string.loading_progress));
			}

			initConnexion();
		}
	}

	@Background
	public void initConnexion()
	{
		try
		{
			String pseudo = loginEdit.getText().toString().trim();
			String password = passwordEdit.getText().toString().trim();

			initConnexionCompleted(NetworkHelper.HttpRequest(DataContainer.HOST + "auth?p=" + pseudo + "&md=" + password));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@UiThread
	protected void initConnexionCompleted(String result)
	{
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}

		if (result != null)
		{
			if (result.length() > 3 && result.length() < 50)
			{
				String login = loginEdit.getText().toString().trim();
				String password = passwordEdit.getText().toString().trim();

				SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("login", login);
				editor.putString("password", password);
				editor.commit();

				// alert(this, "Connexion réussie !", Toast.LENGTH_SHORT);
				DataContainer.getInstance().player = new Player(login, password, result);
				TabManager.navigate(this, DashboardActivity_.class);
			}
			else
			{
				failAtLogin();
				UIHelper.toast(this, getString(R.string.wrong_login), true);
				/* splashscreen.setVisibility(View.GONE); */
			}
		}
		else
		{
			UIHelper.toastConnexion(this);
			/* splashscreen.setVisibility(View.GONE); */
		}

		isLoading = false;
	}

	public void failAtLogin()
	{
		shakeMdp();
		((EditText) findViewById(R.id.passwordEdit)).setText("");
	}

	public boolean checkFields()
	{
		String login = loginEdit.getText().toString().trim();
		String password = passwordEdit.getText().toString().trim();

		if (login.length() < 3 || password.length() < 3)
		{
			shakeMdp();
			return false;
		}
		else
			return true;
	}

	/**
	 * Permet d'animer le champ de mot de passe
	 */
	private void shakeMdp()
	{
		 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 findViewById(R.id.passwordEdit).startAnimation(shake); 
	}

}
