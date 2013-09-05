AQ.app = {

    // Initialize the app based on the app.json data
    init: function(data)
    {
        if(data.locales === true)
        {
            $.jsperanto.init(function(t)
            {
                AQ.app._initFiles(data);
            }, {
                lang : AQ.app.getLanguage(),
                dicoPath : "../"+ AQ.hash.currentApp +"/"+ data.localesFolder
            });
        }else
        {
            AQ.app._initFiles(data);
        }
    },

    _initFiles: function(data)
    {
        if( AQ.constants.environment === "production" )
        {
            var files = data.productionFiles;
        }else{
            var files = data.files;
        }

        for(var i in files)
        {
            files[i] = "../"+ AQ.hash.currentApp +"/"+ files[i];
        }

        console.log("Loading files: "+ JSON.stringify(files) );

        AQ.app.loadScript(files, function()
        {
            APP = new window[data.mainClass]();
            APP.init()
        });
    },


    updateScreen: function(resp_json)
    {
        // return this._updateScreenNormal(resp_json);
    	//console.log("updateScreen is called: "+JSON.stringify(resp_json));

    	return this._updateScreenTimeout(resp_json);
	},

    _updateScreenNormal: function(resp_json)
    {
        if( JSON.stringify(AQ.hash.lastPage) === JSON.stringify(resp_json) )
        {
            return {status: "error", description: "SAME_AS_LAST_PAGE"};
        }

        log( "AQ.hash.huState.sendOk == " + AQ.hash.huState.sendOk );

        // If the head unit is not ready to receive the screen update,
        // save the data to the staging, which will be later used to
        // generate a screen once the HU is available
        if( AQ.hash.huState.sendOk === false )
        {
            // Set the response json into staging, which will be used later to generate a screen
            AQ.hash.stagingPage = $.extend(true, {}, resp_json);
            return;
        }

        //log(" ");
        //log(" ");
        //log(" ");
        //log( "updateScreen: " + JSON.stringify(resp_json) );

        AQ.hash.lastPage = $.extend(true, {}, resp_json);
        AQ.respond.updateScreen( resp_json );

        return {status: "ok"};
    },

    _updateScreenTimeout: function(resp_json)
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
        
        //log(" ");
        //log(" ");
        //log(" ");
        //log( "updateScreen: " + JSON.stringify(resp_json) );

        // Wait for timeout, and in case HMI sends another screen update
        // event within timeout period, ignore the previous one and
        // send the latest one. This is a TEMP workaround for the case when
        // HMI is sending too many screen update events within the short time
        setTimeout(function(resp_json) //TODO: improve this implementation
        {
            // if( AQ.hash.huState.sendOk === false )
            // {
            //     AQ.hash.lastPage = $.extend(true, {}, AQ.hash.stagingPage);
            //     return;
            // }

            // If the data was updated during the timeout time, then ignore the current one
            if( AQ.hash.stagingPage.time + timeoutTime - 50 > (new Date()).getTime() ||
                JSON.stringify(AQ.hash.stagingPage.data) != JSON.stringify(resp_json) )
            {
                log("DATA WAS UPDATED... IGNORING THE CURRENT DATA");
                return;
            }

            var screenData = $.extend(true, {}, AQ.hash.stagingPage.data);

            //log("SENDING THE DATA TO THE HEAD UNIT");
            //log( JSON.stringify(screenData, undefined, 2) );

            // Reset the staging
            //AQ.hash.stagingPage = {};

            AQ.hash.lastPage = $.extend(true, {}, screenData);
            //AQ.respond.updateScreen( screenData );
            AQ.eventqueue.add("ScreenUpdate",null);
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
	
	setCurrentApp: function(appName)
	{
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
	
	getPlatform:function(){
		return AQ.hash.platform;
	},
	
    getCurrentContainer:function(){
        NativeBridge.call("getCurrentContainer", JSON.stringify(["objContainerName"])); //return some thing  like "com.airbiquity.ihr"
        var containerName = objContainerName;
        log("containerName: " + JSON.stringify(containerName));
        return containerName;
    },
    
    switchApps : function(appName,appId,screenId){
        NativeBridge.call("switchApp",JSON.stringify([appName, appId, screenId])); // "iHR", "Pandora"
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


    showLoading : function()
    {
        return AQ.respond.showLoading(1);
    },

    hideLoading : function()
    {
        return AQ.respond.showLoading(0);
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
            scriptNode.src = "../"+AQ.hash.currentApp+"/"+filename[i];
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
                NativeBridge.call("getLocation", JSON.stringify(["objClocation"]));
                var loc = objClocation;
                return loc;
//                return { latitude: 47.604535, longitude: -122.337935 }; //TODO: Get the location in iOS
                break;
        }
    },


    //TODO: emulate: http://api.jquery.com/jQuery.ajax/
    ajax: function(args)
    {
    	var gatewayInfo = AQ.storage.getClientGatewayInfo();
    	/*{
    	    "headers": {
    	        "Mip-Id": "4722bac9-b350-11e2-862c-bd8a87e207bd",
    	        "App-Token": "S2V5VmVyc2lvbj0xO1R5cGU9QU5EUk9JRDtUaW1lPTEzNjc2MDA4NDg2NTImMTY4NzBmNzkxMGFmMGUzODIyOTViOTc0OGQ0NWJmZDI0NzhiZjMwMTM3NmEyOGFlNWE4NmQ1MjliMmU0ZGFiYw==",
    	        "Auth-Token": "NDcyNDY4N2EtYjM1MC0xMWUyLTg2MmMtYmQ4YTg3ZTIwN2Jk",
    	        "Access-Key-Id": "A1r619u1ty!",
    	        "Hu-Id": "123456789000"
    	    },
    	    "url": "https://nissanmip-mipgw.viaaq.com/"
    	}*/
    	
        args.url = gatewayInfo.url + args.url;

        var defaults = {
            headers:{
                "Content-Type" : "application/json"
            }
        }
        args = $.extend(true, defaults, args);

        // Headers are overwritten with the current Mip Id and HU Id
        args.headers["Hu-Id"] = gatewayInfo.headers["Hu-Id"];
        args.headers["Mip-Id"] = gatewayInfo.headers["Mip-Id"];// AQ.storage.getMipId();
        args.headers["Auth-Token"] = gatewayInfo.headers["Auth-Token"];
        args.headers["Access-Key-Id"] = gatewayInfo.headers["Access-Key-Id"];
        args.headers["App-Token"] = gatewayInfo.headers["App-Token"];
        
        log("Making an ajax call...");
        log( JSON.stringify(args) );

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


    switchToAudioMode : function()
    {
    	AQ.eventqueue.add("AudioPlay",1);
    	return;
        //return AQ.respond.switchToAudioMode(1);
    },

    exitAudioMode : function()
    {
    	AQ.eventqueue.add("AudioPlay",2);
    	return;
        //return AQ.respond.switchToAudioMode(2);
    },

    updateMeter : function(data)
    {
        //log( "Setting a meter with the following data: "+ JSON.stringify(data, undefined, 2) );
    	AQ.eventqueue.add("MeterUpdate",data);
        return;
    },

    makecall : function(num)
    {
        //TODO: Implement the phone dialing API
        return AQ.respond.dialNumber(num);
    },


    sendAppRequest : function(args)
    {
        return AQ.respond.sendAppRequest(args);
    },

    startVR : function(language)
    {
        return AQ.respond.startVR(language);
    },
    stopVR : function(vrId)
    {
        return AQ.respond.stopVR(vrId);
    },


    processTts : function(text, language)
    {
        return AQ.respond.processTts(text, language);
    },
    playTts : function(ttsId)
    {
        return AQ.respond.playTts(ttsId);
    },
    stopTts : function(ttsId)
    {
        return AQ.respond.stopTts(ttsId);
    }
}
