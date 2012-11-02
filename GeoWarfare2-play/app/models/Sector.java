package models;

import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.GeoEvent.GeoEventType;

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

	public int influence = 100;
	public int development = 0;

	public Sector(String name, String venueId, double longitude, double latitude, String owner, String cityName,
			String category, int units)
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

	public void changeInfluence(int amount)
	{
		Player playerOwner = Player.find.where().eq("login", this.owner).findUnique();
		int playerFactionIndex = playerOwner.faction + 1;

		int factionIndex = (int) Math.floor(this.influence / 100);
		int actualInfluence = this.influence % 100;

		if (factionIndex != playerFactionIndex)
		{
			amount *= -1;
		}

		actualInfluence += amount;

		if (actualInfluence > 99)
			actualInfluence = 99;
		else if (actualInfluence < 0)
		{
			factionIndex = playerFactionIndex;
			actualInfluence = 1;
		}

		this.influence = factionIndex * 100 + actualInfluence;
	}

	public void changeDevelopment(int amount)
	{
		this.development += amount;
		if (this.development > 99)
			this.development = 99;
		else if (this.development < 0)
			this.development = 0;
	}

	public void generateEvent(Random rand)
	{
		int influenceFaction = (int) Math.floor(this.influence / 100);
		int realInfluenceSection = (int) Math.floor(this.influence % 100 / 25);
		int event = rand.nextInt(9);

		Player playerOwner = Player.find.where().eq("login", this.owner).findUnique();

		switch (influenceFaction)
		{
			case 2: // Navaho
				break;
			case 3: // Dynastie
				break;
			default: // Act as 1 - NMO
				switch (realInfluenceSection)
				{
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					default: // Act as 0
						switch (event)
						{
							default: // Météorite
								this.units -= 10;
								if (this.units < 0)
									this.units = 0;
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_0",
										"ROUTINE_EVENT_NMO_0_0_description|" + this.name, GeoEventType.Event);
								break;
							case 1: // Salon de la technologie
								this.changeInfluence(2);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_1",
										"ROUTINE_EVENT_NMO_0_1_description|" + this.name, GeoEventType.Event);
								break;
							case 2: // Tempête solaire
								this.changeDevelopment(-2);
								this.changeInfluence(-1);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_2",
										"ROUTINE_EVENT_NMO_0_2_description|" + this.name, GeoEventType.Event);
								break;
							case 3: // Manifestation d'esprits
								this.changeInfluence(-5);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_3",
										"ROUTINE_EVENT_NMO_0_3_description|" + this.name, GeoEventType.Event);
								break;
							case 4: // Démonstration de force
								this.changeInfluence(3);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_4",
										"ROUTINE_EVENT_NMO_0_4_description|" + this.name, GeoEventType.Event);
								break;
							case 5: // Nouveau chef de la sécurité
								this.changeInfluence(1);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_5",
										"ROUTINE_EVENT_NMO_0_5_description|" + this.name, GeoEventType.Event);
								break;
							case 6: // Héros local
								this.changeInfluence(3);
								this.changeDevelopment(1);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_6",
										"ROUTINE_EVENT_NMO_0_6_description|" + this.name, GeoEventType.Event);
								break;
							case 7: //Manifestations
								this.changeInfluence(-2);
								this.changeDevelopment(-1);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_7",
										"ROUTINE_EVENT_NMO_0_6_description|" + this.name, GeoEventType.Event);
								break;
							case 8: //Vagues de recrutement
								this.units += 5;
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_8",
										"ROUTINE_EVENT_NMO_0_8_description|" + this.name, GeoEventType.Event);
								break;
							case 9: //Rencontre du 3e type
								this.changeInfluence(-10);
								this.changeDevelopment(3);
								Player.addGeoEvent(playerOwner, "ROUTINE_EVENT_NMO_0_9",
										"ROUTINE_EVENT_NMO_0_9_description|" + this.name, GeoEventType.Event);
								break;
						}
						break;
				}
				break;
		}
	}

	public static Finder<Long, Sector> find = new Finder<Long, Sector>(Long.class, Sector.class);
}
