/**
*
* GENERAL SCREENS
*
**/
TS.Display.general_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);
    template.template_content.list = [{
        "text" : "4 same text strings",
        "action" : "loadPage",
        "value" : "general_1"
    },{
        "text" : "3 screen updates - Template B",
        "action" : "loadPage",
        "value" : "general_2"
    }];

    AQ.app.updateScreen( template );
}

// 4 same List items
TS.Display.general_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    // 4 list items
    for(var i=1; i<=4; i++)
    {
        template.template_content.list.push({
            "text" : "All list items are same"
        });
    }

    AQ.app.updateScreen( template );
}

TS.Display.general_2 = function(data)
{
    var defaults = { requestNum: 1, requestMax: 3 };
    data = $.extend(true, defaults, data);

    console.log( "Screen update:::: "+ JSON.stringify(data, undefined, 2) );

    var template = $.extend(true, {}, TS.STATE.b);

    // Use the big image to delay the screen update
    template.template_content.image_left = TS.IMAGES.e_big;

    template.template_content.text_right_main = "Request - "+data.requestNum;

    AQ.app.updateScreen( template );

    if( data.requestNum < data.requestMax )
    {
        TS.Display.general_2({requestNum: data.requestNum+1});
    }
}
/*** END OF GENERAL SCREENS ***/