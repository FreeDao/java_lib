/**
*
* Template G_2
*
**/
TS.Display.g_2_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "g_2_0"
    },{
        "text" : "4 items",
        "action" : "loadPage",
        "value" : "g_2_1"
    },{
        "text" : "20 items",
        "action" : "loadPage",
        "value" : "g_2_2"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.g_2_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.g_2);

    // template.template_content.header = "";

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "image1" : "",
            "image2" : "",
            "text" : ""
        });
    }

    AQ.app.updateScreen( template );
}

// 3 List items
TS.Display.g_2_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.g_2);

    // 3 list items
    for(var i=1; i<=4; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "image2" : TS.IMAGES.a2,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}

// 20 List items
TS.Display.g_2_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.g_2);

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "image2" : TS.IMAGES.a2,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE G_2 ***/