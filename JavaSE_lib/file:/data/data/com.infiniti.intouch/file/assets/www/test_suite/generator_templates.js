/**
*
* Main templates page
*
**/
TS.Display.templates_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "General screens",
        "action" : "loadPage",
        "value" : "general_main"
    },{
        "text" : "Template A_1",
        "action" : "loadPage",
        "value" : "a_1_main"
    },{
        "text" : "Template A_2",
        "action" : "loadPage",
        "value" : "a_2_main"
    },{
        "text" : "Template A_3",
        "action" : "loadPage",
        "value" : "a_3_main"
    },{
        "text" : "Template B",
        "action" : "loadPage",
        "value" : "b_main"
    },{
        "text" : "Template C",
        "action" : "loadPage",
        "value" : "c_main"
    }/*,{
        "text" : "Template D",
        "action" : "loadPage",
        "value" : "d_main"
    },{
        "text" : "Template E",
        "action" : "loadPage",
        "value" : "e_main"
    }*/,{
        "text" : "Template F",
        "action" : "loadPage",
        "value" : "f_main"
    },{
        "text" : "Template G - List 1",
        "action" : "loadPage",
        "value" : "g_1_main"
    },{
        "text" : "Template G - List 2",
        "action" : "loadPage",
        "value" : "g_2_main"
    },{
        "text" : "Template G - List 3",
        "action" : "loadPage",
        "value" : "g_3_main"
    },{
        "text" : "Template H",
        "action" : "loadPage",
        "value" : "h_main"
    },{
        "text" : "Template J",
        "action" : "loadPage",
        "value" : "j_main"
    }]

    AQ.app.updateScreen( template );
}
