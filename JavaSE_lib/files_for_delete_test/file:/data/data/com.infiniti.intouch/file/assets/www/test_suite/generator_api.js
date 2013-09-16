// Show the main menu for API testing pages
TS.Display.api_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Create the template J",
        "action" : "loadPage",
        "value" : "api_1"
    },{
        "text" : "Dial Numbers",
        "action" : "loadPage",
        "value" : "api_2"
    }]

    AQ.app.updateScreen( template );
}

// Create a new template - disabled for now
TS.Display.api_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);
 
    var success = false;
    switch(AQ.hash.platform)
    {
        case "android":
        	success = window.android.sendTemplateRequestToHeadUnit("www/aq/templates/templateJ.bin");
            break;

        case "ios":
        	NativeBridge.call("sendTemplateRequestToHeadUnit", JSON.stringify(["aq/templates/templateJ.bin", "isCreateSuccess"]));
        	success = isCreateSuccess;
            break;
    }
    
    if( success == true )
    {
        template.template_content.list.push({text:"Success"});
    }else{
        template.template_content.list.push({text:"Failed"});
    }

    AQ.app.updateScreen( template );
}

// Phone dial API
TS.Display.api_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);
    var numbers = {
        '8007773456' : "MovieFone - 800-777-FILM",
        '177' : "Japan Weather - 177",
        '2486202355' : "Detroit Weather - 248-620-2355",
        '2065266087' : "Seattle Weather - 206-526-6087"
    };

    for(var i in numbers)
    {
        template.template_content.list.push({
            "text" : numbers[i],
            "action" : "dialNum",
            "value" : i
        });
    }
    AQ.app.updateScreen( template );
}
