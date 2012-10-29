package models;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class GeoEvent extends Model
{
	public enum GeoEventType
	{
		Battle, Success, Ally, Event, Production
	};

	@Id
	public Long id;

	@ManyToOne
	public Player player;

	public String title;
	public String content;

	public Date datePublication;
	public GeoEventType geoEventType;
}
