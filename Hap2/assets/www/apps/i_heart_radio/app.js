console.log("iHeartRadio app is initializing...");
var APP;
var iHR = {};

AQ.app.loadScript(["constants.js", "generator.js", "model.js", "cache.js"], function() {
	try {
		APP = new iHeartRadio();
		APP.init();
	} catch(err) {
		console.log("Unable to initialize the iHeartRadio: " + err);
	}
});

iHR.asyRequestCallbackMap = {
	defaults: {
		_npos : -1, // DONOT change this
		_seqNums: [],
		_requestCallbacks:[]
	},
	_comp : function (k1, k2) {
		return (k1==k2);
	},
	_find : function (key) {
		for (var i=0; i<this.defaults._seqNums.length; i++) {
			if (this._comp(this.defaults._seqNums[i], key)) {
				return i;
			}
		}
		return this.defaults._npos;
	},
	insert : function (key, value) {
		for (var i=0; i<this.defaults._seqNums.length; i++) {
			if (this._comp(this.defaults._seqNums[i], key)) {
				this.defaults._requestCallbacks[i] = value;
				return;
			}
		}
		this.defaults._seqNums.push(key);
		this.defaults._requestCallbacks.push(value);
	},
	getSize : function () {
		return this.defaults._seqNums.length;
	},

	getCallback : function (key) {
		var index = this._find(key);
		return this.defaults._requestCallbacks[index];
	},
	erase : function (key) {
		var index = this._find(key);
		if (index != this.defaults._npos) {
			this.defaults._seqNums.splice(index, 1);
			this.defaults._requestCallbacks.splice(index, 1);
			return true;
		}
		return false;
	},
	clear : function () {
		this.defaults._seqNums.splice(0);
		this.defaults._requestCallbacks.splice(0);
	}
};

/**
 * iHeartRadio app class
 */
 function iHeartRadio() {
	var that  = this;
	this.seqNumWrapper = {};

 	this.init = function() {
		this.requestCallbackDispture(JSON.stringify({"name": "main_menu", "actionCommand" : {"command":"mainMenu"}}));
 	};

	this.requestCallbackDispture = function(data){
		var para = JSON.parse(data);
		var requestCommand = para.actionCommand.command;
		var requestParams = null;
		var requestCallback = null;

		switch (para.name){
			case "main_menu":
				//command : mainMenu
				requestCallback = this.displayMainMenu;
			break;
			case "my_stations":
				this.displayMyStationSubmenu();
				return;
			break;
			case "my_stations_orderby":
				//command : getMyStationsList , params : {"sortOrder": opts}
				requestCallback = this.displayMyStationList;
			break;
			case "my_stations_custom":
				requestCallback = this.displayCustomeStationList;
			break;
			case "deletable_stations":
				//command : getMyStationsList
				requestCallback = this.displayDeletableStation;
			break;
			case "stations_nearby":
				//command : getStationsNearby
				requestCallback = this.displayNearbyStationList;
			break;
			case "live_stations":
				//command : liveStationsMenu
				requestCallback = this.displayLiveStationsMenu;
			break;
			case "music_and_entertainment":
				//command : liveStationsMusicGenresMenu
				requestCallback = this.displayLiveMusicGenresMenu;
			break;
			case "talk_radio":
				//command : liveStationsTalkGenresMenu
				requestCallback = this.displayLiveTalkGenresMenu;
			break;
			case "live_stations_by_filter":
				//command : getLiveStationsByGenre ,  params :{ "genreId":"18" }
				//command : getLiveStationsByCity ,  params :{ "cityId":"3" }
				requestParams = para.actionCommand.params;
				requestCallback = this.displayLiveStationList;
			break;
			case "stations_by_location":
				//command : liveStationsStatesMenu
				requestCallback = this.displayLiveStatesMenu;
			break;
			case "live_stations_states_cities":
				//command : liveStationsCitiesMenu,  params :{ "stateId":"2" }
				requestParams = para.actionCommand.params;
				requestCallback = this.displayLiveCitiesMenu;
			break;

			case "featured_custom_stations":
				//command : featuredStationsGenresMenu
				requestCallback = this.displayFeaturedStationsGenresMenu;
			break;
			/*
			case "iheart_original_stations":
				//command : getFeaturedStations
				//requestCallback = this.displayFeaturedStationList;
			break;
			*/
			case "recently_played_stations":
				//command : getRecentStations
				requestCallback = this.displayRecentStationList;
			break;
			case "artists_by_genres":
				//command : getFeaturedArtistsByGenre, params : { "artistCategoryId": "56" }
				requestParams = para.actionCommand.params;
				requestCallback = this.displayFeaturedArtists;
			break;

		}
		if(requestCallback!=null && requestCommand!=null){
			var requestSeqNum = iHR.Model.actionCommandRequest(requestCommand, requestParams);
			this.registerRequestHandle(requestSeqNum, requestCallback);
		}
	};

	this.asynchronousEventPostDispture = function(data){
		var currentPlayInfo = iHR.Cache.getPlayingState();
		var requestCommand = null;
		var requestParams = null;
		var requestCallback = null;
		switch(data){
			case "PLAY" :
				requestCommand = "play";
				requestParams = { "stationId": currentPlayInfo.stationInfo.id, "stationType": currentPlayInfo.stationInfo.type };
			break;
			case "PAUSE" :
				requestCommand = "pause";
			break;
			case "SCAN" :
				requestCommand = "scanLiveStation";
			break;
			case "SAVE" :
				iHR.Display.showNotifaciation($.t("saving_Station"));
				requestCommand = "saveLiveStation";
				requestParams = { "stationId": currentPlayInfo.stationInfo.id };
				requestCallback = this.back;
			break;
			case "NEW" :
				iHR.Display.showNotifaciation($.t("creating_New_Station"));
				requestCommand = "createCustomStation";
				if(currentPlayInfo.trackInfo!=null && currentPlayInfo.trackInfo.artistId!=null){
					requestParams =  { "artistId": currentPlayInfo.trackInfo.artistId, "playStation": "true" };	//songId
				}else{
					requestParams =  { "featuredStationId": currentPlayInfo.stationInfo.id, "playStation": "true" };
				}
				requestCallback = this.eventNewStationConfirmation;
			break;
			case "SKIP" : //?
				requestCommand = "skipSong";
				requestCallback = this.eventSkipSongCallBack;
			break;
			case "THUMBSUP" : //?
				requestCommand = "thumbsUpSong";
				requestCallback = this.eventThumbsUpSongCallBack;
			break;
			case "THUMBSDOWN" : //?
				requestCommand = "thumbsDownSong";
				requestCallback = this.eventThumbsDownSongCallBack;
			break;
			case "SETVARIETYLEVEL" : //?
				requestCommand = "setVarietyLevel";
				requestParams = { "level": "1" };
			break;
			case "SETTING": //?
			break;
		};
		var actionSeqnum = iHR.Model.actionCommandRequest(requestCommand, requestParams);
		if(requestCallback!=null){
			this.registerRequestHandle(actionSeqnum, requestCallback);
		}
	};

 	this.displayMainMenu = function(data) {
		//command: mainMenu
		var mainMenu = data.menu;
 		iHR.Display.home(mainMenu);
 		console.log("iHeartRadio Initialized");
 	};

	this.displayMyStationSubmenu = function(){
		iHR.Display.myStationSubMenu();
	};

	this.displayLiveStationsMenu = function(data){
		//command : liveStationsMenu
		var menuData = {"name":"LIVE_MENU","data" : data.menu}; ////liveStationsMenu has different name for each items.
 		iHR.Display.subMenu(menuData);
	};

	this.displayLiveMusicGenresMenu = function(data){
		//command : liveStationsMusicGenresMenu"
		var menuData = {"name" : "live_stations_by_filter", "data" : data.musicGenres};
 		iHR.Display.subMenu(menuData);
	};

	this.displayLiveTalkGenresMenu = function(data){
		//command : liveStationsTalkGenresMenu"
		var menuData = {"name" : "live_stations_by_filter", "data" : data.talkGenres};
 		iHR.Display.subMenu(menuData);
	};

	this.displayLiveStatesMenu = function(data){
		//command : liveStationsStatesMenu"
		var menuData = {"name" : "live_stations_states_cities", "data" : data.states};
 		iHR.Display.subMenu(menuData);
	};

	this.displayLiveCitiesMenu = function(data){
		//command : liveStationsCitiesMenu",
		var menuData = {"name" : "live_stations_by_filter", "data" : data.cities};
 		iHR.Display.subMenu(menuData);
	};

	this.displayFeaturedStationsGenresMenu = function(data){
		//command : featuredStationsGenresMenu
		var menuData = {"name" : "artists_by_genres", "data" : data.genres};
 		iHR.Display.subMenu(menuData);
	};

	this.displayFeaturedArtists = function(data){
		//command : getFeaturedArtistsByGenre
		var artists = data.artists;
 		iHR.Display.featuredArtists(artists);
	};

	this.displayNearbyStationList = function(data){
		//command : getStationsNearby
		var nearbyStations = data.nearbyStations;
		//
		iHR.Display.stationList(nearbyStations);
	}

	this.displayMyStationList = function(data) {
		var myStations = data.stations;
		//
		iHR.Display.stationList(myStations);
	};

	this.displayLiveStationList = function(data) {
		//command : getLiveStationsByGenre
		//command : getLiveStationsByCity
		var liveStations = data.stations;
		//
		iHR.Display.stationList(liveStations);
	};

	this.displayRecentStationList = function(data){
		var recentStations = data.recentStations;
		//
		iHR.Display.stationList(recentStations);
	};

	/* TODO : this function was removed
	this.displayFeaturedStationList = function(data){
		var featuredStations = data.featuredStations;
		//
		iHR.Display.stationList(featuredStations);
	};*/

	this.displayCustomeStationList = function(data) {
		var customStations = data.stations;
		//
		iHR.Display.customStationList(customStations);
	};
	this.loadCustomerPlayer = function(data){
		//display player
		this.initPlayerData(data);
		//TODO : this logic had some issues...
		iHR.Display._historyRemove(2);
	};

 	this.displayDeletableStation = function(data) {
		var myStations = data.stations;
 		iHR.Display.deleteableStationList(myStations);
 	};
	this.deleteConfirmation = function(data) {
		var deleteData = JSON.parse(data);
		var deleteConfirmData = {
			"msg" : $.t("delete_Confirm_Txt").replace("[station_name]",deleteData.name),
			"action" : "deleteStation",
			"parameter" : deleteData
		};
		iHR.Display.showConfirmaction(deleteConfirmData);
	};
	this.deleteStation = function(data) {
		var deleteItem = JSON.parse(data);
		var deleteSeqnum = iHR.Model.actionCommandRequest("deleteStation", {"stationId" : deleteItem.id, "stationType":deleteItem.type});
		this.registerRequestHandle(deleteSeqnum, function(data){
			console.log("=== delete station callback: =====" + JSON.stringify(data));
			that.back();
		});
	};
	this.confirmBack = function(){
 		//
		this.back();
	};

	this.displayDiscoveryTuner = function(data){
		iHR.Display.discoveryTuner(data);
	};
	
	this.initPlayerData = function(data) {
		iHR.Display.showLoading();
		var _stationInfo = JSON.parse(data);
		if(iHR.Cache.getPlayingState().stationInfo!=null && iHR.Cache.getPlayingState().stationInfo.id == _stationInfo.id){
			this.showPlayer();
		}else{
			if(iHR.Cache.getPlayingState().stationInfo!=null && iHR.Cache.getPlayingState().isPlayingSong){
				//TODO : Stop Play
				//this.asynchronousEventPostDispture("PAUSE");
			}
			iHR.Cache.setPlayingState({"image" : null, "stationInfo" : _stationInfo, "trackInfo": null, "isPlayingSong" : false, "isScanAvailable" : false});
			var imgSeqNum = iHR.Model.getImageRequest(_stationInfo.imageCommand.params);
			this.registerRequestHandle(imgSeqNum, this.displayPlayerWithImage);
			//this.displayPlayerWithImage(null);
		}
	};

	this.displayPlayerWithImage = function(imageItem) {
		iHR.Cache.updateImg(imageItem);
		that.showPlayer();
		//Auto Play
		this.asynchronousEventPostDispture("PLAY");
	};

	/* */
	this.onStateChanged = function(data){
		/*
		"eventData":{ "playerPlaying":"(true|false)" }
		*/
		console.log("onStateChanged was called");
		iHR.Cache.updatePlayStatus(data.playerPlaying);
		if(iHR.Display._getCurrentScreen() == "player" ){
			that.showPlayer();
		}
	};
	this.onScanAvailableChanged = function(data){
		/*
		"eventData":{ "scanAvailable": "true" }
		*/
		console.log("onScanAvailableChanged was called");
		iHR.Cache.updateScanStatus(data.scanAvailable);
		if(iHR.Display._getCurrentScreen() == "player" ){
			that.showPlayer();
		}
	};

	this.onStationChanged = function(data){
		/*
		Custom : "eventData":{ "seedValue":"653018", "id":"4fc647c8f0f05252fd63780f", "imageCommand":{ "command":"getImage", "params":{ "type":"custom", "id":"653018", "subType":"track" }}, "varietyLevel":"1", "seedType":"track", "type":"custom", "name":"Say Something" }
		Live : "eventData":{ "callLetter":"WLTW-FM", "id":"1477", "band":"FM", "description":"New York's Best Variety", "name":"106.7 Lite fm", "state":"NY", "imageCommand":{ "command":"getImage", "params":{ "type":"live", "id":"1477", "subType":"live" } }, "frequency":"106.7", "type":"live", "city":"New York" },
		*/
		console.log("onStationChanged was called");
		iHR.Cache.updateStationInfo(data);
		if(iHR.Display._getCurrentScreen() == "player"){
			that.showPlayer();
		}
	};

	//(Live Radio)
	this.onMetaDataChanged = function(data){
		/*
		"eventData":{"songId": STRING, "artistId": STRING, "artistName": STRING, "thumbs": "true" | "false", "isPlayingSong": "true" | "false", "songTitle": STRING }
		*/
		console.log("onMetaDataChanged was called");
		iHR.Cache.updateTrackInfo(data);
		if(iHR.Display._getCurrentScreen() == "player" ){
			that.showPlayer();
		}
	};

	//(Custom Radio)
	this.onTrackChanged = function(data){
		/*
		"eventData":{ "id":"2502871", "title":"Nice To Know You", "albumName":"Monuments And Melodies", "albumId":"2502104", "artistId":"6768", "trackNumber":"0", "explicitLyrics":"false", "trackLength":"283" }
		*/
		console.log("onTrackChanged was called");
		iHR.Cache.updateTrackInfo(data);
		if(iHR.Display._getCurrentScreen() == "player" ){
			that.showPlayer();
		}
	};

	this.onDMCASkipFail = function(data){
		/*
		"eventData":{ "errorMessage":"Sorry, you've reached your skip limit for this station.\nWant to know more? Visit help.iheartradio.com.", "timeToNextAvailableSkip":"3506991" }
		*/
		console.log("onDMCASkipFail was called");
		//TODO : Show Notification ?
	};

	this.onPlayerError = function(data){
		/*
		"eventData":{ "code": <error_code>, "description":<error_message> }
		*/
		console.log("onPlayerError was called");
		//TODO :Show Notification ?
	};

	this.asynchronousEventCallbackDispture = {
		"onStateChanged" : this.onStateChanged,
		"onMetaDataChanged": this.onMetaDataChanged,
		"onDMCASkipFail": this.onDMCASkipFail,
		"onScanAvailableChanged": this.onScanAvailableChanged,
		"onPlayerError": this.onPlayerError,
		"onTrackChanged": this.onTrackChanged,
		"onStationChanged": this.onStationChanged
	};
	/*  */
	
	/*  */
	this.eventSkipSongCallBack = function(data){
		if(data.errors!=null){
			that.back();
		}else{
			//TODO: wait screen update ?
		}
	};
	
	this.eventThumbsUpSongCallBack = function(data){
		if(data.errors!=null){
			return;
		}else{
			iHR.Cache.updateThumbsSong(1);
			that.showPlayer();
		}
	};
	
	this.eventThumbsDownSongCallBack = function(data){
		if(data.errors!=null){
			return;
		}else{
			iHR.Cache.updateThumbsSong(2);
			that.showPlayer();
		}
	};

	this.eventNewStationConfirmation = function(data){
		if(data.errors!=null){
			that.back();
		}else{
			console.log(data);
			var playConfirmData = {
				"msg" : $.t("play_now"),
				"action" : "playNewStation",
				"parameter" : iHR.Cache.getStationId()
			};
			iHR.Display.showConfirmaction(playConfirmData);
			iHR.Display._historyRemove(2);
		}
	};
	/*  */
	
	this.playNewStation = function(data){
		var stationId = data;
		//TODO : playing new station with current id
	};

	this.showPlayer = function(){
		var playerInfo = iHR.Cache.getPlayingState();
		if(playerInfo.stationInfo.type == "live"){
			iHR.Display.showLiveStationPlayer(playerInfo);
		}else{
			iHR.Display.showCustomStationPlayer(playerInfo);
		}
	};

	/**
	 * register Asynchronous Request Callback
	*/
	this.registerRequestHandle = function(seqNum,handle){
		iHR.asyRequestCallbackMap.insert(seqNum,handle);
	};

	/**
	 * Called by APP when get appResponse
	*/
	this.AQ_APP_RESPONSE = function(args){
		try{
			args.content = JSON.parse(args.content);
		}catch(e){}

		var seqNum = args.seqNum;

		console.log( ">>>>>>>> AQ_APP_RESPONSE <<<<<<<<<" );
		//console.log( JSON.stringify(args, undefined, 2) );
		//console.log( "seqNum command: "+ JSON.stringify(this.seqNumWrapper[seqNum]) );

		if(typeof this.seqNumWrapper[seqNum] == "undefined")
		{
			if(args.content.eventID!=null)
			{
				var handle = this.asynchronousEventCallbackDispture[args.content.eventID];
				if( typeof handle != "undefined" )
				{
					handle(args.content.eventData);
				}
			}
		}else
		{
			var handle = iHR.asyRequestCallbackMap.getCallback(args.seqNum);
			if( handle != null )
			{
				handle(args.content);
				iHR.asyRequestCallbackMap.erase(args.seqNum);
			}
		}
	};

	/**
	 * History back
	 */
 	this.back = function() {
 		iHR.Display.historyBack();
 	};
	
	this.AQ_APP_SEEK_NEXT = function(){
		//TODO: check if current screen is player screen
		this.asynchronousEventPostDispture("SKIP");
	};
	
	this.AQ_APP_EXIT = function(){
		//TODO : Stop and clear everything
		this.asynchronousEventPostDispture("PAUSE");
	};
	/**
	 * Handles the actions
	 */
	 this.handleAction = function(action, value) {
		//console.log(action, value);
		try {
			this[action](value);
		}catch(ex){
			console.log(ex);
		}

		return;
	};
}//end of constructor