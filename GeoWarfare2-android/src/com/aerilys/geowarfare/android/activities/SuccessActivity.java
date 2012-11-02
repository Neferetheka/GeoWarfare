package com.aerilys.geowarfare.android.activities;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.aerilys.geowarfare.android.R;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_success)
public class SuccessActivity extends SherlockActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	 @AfterViews
	 public void bindDatas()
	 {
		  
	 } 
}
