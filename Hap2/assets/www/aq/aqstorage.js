/**
 * Variable to store the map of data - id key pairs for text data and image data.
 */

AQ.hash = {
	text : { AQCOUNT : 1, '' : 0, 'null' : 0, 'undefined' : 0 },
	image : { AQCOUNT : 1 },
	actions : { },
	platform : (function(){ //Find out the current platform by checking the browser user agent. Returns "android" || "ios"
	    var platform = navigator.userAgent.match(/iPhone|Android/i);
        platform =  platform[0].toLowerCase();
        if(platform == "iphone"){ platform = "ios"; }
        return platform;
	}()),
	lastPage : {},
	stagingPage : {}
}


/**
 * AQ.storage class is used to save/access variables
 * to/from the AQ.hash
 */
AQ.storage = {
	/**
	 * Get MIP ID from HAP
	 */
	getMipId: function()
	{
	    switch(AQ.hash.platform)
        {
            case "android":
                var profile = window.android.getMipProfile();
                break;

            case "ios":
                var profile = {
                	userInfo : {
                		mipId : "a611530d-5df5-11e2-bde3-e510d88d7faf"
                	}
                };
                //TODO: Get the MIP profile in iOS
                break;
        }

		try{
			profile = JSON.parse(profile);
		}catch(err){}

		return profile.userInfo.mipId;
	},


	/**
	 * Get the Choreo URL from HAP
	 */
	getChoreoURL: function()
	{
		switch(AQ.hash.platform)
        {
            case "android":
                var profile = window.android.getMipProfile();
                break;

            case "ios":
                var profile = {
                	backendInfo : {
                		clientGatewayUrl : "http://nissanmip-mipgw-pre.viaaq.net:80/"
                	}
                };
                //TODO: Get the MIP profile in iOS
                break;
        }

		try{
			profile = JSON.parse(profile);
		}catch(err){}

		return profile.backendInfo.clientGatewayUrl;
	},


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
}