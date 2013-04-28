/**
 * Declare constants to be used in the Home app
 *
 * @author: skutfiddinov
 */

TS.IMAGES = {
    'logo' : 'http://dummyimages.com/247x40/fff/000.png&text=Template%20Test%20Suite',
    'localHeader' : "file:///android_assets/www/images/header-nissan-apps.png",
    'header' : "http://dummyimages.com/247x40/fff/000.png",
    'button' : "http://dummyimages.com/76x29/fff/000.png",
    'a1' : "http://dummyimages.com/38x32/fff/000.png",
    'a2' : "http://dummyimages.com/54x24/fff/000.png",
    'a3' : "http://dummyimages.com/54x24/fff/000.png",
    'b' : "http://dummyimages.com/153x80/fff/000.png",
    'd' : "http://dummyimages.com/174x152/fff/000.png",
    'e' : "http://dummyimages.com/456x142/fff/000.png",
    'g1' : "http://dummyimages.com/38x32/fff/000.png",
    'g2' : "http://dummyimages.com/54x24/fff/000.png",
    'g3' : "http://dummyimages.com/54x24/fff/000.png"
}

/**
 * Holds the available states/pages for this app
 */
TS.STATE = {
    /**
     * Menu
     */
    menu:{
        "template_id" : "c",
        "template_content" : {
            "header" : TS.IMAGES.logo,
            "buttons" : {},
            "list" : []
        }
    },


    /**
     * Template A - List pattern 1
     */
    a_1 : {
        "template_id" : "a",
        "template_content" : {
            "header" : TS.IMAGES.logo,
            "list_pattern" : 1,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "list" : []
        }
    },


    /**
     * Template A - List pattern 2
     */
    a_2 : {
        "template_id" : "a",
        "template_content" : {
            "header" : TS.IMAGES.logo,
            "list_pattern" : 2,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "list" : []
        }
    },

    /**
     * Template A - List pattern 3
     */
    a_3 : {
        "template_id" : "a",
        "template_content" : {
            "header" : TS.IMAGES.logo,
            "list_pattern" : 3,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "list" : []
        }
    },


    /**
     * Template B
     */
    b:{
        "template_id" : "b",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "text_left_top" : "Text 1.",
            "text_left_bottom" : "Text 2.",
            "text_right_main" : "",
            "image_left" : TS.IMAGES.b
        }
    },


    /**
     * Template C
     */
    c:{
        "template_id" : "c",
        "template_content" : {
            "header" : TS.IMAGES.logo,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "list" : []
        }
    },


    /**
     * Template D
     */
    d:{
        "template_id" : "d",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "image_left" : TS.IMAGES.d,
            "list" : []
        }
    },


    /**
     * Template E
     */
    e:{
        "template_id" : "e",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button },
            },
            "image_main" : TS.IMAGES.e,
        }
    },


    /**
     * Template F
     */
    f:{
        "template_id" : "f",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "text_main" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1",
            "text_bottom" : {}
        }
    },


    /**
     * Template G - List pattern 1
     */
    g_1 : {
        "template_id" : "g",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "list_pattern" : 1,
            "list" : []
        }
    },


    /**
     * Template G - List pattern 2
     */
    g_2 : {
        "template_id" : "g",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "list_pattern" : 2,
            "list" : []
        }
    },


    /**
     * Template G - List pattern 3
     */
    g_3 : {
        "template_id" : "g",
        "template_content" : {
            "header" : TS.IMAGES.header,
            "list_pattern" : 3,
            "list" : []
        }
    }
}