PANDORA.Cache = {
	sessionConnected : false,
	stations : [],
	stationArts : [],
	currentStationToken : null,
	trackInfo : {
		isPlaying : false,
		trackToken : null,
		trackName : "",
		artistName : "",
		albumName : "",
		albumCover : {
			token:"",
			total:"",
			imageData:{},
			image : ""
		},
		trackRating : 0,
		trackDescription : "",
		trackInfoExt : {
			/*
			"TrackToken":1,
			"AlbumArtLength":9383,
			"Duration":295,
			"Elapsed":0,
			"Rating":null,
			"PermissionFlags":31,
			"PermissionStrings":"PNDR_TRACK_FLAG_ALLOW_RATING, PNDR_TRACK_FLAG_ALLOW_SKIP",
			"IdentityFlags":0,
			"IdentityStrings":"",
			"AllowSkip":2,
			"IsAudioAd":0,
			"Command":145,"CommandName":"PNDR_RETURN_TRACK_INFO"
			*/
		},
		appStatus : -1,
		userStationsCount : -1,
		activeStation : -1,
		searchResults : []
	},

	setCurrentStationToken : function(token) {
		this.currentStationToken = token;
	},
	getCurrentStationToken : function() {
		return this.currentStationToken;
	},
	setStations : function(stations) {
		this.stations = stations;
	},
	getStations : function() {
		if(this.stations.length > 0 && this.stations.length == this.stationArts.length){
			/*var lastStationArt = this.stationArts[this.stationArts.length-1];
			if(lastStationArt.SegmentIndex == lastStationArt.TotalSegments-1){
				//APP.displayStationList();
				/*for(var i = 0; i<this.stations.length;i++){
					var _imageData=[];
					var _imageDataHex = [];
					for(var j =0 ; j < this.stationArts[i].ImageData.length; j++){
						_imageData = _imageData.concat(this.stationArts[i].ImageData[j]);
					}//ArtLength
					for(var d=0; d<_imageData.length; d++){
						_imageDataHex.push(parseInt(_imageData[d]).toString(16));
					};
					var base64Str = AQ.app.hexToBase64(_imageDataHex);
					this.stations[i].stationIcon = base64Str;//AQ.app.hexToBase64(_imageData); //stationArt.encodedImages = BinaryUtils.binaryToBase64(images);
				}
			}*/
			return this.stations;
		}else{
			return null;
		}
	},

	//TODO: Needs optimization similar to setAlbumCover()
	setStationArts : function(token,index,total,data){
		var existToken = false;
		for(var i=0;i<this.stationArts.length;i++){
			if(this.stationArts[i].StationToken == token){
				existToken = true;
				//this.stationArts[i].SegmentIndex = index;
				//this.stationArts[i].ImageData.push(data);
				this.stationArts[i].ImageData[index] = data;
				if(this.stationArts[i].ImageData.length == total){
					var base64Str = "";
					for(var j in this.stationArts[i].ImageData){
						base64Str += this.stationArts[i].ImageData[j].join(" ");
					}
					for(var s = 0; s<this.stations.length;s++){
						if(this.stations[s].stationToken == token){
							this.stations[s].stationIcon = AQ.app.hexToBase64( base64Str );
						}
					}
				}
				break;
			}
		}
		if(!existToken){
			var artData = {"StationToken":token,"ImageData":[]};
			artData.ImageData.push(data);
			this.stationArts.push(artData);
		}
	},

	//TODO: Needs optimization similar to getAlbumCover()
	getStationArts : function(){
		return this.stationArts;
	},


	getTrackInfoExt : function() {
		return this.trackInfo.trackInfoExt;
	},
	setTrackInfoExt : function(data) {
		this.trackInfo.trackInfoExt = data;
		this.trackInfo.trackToken = data.TrackToken;
	},

	getTrackName : function() {
		return this.trackInfo.trackName;
	},
	setTrackName : function(data) {
		this.trackInfo.trackName = data;
	},

	getArtistName : function() {
		return this.trackInfo.artistName;
	},
	setArtistName : function(data) {
		this.trackInfo.artistName = data;
	},

	getAlbumName : function() {
		return this.trackInfo.albumName;
	},
	setAlbumName : function(data) {
		this.trackInfo.albumName = data;
	},

	getTrackRating : function() {
		return this.trackInfo.trackRating;
	},
	setTrackRating : function(data) {
		this.trackInfo.trackRating = data;
	},


	getAlbumCover : function() {

		return this.trackInfo.albumCover.image;
	},
	setAlbumCover : function(token,index,total,data) {
		if( this.trackInfo.albumCover.token == token )
		{
			this.trackInfo.albumCover.imageData[ index ] = data;
		}else
		{
			// Reset the image string before collecting the next image
			this.trackInfo.albumCover.image = "";

			this.trackInfo.albumCover.token = token;
			this.trackInfo.albumCover.total = total;
			this.trackInfo.albumCover.imageData = {};
			this.trackInfo.albumCover.imageData[ index ] = data;
		}

		var keys = [];
		for( var key in this.trackInfo.albumCover.imageData )
		{
			keys.push( key );
		}

		//console.log("COLLECTING IMAGE "+token+" ----- index: "+index+"; total: "+total+"; currently received: "+PANDORA.UTIL.getObjSize(this.trackInfo.albumCover.imageData) );
		console.log("COLLECTING IMAGE "+token+" ----- index: "+index+"; total: "+total+"; currently received: "+keys.toString() );

		if( PANDORA.UTIL.getObjSize(this.trackInfo.albumCover.imageData) == total )
		{
			for(var i in this.trackInfo.albumCover.imageData)
			{
				this.trackInfo.albumCover.image += this.trackInfo.albumCover.imageData[i].join(" ");
			}

			this.trackInfo.albumCover.image = AQ.app.hexToBase64( this.trackInfo.albumCover.image );

			delete this.trackInfo.albumCover.imageData;
			this.trackInfo.albumCover.imageData = {};

			console.log("----- IMAGE "+token+" IS READY -----");

			// When the image is ready and the user is in the player page, update the screen
			console.log("---- is player screen now ? " + PANDORA.Display._isPlayerScreen());
			if( PANDORA.Display._isPlayerScreen() === true )
			{
				APP.displayPlayer();
			}
		}

		return;

		// if(this.trackInfo.albumCover.token == "" && this.trackInfo.albumCover.token == token){
		// 	this.trackInfo.albumCover.index = index;
		// 	this.trackInfo.albumCover.total = total;
		// }else{
		// 	this.trackInfo.albumCover.imageData={};
		// }

		// for(var i=0; i< data.length; i++){
		// 	this.trackInfo.albumCover.imageData.push(parseInt(data[i]).toString(16));
		// }
	},

	getTrackImage : function() {
		return this.trackInfo.trackImage;
	},
	setTrackImage : function(data) {
		this.trackInfo.trackImage = data;
	},
	resetTrackInfo : function(){
		//TODO : clear the cache
		this.trackInfo.isPlaying = false;
		this.trackInfo.trackName = "";
		this.trackInfo.artistName = "";
		this.trackInfo.albumName = "";
		this.trackInfo.albumCover = {
			token:"",
			total:"",
			imageData:null
		};
		this.trackInfo.trackRating = 0;
	},
	setPlayStatus : function(status) {
		this.trackInfo.isPlaying = status;
	},
	getPlayStatus : function() {
		return this.trackInfo.isPlaying;
	},
	getTrackInfo : function() {
		return this.trackInfo;
	}
};