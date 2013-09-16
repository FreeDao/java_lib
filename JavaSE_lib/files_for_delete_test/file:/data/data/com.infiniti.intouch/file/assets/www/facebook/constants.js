/**
 * Declare constants to be used in the Facebook app
 *
 * @author: skutfiddinov
 */
/*
FB.DEFAULTS = {
    presetComments: [
        "Pre-written comment text 1",
        "Pre-written comment text 2",
        "Pre-written comment text 3",
        "Pre-written comment text 4",
        "Pre-written comment text 5"
    ],
    presetStatus: [
        "Testing the new technology",
        "I'm in the car",
        "Heading home now!",
        "Pre-written status1",
        "Pre-written status2"
    ]
}*/

FB.IMAGES = {
    'header' : "file:///facebook/images/header.png",
    'button' : {
        '1_refresh' : "aq:///images/1-refresh.png",
        '1_tts' : "aq:///images/1-tts.png",
        '1_tts-active' : "aq:///images/1-tts-active.png",

        '2_like' : "aq:///images/2-thumbs-up.png",
        '2_liked' : "aq:///images/2-check.png",
        '2_call' : "aq:///images/2-call.png",

        '3_add_comment' : "file:///facebook/images/3-addcomment.png",
        "3_prev" : "aq:///images/3-left.png",

        "4_next" : "aq:///images/4-right.png",
        '4_comments' : "file:///facebook/images/4-comments.png",

        '5_up' : "aq:///images/5-up.png",
        '6_down' : "aq:///images/6-down.png"
    },
    'list':{
        'event_birthday' : "file:///facebook/images/indicator-birthday.png",
        'message' : "file:///facebook/images/indicator-message.png",
        'link' : "file:///facebook/images/indicator-link.png",
        'comment' : "file:///facebook/images/indicator-addcomment.png",
        'photo' : "file:///facebook/images/indicator-photo.png",
        'status' : "file:///facebook/images/indicator-statuspost.png",
        'default_profile' : "file:///facebook/images/avatar-generic.png"
    }
};

/**
 * Holds the available states/pages for this app
 */
FB.STATE = {
    "main_menu" : {
        "template_id" : "c", //TODO: Change this to G once we receive category images
        "template_content" : {
            "header" : FB.IMAGES.header,
            "list_pattern" : 1,
            "buttons" : {},
            "list" : 
            	[{
                    "image1" : FB.IMAGES.list.x, //TODO: Update the image once received from Boris
                    "text" : $.t("news_Feed"),
                    "action" : "loadNewsFeed"
                },{
                    "image1" : FB.IMAGES.list.x,  //TODO: Update the image once received from Boris
                    "text" : $.t("status"),
                    "action" : "loadPresetStatus"
                },{
                    "image1" : FB.IMAGES.list.x,  //TODO: Update the image once received from Boris
                    "text" : $.t("events"),
                    "action" : "loadEventsMenu"
                },{
                    "image1" : FB.IMAGES.list.x,  //TODO: Update the image once received from Boris
                    "text" : $.t("nearby_Friends"),
                    "action" : "loadNearbyFriends"
                },{
                    "image1" : FB.IMAGES.list.x,  //TODO: Update the image once received from Boris
                    "text" : $.t("friends"),
                    "action" : "loadFriends",
                    "policy" : ["BUTTON.DISABLED_WHILE_DRIVING"]
                },{
                    "image1" : FB.IMAGES.list.x,  //TODO: Update the image once received from Boris
                    "text" : $.t("messages"),
                    "action" : "loadMessages"
                },{
                    "image1" : FB.IMAGES.list.x,  //TODO: Update the image once received from Boris
                    "text" : $.t("check_In"),
                    "action" : "loadCheckInLocations"
                }]
        }
    },
    "submenu" : {
        "template_id" : "c",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },
    "preset_list" : {
        "template_id" : "c",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },
    "events_page" : {
        "template_id" : "c",
        "template_content" : {
            "header" : FB.IMAGES.header,
//          "list_pattern" : 1,
            "buttons" : {},
            "list" : []
        }
    },
    "events_birthday_page" : {
        "template_id" : "a",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "list_pattern" : 1,
            "buttons" : {},
            "list" : []
        }
    },
    "list_page" : {
        "template_id" : "a",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "list_pattern" : 2,
            "buttons" : {},
            "list" : []
        }
    },
    "post_list" : {
        "template_id" : "a",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "list_pattern" : 2,
            "buttons" : {},
            "list" : []
        }
    },
    "checkin_list" : {
        "template_id" : "c",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },
    "detail_page" : {
        "template_id" : "b",
        "template_content" : {
            "header" :  FB.IMAGES.header,
            "buttons" : {},
            "text_left_top" : {"policy" : [], "value" : ""},
            "text_left_bottom" : {"policy" : [], "value" : ""},
            "text_right_main" : {"policy" : ["TEXT.LONG_DESCRIPTION"], "value" :""},
            "image_left" : {"policy" : ["IMAGE.USER_ICON"], "value" :null}
        }
    },
    "confirmation":{
        "template_id" : "f",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "buttons" : {},
            "text_main" : "",
            "text_bottom" : {}
        }
    },

    "no_item_page":{
        "template_id" : "c",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },

    "error":{
        "template_id" : "f",
        "template_content" : {
            "header" : FB.IMAGES.header,
            "buttons" : {},
            "text_main" : "",
            "text_bottom" : {}
        }
    }

};