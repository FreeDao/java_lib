/**
*
* Template C
*
**/
TS.Display.c_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "c_0"
    },{
        "text" : "Mixed Policy Pages",
        "action" : "loadPage",
        "value" : "c_policy"
    },{
        "text" : "3 items",
        "action" : "loadPage",
        "value" : "c_1"
    },{
        "text" : "20 items",
        "action" : "loadPage",
        "value" : "c_2"
    }];

    AQ.app.updateScreen( template );
}

// Empty page
TS.Display.c_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.c);

    // template.template_content.header = "";

    // all buttons
    template.template_content.buttons = {};
    /*
    for(var i=1; i<=6; i++)
    {
        template.template_content.buttons[i] = {
            "image" : "",
            "policy" : []
        }
    }
    */
    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "text" : ""
        });
    }

    AQ.app.updateScreen( template );
}

//mixed policy 
TS.Display.c_policy = function()
{
    var template = $.extend(true, {}, TS.STATE.c_policy);

    // 3 list items
    for(var i=1; i<= 6; i++)
    {
    	var rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
    	var list_rand_policy = (parseInt(10*Math.random())%2 == 0)? ["ELEMENT." + rand_policy + "_WHILE_DRIVING"] : [];

        template.template_content.list.push({
            "text" : "Policy : " + list_rand_policy,
            "policy" : list_rand_policy
        });
    }

    AQ.app.updateScreen( template );
}

// 3 List items
TS.Display.c_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.c);

    // 3 list items
    for(var i=1; i<=3; i++)
    {
        template.template_content.list.push({
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}

// 20 List items
TS.Display.c_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.c);

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE C ***/