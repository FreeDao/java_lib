/**
*
* Template D
*
**/
TS.Display.d_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "d_0"
    },{
        "text" : "3 items",
        "action" : "loadPage",
        "value" : "d_1"
    },{
        "text" : "20 items",
        "action" : "loadPage",
        "value" : "d_2"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.d_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.d);

    // template.template_content.header = "";
    template.template_content.image_left = "";

    // all buttons
    for(var i=1; i<=6; i++)
    {
        template.template_content.buttons[i] = {
            "image" : ""
        }
    }

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "text" : "",
            "image" : ""
        });
    }

    AQ.app.updateScreen( template );
}


// 3 List items
TS.Display.d_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.d);

    // 3 list items
    for(var i=1; i<=3; i++)
    {
        template.template_content.list.push({
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i,
            "image" : TS.IMAGES.a1
        });
    }

    AQ.app.updateScreen( template );
}

// 20 List items
TS.Display.d_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.d);

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i,
            "image" : TS.IMAGES.a1
        });
    }

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE D ***/