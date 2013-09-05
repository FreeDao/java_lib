/**
 * Pandord Model
 */

PANDORA.Model = {
	/*
	 * Session start command
	 */
	sessionStart : function() {
		//PNDR_SESSION_START

        var payload = new SessionStartPayload();
        payload.setApiVersion( PANDORA.CONFIG.apiVersion );
        payload.setAccessoryId( PANDORA.CONFIG.cId );
        payload.setAlbumArtSize( PANDORA.CONFIG.albumArtDimension );
        payload.setStationArtDimension( PANDORA.CONFIG.stationArtDimension );
        payload.setImageType( PANDORA.CONFIG.imageType );
        payload.setFlags(0);

		this.sendData(payload);
	},

	sessionTerminate : function() {
		//PNDR_SESSION_TERMINATE
		var payload = new SessionTerminatePayload();
		this.sendData(payload);
	},

	getStationTokens : function(){
		//PNDR_GET_STATION_TOKENS
        //PNDR_GET_ALL_STATION_TOKENS
		var payload = new GetStationTokensPayload();
		payload.setCount(10);
		this.sendData(payload);
	},

	getStationList : function(tokens){
		//PNDR_GET_STATION_INFO

		var stationInfoPayload = new GetStationInfoPayload();
		stationInfoPayload.setStationTokens(tokens);
		this.sendData(stationInfoPayload);

		//PNDR_GET_STATION_ART
		var stationArtPayload = new GetStationArtPayload();
		stationArtPayload.setMaxPayloadLength(400);
		stationArtPayload.setStationTokens(tokens);
		this.sendData(stationArtPayload);

		//PNDR_GET_ALL_STATIONS
	},

	getStationArt : function(tokens){

	},

	/*
	getStationsOrder : function(){
		var stationsOrderPayload = new GetStationsOrderPayload();
		this.sendData(stationsOrderPayload);
		console.log("========get stationsOrderPayload ... ");
	},

	getStationActived : function(){
		//PNDR_GET_STATION_ACTIVE
		var payload = new GetStationActivePayload();
		this.sendData(payload);
		console.log("========get GetStationActivePayload ... ");
	},
	*/

	selectStation : function(token){
		//PNDR_EVENT_STATION_SELECT
		var payload = new EventStationSelectPayload();
		payload.setStationToken(token);
		this.sendData(payload);
		console.log("========get EventStationSelectPayload : " + token);
	},

	getTrackInfo : function(){
		//PNDR_GET_TRACK_INFO
		//PNDR_GET_TRACK_TITLE
		//PNDR_GET_TRACK_ARTIST
		//PNDR_GET_TRACK_EXPLAIN
		//PNDR_GET_TRACK_ALBUM
		//PNDR_GET_TRACK_ALBUM_ART

		var trackTitlePayload = new GetTrackTitlePayload();
		this.sendData(trackTitlePayload);

		var trackArtistPayload = new GetTrackArtistPayload();
		this.sendData(trackArtistPayload);

		//var trackExplainPayload = new GetTrackExplainPayload();
		//this.sendData(trackExplainPayload);

		var trackAlbumPayload = new GetTrackAlbumPayload();
		this.sendData(trackAlbumPayload);

		var trackInfoPayload = new GetTrackInfoPayload();
		this.sendData(trackInfoPayload);

		var trackAlbumArtPayload = new GetTrackAlbumArtPayload();
		trackAlbumArtPayload.setMaxPayloadLength( PANDORA.CONFIG.maxImagePayloadSize );
		this.sendData(trackAlbumArtPayload);
	},

	getExtendTrackInfo : function(){
		//PNDR_GET_TRACK_INFO_EXTENDED
		var requestExtendedInfoPayload = new GetTrackInfoExtendedPayload();
		this.sendData(requestExtendedInfoPayload);

		// Request for the album art
		var trackAlbumArtPayload = new GetTrackAlbumArtPayload();
		trackAlbumArtPayload.setMaxPayloadLength( PANDORA.CONFIG.maxImagePayloadSize );
		this.sendData(trackAlbumArtPayload);
	},

	deleteStation : function(token){
		//PNDR_EVENT_STATION_DELETE
		var deleteStationPayload = new EventStationDeletePayload();
		deleteStationPayload.setStationToken(token);
		console.log("delete Station : " + token);
		this.sendData(deleteStationPayload);
	},

	sendCommand : function(data){
		var requestPayload = null;
		switch(data.toUpperCase()){
			case "STATUS":
				//PNDR_GET_STATUS
				requestPayload = new GetStatusPayload();
			break;
			case "PLAY":
				//PNDR_EVENT_TRACK_PLAY
				requestPayload = new EventTrackPlayPayload();
			break;
			case "PAUSE":
				//PNDR_EVENT_TRACK_PAUSE
				requestPayload = new EventTrackPausePayload();
			break;
			case "SKIP":
				//PNDR_EVENT_TRACK_SKIP
				requestPayload = new EventTrackSkipPayload();
			break;
			case "THUMBSUP":  //thumbsUp
				//PNDR_EVENT_TRACK_RATE_POSITIVE
				requestPayload = new EventTrackRatePositivePayload();
			break;
			case "THUMBSDOWN":  //thumbsDown
				//PNDR_EVENT_TRACK_RATE_NEGATIVE
				requestPayload = new EventTrackRateNegativePayload();
			break;
			case "CREATEBYTRACK":  //createByTrack
				//PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK
				requestPayload = new EventStationCreateFromCurrentTrackPayload();
			break;
			case "CREATEBYARTIST":  //createByArtist
				//PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST
				requestPayload = new EventStationCreateFromCurrentArtistPayload();
			break;
			case "SORTBYDATE":  //sortByDate
				//PNDR_EVENT_STATIONS_SORT
				requestPayload = new EventStationsSortPayload();
				requestPayload.setSortOrder(resources.sort.PNDR_SORT_BY_DATE);
			break;
			case "SORTBYNAME":  //sortByName
				//PNDR_EVENT_STATIONS_SORT
				requestPayload = new EventStationsSortPayload();
				requestPayload.setSortOrder(resources.sort.PNDR_SORT_BY_NAME);
			break;
			case "BOOKMARKBYTRACK":  //bookmarkByTrack
				//PNDR_EVENT_TRACK_BOOKMARK_TRACK
				requestPayload = new EventTrackBookmarkTrackPayload();
			break;
			case "BOOKMARKBYARTIST":  //bookmarkByArtist
				//PNDR_EVENT_TRACK_BOOKMARK_ARTIST
				requestPayload = new EventTrackBookmarkArtistPayload();
			break;
		}
		console.log("========sendCommand ... " + data);
		this.sendData(requestPayload);
	},
	/*
	byte2HexArray : function(array){
		var hexStrings = [];
	    $(array).each(function(index, item) {
	    	var hexString = parseInt(item).toString(16);
	     	if (hexString.length < 2) {
	      		hexString = "0" + hexString;
	     	}
	     	hexStrings.push(hexString.toUpperCase());
	    });
	    return hexStrings;
	},
	hexArray2Byte : function(array){
		var bytes = [];
		for (var i=0; i<array.length; i++){
			bytes.push(parseInt(array[i].trim(), 16));
		}
		return bytes;
	},
	*/
	byte2HexString : function(array){
		var hexStrings = "";
	    $(array).each(function(index, item) {
	    	var hexString = parseInt(item).toString(16);
	     	if (hexString.length < 2) {
	      		hexString = "0" + hexString;
	     	}
	     	hexStrings = hexStrings + hexString.toUpperCase() + " ";
	    });
	    return hexStrings.trim();
	},
	sendData : function(payload){
		var frameData = FrameHandler.frameBytesForPayload(payload);
		var requestData = this.byte2HexString(frameData);

        log("=======SENDING DATA TO HAP hex: "+ requestData);
        var data = AQ.util.hexToBase64(requestData);
        log("=======SENDING DATA TO HAP base64: "+ data);
        AQ.app.sendAppRequest({
            appName: "Pandora",
            contentType:"application/octet-stream",
            contentTransferEncoding:"base64",
            content: data
        });
        return;
    }
};
//end of Pandora Model