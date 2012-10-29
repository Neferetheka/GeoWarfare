package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity	
public class Ally extends Model
{
	@Id
    public Long id;
	
	@OneToMany(mappedBy = "alliance")
	public List<Player> membersList;

	public String name;
	
	public static Finder<Long,Ally> find = new Finder<Long,Ally>(
			Long.class, Ally.class
			);
}
