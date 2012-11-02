package com.aerilys.geowarfare.android.models;

import java.util.List;


import com.aerilys.geowarfare.android.R;

public class Success
{
	int id;
	String nom;
	String description;
	String objectif;
	boolean isHidden = false;
	boolean isElite = false;
	int ptsRecompense;
	boolean isDone = false;
	public int Image;

	public Success(int id, String nom, String description, String objectif, boolean isHidden)
	{
		super();
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.objectif = objectif;
		this.isHidden = isHidden;
	}

	public Success()
	{
		Image = R.drawable.question;
	}

	public static int getSuccessPts(String successAsString)
	{
		//Log.d("GeoDebug", "SuccessAsString : " + successAsString+"/"+DataContainer.getInstance().ListeSuccess.size());
		
		int pts = 0;
		//int index = -1;
/*		for (String success : successAsString.split("-"))
		{
			if (success.length() > 0)
			{
				index = Converter.ctI(success);
				if (DataContainer.getInstance().ListeSuccess.size() > index)
					pts += DataContainer.getInstance().ListeSuccess.get(index).ptsRecompense;
			}
		}*/
		return pts;
	}

	public static int getSuccessPts(List<Success> listeSuccess)
	{
		int pts = 0;
		for (Success success : listeSuccess)
		{
			pts += success.ptsRecompense;
		}
		return pts;
	}

	public static int getSuccessPts(List<Success> listeSuccess, boolean mustChecked)
	{
		int pts = 0;
		for (Success success : listeSuccess)
		{
			if (!mustChecked || success.isDone)
				pts += success.ptsRecompense;
		}
		return pts;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return the nom
	 */
	public String getNom()
	{
		return nom;
	}

	/**
	 * @param nom
	 *            the nom to set
	 */
	public void setNom(String nom)
	{
		this.nom = nom;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the objectif
	 */
	public String getObjectif()
	{
		return objectif;
	}

	/**
	 * @param objectif
	 *            the objectif to set
	 */
	public void setObjectif(String objectif)
	{
		this.objectif = objectif;
	}

	/**
	 * @return the isHidden
	 */
	public boolean isHidden()
	{
		return isHidden;
	}

	/**
	 * @param isHidden
	 *            the isHidden to set
	 */
	public void setHidden(boolean isHidden)
	{
		this.isHidden = isHidden;
	}

	/**
	 * @return the isElite
	 */
	public boolean isElite()
	{
		return isElite;
	}

	/**
	 * @param isElite
	 *            the isElite to set
	 */
	public void setElite(boolean isElite)
	{
		this.isElite = isElite;
	}

	/**
	 * @return the ptsRecompense
	 */
	public int getPtsRecompense()
	{
		return ptsRecompense;
	}

	/**
	 * @param ptsRecompense
	 *            the ptsRecompense to set
	 */
	public void setPtsRecompense(int ptsRecompense)
	{
		this.ptsRecompense = ptsRecompense;
	}

	/**
	 * @return the isDone
	 */
	public boolean isDone()
	{
		return isDone;
	}

	/**
	 * @param isDone
	 *            the isDone to set
	 */
	public void setDone(boolean isDone)
	{
		this.isDone = isDone;
	}
}
