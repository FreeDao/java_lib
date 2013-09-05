/*
===========================================================================================
=            AQ.hash -- Variable to store the map of data valid for this session          =
===========================================================================================
*/

/**
 * Variable to store the map of data - id key pairs for text data and image data.
 */
AQ.hash = {
	text : { AQCOUNT : 1, '' : 0, 'null' : 0, 'undefined' : 0 },
	image : { AQCOUNT : 1 },
	screenId : 0,
	actions : { },
	platform : (function(){ //Find out the current platform by checking the browser user agent. Returns "android" || "ios"
	    var platform = navigator.userAgent.match(/iPhone|Android/i);
        platform =  platform[0].toLowerCase();
        if(platform == "iphone"){ platform = "ios"; }
        return platform;
	}()),
	lastPage : {},
	stagingPage : {},
	huState : {
		sendOk : true,
		processingItems : []
	}
}

/*-----  End of AQ.hash -- Variable to store the map of data valid for this session  ------*/


/*======================================================================================
=            AQ.storage -- manages the data storage for the current session            =
=                The class helps to access and save the data in AQ.hash                =
======================================================================================*/
AQ.storage = {

	/*=======================================
	=            Handset Profile            =
	=======================================*/
	/**
	 * Returns Gateway Info for Choreo.
	 */
	getClientGatewayInfo: function()
	{
	    switch(AQ.hash.platform)
        {
            case "android":
                var gatewayInfo = window.android.getClientGatewayInfo();
                break;

            case "ios":
            	//TODO : 
                NativeBridge.call("getClientGatewayInfo", JSON.stringify(["objCgatewayInfo"]));
                var gatewayInfo = objCgatewayInfo;
                break;
        }
	    //log("gatewayInfo >>>>>>>>> "+ gatewayInfo);
	    return JSON.parse(gatewayInfo);
	},
		
	/**
	 * Returns URL for Choreo.
	 */
//	getClientGatewayUrl: function()
//	{
//	    switch(AQ.hash.platform)
//        {
//            case "android":
//                var url = window.android.getClientGatewayUrl();
//                break;
//
//            case "ios":
//            	//TODO : 
//                NativeBridge.call("getClientGatewayUrl", JSON.stringify(["objCurl"]));
//                var url = objCurl;
//                break;
//        }
//	    //log("url >>>>>>>>> "+ JSON.stringify(url));
//	    return url;
//	},
	
	/**
	 * Returns the MIP ID, Auth Token, Acccess Key ID, 
	 * and App Token headers in JSON format for requests to Choreo.
	 */
//	getClientGatewayHeaders: function()
//	{
//	    switch(AQ.hash.platform)
//        {
//            case "android":
//                var header = window.android.getClientGatewayHeaders();
//                break;
//
//            case "ios":
//            	//TODO : 
//                NativeBridge.call("getClientGatewayHeaders", JSON.stringify(["objCheader"]));
//                var header = objCheader;
//                break;
//        }
//	    //log("header >>>>>>>>> "+ header);
//	    return JSON.parse(header);
//	},

	/**
	 * Get MIP ID from HAP
	 */
//	getMipId: function()
//	{
//	    switch(AQ.hash.platform)
//        {
//            case "android":
//                var profile = window.android.getMipProfile();
//                break;
//
//            case "ios":
//                NativeBridge.call("getMipProfile", JSON.stringify(["objCmipProfile"]));
//                var profile = objCmipProfile;
//                break;
//        }
//
//		try{
//			profile = JSON.parse(profile);
//		}catch(err){}
//
//		return profile.userInfo.mipId;
//	},

	/**
	 * Get the Choreo URL from HAP
	 */
//	getChoreoURL: function()
//	{
//		switch(AQ.hash.platform)
//        {
//            case "android":
//                var profile = window.android.getMipProfile();
//                break;
//
//            case "ios":
//                NativeBridge.call("getMipProfile", JSON.stringify(["objCmipProfile"]));
//                var profile = objCmipProfile;
//
//                //log("mipProfile: "+ JSON.stringify(profile));
//                break;
//        }
//
//		try{
//			profile = JSON.parse(profile);
//		}catch(err){}
//
//		return profile.backendInfo.clientGatewayUrl;
//	},

	/*-----  End of Handset Profile  ------*/

	
	/*================================
	=            Policy Rules        =
	=================================*/
	/**
	 * Get the Policy rules from Choreo.
	 */
	getPolicyRules: function(appName)
	{
		switch(AQ.hash.platform)
        {
            case "android":
            	//TODO : window.android.getPolicyRules(appName);
                break;

            case "ios":
            	//TODO :
                break;
        }

		try{
			//var policyRules = JSON.parse(rules);
			var policyRules = [{
				category : "AQ",
				action : "ELEMENT.DISABLED_WHILE_DRIVING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "AQ",
				action : "ELEMENT.HIDDEN_WHILE_DRIVING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "IMAGE.ALBUM_ART",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "IMAGE.STATION_LOGO",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "IMAGE.ARTIST_IMAGE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "IMAGE.USER_ICON",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "IMAGE.POI_ICON",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "IMAGE.DISABLED_WHILE_DRIVING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "IMAGE.HEADER",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "BUTTON.THUMBS_UP",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.THUMBS_DOWN",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.MENU_ITEM",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.PLAY",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.STOP",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.FAVORITE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.BOOKMARK",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "BUTTON.DISABLED_WHILE_DRIVING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "TEXT.NAME",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "TEXT.ADDRESS",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "TEXT.SHORT_DESCRIPTION",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "TEXT.LONG_DESCRIPTION",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "TEXT.DATE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "TEXT.DISABLED_WHILE_DRIVING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "TEXT.HEADER",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "TEXT.SCROLLING_HEADER",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "TEXT.SCROLLING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "LIST.ITEM_SELECT",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Graphic",
				action : "LIST.PAGE_BY_PAGE_SCROLLING",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Graphic",
				action : "LIST.ITEM_BY_ITEM_SCROLLING",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Audio",
				action : "TTS",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Input",
				action : "KEYBOARD",
				vehicleState : "MOVING",
				allowed : true
			}, {
				category : "Vehicle Bus",
				action : "VB.MAKE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.CONNECT_METHOD",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.GPS_LOCATION",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.SPEED",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.DRIVER_SEAT_OCCUPIED",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.PASSENGER_SEAT_OCCUPIED",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.DOOR_OPEN",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.HEADUNIT_NAME",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.HEADUNIT_DIMENSIONS",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.HEADUNIT_RESOLUTION",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.RPM",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.FUEL_LEVEL",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.TOTAL_MILEAGE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.COOLANT_TEMPERATURE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.AIR_INTAKE_TEMPERATURE/VB.AIR_INTAKE_PRESSURE",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.ENGINE_MAL_FUNCTION",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Vehicle Bus",
				action : "VB.CAN_BUS_DATA",
				vehicleState : "MOVING",
				allowed : false
			}, {
				category : "Clicks",
				action : "CLICKS.COUNT_TILL_FREEZE",
				vehicleState : "MOVING",
				allowed : false
			}];
		}catch(err){}

		return policyRules;
	},

	/*-----  End of Handset Profile  ------*/
	
	
	/*=================================
	=            Screen ID            =
	=================================*/

	generateScreenId: function()
	{
		AQ.hash.screenId++;

		// if screen id count gets over the 64k, reset it to 1
		if( AQ.hash.screenId > 0xFFFF )
		{
			AQ.hash.screenId = 1;
		}

		return AQ.hash.screenId;
	},

	getScreenId: function()
	{
		return AQ.hash.screenId;
	},

	/*-----  End of Screen ID  ------*/


	/*==============================================================================
	=            Managing and storing the text and image values and ids            =
	==============================================================================*/

	/**
	 * Looks for the id for the given value in the hash map.
	 * If no ids found, it will generate a new id and add it
	 * to the hash map and return the new id.
	 */
	getTextId: function(value)
	{
		var id;

		if(typeof value == "undefined" || value == "" || value == null || value == "AQCOUNT")
		{
			return 0;
		}

		if( typeof AQ.hash.text[value] != 'undefined' )
		{
			id = AQ.hash.text[value];
		}else
		{
			id = AQ.storage.addValue(value, 'text');
		}

		//log("Text id is generated for: '"+id+"' ::: "+value);

	  	return id;
	},


	/**
	 * Looks for the id for the given value in the images hash map.
	 * If no ids found, it will generate a new id and add it
	 * to the hash map and return the new id.
	 */
	getImageId: function(value, imgId, templateId)
	{
		var id;

		if(typeof value == "undefined" || value == "" || value == null || value == "AQCOUNT")
		{
			return 0;
		}

		var imageData = {
			data: value,
			w: AQ.constants.imageSizes[templateId][imgId].w,
			h: AQ.constants.imageSizes[templateId][imgId].h
		}
		imageData = JSON.stringify( imageData );


		if( typeof AQ.hash.image[imageData] != 'undefined' )
		{
			id = AQ.hash.image[imageData];
		}else
		{
			id = AQ.storage.addValue(imageData, 'image');
		}

		//log("Image id is generated for: '"+id+"' ::: "+value);

	  	return id;
	},


	/**
	 * Retrieves the value based on the given id.
	 * If no id is found, it returns null.
	 */
	getValue: function(id, type)
	{
		for (var value in AQ.hash[type])
		{
		    if(value != "AQCOUNT")
		    {
		      	if( AQ.hash[type][value] == id )
		      	{
		        	return value;
		    	}
			}
		}
		return null;
	},


	/**
	 * Adds a new entry to the hash map and returns the newly
	 * generated id.
	 */
	addValue: function(value, type)
	{
		var id = AQ.hash[type].AQCOUNT;

		setTimeout(function(){
			AQ.storage.removeValue(id, value, type);
		}, 0);

		AQ.hash[type][value] = id;
		if( AQ.hash[type].AQCOUNT >= 0xFFFF )
		{
			AQ.hash[type].AQCOUNT = 1;
		}else
		{
			AQ.hash[type].AQCOUNT++;
		}
		return id;
	},


	/**
	 * Removes the entry for the given id. Used to manage the
	 * size of the hash map.
	 */
	removeValue: function(id, newValue, type)
	{
		for (var value in AQ.hash[type])
		{
		    if(value != "AQCOUNT")
		    {
		      	if( AQ.hash[type][value] == id && newValue != value)
		      	{
		        	delete AQ.hash[type][value];
		        	return true;
		    	}
			}
		}
		return null;
	},

	/*-----  End of Managing and storing the text and image values and ids  ------*/


	/*===============================
	=            Actions            =
	===============================*/

	/**
	 * Clears the actions map
	 */
	clearAction: function(){
		delete AQ.hash.actions;
		AQ.hash.actions = {};
		return;
	},


	/**
	 * Saves the action in the keyId:action map
	 */
	setAction: function(keyId, action, value)
	{
		AQ.hash.actions[keyId] = {
			action:action,
			value:value
		};

		return;
	},


	/**
	 * Gets the action from the keyId:action map
	 */
	getAction: function(keyId)
	{
		return $.extend({}, AQ.hash.actions[keyId]);
	}

	/*-----  End of Actions  ------*/
}