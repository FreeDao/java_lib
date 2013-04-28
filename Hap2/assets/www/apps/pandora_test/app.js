log("Pandora app is initializing...");

//Make the APP variable globally accessible
var APP;
var PANDORA = {};

AQ.app.loadScript(["constants.js", "PandoraLink.js"], function()
{
    APP = new Pandora();
    APP.init();
});


PANDORA.callbackObj = {
	onSessionStart : function(data){
		log("onSessionStart was called...");
	},
	onEventTrackPlay : function(data){
		log("onEventTrackPlay was called...");
	},
	onEventTrackPause : function(data){
		log("onEventTrackPause was called...");
	},
	onEventTrackSkip : function(data){
		log("onEventTrackSkip was called...");
	},
	onUpdateStatus : function(data){
		log("PNDR_UPDATE_STATUS was called...");
		log( "payload: "+ JSON.stringify(data, undefined, 2) );
	}
};


/**
 * Pandora class
 */
function Pandora()
{
    this.defaults = {}

    /**
     * Initialize the app and load the home screen
     */
    this.init = function()
    {
        log("Pandora is initialized");
		FrameHandler.setCallback(PANDORA.callbackObj);
        this.loadHome();
    }

    this.back = function()
    {
        AQ.app.exit();
    }

    /**
     * Load home screen - list of apps
     */
    this.loadHome = function()
    {
        AQ.app.updateScreen( PANDORA.STATE.home );
    }

    this.connect = function()
    {
        this.sendData("7E 00 00 00 00 00 11 00 00 03 41 50 49 54 4F 4F 4C 30 00 C8 01 00 00 64 88 FA 7C");
    }

    this.play = function()
    {
        this.sendData("7E 00 00 00 00 00 01 30 F4 AC 7C");
    }

    this.pause = function()
    {
        this.sendData("7E 00 00 00 00 00 01 31 E4 8D 7C");
    }

    this.skip = function()
    {
        this.sendData("7E 00 00 00 00 00 01 32 D4 EE 7C");
    }

    this.sendData = function(data)
    {
        log("SENDING DATA TO HAP hex: "+ data);
        data = AQ.app.hexToBase64(data);
        log("SENDING DATA TO HAP base64: "+ data);

        AQ.respond.sendAppRequest({
            appName: "Pandora",
            contentType:"application/octet-stream",
            contentTransferEncoding:"base64",
            content: data
        });
        return;
    }

    this.AQ_APP_RESPONSE = function(data)
    {
        log( "******************** " + new Date() + " ********************" );
        log( "RAW data.content: " + data.content );

        if(data.transferEncoding == "base64")
        {
            data.content = AQ.app.base64ToHex( data.content );

            if( data.content === false )
            {
                return;
            }

            data.content = data.content.split(" ");
            data.content = this.hexArray2Array( data.content );
        }

        log("data content being sent to Pandora Parser: "+ EscapeTool.unescape(data.content) );

		FrameHandler.onFrame( EscapeTool.unescape(data.content) );

  //       var parser = new Parser();
  //       log("parser: "+ JSON.stringify(parser, undefined, 2) );

  //       var frame = parser.parse( EscapeTool.unescape(itemdata) );
  //       log("frame: "+ JSON.stringify(frame, undefined, 2) );

  //       var payload = frame.getPayload();

  //       log( "payload: "+ JSON.stringify(payload, undefined, 2) );

		// var command = payload.getCommand();
  //       log( "command: "+ JSON.stringify(command, undefined, 2) );
    };

	this.hexArray2Array = function(array){
		var bytes = [];
		for (var i=0; i<array.length; i++)
        {
			bytes.push(parseInt(array[i].trim(), 16));
        }
		return bytes;
	};

    /**
     * Handles the actions
     */
    this.handleAction = function( action, value )
    {
        this[action](value);

        return;
    }
}//end of contructor