/*
 * 
 * onCreate
 * 
 * onStart
 * onRestart
 * onPause
 * onResume
 * onStop
 * 
 * onDestroy
 * 
 * */

switch(AQ.hash.platform)
{
    case "android":
        (function(){
            AQ.hash.isFirstStart = window.android.isFirstStart();


            if( AQ.hash.isFirstStart === true )
            {
                window.android.putString("CURRENT_APP", 'home');
            }
        })();

        AQ.hash.currentApp = window.android.getString("CURRENT_APP", 'home');
        break;

    case "ios":
        //TODO Implement isFirstStart() function on IOS layer
        (function(){
            NativeBridge.call("isFirstStart", JSON.stringify(["objCisFirstStart"]));
            AQ.hash.isFirstStart = objCisFirstStart;
            
            if(AQ.hash.isFirstStart == true){
                NativeBridge.call("putString", JSON.stringify(["CURRENT_APP", 'home']));
            }
        })();
        
        NativeBridge.call("getString", JSON.stringify(["CURRENT_APP", "home", "objCcurrentApp"]));
        AQ.hash.currentApp = objCcurrentApp;
        break;
}

log("Current platform: "+ AQ.hash.platform);
log("Current app: "+ AQ.hash.currentApp);
//log("MIP ID: "+ AQ.storage.getMipId());

$(document).ready(function()
{
    if( AQ.hash.isFirstStart == true )
    {
        // Notify the HAP that JS layer is ready to receive the data
        switch(AQ.hash.platform)
        {
            case "android":
                window.android.ready();
                break;

            case "ios":
                //TODO - notify iOS that JS is ready to receive the data
                  NativeBridge.call("jsReady", JSON.stringify(["jsReady"]));
                break;
        }
        return;
    }
    
    //Get policy rules for current app
    var policyRules = AQ.storage.getPolicyRules(AQ.hash.currentApp);
    AQ.policy.setPolicyRules(policyRules);

    /**
    * LOAD THE APPLICATION JS FILE
    *
    * TODO: handle the case when there's no js file in apps folder
    */
    $.get("../"+AQ.hash.currentApp+"/app.json", function(data, textStatus, jqXHR)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        AQ.app.init(data);
    });
});


//TEMP for IOS debugging
$(document).delegate('#clear', 'click', function()
{
    $('#log').html('');
});