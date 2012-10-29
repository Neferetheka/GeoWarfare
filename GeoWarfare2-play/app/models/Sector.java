package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Sector extends Model
{
	@Id
	public Long id;

	public String name;
	public String venueId;
	public double longitude;
	public double latitude;
	public String cityName;
	public String owner;
	public String category;
	public int units;

	public Sector(String name, String venueId, double longitude, double latitude, String owner, String cityName, String category, int units)
	{
		super();
		this.name = name;
		this.venueId = venueId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.cityName = cityName;
		this.owner = owner;
		this.category = category;
		this.units = units;
	}

	public static Finder<Long, Sector> find = new Finder<Long, Sector>(Long.class, Sector.class);
}
