package com.aerilys.geowarfare.android.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aerilys.geowarfare.android.R;

import android.content.Context;

public class Player
{
	public enum Factions
	{
		NMO, Navaho, Dynasty
	}

	private String login;
	private String password;
	private String key;
	private String biography;
	private String avatar;

	public int units;
	public int production;
	public int faction;

	private List<Sector> listSectors = new ArrayList<Sector>();
	private List<GeoEvent> listGeoEvents = new ArrayList<GeoEvent>();

	public Player(String login, String password, String key)
	{
		this.login = login;
		this.password = password;
		this.key = key;
	}

	public Sector getSectorByVenueId(String venueId)
	{
		for (Sector sector : listSectors)
		{
			if (sector.getVenueId().equals(venueId))
				return sector;
		}
		return null;
	}
	
	public static String getFactionNameFromIndex(Context context, int index)
	{
		int id = -1;
		switch(index)
		{
			case 0:
				id = R.string.NMO; 
				break;
			case 1:
				id = R.string.Navaho;
				break;
			case 2:
				id = R.string.Dynasty;
				break;
		}
		
		if(id != -1)
			return context.getString(id);
		else
			return "";
	}

	public String AccessToken;

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getKey()
	{
		return key;
	}

	public List<Sector> getListSectors()
	{
		return listSectors;
	}

	public List<GeoEvent> getListGeoEvents()
	{
		return listGeoEvents;
	}

	public void setListGeoEvents(List<GeoEvent> listGeoEvents)
	{
		this.listGeoEvents = listGeoEvents;
		Collections.reverse(this.listGeoEvents);
	}
	
	public String getBiography()
	{
		return biography;
	}

	public void setBiography(String biography)
	{
		this.biography = biography;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
}
