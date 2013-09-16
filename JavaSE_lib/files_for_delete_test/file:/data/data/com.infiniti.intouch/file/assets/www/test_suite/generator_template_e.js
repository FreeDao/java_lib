/**
*
* Template E
*
**/
TS.Display.e_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "e_0"
    },{
        "text" : "Regular Image",
        "action" : "loadPage",
        "value" : "e_1"
    },{
        "text" : "Large Image From Internet - ~950KB",
        "action" : "loadPage",
        "value" : "e_2"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.e_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.e);

    // all buttons
    for(var i=1; i<=6; i++)
    {
        template.template_content.buttons[i] = {
            "image" : ""
        }
    }

    // template.template_content.header = "";
    template.template_content.image_main = "";

    AQ.app.updateScreen( template );
}

// short text - 56 characters
TS.Display.e_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.e);

    AQ.app.updateScreen( template );
}


// short text - 56 characters
TS.Display.e_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.e);

    template.template_content.image_main = TS.IMAGES.e_big;

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE E ***/