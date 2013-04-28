/**
 * Declare constants to be used in the Pandora app
 *
 * @author: skutfiddinov
 */

PANDORA.CONFIG = {
	albumArtDimension: 80, // used to be 200
	stationArtDimension: 32, // used to be 44
	maxImagePayloadSize : 20 * 1024, // 20KB
	imageType : 2, // PNDR_IMAGE_PNG
};

PANDORA.IMAGES = {
	'header' : "file:///pandora/images/header.png",
	'button' : {
		'1_play' : "aq:///images/1-play.png",
		'1_pause' : "aq:///images/1-pause.png",
		'1_nowplaying' : "file:///pandora/images/1-playingnow.png",
		'2_delete' : "file:///pandora/images/2-delete.png",
		'2_dislike': "aq:///images/2-thumbs-down.png",
		'2_disliked': "aq:///images/2-check.png",
		'3_like' : "aq:///images/3-thumbs-up.png",
		'3_liked' : "aq:///images/3-check.png",
		'4_new' : "file:///pandora/images/4-newstation.png",
		'5_menu' : "file:///pandora/images/5-menu.png",
		'6_stations' : "file:///pandora/images/6-stations.png"
	},
	'list' : {
		'sort_icon' : "file:///pandora/images/icon-sort.png",
		'quickMix_icon' : "file:///pandora/images/icon-shuffle.png",
		'nowplaying' : "file:///pandora/images/indicator-playing.png"
	},
	'img':{
		'albumCover' : "file:///pandora/images/default-albumCover.png"//
	}
}

/**
 * Holds the available states/pages for this app
 */
PANDORA.STATE = {
	/**
	 * Main Station List menu
	 * Template C
	 */
	"main_station_list" : {
		"template_id" : "a",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"list_pattern" : 2,
			"buttons" : {
				"1" : {
					"image" : PANDORA.IMAGES.button['1_nowplaying'],
					"action" : "displayPlayer"
				},
				"2" : {
					"image" : PANDORA.IMAGES.button["2_delete"],
					"action" : "displayDetelableStationList"
				}
			},
			"list" : [{
				"image1" : PANDORA.IMAGES.list.sort_icon,
				"text" : $.t("sort_by_Date_or_A-Z"),
				"action" : "displaySortOptionsMenu"
			}]
		}
	},

	/**
	 * Now Playing
	 */
	"now_playing" : {
		"template_id" : "b",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {
				"1" : { },
				/*"2" : {
					"image" : PANDORA.IMAGES.button['2_skip'],
					"action" : "skip"
				},*/
				"2" : {
					"image" : PANDORA.IMAGES.button['2_dislike'],
					"action" : "thumbsDown"
				},
				"3" : {
					"image" : PANDORA.IMAGES.button['3_like'],
					"action" : "thumbsUp"
				},
				"4" : {
					"image" : PANDORA.IMAGES.button["4_new"],
					"action" : "displayCreateStationSubMenu"
				},
				"5" : {
					"image" : PANDORA.IMAGES.button['5_menu'],
					"action" : "displayPlayerSubmenu"
				},
				"6" : {
					"image" : PANDORA.IMAGES.button['6_stations'],
					"action" : "displayStationList"
				}
			},
			"text_left_top" : "",
			"text_left_bottom" : "",
			"text_right_main" : "",
			"image_left" : ""
		}
	},

	/**
	 * Track Info
	 */
	"track_info" : {
		"template_id" : "b",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {},
			"text_left_top" : "",
			"text_left_bottom" : "",
			"text_right_main" : "",
			"image_left" : ""
		}
	},

	/**
	 * Detelable Station List
	 */
	"detelable_station_list" : {
		"template_id" : "a",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"list_pattern" : 2,
			"buttons" : {},
			"list" : []
		}
	},

	/**
	 * Sort Screen
	 */
	"sort_submenu" : {
		"template_id" : "c",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {},
			"list" : [{
				"text" : $.t("sort_Stations_by_Date"),
				"action" : "sortStationByDate"
			}, {
				"text" : $.t("sort_Stations_by_A-Z"),
				"action" : "sortStationByName"
			}]
		}
	},

	/**
	 * New Station
	 */
	"new_station_submenu" : {
		"template_id" : "c",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {},
			"list" : [{
				"text" : $.t("new_Station_from_Track"),
				"action" : "createStationByTrack"
			}, {
				"text" : $.t("new_Station_from_Artist"),
				"action" : "createStationByArtist"
			}]
		}
	},

	/**
	 * Sub Menu List
	 */
	"player_submenu" : {
		"template_id" : "c",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {},
			"list" : [{
				"text" : $.t("track_Information"),
				"action" : "loadTrackInfo"
			}, {
				"text" : $.t("bookmark_Track"),
				"action" : "bookMarkByTrack"
			}, {
				"text" : $.t("bookmark_Artist"),
				"action" : "bookMarkByArtist"
			}, {
				"text" : $.t("delete_Station"),
				"action" : "deleteCurrentStation"
			}]
		}
	},

	/**
	 * Poup Windows
	 */
	"poup_windows" : {
		"template_id" : "f",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {},
			"text_main" : "",
			"text_bottom" : {}
		}
	},

	/**
	 * Confirm Action
	 */
	"delete_confirmaction" : {
		"template_id" : "f",
		"template_content" : {
			"header" : PANDORA.IMAGES.header,
			"buttons" : {},
			"text_main" : "",
			"text_bottom" : {}
		}
	}
};