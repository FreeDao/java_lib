/**
*
* Template A 1
*
**/
TS.Display.a_1_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);
    template.template_content.list = [{
        "text" : "Empty page",
        "action" : "loadPage",
        "value" : "a_1_0"
    },{
        "text" : "Mixed Policy Page",
        "action" : "loadPage",
        "value" : "a_1_policy"
    },{
        "text" : "4 items",
        "action" : "loadPage",
        "value" : "a_1_1"
    },{
        "text" : "20 items",
        "action" : "loadPage",
        "value" : "a_1_2"
    },{
        "text" : "20 items; 6 - active key;",
        "action" : "loadPage",
        "value" : "a_1_3"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.a_1_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_1);

    // template.template_content.header = "";

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
            "image1" : "",
            "text" : ""
        });
    }

    AQ.app.updateScreen( template );
}

//mixed policy
TS.Display.a_1_policy = function()
{
    var template = $.extend(true, {}, TS.STATE.a_1_policy);

    // 4 list items
    for(var i=1; i<=4; i++)
    {
    	//Using random to simulator
    	var list_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
    	var text_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
    	
        template.template_content.list.push({
        	"policy" : ["ELEMENT." + list_rand_policy + "_WHILE_DRIVING"],
            "image1" : TS.IMAGES.a1,
            "text" : {
            	"policy" : ["ELEMENT." + text_rand_policy + "_WHILE_DRIVING"],
            	"value" : "List:" + list_rand_policy + " - Text:" + text_rand_policy
            }
        });
    }
    AQ.app.updateScreen( template );
}

// 4 items
TS.Display.a_1_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_1);

    // 4 list items
    for(var i=1; i<=4; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}

// 20 items; text and image hatching - gray; button hatching - black;
TS.Display.a_1_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_1);

    // 20 list items
    // TODO: add hatching info
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}

// 20 items; text and image hatching - some gray; button hatching - gray; active key is 6;
TS.Display.a_1_3 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_1);

    // set the active key to 6
    template.template_content.active_key = 6;

    // 20 list items
    // TODO: add hatching info
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE A 1 ***/