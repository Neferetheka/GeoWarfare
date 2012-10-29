package com.aerilys.geowarfare.android.api.foursquare;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.NetworkHelper;

import android.util.Log;

public abstract class FoursquareManager
{
	public static final String urlVenueApi = "https://api.foursquare.com/v2/venues/search?";
	public static final String CLIENTID = "JA3LFHKLOQSASKIEKPNJBEMHC0ROZERHQE55FHYCD0JDEXNL";
	public static final String CLIENTSECRET = "QZIP133AKMYTH1RZ0VPLLCHWA2GGHZFU1YYV2JJL2SKNPW4A";
	public static final String loginUrl = "https://foursquare.com/oauth2/authenticate" + "?display=touch&client_id="
			+ FoursquareManager.CLIENTID + "&response_type=token" + "&redirect_uri=http://www.geowarfare.net";
	public static final String profileUrl = "https://api.foursquare.com/v2/users/self";
	public static final String checkinUrl = "https://api.foursquare.com/v2/checkins/";
	public static final String myCheckinsUrl = "https://api.foursquare.com/v2/users/self/checkins";

	public static List<Venue> getVenuesAroundMe(double latitude, double longitude)
	{
		List<Venue> listVenues = new ArrayList<Venue>();

		String key = "4sqcache_" + latitude + "_" + longitude;
		String result = "";
		if (DataContainer.getInstance().getTempCache().get(key) != null)
			result = DataContainer.getInstance().getTempCache().get(key);
		else
		{
			result = NetworkHelper.HttpRequest(urlVenueApi + "ll=" + latitude + "," + longitude + "&client_id="
					+ CLIENTID + "&client_secret=" + CLIENTSECRET + "&limit=7&v=" + getFormattedDate());
			DataContainer.getInstance().getTempCache().put(key, result);
		}
		
		JSONObject jsonReponse;
		try
		{
			jsonReponse = new JSONObject(result);
			JSONArray array = jsonReponse.getJSONObject("response").getJSONArray("venues");
			for (int i = 0; i < array.length(); i++)
			{
				JSONObject json = array.getJSONObject(i);
				try
				{
					listVenues.add(new Venue(json.getString("name"), json.getString("id"), json
							.getJSONObject("location"), json.getJSONArray("categories")));
				}
				catch (Exception e)
				{
					Log.e("Foursquare", "Erreur de parse du json Foursquare : " + e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return listVenues;
	}

	public static String publishCheckin(Venue venue, String shout)
	{
		String url = FoursquareManager.checkinUrl + "add";
		HashMap<String, String> parameters = new HashMap<String, String>(4);
		parameters.put("oauth_token", DataContainer.getPlayerI().AccessToken);
		parameters.put("venueId", venue.Id);
		// parameters.put("broadcast", "private");
		parameters.put("shout", shout);
		parameters.put("v", FoursquareManager.getFormattedDate());
		return NetworkHelper.HttpRequest(url, parameters);
	}

	public static String getFormattedDate()
	{
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date);
	}

	@SuppressWarnings("deprecation")
	public static List<Checkin> getCheckins()
	{
		List<Checkin> listCheckins = new ArrayList<Checkin>();
		String url = myCheckinsUrl + "?oauth_token=" + DataContainer.getPlayerI().AccessToken + "&limit=180&v="
				+ getFormattedDate();
		String result = NetworkHelper.HttpRequest(url);

		JSONObject jsonReponse;
		try
		{
			jsonReponse = new JSONObject(result);
			JSONArray array = jsonReponse.getJSONObject("response").getJSONObject("checkins").getJSONArray("items");
			Checkin checkin;
			for (int i = 0; i < array.length(); i++)
			{
				JSONObject json = array.getJSONObject(i);
				try
				{
					checkin = new Checkin();
					checkin.Id = json.getString("id");
					Long date = Long.parseLong(json.getString("createdAt") + "000");
					checkin.date = new Date(date).toLocaleString();
					json = json.getJSONObject("venue");
					checkin.setVenue(new Venue(json.getString("name"), json.getString("id"), json
							.getJSONObject("location"), json.getJSONArray("categories")));
					listCheckins.add(checkin);
				}
				catch (Exception e)
				{
					Log.e("Foursquare", "Erreur de parse du json Foursquare sur getCheckins : " + e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return listCheckins;
	}
}
