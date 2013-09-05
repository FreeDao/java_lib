/**
 * iHeartRadio Model
 */

iHR.Model = {
	/**
	* Send command
	*
	* {"command": "mainMenu" , "ihrcp": "0.5"}
	* {"command": "getRecentStations","ihrcp": "0.5"}
	* {"command": "getStationsNearby", "ihrcp": "0.5"}
	* {"command": "getFeaturedStations", "ihrcp": "0.5"}
	* {"command": "featuredStationsGenresMenu", "ihrcp": "0.5"}
	* {"command": "liveStationsMenu", "ihrcp": "0.5"}
	* {"command": "getMyStationsList", "ihrcp": "0.5"}
	* {"command": "getMyStationsList", "params": {"sortOrder": "recency"}, "ihrcp": "0.5"}
	* {"command": "getMyStationsList", "params": {"sortOrder": "name"}, "ihrcp": "0.5"}
	* {"command": "getFeaturedArtistsByGenre", "params" : { "artistCategoryId": "56" }, "ihrcp": "0.5"}
	* {"command": "liveStationsMusicGenresMenu", "ihrcp": "0.5"}
	* {"command": "liveStationsTalkGenresMenu", "ihrcp": "0.5"}
	* {"command": "getLiveStationsByGenre", "params":{ "genreId":"9" } }
	* {"command": "liveStationsStatesMenu", "ihrcp": "0.5"}
	* {"command": "liveStationsCitiesMenu", "params":{ "stateId":"2" }
	* {"command": "getLiveStationsByCity", "params":{ "cityId":"3" }
	* {"command": "getPlayerState", "ihrcp": "0.5"}
	*/
	 //createStationMenu
	actionCommandRequest : function(comd,para) {
		console.log('----Send command:' + comd);
		var command  =null;
		if(para!=null){
			var command ={"command": comd, "params" : para, "ihrcp": "0.5"};
		}else{
			var command ={"command": comd, "ihrcp": "0.5"};
		}
		return this.sendRequest(command);
	},

	getImageRequest : function(opts) {
		//"command": "getImage", "params": { "type": "custom", "id": "428", "subType": "mood" }
		var command = { "command" : "getImage", "params" : opts };
		return this.sendRequest(command);
	},

	/* TODO :  Get Discovery Menu
	getDiscoveryMenu : function(opts) {
		var command = { "command" : "discoveryMenu" };
	},
	*/

    sendRequest : function(command){
    	var data = {
            appName: "com.clearchannel.iHeartRadio",
            contentType:"application/json",
            contentTransferEncoding:"application/json",
            content: JSON.stringify(command)
        };

        var seqNum = AQ.app.sendAppRequest(data);

        APP.seqNumWrapper[seqNum] = data;
		return seqNum;
    }
};//end of iHeartRadio Model

console.log("Finished load model.js");