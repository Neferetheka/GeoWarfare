package com.aerilys.geowarfare.android.tools;

import java.util.ArrayList;
import java.util.List;

import com.aerilys.geowarfare.android.models.Player;
import com.aerilys.geowarfare.android.models.Sector;
import com.aerilys.geowarfare.android.models.Success;


public class DataContainer
{
	private static DataContainer instance;

	public static DataContainer getInstance()
	{
		if (instance == null)
			instance = new DataContainer();
		return instance;
	}
	
	public final static String HOST = "http://www.geowarfare.net/API/";
	public final static String HOSTPUBLIC = "http://www.geowarfare.net/assets/";
	
	
	public Player player;
	public Sector currentSector;

	public static Player getPlayerI()
	{
		return DataContainer.getInstance().player;
	}
	
	private TempCache tempCache = new TempCache(10);
	
	public TempCache getTempCache()
	{
		return this.tempCache;
	}
	
	private BitmapCache bitmapCache = new BitmapCache(25);

	public BitmapCache getBitmapCache()
	{
		return bitmapCache;
	} 
	
	public List<Success> listSuccess = new ArrayList<Success>();
	

/*	Player player;
	public List<String> listVisitedSecteurs = new ArrayList<String>();
	public boolean hasNewMessages = false;

	public DataContainer()
	{
		;
	}

	public static String getLoginInfos()
	{
		if (getInstance().getPlayer() == null)
			return null;
		else
			return "p=" + getInstance().getPlayer().getPseudo() + "&md=" + getInstance().getPlayer().getMdp();
	}

	public static Player getPlayerI()
	{
		return DataContainer.getInstance().getPlayer();
	}

	public static Alliance getAlliance()
	{
		if (DataContainer.getInstance().getPlayer() == null)
			return null;
		return DataContainer.getInstance().getPlayer().getAlliance();
	}

	public void loadSuccess(Activity activity, String reponse)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream in = new ByteArrayInputStream(reponse.getBytes());
			Document dom = builder.parse(in);
			Element root = dom.getDocumentElement();
			NodeList successSet = root.getChildNodes();
			Node item;
			Node successNode;
			ListeSuccess.clear();

			for (int i = 0; i < successSet.getLength(); i++)
			{
				item = successSet.item(i);
				if (item.getNodeName().toLowerCase().equals("success"))
				{
					Success success = new Success();
					for (int j = 0; j < item.getChildNodes().getLength(); j++)
					{
						successNode = item.getChildNodes().item(j);
						if (successNode.getNodeName().toLowerCase().equals("id"))
							success.setId(Integer.parseInt(successNode.getTextContent()));
						else if (successNode.getNodeName().toLowerCase().equals("nom"))
							success.setNom(successNode.getTextContent());
						else if (successNode.getNodeName().toLowerCase().equals("description"))
							success.setDescription(successNode.getTextContent());
						else if (successNode.getNodeName().toLowerCase().equals("objectif"))
							success.setObjectif(successNode.getTextContent());
						else if (successNode.getNodeName().toLowerCase().equals("ishidden"))
							success.setHidden(Converter.convertStringToBool(successNode.getTextContent()));
						else if (successNode.getNodeName().toLowerCase().equals("iselite"))
							success.setElite(Converter.convertStringToBool(successNode.getTextContent()));
						else if (successNode.getNodeName().toLowerCase().equals("ptsrecompense"))
							success.setPtsRecompense(Integer.parseInt(successNode.getTextContent()));
					}
					try
					{
						int id = activity.getResources().getIdentifier(
								success.getNom().toLowerCase().replace(" ", "_").replace("é", "e").replace("'", "")
										.replace("è", "e").replace("ô", "o").replace("î", "i"), "drawable",
								activity.getPackageName());
						if (i > 0)
							success.Image = id;
					}
					catch (Exception e)
					{
						success.Image = R.drawable.alpiniste;
					}
					ListeSuccess.add(success);
				}
			}
		}
		catch (Exception e)
		{
			Log.d("GeoError", "Erreur de chargement des fichiers ! " + e.getMessage());
			Main.alertConnexion(activity);
		}
	}

	 Traite le chargement des succÃ¨s du player 
	public void loadSuccessPlayer(Activity activity, String raiponce)
	{
		if (raiponce.contains("$"))
			return;
		String[] indexs = raiponce.split("-");
		Log.d("GeoDebug", raiponce);
		for (String index : indexs)
		{
			if (index.length() > 0)
			{
				try
				{
					int nb = Integer.parseInt(index);
					ListeSuccess.get(nb).setDone(true);
					Log.d("GeoDebug", "j'indexe Ã  true !");
				}
				catch (Exception e)
				{
					Log.d("GeoDebug", "Erreur lors du parsing des succes du joueur ");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void loadVisitedSecteurs(Context context)
	{
		byte[] visitedSecteursAsBytesArray = StorageHelper.ReadFileByte(context, StorageHelper.TARDISPATH
				+ DataContainer.getPlayerI().getPseudo());

		if (visitedSecteursAsBytesArray != null)
		{
			try
			{
				DataContainer.getInstance().listVisitedSecteurs = (List<String>) Serializer
						.deserializeObject(visitedSecteursAsBytesArray);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void addVisitedSecteur(Context context, Secteur secteur)
	{
		if (!listVisitedSecteurs.contains(secteur.getNom()))
		{
			try
			{
				listVisitedSecteurs.add(secteur.getNom());
				byte[] serializedPlayer = Serializer.serializeObject(listVisitedSecteurs);
				StorageHelper.WriteFile(context, StorageHelper.TARDISPATH + DataContainer.getPlayerI().getPseudo(),
						serializedPlayer);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}*/

}
