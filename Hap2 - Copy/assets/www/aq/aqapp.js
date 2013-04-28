AQ.app = {

    updateScreen: function(resp_json)
    {
        if( JSON.stringify(AQ.hash.lastPage) === JSON.stringify(resp_json) )
        {
            return {status: "error", description: "SAME_AS_LAST_PAGE"};
        }

        var timeoutTime = 200; //in milliseconds

        if( $.isEmptyObject(AQ.hash.stagingPage) === false )
        {
            AQ.hash.stagingPage = {};
        }

        // Set the screen data into staging, which will be used later to generate a screen
        AQ.hash.stagingPage.data = $.extend(true, {}, resp_json);
        AQ.hash.stagingPage.time = (new Date()).getTime();
        log(" ");
        log(" ");
        log(" ");
        log( "updateScreen: " + JSON.stringify(resp_json) );

        // Wait for timeout, and in case HMI sends another screen update
        // event within timeout period, ignore the previous one and
        // send the latest one. This is a TEMP workaround for the case when
        // HMI is sending too many screen update events within the short time
        setTimeout(function(resp_json) //TODO: improve this implementation
        {
            // If the data was updated during the timeout time, then ignore the current one
            if( AQ.hash.stagingPage.time + timeoutTime - 50 > (new Date()).getTime() ||
                JSON.stringify(AQ.hash.stagingPage.data) != JSON.stringify(resp_json) )
            {
                log("DATA WAS UPDATED... IGNORING THE CURRENT DATA");
                return;
            }

            log("SENDING THE DATA TO THE HEAD UNIT");

            var screenData = $.extend(true, {}, AQ.hash.stagingPage.data);

            log( JSON.stringify(screenData, undefined, 2) );

            // Reset the staging
            AQ.hash.stagingPage = {};

            AQ.hash.lastPage = $.extend(true, {}, screenData);
            AQ.respond.updateScreen( screenData );
        }, timeoutTime, resp_json);

        return {status: "ok"};
    },

	/**
	 * Loads the given app - sets the current app variable and refreshes the page
	 * @param {string} appName
	 */
	load: function(appName)
	{
		log("Got the request to load the: "+ appName);

		if( this.isValidApp(appName) )
		{
		    switch(AQ.hash.platform)
            {
                case "android":
                    window.android.putString("CURRENT_APP", appName);
                    break;

                case "ios":
                    NativeBridge.call("putString", JSON.stringify(["CURRENT_APP", appName]));
                    break;
            }

			window.location.reload();
		}
		return;
	},


	/**
	* Quit the current app and load the home app
	**/
    exit: function()
    {
        AQ.app.load('home');
    },


	/**
	 * Goes through staticly saved app list and checks if the given
	 * app name is there.
	 *
 	 * @param {string} appName
	 */
	isValidApp: function(appName)
	{
		for(var app in AQ.constants.apps)
		{
			if( app == appName )
			{
				return true;
			}
		}

		return false;
	},


	/**
	 * Returns the currently running app's id
	 */
	getAppId: function()
	{
		var appName = AQ.hash.currentApp;

		return AQ.constants.apps[ appName ];
	},


	/**
    * Returns the language code
    * TODO: should come from the phone
    **/
    getLanguage: function()
    {
        var languages = ["en-US", "zh-CN"];
        return languages[0];
    },


	/**
     * Loads the Javascript file dynamically
     *
     * @param {string} filename - should include the app name. Eg.: facebook/script.js
     * @param {function} callback [optional] - callback function to be evoked when the script is loaded
     */
    loadScript: function(filename, callback)
    {
        log("Loading JS: "+ JSON.stringify(filename));

        if( typeof filename == 'string' )
        {
            filename = [ filename ];
        }

        if( typeof callback == 'undefined' )
        {
            callback = function(){};
        }

        var countLoaded = 0;

        for(var i in filename)
        {
            var headID = document.getElementsByTagName("body")[0];
            var scriptNode = document.createElement('script');
            scriptNode.type = 'text/javascript';
            scriptNode.src = "apps/"+AQ.hash.currentApp+"/"+filename[i];
            log("scriptNode.src == "+ scriptNode.src);

            scriptNode.className = 'app_js';

            scriptNode.onload = function()
            {
                countLoaded++;
                if(countLoaded == filename.length)
                {
                    callback();
                }
            }

            headID.appendChild(scriptNode);
        }

        return true;
    },


    //TODO: Get the current location from the phone
    getLocation: function()
    {
        switch(AQ.hash.platform)
        {
            case "android":
                var loc = window.android.getLocation();

                try{
                    loc = JSON.parse(loc);
                }catch(e){}

                console.log( JSON.stringify(loc, undefined, 2) );
                return loc;
                break;

            case "ios":
                return { latitude: 47.604535, longitude: -122.337935 }; //TODO: Get the location in iOS
                break;
        }
    },


    //TODO: emulate: http://api.jquery.com/jQuery.ajax/
    ajax: function(args)
    {
        args.url = AQ.storage.getChoreoURL() + args.url;

        var defaults = {
            headers:{
                "Content-Type" : "application/json"
            }
        }

        // Merge with default values
        args = $.extend(true, defaults, args);

        // Headers are overwritten with the current Mip Id and HU Id
        args.headers["Hu-Id"] = "12345678901233119";
        args.headers["Mip-Id"] = AQ.storage.getMipId();

        log("Making an ajax call...");
        log( JSON.stringify(args, undefined, 2) );

        return $.ajax(args);
    },


    /**
     * Gets the action from the keyId:action map and executes it
     */
    runAction: function(keyId, listItemId)
    {
        log("--- keyId: "+ keyId +"; listItemId: "+ listItemId);

        var softKey = $.extend({}, AQ.hash.actions[keyId]);

        if(typeof softKey == "undefined")
        {
            return;
        }

        // If one of the 6 soft buttons is clicked and the value is of
        // the button is aq_active_list_item, get the value of the
        // value of the active list item
        if( keyId > 0xF0 && softKey.value == "aq_active_list_item" )
        {
            listItemAction = $.extend({}, AQ.hash.actions[listItemId]);
            softKey.value = listItemAction.value;
        }

        log("--- Running the action final: "+ JSON.stringify(softKey) );

        if( typeof softKey.action == "undefined" && typeof softKey.value == "undefined" )
        {
            return;
        }

        APP.handleAction( softKey.action, softKey.value );

        return;
    },


    hexToBase64 : function(str)
    {
        return AQ.util.hexToBase64(str);
    },

    base64ToHex : function(str)
    {
        return AQ.util.base64ToHex(str);
    },

	base64ToArray : function(Str)
	{
        return AQ.util.base64ToArray(Str);
	}
}
