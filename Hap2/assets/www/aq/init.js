switch(AQ.hash.platform)
{
    case "android":
        (function(){
            var isFirstStart = window.android.isFirstStart();
            if( isFirstStart === true )
            {
                window.android.putString("CURRENT_APP", 'home');
            }
        })();

        AQ.hash.currentApp = window.android.getString("CURRENT_APP", 'home');
        break;

    case "ios":
        //TODO Implement isFirstStart() function on IOS layer

        NativeBridge.call("getString", JSON.stringify(["CURRENT_APP", "home", "AQ.hash.currentApp"]));
        break;
}

log("Current platform: "+ AQ.hash.platform);
log("Current app: "+ AQ.hash.currentApp);

log("MIP ID: "+ AQ.storage.getMipId());

$(document).ready(function(){
    /**
    * LOAD THE APPLICATION JS FILE
    *
    * TODO: handle the case when there's no js file in apps folder
    */

    log("apps/"+AQ.hash.currentApp+"/app.js is loading to the DOM...");

	$.jsperanto.init(function(t) {
    $('#app').load("apps/"+AQ.hash.currentApp+"/app.js", function(data, textStatus, jqXHR){
        log("apps/"+AQ.hash.currentApp+"/app.js is loaded to the DOM");
        log( "textStatus: " + textStatus );
    });
	}, {
		lang : AQ.app.getLanguage(),
		dicoPath : "apps/"+AQ.hash.currentApp+"/locales"
	});

});


//TEMP for IOS debugging
$(document).delegate('#clear', 'click', function()
{
    $('#log').html('');
});