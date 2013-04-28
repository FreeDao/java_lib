/**
 * iHeartRadio Display
 *
 * Holds all functions to convert the given data to the template format JSON
 */
 iHR.Display = {
 	defaults: {
 		historyWrapper: [],
		currentScreen: ""
 	},

 	home: function(data) {
 		this._historyReset();
 		this._historySave({page:'home',value : data});
 		var template_data = $.extend(true, {}, iHR.STATE.main);
 		for(var i in data){
			//TODO : Remove this filter after get the latest APK.
			if(data[i].name == "iheart_original_stations"){
				continue;
			}
 			template_data.template_content.list.push({
 				"text" : data[i].menuText,
 				"action" : "requestCallbackDispture",
 				"value" : JSON.stringify({"name": data[i].name, "actionCommand" : data[i].actionCommand})
 			});
 		}

 		AQ.app.updateScreen( template_data );
 	},
 	myStationSubMenu: function() {
		this._historySave({page:'myStationSubMenu'});
 		AQ.app.updateScreen( iHR.STATE.my_station_submenu );
 	},
 	subMenu: function(data) {
 		this._historySave({page:'subMenu', value:data});
 		var template_data = $.extend(true, {}, iHR.STATE.station_submenus);
		var menuItems = data.data;
 		for(var i in menuItems){
			var cateName = (data.name == "LIVE_MENU")? menuItems[i].name : data.name; //liveStationsMenu has different name for each items.
 			template_data.template_content.list.push({
 				"text" : menuItems[i].menuText,
 				"action" : "requestCallbackDispture",
 				"value" : JSON.stringify({"name": cateName, "actionCommand" : menuItems[i].actionCommand})
 			});
 		}

 		AQ.app.updateScreen( template_data );
 	},
 	stationList: function(data) {
 		this._historySave({page:'stationList', value:data});
 		var template_data = $.extend(true, {}, iHR.STATE.station_list);
 		for(var i in data){
 			template_data.template_content.list.push({
 				"text" : data[i].name,
 				"action" : "initPlayerData",
 				"value" : JSON.stringify(data[i])
 			});
 		}

 		AQ.app.updateScreen( template_data );
 	},
 	customStationList: function(data) {
 		this._historySave({page:'customStationList', value:data});
 		var template_data = $.extend(true, {}, iHR.STATE.station_list);
 		for(var i in data){
 			template_data.template_content.list.push({
 				"text" : data[i].name,
 				"action" : "loadCustomerPlayer",//initPlayerData
 				"value" : JSON.stringify(data[i])
 			});
 		}

 		AQ.app.updateScreen( template_data );
 	},
	featuredArtists: function(data) {
 		this._historySave({page:'featuredArtists', value:data});
 		var template_data = $.extend(true, {}, iHR.STATE.station_list);
 		for(var i in data){
			var dataItem = data[i];
			dataItem.type= "custom";
 			template_data.template_content.list.push({
 				"text" : data[i].name,
 				"action" : "initPlayerData",
 				"value" : JSON.stringify(dataItem)
 			});
 		}

 		AQ.app.updateScreen( template_data );
 	},
 	showCustomStationPlayer: function(data) {
 		this._historySave({page:'showCustomStationPlayer', value:data});
 		var template_data   = $.extend(true, {}, iHR.STATE.custom_station_player);
		if(data.isPlayingSong){
 			template_data.template_content.buttons["1"] = {
 				"image" : iHR.IMAGES.button['1_pause'],
 				"action" : "asynchronousEventPostDispture",
 				"value" :   "PAUSE"
 			};
 		}else{
			template_data.template_content.buttons["1"] = {
				"image" : iHR.IMAGES.button['1_play'],
				"action" : "asynchronousEventPostDispture",
				"value" :   "PLAY"
			};
 		}
		
		template_data.template_content.buttons["2"] = {
			"image" : (data.isliked==2) ? iHR.IMAGES.button['2_disliked']:iHR.IMAGES.button['2_dislike'],
			"action" : (data.isliked==2) ? "":"asynchronousEventPostDispture",
			"value" :  "THUMBSDOWN"
		};
		template_data.template_content.buttons["3"] = {
			"image" :  (data.isliked==1) ? iHR.IMAGES.button['3_liked'] : iHR.IMAGES.button['3_like'] ,
			"action" :  (data.isliked==1) ? "":"asynchronousEventPostDispture",
			"value" :  "THUMBSUP"
		};
		var displayInfo = data.stationInfo.name;
		//{ "seedValue":"653018", "id":"4fc647c8f0f05252fd63780f", "varietyLevel":"1", "seedType":"track", "type":"custom", "name":"Say Something" }
		//{ "id":"2502871", "title":"Nice To Know You", "albumName":"Monuments And Melodies", "albumId":"2502104", "artistId":"6768", "trackNumber":"0", "explicitLyrics":"false", "trackLength":"283" }
		if(data.trackInfo!=null){
			displayInfo = data.trackInfo.title + "\r\n" + data.trackInfo.albumName;
		}
 		template_data.template_content.text_right_main = displayInfo;
 		template_data.template_content.image_left = (data.image!=null)? data.image : iHR.IMAGES.album_cover;
 		AQ.app.updateScreen( template_data );
		this._setCurrentScreen("player");
 	},
 	showLiveStationPlayer: function(data) {
 		this._historySave({page:'showLiveStationPlayer', value:data});
 		var template_data  = $.extend(true, {}, iHR.STATE.live_station_player);
		if(data.isPlayingSong){
 			template_data.template_content.buttons["1"] = {
 				"image" : iHR.IMAGES.button['1_pause'],
 				"action" : "asynchronousEventPostDispture",
 				"value" :   "PAUSE"
 			};
 		}else{
			template_data.template_content.buttons["1"] = {
				"image" : iHR.IMAGES.button['1_play'],
				"action" : "asynchronousEventPostDispture",
				"value" :   "PLAY"
			};
 		}
		if(data.isScanAvailable){
			template_data.template_content.buttons["2"] = {
				"image" : iHR.IMAGES.button['2_scan_enabled'],
				"action" : "asynchronousEventPostDispture",
				"value" : "SCAN"
			};
		}else{
			template_data.template_content.buttons["2"] = {
				"image" : iHR.IMAGES.button['2_scan_disabled'],
				"action" : ""
			};
		}
		var displayInfo = data.stationInfo.name + "\r\n" + data.stationInfo.description;
		//Station : { "callLetter":"WLTW-FM", "id":"1477", "band":"FM", "description":"New York's Best Variety", "name":"106.7 Lite fm", "state":"NY", "frequency":"106.7", "type":"live", "city":"New York" }
		//Track : {"songId": STRING, "artistId": STRING, "artistName": STRING, "thumbs": "true" | "false", "isPlayingSong": "true" | "false", "songTitle": STRING }
		if(data.trackInfo!=null){
			displayInfo = data.trackInfo.artistName + "\r\n" + data.trackInfo.songTitle;
		}
 		template_data.template_content.text_right_main = displayInfo;
 		template_data.template_content.image_left = (data.image!=null)? data.image : iHR.IMAGES.album_cover;
 		AQ.app.updateScreen( template_data );
		this._setCurrentScreen("player");
 	},
    discoveryTuner : function(data){
    	this._historySave({page:'discoveryTuner'});
		//TODO : Check the API
		/*
    	var template_data = $.extend(true, {}, iHR.STATE.select_discovery_tuner);
    	for(var i in data){
    		template_data.template_content.list.push({
    			"text" : data[i],
    			"action" : "discoveryTuner",
    			"value" :data[i]
    		});
    	}
    	*/
    	AQ.app.updateScreen( iHR.STATE.select_discovery_tuner );
    },

    deleteableStationList: function(data) {
    	this._historySave({page:'deleteableStationList', value:data});
    	var template_data = $.extend(true, {}, iHR.STATE.delete_station_list);
    	for(var i in data){
    		template_data.template_content.list.push({
    			"text" : data[i].name,
    			"action" : "deleteConfirmation",
    			"value" : JSON.stringify(data[i])
    		});
    	}

    	AQ.app.updateScreen( template_data );
    },
    showConfirmaction: function(data) {
    	this._historySave({page:'showConfirmaction'});
    	var template_data = $.extend(true, {}, iHR.STATE.confirmaction);
    	template_data.template_content.text_bottom["1"] = {
    		"text" : $.t("yes"),
    		"action" : data.action,
    		"value" : JSON.stringify(data.parameter)
    	};
    	template_data.template_content.text_bottom["3"] = {
    		"text" : $.t("no"),
    		"action" : "back"//"confirmBack"
    	};
    	template_data.template_content.text_main = data.msg;

    	AQ.app.updateScreen( template_data );
    },
    showNotifaciation: function(data) {
    	this._historySave({page:'showNotifaciation'});
    	var template_data = $.extend(true, {}, iHR.STATE.notifaciation);
    	template_data.template_content.text_main = data;
    	AQ.app.updateScreen( template_data );
    },
	/**
    *
    * Usually used when no item is found
    *
    **/
    noItemPage: function(data){
        var defaults = {
            text: "No items found. Go back?",
            action: "back"
        }

        data = $.extend(true, defaults, data);

		//TODO:
        var template_data = $.extend(true,{}, iHR.STATE.no_item_page);
        template_data.template_content.list.push(data);
        AQ.app.updateScreen(template_data);
    },

    showLoading: function(){
		AQ.respond.showLoading();
    },

	_setCurrentScreen:function(page){
		iHR.Display.defaults.currentScreen = page;
	},
	_getCurrentScreen:function(){
		return iHR.Display.defaults.currentScreen;
	},

    /**
     * Save the page in the history
     */
     _historySave : function(history){
        //If the previous page was the temp history page, remove it from the history and relaunch the function
        if( iHR.Display.defaults.historyWrapper.length > 1 && iHR.Display.defaults.historyWrapper[iHR.Display.defaults.historyWrapper.length-1].isTemp === true )
        {
        	iHR.Display.defaults.historyWrapper.pop();
        	iHR.Display._historySave(history);
        	return;
        }
        if( iHR.Display.defaults.historyWrapper.length == 0 )
        {
        	iHR.Display.defaults.historyWrapper.push( history );
        }
        else if( iHR.Display.defaults.historyWrapper[iHR.Display.defaults.historyWrapper.length-1].page != history.page )
        {
        	iHR.Display.defaults.historyWrapper.push( history );
        }
		//Added by Kenny to solved the problem on overwrite history data of reused submenu page
		else if( history.page == "subMenu" )
        {
        	iHR.Display.defaults.historyWrapper.push( history );
        }
		else
        {
        	console.log("Same history. Overwriting the data.");
        	iHR.Display.defaults.historyWrapper[ iHR.Display.defaults.historyWrapper.length -1 ] = history;
        }
        return;
    },

    /**
     * Reset the history
     */
     _historyReset : function(){
     	iHR.Display.defaults.historyWrapper = [];
     },

    /**
     * Removes the number of history items starting with the newest
     */
     _historyRemove : function(num){
     	num = num || 1;

     	for(var i=0; i<num; i++)
     	{
     		iHR.Display.defaults.historyWrapper.pop();
     	}
     },

	/**
     * Move back in the history and load the previous page
     */
     historyBack : function(){
        //Remove the current page from the history array
        iHR.Display.defaults.historyWrapper.pop();
        if(iHR.Display.defaults.historyWrapper.length < 1){
			AQ.app.load('home');
			return;
		}
        var history = iHR.Display.defaults.historyWrapper.pop();
        iHR.Display[ history.page ]( history.value );
	}
};

console.log("Finished load generator.js");