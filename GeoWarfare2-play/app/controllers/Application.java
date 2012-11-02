package controllers;

import java.util.List;

import models.News;
import models.Player;
import models.Success;
import play.mvc.*;

import views.html.*;
import views.html.application.*;

public class Application extends Controller
{

	public static Result index()
	{	
		return ok(index.render("Your new application is ready."));
	}

	public static Result download()
	{
		return ok(download.render(""));
	}
	
	public static Result cgu()
	{
		return ok(cgu.render(""));
	}
	
	public static Result about()
	{
		return ok(about.render(""));
	}
	
	public static Result help()
	{
		return ok(help.render(""));
	}
	
	public static Result gallery()
	{
		return ok(gallery.render(""));
	}
	
	public static Result platform()
	{
		return ok(platform.render(""));
	}
	
	public static Result register()
	{
		return ok(register.render(""));
	}
	
	public static Result news()
	{
		List<News> newsList = News.find.findList();
		return ok(news.render(newsList));
	}
	
	public static Result world()
	{
		return ok(world.render(""));
	}
	
	public static Result profile(String p)
	{
		Player player = Player.find.where().eq("login", p).findUnique();
		if(player == null)
		{
			player = new Player();
			player.login ="Chuck Norris";
		}
		Success success = new Success(0, "Heart", "I'm the best in what I do", "Coder Geowarfare", false);
		
		return ok(profile.render(player, success));
	}
}