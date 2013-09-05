/**
 * This file wraps all request functions coming
 * from HAP layer to JavaScript layer.
 */



/**
 * Handling soft button click requests - these include
 * list items and 6 soft buttons on the bottom of the screen
 *
 * @param screenId: base 10 screen id
 * @param keyId: base 10 key id of the template
 */
window.softKeyClick = function(screenId, keyId, listItemId)
{
	log("SOFT_KEY_CLICK: Screen ID: "+screenId+"; Key ID: "+keyId+"; List item ID: "+listItemId);
	log( "typeof APP ::: " + typeof APP );

	if( keyId == listItemId && keyId < 0xF0 )
	{
		log("JS - ENTER BUTTON WAS PRESSED");
		AQ.app.runAction( listItemId, listItemId );
		return;
	}

	log("JS - BUTTON #"+keyId.toString(16).toUpperCase()+" WAS PRESSED");
	AQ.app.runAction( keyId, listItemId );

	return;
}


/**
 * Handling soft button click requests - these include
 * list items and 6 soft buttons on the bottom of the screen
 *
 * @param screenId: base 10 screen id
 * @param keyId: base 10 hard key id
 */
window.hardKeyClick = function(screenId, keyId, listItemId)
{
	log("HARD_KEY_CLICK: Screen ID: "+screenId+"; Key ID: "+keyId+"; List item ID: "+listItemId);

	var key = AQ.constants.hardKeys[keyId];

	log("HARD_KEY_CLICK readable name: "+key);

	switch(key)
	{
		case "apps":
			log("APPS button is clicked!!!");
			try{
				AQ.app.load("home");
			}catch(e){log(e);}
		break;

		case "enter":
			AQ.app.runAction( listItemId );
		break;

		case "back":
			log("Action: BACK");
			APP.handleAction("back");
		break;

		case "seek_next":
			log("Action: AQ_SEEK_NEXT");
			APP.handleAction("AQ_SEEK_NEXT");
		break;

		case "seek_previous":
			log("Action: AQ_SEEK_PREVIOUS");
			APP.handleAction("AQ_SEEK_PREVIOUS");
		break;
	}

	return;
}


/**
 * Handling the request to get the text data based
 * on the given array of ids
 *
 * @param appId: application ID base 10
 * @param textId: an array of text ids
 */
window.getText = function(appId, textIds)
{
	//In case if textIds is a string, try to parse it
	if( typeof textIds == "string" )
	{
		try{
			textIds = JSON.parse(textIds);
		}catch(err){
			log( err );
		}
	}

	var textData = {};

	//Go through each text id and get the value of it
	for(var i=0; i<textIds.length; i++)
	{
		var id = textIds[i];
		var value = AQ.storage.getValue(id, 'text');
		textData[id] = value;
	}

	textData = JSON.stringify(textData);

	log("Returning requested text data for text id: "+ JSON.stringify(textIds));
	log( textData );

	AQ.respond.updateText(appId, textData);
}


/**
 * HANDLING THE REQUEST TO GET THE IMAGE DATA
 * Handling the request to get the image data based
 * on the given id
 *
 * @param appId: application ID base 10
 * @param imgId: image ID base 10
 */
window.getImage = function(appId, imgId)
{
	log("Image request on "+ (new Date()) );
	log("Image ID request for: "+imgId);

	if( imgId === 0 )
	{
		AQ.respond.updateImage(appId, imgId, 0, 0, "");
		return;
	}

	var imgData = AQ.storage.getValue(imgId, 'image');

	try{
		imgData = JSON.parse(imgData);
	}catch(e){}

	try{
		// Replace file:///  with  file:///android_asset/www/
		imgData.data = imgData.data.replace(AQ.constants.url.apps_hmi, AQ.constants.url.apps);

		// Replace aq:///  with  file:///android_asset/www/aq/
		imgData.data = imgData.data.replace(AQ.constants.url.common_hmi, AQ.constants.url.common);
	}catch(e){}

	if( AQ.hash.platform == "ios" )
	{
		imgData.data = imgData.data.replace(AQ.constants.url.apps, "file:///");
	}

	log("App ID: "+appId+"; Img ID: "+imgId+"; Data length: "+imgData.data.length+"; Width: "+imgData.w+"; Height: "+imgData.h);

	try{
	   AQ.respond.updateImage(appId, imgId, imgData.w, imgData.h, imgData.data);
	}catch(err){ log(err) }
}


/**
 * HANDLING THE NOTIFICATION ABOUT TTS READY
 *
 * @param ttsId: TTS ID base 10
 */
window.ttsReady = function(ttsId)
{
	log("TTS is ready for id: "+ttsId);

	APP.handleAction("TTS_READY", {ttsId: ttsId});
}


/**
 * HANDLING THE NOTIFICATION ABOUT TTS END
 *
 * @param ttsId: TTS ID base 10
 */
window.ttsEnd = function(ttsId)
{
	log("TTS is finished for id: "+ttsId);

	APP.handleAction("TTS_END", {ttsId: ttsId});
}


/**
 * HANDLING THE NOTIFICATION ABOUT VR PROCESSING END
 *
 * @param text: VR result string
 * @param vrId: VR ID base 10
 */
window.vrEnd = function(vrId, text)
{
	log("VR is ready for id: "+vrId+" - "+text);

	APP.handleAction("VR_END", {text: text, vrId: vrId});
}


/**
 * HANDLING THE NOTIFICATION FROM NOMADIC APP
 *
 * @param string seqNum: sequence number
 * @param string appName: application name
 * @param string transferEncoding
 * @param string contentType
 * @param json content
 */
window.sendAppResponse = function(seqNum, appName, transferEncoding, contentType, content)
{
	log(">>>>>>>> sendAppResponse");

	var defaults = {seqNum:null, appName:null, transferEncoding:null, contentType:null, content:null};
	var args = $.extend(true, defaults, {seqNum:seqNum, appName:appName, transferEncoding:transferEncoding, contentType:contentType, content:content});
	APP.handleAction("AQ_APP_RESPONSE", args);
}


window.notifyAppMessage = function(seqNum, appName, transferEncoding, contentType)
{
	var content = window.android.getAppMessage(seqNum);
	log(">>>>>>>> notifyAppMessage");
	window.sendAppResponse(seqNum, appName, transferEncoding, contentType, content);
}

window.notifyAppImage = function(seqNum,  appName,  contentType,  path)
{
	log(">>>>>>>> notifyAppImage");
	log(path);
	window.sendAppResponse(seqNum, appName, "image/path", contentType, path);
}


window.streamingAudioStateChange = function(){
	AQ.hash.huState.sendOk = true;

	console.log(">>>>>>>> streamingAudioStateChange");
	
	//TODO:AQ.eventqueue.notifyNext();
	AQ.eventqueue.notifyNext();
	/*
	if( $.isEmptyObject(AQ.hash.stagingPage) === false )
    {
        var resp_json =  $.extend(true, {}, AQ.hash.stagingPage.data);

		// Reset the staging
    	AQ.hash.stagingPage = {};

    	AQ.app.updateScreen( resp_json );
    }
    */
}

/**
 * Handle notification from the native layer.
 * @param notification: JSON notification data
 */
window.onNotification = function(notification)
{
	// parse the notification data
	try{
		notification = JSON.parse(notification);
	}catch(err){}

	// process notification
	switch ( notification.name )
	{
		case "setLanguage":
			console.log(">>>>>>>> Set Language Request Message from Head Unit");
			// TODO: internationalize screen data based on this setting
			break;

		case "exit":
			console.log(">>>>>>>> Exit Message from Head Unit");
			// TODO: stop any audio processing

			AQ.hash.huState.sendOk = false;
			break;

		case "headUnitInfo":
			console.log(">>>>>>>> Head Unit Info Message from Head Unit");
			// TODO: save head unit serial number, head unit type
			// TODO: check if the handset has any newer templates
			// TODO: save the meter availability
			
			console.log(JSON.stringify(notification));
			break;

		case "hupScreenUpdated":
			console.log(">>>>>>>> Display Change End Message from Head Unit");

			AQ.hash.huState.sendOk = true;
			AQ.eventqueue.notifyNext();
			/*
			if( $.isEmptyObject(AQ.hash.stagingPage) === false )
	        {
	            var resp_json =  $.extend(true, {}, AQ.hash.stagingPage.data);

				// Reset the staging
	        	AQ.hash.stagingPage = {};

	        	AQ.app.respond( resp_json ); //TODO : ??
	        }
			 */

			break;

		case "templateUpdated":
			console.log(">>>>>>>> Template Update Response Message from Head Unit");
			// TODO: try again if failure?
			break;

		case "dialResponse":
			console.log(">>>>>>>> Dial Response Message from Head Unit");
			if(!notification.status){
				console.log("Dial Failed.");			
				// TODO: try again if failure?
			}else{
				console.log("Dial Succeed.");
			}
			break;

		case "telephoneInterrupt":
			console.log(">>>>>>>> Telephone Interrupt Message from Head Unit");
			// TODO: stop any audio processing
			AQ.hash.huState.sendOk = false;
			
			//TODO : 
			
			break;

		case "resumeApplication":
			console.log(">>>>>>>> Resume Application Request Message from Head Unit");
			// TODO: ACK/NAK request
			// TODO: resume any application if possible

			try{
				AQ.app.load(AQ.hash.currentApp);
			}catch(e){log(e);}

			// AQ.hash.huState.sendOk = true;
			// AQ.respond.updateScreen( AQ.hash.lastPage );

			break;

		case "resumeAudioApplication":
			console.log(">>>>>>>> Resume Audio Application Request Message from Head Unit");
			// TODO: ACK/NAK request
			// TODO: resume last audio application if possible

			// TEMP: For now, send the latest screen update
			try{
				
				// check if AQ.hash.currentApp is audio app
				// if not , return "not good"
				
				AQ.app.load("home");
			}catch(e){log(e);}

			// AQ.hash.huState.sendOk = true;
			// AQ.respond.updateScreen( AQ.hash.lastPage );

			break;
		case "profile":

			AQ.profile.sync(notification);
			
			//AQ.profile.store(notification);
			//AQ.app.load("home");
			
			break;
	}
}