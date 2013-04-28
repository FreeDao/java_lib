log("Pandora app is initializing...");

//Make the APP variable globally accessible
var APP;
var PANDORA = {};

AQ.app.loadScript(["constants.js", "generator.js", "callbackobjects.js", "model.js", "PandoraLink.js", "cache.js", "util.js"], function(){
	APP = new Pandora();
	APP.init();
});

/**
 * Pandora class
 */
function Pandora()
{
    this.defaults = {}

	/**
	 * Initialize the app and load the home screen
	 */
	this.init = function(){
		log("Pandora is initialized");
		FrameHandler.setCallback(PANDORA.CallbackObj);
		PANDORA.Display.showLoading();
		PANDORA.Model.sessionStart(3,"APITOOL0",160,44);
	};

	this.displayInvalidLoginNotification = function(){
		PANDORA.Display.poupNotification($.t('invalid_Login_Notification'));
	};

	this.displayNoStationNotification = function(){
		PANDORA.Display.poupNotification($.t('no_Station_Notification'));
	};

	/** Station List */
	this.displayStationList = function(){
		var stations = PANDORA.Cache.getStations();
		if(stations != null){
			PANDORA.Display.stationList(stations);
		}else{
			PANDORA.Model.getStationTokens();
		}
	};

	this.notifyUpdateStationArt = function(){
		var stations = PANDORA.Cache.getStations();
		if(stations != null){
			PANDORA.Display.stationList(stations);
		}
	};

	this.selectStation = function(data){
		var stationToken = data;
		PANDORA.Cache.setCurrentStationToken(stationToken);
		PANDORA.Model.selectStation(stationToken);
		PANDORA.Display.showLoading();
	};

	/** Sort List */
	this.displaySortOptionsMenu = function() {
		PANDORA.Display.sortOptions();
	};

	this.sortStationByDate = function(){
		PANDORA.Model.sendCommand("sortByDate");
	};

	this.sortStationByName = function(){
		PANDORA.Model.sendCommand("sortByName");
	};

	/** show Player */
	this.displayPlayer = function(){
		//console.log("------------------------"+PANDORA.Display._isPlayerScreen());
		//TODO : if is not player, don't update screen when new track info arrived
		var playInfo = PANDORA.Cache.getTrackInfo();

		// playInfo.TrackImg = PANDORA.IMAGES.img.albumCover;
		// playInfo.TrackImg = PANDORA.Cache.getAlbumCover();
		PANDORA.Display.nowPlaying(playInfo);
	};

	this.play = function(){
		PANDORA.Model.sendCommand("play");
	};
	this.pause = function(){
		PANDORA.Model.sendCommand("pause");
	};
	this.skip = function(){
		PANDORA.Model.sendCommand("skip");
	};
	this.thumbsUp = function(){
		PANDORA.Model.sendCommand("thumbsUp");
	};
	this.thumbsDown = function(){
		PANDORA.Model.sendCommand("thumbsDown");
	};

	this.displaySkipLimitNotification = function(){
		PANDORA.Display.poupNotification($.t('skip_Limit_Reached_Warning'));
	};

	/** Create Station Sub-Menu */
	this.displayCreateStationSubMenu = function(data) {
		//
		PANDORA.Display.createStationSubMenu();
	};

	this.createStationByTrack = function(){
		PANDORA.Model.sendCommand("createByTrack");
		//TODO : Need api to get the status and back to player screen
		//this.displayCreatingStationNotification();
	};
	this.createStationByArtist = function(){
		PANDORA.Model.sendCommand("createByArtist");
		//TODO : Need api to get the status and back to player screen
		//this.displayCreatingStationNotification();
	};

	this.displayCreatingStationNotification = function(){
		PANDORA.Display.poupNotification($.t('creating_Station'));
	};

	/**  Player Sub-Menu */
	this.displayPlayerSubmenu = function() {
		//
		PANDORA.Display.playerSubmenu();
	};
	this.loadTrackInfo = function(){
		PANDORA.Model.getExtendTrackInfo();
	};
	this.bookMarkByTrack = function(){
		//
		PANDORA.Model.sendCommand("bookMarkByTrack");
	};
	this.bookMarkByArtist = function(){
		//
		PANDORA.Model.sendCommand("bookMarkByArtist");
	};

	this.deleteCurrentStation = function(){
		//TODO : reflect on cache's data structure
		//TODO :How can we get the token if start with the player.
		var currentStation = {
			"token" : PANDORA.Cache.getCurrentStationToken(),
			"name": ""
		};
		console.log("===currentStation====" + JSON.stringify(currentStation));
		this.displayDeleteConfirmation(currentStation);
	};

	/** Extend Track Info */
	this.displayTrackInfo = function(data){
		data.albumCover = PANDORA.Cache.getAlbumCover();
		PANDORA.Display.trackInfo(data);
	};

	/** Detelable Station List */
	this.displayDetelableStationList = function(){
		var stations = PANDORA.Cache.getStations();
		PANDORA.Display.detelableStationList(stations);
	};

	this.displayDeleteConfirmation = function(data){
		var deleteData = JSON.parse(data);
		var deleteConfirmData = {
			"msg" : $.t('delete_Confirm_Txt').replace('[stationName]',deleteData.name),
			"action" : "deleteStation",
			"paratemer" : deleteData.token
		};
		PANDORA.Display.poupWindows(deleteConfirmData);
	};

	this.deleteStation = function(stationToken){
		PANDORA.Model.deleteStation(stationToken);
	};

	/**
	 * Handles the actions
	 */
	this.AQ_APP_RESPONSE = function(data){
        log("GET RESOPNCE FROM PANDORA: "+ JSON.stringify(data));
		if(data.transferEncoding == "base64"){
			/*
				data.content = AQ.app.base64ToHex( data.content); //data.content = AQ.app.base64ToHex( data.content.replace(/\n/ig,"") );
				if( data.content === false ){
					return;
				}
				console.log("============hex string : " + data.content);

				data.content = data.content.split(" ");
				var bytes = [];
				for (var i=0; i<data.content.length; i++){
					bytes.push(parseInt(data.content[i].trim(), 16));
				}
				console.log("============bytes : " + bytes);
				FrameHandler.onFrame(bytes);
			*/
			var payload = AQ.app.base64ToArray(data.content);
			FrameHandler.onFrame(payload);
		}
	};

	/**
	 * Handle the app exit event
	 */
	this.AQ_APP_EXIT = function(){
		PANDORA.Model.sessionTerminate();
	};

	/**
	 * Handle the skip next button event
	 */
	this.AQ_SEEK_NEXT = function(){
		//TODO : if current screen is player, and song is playing, skip to the next
		PANDORA.Model.sendCommand("skip");
	};

	/**
	 * Handle the back button
	 */
	this.back = function() {
		PANDORA.Display.historyBack();
	};

	/**
	 * Handles the actions
	 */
	 this.handleAction = function(action, value) {
		this[action](value);
		return;
	};
}//end of contructor