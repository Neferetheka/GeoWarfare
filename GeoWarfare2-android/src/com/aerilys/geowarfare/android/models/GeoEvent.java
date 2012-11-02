package com.aerilys.geowarfare.android.models;


public class GeoEvent
{
	public enum GeoEventType
	{
		Battle, Success, Ally, Event, Production
	};

	private String title;
	private String content;

	private String datePublication;
	private GeoEventType geoEventType;
	private String image;
	private String type;
	 
	//Basically this method get the right enum for the type according to the type as String
	public void init()
	{
		if(type.equals("Battle"))
			geoEventType = GeoEventType.Battle;
		else if(type.equals("Success"))
			geoEventType = GeoEventType.Success; 
		else if(type.equals("Ally"))
			geoEventType = GeoEventType.Ally; 
		else if(type.equals("Event"))
			geoEventType = GeoEventType.Event; 
		else if(type.equals("Production"))
			geoEventType = GeoEventType.Production; 
	}
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getDatePublication()
	{
		return datePublication;
	}
	public void setDatePublication(String datePublication)
	{
		this.datePublication = datePublication;
	}
	public GeoEventType getGeoEventType()
	{
		return geoEventType;
	}
	public void setGeoEventType(GeoEventType geoEventType)
	{
		this.geoEventType = geoEventType;
	}
	public String getImage()
	{
		return image;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
}
