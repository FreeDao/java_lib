iHR.Cache = {
    defaults: {
		markedUpTrackId:"",
        cache:{
			myStationList : [],
            playingState : {
				"image" : null,
				"stationInfo" : null,
				"trackInfo" : null,
				"isPlayingSong" : false,
				"isliked" : 0,
				"isScanAvailable" : false
			}
        }
    },
	
	markedUpCurrentTrack: function(){
		this.defaults.markedUpTrackId = this.defaults.cache.playingState.trackInfo.id;
        return;
    },
	isMarkedUpTrack: function(){
		return this.defaults.markedUpTrackId == this.defaults.cache.playingState.trackInfo.id;
    },
    getMyStationList : function(){
        return this.defaults.cache.myStationList;
    },
    setMyStationList : function(data){
        this.defaults.cache.myStationList = data;
        return;
    },
	
    getPlayingState : function(){
        return this.defaults.cache.playingState;
    },
	getStationId: function(){
        return this.defaults.cache.playingState.stationInfo.id;
    },
	
	updateStationInfo : function(data){
        this.defaults.cache.playingState.stationInfo = null;
        this.defaults.cache.playingState.stationInfo = data;
        return;
	},
	updateTrackInfo : function(data){
		this.defaults.cache.playingState.image = null;
		this.defaults.cache.playingState.isliked = 0;
		this.defaults.cache.playingState.trackInfo = null;
        this.defaults.cache.playingState.trackInfo = data;
        return;
	},
	updatePlayStatus : function(data){
        this.defaults.cache.playingState.isPlayingSong = data;
        return;
	},	
	updateThumbsSong : function(data){
		/*
			0: default;
			1: liked;
			2: disliked;
		*/
        this.defaults.cache.playingState.isliked = data;
        return;
	},	
	
	updateScanStatus : function(data){
        this.defaults.cache.playingState.isScanAvailable = data;
        return;
	},
	updateImg : function(data){
        this.defaults.cache.playingState.image = null;
        this.defaults.cache.playingState.image = data;
	},
	resetStationInfo : function(){
	    this.defaults.cache.playingState = {
			"image" : null,
			"stationInfo" : null,
			"trackInfo" : null,
			"isPlayingSong" : false,
			"isliked" : 0,
			"isScanAvailable" : false
		}
		return;
	}
};

console.log("Finished load cache.js");