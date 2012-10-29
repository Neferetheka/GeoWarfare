package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class City extends Model
{
	@Id
	public Long id;
	
	public String name;
	public double minLatitude;
	public double minLongitude;
	public double maxLatitude;
	public double maxLongitude;
	
	public static Finder<Long,City> find = new Finder<Long,City>(
			Long.class, City.class
			);
}
