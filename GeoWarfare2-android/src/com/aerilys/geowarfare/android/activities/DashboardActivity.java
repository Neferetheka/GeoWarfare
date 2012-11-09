package com.aerilys.geowarfare.android.activities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.fragments.ActivitiesFragment;
import com.aerilys.geowarfare.android.fragments.AllyFragment;
import com.aerilys.geowarfare.android.fragments.ArmyFragment;
import com.aerilys.geowarfare.android.fragments.GeoFragment;
import com.aerilys.geowarfare.android.fragments.ProfileFragment;
import com.aerilys.geowarfare.android.models.GeoEvent;
import com.aerilys.geowarfare.android.models.Sector;
import com.aerilys.geowarfare.android.models.Success;
import com.aerilys.geowarfare.android.tools.Cache;
import com.aerilys.geowarfare.android.tools.Converter;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.NetworkHelper;
import com.aerilys.geowarfare.android.tools.TabManager;
import com.aerilys.geowarfare.android.tools.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;

@EActivity(R.layout.activity_dashboard)
public class DashboardActivity extends SherlockFragmentActivity
{
	private static final int NUM_ITEMS = 4;
	private DashboardFragmentAdapter adapter;

	private ViewPager pager;
	public static boolean isPlayerProfileLoaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void bindDatas()
	{
		setProgressBarIndeterminateVisibility(false);
		adapter = new DashboardFragmentAdapter(getSupportFragmentManager());

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		pager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				updateUIFromPageSelection(position);
				// updateUIPlayer();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				;
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				;
			}
		});

		if (!isPlayerProfileLoaded)
		{
			loadPlayerProfile();
			setProgressBarIndeterminateVisibility(true);
		}
		else
			updateUIPlayer();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (DataContainer.getPlayerI().getKey() == null)
			this.finish();

		if (pager != null && !isPlayerProfileLoaded)
		{
			loadPlayerProfile();
			setProgressBarIndeterminateVisibility(true);
		}
	}

	@Background
	protected void loadPlayerProfile()
	{
		String result = null;
		try
		{
			result = NetworkHelper.HttpRequest(DataContainer.HOST + "getArmy?key="
					+ DataContainer.getPlayerI().getKey());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		loadPlayerProfileCompleted(result);
	}

	@UiThread
	protected void loadPlayerProfileCompleted(String result)
	{
		setProgressBarIndeterminateVisibility(false);
		if (result != null)
		{
			try
			{
				JSONObject jsonReponse = new JSONObject(result);

				DataContainer.getPlayerI().units = jsonReponse.getInt("units");
				DataContainer.getPlayerI().production = jsonReponse.getInt("production");

				DataContainer.getPlayerI().getListSectors().clear();
				JSONArray array = new JSONArray(jsonReponse.get("sectors").toString());
				for (int i = 0; i < array.length(); i++)
				{
					JSONObject json = array.getJSONObject(i);
					Sector sector = new Sector(json.getString("name"), json.getString("venueId"),
							json.getDouble("longitude"), json.getDouble("latitude"), json.getString("owner"),
							json.getString("cityName"), json.getInt("units"));

					sector.influence = json.getInt("influence");
					sector.development = json.getInt("development");

					DataContainer.getPlayerI().getListSectors().add(sector);
				}

				isPlayerProfileLoaded = true;

				setProgressBarIndeterminateVisibility(true);
				loadGeoEvents();
				loadSuccess();

				updateUIPlayer();
			}
			catch (Exception e)
			{
				UIHelper.toastError(this, e);
			}
		}
	}

	@Background
	protected void loadGeoEvents()
	{
		String result = null;

		try
		{
			String url = DataContainer.HOST + "getGeoEvents?key=" + DataContainer.getPlayerI().getKey();
			result = NetworkHelper.HttpRequest(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		loadGeoEventsCompleted(result);
	}

	@SuppressWarnings("unchecked")
	@UiThread
	protected void loadGeoEventsCompleted(String result)
	{
		setProgressBarIndeterminateVisibility(false);
		if (result == null)
		{
			UIHelper.toastConnexion(this);
		}
		else
		{
			try
			{
				Gson gson = new Gson();
				Type listType = new TypeToken<List<GeoEvent>>()
				{
				}.getType();

				DataContainer.getPlayerI().getListGeoEvents().clear();
				DataContainer.getPlayerI().setListGeoEvents((List<GeoEvent>) gson.fromJson(result, listType));
				for (GeoEvent event : DataContainer.getPlayerI().getListGeoEvents())
				{
					event.init();
				}

				updateUIFromPageSelection(0);
			}
			catch (Exception e)
			{
				UIHelper.toastError(this, e);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Background
	protected void loadSuccess()
	{
		if (DataContainer.getInstance().listSuccess != null && DataContainer.getInstance().listSuccess.size() > 0)
			return;
		String result = null;
		try
		{
			result = Cache.getItem(this, "successCache");

			if (result == null)
			{
				String url = DataContainer.HOSTPUBLIC + "XML/Success.xml";
				result = NetworkHelper.HttpRequest(url);

				Date date = new Date();
				date.setHours(date.getHours() + 48);
				Cache.setitem(this, "successCache", result, date);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		loadSuccessCompleted(result);
	}

	@UiThread
	protected void loadSuccessCompleted(String result)
	{
		if (result == null)
		{
			UIHelper.toastConnexion(this);
		}
		else
		{
			try
			{
				result = result.replace("ï»¿", "");
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				DocumentBuilder builder = factory.newDocumentBuilder();
				InputStream in = new ByteArrayInputStream(result.getBytes());
				Document dom = builder.parse(in);
				Element root = dom.getDocumentElement();
				NodeList successSet = root.getChildNodes();
				Node item;
				Node successNode;
				DataContainer.getInstance().listSuccess.clear();

				for (int i = 0; i < successSet.getLength(); i++)
				{
					item = successSet.item(i);
					if (item.getNodeName().toLowerCase().equals("success"))
					{
						Success success = new Success();
						for (int j = 0; j < item.getChildNodes().getLength(); j++)
						{
							successNode = item.getChildNodes().item(j);
							if (successNode.getNodeName().toLowerCase().equals("id"))
								success.setId(Integer.parseInt(successNode.getTextContent()));
							else if (successNode.getNodeName().toLowerCase().equals("nom"))
								success.setNom(successNode.getTextContent());
							else if (successNode.getNodeName().toLowerCase().equals("description"))
								success.setDescription(successNode.getTextContent());
							else if (successNode.getNodeName().toLowerCase().equals("objectif"))
								success.setObjectif(successNode.getTextContent());
							else if (successNode.getNodeName().toLowerCase().equals("ishidden"))
								success.setHidden(Converter.convertStringToBool(successNode.getTextContent()));
							else if (successNode.getNodeName().toLowerCase().equals("iselite"))
								success.setElite(Converter.convertStringToBool(successNode.getTextContent()));
							else if (successNode.getNodeName().toLowerCase().equals("ptsrecompense"))
								success.setPtsRecompense(Integer.parseInt(successNode.getTextContent()));
						}
						try
						{
							int id = this.getResources().getIdentifier(
									success.getNom().toLowerCase().replace(" ", "_").replace("é", "e").replace("'", "")
											.replace("è", "e").replace("ô", "o").replace("î", "i"), "drawable",
									this.getPackageName());
							if (i > 0)
								success.Image = id;
							else
								success.Image = R.drawable.question;
						}
						catch (Exception e)
						{
							success.Image = R.drawable.question;
						}
						DataContainer.getInstance().listSuccess.add(success);
					}
				}
			}
			catch (Exception e)
			{
				Log.d("GeoError", "Erreur de chargement des fichiers ! " + e.getMessage());
				UIHelper.toastError(this, e);
			}
		}
	}

	private void updateUIPlayer()
	{
		((GeoFragment) adapter.getItem(1)).datasLoaded();
	}

	protected void updateUIFromPageSelection(int position)
	{
		((GeoFragment) adapter.getItem(position)).datasLoaded();
	}

	public void onActivitiesClick(View v)
	{
		pager.setCurrentItem(0, true);
	}

	public void onArmyClick(View v)
	{
		pager.setCurrentItem(1, true);
	}

	public void onAllyClick(View v)
	{
		pager.setCurrentItem(2, true);
	}

	public void onProfileClick(View v)
	{
		pager.setCurrentItem(3, true);
	}

	public void sectorsClick(View v)
	{
		TabManager.navigate(this, SectorActivity_.class);
	}

	public void successClick(View v)
	{
		TabManager.navigate(this, SuccessActivity_.class);
	}

	public static class DashboardFragmentAdapter extends FragmentPagerAdapter
	{
		ActivitiesFragment activitiesFragment = new ActivitiesFragment();
		ArmyFragment armyFragment = new ArmyFragment();
		ProfileFragment profileFragment = new ProfileFragment();
		AllyFragment allyFragment = new AllyFragment();

		public DashboardFragmentAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public int getCount()
		{
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position)
		{
			if (position == 0)
				return activitiesFragment;
			else if (position == 1)
				return armyFragment;
			else if (position == 2)
				return allyFragment;
			else if (position == 3)
				return profileFragment;
			return new ActivitiesFragment();
		}
	}
}
