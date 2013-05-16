TS.Display.main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Template Display",
        "action" : "loadPage",
        "value" : "templates_main",
        "policy" : []
    },{
        "text" : "Template Transitions",
        "action" : "loadPage",
        "value" : "transitions_main",
        "policy" : []
    },{
        "text" : "API Test",
        "action" : "loadPage",
        "value" : "api_main",
        "policy" : []
    }]

    AQ.app.updateScreen( template );
}