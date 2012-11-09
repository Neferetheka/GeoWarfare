package com.aerilys.geowarfare.android.activities;


import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Sector;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.TaskHelper;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_result_battle)
public class ResultBattleActivity extends SherlockActivity
{
	String result;
	String sectorName;
	String stringToShare;
	int unitsCount;

	@ViewById
	protected TextView resultBattleLabel;
	@ViewById
	protected TextView lossBattleLabel;
	@ViewById
	protected TextView resultBattleSectorName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		result = getIntent().getExtras().getString("result");
		unitsCount = getIntent().getExtras().getInt("unitsCount");

		if (result == null)
			this.finish();
	}

	@Override
	public void onBackPressed()
	{
		NavUtils.navigateUpFromSameTask(this);
	}

	@AfterViews
	public void bindDatas()
	{
		this.getSherlock().getActionBar().setTitle(R.string.battle_result);
		this.getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);

		try
		{
			sectorName = DataContainer.getInstance().currentSector.getName();

			JSONObject json = new JSONObject(result);
			String resultBattle = json.getString("result");
			int difference = json.getInt("difference");

			// Secteur neutre
			if (resultBattle.equals("wn"))
			{
				resultBattle = getString(R.string.battle_result_neutral_sector);
				difference = unitsCount - difference;
				stringToShare = getString(R.string.the_general) + DataContainer.getPlayerI().getLogin()
						+ getString(R.string.has_conquest) + sectorName;
				
				DataContainer.getInstance().currentSector.setUnits(difference);
				Sector sector = Sector.clone(DataContainer.getInstance().currentSector);
				DataContainer.getPlayerI().getListSectors().add(sector);
			}
			else if (resultBattle.equals("w"))
			{
				resultBattle = getString(R.string.battle_result_win)
						+ DataContainer.getInstance().currentSector.getOwner();
				difference = unitsCount - difference;
				stringToShare = getString(R.string.the_general) + DataContainer.getPlayerI().getLogin()
						+ getString(R.string.has_conquest) + sectorName + getString(R.string.against_general)
						+ DataContainer.getInstance().currentSector.getOwner();
				
				DataContainer.getInstance().currentSector.setUnits(difference);
				Sector sector = Sector.clone(DataContainer.getInstance().currentSector);
				DataContainer.getPlayerI().getListSectors().add(sector);
			}
			else if (resultBattle.equals("e"))
			{
				resultBattle = getString(R.string.battle_result_equal)
						+ DataContainer.getInstance().currentSector.getOwner();
				stringToShare = getString(R.string.terrible_battle_between) + DataContainer.getPlayerI().getLogin()
						+ getString(R.string.and_the_general) + DataContainer.getInstance().currentSector.getOwner();
			}
			else
			{
				resultBattle = getString(R.string.battle_result_defeat)
						+ DataContainer.getInstance().currentSector.getOwner();
				difference = unitsCount - difference;
				stringToShare = getString(R.string.the_general) + DataContainer.getPlayerI().getLogin()
						+ getString(R.string.has_lost) + getString(R.string.the_general)
						+ DataContainer.getPlayerI().getLogin();
			}

			resultBattleSectorName.setText(sectorName);
			resultBattleLabel.setText(resultBattle);
			lossBattleLabel.setText(getString(R.string.loss) + difference);

			DataContainer.getInstance().currentSector = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem itemCheckin = menu.add(R.string.share);
		itemCheckin.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		itemCheckin.setIcon(R.drawable.ic_action_share);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, android.view.MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		else if (item.getTitle().equals(getString(R.string.share)))
		{
			TaskHelper.shareTask(this, getString(R.string.share_battle_result), stringToShare);
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		else if (item.getTitle().equals(getString(R.string.share)))
		{
			TaskHelper.shareTask(this, getString(R.string.share_battle_result), stringToShare);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
