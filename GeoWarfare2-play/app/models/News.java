package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class News extends Model
{
	@Id
	public Long id;
	
	public String title;
	@Lob
	public String content;
	public Date datePublication;
	public String author;
	public String pathImage;
	
	public static Finder<Long,News> find = new Finder<Long,News>(
			Long.class, News.class
			);
}
