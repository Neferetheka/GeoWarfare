var Cache, Message, Player, Success, Unit, actualPosition, actualSecteurOnMap, answerMessage, cache, checkinClick, checkinClickCompleted, conquestValidClick, detailsSecteurClick, detailsSecteurValid, detailsSecteurValidCompleted, displayDetailsSecteur, displayDetailsSuccess, displayResultConquest, errorCallback, getLoginInfos, getSuccessImage, getUnitByIndex, init, isDBSupported, launchAuth, launchAuthCompleted, loadArmyPlayer, loadArmyPlayerCompleted, loadDatasPlayer, loadDatasPlayerCompleted, loadMessages, loadMessagesCompleted, loadSecteurs, loadSecteursCompleted, loadSuccess, loadSuccessCompleted, loading, nativeLocation, player, popup, readMessage, selectedMessage, selectedSecteur, selectedSuccess, sendMessage, sendMessageCompleted, shareSuccess, shareTask, successArray, successCallback, updateUI, writeMessage;
loading = false;
Player = (function() {
  function Player(pseudo, mdp, avatar, biographie, nbSecteurs, successAsString, killedUnits, units, secteurs, listMessages) {
    this.pseudo = pseudo;
    this.mdp = mdp;
    this.avatar = avatar;
    this.biographie = biographie;
    this.nbSecteurs = nbSecteurs;
    this.successAsString = successAsString;
    this.killedUnits = killedUnits;
    this.units = units;
    this.secteurs = secteurs;
    this.listMessages = listMessages;
  }
  return Player;
})();
Success = (function() {
  function Success(id, nom, description, objectif, isHidden, pts) {
    this.id = id;
    this.nom = nom;
    this.description = description;
    this.objectif = objectif;
    this.isHidden = isHidden;
    this.pts = pts;
  }
  return Success;
})();
Unit = (function() {
  function Unit(nombre, production) {
    this.nombre = nombre;
    this.production = production;
  }
  return Unit;
})();
Message = (function() {
  function Message(id, expediteur, sujet, content, date, isRead) {
    this.id = id;
    this.expediteur = expediteur;
    this.sujet = sujet;
    this.content = content;
    this.date = date;
    this.isRead = isRead;
  }
  return Message;
})();
Cache = (function() {
  function Cache() {}
  Cache.prototype.initDB = function(dbName, version, size) {
    if (!this.isDBSupported()) {
      return null;
    }
    try {
      return openDatabase(dbName, version, dbName, size);
    } catch (e) {
      return null;
    }
  };
  Cache.prototype.isDBSupported = function() {
    try {
      return "localStorage" in window && window["localStorage"] !== null;
    } catch (e) {
      return false;
    }
  };
  Cache.prototype.getItem = function(key) {
    var cacheTime;
    if (localStorage.getItem(key + "cache")) {
      cacheTime = parseInt(localStorage.getItem(key + "cache"));
      if (cacheTime < this.getTS()) {
        localStorage.removeItem(key);
        localStorage.removeItem(key + "cache");
        return null;
      }
    }
    if (localStorage.getItem(key)) {
      return localStorage.getItem(key);
    } else {
      return null;
    }
  };
  Cache.prototype.setItem = function(key, value, cacheTime) {
    var TSEndCache;
    TSEndCache = parseInt(cacheTime) + this.getTS();
    localStorage.setItem(key, value);
    return localStorage.setItem(key + "cache", TSEndCache);
  };
  Cache.prototype.getTS = function() {
    return Math.round(new Date().getTime() / 1000);
  };
  return Cache;
})();
player = new Player();
successArray = new Array();
actualPosition = 0;
actualSecteurOnMap = 0;
selectedSecteur = 0;
selectedSuccess = 0;
selectedMessage = 0;
cache = new Cache();
cache.initDB("GeoCache", "0.5", "100000");
init = function() {
  if (cache.getItem('pseudo')) {
    $('#connexionPseudo').val(cache.getItem('pseudo'));
  }
  if (cache.getItem('mdp')) {
    $('#connexionPassword').val(cache.getItem('mdp'));
  }
  if (cache.getItem('pseudo') && cache.getItem('mdp')) {
    return launchAuth();
  }
};
launchAuth = function() {
  var mdp, pseudo;
  if (!loading) {
    pseudo = $('#connexionPseudo').val();
    mdp = $('#connexionPassword').val();
    if (pseudo.length < 3 || mdp.length < 3) {
      return alert('Vous devez rentrer vos identifiants pour vous connecter mon général');
    } else {
      loading = true;
      return $.get('geowarfare/auth?p=' + pseudo + '&md=' + mdp, function(jqXHR, textStatus, errorThrown) {
        return launchAuthCompleted(errorThrown.responseText);
      });
    }
  }
};
launchAuthCompleted = function(datas) {
  loading = false;
  if (datas === "*") {
    player = new Player();
    player.pseudo = $('#connexionPseudo').val();
    player.mdp = $('#connexionPassword').val();
    cache.setItem('pseudo', player.pseudo, 10000);
    cache.setItem('mdp', player.mdp, 10000);
    $.mobile.changePage('#home');
    loadDatasPlayer();
    loadArmyPlayer();
    return loadSecteurs();
    /*player.pseudo(self.pseudoTmp());
    		player.mdp(self.mdpTmp());
    		localStorage.setItem('pseudoTmp', self.pseudoTmp());
    		localStorage.setItem('mdpTmp', self.mdpTmp());
    
    		self.transition("home");
    
    		player.initArmy();
    		self.loadDatasPlayer();
    		self.loadArmyPlayer();
    		self.loadSecteurs();
    		self.loadAlly();
    		self.initMap();*/
  } else {
    return alert("Damn it !", "Vos identifiants sont faux ou la connexion a été perdue. Vous pouvez réessayer mon général ?");
  }
};
self.initDB = function() {};
loadDatasPlayer = function() {
  return $.get('geowarfare/getProfil?p=' + player.pseudo, function(jqXHR, textStatus, result) {
    return loadDatasPlayerCompleted(result.responseText);
  });
};
loadDatasPlayerCompleted = function(datas) {
  var json;
  datas = datas.substring(0, datas.length - 1);
  json = jQuery.parseJSON(datas);
  player.avatar = json.avatar;
  player.biographie = json.biographie;
  player.nbSecteurs = json.nbSecteurs;
  player.successAsString = json.success;
  player.killedUnits = json.KilledUnits;
  player.listMessages = new Array();
  updateUI('home');
  return loadSuccess();
};
loadArmyPlayer = function() {
  return $.get('geowarfare/army?pr=1&' + getLoginInfos(), function(jqXHR, textStatus, result) {
    return loadArmyPlayerCompleted(result.responseText);
  });
};
loadArmyPlayerCompleted = function(response) {
  var armyAsString, i, productionAsString;
  armyAsString = response.split('|')[0].split('-');
  productionAsString = response.split('|')[1].split('-');
  player.units = new Array();
  for (i = 0; i <= 4; i++) {
    player.units[i] = new Unit(armyAsString[i], productionAsString[i]);
  }
  return updateUI('army');
};
loadSecteurs = function() {
  return $.get('geowarfare/army?ls=json&' + getLoginInfos(), function(jqXHR, textStatus, result) {
    return loadSecteursCompleted(result.responseText);
  });
};
loadSecteursCompleted = function(response) {
  var json;
  if (response.length > 16) {
    json = jQuery.parseJSON(response);
    player.secteurs = json.secteur;
    return updateUI('secteurs');
  }
};
/*self.loadSecteurs = function(){
		self.player().clearSecteurs();
		var response = self.requete(HOST+"army?ls=json&"+self.player().loginInfos());
		if(response.length < 16)
			return;
		try{
			var json = jQuery.parseJSON(response);
			for(var i = 0; i < json.secteur.length; i++){
				self.player().pushSecteur(new Secteur(i, json.secteur[i].nom, json.secteur[i].nomVille,
						json.secteur[i].longitude, json.secteur[i].latitude, json.secteur[i].proprietaire, json.secteur[i].unites));
			}
		}
		catch(e){
			console.log(e);
		}
	}*/
loadSuccess = function() {
  return $.get('public/XML/Success.xml', function(jqXHR, textStatus, result) {
    return loadSuccessCompleted(result.responseText);
  });
};
loadSuccessCompleted = function(response) {
  var element, parser, success, successAsXML, xmlDoc, _i, _j, _len, _len2, _ref;
  parser = new DOMParser();
  xmlDoc = parser.parseFromString(response, "text/xml");
  successAsXML = xmlDoc.childNodes[0];
  element;
  _ref = successAsXML.childNodes;
  for (_i = 0, _len = _ref.length; _i < _len; _i++) {
    element = _ref[_i];
    if (element.nodeType !== 3 && element.nodeType !== 4) {
      successArray.push(new Success(element.childNodes[1].childNodes[0].nodeValue, element.childNodes[3].childNodes[0].nodeValue, element.childNodes[5].childNodes[0].nodeValue, element.childNodes[7].childNodes[0].nodeValue, element.childNodes[9].childNodes[0].nodeValue, element.childNodes[13].childNodes[0].nodeValue));
    }
  }
  for (_j = 0, _len2 = successArray.length; _j < _len2; _j++) {
    success = successArray[_j];
    if (player.successAsString.indexOf('-' + success.id + '-') !== -1) {
      success.isDone = true;
    }
  }
  return updateUI('success');
};
getLoginInfos = function() {
  return "p=" + player.pseudo + "&md=" + player.mdp;
};
displayDetailsSecteur = function(secteurName) {
  var content, i, secteur, unites, _i, _len, _ref;
  selectedSecteur = 0;
  _ref = player.secteurs;
  for (_i = 0, _len = _ref.length; _i < _len; _i++) {
    secteur = _ref[_i];
    if (secteur.nom === secteurName) {
      selectedSecteur = secteur;
    }
  }
  if (selectedSecteur !== 0) {
    content = "";
    unites = selectedSecteur.unites.split('-');
    for (i = 0; i <= 4; i++) {
      content += '<li data-role="fieldcontain"><img src="public/gfx/Units/' + getUnitByIndex(i) + '.png" /><label style="width:90px;">' + getUnitByIndex(i) + '</label>\
			<input type="number" id="detailsSecteur' + getUnitByIndex(i) + '" value="' + unites[i] + '" placeholder="0"/></li>';
    }
    $('#detailsSecteurListview').html(content);
    return $.mobile.changePage('#detailsSecteur');
  }
};
detailsSecteurValid = function() {
  var error, i, nombreArtilleur, nombreAvion, nombreHelicoptere, nombreInfanterie, nombreTank, secteur, toCompare, total, unites, url;
  nombreInfanterie = -1;
  nombreArtilleur = -1;
  nombreHelicoptere = -1;
  nombreTank = -1;
  nombreAvion = -1;
  try {
    nombreInfanterie = parseInt($("#detailsSecteurInfanterie").val());
    nombreArtilleur = parseInt($("#detailsSecteurArtilleur").val());
    nombreHelicoptere = parseInt($("#detailsSecteurHelicoptere").val());
    nombreTank = parseInt($("#detailsSecteurChar").val());
    nombreAvion = parseInt($("#detailsSecteurAvion").val());
  } catch (e) {
    alert("Le format de nombre n'est pas correct mon général !");
  }
  if (nombreInfanterie < 0 || nombreArtilleur < 0 || nombreHelicoptere < 0 || nombreTank < 0 || nombreAvion < 0) {
    return alert("Le format de nombre n'est pas correct mon général !");
  } else {
    error = false;
    i = 0;
    while (i < player.units.length) {
      toCompare = 0;
      switch (i) {
        case 0:
          toCompare = nombreInfanterie;
          break;
        case 1:
          toCompare = nombreArtilleur;
          break;
        case 2:
          toCompare = nombreHelicoptere;
          break;
        case 3:
          toCompare = nombreTank;
          break;
        case 4:
          toCompare = nombreAvion;
      }
      if (player.units[i].nombre + selectedSecteur.unites[i] < toCompare) {
        total = player.units[i].nombre + selectedSecteur.unites[i];
        alert("Vous n'avez pas assez d'unités pour en déployer autant mon général !");
        error = true;
        return;
      }
      i++;
    }
    unites = nombreInfanterie + "-" + nombreArtilleur + "-" + nombreHelicoptere + "-" + nombreTank + "-" + nombreAvion;
    secteur = selectedSecteur.nom;
    url = "geowarfare/putarmy?" + "s=" + secteur + "&u=" + unites + "&" + getLoginInfos();
    return $.get(url, function(jqXHR, textStatus, result) {
      return detailsSecteurValidCompleted(result.responseText, unites);
    });
  }
};
detailsSecteurValidCompleted = function(response, unites) {
  if (response.length > 0) {
    self.loadArmyPlayer();
    selectedSecteur.unites = unites;
    alert("Déploiement terminé mon général !");
  } else {
    alert("Une erreur est survenue lors du déploiement mon général. Vous pouvez réessayer ?");
  }
  return $.mobile.changePage('#secteurs', {
    reverse: true
  });
};
updateUI = function(page) {
  var content, i, nbSuccess, secteur, success, _i, _j, _len, _len2, _ref;
  switch (page) {
    case "home":
      $('#homePseudo').html(player.pseudo);
      $('#homeBiographie').html(player.biographie);
      nbSuccess = player.successAsString.split('-').length - 2;
      if (nbSuccess < 0) {
        nbSuccess = 0;
      }
      $('#homeNbSuccess').html(nbSuccess);
      $('#homeKilledUnits').html(player.killedUnits);
      $('#homeNbSecteurs').html(player.nbSecteurs);
      return $('#homeAvatar').attr('src', player.avatar);
    case "army":
      content = "";
      for (i = 0; i <= 4; i++) {
        content += '<li><a href="#"><img src="public/gfx/Units/' + getUnitByIndex(i) + '.png" /><h3>' + getUnitByIndex(i) + '</h3><p>Nombre : ' + player.units[i].nombre + ' - Production : ' + player.units[i].production + '</p></a></li>';
      }
      return $('#armyListview').html(content);
    case "secteurs":
      content = "";
      _ref = player.secteurs;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        secteur = _ref[_i];
        content += '<li onclick="displayDetailsSecteur(\'' + secteur.nom + '\');"><a href="#" >' + secteur.nom + '</a></li>';
      }
      return $('#secteursListview').html(content);
    case "success":
      content = "";
      for (_j = 0, _len2 = successArray.length; _j < _len2; _j++) {
        success = successArray[_j];
        if (success.isHidden === "false" || (success.isHidden === "true" && success.isDone === true)) {
          content += '<li onclick="displayDetailsSuccess(' + success.id + ');"><a href="#"><img src="public/gfx/Success/' + success.nom.replace(' ', '_').replace('\'', '').replace('î', 'i') + '.png" alt="' + success.nom + '"/>' + success.nom + (success.isDone === true ? "<br/>D&eacute;bloqu&eacute; !" : "") + '</a></li>';
        }
      }
      return $('#successListview').html(content);
  }
};
nativeLocation = function() {
  if (navigator.geolocation) {
    return navigator.geolocation.getCurrentPosition(successCallback, errorCallback);
  } else {
    return alert("Votre navigateur ne supporte pas la géolocalisation. Désolé mon général. Du café pour compenser ?");
  }
};
successCallback = function(position) {
  var height, latlng, scale, width;
  actualPosition = position;
  latlng = position.coords.latitude + "," + position.coords.longitude;
  width = document.documentElement.clientWidth - 20;
  height = document.documentElement.clientHeight - 60;
  scale = 1;
  if (width > 800) {
    scale = 2;
  }
  $('#googleMap').attr('src', 'https://maps.googleapis.com/maps/api/staticmap?center=' + latlng + '&zoom=14&size=' + width + 'x' + height + '&markers=' + latlng + '&sensor=false&maptype=hybrid&scale=' + scale);
  $('#googleMap').attr('width', width + 'px');
  return $('#googleMap').attr('height', height + 'px');
};
errorCallback = function(error) {
  switch (error.code) {
    case error.PERMISSION_DENIED:
      return alert("Vous n'avez pas accepté la localisation mon général, nous ne pouvons donc pas localiser le QG.");
    case error.POSITION_UNAVAILABLE:
      return alert("Il semblerait que nous soyons hors de la zone de couverture GPS mon général.");
    case error.TIMEOUT:
      return alert("Excusez-moi mon général, mais le service de localisation est trop long à répondre. Oui mon général, je vais de suite convoquer le service des trans' en cour martiale !");
  }
};
checkinClick = function() {
  var url;
  if (actualPosition) {
    url = "geowarfare/checkin?f=json&lon=" + actualPosition.coords.longitude + "&lat=" + actualPosition.coords.latitude;
    return $.get(url, function(jqXHR, textStatus, result) {
      return checkinClickCompleted(result.responseText);
    });
  }
};
checkinClickCompleted = function(response) {
  var secteurAsJson;
  if (response === "$") {
    return alert('Votre secteur actuel n\'a pas de valeur stratégique mon général !');
  } else {
    secteurAsJson = jQuery.parseJSON(response);
    $('#dialogSecteurTitle').html(secteurAsJson.secteur.nom);
    if (secteurAsJson.secteur.proprietaire.length < 3) {
      secteurAsJson.secteur.proprietaire = "personne";
    }
    actualSecteurOnMap = secteurAsJson.secteur;
    $('#dialogSecteurContent').html("D&eacute;tenu par " + secteurAsJson.secteur.proprietaire);
    if (secteurAsJson.secteur.proprietaire === player.pseudo) {
      $('#dialogSecteurDetailsButton').addClass("show");
      $('#dialogSecteurConquestButton').addClass("hidden");
    } else {
      $('#dialogSecteurDetailsButton').addClass("hidden");
      $('#dialogSecteurConquestButton').addClass("show");
    }
    return $.mobile.changePage('#dialogSecteur', {
      transition: "slidedown"
    });
  }
};
detailsSecteurClick = function() {
  var content, i, units;
  if (actualSecteurOnMap.proprietaire === player.pseudo) {
    return displayDetailsSecteur(actualSecteurOnMap.nom);
  } else {
    if (actualSecteurOnMap) {
      content = "";
      units = player.units;
      for (i = 0; i <= 4; i++) {
        content += '<li data-role="fieldcontain"><img src="public/gfx/Units/' + getUnitByIndex(i) + '.png" /><label style="width:90px;">' + getUnitByIndex(i) + '<br/>Nombre : ' + units[i].nombre + '</label>\
				<input type="number" id="conquete' + getUnitByIndex(i) + '" value="0" placeholder="0"/></li>';
      }
      $('#conqueteListview').html(content);
      $.mobile.changePage('#conquete');
      $('#conqueteListview').listview('refresh');
    }
  }
};
conquestValidClick = function() {
  var intensite, nombreArtilleur, nombreAvion, nombreHelicoptere, nombreInfanterie, nombreTank, unites, url;
  nombreInfanterie = parseInt($("#conqueteInfanterie").val());
  nombreArtilleur = parseInt($("#conqueteArtilleur").val());
  nombreHelicoptere = parseInt($("#conqueteHelicoptere").val());
  nombreTank = parseInt($("#conqueteChar").val());
  nombreAvion = parseInt($("#conqueteAvion").val());
  if (nombreInfanterie < 0 || nombreArtilleur < 0 || nombreHelicoptere < 0 || nombreTank < 0 || nombreAvion < 0) {
    alert("Le format de nombre n'est pas correct mon général !");
    return;
  }
  if (nombreInfanterie > player.units[0].nombre || nombreArtilleur > player.units[1].nombre || nombreHelicoptere > player.units[2].nombre || nombreTank > player.units[3].nombre || nombreAvion > player.units[4].nombre) {
    alert("Vous n'avez pas assez d'unités à envoyer au combat mon général.");
    return;
  }
  unites = nombreInfanterie + "-" + nombreArtilleur + "-" + nombreHelicoptere + "-" + nombreTank + "-" + nombreAvion;
  intensite = $("#conqueteIntensite").val();
  if (unites === "0-0-0-0-0") {
    return;
  }
  url = "geowarfare/combat?lon=" + actualPosition.coords.longitude + "&lat=" + actualPosition.coords.latitude + "&u=" + unites + "&i=" + intensite + "&" + getLoginInfos();
  url = url.replace(",", ";");
  return $.get(url, function(jqXHR, textStatus, result) {
    return displayResultConquest(result.responseText);
  });
};
displayResultConquest = function(result) {
  var combatNode, contentAttacker, contentOpponent, i, labelSecteur, parser, pertesAttaquantTotal, pertesDefenseurTotal, proprietaire, resultToShare, title, victoire, xmlDoc;
  $.mobile.changePage('#resultConquest');
  resultToShare = "";
  title = "Victoire !";
  proprietaire = actualSecteurOnMap.proprietaire;
  labelSecteur = actualSecteurOnMap.nom;
  if (result === "EasyWin !") {
    alert("Vous venez de remporter une victoire facile sur le secteur neutre " + labelSecteur);
    loadArmyPlayer();
    loadSecteurs();
    $.mobile.changePage('#home');
    return;
  }
  console.log(result);
  result = result.replace(/&/g, "-");
  parser = new DOMParser();
  xmlDoc = parser.parseFromString(result, "text/xml");
  combatNode = xmlDoc.childNodes[0];
  victoire = combatNode.childNodes[3].childNodes[0].nodeValue;
  if (victoire.toLowerCase() === "defenseur") {
    title = "Défaite !";
    resultToShare = "Le général " + player.pseudo + " vient de perdre misérablement contre le général " + proprietaire + " sur le secteur " + labelSecteur + ".";
  } else {
    resultToShare = "Le général " + player.pseudo + " vient de remporter le secteur " + labelSecteur + " aux dépends du général " + proprietaire + ".";
  }
  $("#resultConquestTitle").html(title);
  $("#resultConquestLabel").html(labelSecteur);
  pertesAttaquantTotal = combatNode.childNodes[4].childNodes[0].nodeValue.split("-");
  pertesDefenseurTotal = combatNode.childNodes[5].childNodes[0].nodeValue.split("-");
  i = 0;
  contentAttacker = "";
  contentOpponent = "";
  while (i < 5) {
    contentAttacker += '<li><a href="#"><img src="public/gfx/Units/' + getUnitByIndex(i) + '.png" alt="' + getUnitByIndex(i) + '"/><h3>' + getUnitByIndex(i) + '</h3><p>Pertes : ' + pertesAttaquantTotal[i] + '</p></a></li>';
    contentOpponent += '<li><a href="#"><img src="public/gfx/Units/' + getUnitByIndex(i) + '.png" alt="' + getUnitByIndex(i) + '"/><h3>' + getUnitByIndex(i) + '</h3><p>Pertes : ' + pertesDefenseurTotal[i] + '</p></a></li>';
    i++;
  }
  $('#attackerListview').append(contentAttacker);
  $('#opponentListview').append(contentOpponent);
  $("#attackerListview").listview('refresh');
  $("#opponentListview").listview('refresh');
  loadArmyPlayer();
  return loadSecteurs();
};
displayDetailsSuccess = function(successId) {
  selectedSuccess = successArray[successId];
  $('#detailsSuccessTitle').html(selectedSuccess.nom);
  $('#detailsSuccessDescription').html(selectedSuccess.description);
  $('#detailsSuccessObjectif').html("<strong>Objectif : </strong>" + selectedSuccess.objectif);
  $('#detailsSuccessImage').attr('src', getSuccessImage(selectedSuccess.nom));
  return $.mobile.changePage('#detailSuccess');
};
shareSuccess = function() {
  var toShare;
  toShare = "Le général " + player.pseudo + " a débloqué le succès " + selectedSuccess.nom;
  return shareTask(toShare);
};
loadMessages = function() {
  var url;
  if (player.listMessages.length > 0) {} else {
    url = "geowarfare/GetMessages?" + getLoginInfos();
    return $.get(url, function(jqXHR, textStatus, result) {
      return loadMessagesCompleted(result.responseText);
    });
  }
};
loadMessagesCompleted = function(response) {
  var content, i, json, message, _i, _len, _ref;
  json = jQuery.parseJSON(response.replace(/models.GeoMessages/g, "geomessages"));
  console.log(response);
  i = 0;
  while (i < json.geomessages.length) {
    player.listMessages.push(new Message(i, json.geomessages[i].expediteur, json.geomessages[i].sujet, json.geomessages[i].content, json.geomessages[i].date, json.geomessages[i].isRead));
    i++;
  }
  player.listMessages.reverse();
  content = "";
  _ref = player.listMessages;
  for (_i = 0, _len = _ref.length; _i < _len; _i++) {
    message = _ref[_i];
    content += '<li><a href="#" onclick="readMessage(' + message.id + ');"><img src="public/gfx/Noun/' + (message.isRead ? "mailopened" : "noun_mail") + '.png" alt="Message"/><h3>' + message.sujet + '</h3><p>Envoy&eacute; par ' + message.expediteur + '</p></a></li>';
  }
  $("#messagerieListview").html(content);
  return $("#messagerieListview").listview('refresh');
};
readMessage = function(messageId) {
  var message, _i, _len, _ref;
  selectedMessage = 0;
  _ref = player.listMessages;
  for (_i = 0, _len = _ref.length; _i < _len; _i++) {
    message = _ref[_i];
    if (message.id === messageId) {
      selectedMessage = message;
    }
  }
  if (selectedMessage !== 0) {
    $('#detailsMessagerieTitle').html(selectedMessage.sujet);
    $('#detailsMessagerieContent').html(selectedMessage.content);
    $('#detailsMessagerieDate').html('Envoy&eacute; par ' + selectedMessage.expediteur + ' le ' + selectedMessage.date);
    return $.mobile.changePage('#detailsMessagerie');
  }
};
writeMessage = function() {
  $('#messagerieWriterDestinataire').val('');
  $('#messagerieWriterSubject').val('');
  $('#messagerieWriterContent').val('');
  $.mobile.changePage('#messagerieWriter');
  return selectedMessage = 0;
};
sendMessage = function() {
  var content, destinataire, sujet, url;
  destinataire = $("#messagerieWriterDestinataire").val();
  destinataire = destinataire.replace(" ", "").replace("\n", "");
  sujet = $("#messagerieWriterSubject").val();
  content = $("#messagerieWriterContent").val();
  url = "geowarfare/sendmessage?" + getLoginInfos() + "&d=" + destinataire + "&s=" + sujet + "&c=" + content;
  if (destinataire.length <= 2) {
    alert("Nous n'envoyons pas de bouteille à la mer mon général ! Encore moins au QG.");
  } else if (sujet.length < 2) {
    alert("Le sujet de la transmission doit faire au moins trois caractères.");
  } else if (content.length < 2) {
    alert("La transmission doit faire au moins trois caractères.");
  } else if (destinataire.trim().toLowerCase() === player.pseudo.trim().toLowerCase()) {
    alert("Ce n'est pas très utile de s'envoyer des messages à soi-même vous ne croyez pas ?");
  } else {
    return $.get(url, function(jqXHR, textStatus, result) {
      return sendMessageCompleted(result.responseText);
    });
  }
};
sendMessageCompleted = function(response) {
  if (response === "!!") {
    return alert("Désolé mon général. Le destinataire spécifié n'existe pas !");
  } else if (response === "!!!") {
    return alert("La boite de réception du destinataire est pleine mon général ! Vous pouvez réessayer plus tard ?");
  } else {
    alert("Message envoyé !");
    return $.mobile.changePage('#messagerie');
  }
};
answerMessage = function() {
  $('#messagerieWriterDestinataire').val(selectedMessage.expediteur);
  $('#messagerieWriterSubject').val("Re : " + $('#detailsMessagerieTitle').html());
  $('#messagerieWriterContent').val('');
  $.mobile.changePage('#messagerieWriter');
  return selectedMessage = 0;
};
shareTask = function(message) {
  var url;
  message += " - via GeoWarfare mobile";
  url = "https://twitter.com/intent/tweet?text=" + message;
  return popup(url);
};
popup = function(url) {
  return window.open(url, null, 'height = 420, width = 550, status = yes, toolbar = no, scrollbars=yes, menubar = no, location = no');
};
getSuccessImage = function(src) {
  return src = "public/gfx/Success/" + src.replace(' ', '_').replace('\'', '').replace('î', 'i') + ".png";
};
getUnitByIndex = function(index) {
  switch (index) {
    case 0:
      return "Infanterie";
    case 1:
      return "Artilleur";
    case 2:
      return "Helicoptere";
    case 3:
      return "Char";
    case 4:
      return "Avion";
  }
};
isDBSupported = function() {
  try {
    return "localStorage" in window && window["localStorage"] !== null;
  } catch (e) {
    return false;
  }
};
$(document).ready(function() {
  return init();
});