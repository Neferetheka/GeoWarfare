package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class GeoMessage extends Model
{
	@Id
	public Long id;
	
	@ManyToOne
	public Player destinataire;

	public String expediteur;
	public String sujet;

	public String content;
	public Date date;
	public boolean isRead = false;
}
