/**
 *
 * @type {*}
 */
var HuView = PandoraView.extend({

    init:function (apiVersion, accessoryId, albumArtDimension, imageType, flags, stationArtDimension, numStationsPerPage, trackElapsedPolling) {
        this._super(apiVersion, accessoryId, albumArtDimension, imageType, flags, stationArtDimension, numStationsPerPage, trackElapsedPolling);
    },

	onConnect:function() {
		console.log("Pandora is connected");
	},

	onDisconnected:function() {
		console.log("Pandora is disconnected");
	},

	setTrackTitle:function(trackTitle) {
		console.log("Setting track title");
	},

	setTrackArtist:function(trackArtist) {
		console.log("Setting track artist");
	},

	setTrackAlbum:function(trackAlbum) {
		console.log("Setting track album");
	},

	setTrackElapsed:function(trackElapsed) {},
	setTrackDuration:function(trackDuration) {},

	setTrackRating:function(trackRating) {
		console.log("Setting track rating");
		PANDORA.Cache.setTrackRating( trackRating );

		APP.displayPlayer();
	},

	setTrackBookmarkTrack:function(isBookmarked) {
		console.log("Setting track bookmarked flag");
	},

	setTrackExplain:function(trackExplainText) {
		console.log("Setting track explain");
	},

	setTrackInfo: function(trackInfo)
	{
		// PANDORA.Cache.setAlbumName( trackInfo.getAlbumName() );
		// PANDORA.Cache.setTrackName( trackInfo.getTrackName() );
		// PANDORA.Cache.setArtistName( trackInfo.getArtistName() );
		// PANDORA.Cache.setTrackRating( trackInfo.getRating() );

		// controller.pv.setTrackDuration(getTimeFromSeconds(payload.getDuration()));
		// controller.pv.setTrackElapsed(getTimeFromSeconds(payload.getElapsed()));
		// controller.pv.disableRating(payload.getAllowRating());
		// controller.pv.disableSkip(payload.getAllowSkip());
		// controller.pv.disableBookmark(payload.getAllowBookmark());
		// controller.pv.disableExplain(payload.getAllowExplain());
		// controller.pv.disableCreateStationFrom(payload.getAllowCreateStationFrom());


		APP.displayPlayer();
	},

	setTrackAlbumArtGeneric:function(albumArt) {
		// PANDORA.Cache.setAlbumCover(albumArt);
		APP.displayPlayer();
	},

	setDefaultTrackAlbumArt:function(albumArt) {
		// PANDORA.Cache.setAlbumCover(albumArt);
		APP.displayPlayer();
	},

	clearTrackAlbumArt:function() {
		// PANDORA.Cache.setAlbumCover("");
	},

	clearTrackInfo:function() {
		//Clear track info (meta data, album art time, etc)
		this.setTrackTitle("");
		this.setTrackArtist("");
		this.setTrackAlbum("");
		this.setTrackElapsed("");
		this.setTrackDuration("");
		this.setTrackRating(resources.track.rating.PNDR_RATING_NONE);
		this.setIsTrackBookmarked(false);
		this.setTrackExplain("");
		this.clearTrackAlbumArt();
	},

	disableRating:function(allowRating) {

	},

	disableSkip:function(allowSkip) {

	},

	disableBookmark:function(allowBookmark) {

	},

	disableExplain:function(allowExplain) {

	},

	disableCreateStationFrom:function(allowCreateStationFrom) {

	},

	setIsAudioAd:function(isAudioAd) {

	},

	setIsTrackBookmarked:function(isTrackBookmarked) {

	},

	showStatusPlaying:function() {
		//Set Play/Pause button to show that user can pause
		PANDORA.Cache.setPlayStatus(true);

		APP.displayPlayer();
	},

	showStatusPaused:function() {
		//Set Play/Pause button to show that user can play
		PANDORA.Cache.setPlayStatus(false);

		APP.displayPlayer();
	},

	setTrackElapsedPolling:function(trackElapsedPolling) {
		//Set any indicator that the track timer will be updated
		// or enable UI to start displaying track elapsed
	},

	showLoading:function() {
		//Show Loading screen
		AQ.app.showLoading();
	},

	hideLoading:function() {
		//Hide Loading screen
		AQ.app.hideLoading();
	},

	showNowPlaying:function() {
		//Show NowPlaying screen
		APP.displayPlayer();
	},

	hideNowPlaying:function() {},

	showStationList:function() {
		//Show station list screen

	},

	hideStationList:function() {
		//Hide station list screen

	},

	showStationCreation:function() {
		//Show station creation screen

	},

	hideStationCreation:function() {
		//Hide station creation screen

	},

	showExtendedSearchResults:function(searchResults) {
		//Show search results
	},

	showAutoCompleteSearchResults:function(searchResults) {
		//Show search results
	},

	hideSearchResults:function() {
		//Hide search results
	},

	showTrackExplain:function() {

	},

	hideTrackExplain:function() {

	},

	showStatusMessage:function(statusCode) {
		//Show status message

	},

	hideStatusMessage:function() {
		//Clear status message

	},

	showNoticeMessage:function(noticeCode) {
		//Show notice message

	},

	hideNoticeMessage:function() {
		//Clear notice message

	},

	setStationsOrder:function(stationsOrder) {
		// Set the stations order
	},

	setStationCount:function(stationCount) {
		//Display the station count

	},

	setStartIndex:function(startIndex) {
		//Display the station start index

	},

	setEndIndex:function(endIndex) {
		//Display the station end index

	},

	setActiveStationInfo:function(activeStationInfo) {

	},

	clearActiveStation:function() {

	},

	setStations:function(stationInfoList, isUpdatedStationList) {
		//Sets ALL stations info
		PANDORA.Cache.setStations(stationInfoList);
	},

	setStationsArtGeneric:function(stationsArt, isUpdatedStationList) {
		//Sets artwork for ALL stations with NO image specific header
	},

	setStation:function(stationToken, station) {
		// Update/refresh the station info for stationToken

	},

	setStationArtGeneric:function(stationToken, stationArt) {
		// Update/refresh the station art image for stationToken with NO image specific header
	}
});
