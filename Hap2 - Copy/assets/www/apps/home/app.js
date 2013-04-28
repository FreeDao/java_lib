log("Home app is initializing...");
var APP = new Home();
APP.init();


/**
 * Home app class
 */
function Home()
{
	/**
	 * Initialize the app and respond the first page
	 */
	this.init = function()
	{
		log("Home Initialized");
		AQ.app.updateScreen( this.state.main );
	}


	/**
	 * Holds the available states/pages for this app
	 */
	this.state =
	{
		"main" : {
			"template_id" : "g",
			"screen_id" : 0,
			"template_content" : {
			 	"header" : "aq:///images/header.png",
			 	"list_pattern" : 1,
			 	"buttons" : {},
			 	"list" : [{
			 	 	"image1" : "file:///pandora/images/launcher.png",
			 	 	"text" : "Pandora",
			 	 	"action" : "loadApp",
			 	 	"value" : "pandora"
			 	// },{
			 	// 	"image1" : "file:///pandora/images/launcher.png",
			 	// 	"text" : "Pandora Test",
			 	// 	"action" : "loadApp",
			 	// 	"value" : "pandora_test"
			 	},{
			 		"image1" : "file:///facebook/images/launcher.png",
			 		"text" : "Facebook",
			 		"action" : "loadApp",
			 		"value" : "facebook"
			 	},{
			 		"image1" : "file:///i_heart_radio/images/launcher.png",
			 		"text" : "iHeartRadio",
			 		"action" : "loadApp",
			 		"value" : "i_heart_radio"
			 	},{
			 		"image1" : "file:///nbc_news/images/launcher.png",
			 		"text" : "NBC News",
			 		"action" : "loadApp",
			 		"value" : "nbc_news"
			 	},{
			 		"image1" : "file:///test_suite/images/launcher.png",
			 		"text" : "Template Testing Suite",
			 		"action" : "loadApp",
			 		"value" : "test_suite"
			 	},{
			 		"image1" : "file:///template_tester/images/launcher.png",
			 		"text" : "Template Tester",
			 		"action" : "loadApp",
			 		"value" : "template_tester"
			 	},{
			 		"image1" : "file:///audio_tester/images/launcher.png",
			 		"text" : "Audio Tester",
			 		"action" : "loadApp",
			 		"value" : "audio_tester"
			 	}]
			}
		}
	}


	/**
	 * Holds the set of actions available on this app
	 */
	this.actions = {
		"loadApp" : function(appName){
			AQ.app.load( appName );
		}
	}


	/**
	 * Handles the actions
	 */
	this.handleAction = function( action, value )
	{
		try{
			this.actions[action]( value );
		}catch(err){
			log("Unable to complete action: "+action);
			log(err);
		}
	}
}//end of constructor