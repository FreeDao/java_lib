/**
*
* Template J
*
**/
TS.Display.j_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Regular Full Page",
        "action" : "loadPage",
        "value" : "j_1"
    }];

    AQ.app.updateScreen( template );
}

// Regular Full Page
TS.Display.j_1 = function()
{
	var template = $.extend(true, {}, TS.STATE.j);

	template.template_content.text_main = "Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1 - Text 1";
	AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE H ***/
