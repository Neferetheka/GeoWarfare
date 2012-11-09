package com.aerilys.geowarfare.android.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Success;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.views.adapters.SuccessAdapter;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_success)
public class SuccessActivity extends SherlockActivity
{
	@ViewById
	public ListView listviewSuccess;
	private SuccessAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void bindDatas()
	{
		this.getSupportActionBar().setTitle(getString(R.string.success));
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (adapter == null)
			adapter = new SuccessAdapter(this, DataContainer.getInstance().listSuccess);

		listviewSuccess.setAdapter(adapter);

		listviewSuccess.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
			{
				selectSuccess(index);
			}
		});
		listviewSuccess.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				longSelectSuccess(position);
				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			this.finish();
		return super.onOptionsItemSelected(item);
	}

	protected void selectSuccess(int index)
	{
		final Success selectedSuccess = adapter.listeSuccess.get(index);

		Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_success);
		dialog.setTitle(selectedSuccess.getNom());
		dialog.setCanceledOnTouchOutside(true);

		TextView text = (TextView) dialog.findViewById(R.id.customSuccessObjectif);
		text.setText("Objectif : " + selectedSuccess.getObjectif());

		((TextView) dialog.findViewById(R.id.customSuccessPts)).setText("Points de succès : "
				+ selectedSuccess.getPtsRecompense());

		TextView textDescription = (TextView) dialog.findViewById(R.id.customSuccessDescription);
		textDescription.setText(selectedSuccess.getDescription());

		ImageView image = (ImageView) dialog.findViewById(R.id.customSuccessImage);

		if (selectedSuccess.isDone())
		{
			((LinearLayout) dialog.findViewById(R.id.customSuccessLayoutShare)).setVisibility(View.VISIBLE);
			//((LinearLayout) dialog.findViewById(R.id.customSuccessLayoutShare)).startAnimation(successAnimation);
		}

		try
		{
			image.setImageDrawable(this.getResources().getDrawable(selectedSuccess.Image));
		}
		catch (Exception e)
		{
			image.setImageDrawable(this.getResources().getDrawable(R.drawable.question));
		}

		/*if (!selectedSuccess.isDone())
			((Button) dialog.findViewById(R.id.shareButton)).setVisibility(View.GONE);

		((Button) dialog.findViewById(R.id.shareButton)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				shareSuccess(selectedSuccess);
			}
		});*/

		dialog.show();

	}
	
	public void longSelectSuccess(final int position)
	{
		final Success selectedSuccess = adapter.listeSuccess.get(position);
		final CharSequence[] items =
		{ "Partager"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Actions");
		builder.setItems(items, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				switch (item)
				{
					case 0:
						shareSuccess(selectedSuccess);
						break;
				}
			}
		});
		builder.show();
	}

	protected void shareSuccess(Success selectedSuccess)
	{
		
	}

}
