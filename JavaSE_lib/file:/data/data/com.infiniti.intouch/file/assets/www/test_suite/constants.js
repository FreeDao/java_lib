/**
 * Declare constants to be used in the Home app
 *
 * @author: skutfiddinov
 */

TS.IMAGES = {
    'logo' : 'file:///test_suite/images/header.png',
    'localHeader' : "aq:///images/header.png",
    'header' : "aq:///images/test/247x40.png",
    'button' : "aq:///images/test/76x29.png",
    'a1' : "aq:///images/test/38x32.png",
    'a2' : "aq:///images/test/54x24.png",
    'a3' : "aq:///images/test/54x24.png",
    'b' : "aq:///images/test/153x80.png",
    'd' : "aq:///images/test/174x152.png",
    'e' : "aq:///images/test/456x142.png",
    'e_big' : "http://farm9.staticflickr.com/8084/8401825180_0ccd27acd4_k.jpg", // 950KB Image
    'g1' : "aq:///images/test/38x32.png",
    'g2' : "aq:///images/test/54x24.png",
    'g3' : "aq:///images/test/54x24.png",
    'h1' : "aq:///images/test/106x30.png",
    'h2' : "aq:///images/test/80x70.png"
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
            "header" : {"policy" : [], "value" :TS.IMAGES.logo},
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
            "header" : TS.IMAGES.header,
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
    
    a_1_policy : {
        "template_id" : "a",
        "template_content" : {
            "header" : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header },
            "list_pattern" : 1,
            "buttons" : {
                1 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                6 : { "policy" : [], "image" : TS.IMAGES.button },
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
            "header" : TS.IMAGES.header,
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

    a_2_policy : {
        "template_id" : "a",
        "template_content" : {
            "header" : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header },
            "list_pattern" : 2,
            "buttons" : {
                1 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                6 : { "policy" : [], "image" : TS.IMAGES.button },
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
            "header" : TS.IMAGES.header,
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
    
    a_3_policy : {
        "template_id" : "a",
        "template_content" : {
            "header" :  {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "list_pattern" : 3,
            "buttons" : {
                1 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                2 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                3 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                4 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                5 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                6 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
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
            "header" :  {"policy" : [], "value" :TS.IMAGES.header},
            "buttons" : {
                1 : {"policy" : [],  "image" : TS.IMAGES.button },
                2 : {"policy" : [],  "image" : TS.IMAGES.button },
                3 : {"policy" : [],  "image" : TS.IMAGES.button },
                4 : {"policy" : [],  "image" : TS.IMAGES.button },
                5 : {"policy" : [],  "image" : TS.IMAGES.button },
                6 : {"policy" : [],  "image" : TS.IMAGES.button }
            },
            "text_left_top" :  {"policy" : [], "value" :"Text 1."},
            "text_left_bottom" :  {"policy" : [], "value" :"Text 2."},
            "text_right_main" : {"policy" : [], "value" :null},
            "image_left" : {"policy" : [], "value" :TS.IMAGES.b}
        }
    },
    b_disabled:{
        "template_id" : "b",
        "template_content" : {
            "header" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "buttons" : {
                1 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                2 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                3 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                4 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                5 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                6 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button }
            },
            "text_left_top" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :"Text 1."},
            "text_left_bottom" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :"Text 2."},
            "text_right_main" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :null},
            "image_left" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.b}
        }
    },    
    b_hidden:{
        "template_id" : "b",
        "template_content" : {
            "header" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "buttons" : {
                1 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                2 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                3 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                4 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                5 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                6 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button }
            },
            "text_left_top" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :"Text 1."},
            "text_left_bottom" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :"Text 2."},
            "text_right_main" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :null},
            "image_left" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :TS.IMAGES.b}
        }
    },
    b_mixed:{
        "template_id" : "b",
        "template_content" : {
            "header" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "buttons" : {
                1 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                2 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                3 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                4 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                5 : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"],  "image" : TS.IMAGES.button },
                6 : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button }
            },
            "text_left_top" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :"Text 1."},
            "text_left_bottom" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :"Text 2."},
            "text_right_main" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :null},
            "image_left" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.b}
        }
    },

    /**
     * Template C
     */
    c:{
        "template_id" : "c",
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
            "list" : []
        }
    },
    
    c_policy:{
        "template_id" : "c",
        "template_content" : {
            "header" : {"policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "buttons" : {
                1 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                2 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                3 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                4 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                5 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
                6 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
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
    
    f_policy:{
        "template_id" : "f",
        "template_content" : {
            "header" :  {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "text_main" : {
            	"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], 
            	"value" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1",
            },
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
    },
    g_3_policy: {
        "template_id" : "g",
        "template_content" : {
            "header" :  {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header},
            "list_pattern" : 3,
            "list" : []
        }
    },
    
    
    
	/**
	 * Template H 
	 */
	h : {
	    "template_id" : "h",
	    "template_content" : {
			"header" : TS.IMAGES.header,
			"buttons" : {},
			"items" : {},
			"main_image" : ""
	    }
	},
	
	h_policy : {
	    "template_id" : "h",
	    "template_content" : {
			"header" : {"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.header},
			"buttons" : {
	            1 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
	            2 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
	            3 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
	            4 : { "policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "image" : TS.IMAGES.button },
	            5 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button },
	            6 : { "policy" : ["ELEMENT.HIDDEN_WHILE_DRIVING"], "image" : TS.IMAGES.button }
			},
			"items" : {},
			"main_image" :{"policy" : ["ELEMENT.DISABLED_WHILE_DRIVING"], "value" :TS.IMAGES.h2}
	    }
	},
	/**
	 * Template J 
	 */
	j : {
		"template_id" : "j",
		"template_content" : {
			"header" : TS.IMAGES.header,
			"buttons" : {
                1 : { "image" : TS.IMAGES.button },
                2 : { "image" : TS.IMAGES.button },
                3 : { "image" : TS.IMAGES.button },
                4 : { "image" : TS.IMAGES.button },
                5 : { "image" : TS.IMAGES.button },
                6 : { "image" : TS.IMAGES.button }
			},
			"text_main" : ""
		}
	}
}
