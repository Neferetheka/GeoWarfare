package controllers;

import java.util.List;
import java.util.UUID;

import models.GeoEvent;
import models.Player;
import models.Sector;
import models.GeoEvent.GeoEventType;
import play.cache.Cache;
import play.mvc.*;
import tools.LocationTool;

public class API extends Controller
{

	/* *** Auth *** */

	public static Result auth(String p, String md)
	{
		if (p == null || md == null)
			return ok("SM"); // Sauron and Morgoth

		p = cleanInput(p);
		md = cleanInput(md);

		int count = Player.find.where().eq("login", p).eq("password", md).findRowCount();
		if (count == 1)
		{
			String uid = UUID.randomUUID().toString();
			Cache.set(uid, p, 3000);
			return ok(uid);
		}
		else
			return ok("DP"); // Dokku and Palpatine
	}

	public static Result disconnect(String key)
	{
		if (Cache.get(key) != null)
			Cache.set(key, null);
		return ok("OK");
	}

	/* *** Geolocation *** */
	public static Result checkin(String key, String lon, String lat, String a, String venueName, String venueId,
			String cityName, String venueType)
	{
		if (lon == null || lat == null)
			return ok("SM");

		Double longitude = null;
		Double latitude = null;
		lon = lon.replace(" ", "+");
		lat = lat.replace(" ", "+");
		float golden = 1.61803399f;

		try
		{
			// Golden transform
			longitude = Double.parseDouble(lon) / golden;
			latitude = Double.parseDouble(lat) / golden;
		}
		catch (Exception e)
		{
			return ok("SM");
		}

		venueName = venueName.replace("SPACE", " ");
		cityName = cityName.replace("SPACE", " ");
		venueType = venueType.replace("SPACE", " ");

		Sector sector = LocationTool.getSectorFromVenueId(venueName, venueId, longitude, latitude, cityName, venueType);

		if (key != null)
		{
			// Now it's success !
			/* Success.putSuccess((User) User.find("byPseudoAndMdp", p,
			 * SecurityTool.Sha1_hash(md)).first(), 0); if (a != null &&
			 * Double.parseDouble(a) > 1000) Success.putSuccess((User)
			 * User.find("byPseudoAndMdp", p,
			 * SecurityTool.Sha1_hash(md)).first(), 12); */
		}

		return ok(formatSecteur(sector));
	}

	/* public static Result createCity(String key, String city, double latitude,
	 * double longitude) { if (key == null) return ok("SM"); Player player =
	 * getPlayerByKey(key); if (player == null) return ok("DP");
	 * 
	 * City cityTmp = City.find.where().eq("name",
	 * Strings.ucfirst(city)).findUnique();
	 * 
	 * if (cityTmp != null) { Sector sectorTmp =
	 * LocationTool.getSecteurFromCoords(longitude, latitude);
	 * 
	 * if (sectorTmp != null) { return ok("DP"); } else { // Agrandir la ville
	 * // Faire attention de ne pas faire un agrandissement trop grand if
	 * (Math.floor(longitude - cityTmp.maxLongitude) < 2) { if (longitude >
	 * cityTmp.maxLongitude) { cityTmp.maxLongitude = longitude; } else if
	 * (longitude < cityTmp.minLongitude) { cityTmp.minLongitude = longitude; }
	 * }
	 * 
	 * if (Math.floor(latitude - cityTmp.maxLatitude) < 2) { if (latitude >
	 * cityTmp.maxLatitude) { cityTmp.maxLatitude = latitude; } else if
	 * (latitude < cityTmp.minLatitude) { cityTmp.minLatitude = latitude; } }
	 * 
	 * cityTmp.save();
	 * 
	 * return ok("OK"); } } else { // Ajouter la ville City newCity = new
	 * City(); newCity.name = Strings.ucfirst(city); newCity.minLongitude =
	 * longitude - 0.01; newCity.maxLongitude = longitude + 0.01;
	 * newCity.minLatitude = latitude - 0.01; newCity.maxLatitude = latitude +
	 * 0.01; if (City.find.where().eq("name",
	 * Strings.ucfirst(city)).findRowCount() == 0) newCity.save();
	 * 
	 * return ok("OK"); } } */

	/* *** Game Logic *** */

	public static Result putArmy(String key, String venueId, Long units)
	{
		if (key == null)
			return ok("SM");
		Player player = getPlayerByKey(key);
		if (player == null)
			return ok("DP");

		venueId = cleanInput(venueId);

		Sector sector = Sector.find.where().eq("venueId", venueId).findUnique();

		if (sector == null || !sector.owner.toLowerCase().equals(player.login.toLowerCase()))
			return ok("DP");

		if (player.units < units || units < 1)
			return ok("not enough");

		player.units -= units;
		sector.units += units;

		player.save();
		sector.save();

		return ok("OK");
	}

	public static Result battle(String key, String venueId, Long units)
	{
		if (key == null)
			return ok("SM");
		Player player = getPlayerByKey(key);
		if (player == null)
			return ok("DP");

		venueId = cleanInput(venueId);

		Sector sector = Sector.find.where().eq("venueId", venueId).findUnique();
		if (sector == null || sector.owner.toLowerCase().equals(player.login.toLowerCase()))
			return ok("DP");

		if (units <= 0 || player.units < units
				|| player.login.toLowerCase().trim().equals(sector.owner.toLowerCase().trim()))
			return ok("not good");

		player.units -= units;
		player.save();

		/* Neutral sector */
		if (sector.owner.length() < 2)
		{
			sector.units = units.intValue();
			sector.owner = player.login;
			sector.save();
			Player.addGeoEvent(player, "ROUTINE_battle_title", "ROUTINE_battle_neutral|" + sector.name,
					GeoEventType.Battle);
			return ok("{\"result\":\"wn\", \"difference\":" + units + "}");
		}

		/* Real battle */
		int result = sector.units - units.intValue();

		if (result > 0) // Defaite
		{
			sector.units = result;
			sector.save();
			Player.addGeoEvent(player, "ROUTINE_battle_title", "ROUTINE_battle_defeat|" + sector.name + "|"
					+ sector.name, GeoEventType.Battle);
			return ok("{\"result\":\"d\", \"difference\":" + result + "}");
		}
		else if (result == 0)
		{
			sector.owner = "";
			sector.units = 0;
			sector.save();
			Player.addGeoEvent(player, "ROUTINE_battle_title", "ROUTINE_battle_equality|" + sector.name + "|"
					+ sector.name, GeoEventType.Battle);

			return ok("{\"result\":\"e\", \"difference\":0}");
		}
		else
		{
			sector.units = -result;
			sector.owner = player.login;
			sector.save();
			Player.addGeoEvent(player, "ROUTINE_battle_title", "ROUTINE_battle_win|" + sector.name + "|" + sector.name,
					GeoEventType.Battle);

			return ok("{\"result\":\"w\", \"difference\":" + result + "}");
		}
	}

	/* *** Player information *** */

	public static Result getArmy(String key)
	{
		if (key == null)
			return ok("SM");
		Player player = getPlayerByKey(key);
		if (player == null)
			return ok("DP");

		StringBuilder sb = new StringBuilder("{");
		sb.append("\"units\":" + player.units + ", \"production\":" + player.production + ", \"sectors\":");

		String result = null;
		
		List<Sector> playerSectors = Sector.find.where().eq("owner", player.login).findList();
		if (playerSectors.size() > 0)
		{
			sb.append("[");

			for (Sector sector : playerSectors)
			{
				sb.append("{\"name\":\"" + sector.name + "\", \"venueId\": \"" + sector.venueId + "\", \"owner\":\""
						+ sector.owner + "\"," + "\"units\":\"" + sector.units + "\", \"cityName\": \""
						+ sector.cityName + "\", \"latitude\": \"" + sector.latitude + "\", \"longitude\": \""
						+ sector.longitude + "\"},");
			}
			result = sb.toString();
			result = result.substring(0, result.length() - 1) + "]}";
		}
		else
			result = sb.append("[]}").toString();

		return ok(result);
	}

	public static Result getProfile(String key)
	{
		if (key == null)
			return ok("SM");
		Player player = getPlayerByKey(key);
		if (player == null)
			return ok("DP");

		String allyName = "null";
		if (player.alliance != null)
			allyName = player.alliance.name;

		return ok("{\"login\":\"" + player.login + "\", \"biography\":\"" + player.biography + "\", \"avatar\":\""
				+ player.avatar + "\", \"success\":\"" + player.success + "\", \"ally\":\"" + allyName + "\"}");
	}

	public static Result getGeoEvents(String key)
	{
		if (key == null)
			return ok("SM");
		Player player = getPlayerByKey(key);
		if (player == null)
			return ok("DP");

		StringBuilder sb = new StringBuilder("[");

		for (GeoEvent event : player.listGeoEvents)
		{
			sb.append("{\"title\":\"" + event.title + "\", \"content\":\"" + event.content
					+ "\", \"datePublication\":\"" + event.datePublication + "\", \"type\":\"" + event.geoEventType
					+ "\" },");
		}

		String result = sb.toString();
		result = result.substring(0, result.length() - 1) + "]";

		return ok(result);
	}

	/* *** Utils *** */

	public static Player getPlayerByKey(String key)
	{
		if (Cache.get(key) == null)
			return null;

		String fromCache = Cache.get(key).toString();

		if (fromCache == null)
			return null;

		return Player.find.where().like("Login", fromCache).findUnique();
	}

	private static String cleanInput(String toClean)
	{
		return toClean.trim().replace("SPACE", " ");
	}

	private static String formatSecteur(Sector sector)
	{
		String result = "{" + "\"name\":\"" + sector.name + "\"" + ", \"venueId\": \"" + sector.venueId
				+ "\",  \"owner\":\"" + sector.owner + "\"" + ", \"units\":\"" + sector.units + "\""
				+ ", \"cityName\": \"" + sector.cityName + "\", \"latitude\": \"" + sector.latitude
				+ "\", \"longitude\": \"" + sector.longitude + "\" }";
		return result;
	}
}
