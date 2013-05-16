/**
 * Holds the images for the template app
 */
 iHR.IMAGES = {
	'header' : "file:///i_heart_radio/images/header.png",
    'button':{
		'1_play' : "aq:///images/1-play.png",
		'1_pause' : "aq:///images/1-pause.png",
		'1_sort' : "file:///i_heart_radio/images/1-sort.png",
		'2_delete':"file:///i_heart_radio/images/2-delete.png",
		'2_scan_enabled' : "file:///i_heart_radio/images/2-scan.png",
		'2_scan_disabled' : "",
		'2_skip' : "file:///i_heart_radio/images/2-skip.png",
		'4_like' : "aq:///images/4-thumbs-up.png",
		'4_liked' : "aq:///images/4-check.png",
		'5_dislike' : "aq:///images/5-thumbs-down.png",
		'5_disliked' : "aq:///images/5-check.png",
		'3_newstation' : "file:///i_heart_radio/images/3-newstation.png",
		'4_newstation' : "file:///i_heart_radio/images/4-newstation.png",
		'4_save' : "file:///i_heart_radio/images/4-save.png",
		'5_stations' : "file:///i_heart_radio/images/5-stations.png",
		'5_setting' : "file:///i_heart_radio/images/5-settings.png",
		'6_setting' : "file:///i_heart_radio/images/6-settings.png",
		'6_stations' : "file:///i_heart_radio/images/6-stations.png"
  },
  'album_cover' : "file:///i_heart_radio/images/default-albumCover.png"
};

/**
 * Holds the available states/pages for this app
 */
 iHR.STATE = {
    /**
     * Main menu
     * Template c
     */
     "main" : {
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
           "list" : [{
                "text" : $.t("now_Playing"),
                "action" : "requestCallbackDispture",
                "value" : JSON.stringify({"name": "now_playing", "actionCommand" : {"command":"getPlayerState"}})
            }]
        }
    },
	
    /**
     * My Station List
     * Template C
     */
     "my_station_list" : {
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {
                "1" : {
                    "image" : iHR.IMAGES.button['1_sort'],
                    "action" : "displayMyStationSubmenu"
                },
                "2" : {
                    "image" : iHR.IMAGES.button['2_delete'],
                    "action" : "deleteConfirmation",
					"value" : "aq_active_list_item"
                }
			},
            "list" : []
        }
    },
	
    /**
     * My Station Submenu
     * Template c 
     */
     "my_station_submenu" : {
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
            "list" : [{
                "text" : $.t("by_Name"),
                "action" : "requestCallbackDispture",
                "value" : JSON.stringify({"name": "my_stations", "actionCommand" : {"command":"getMyStationsList","params":{ "sortOrder":"name" }}})
            },{
                "text" : $.t("by_Last_Played"),
                "action" : "requestCallbackDispture",				
                "value" : JSON.stringify({"name": "my_stations", "actionCommand" : {"command":"getMyStationsList","params":{ "sortOrder":"recency" }}})
            }]
        }
    },
    
    /**
     * Live Station Submenu
     * Template c
     */
     "station_submenus" : {
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },
    
    /**
     * Station List
     * Template C
     */
     "station_list" : {
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : { },
            "list" : []
        }
    },
    /**
     * Delete Station List
     * Template a, List pattern 1
     */
     "delete_station_list" : {
        //delete
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },
    
    /**
     * Live Station Player
     * Template b 
     */
     "live_station_player" : {
        "template_id" : "b",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {
                "1" : {},
                "2" : {},
                "3" : {
                    "image" : iHR.IMAGES.button['3_newstation'],
                    "action" : "asynchronousEventPostDispture",
					"value" : "NEW"
                },
                "4" : {
                    "image" : iHR.IMAGES.button['4_save'],
                    "action" : "asynchronousEventPostDispture",
					"value" : "SAVE"
                },
                "5" : {/*
                    "image" : iHR.IMAGES.button['5_stations'],
                    "action" : "requestCallbackDispture",
					"value" : JSON.stringify({"name": "my_stations_orderby", "actionCommand" : {"command":"getMyStationsList","params":{ "sortOrder":"name" }}})
                */},
                "6" : {}
            },
            "text_left_top" : "",
            "text_left_bottom" : "",
            "text_right_main" : "",
            "image_left" : ""
        }
    },
    /**
     * Custom Station Player
     * Template b 
     */
     "custom_station_player" : {
        "template_id" : "b",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {
                "1" : {},
                "2" : {
                    "image" : iHR.IMAGES.button['2_skip'],
                    "action" : "asynchronousEventPostDispture",
					"value" : "SKIP"
				},
                "3" : {
					"image" : iHR.IMAGES.button['3_newstation'],
                    "action" : "asynchronousEventPostDispture",
					"value" : "NEW"
				},
                "4" : {},
                "5" : {},
                "6" : {
					"image" : iHR.IMAGES.button['6_setting'],
                    "action" : "displayDiscoveryTuner"
				}
            },
            "text_left_top" : "",
            "text_left_bottom" : "",
            "text_right_main" : "",
            "image_left" : ""
        }
    },
    
    
    /**
     * Select Discovery Tuner
     * Template a, List pattern 1
     */
     "select_discovery_tuner" : {
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
            "list" : [{
                "text" : $.t("familar_Airtists"),
                "action" : "setVarietyLevel",				
                "value" : "1"
            },{
                "text" : $.t("mixed"),
                "action" : "setVarietyLevel",
                "value" : "2"
            },{
                "text" : $.t("more_Discovery"),
                "action" : "setVarietyLevel",
				"value" : "3"
            }]
        }
    },
    
    /**
     * Confirm Action
     * Template f 
     */
     "confirmaction" : {
        "template_id" : "f",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : { },
            "text_main" : "",
            "text_bottom" : {
                "1" : {},
                "3" : {}
            }
        }
    },
    
    /**
     * Info
     * Template f 
     */
     "notifaciation" : {
        "template_id" : "f",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
            "text_main" : "",
            "text_bottom" : {}
        }
    },

    "no_item_page":{
        "template_id" : "c",
        "template_content" : {
            "header" : iHR.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    }
};

console.log("Finished load constant.js");