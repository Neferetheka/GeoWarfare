package com.aerilys.geowarfare.android;

import com.aerilys.geowarfare.android.activities.LoginActivity_;
import com.aerilys.geowarfare.android.tools.TabManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class MainActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loginClick(null);
	}
	
	public void loginClick(View v)
	{
		TabManager.navigate(this, LoginActivity_.class);
	}

}
