/**
 * Declare constants to be used in the Pandora app
 *
 * @author: skutfiddinov
 */

PANDORA.IMAGES = {
    'header' : "http://63.149.221.165/pan/images/pandora/header.png",
    'button' : {}
}

/**
 * Holds the available states/pages for this app
 */
PANDORA.STATE = {
    "home" : {
        "template_id" : "c",
        "template_content" : {
            "header" : PANDORA.IMAGES.header,
            "buttons" : {},
            "list" : [
                {
                    "text" : "Connect",
                    "action" : "connect"
                },{
                    "text" : "Play",
                    "action" : "play"
                },{
                    "text" : "Pause",
                    "action" : "pause"
                },{
                    "text" : "Skip",
                    "action" : "skip"
                }
            ]
        }
    }
}