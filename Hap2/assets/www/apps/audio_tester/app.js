log("Audio_tester app is initializing...");
var APP = new Audio_tester();
APP.init();


/**
 * Audio_tester app class
 */
function Audio_tester()
{
    /**
     * Initialize the app and respond the first page
     */
    this.init = function()
    {
        log("Audio_tester Initialized");
        AQ.app.updateScreen( STATE.main );
    };


    /**
     * Holds the images for the template app
     */
    var IMAGES =
    {
        'logo' : 'http://dummyimages.com/247x40/fff/000.png&text=Audio%20Tester',
        'button' : "http://dummyimages.com/76x29/fff/000.png"
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
                    "1" : {
                        "image" : IMAGES.button+"&text=TTS",
                        "action" : "playTTS",
                        "value" : "aq_active_list_item"
                    },
                    "2" : {
                        "image" : IMAGES.button+"&text=VR",
                        "action" : "startVR",
                        "value" : "0"
                    },
                    "6" : {
                        "image" : IMAGES.button+"&text=EXIT",
                        "action" : "loadHome"
                    }
                },
                "list" : [{
                    "text" : "Hello! My name is Android.",
                    "value" : "Hello! My name is Android."
                },{
                    "text" : "Testing text to speech.",
                    "value" : "Testing text to speech."
                },{
                    "text" : "This is really long text. Sometimes it seems like it will never finish. But one day... one day, it will come to an end. I could keep going, but it's just getting boring. So I guess I will stop right here. Thank you for listening. It was my pleasure talking. Bye bye.",
                    "value" : "This is really long text. Sometimes it seems like it will never finish. But one day... one day, it will come to an end. I could keep going, but it's just getting boring. So I guess I will stop right here. Thank you for listening. It was my pleasure talking. Bye bye."
                }]
            }
        },

        result:{
            "template_id" : "f",
            "template_content" : {
                "header" : IMAGES.header,
                "text_main" : "",
                "text_bottom" : {
                    "1" : {
                        "text" : "OK",
                        "action" : "back"
                    }
                }
            }
        },
    }


    /**
     * Holds the set of actions available on this app
     */
    var ACTIONS = {
        "back" : function(){
            APP.init();
        },
        "loadHome" : function(){
            AQ.app.load('home');
        },
        "playTTS" : function(text){
            AQ.respond.processTts(text);
        },
        "TTS_READY" : function( resp ){
            AQ.respond.playTts(resp.ttsId);
        },
        "TTS_END" : function( resp ){
            // TODO show some notification
            log("TTS is finished for id: "+resp.ttsId);
        },
        "startVR" : function(language){
            // TODO
            // AQ.respond.processVr();
        	AQ.respond.startVR(language)
        },
        "VR_END" : function(resp){
            log("VR Response for id "+resp.vrId+": " +resp.text);
            STATE.result.template_content.text_main = resp.vrId+" - "+resp.text;
            AQ.app.updateScreen( STATE.result );
        }
    }


    /**
     * Handles the actions
     */
    this.handleAction = function( action, value )
    {
        log("Action: "+action);
        log("Value: "+JSON.stringify(value));

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