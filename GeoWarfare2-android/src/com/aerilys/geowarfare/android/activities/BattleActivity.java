package com.aerilys.geowarfare.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Sector;
import com.aerilys.geowarfare.android.tools.Converter;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.ImageThreadLoader;
import com.aerilys.geowarfare.android.tools.ImageThreadLoader.ImageLoadedListener;
import com.aerilys.geowarfare.android.tools.NetworkHelper;
import com.aerilys.geowarfare.android.tools.Strings;
import com.aerilys.geowarfare.android.tools.UIHelper;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_battle)
public class BattleActivity extends SherlockActivity
{
	Sector currentSector;

	@ViewById
	protected EditText battleUnitEdit;
	@ViewById
	protected TextView battleUnitLabel;
	@ViewById
	protected TextView battleSectorLabel;
	@ViewById
	protected ImageView battleSectorImage;

	ProgressDialog dialog;
	private ImageThreadLoader imageLoader = new ImageThreadLoader();

	private int unitsCount;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		currentSector = DataContainer.getInstance().currentSector;
	}

	@Override
	protected void onResume()
	{
		if (currentSector == null)
			this.finish();

		super.onResume();
	}

	@AfterViews
	public void bindDatas()
	{
		getSherlock().getActionBar().setTitle(currentSector.getName());
		getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);

		battleUnitLabel.setText(getString(R.string.number) + DataContainer.getPlayerI().getUnits());

		if (Strings.isNullOrEmpty(currentSector.getOwner()))
			battleSectorLabel.setText(getString(R.string.neutral_sector));
		else
			battleSectorLabel.setText(getString(R.string.zone_possessed_by) + currentSector.getOwner());

		if (dialog == null)
		{
			dialog = new ProgressDialog(this);
			dialog.setTitle(getString(R.string.battle_in_progress));
			dialog.setMessage(getString(R.string.battle_is_going_crazy));
		}
		else
			dialog.show();

		Bitmap cachedImage = null;

		final String key = "cache_" + currentSector.getCategory();

		cachedImage = DataContainer.getInstance().getBitmapCache().get(key);

		if (cachedImage == null)
		{
			try
			{
				cachedImage = imageLoader.loadImage(currentSector.getImage(), new ImageLoadedListener()
				{
					public void imageLoaded(Bitmap imageBitmap)
					{
						battleSectorImage.setImageBitmap(imageBitmap);
						if (DataContainer.getInstance().getBitmapCache().get(key) == null)
							DataContainer.getInstance().getBitmapCache().put(key, imageBitmap);
					}
				});

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (cachedImage != null)
		{
			battleSectorImage.setImageBitmap(cachedImage);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem itemCheckin = menu.add(R.string.valid);
		itemCheckin.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		itemCheckin.setIcon(R.drawable.ic_action_done);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Since we have only one menu item, we don't have to discriminate by id
		// or label

		if (item.getItemId() == android.R.id.home)
		{
			this.finish();
			return super.onOptionsItemSelected(item);
		}

		unitsCount = Converter.ctI(battleUnitEdit.getText().toString());
		if (unitsCount <= 0 || unitsCount > DataContainer.getPlayerI().getUnits())
		{
			UIHelper.toast(this, getString(R.string.have_to_send_units), true);
			return super.onOptionsItemSelected(item);
		}

		dialog.show();
		startBattle();

		return super.onOptionsItemSelected(item);
	}

	@Background
	protected void startBattle()
	{
		String result = null;
		try
		{
			String url = DataContainer.HOST + "battle?key=" + DataContainer.getPlayerI().getKey() + "&venueId="
					+ currentSector.getVenueId() + "&units=" + unitsCount;
			result = NetworkHelper.HttpRequest(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		startBattleCompleted(result);
	}

	@UiThread
	protected void startBattleCompleted(String result)
	{
		dialog.dismiss();
		if (result == null)
		{
			UIHelper.toastConnexion(this);
		}
		else if (result.contains("DOCTYPE"))
			UIHelper.toastError(this, new Exception("Wrong parameter or server error"));
		else
		{
			Intent intent = new Intent(this, ResultBattleActivity_.class);
			intent.putExtra("result", result);
			intent.putExtra("unitsCount", unitsCount);
			startActivity(intent);
		}
	}
}
