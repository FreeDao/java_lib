log("Template_tester app is initializing...");
var APP = new Template_tester();
APP.init();

AQ.app.loadScript("test.js", function(){ log("EXTERNAL FILE: Callback is fired on loading test.js"); });

/**
 * Template_tester app class
 */
function Template_tester()
{
    /**
     * Initialize the app and respond the first page
     */
    this.init = function()
    {
        log("Template_tester Initialized");
        AQ.app.updateScreen( STATE.main );
    };


    /**
     * Holds the images for the template app
     */
    var IMAGES =
    {
        'logo' : 'file:///template_tester/images/header.png',
        'exit' : 'file:///template_tester/images/exit-button.png',
        'header' : "aq:///images/test/247x40.png",
        'button' : "aq:///images/test/76x29.png",
        'a1' : "aq:///images/test/38x32.png",
        'a2' : "aq:///images/test/54x24.png",
        'a3' : "aq:///images/test/54x24.png",
        'b' : "aq:///images/test/153x80.png",
        'd' : "aq:///images/test/174x152.png",
        'e' : "aq:///images/test/456x142.png",
        'g1' : "aq:///images/test/38x32.png",
        'g2' : "aq:///images/test/54x24.png",
        'g3' : "aq:///images/test/54x24.png",
        'h1' : "aq:///images/test/106x30.png",
        'h2' : "aq:///images/test/80x70.png",
        '5_up' : "aq:///images/5-up.png",
        '6_down' : "aq:///images/6-down.png"
    };

    /**
     * Holds the available states/pages for this app
     */
    var STATE =
    {
        /**
         * Main menu
         */
        "main" : {
            "template_id" : "c",
            "template_content" : {
                "header" : IMAGES.logo,
                "buttons" : {
                    "6" : {
                        "image" : IMAGES.exit,
                        "action" : "loadHome"
                    }
                },
                "list" : [{
                    "text" : "Template A - List 1",
                    "action" : "loadA1"
                },{
                    "text" : "Template A - List 2",
                    "action" : "loadA2"
                },{
                    "text" : "Template A - List 3",
                    "action" : "loadA3"
                },{
                    "text" : "Template B",
                    "action" : "loadB"
                },{
                    "text" : "Template C",
                    "action" : "loadC"
                },{
                    "text" : "Template D",
                    "action" : "loadD"
                },{
                    "text" : "Template E",
                    "action" : "loadE"
                },{
                    "text" : "Template E - Large Image",
                    "action" : "loadE",
                    "value" : "large"
                },{
                    "text" : "Template F",
                    "action" : "loadF"
                },{
                    "text" : "Template G - List 1",
                    "action" : "loadG1"
                },{
                    "text" : "Template G - List 2",
                    "action" : "loadG2"
                },{
                    "text" : "Template G - List 3",
                    "action" : "loadG3"
                },{
                    "text" : "Template H",
                    "action" : "loadH"
                }]
            }
        },


        /**
         * Template A - List pattern 1
         */
        a_1 : {
            "template_id" : "a",
            "template_content" : {
                "header" : IMAGES.header,
                "list_pattern" : 1,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "list" : [{
                    "image1" : IMAGES.a1,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image1" : IMAGES.a1,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image1" : IMAGES.a1,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image1" : IMAGES.a1,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image1" : IMAGES.a1,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image1" : IMAGES.a1,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                }]
            }
        },


        /**
         * Template A - List pattern 2
         */
        a_2 : {
            "template_id" : "a",
            "template_content" : {
                "header" : IMAGES.header,
                "list_pattern" : 2,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "list" : [{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                }]
            }
        },


        /**
         * Template A - List pattern 3
         */
        a_3 : {
            "template_id" : "a",
            "template_content" : {
                "header" : IMAGES.header,
                "list_pattern" : 3,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "list" : [{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "image3" : IMAGES.a3,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "image3" : IMAGES.a3,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "image3" : IMAGES.a3,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "image3" : IMAGES.a3,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "image3" : IMAGES.a3,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image1" : IMAGES.a1,
                    "image2" : IMAGES.a2,
                    "image3" : IMAGES.a3,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                }]
            }
        },


        /**
         * Template B
         */
        b:{
            "template_id" : "b",
            "template_content" : {
                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "text_left_top" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1",
                "text_left_bottom" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2",
                "text_right_main" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3 - Text 3",
                "image_left" : IMAGES.b
            }
        },


        /**
         * Template C
         */
        c:{
            "template_id" : "c",
            "template_content" : {
                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "list" : [{
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                }]
            }
        },


        /**
         * Template D
         */
        d:{
            "template_id" : "d",
            "template_content" : {
                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "image_left" : IMAGES.d,
                "list" : [{
                    "image" : IMAGES.a1,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image" : IMAGES.a1,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image" : IMAGES.a1,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image" : IMAGES.a1,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image" : IMAGES.a1,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image" : IMAGES.a1,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                }]
            }
        },


        /**
         * Template E
         */
        e:{
            "template_id" : "e",
            "template_content" : {
                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "image_main" : IMAGES.e,
            }
        },


        /**
         * Template F
         */
        f:{
            "template_id" : "f",
            "template_content" : {
                "header" : IMAGES.header,
                "text_main" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1",
                "text_bottom" : {
                    "1" : {
                        "text" : "Tx2"
                    },
                    "2" : {
                        "text" : "Tx3"
                    },
                    "3" : {
                        "text" : "Tx4"
                    }
                }
            }
        },


        /**
         * Template G - List pattern 1
         */
        g_1 : {
            "template_id" : "g",
            "template_content" : {
                "header" : IMAGES.header,
                "list_pattern" : 1,
                "list" : [{
                    "image1" : IMAGES.g1,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image1" : IMAGES.g1,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image1" : IMAGES.g1,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image1" : IMAGES.g1,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image1" : IMAGES.g1,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image1" : IMAGES.g1,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                },{
                    "image1" : IMAGES.g1,
                    "text" : "Text 7 - Text 7 - Text 7 - Text 7 - Text 7"
                }]
            }
        },


        /**
         * Template G - List pattern 2
         */
        g_2 : {
            "template_id" : "g",
            "template_content" : {
                "header" : IMAGES.header,
                "list_pattern" : 2,
                "list" : [{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "text" : "Text 7 - Text 7 - Text 7 - Text 7 - Text 7"
                }]
            }
        },


        /**
         * Template G - List pattern 3
         */
        g_3 : {
            "template_id" : "g",
            "template_content" : {
                "header" : IMAGES.header,
                "list_pattern" : 3,
                "list" : [{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 5 - Text 5 - Text 5 - Text 5 - Text 5"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 6 - Text 6 - Text 6 - Text 6 - Text 6"
                },{
                    "image1" : IMAGES.g1,
                    "image2" : IMAGES.g2,
                    "image3" : IMAGES.g3,
                    "text" : "Text 7 - Text 7 - Text 7 - Text 7 - Text 7"
                }]
            }
        },
	
	
	/**
         * Template H 
         */
        h : {
            "template_id" : "h",
            "template_content" : {

                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.button
                    },
                    "2" : {
                        "image" : IMAGES.button
                    },
                    "3" : {
                        "image" : IMAGES.button
                    },
                    "4" : {
                        "image" : IMAGES.button
                    },
                    "5" : {
                        "image" : IMAGES.button
                    },
                    "6" : {
                        "image" : IMAGES.button
                    }
                },
                "items" : {
	                "1":{
	                    "image" : IMAGES.h1,
	                    "text" : "Text 1 - Text 1 - Text 1 - Text 1 - Text 1"
	                },
	                "2":{
	                    "image" : IMAGES.h1,
	                    "text" : "Text 2 - Text 2 - Text 2 - Text 2 - Text 2"
	                },
	                "3":{
	                    "image" : IMAGES.h1,
	                    "text" : "Text 3 - Text 3 - Text 3 - Text 3 - Text 3"
	                },
	                "4":{
	                    "image" : IMAGES.h1,
	                    "text" : "Text 4 - Text 4 - Text 4 - Text 4 - Text 4"
	                }
				},

                "main_image" : IMAGES.h2

            }
        }

    }


    /**
     * Holds the set of actions available on this app
     */
    var ACTIONS = {
        "back" : function(){ APP.init(); },
        "loadHome" : function(){ AQ.app.load('home'); },
        "loadA1" : function(){ AQ.app.updateScreen( STATE.a_1 ); },
        "loadA2" : function(){ AQ.app.updateScreen( STATE.a_2 ); },
        "loadA3" : function(){ AQ.app.updateScreen( STATE.a_3 ); },
        "loadB" : function(){ AQ.app.updateScreen( STATE.b ); },
        "loadC" : function(){ AQ.app.updateScreen( STATE.c ); },
        "loadD" : function(){ AQ.app.updateScreen( STATE.d ); },
        "loadE" : function(val)
        {
            if(typeof val != "undefined" && val == "large")
            {
                var template = $.extend(true, {}, STATE.e);
                template.template_content.image_main = "http://farm9.staticflickr.com/8084/8401825180_0ccd27acd4_k.jpg";
                AQ.app.updateScreen( template );
            }else{
                AQ.app.updateScreen( STATE.e );
            }
        },
        "loadF" : function(){ AQ.app.updateScreen( STATE.f ); },
        "loadG1" : function(){ AQ.app.updateScreen( STATE.g_1 ); },
        "loadG2" : function(){ AQ.app.updateScreen( STATE.g_2 ); },
        "loadG3" : function(){ AQ.app.updateScreen( STATE.g_3 ); },
        "loadH" : function(){ AQ.app.updateScreen( STATE.h ); }
    }


    /**
     * Handles the actions
     */
    this.handleAction = function( action, value )
    {
        log("Action: "+action);
        log("Value: "+value);

        try
        {
            ACTIONS[action]( value );
        }catch(err)
        {
            log("Unable to complete action: "+action);
            log("Error: "+err);
        }
    }
}//end of contructor
