package com.aerilys.geowarfare.android.models;

public class Sector
{
	private String name;
	private String venueId;
	private double longitude;
	private double latitude;
	private String cityName;
	private String owner;
	private int units;
	private String image ="";
	private String category;

	public Sector(String name, String venueId, double longitude, double latitude, String owner, String cityName, int units)
	{
		super();
		this.name = name;
		this.venueId = venueId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cityName = cityName;
		this.owner = owner;
		this.units = units;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getVenueId()
	{
		return venueId;
	}

	public void setVenueId(String venueId)
	{
		this.venueId = venueId;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public int getUnits()
	{
		return units;
	}

	public void setUnits(int units)
	{
		this.units = units;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}
}
