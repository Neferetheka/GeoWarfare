package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.GeoEvent.GeoEventType;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Player extends Model
{
	@Id
    public Long id;
	
	@Required
	public String login;
	
	@Required
	public String password;
	
	public Date dateInscription;
	public Date dateConnexion;
	
	public int faction = 0;
	public int units = 0;
	public int production = 1;
	
	public String avatar;
	public String biography;
	public String success = "";
	
	public int spiesNb = 0;
	
	@ManyToOne
	public Ally alliance;

	/*@ManyToOne
	public Alliance candidature;*/
	public String messageCandidature = "";
	
	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
	public List<DeviceInfo> listDevices;
	
	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
	public List<GeoEvent> listGeoEvents;

	@OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
	public List<GeoMessage> listMessages;

	
	public static Finder<Long,Player> find = new Finder<Long,Player>(
			Long.class, Player.class
			);
	
	public static void addGeoEvent(Player player, String title, String content, GeoEventType type)
	{
		GeoEvent event = new GeoEvent();
		event.title = title;
		event.content = content;
		event.geoEventType = type;
		event.datePublication = new Date();
		
		player.listGeoEvents.add(event);
		event.player = player;
		event.save();
		player.save();
	}
}
