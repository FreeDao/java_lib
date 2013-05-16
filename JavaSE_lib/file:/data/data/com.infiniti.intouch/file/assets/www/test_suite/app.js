/**
 * Home app
 *
 * Shows the list of available applications and starts them when selected.
 * This app is loaded when the connection with the head unit is established
 * and the user requested to start the HMI apps.
 *
 * @author: skutfiddinov
 */


log("Home app is initializing...");

//Make the APP variable globally accessible
var APP;
var TS = {};
TS.Display = {};

/**
 * Home app class
 */
function Test_suite()
{
	var timer;
	var timerCounter = 0;

	/**
	 * Initialize the app and load the home screen
	 */
	this.init = function()
	{
		log("Home Initialized");
		this.loadPage('main');
		return;
	}

	/**
	 * Load the pages
	 */
	this.loadPage = function(state)
	{
		HISTORY.push(state);
		TS.Display[state]();
		return;
	}

	this.launchTimer = function(data)
	{
		timer = setInterval(function()
		{
			TS.Display.showTimer(timerCounter+=data.interval, data.templateId);
		}, data.interval*1000);
	}

	this.test = function(data)
	{
		console.log("test --- "+ JSON.stringify(data) );
	}

	var HISTORY = [];

	/**
	 * Handle the back button
	 */
	this.back = function()
	{
		clearInterval( timer );
		timerCounter = 0;

		if( HISTORY.length <= 1 ){
			AQ.app.exit();
			return;
		}

		HISTORY.pop();
		var state = HISTORY.pop();
		APP.loadPage( state );
		return;
	}

	this.dialNum = function(num){
    	console.log("Dial Num : " + num);
    	AQ.app.makecall(num);
	}

	/**
	 * Handles the actions
	 */
	this.handleAction = function( action, value )
	{
	    //TEMP so that we can see errors better
	    this[action](value);
	    return;

		try{
			this[action](value);
		}catch(err){
			log("Unable to complete action: "+JSON.stringify(action)+"; value: "+JSON.stringify(value) );
			log(err);
		}
	}
}//end of contructor