/**
 * Pandora Display
 *
 * Holds all functions to convert the given data to the template format JSON
 */
PANDORA.Display = {
	defaults : {
		historyWrapper : []
	},

	/**
	 * Station List in Main Menu
	 */
	stationList : function(data) {
		this._historyReset();
		this._historySave({ page : 'stationList', value : data });

		var template_data = $.extend(true, {}, PANDORA.STATE.main_station_list);
		for (var i in data) {
			//TODO : if current station is playing, mark it up...
			var playing_icon = PANDORA.IMAGES.list.nowplaying;
			template_data.template_content.list.push({
				"image1" : data[i].stationIcon,
				//"image2" : playing_icon,
				"text" : data[i].stationName,
				"action" : "selectStation",
				"value" : data[i].stationToken
			});
		}
		AQ.app.updateScreen(template_data);
	},
	nowPlaying : function(data) {
		this._historySave({ page : 'nowPlaying', value : data });
		var template_data = $.extend(true, {}, PANDORA.STATE.now_playing);
		if (data.isPlaying) {
			template_data.template_content.buttons["1"] = {
				"image" : PANDORA.IMAGES.button['1_pause'],
				"action" : "pause"
			};
		} else {
			template_data.template_content.buttons["1"] = {
				"image" : PANDORA.IMAGES.button['1_play'],
				"action" : "play"
			};
		}
		template_data.template_content.buttons["2"] = {
			"image" : (data.trackRating==1) ? PANDORA.IMAGES.button['2_liked'] : PANDORA.IMAGES.button['2_like'],
			"action" : (data.trackRating==1)  ? "" : "thumbsUp"
		};
		template_data.template_content.buttons["3"] = {
			"image" : (data.trackRating==2) ? PANDORA.IMAGES.button['3_disliked'] : PANDORA.IMAGES.button['3_dislike'],
			"action" : (data.trackRating==2)  ? "" : "thumbsDown"
		};

		try{
			console.log( "data.artistName - " + data.artistName );
			console.log( "data.trackName - " + data.trackName );
			console.log( "data.albumName - " + data.albumName );
			console.log( "data.albumCover.image.length - " + data.albumCover.image.length );
		}catch(e){}

		template_data.template_content.text_left_top = data.artistName.toString();
		template_data.template_content.text_right_main = data.trackName.toString() +"\r\n"+ data.albumName.toString();

		var albumCoverData = PANDORA.IMAGES.img.albumCover;
		if(typeof data.albumCover.image != "undefined" && data.albumCover.image.length > 0)
		{
			albumCoverData = data.albumCover.image;
		}

		template_data.template_content.image_left = albumCoverData;

		AQ.app.updateScreen(template_data);
		console.log("--- nowPlaying ---");
	},
	trackInfo : function(data) {
		this._historySave({ page : 'trackInfo', value : data });
		var template_data = $.extend(true, {}, PANDORA.STATE.track_info);
		template_data.template_content.text_left_top = data.artistName.toString();
		//template_data.template_content.text_left_bottom = data.Duration + "s";
		template_data.template_content.text_right_main = data.TrackName.toString() +"\r\n"+ data.albumName.toString();

		var albumCoverData = PANDORA.IMAGES.img.albumCover;
		if(data.albumCover.imageData!= null){
			albumCoverData = data.albumCover.imageData;
		}
		template_data.template_content.image_left = albumCoverData;

		AQ.app.updateScreen(template_data);
	},
	createStationSubMenu : function(data) {
		this._historySave({ page : 'createStationSubMenu', value : data });
		AQ.app.updateScreen(PANDORA.STATE.new_station_submenu);
	},
	sortOptions : function(data) {
		this._historySave({ page : 'sortOptions' });
		AQ.app.updateScreen(PANDORA.STATE.sort_submenu);
	},
	playerSubmenu : function() {
		this._historySave({ page : 'playerSubmenu' });
		AQ.app.updateScreen(PANDORA.STATE.player_submenu);
	},
	detelableStationList : function(data) {
		this._historySave({ page : 'detelableStationList', value : data });
		var template_data = $.extend(true, {}, PANDORA.STATE.detelable_station_list);
		for (var i in data) {
			template_data.template_content.list.push({
				//"image1" : data[i].icon,
				"text" : data[i].stationName,
				"action" : "displayDeleteConfirmation",
				"value" : JSON.stringify({"token" : data[i].stationToken, "name":data[i].stationName})
			});
		}
		AQ.app.updateScreen(template_data);
	},
	poupConfirm : function(data) {
		this._historySave({page : 'poupConfirm'});
		var template_data = $.extend(true, {}, PANDORA.STATE.poup_windows);
		template_data.template_content.text_main = data.msg;
		template_data.template_content.text_bottom = {
			"1" :{
				"text" : $.t("yes"),
				"action" : data.action,
				"value" : data.paratemer
			},
			"3" : {
				"text" : $.t("no"),
				"action" : "back"
			}
		};
		AQ.app.updateScreen(template_data);
	},
	poupNotification : function(data) {
		this._historySave({page : 'poupNotification'});
		var template_data = $.extend(true, {}, PANDORA.STATE.poup_windows);
		//template_data.template_content.active_key = 2;
		template_data.template_content.text_main = data;
		template_data.template_content.text_bottom = {
			"2" : {
				"text" : $.t("ok"),
				"action" : "back"
			}
		};
		AQ.app.updateScreen(template_data);
	},
	poupWindows : function(data) {
		this._historySave({page : 'poupWindows'});
		var template_data = $.extend(true, {}, PANDORA.STATE.poup_windows);
		//template_data.template_content.active_key = 2;
		template_data.template_content.text_main = data;
		AQ.app.updateScreen(template_data);
	},
	poupError: function(data){
		this._historySave({page : 'poupError'});
        var template_data = $.extend(true,{}, PANDORA.STATE.poup_windows);
        var defaults = {
            "text_main" : $.t("error_Notification"),
            "text_bottom" : {
                "2" : {
                    "text" : $.t("ok"),
                    "action" : "back"
                }
            }
        }
        template_data.template_content = $.extend(true, defaults, data);

        AQ.app.updateScreen(template_data);
    },

    showLoading: function(){
        AQ.app.showLoading();
    },

	_isPlayerScreen : function(templateData) {
		var result = false;
		var pagesNum = PANDORA.Display.defaults.historyWrapper.length;
		if(pagesNum>0){
			result = PANDORA.Display.defaults.historyWrapper[pagesNum - 1].page == "nowPlaying";
		}
		return result;
	},

	/**
	 * Save the page in the history
	 */
	_historySave : function(history) {
		//If the previous page was the temp history page, remove it from the history and relaunch the function
		if (PANDORA.Display.defaults.historyWrapper.length > 1 && PANDORA.Display.defaults.historyWrapper[PANDORA.Display.defaults.historyWrapper.length - 1].isTemp === true) {
			PANDORA.Display.defaults.historyWrapper.pop();
			PANDORA.Display._historySave(history);
			return;
		}

		if (PANDORA.Display.defaults.historyWrapper.length == 0) {
			PANDORA.Display.defaults.historyWrapper.push(history);
		} else if (PANDORA.Display.defaults.historyWrapper[PANDORA.Display.defaults.historyWrapper.length - 1].page != history.page) {
			PANDORA.Display.defaults.historyWrapper.push(history);
		} else {
			console.log("Same history. Overwriting the data.");
			PANDORA.Display.defaults.historyWrapper[PANDORA.Display.defaults.historyWrapper.length - 1] = history;
		}
		return;
	},

	/**
	 * Reset the history
	 */
	_historyReset : function() {
		PANDORA.Display.defaults.historyWrapper = [];
	},

	/**
	 * Removes the number of history items starting with the newest
	 */
	_historyRemove : function(num) {
		num = num || 1;

		for (var i = 0; i < num; i++) {
			PANDORA.Display.defaults.historyWrapper.pop();
		}
	},

	/**
	 * Move back in the history and load the previous page
	 */
	historyBack : function() {
		//Remove the current page from the history array
		PANDORA.Display.defaults.historyWrapper.pop();

		console.warn("History:", PANDORA.Display.defaults.historyWrapper);

		if (PANDORA.Display.defaults.historyWrapper.length < 1) {
			APP.AQ_APP_EXIT();
			AQ.app.load("home");
			return;
		}

		var history = PANDORA.Display.defaults.historyWrapper.pop();
		console.log("Going back to", history);
        PANDORA.Display[ history.page ]( history.value );
	}
};
