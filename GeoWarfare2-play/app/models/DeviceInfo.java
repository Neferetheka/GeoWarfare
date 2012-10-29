package models;

import javax.persistence.*;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DeviceInfo extends Model
{
	@Id
	public Long id;

	@ManyToOne
	public Player player;

	public String url;
	public String type;

	public DeviceInfo(Player player, String url, String type)
	{
		super();
		this.player = player;
		this.url = url;
		this.type = type;
	}

}
