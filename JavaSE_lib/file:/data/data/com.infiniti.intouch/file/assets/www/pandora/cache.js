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
			image : []
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
					var base64Str = AQ.util.hexToBase64(_imageDataHex);
					this.stations[i].stationIcon = base64Str;//AQ.util.hexToBase64(_imageData); //stationArt.encodedImages = BinaryUtils.binaryToBase64(images);
				}
			}*/
			return this.stations;
		}else{
			return null;
		}
	},

	setStationArts : function(token,index,total,data)
	{

		var existToken = false;

		for(var i=0;i<this.stationArts.length;i++)
		{
			if(this.stationArts[i].StationToken == token)
			{
				existToken = true;
				//this.stationArts[i].SegmentIndex = index;
				//this.stationArts[i].ImageData.push(data);
				this.stationArts[i].ImageData[index] = data;

				if(this.stationArts[i].ImageData.length == total)
				{
					var imageHolder = [];

					for(var j in this.stationArts[i].ImageData)
					{
						imageHolder = imageHolder.concat( this.stationArts[i].ImageData[j] );
					}

					for(var s = 0; s<this.stations.length;s++)
					{
						if(this.stations[s].stationToken == token)
						{
							// this.stations[s].stationIcon = AQ.util.hexToBase64( imageHolder );
							this.stations[s].stationIcon = binaryToBase64( imageHolder );

							// delete the station art segments to clear up the memory
							// delete this.stationArts[i];
						}
					}
				}
				break;
			}
		}

		if(!existToken)
		{
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
		}else if( this.trackInfo.albumCover.token > token )
		{
			// if the token is smaller than the current token,
			// we assume that it's the old image, and ignore its segments
			return;
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
			var imageHolder = [];

			for(var i in this.trackInfo.albumCover.imageData)
			{
				imageHolder = imageHolder.concat( this.trackInfo.albumCover.imageData[i] );
			}

			// this.trackInfo.albumCover.image = AQ.util.hexToBase64( this.trackInfo.albumCover.image );
			this.trackInfo.albumCover.image = binaryToBase64( imageHolder );


			// delete all image segment related data to clear up memory
			delete this.trackInfo.albumCover.imageData;
			delete this.trackInfo.albumCover.token;
			delete this.trackInfo.albumCover.total;

			this.trackInfo.albumCover.token = "";
			this.trackInfo.albumCover.total = "";
			this.trackInfo.albumCover.imageData = {};

			console.log("----- IMAGE "+token+" IS READY -----");

			// When the image is ready and the user is in the player page, update the screen
			if( PANDORA.Display._isPlayerScreen() === true )
			{
				APP.displayPlayer();
			}
		}

		return;
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