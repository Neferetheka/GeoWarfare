loading =false

class Player 
	constructor: (@pseudo, @mdp, @avatar, @biographie, @nbSecteurs, @successAsString, @killedUnits, @units, @secteurs, @listMessages) ->
	
class Success
	constructor:(@id, @nom, @description, @objectif, @isHidden, @pts)->
	
class Unit
	constructor:(@nombre, @production) ->
	
class Message
	constructor:(@id, @expediteur, @sujet, @content, @date, @isRead)->
	
class Cache
	constructor:()->
	initDB :(dbName, version, size) ->
		return null  unless this.isDBSupported()
		try
			openDatabase(dbName, version, dbName, size)
		catch e
			return null
	
	isDBSupported:() ->
		try
			return "localStorage" of window and window["localStorage"] isnt null
		catch e
			return false
	
	getItem: (key) ->
		if localStorage.getItem(key + "cache")
			cacheTime = parseInt(localStorage.getItem(key + "cache"))
			if cacheTime < this.getTS()
				localStorage.removeItem(key)
				localStorage.removeItem key + "cache"
				return null
		if localStorage.getItem(key)
			localStorage.getItem(key)
		else
			null
	
	setItem: (key, value, cacheTime) ->
		TSEndCache = parseInt(cacheTime) + this.getTS()
		localStorage.setItem key, value
		localStorage.setItem key + "cache", TSEndCache
	
	getTS: ->
		Math.round new Date().getTime() / 1000

player = new Player()
successArray = new Array()
actualPosition = 0
actualSecteurOnMap = 0
selectedSecteur = 0
selectedSuccess = 0
selectedMessage = 0

cache = new Cache()
cache.initDB("GeoCache", "0.5", "100000")

init = ()->
	if cache.getItem('pseudo')
		$('#connexionPseudo').val(cache.getItem('pseudo'))
	if cache.getItem('mdp')
		$('#connexionPassword').val(cache.getItem('mdp'))
	if cache.getItem('pseudo') and cache.getItem('mdp')
		launchAuth()

launchAuth = ()->
	if !loading	
		pseudo = $('#connexionPseudo').val();
		mdp = $('#connexionPassword').val();
		
		if pseudo.length < 3 or mdp.length < 3
			alert('Vous devez rentrer vos identifiants pour vous connecter mon général')
		else
			loading = true
			$.get 'geowarfare/auth?p='+pseudo+'&md='+mdp, (jqXHR, textStatus, errorThrown) ->
				launchAuthCompleted(errorThrown.responseText)
		
launchAuthCompleted =(datas) ->
	loading = false
	if datas == "*"
		player = new Player()
		player.pseudo = $('#connexionPseudo').val()
		player.mdp = $('#connexionPassword').val()
			
		cache.setItem('pseudo', player.pseudo, 10000)
		cache.setItem('mdp', player.mdp, 10000)
		
		$.mobile.changePage('#home')
		loadDatasPlayer()
		loadArmyPlayer()
		loadSecteurs()
		 
		###player.pseudo(self.pseudoTmp());
		player.mdp(self.mdpTmp());
		localStorage.setItem('pseudoTmp', self.pseudoTmp());
		localStorage.setItem('mdpTmp', self.mdpTmp());

		self.transition("home");

		player.initArmy();
		self.loadDatasPlayer();
		self.loadArmyPlayer();
		self.loadSecteurs();
		self.loadAlly();
		self.initMap();###
	else 
		alert("Damn it !", "Vos identifiants sont faux ou la connexion a été perdue. Vous pouvez réessayer mon général ?");
		
self.initDB = ()->
	
		
loadDatasPlayer = ()->
	$.get 'geowarfare/getProfil?p='+player.pseudo, (jqXHR, textStatus, result) ->
				loadDatasPlayerCompleted(result.responseText)
loadDatasPlayerCompleted = (datas) ->
	datas = datas.substring(0, datas.length-1)

	json = jQuery.parseJSON(datas)
	player.avatar=json.avatar
	player.biographie=json.biographie
	player.nbSecteurs=json.nbSecteurs
	player.successAsString=json.success
	player.killedUnits=json.KilledUnits
	player.listMessages = new Array()
	updateUI('home')
	loadSuccess()
	
loadArmyPlayer = ()->
	$.get 'geowarfare/army?pr=1&'+getLoginInfos(), (jqXHR, textStatus, result) ->
				loadArmyPlayerCompleted(result.responseText)
loadArmyPlayerCompleted = (response)->
	armyAsString = response.split('|')[0].split('-')
	productionAsString = response.split('|')[1].split('-')
	player.units = new Array()
	for i in [0..4]
		player.units[i] = new Unit(armyAsString[i],productionAsString[i])
	updateUI('army')
	
	
loadSecteurs = ()->
	$.get 'geowarfare/army?ls=json&'+getLoginInfos(), (jqXHR, textStatus, result) ->
				loadSecteursCompleted(result.responseText)
	
loadSecteursCompleted = (response)->
	if response.length > 16
		json = jQuery.parseJSON(response);
		player.secteurs = json.secteur
		updateUI('secteurs')
	
###self.loadSecteurs = function(){
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
	}###
	
	
loadSuccess = ()->
	$.get 'public/XML/Success.xml', (jqXHR, textStatus, result) ->
				loadSuccessCompleted(result.responseText)
loadSuccessCompleted = (response)->
	parser=new DOMParser();
	xmlDoc=parser.parseFromString(response,"text/xml");
	successAsXML = xmlDoc.childNodes[0];
	element;

	for element in successAsXML.childNodes
		if element.nodeType!=3 and element.nodeType!=4
			successArray.push(new Success(element.childNodes[1].childNodes[0].nodeValue,
					element.childNodes[3].childNodes[0].nodeValue,
					element.childNodes[5].childNodes[0].nodeValue,
					element.childNodes[7].childNodes[0].nodeValue,
					element.childNodes[9].childNodes[0].nodeValue,
					element.childNodes[13].childNodes[0].nodeValue
			))
	for success in successArray
		if player.successAsString.indexOf('-'+success.id+'-') isnt -1
			success.isDone = true
	updateUI('success')

getLoginInfos = ()->
	"p="+player.pseudo+"&md="+player.mdp
	
displayDetailsSecteur = (secteurName)->
	selectedSecteur = 0
	for secteur in player.secteurs
		if secteur.nom == secteurName
			selectedSecteur = secteur
	if(selectedSecteur != 0)
		content = ""
		unites = selectedSecteur.unites.split('-')
		for i in [0..4]
			content +='<li data-role="fieldcontain"><img src="public/gfx/Units/'+getUnitByIndex(i)+'.png" /><label style="width:90px;">'+getUnitByIndex(i)+'</label>
			<input type="number" id="detailsSecteur'+getUnitByIndex(i)+'" value="'+unites[i]+'" placeholder="0"/></li>'
		$('#detailsSecteurListview').html(content)
		$.mobile.changePage('#detailsSecteur')
		
detailsSecteurValid = ()->
	nombreInfanterie = -1
	nombreArtilleur = -1
	nombreHelicoptere = -1
	nombreTank = -1
	nombreAvion = -1
	try
		nombreInfanterie = parseInt($("#detailsSecteurInfanterie").val())
		nombreArtilleur = parseInt($("#detailsSecteurArtilleur").val())
		nombreHelicoptere = parseInt($("#detailsSecteurHelicoptere").val())
		nombreTank = parseInt($("#detailsSecteurChar").val())
		nombreAvion = parseInt($("#detailsSecteurAvion").val())
	catch e
		alert "Le format de nombre n'est pas correct mon général !"
	
	if nombreInfanterie < 0 or nombreArtilleur < 0 or nombreHelicoptere < 0 or nombreTank < 0 or nombreAvion < 0
		  alert "Le format de nombre n'est pas correct mon général !"
	else
		error = false
		i = 0
		
		while i < player.units.length
			toCompare = 0
			switch i
				when 0
					toCompare = nombreInfanterie
				when 1
					toCompare = nombreArtilleur
				when 2
					toCompare = nombreHelicoptere
				when 3
					toCompare = nombreTank
				when 4
					toCompare = nombreAvion
			if player.units[i].nombre + selectedSecteur.unites[i] < toCompare
				total = player.units[i].nombre + selectedSecteur.unites[i]
				alert "Vous n'avez pas assez d'unités pour en déployer autant mon général !"
				error = true
				return
			i++
		unites = nombreInfanterie + "-" + nombreArtilleur + "-" + nombreHelicoptere + "-" + nombreTank + "-" + nombreAvion
		secteur = selectedSecteur.nom
		url = "geowarfare/putarmy?" + "s=" + secteur + "&u=" + unites + "&" + getLoginInfos()
		$.get url, (jqXHR, textStatus, result) ->
			detailsSecteurValidCompleted(result.responseText, unites)
		

detailsSecteurValidCompleted = (response, unites)->
	if response.length > 0
		self.loadArmyPlayer()
		selectedSecteur.unites = unites
		alert "Déploiement terminé mon général !"
	else
		alert "Une erreur est survenue lors du déploiement mon général. Vous pouvez réessayer ?"
	$.mobile.changePage('#secteurs', {reverse:true})	
		
updateUI = (page) ->
	switch page
		when "home"
			$('#homePseudo').html(player.pseudo)
			$('#homeBiographie').html(player.biographie);
			nbSuccess = player.successAsString.split('-').length-2
			if(nbSuccess < 0)
				nbSuccess = 0
			$('#homeNbSuccess').html(nbSuccess);
			$('#homeKilledUnits').html(player.killedUnits);
			$('#homeNbSecteurs').html(player.nbSecteurs);
			$('#homeAvatar').attr('src', player.avatar);
		when "army"
			content = ""
			for i in [0..4]
				content +='<li><a href="#"><img src="public/gfx/Units/'+getUnitByIndex(i)+'.png" /><h3>'+getUnitByIndex(i)+'</h3><p>Nombre : '+player.units[i].nombre+' - Production : '+player.units[i].production+'</p></a></li>'
			$('#armyListview').html(content)
		when "secteurs"
			content = ""
			for secteur in player.secteurs
				content += '<li onclick="displayDetailsSecteur(\''+secteur.nom+'\');"><a href="#" >'+secteur.nom+'</a></li>'
			$('#secteursListview').html(content)
		when "success"
			content = ""
			for success in successArray
				if success.isHidden is "false" or (success.isHidden is "true" and success.isDone is true)
					content += '<li onclick="displayDetailsSuccess('+success.id+');"><a href="#"><img src="public/gfx/Success/'+success.nom.replace(' ', '_').replace('\'', '').replace('î', 'i')+'.png" alt="'+success.nom+'"/>'+success.nom+ (if success.isDone is true then "<br/>D&eacute;bloqu&eacute; !" else "")+'</a></li>'
			$('#successListview').html(content)
		
		
nativeLocation = ()->
	if navigator.geolocation
		navigator.geolocation.getCurrentPosition(successCallback,
						errorCallback);
	else 
		alert("Votre navigateur ne supporte pas la géolocalisation. Désolé mon général. Du café pour compenser ?");

successCallback = (position)->
	actualPosition = position;
	latlng = position.coords.latitude+","+position.coords.longitude
	width = document.documentElement.clientWidth - 20
	height = document.documentElement.clientHeight - 60
	scale = 1
	if(width > 800)
		scale = 2
	$('#googleMap').attr('src', 'https://maps.googleapis.com/maps/api/staticmap?center='+latlng+'&zoom=14&size='+width+'x'+height+'&markers='+latlng+'&sensor=false&maptype=hybrid&scale='+scale)
	$('#googleMap').attr('width', width+'px')
	$('#googleMap').attr('height', height+'px')
	
errorCallback = (error)->
	switch error.code
		when error.PERMISSION_DENIED
			alert("Vous n'avez pas accepté la localisation mon général, nous ne pouvons donc pas localiser le QG.");
		when error.POSITION_UNAVAILABLE
			alert("Il semblerait que nous soyons hors de la zone de couverture GPS mon général.");
		when error.TIMEOUT
			alert("Excusez-moi mon général, mais le service de localisation est trop long à répondre. Oui mon général, je vais de suite convoquer le service des trans' en cour martiale !");
	
checkinClick = ()->
	if actualPosition
		url = "geowarfare/checkin?f=json&lon=" + actualPosition.coords.longitude + "&lat=" + actualPosition.coords.latitude
		$.get url, (jqXHR, textStatus, result) ->
					checkinClickCompleted(result.responseText)

checkinClickCompleted = (response) ->
	if response == "$"
		alert('Votre secteur actuel n\'a pas de valeur stratégique mon général !');
	else
		secteurAsJson = jQuery.parseJSON(response)
		$('#dialogSecteurTitle').html(secteurAsJson.secteur.nom)
		if secteurAsJson.secteur.proprietaire.length < 3
			secteurAsJson.secteur.proprietaire = "personne"
			
		actualSecteurOnMap = secteurAsJson.secteur
		
		$('#dialogSecteurContent').html("D&eacute;tenu par "+secteurAsJson.secteur.proprietaire)
		if secteurAsJson.secteur.proprietaire == player.pseudo
			$('#dialogSecteurDetailsButton').addClass("show")
			$('#dialogSecteurConquestButton').addClass("hidden")
		else
			$('#dialogSecteurDetailsButton').addClass("hidden")
			$('#dialogSecteurConquestButton').addClass("show")
			
		$.mobile.changePage('#dialogSecteur', { transition: "slidedown"})
		
detailsSecteurClick = ()->
	if actualSecteurOnMap.proprietaire == player.pseudo
		displayDetailsSecteur(actualSecteurOnMap.nom)
	else
		if actualSecteurOnMap
			content = ""
			units = player.units
			for i in [0..4]
				content +='<li data-role="fieldcontain"><img src="public/gfx/Units/'+getUnitByIndex(i)+'.png" /><label style="width:90px;">'+getUnitByIndex(i)+'<br/>Nombre : '+units[i].nombre+'</label>
				<input type="number" id="conquete'+getUnitByIndex(i)+'" value="0" placeholder="0"/></li>'
			$('#conqueteListview').html(content)
			$.mobile.changePage('#conquete')
			$('#conqueteListview').listview('refresh')

conquestValidClick = ->
	nombreInfanterie = parseInt($("#conqueteInfanterie").val())
	nombreArtilleur = parseInt($("#conqueteArtilleur").val())
	nombreHelicoptere = parseInt($("#conqueteHelicoptere").val())
	nombreTank = parseInt($("#conqueteChar").val())
	nombreAvion = parseInt($("#conqueteAvion").val())
	
	if nombreInfanterie < 0 or nombreArtilleur < 0 or nombreHelicoptere < 0 or nombreTank < 0 or nombreAvion < 0
		alert "Le format de nombre n'est pas correct mon général !"
		return
	if nombreInfanterie > player.units[0].nombre or nombreArtilleur > player.units[1].nombre or nombreHelicoptere > player.units[2].nombre or nombreTank > player.units[3].nombre or nombreAvion > player.units[4].nombre
		alert "Vous n'avez pas assez d'unités à envoyer au combat mon général."
		return
	unites = nombreInfanterie + "-" + nombreArtilleur + "-" + nombreHelicoptere + "-" + nombreTank + "-" + nombreAvion
	intensite = $("#conqueteIntensite").val()
	if unites is "0-0-0-0-0"
		return
	url = "geowarfare/combat?lon=" + actualPosition.coords.longitude + "&lat=" + actualPosition.coords.latitude + "&u=" + unites + "&i=" + intensite + "&" + getLoginInfos()
	url = url.replace(",", ";")
	$.get url, (jqXHR, textStatus, result) ->
		displayResultConquest(result.responseText)

displayResultConquest = (result) ->
	$.mobile.changePage('#resultConquest')
	resultToShare = ""
	title = "Victoire !"
	proprietaire = actualSecteurOnMap.proprietaire
	labelSecteur = actualSecteurOnMap.nom
	if result is "EasyWin !"
		alert "Vous venez de remporter une victoire facile sur le secteur neutre " + labelSecteur
		loadArmyPlayer()
		loadSecteurs()
		$.mobile.changePage('#home')
		return
		
	console.log result
	result = result.replace(/&/g, "-")
	parser = new DOMParser()
	xmlDoc = parser.parseFromString(result, "text/xml")
	combatNode = xmlDoc.childNodes[0]
	victoire = combatNode.childNodes[3].childNodes[0].nodeValue
	if victoire.toLowerCase() is "defenseur"
		title = "Défaite !"
		resultToShare = "Le général " + player.pseudo + " vient de perdre misérablement contre le général " + proprietaire + " sur le secteur " + labelSecteur + "."
	else
		resultToShare = "Le général " + player.pseudo + " vient de remporter le secteur " + labelSecteur + " aux dépends du général " + proprietaire + "."
	$("#resultConquestTitle").html(title)
	$("#resultConquestLabel").html(labelSecteur)
	pertesAttaquantTotal = combatNode.childNodes[4].childNodes[0].nodeValue.split("-")
	pertesDefenseurTotal = combatNode.childNodes[5].childNodes[0].nodeValue.split("-")
	i = 0
	
	contentAttacker = ""
	contentOpponent = ""
	while i < 5
		contentAttacker +='<li><a href="#"><img src="public/gfx/Units/'+getUnitByIndex(i)+'.png" alt="'+getUnitByIndex(i)+'"/><h3>'+getUnitByIndex(i)+'</h3><p>Pertes : '+pertesAttaquantTotal[i]+'</p></a></li>'
		contentOpponent +='<li><a href="#"><img src="public/gfx/Units/'+getUnitByIndex(i)+'.png" alt="'+getUnitByIndex(i)+'"/><h3>'+getUnitByIndex(i)+'</h3><p>Pertes : '+pertesDefenseurTotal[i]+'</p></a></li>'
		i++
		
	$('#attackerListview').append(contentAttacker);
	$('#opponentListview').append(contentOpponent);
	$("#attackerListview").listview('refresh');
	$("#opponentListview").listview('refresh');
	loadArmyPlayer()
	loadSecteurs()
	
displayDetailsSuccess = (successId)->
	selectedSuccess = successArray[successId];
	$('#detailsSuccessTitle').html(selectedSuccess.nom)
	$('#detailsSuccessDescription').html(selectedSuccess.description)
	$('#detailsSuccessObjectif').html("<strong>Objectif : </strong>"+selectedSuccess.objectif)
	$('#detailsSuccessImage').attr('src', getSuccessImage(selectedSuccess.nom))
	$.mobile.changePage('#detailSuccess')
	
shareSuccess = ()->
	toShare = "Le général "+player.pseudo+" a débloqué le succès "+selectedSuccess.nom
	shareTask(toShare)

loadMessages = ()->
	if player.listMessages.length > 0
		return
	else
		url = "geowarfare/GetMessages?" + getLoginInfos()
		$.get url, (jqXHR, textStatus, result) ->
			loadMessagesCompleted(result.responseText)
	
loadMessagesCompleted = (response)->
	json = jQuery.parseJSON(response.replace(/models.GeoMessages/g, "geomessages"))
	console.log response
	i = 0
	
	while i < json.geomessages.length
		player.listMessages.push(new Message(i, json.geomessages[i].expediteur, json.geomessages[i].sujet, json.geomessages[i].content, json.geomessages[i].date, json.geomessages[i].isRead))
		i++
		
	player.listMessages.reverse()
	content = ""
	for message in player.listMessages
		content +='<li><a href="#" onclick="readMessage('+message.id+');"><img src="public/gfx/Noun/'+(if message.isRead then "mailopened" else "noun_mail")+'.png" alt="Message"/><h3>'+message.sujet+'</h3><p>Envoy&eacute; par '+message.expediteur+'</p></a></li>'
		
	$("#messagerieListview").html(content)
	$("#messagerieListview").listview('refresh')
	
readMessage = (messageId) ->
	selectedMessage = 0
	for message in player.listMessages
		if message.id is messageId
			selectedMessage = message
	if selectedMessage isnt 0
	  $('#detailsMessagerieTitle').html(selectedMessage.sujet)
	  $('#detailsMessagerieContent').html(selectedMessage.content)
	  $('#detailsMessagerieDate').html('Envoy&eacute; par '+selectedMessage.expediteur+' le '+selectedMessage.date)
	  $.mobile.changePage('#detailsMessagerie')
	  
writeMessage =  ()->
	$('#messagerieWriterDestinataire').val('')
	$('#messagerieWriterSubject').val('')
	$('#messagerieWriterContent').val('')
	$.mobile.changePage('#messagerieWriter')
	selectedMessage = 0

sendMessage = ()->
	destinataire = $("#messagerieWriterDestinataire").val()
	destinataire = destinataire.replace(" ", "").replace("\n", "")
	sujet = $("#messagerieWriterSubject").val()
	content = $("#messagerieWriterContent").val()
	url = "geowarfare/sendmessage?" + getLoginInfos() + "&d=" + destinataire + "&s=" + sujet + "&c=" + content
	if destinataire.length <= 2
		alert "Nous n'envoyons pas de bouteille à la mer mon général ! Encore moins au QG."
		return
	else if sujet.length < 2
		alert "Le sujet de la transmission doit faire au moins trois caractères."
		return
	else if content.length < 2
		alert "La transmission doit faire au moins trois caractères."
		return
	else if destinataire.trim().toLowerCase() is player.pseudo.trim().toLowerCase()
		alert "Ce n'est pas très utile de s'envoyer des messages à soi-même vous ne croyez pas ?"
		return
	else
		$.get url, (jqXHR, textStatus, result) ->
			sendMessageCompleted(result.responseText)
		
sendMessageCompleted = (response) ->
	if response is "!!"
		alert "Désolé mon général. Le destinataire spécifié n'existe pas !"
	else if response is "!!!"
		alert "La boite de réception du destinataire est pleine mon général ! Vous pouvez réessayer plus tard ?"
	else
		alert "Message envoyé !"
		$.mobile.changePage('#messagerie')
		
answerMessage = ()->
	$('#messagerieWriterDestinataire').val(selectedMessage.expediteur)
	$('#messagerieWriterSubject').val("Re : "+$('#detailsMessagerieTitle').html())
	$('#messagerieWriterContent').val('')
	$.mobile.changePage('#messagerieWriter')
	selectedMessage = 0
	
shareTask = (message)->
	message += " - via GeoWarfare mobile"
	url = "https://twitter.com/intent/tweet?text="+message
	popup(url)
	
popup = (url)->
	window.open(url, null, 'height = 420, width = 550, status = yes, toolbar = no, scrollbars=yes, menubar = no, location = no')
	
getSuccessImage = (src) ->
	src = "public/gfx/Success/"+src.replace(' ', '_').replace('\'', '').replace('î', 'i')+".png"
			
getUnitByIndex = (index)->
	switch index
		when 0
			"Infanterie"
		when 1
			"Artilleur"
		when 2
			"Helicoptere"
		when 3
			"Char"
		when 4
			"Avion"
		
isDBSupported = ()->
	try
		return "localStorage" of window and window["localStorage"] isnt null
	catch e
		return false
		
$(document).ready ->
  init()
