/**
*
* Template F
*
**/
TS.Display.f_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "f_0"
    },{
        "text" : "Mixed Policy Page",
        "action" : "loadPage",
        "value" : "f_policy"
    },{
        "text" : "No selection",
        "action" : "loadPage",
        "value" : "f_1"
    },{
        "text" : "All selection",
        "action" : "loadPage",
        "value" : "f_2"
    },{
        "text" : "Selection 1",
        "action" : "loadPage",
        "value" : "f_3"
    },{
        "text" : "Selection 2",
        "action" : "loadPage",
        "value" : "f_4"
    },{
        "text" : "Selection 3",
        "action" : "loadPage",
        "value" : "f_5"
    },{
        "text" : "Selection 1 and 2",
        "action" : "loadPage",
        "value" : "f_6"
    },{
        "text" : "Selection 2 and 3",
        "action" : "loadPage",
        "value" : "f_7"
    },{
        "text" : "Selection 1 and 3",
        "action" : "loadPage",
        "value" : "f_8"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.f_0 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);

    // template.template_content.header = "";
    template.template_content.text_main = "";

    AQ.app.updateScreen( template );
}

// no selection
TS.Display.f_1 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);

    AQ.app.updateScreen( template );
}

TS.Display.f_policy = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f_policy);

    template.template_content.text_bottom[1] = { policy : ["ELEMENT.HIDDEN_WHILE_DRIVING"], text: "Hidden", action:"test", value:"option 1 pressed" };
    template.template_content.text_bottom[2] = { policy : ["ELEMENT.DISABLED_WHILE_DRIVING"], text: "Disable", action:"test", value:"option 2 pressed" };
    template.template_content.text_bottom[3] = { policy : ["ELEMENT.HIDDEN_WHILE_DRIVING"], text: "Hidden", action:"test", value:"option 3 pressed" };

    AQ.app.updateScreen( template );
}
// all selection
TS.Display.f_2 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);

    template.template_content.text_bottom[1] = { policy : [], text: "TX1", action:"test", value:"option 1 pressed" };
    template.template_content.text_bottom[2] = { text: "TX2", action:"test", value:"option 2 pressed" };
    template.template_content.text_bottom[3] = { text: "TX3", action:"test", value:"option 3 pressed" };

    AQ.app.updateScreen( template );
}

// selection 1
TS.Display.f_3 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);
    template.template_content.text_bottom[1] = { text: "TX1", action:"test", value:"option 1 pressed" };
    AQ.app.updateScreen( template );
}

// selection 2
TS.Display.f_4 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);
    template.template_content.text_bottom[2] = { text: "TX2", action:"test", value:"option 2 pressed" };
    AQ.app.updateScreen( template );
}

// selection 3
TS.Display.f_5 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);
    template.template_content.text_bottom[3] = { text: "TX3", action:"test", value:"option 3 pressed" };
    AQ.app.updateScreen( template );
}

// selection 1 and 2
TS.Display.f_6 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);
    template.template_content.text_bottom[1] = { text: "TX1", action:"test", value:"option 1 pressed" };
    template.template_content.text_bottom[2] = { policy : [], text: "TX2", action:"test", value:"option 2 pressed" };
    AQ.app.updateScreen( template );
}

// selection 2 and 3
TS.Display.f_7 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);
    template.template_content.text_bottom[2] = { text: "TX2", action:"test", value:"option 2 pressed" };
    template.template_content.text_bottom[3] = { text: "TX3", action:"test", value:"option 3 pressed" };
    AQ.app.updateScreen( template );
}

// selection 1 and 3
TS.Display.f_8 = function( data )
{
    var template = $.extend(true, {}, TS.STATE.f);
    template.template_content.text_bottom[1] = { text: "TX1", action:"test", value:"option 1 pressed" };
    template.template_content.text_bottom[3] = { text: "TX3", action:"test", value:"option 3 pressed" };
    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE F ***/