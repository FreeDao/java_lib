log("TTS app is initializing...");

try{
	var APP = new Tts_tester();
	APP.init();
}catch(err){
	log("Unable to initialize the TTS: "+err);
}



/**
 * TTS app class
 */
function Tts_tester()
{
	/**
	 * Holds the available states/pages for this app
	 */
	var STATE =
	{
		"main" : {
			"template_id" : "a",
			"screen_id" : 0,
			"template_content" : {
			 	"list_pattern" : 1,
			 	"header" : "http://dummyimages.com/247x40/fff/000&text=TTS%20Tester",
			 	"buttons" : {
                    "1" : {
                        "image" : "http://dummyimages.com/76x29/fff/000&text=Play",
                        "action" : "ttsPlay"
                    },
                    "2" : {
                        "image" : "http://dummyimages.com/76x29/fff/000&text=Pause",
                        "action" : "ttsPause"
                    },
                    "3" : {
                        "image" : "http://dummyimages.com/76x29/fff/000&text=Resume",
                        "action" : "ttsResume"
                    },
                    "4" : {
                        "image" : "http://dummyimages.com/76x29/fff/000&text=Stop",
                        "action" : "ttsStop"
                    }
			 	},
			 	"list" : []
			}
		}
	}

	var ttsId = 0;

	this.ttsPlay = function(){
		ttsId = window.android.processTts("The creators of AutoRap, Magic Piano, and Songify talk about launching on Android and the explosive global growth they¡¯ve seen on Google Play.",0);
		log("process TTS id = " + ttsId);
		window.android.playTts(ttsId);
	}

	this.ttsPause = function (){
		log("pause tts here");
		window.android.pauseTts(ttsId);
	}
	this.ttsResume = function (){
		log("resume tts here");
		window.android.resumeTts(ttsId);
	}


	this.ttsStop = function (){
		log("stop tts here");
		window.android.stopTts(ttsId);
	}



	/**
	 * Holds the set of actions available on this app
	 */
	var ACTIONS = {
		"loadHome" : function(){ AQ.app.load('home'); },
		"back" : function(){ AQ.app.load('home'); },
		"ttsPlay" : this.ttsPlay,
		"ttsPause" : this.ttsPause,
		"ttsResume" : this.ttsResume,
		"ttsStop" : this.ttsStop
	}


	/**
	 * Handles the actions
	 */
	this.handleAction = function( action, value )
	{
		try
		{
			ACTIONS[action]( value );
		}catch(err)
		{
			log("Unable to complete action: "+action);
			log("Error: "+err);
		}
	}


	/**
	 * Initialize the app and respond the first page
	 */
	this.init = function()
	{
		log("Facebook Initialized");
		var url = "http://cultofmac.cultofmaccom.netdna-cdn.com/wp-content/uploads/2012/08/iMessage-Icon.jpg";
		STATE.main.template_content.list.push({  image1:url, text:"hello, this is airbiquity text" });
		STATE.main.template_content.list.push({  image1:url, text:"hello, choreo, speek it out." });

		AQ.app.updateScreen( STATE.main );
	}
}//end of constructor