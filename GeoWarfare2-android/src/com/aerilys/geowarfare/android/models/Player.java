package com.aerilys.geowarfare.android.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player
{
	private String login;
	private String password;
	private String key;
	private String biography;
	private String avatar;
	
	private int units;
	private int production;
	
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
			if(sector.getVenueId().equals(venueId))
				return sector;
		}
		return null;
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

	public int getUnits()
	{
		return units;
	}

	public void setUnits(int units)
	{
		this.units = units;
	}

	public int getProduction()
	{
		return production;
	}

	public void setProduction(int production)
	{
		this.production = production;
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
