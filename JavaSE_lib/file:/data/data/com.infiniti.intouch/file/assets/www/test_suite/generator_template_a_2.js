/**
*
* Template A 2
*
**/
TS.Display.a_2_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);
    template.template_content.list = [{
        "text" : "Empty page",
        "action" : "loadPage",
        "value" : "a_2_0"
    },{
        "text" : "Mixed Policy Page",
        "action" : "loadPage",
        "value" : "a_2_policy"
    },{
        "text" : "4 items",
        "action" : "loadPage",
        "value" : "a_2_1"
    },{
        "text" : "20 items;",
        "action" : "loadPage",
        "value" : "a_2_2"
    },{
        "text" : "20 items; 6 - active key;",
        "action" : "loadPage",
        "value" : "a_2_3"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.a_2_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_2);

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
            "image2" : "",
            "text" : ""
        });
    }

    AQ.app.updateScreen( template );
}

//Mixed policy
TS.Display.a_2_policy = function()
{
 var template = $.extend(true, {}, TS.STATE.a_2_policy);

 // 4 list items
 for(var i=1; i<=4; i++)
 {
 	var list_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
	var image2_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
	
     template.template_content.list.push({
    	 "policy" : ["ELEMENT." + list_rand_policy + "_WHILE_DRIVING"],
         "image1" : TS.IMAGES.a1,
         "image2" : {
        	 "policy" : ["ELEMENT." + image2_rand_policy + "_WHILE_DRIVING"], 
        	 "value" :TS.IMAGES.a2
         },
         "text" : {
        	 "policy" : [], 
        	 "value" : ",T:[]" + "L:" + list_rand_policy + ",1:[]" + ",2:" + image2_rand_policy
         }
     });
 }

 AQ.app.updateScreen( template );
}
// 4 items
TS.Display.a_2_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_2);

    // 4 list items
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

// 20 items; text and image hatching - gray; button hatching - black;
TS.Display.a_2_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_2);

    // 20 list items
    // TODO: add hatching info
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

// 20 items; text and image hatching - some gray; button hatching - gray; active key is 6;
TS.Display.a_2_3 = function()
{
    var template = $.extend(true, {}, TS.STATE.a_2);

    // set the active key to 6
    template.template_content.active_key = 6;

    // 20 list items
    // TODO: add hatching info
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
/*** END OF TEMPLATE A 2 ***/