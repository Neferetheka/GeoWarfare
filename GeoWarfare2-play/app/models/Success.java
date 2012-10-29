package models;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.libs.XML;

import tools.PushNotificationWP;

public class Success
{
	public int id;
	public String nom;
	public String description;
	public String objectif;
	public boolean isHidden = false;
	public boolean isElite = false;
	public int ptsRecompense;
	public static List<Success> listeSucces;

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
		;
	}

	public static void loadAllSuccess()
	{
		listeSucces = new ArrayList<Success>();

		System.out.println("File : " + System.getProperty("user.dir"));
		//File file = new File("GeoWarfare-play/public/XML/Success.xml");
		//InputStream is = new FileInputStream(file);
		
		//TODO : load Success
		
		Document doc = XML.fromString("");

		if (doc != null)
		{
			Element root = doc.getDocumentElement();
			NodeList successSet = root.getChildNodes();
			Node item;
			Node successNode;

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
							success.id = Integer.parseInt(successNode.getTextContent());
						else if (successNode.getNodeName().toLowerCase().equals("nom"))
							success.nom = successNode.getTextContent();
						else if (successNode.getNodeName().toLowerCase().equals("description"))
							success.description = successNode.getTextContent();
						else if (successNode.getNodeName().toLowerCase().equals("objectif"))
							success.objectif = successNode.getTextContent();
					}

					listeSucces.add(success);
				}
			}
		}
	}

	public static void putSuccess(Player player, int index)
	{
		boolean contains = false;
		// On verifie si le player a le succès ou non
		String[] successAsString = player.success.split("-");
		int[] success = new int[successAsString.length];

		for (int i = 0; i < successAsString.length; i++)
		{
			if (successAsString[i].length() > 0)
			{
				try
				{
					success[i] = Integer.parseInt(successAsString[i]);
					if (success[i] == index)
					{
						contains = true;
						return;
					}
				}
				catch (Exception e)
				{
					;
				}
			}
		}

		if (!contains)
		{
			player.success += index + "-";

			if (success.length + 1 >= 10 && !player.success.contains("-21-"))
				player.success += "21-";
			if (success.length + 1 >= 25 && !player.success.contains("-22-"))
				player.success += "22-";

			if (player.success.contains("17") && player.success.contains("18") && player.success.contains("19")
					&& !player.success.contains("-20-"))
				player.success += "20-";

			if (Success.listeSucces.size() >= index)
			{
				PushNotificationWP.PushNotifAll(player, Success.listeSucces.get(index).nom, "Nouveau succès !");

				//TODO: activer les messages de succès
			/*	GeoMessage geoMessage = new GeoMessage("QG", "Nouveau succès",
						"Vous venez de débloquer un nouveau succès : " + Success.listeSucces.get(index).nom);
				geoMessage.destinataire = player;
				player.listeMessages.add(geoMessage);
				player.save();*/
			}
		}

	}
}
