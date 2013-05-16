/**
 * Declare constants to be used in the NBC News app
 *
 * @author: skutfiddinov
 */

NBC.IMAGES = {
    'header' : "file:///nbc_news/images/header.png",
    'button' : {
        "1_tts" : "aq:///images/1-tts.png",
        "1_tts_active" : "aq:///images/1-tts-active.png",
        "1_refresh" : "aq:///images/1-refresh.png",
        "2_prev" : "aq:///images/2-left.png",
        "3_next" : "aq:///images/3-right.png",
        "4_share" : "aq:///images/4-share.png",
        '5_up' : "aq:///images/5-up.png",
        '6_down' : "aq:///images/6-down.png"
    },
    'list' : {
        "facebook" : "file:///nbc_news/images/facebook.png",
        "twitter" : "file:///nbc_news/images/twitter.png",
        "cat_1" : "file:///nbc_news/images/icon-topnews.png", //top news
        "cat_2" : "file:///nbc_news/images/icon-usnews.png", //us news
        "cat_3" : "file:///nbc_news/images/icon-worldnews.png", //worls news
        "cat_4" : "file:///nbc_news/images/icon-news.png", //politics
        "cat_5" : "file:///nbc_news/images/icon-news.png", //business
        "cat_6" : "file:///nbc_news/images/icon-news.png", //entertainment
        "cat_7" : "file:///nbc_news/images/icon-news.png", //sports
        "cat_8" : "file:///nbc_news/images/icon-news.png", //travel
        "cat_9" : "file:///nbc_news/images/icon-news.png", //health
        "cat_10" : "file:///nbc_news/images/icon-news.png", //tech & science
        "cat_11" : "file:///nbc_news/images/icon-news.png", //today
        "cat_12" : "file:///nbc_news/images/icon-weather.png", //weather
        "cat_default" : "file:///nbc_news/images/icon-news.png" //default
    }
}

/**
 * Holds the available states/pages for this app
 */
NBC.STATE = {
    "home" : {
        "template_id" : "g",
        "template_content" : {
            "header" : NBC.IMAGES.header,
            "list_pattern" : "1",
            "buttons" : {},
            "list" : []
        }
    },


    /**
     * Topic stories page
     */
    "topic" : {
        "template_id" : "c",
        "template_content" : {
            "header" : NBC.IMAGES.header,
            "buttons" : {},
            "list" : []
        }
    },


    /**
     * Story page
     */
    "story" : {
        "template_id" : "b",
        "template_content" : {
            "header" : NBC.IMAGES.header,
            "buttons" : {
            	"1" : {},
            	"2" : {},
            	"3" : {},
            	"4" : {},
            	"5" : {},
            	"6" : {}
            },
            "text_left_top" : {"policy" : [], "value" :null },
            "text_left_bottom" : {"policy" : [], "value" :null },
            "text_right_main" : {"policy" : ["TEXT.LONG_DESCRIPTION"], "value" :null},
            "image_left" : {"policy" : ["IMAGE.USER_ICON"], "value" :null}
        }
    },


    /**
     * Social network share button
     */
    "share" : {
        "template_id" : "a",
        "template_content" : {
            "header" : NBC.IMAGES.header,
            "list_pattern" : 1,
            "buttons" : {},
            "list" : [{
                "image1" : NBC.IMAGES.list.facebook,
                "text" : "Facebook - " + $.t("unavailable")
            },{
                "image1" : NBC.IMAGES.list.twitter,
                "text" : "Twitter - " + $.t("unavailable")
            }]
        }
    },


    /**
     * Topic stories page
     */
    "error" : {
        "template_id" : "c",
        "template_content" : {
            "header" : NBC.IMAGES.header,
            "buttons" : {},
            "list" : [{
                text: $.t("error_Notification"),
                action: "back"
            }]
        }
    }
}