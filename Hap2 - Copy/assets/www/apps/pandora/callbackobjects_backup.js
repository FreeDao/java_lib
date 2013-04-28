/**
 * 
 */
PANDORA.CallbackObj = {
	onSessionStart : function(payload){
		console.log("onSessionStart was called...\n" + JSON.stringify(payload));
	},
	//
	onGetStatus : function(payload){
		//PNDR_GET_STATUS
		console.log("onGetStatus was called...\n" + JSON.stringify(payload));
	},
	onReturnStatus : function(payload){
		//PNDR_RETURN_STATUS
		console.log("onReturnStatus was called...\n" + JSON.stringify(payload));
	},
	onUpdateStatus : function(payload){
		//PNDR_UPDATE_STATUS
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateStatusPayload(payload.toByte());
		console.log("Current status is : " + data.getStatus());
		var code = data.getCode();
		switch(data.getCode()){
			case resources.status.PNDR_STATUS_UNKNOWN_ERROR:
			case resources.status.PNDR_STATUS_LICENSING_RESTRICTIONS:
			case resources.status.PNDR_STATUS_INSUFFICIENT_CONNECTIVITY:
				//TODO : Show an error windows
				console.log("There is an error with status : " + status);
			break;
			case resources.status.PNDR_STATUS_INVALID_LOGIN:
				//TODO : Show an error windows, exit, onSessionTerminate
				console.log("Please login and try again later : " + status);
				APP.displayInvalidLoginNotification();
			break;
			case resources.status.PNDR_STATUS_PLAYING:
				PANDORA.Cache.setPlayStatus(true);
				console.log("Show Now playing page ...: " + status);
				PANDORA.Model.getTrackInfo();
			break;
			case resources.status.PNDR_STATUS_PAUSED:
				PANDORA.Cache.setPlayStatus(false);
				console.log("Show Now playing page ...: " + status);
				PANDORA.Model.getTrackInfo();
			break;
			case resources.status.PNDR_STATUS_NO_STATION_ACTIVE:
				console.log("Get station list ...: " + status);
				PANDORA.Model.getStationTokens();
			break;
			default :
				console.log("default --- Status is : " +  status);
			break;			
		}
	},
	onSessionTerminate : function(payload){
		console.log("onSessionTerminate was called...\n" + JSON.stringify(payload));
	},
	//
	onEventOpenApp : function(payload){
		console.log("onEventOpenApp was called...\n" + JSON.stringify(payload));
	},
	//
	onGetBrandingImage : function(payload){
		console.log("onGetBrandingImage was called...\n" + JSON.stringify(payload));
	},
	onUpdateBrandingImage : function(payload){
		console.log("onUpdateBrandingImage was called...\n" + JSON.stringify(payload));
	},
	onReturnBrandingImageSegment : function(payload){
		console.log("onReturnBrandingImageSegment was called...\n" + JSON.stringify(payload));
	},
	//
	onUpdateNotice : function(payload){
		//PNDR_UPDATE_NOTICE
		console.log("onUpdateNotice was called...\n" + JSON.stringify(payload));
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateNoticePayload(payload.toByte());
		var code = data.getCode();
		var notice = data.getNotice();
		console.log("Code :" + code + "; Notice : " + notice);//displayNoStationNotification
		switch (notice){
			case "PNDR_NOTICE_ERROR_SESSION_ALREADY_STARTED":
				PANDORA.Model.getTrackInfo();
			break;
			case "PNDR_NOTICE_SKIP_LIMIT_REACHED":
			case "PNDR_NOTICE_STATION_LIMIT_REACHED":
				APP.displaySkipLimitNotification();
			break;
			case "PNDR_NOTICE_ERROR_TRACK_RATING":
			case "PNDR_NOTICE_ERROR_STATION_DELETE":
			case "PNDR_NOTICE_ERROR_SEARCH_EXTENDED":
			case "PNDR_NOTICE_ERROR_SEARCH_SELECT":
			case "PNDR_NOTICE_ERROR_BOOKMARK":
			case "PNDR_NOTICE_ERROR_MAINTENANCE":
			case "PNDR_NOTICE_ERROR_TRACK_EXPLAIN":
			case "PNDR_NOTICE_ERROR_NO_ACTIVE_SESSION":
			case "PNDR_NOTICE_ERROR_APP_URL_NOT_SUPPORTED":
			case "PNDR_NOTICE_ERROR_STATION_DOES_NOT_EXIST":
			break;
		}
	},
	//
	onGetListener : function(payload){
		console.log("onGetListener was called...\n" + JSON.stringify(payload));
	},
	onReturnListener : function(payload){
		console.log("onReturnListener was called...\n" + JSON.stringify(payload));
	},
	/** Station */
	//
	onGetStationsOrder : function(payload){
		console.log("onGetStationsOrder was called...\n" + JSON.stringify(payload));
	},
	onReturnStationsOrder : function(payload){
		//PNDR_RETURN_STATIONS_ORDER
		console.log("ReturnStationsOrderPayload was called...\n" + JSON.stringify(payload));
		var data = new ReturnStationsOrderPayload(payload.toByte());
		var sortOrder = data.getOrder();
		//console.log("=====" + sortOrder);
		//PANDORA.Display.sortOptions();
	},
	onUpdateStationsOrder : function(payload){
		//PNDR_EVENT_STATIONS_SORT
		console.log("onUpdateStationsOrder was called...\n" + payload);
		var data = new UpdateStationsOrderPayload(payload.toByte());
		//var sortOrder = data.getOrder();
		PANDORA.Model.getStationTokens();
	},
	//
	onEventStationsSort : function(payload){
		//PNDR_EVENT_STATIONS_SORT
		console.log("onEventStationsSort was called...\n" + JSON.stringify(payload));
		var data = new EventStationsSortPayload(payload.toByte());
		var sortOrder = data.getSortOrder();
		var sortOrderStr = data.getSortOrderString();
		console.log("sortOrder:" + sortOrder + "; sortOrderStr :" + sortOrderStr);
		
	},
	//
	onEventStationSelect : function(payload){
		console.log("onEventStationSelect was called...\n" + JSON.stringify(payload));
	},
	//
	onEventStationDelete : function(payload){
		console.log("onEventStationDelete was called...\n" + JSON.stringify(payload));
	},
	onUpdateStationDeleted : function(payload){
		console.log("onUpdateStationDeleted was called...\n" + JSON.stringify(payload));
		//TODO : refresh the station list
		//APP.back();
	},
	//
	onEventStationCreateFromCurrentArtist : function(payload){
		console.log("onEventStationCreateFromCurrentArtist was called...\n" + JSON.stringify(payload));
	},
	onEventStationCreateFromCurrentTrack : function(payload){
		//PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK
		console.log("onEventStationCreateFromCurrentTrack was called...\n" + JSON.stringify(payload));
		//EventStationCreateFromCurrentTrackPayload
	},
	//
	onGetStationActive : function(payload){
		//PNDR_GET_STATION_ACTIVE
		console.log("onGetStationActive was called...\n" + JSON.stringify(payload));
	},
	onReturnStationActive : function(payload){
		//PNDR_GET_STATION_ACTIVE
		console.log("onReturnStationActive was called...\n" + JSON.stringify(payload));
		/*
		{"StationToken":1060,"Command":177,"CommandName":"PNDR_RETURN_STATION_ACTIVE","TrackToken":null}
		*/
	},
	onUpdateStationActive : function(payload){
		console.log("onUpdateStationActive was called...\n" + JSON.stringify(payload));
		var data = new UpdateStationActivePayload(payload.toByte());
		var stationToken = data.getStationToken();//1060
		console.log("onUpdateStationActive , station token : " + JSON.stringify(stationToken));
		//TODO : Get Track Info, If current screen is "Player", update screen.
	},
	//
	onGetStationCount : function(payload){
		console.log("onGetStationCount was called...\n" + JSON.stringify(payload));
	},
	onReturnStationCount : function(payload){
		console.log("onReturnStationCount was called...\n" + JSON.stringify(payload));
	},
	//
	onGetStationTokens : function(payload){
		console.log("onGetStationTokens was called...\n" + JSON.stringify(payload));
	},
	onReturnStationTokens : function(payload){
		console.log("onReturnStationTokens was called...toJSON : " + JSON.stringify(payload));
		
		var data = new ReturnStationTokensPayload(payload.toByte());
		var _tokens = data.getStationTokens();
		PANDORA.Model.getStationList(_tokens);
	},
	onGetAllStationTokens : function(payload){
		console.log("onGetAllStationTokens was called...\n" + payload);
		var data = new GetAllStationTokensPayload(payload);
	},
	//
	onGetStationInfo : function(payload){
		console.log("onGetStationInfo was called...\n" + JSON.stringify(payload));
	},
	onReturnStationInfo : function(payload){
		console.log("onReturnStationInfo was called...\n" + JSON.stringify(payload));
		var data = Payload.factory(payload.toByte());
		//var data = new ReturnStationInfoPayload(payload.toByte());
		var stations = data.getStationInfoList();
		PANDORA.Cache.setStations(stations);
	},
	//
	onGetStationArt : function(payload){
		console.log("onGetStationArt was called...\n" + JSON.stringify(payload));
	},
	onEventCancelStationArt : function(payload){
		console.log("onEventCancelStationArt was called...\n" + JSON.stringify(payload));
	},
	onReturnStationArtSegment : function(payload){
		//console.log("onReturnStationArtSegment was called...\n" + JSON.stringify(payload));
		var data = Payload.factory(payload.toByte());
		//var data = new ReturnStationArtSegmentPayload(payload.toByte());
		var token = data.getStationToken();
		var index = data.getSegmentIndex();
		var total = data.getTotalSegments();
		var data = data.getImageData();
		PANDORA.Cache.setStationArts(token,index,total,data);
		APP.notifyUpdateStationArt();
	},
	/** Track */
	onSetTrackElapsedPolling : function(payload){
		console.log("onSetTrackElapsedPolling was called...\n" + JSON.stringify(payload));
	},
	onEventTrackPlay : function(payload){
		console.log("onEventTrackPlay was called...\n" + JSON.stringify(payload));
	},
	onEventTrackPause : function(payload){
		console.log("onEventTrackPause was called...\n" + JSON.stringify(payload));
	},
	onEventTrackSkip : function(payload){
		console.log("onEventTrackSkip was called...\n" + JSON.stringify(payload));
	},
	onEventTrackRatePositive : function(payload){
		console.log("onEventTrackRatePositive was called...\n" + JSON.stringify(payload));
	},
	onEventTrackRateNegative : function(payload){
		console.log("onEventTrackRateNegative was called...\n" + JSON.stringify(payload));
	},
	onEventTrackExplain : function(payload){
		console.log("onEventTrackExplain was called...\n" + JSON.stringify(payload));
	},
	onEventTrackBookmarkTrack : function(payload){
		console.log("onEventTrackBookmarkTrack was called...\n" + JSON.stringify(payload));
	},
	onEventTrackBookmarkArtist : function(payload){
		console.log("onEventTrackBookmarkArtist was called...\n" + JSON.stringify(payload));
	},
	// Track begin
	onUpdateTrack : function(payload){
		//PNDR_UPDATE_TRACK
		console.log("onUpdateTrack was called...\n" + JSON.stringify(payload));
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateTrackPayload(payload.toByte());
		var trackToken = data.getTrackToken();
		console.log("trackToken : " + trackToken);//0
		
		//PANDORA.Cache.setPlayStatus(true);
		PANDORA.Model.getTrackInfo();
	},
	//
	onGetTrackInfo : function(payload){
		//PNDR_GET_TRACK_INFO
		console.log("onGetTrackInfo was called...\n" + JSON.stringify(payload));
	},
	onReturnTrackInfo : function(payload){
		//PNDR_GET_TRACK_INFO
		console.log("onReturnTrackInfo was called...\n" + JSON.stringify(payload));
		PANDORA.Cache.setTrackInfoExt(payload);
		//TODO : ...
		//APP.displayPlayer();
	},
	//
	onGetTrackTitle : function(payload){
		//PNDR_GET_TRACK_TITLE
		console.log("onGetTrackTitle was called...\n" + JSON.stringify(payload));
	},
	onReturnTrackTitle : function(payload){
		//PNDR_GET_TRACK_TITLE
		console.log("onReturnTrackTitle was called...\n" + JSON.stringify(payload));
		var data = Payload.factory(payload.toByte());
		//var data = new ReturnTrackTitlePayload(payload.toByte());
		PANDORA.Cache.setTrackName(data.getTrackName());
		//APP.displayPlayer();
	},
	//
	onGetTrackArtist : function(payload){
		//PNDR_GET_TRACK_ARTIST
		console.log("onGetTrackArtist was called...\n" + JSON.stringify(payload));

	},
	onReturnTrackArtist : function(payload){
		//PNDR_GET_TRACK_ARTIST
		var data = Payload.factory(payload.toByte());
		//var data = new ReturnTrackArtistPayload(payload.toByte());
		PANDORA.Cache.setArtistName(data.getArtistName());
		//APP.displayPlayer();
	},
	//
	onGetTrackAlbum : function(payload){
		//PNDR_GET_TRACK_ALBUM
		console.log("onGetTrackAlbum was called...\n" + JSON.stringify(payload));
	},
	onReturnTrackAlbum : function(payload){
		//PNDR_GET_TRACK_ALBUM
		var data = Payload.factory(payload.toByte());
		//var data = new ReturnTrackAlbumPayload(payload.toByte());
		PANDORA.Cache.setAlbumName(data.getAlbumName());
		//APP.displayPlayer();
	},
	//
	onGetTrackAlbumArt : function(payload){
		//PNDR_GET_TRACK_ALBUM_ART
		console.log("onGetTrackAlbumArt was called...\n" + JSON.stringify(payload));
	},
	onReturnTrackAlbumArtSegment : function(payload){
		//PNDR_GET_TRACK_ALBUM_ART
		var data = Payload.factory(payload.toByte());
		//var data = new ReturnTrackAlbumArtSegmentPayload(payload.toByte());
		var token = data.getTrackToken();
		var index = data.getSegmentIndex();
		var total = data.getTotalSegments();
		var imageData = data.getImageData();
		
		PANDORA.Cache.setAlbumCover(token,index,total,imageData);
		if(index==total-1){
			console.log("---- is player screen now ? " + PANDORA.Display._isPlayerScreen());
			if(PANDORA.Display._isPlayerScreen()){
				APP.displayPlayer();
			}
		}
	},
	//
	onGetTrackInfoExtended : function(payload){
		//PNDR_GET_TRACK_INFO_EXTENDED
		console.log("onGetTrackInfoExtended was called...\n" + JSON.stringify(payload));
	},
	onReturnTrackInfoExtended : function(payload){
		console.log("onReturnTrackInfoExtended was called...\n" + JSON.stringify(payload));
		APP.displayTrackInfo(payload);
	},
	//
	onGetTrackExplain : function(payload){
		//PNDR_GET_TRACK_EXPLAIN
		console.log("onGetTrackExplain was called...\n" + JSON.stringify(payload));
	},
	onReturnTrackExplainSegment : function(payload){
		console.log("onReturnTrackExplainSegment was called...\n" + JSON.stringify(payload));
		/*
		{"TrackToken":0,"SegmentIndex":0,"TotalSegments":1,"ExplainData":[],"Command":154,"CommandName":"PNDR_RETURN_TRACK_EXPLAIN_SEGMENT"}

		*/
		//TODO : Combine those data
	},
	//
	onUpdateTrackAlbumArt : function(payload){
		//PNDR_UPDATE_TRACK_ALBUM_ART
		//console.log("onUpdateTrackAlbumArt was called...\n" + JSON.stringify(payload));
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateTrackAlbumArtPayload(payload.toByte());
		var trackToken = data.getTrackToken();
		var imageLength = data.getImageLength();
		console.log("trackToken : " + trackToken + "; Image Length : " + imageLength);
	},
	onUpdateTrackExplain : function(payload){
		console.log("onUpdateTrackExplain was called...\n" + JSON.stringify(payload));
	},
	onUpdateTrackElapsed : function(payload){
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateTrackPayload(payload.toByte());
		var trackToken = data.getTrackToken();
		var timeElapsed = data.getTimeElapsed();
		console.log("trackToken : " + trackToken + "; timeElapsed" + timeElapsed);
		//TODO:
	},
	onUpdateTrackRating : function(payload){
		//PNDR_UPDATE_TRACK_RATING
		var data = Payload.factory(payload.toByte());
		var rate = data.getRating();
		PANDORA.Cache.setTrackRating(rate);
		APP.displayPlayer();
	},
	onUpdateTrackBookmarkTrack : function(payload){
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateTrackBookmarkTrackPayload(payload.toByte());
		var isBookmarked = data.getIsBookmarked(); //"IsBookmarked":1
		APP.back();
	},
	onUpdateTrackBookmarkArtist : function(payload){
		var data = Payload.factory(payload.toByte());
		//var data = new UpdateTrackBookmarkArtistPayload(payload.toByte());
		var isBookmarked = data.getIsBookmarked(); //"IsBookmarked":1
		APP.back();
	},
	// Track completed
	onUpdateTrackCompleted : function(payload){
		//PNDR_EVENT_TRACK_RATE_NEGATIVE
		//PNDR_EVENT_TRACK_SKIP
		console.log("onUpdateTrackCompleted was called...\n" + JSON.stringify(payload));
		PANDORA.Cache.resetTrackInfo();
		PANDORA.Cache.setPlayStatus(true);
		APP.displayPlayer();
	}
	/** Genre Category */
	/*
	//
	onGetGenreCategoryCount : function(payload){
		console.log("onGetGenreCategoryCount was called...\n" + JSON.stringify(payload));
	},
	onReturnGenreCategoryCount : function(payload){
		console.log("onReturnGenreCategoryCount was called...\n" + JSON.stringify(payload));
	},
	//
	onGetGenreCategoryNames : function(payload){
		console.log("onGetGenreCategoryNames was called...\n" + JSON.stringify(payload));
	},
	onReturnGenreCategoryNames : function(payload){
		console.log("onReturnGenreCategoryNames was called...\n" + JSON.stringify(payload));
	},
	//
	onGetGenreCategoryStationCount : function(payload){
		console.log("onGetGenreCategoryStationCount was called...\n" + JSON.stringify(payload));
	},
	onReturnGenreCategoryStationCount : function(payload){
		console.log("onReturnGenreCategoryStationCount was called...\n" + JSON.stringify(payload));
	},
	//
	onGetAllGenreCategoryNames : function(payload){
		console.log("onGetAllGenreCategoryNames was called...\n" + JSON.stringify(payload));
	},*/
	/** Genre Station */
	/*
	onGetGenreStationNames : function(payload){
		console.log("onGetGenreStationNames was called...\n" + JSON.stringify(payload));
	},
	onReturnGenreStationNames : function(payload){
		console.log("onReturnGenreStationNames was called...\n" + JSON.stringify(payload));
	},
	//
	onGetGenreStationArt : function(payload){
		console.log("onGetGenreStationArt was called...\n" + JSON.stringify(payload));
	},
	onEventCancelGenreStationArt : function(payload){
		console.log("onEventCancelGenreStationArt was called...\n" + JSON.stringify(payload));
	},
	onReturnGenreStationArtSegment : function(payload){
		console.log("onReturnGenreStationArtSegment was called...\n" + JSON.stringify(payload));
	},
	//
	onEventSelectGenreStation : function(payload){
		console.log("onEventSelectGenreStation was called...\n" + JSON.stringify(payload));
	},
	*/
	/** Search */
	/*
	onEventSearchAutoComplete : function(payload){
		console.log("onEventSearchAutoComplete was called...\n" + JSON.stringify(payload));
	},
	onEventSearchExtended : function(payload){
		console.log("onEventSearchExtended was called...\n" + JSON.stringify(payload));
	},
	onEventSearchSelect : function(payload){
		console.log("onEventSearchSelect was called...\n" + JSON.stringify(payload));
	},
	onGetSearchResultInfo : function(payload){
		console.log("onGetSearchResultInfo was called...\n" + JSON.stringify(payload));
	},
	onReturnSearchResultInfo : function(payload){
		console.log("onReturnSearchResultInfo was called...\n" + JSON.stringify(payload));
	},
	onUpdateSearch : function(payload){
		console.log("onUpdateSearch was called...\n" + JSON.stringify(payload));
	}*/
};//end of callbacks