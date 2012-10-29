package tools;

import models.Sector;

public class LocationTool
{

	public static Sector getSectorFromVenueId(String name, String venueId, double longitude, double latitude, String cityName, String category)
	{
		Sector sector = Sector.find.where().eq("venueId", venueId).findUnique();
		if (sector == null)
		{
			sector = new Sector(name, venueId, longitude, latitude, "", cityName, category, 0);
			sector.save();
		}
		
		return sector;
	}

/*	public static Sector getSecteurFromCoords(Double longitude, Double latitude)
	{
		boolean isSector = false;
		String sectorName = "";
		double X = 1;
		double Y = 1;
		double minLongitude = 0;
		double minLatitude = 0;

		City city = getCityByCoords(latitude, longitude);
		if (city != null)
		{
			isSector = true;
			sectorName = city.name;
			minLongitude = city.minLongitude;
			minLatitude = city.minLatitude;
		}

		if (isSector)
		{
			X = Math.floor((longitude - minLongitude) * 100) + 1;
			Y = Math.floor((latitude - minLatitude) * 100) + 1;

			X = Math.abs(X);
			Y = Math.abs(Y);

			Sector sector = Sector.find.where().eq("X", X).eq("Y", Y).eq("cityName", sectorName).findUnique();

			if (sector == null)
			{

				sector = new Sector(sectorName + " " + X + "-" + Y, minLongitude + (0.01 * (X - 1)), minLatitude
						+ (0.01 * (Y - 1)), sectorName, "", 0);
				sector.save();

			}

			return sector;
		}
		return null;
	}

	public static City getCityByCoords(double latitude, double longitude)
	{
		return City.find.where(
				"minLatitude <= " + latitude + " AND maxLatitude >= " + latitude + " AND minLongitude <= " + longitude
						+ " AND maxLongitude >= " + longitude).findUnique();
		 .find(
		 * "byMinLatitudeLessThanEqualsAndMaxLatitudeGreaterThanEqualsAndMinLongitudeLessThanEqualsAndMaxLongitudeGreaterThanEquals"
		 * , latitude, latitude, longitude, longitude).first(); 
	}*/

}
