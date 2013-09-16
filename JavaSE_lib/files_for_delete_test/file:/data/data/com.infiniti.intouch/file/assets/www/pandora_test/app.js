log("Pandora app is initializing...");

//Make the APP variable globally accessible
var APP;
var PANDORA = {};

AQ.app.loadScript(["PandoraLink.js", "constants.js", "generator.js", "cache.js", "util.js", "HuComm.js", "HuView.js"], function(){
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
		AQ.app.switchToAudioMode();

		// FrameHandler.setCallback(PANDORA.CallbackObj);
		PANDORA.Display.showLoading();
		// PANDORA.Model.sessionStart();

        var pv = new HuView(PANDORA.CONFIG.apiVersion, PANDORA.CONFIG.accessoryId, PANDORA.CONFIG.albumArtDimension, PANDORA.CONFIG.imageType, PANDORA.CONFIG.flags, PANDORA.CONFIG.stationArtDimension, PANDORA.CONFIG.numStationsPerPage, PANDORA.CONFIG.trackElapsedPolling);
        var huComm = new HuComm();
        controller = new PandoraController(huComm, pv, PANDORA.CONFIG.logLevel);
        controller.eventSessionStart(PANDORA.CONFIG.apiVersion, PANDORA.CONFIG.accessoryId, PANDORA.CONFIG.albumArtDimension, PANDORA.CONFIG.imageType, PANDORA.CONFIG.flags, PANDORA.CONFIG.stationArtDimension);

        log("Pandora is initialized");
	};

	this.displayInvalidLoginNotification = function(){
		PANDORA.Display.poupNotification($.t('invalid_Login_Notification'));
		//TODO : settimeout to stop session?
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
		var playInfo = PANDORA.Cache.getTrackInfo();
		playInfo = $.extend(true, playInfo, controller.getTrackInfo() );

		console.log( JSON.stringify(playInfo, undefined, 2) );

		playInfo.albumCover.image = controller.getAlbumArtGeneric();

		PANDORA.Display.nowPlaying(playInfo);
	};

	this.play = function(){
		controller.eventTrackPlay();
	};
	this.pause = function(){
		controller.eventTrackPause();
	};
	this.skip = function(){
		controller.eventTrackSkip();
	};
	this.thumbsUp = function(){
		controller.eventTrackRatePositive();
	};
	this.thumbsDown = function(){
		controller.eventTrackRateNegative();
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
		this.displayCreatingStationNotification();
	};
	this.createStationByArtist = function(){
		PANDORA.Model.sendCommand("createByArtist");
		//TODO : Need api to get the status and back to player screen
		this.displayCreatingStationNotification();
	};

	this.displayCreatingStationNotification = function(){
		PANDORA.Display.poupWindows($.t('creating_Station'));
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
		var currentStationToken = PANDORA.Cache.getCurrentStationToken();
		if(currentStationToken!=null){
			console.log("current station is :" + currentStationToken);
			this.displayDeleteConfirmation(currentStationToken);
		}
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
		var deleteConfirmData = {
			"msg" : $.t('delete_Confirm_Txt'),
			"action" : "deleteStation",
			"paratemer" : data
		};
		PANDORA.Display.poupConfirm(deleteConfirmData);
	};

	this.deleteStation = function(stationToken){
		PANDORA.Model.deleteStation(stationToken);
	};

	/**
	 * Handles the actions
	 */
	this.AQ_APP_RESPONSE = function(data)
	{
        log("GET RESPONCE FROM PANDORA: "+ JSON.stringify(data));
		if(data.transferEncoding == "base64"){
			var payload = AQ.util.base64ToArray(data.content);

			var bytesLength = payload.length;
			for(var i = 0; bytesLength > 0; i++)
			{
				// Look for end of frame escape byte
				if(payload[i] == Frame.FLAG_LAST)
				{
					// Strip off frame
					var frame = payload.splice(0, i+1);

					// Pass frame up
					FrameHandler.onFrame(frame);

					// Reset length and index as byte array has been spliced
					bytesLength = payload.length;
					i = 0;
				}
				// If at end of payload and no complete frame wait for more data
				else if(i >= bytesLength)
				{
					break;
				}
			}
		}
	};

	/**
	 * Handle the app exit event
	 */
	this.AQ_APP_EXIT = function(){
		PANDORA.Cache.resetTrackInfo();
		PANDORA.Model.sessionTerminate();
	};

	/**
	 * Handle the skip next button event
	 */
	this.AQ_SEEK_NEXT = function(){
		this.skip();
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