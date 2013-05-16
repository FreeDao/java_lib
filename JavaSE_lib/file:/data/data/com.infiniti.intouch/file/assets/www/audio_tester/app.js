log("Audio_tester app is initializing...");


/**
 * Audio_tester app class
 */
function Audio_tester()
{

	this.defaults = {
		currentTts : {
			id: null,
			text : null
		}
    }
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
        'header' : 'file:///audio_tester/images/header.png',
        'tts' : "file:///audio_tester/images/button-tts.png",
        'vr' : "file:///audio_tester/images/button-vr.png",
        'set' : "file:///audio_tester/images/button-set.png",
        'meter' : "file:///audio_tester/images/button-meter.png",
        'exit' : "file:///audio_tester/images/button-exit.png"
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
                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.tts,
                        "action" : "playTTS",
                        "value" : "aq_active_list_item"
                    },
                    "2" : {
                        "image" : IMAGES.vr,
                        "action" : "startVR",
                        "value" : "0"
                    },
                    "3" : {
                        "image" : IMAGES.meter,
                        "action" : "showMeter",
                        "value" : "0"
                    },
                    "6" : {
                        "image" : IMAGES.exit,
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


        "meter" : {
            "template_id" : "c",
            "template_content" : {
                "header" : IMAGES.header,
                "buttons" : {
                    "1" : {
                        "image" : IMAGES.set,
                        "action" : "setMeter",
                        "value" : {
                            source : "Test App",
                            track : "Track Name",
                            popup : false,
                            album : "Album Name",
                            artist : "Artist 名前"
                        }
                    }
                },
                "list" : [{
                    "text" : "Source : Test App"
                },{
                    "text" : "Track : Track Name"
                },{
                    "text" : "Album : Album Name"
                },{
                    "text" : "Artist : Artist 名前"
                }]
            }
        },
    }


	var that = this;

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
			if( that.defaults.currentTts.id!=null ){
				AQ.app.stopTts(that.defaults.currentTts.id);
				that.defaults.currentTts.id=null;
			}
			if(that.defaults.currentTts.text == text){
		    	//TODO : update UI to nomal
		    	that.defaults.currentTts.text = null;
			}else{
				AQ.app.processTts(text);
				that.defaults.currentTts.text = text;
			}
	        return;
	        
        },
        "TTS_READY" : function( resp ){
			AQ.app.playTts(resp.ttsId);
			that.defaults.currentTts.id = resp.ttsId;
	    	//TODO : update UI to active
        },
        "TTS_END" : function( resp ){
	        log("TTS is finished for id: "+resp.ttsId);
			if(that.defaults.currentTts.id == resp.ttsId){
				that.defaults.currentTts.id = null;
				that.defaults.currentTts.text = null;
			}
	    	//TODO : update UI to nomal
        },
        "startVR" : function(language){
            // TODO
        	AQ.app.startVR(language)
        },
        "VR_END" : function(resp){
            //log("VR Response for id "+resp.vrId+": " +resp.text);
            STATE.result.template_content.text_main = resp.vrId+" - "+resp.text;
            AQ.app.updateScreen( STATE.result );
        },
        "showMeter" : function()
        {
            AQ.app.updateScreen( STATE.meter );
        },
        "setMeter" : function(data)
        {
            var result = AQ.app.updateMeter(data);

            //console.log( JSON.stringify(result, undefined, 2) );
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
