import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import akka.util.Duration;

import models.Player;
import models.GeoEvent.GeoEventType;
import models.Sector;
import play.*;
import play.libs.Akka;

public class Global extends GlobalSettings
{
	@Override
	public void onStart(Application app)
	{
		int count = Player.find.findRowCount();

		if (count == 0)
		{
			Player player = new Player();
			player.login = "Galaad";
			player.password = "test";
			player.units = 10;
			player.production = 10;
			player.dateInscription = new Date();
			player.save();

			Player.addGeoEvent(player, "ROUTINE_battle_title", "J'apprécie les fruits au sirop !", GeoEventType.Battle);
		}

		Akka.system().scheduler()
				.schedule(Duration.create(10, TimeUnit.SECONDS), Duration.create(1, TimeUnit.HOURS), new Runnable()
				{
					public void run()
					{
						GregorianCalendar calendar = new GregorianCalendar();
						if (calendar.get(Calendar.HOUR) == 0)
						{
							// Production
							for (Player player : Player.find.all())
							{
								player.units = player.production;
								Player.addGeoEvent(player, "Production", "ROUTINE_PRODUCTION|"+player.production, GeoEventType.Production);
								player.save();
							}

							// Culture/development
							for (Sector sector : Sector.find.all())
							{
								sector.changeDevelopment(2);
								sector.changeInfluence(2);
								sector.save();
							}
						}

						// Events
						Random rand = new Random();
						for (Sector sector : Sector.find.all())
						{
							int proba = rand.nextInt(101);
							if (proba < 5 && sector.owner.length() > 1)
							{
								// Event
								sector.generateEvent(rand);
								sector.save();
							}
						}
					}
				});
	}
}