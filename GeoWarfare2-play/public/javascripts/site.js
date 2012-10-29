var rid;
function requete(url, div) {
	try {
		var requete = new XMLHttpRequest();
		requete.open('GET', url, false);
		requete.send(null);
		var contenu = requete.responseText;
		return contenu;
	} catch (e) {
		return "error";
	}
}
function loadCaptcha(){
	rid = requete("/Geowarfare/getcaptchaid", null);
	$("#captchaImg").attr('src', '/Geowarfare/captcha?id='+rid);
}
function validRegisterForm(){
	var valid = true;
	var pseudo = $('#pseudo').val();
	var mdp = $('#mdp').val();
	var mdp2 = $('#mdp2').val();
	var parrain = $('#parrain').val();
	var avatar = $('#avatar').val();
	var captcha = $('#captcha').val();
	var cgu = $('#cgu').attr('checked');
	var hasToFadeIn = false;
	var hasToFadeOut = false;

	if(pseudo.length < 3)
	{
		$('#pseudo').addClass('invalidInput');
		valid = false;
	}
	
	if(mdp.length < 3)
	{
		$('#mdp').addClass('invalidInput');
		valid = false;
	}
	
	if(mdp != mdp2)
	{
		$('#mdp').addClass('invalidInput');
		$('#mdp2').addClass('invalidInput');
		$('#registerErrorDiv').html('Il serait de bon ton que vos deux mots de passe correspondent non ?');
		$('#registerErrorDiv').fadeIn('slow');
		valid = false;
		hasToFadeIn = true;
	}
	else if($('#registerErrorDiv').css('display') == 'block')
	{
		hasToFadeOut = true;
	}
	
	if(cgu != 'checked')
	{
		$('#registerErrorDiv').html('Il est nécessaire d\'accepter les CGU pour s\'inscrire. Mais c\'est logique non ?');
		$('#registerErrorDiv').fadeIn('slow');
		valid = false;
		hasToFadeIn = true;
	}
	else
	{
		hasToFadeOut = true;
	}
	
	if(captcha.length != 5)
	{
		$('#captcha').addClass('invalidInput');
		$('#registerErrorDiv').html('Un captcha valide fait 5 caractères. Ni plus... Ni moins...');
		$('#registerErrorDiv').fadeIn('slow');
		valid = false;
		hasToFadeIn = true;
	}
	else
	{
		hasToFadeOut = true;
	}
	
	if(!hasToFadeIn && hasToFadeOut)
		$('#registerErrorDiv').fadeOut('slow');
	
	if(valid)
	{
		var url = "/Geowarfare/createaccount?p=" + pseudo + "&md=" + mdp+ "&a=" + avatar + "&code=" + captcha
		+ "&rid=" + rid+"&par="+parrain;
		
		var answer = requete(url, null);
		if(answer == "!fa")
		{
			$('#captcha').addClass('invalidInput');
			$('#registerErrorDiv').html('Le captcha indiqué est invalide. Vous pouvez réessayer ?');
			$('#registerErrorDiv').fadeIn('slow');
			loadCaptcha();
		}
		else if(answer == "!aep")
		{
			$('#pseudo').addClass('invalidInput');
			$('#registerErrorDiv').html('Le pseudo indiqué est déjà utilisé. Vous devriez en choisir un autre.');
			$('#registerErrorDiv').fadeIn('slow');
		}
		else if(answer == "!ts")
		{
			$('#registerErrorDiv').html('Une erreur est survenue à l\'inscription. Si cela survient à nouveau, vous devriez contacter l\'administrateur.');
			$('#registerErrorDiv').fadeIn('slow');
		}
		else if(answer == "*")
		{
			$('#registerSuccessDiv').html('Inscription réussie avec succès. Bienvenue général '+pseudo+' !');
			$('#registerSuccessDiv').fadeIn('slow');
			loadCaptcha();
			document.getElementById('formRegister').reset();
		}
	}
}
function changeAvatar(){
	var avatar = $('#avatar').val();
	if(avatar.indexOf('http') == -1)
		avatar = "http://"+avatar;
	if(avatar.length > 12)
		$('#avatarImg').attr('src', avatar);
}
function shareClick(message, shareNetworkIndex){
	var url="";
	switch(shareNetworkIndex){
	case 0:
		break;
	case 1:
		message = "@GeoWarfare "+message+" "+window.location.href;
		url = "https://twitter.com/intent/tweet?text="+message;
		break;
	case 2:
		var toShare = window.location.href;
		toShare = toShare.replace("&", "%26");
		url="https://www.facebook.com/sharer.php?u="+toShare;
		break;
	}
	if(url != "")
		popup(url);
}
function popup(url){				
	window.open(url, null, 'height = 420, width = 550, status = yes, toolbar = no, scrollbars=yes, menubar = no, location = no');
}