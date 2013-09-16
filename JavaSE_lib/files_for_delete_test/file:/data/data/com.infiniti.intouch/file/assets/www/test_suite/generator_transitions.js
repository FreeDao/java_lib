TS.Display.transitions_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Timer - 0.2 sec - text only",
        "action" : "launchTimer",
        "value" : {
            templateId: "menu",
            interval: 0.2
        }
    },{
        "text" : "Timer - 0.5 sec - text only",
        "action" : "launchTimer",
        "value" : {
            templateId: "menu",
            interval: 0.5
        }
    },{
        "text" : "Timer - 1 sec - text only",
        "action" : "launchTimer",
        "value" : {
            templateId: "menu",
            interval: 1
        }
    },{
        "text" : "Timer - 1 sec - with 3 small images",
        "action" : "launchTimer",
        "value" : {
            templateId: "a_3",
            interval: 1
        }
    },{
        "text" : "Timer - 5 sec - text only",
        "action" : "launchTimer",
        "value" : {
            templateId: "menu",
            interval: 5
        }
    }]

    AQ.app.updateScreen( template );
}

TS.Display.showTimer = function(counter, templateId)
{
    // round up the counter to 2 decimal places
    counter = Math.round(counter * 100)/100;

    var template = $.extend(true, {}, TS.STATE[templateId]);

    template.template_content.header = "";

    template.template_content.list = [{
        "image1" : TS.IMAGES.a1,
        "image2" : TS.IMAGES.a2,
        "image3" : TS.IMAGES.a3,
        "text" : "Counter - "+counter
    }]

    AQ.app.updateScreen( template );
}