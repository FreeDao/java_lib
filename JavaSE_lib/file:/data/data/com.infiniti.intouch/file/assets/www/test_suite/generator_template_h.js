/**
*
* Template H
*
**/
TS.Display.h_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Regular Full Page",
        "action" : "loadPage",
        "value" : "h_1"
    },{
        "text" : "Mixed policy Page",
        "action" : "loadPage",
        "value" : "h_policy"
    }];

    AQ.app.updateScreen( template );
}

// Regular Full Page
TS.Display.h_1 = function()
{
	var template = $.extend(true, {}, TS.STATE.h);

        for(var i=1; i<=6; i++)
        {
            template.template_content.buttons[i]={
                "image" : TS.IMAGES.button
            };
        }

        for(var i=1; i<=4; i++)
        {
            template.template_content.items[i]={
                "image" : TS.IMAGES.h1,
		        "text" : "Text " + i + " - Text " + i + " - Text " + i + " - Text " + i + " - Text " + i
            };
        }

	template.template_content.main_image = TS.IMAGES.h2;
	AQ.app.updateScreen( template );
}

TS.Display.h_policy = function()
{
	var template = $.extend(true, {}, TS.STATE.h_policy);

    for(var i=1; i<=4; i++)
    {
    	var rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
    	var list_rand_policy = (parseInt(10*Math.random())%2 == 0)? ["ELEMENT." + rand_policy + "_WHILE_DRIVING"] : [];

    	var image_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		var text_rand_policy = (parseInt(10*Math.random())%2 == 0)? "HIDDEN" : "DISABLED" ;
		
        template.template_content.items[i]={
            "policy" : list_rand_policy,
            "image" : {
                "policy" :  ["ELEMENT." + image_rand_policy + "_WHILE_DRIVING"],
            	"value":TS.IMAGES.h1
            },
	        "text" : {
                "policy" :  ["ELEMENT." + text_rand_policy + "_WHILE_DRIVING"],
	        	"value" : "T:" + text_rand_policy + ",I:" + image_rand_policy
	        }
        };
    }
	
	AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE H ***/
