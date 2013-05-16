/**
*
* Template G_3
*
**/
TS.Display.g_3_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "g_3_0"
    },{
        "text" : "Mixed Policy Page",
        "action" : "loadPage",
        "value" : "g_3_policy"
    },{
        "text" : "4 items",
        "action" : "loadPage",
        "value" : "g_3_1"
    },{
        "text" : "20 items",
        "action" : "loadPage",
        "value" : "g_3_2"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.g_3_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.g_3);

    // template.template_content.header = "";

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "image1" : "",
            "image2" : "",
            "image3" : "",
            "text" : ""
        });
    }

    AQ.app.updateScreen( template );
}

// policy page
TS.Display.g_3_policy = function()
{
    var template = $.extend(true, {}, TS.STATE.g_3_policy);
    // 3 list items
    for(var i=1; i<=4; i++)
    {
		//Using random to simulator
		var rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		var list_rand_policy = (parseInt(10*Math.random())%2 == 0)? ["ELEMENT." + rand_policy + "_WHILE_DRIVING"] : [];
		
		var image1_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		var image2_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		var image3_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		var text_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		
		template.template_content.list.push({
			"policy" : list_rand_policy,
			"image1" : {
				 "policy" : ["ELEMENT." + image1_rand_policy + "_WHILE_DRIVING"],
				 "value" : TS.IMAGES.a1
			},
			 "image2" : {
				 "policy" : ["ELEMENT." + image2_rand_policy + "_WHILE_DRIVING"],
				 "value" : TS.IMAGES.a2
			 },
			 "image3" : {
				 "policy" : ["ELEMENT." + image3_rand_policy + "_WHILE_DRIVING"],
				 "value" : TS.IMAGES.a3
			 },
			 "text" : {
				 "policy" : ["ELEMENT." + text_rand_policy + "_WHILE_DRIVING"],
				 "value" : "L-"+list_rand_policy 
				 			+ ",T-" + text_rand_policy 
				 			+ ",1-" + image1_rand_policy
				 			+ ",2-" + image2_rand_policy
				 			+ ",3-" + image3_rand_policy
			 }
		 });
    }
    AQ.app.updateScreen( template );
}

// 3 List items
TS.Display.g_3_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.g_3);

    // 3 list items
    for(var i=1; i<=4; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "image2" : TS.IMAGES.a2,
            "image3" : TS.IMAGES.a3,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}

// 20 List items
TS.Display.g_3_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.g_3);

    // 20 list items
    for(var i=1; i<=20; i++)
    {
        template.template_content.list.push({
            "image1" : TS.IMAGES.a1,
            "image2" : TS.IMAGES.a2,
            "image3" : TS.IMAGES.a3,
            "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
        });
    }

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE G_3 ***/