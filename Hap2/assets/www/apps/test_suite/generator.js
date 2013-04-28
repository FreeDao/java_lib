TS.Display = {
    main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Template Display",
            "action" : "loadPage",
            "value" : "templates_main"
        },{
            "text" : "Template Transitions",
            "action" : "loadPage",
            "value" : "transitions_main"
        }]

        AQ.app.updateScreen( template );
    },


    transitions_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Timer - 0.5 sec - text only",
            "action" : "launchTimer",
            "value" : {
                templateId: "c",
                interval: 0.5
            }
        },{
            "text" : "Timer - 1 sec - text only",
            "action" : "launchTimer",
            "value" : {
                templateId: "c",
                interval: 1
            }
        },{
            "text" : "Timer - 1 sec - with 3 same images",
            "action" : "launchTimer",
            "value" : {
                templateId: "a_3",
                interval: 1
            }
        }]

        AQ.app.updateScreen( template );
    },

    showTimer : function(counter, templateId)
    {
        // round up the counter to 2 decimal places
        counter = Math.round(counter * 100)/100;

        var template = $.extend(true, {}, TS.STATE[templateId]);

        try{
            template.template_content.header = "";
        }catch(e){}

        template.template_content.list = [{
            "image1" : TS.IMAGES.a1,
            "image2" : TS.IMAGES.a2,
            "image3" : TS.IMAGES.a3,
            "text" : "Counter - "+counter
        }]

        AQ.app.updateScreen( template );
    },


    /**
    *
    * Main templates page
    *
    **/
    templates_main : function()
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
        },{
            "text" : "Template D",
            "action" : "loadPage",
            "value" : "d_main"
        },{
            "text" : "Template E",
            "action" : "loadPage",
            "value" : "e_main"
        },{
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
        }]

        AQ.app.updateScreen( template );
    },

    /**
    *
    * GENERAL SCREENS
    *
    **/
    general_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);
        template.template_content.list = [{
            "text" : "Using local image",
            "action" : "loadPage",
            "value" : "general_1"
        },{
            "text" : "4 same text strings",
            "action" : "loadPage",
            "value" : "general_2"
        }];

        AQ.app.updateScreen( template );
    },

    general_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        // set the active key to 3
        template.template_content.active_key = 3;

        template.template_content.header = TS.IMAGES.localHeader;

        template.template_content.list.push({
            "text" : "Header image is local from HAP"
        });

        AQ.app.updateScreen( template );
    },

    // 4 same List items
    general_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        // 4 list items
        for(var i=1; i<=4; i++)
        {
            template.template_content.list.push({
                "text" : "All list items are same"
            });
        }

        AQ.app.updateScreen( template );
    },
    /*** END OF GENERAL SCREENS ***/


    /**
    *
    * Template A 1
    *
    **/
    a_1_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);
        template.template_content.list = [{
            "text" : "Empty page",
            "action" : "loadPage",
            "value" : "a_1_0"
        },{
            "text" : "4 items",
            "action" : "loadPage",
            "value" : "a_1_1"
        },{
            "text" : "20 items;",
            "action" : "loadPage",
            "value" : "a_1_2"
        },{
            "text" : "20 items; 6 - active key;",
            "action" : "loadPage",
            "value" : "a_1_3"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    a_1_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.a_1);

        template.template_content.header = "";

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
    },

    // 4 items
    a_1_1 : function()
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
    },

    // 20 items; text and image hatching - gray; button hatching - black;
    a_1_2 : function()
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
    },

    // 20 items; text and image hatching - some gray; button hatching - gray; active key is 6;
    a_1_3 : function()
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
    },
    /*** END OF TEMPLATE A 1 ***/


    /**
    *
    * Template A 2
    *
    **/
    a_2_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);
        template.template_content.list = [{
            "text" : "Empty page",
            "action" : "loadPage",
            "value" : "a_2_0"
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
    },

    // Empty Page
    a_2_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.a_2);

        template.template_content.header = "";

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
    },

    // 4 items
    a_2_1 : function()
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
    },

    // 20 items; text and image hatching - gray; button hatching - black;
    a_2_2 : function()
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
    },

    // 20 items; text and image hatching - some gray; button hatching - gray; active key is 6;
    a_2_3 : function()
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
    },
    /*** END OF TEMPLATE A 2 ***/


    /**
    *
    * Template A 3
    *
    **/
    a_3_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);
        template.template_content.list = [{
            "text" : "Empty page",
            "action" : "loadPage",
            "value" : "a_3_0"
        },{
            "text" : "4 items",
            "action" : "loadPage",
            "value" : "a_3_1"
        },{
            "text" : "20 items;",
            "action" : "loadPage",
            "value" : "a_3_2"
        },{
            "text" : "20 items; 6 - active key;",
            "action" : "loadPage",
            "value" : "a_3_3"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    a_3_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.a_3);

        template.template_content.header = "";

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
                "image3" : "",
                "text" : ""
            });
        }

        AQ.app.updateScreen( template );
    },

    // 4 items
    a_3_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.a_3);

        // 4 list items
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
    },

    // 20 items; text and image hatching - gray; button hatching - black;
    a_3_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.a_3);

        // 20 list items
        // TODO: add hatching info
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
    },

    // 20 items; text and image hatching - some gray; button hatching - gray; active key is 6;
    a_3_3 : function()
    {
        var template = $.extend(true, {}, TS.STATE.a_3);

        // set the active key to 6
        template.template_content.active_key = 6;

        // 20 list items
        // TODO: add hatching info
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
    },
    /*** END OF TEMPLATE A 3 ***/


    /**
    *
    * Template B
    *
    **/
    b_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "b_0"
        },{
            "text" : "Short text",
            "action" : "loadPage",
            "value" : "b_1"
        },{
            "text" : "Max text",
            "action" : "loadPage",
            "value" : "b_2"
        },{
            "text" : "Multi-line",
            "action" : "loadPage",
            "value" : "b_3"
        },{
            "text" : "Random Chinese Characters",
            "action" : "loadPage",
            "value" : "b_4"
        },{
            "text" : "Random Arabic Characters",
            "action" : "loadPage",
            "value" : "b_5"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    b_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.b);

        // all buttons
        for(var i=1; i<=6; i++)
        {
            template.template_content.buttons[i] = {
                "image" : ""
            }
        }

        template.template_content.header = "";
        template.template_content.image_left = "";
        template.template_content.text_left_bottom = "";        
        template.template_content.text_left_top = "";
        template.template_content.text_right_main = "";

        AQ.app.updateScreen( template );
    },

    // short text - 56 characters
    b_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.b);

        template.template_content.text_right_main = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

        AQ.app.updateScreen( template );
    },

    // Long text - 1,500 characters including spaces
    b_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.b);

        template.template_content.text_right_main = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam lorem metus, aliquam et suscipit eget, volutpat at odio. Etiam id nibh ac erat dignissim egestas non eget nisl. Quisque rutrum lacus non neque gravida at ornare orci dignissim. Praesent pellentesque mi bibendum sem dictum at tempus tortor pretium. Cras libero est, vulputate non commodo at, facilisis ut nisl. Suspendisse potenti. Mauris gravida rhoncus condimentum. Nam rutrum quam et mauris accumsan aliquam. Nam eget nisl eget nisl vehicula adipiscing vel ac nisi. Suspendisse mollis sagittis nibh quis auctor. Maecenas quis orci mi. Vestibulum tellus enim, consequat eget suscipit ut, vulputate nec massa. Quisque fermentum, ipsum at fermentum malesuada, metus dui luctus nunc, at. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam lorem metus, aliquam et suscipit eget, volutpat at odio. Etiam id nibh ac erat dignissim egestas non eget nisl. Quisque rutrum lacus non neque gravida at ornare orci dignissim. Praesent pellentesque mi bibendum sem dictum at tempus tortor pretium. Cras libero est, vulputate non commodo at, facilisis ut nisl. Suspendisse potenti. Mauris gravida rhoncus condimentum. Nam rutrum quam et mauris accumsan aliquam. Nam eget nisl eget nisl vehicula adipiscing vel ac nisi. Suspendisse mollis sagittis nibh quis auctor. Maecenas quis orci mi. Vestibulum tellus enim, consequat eget suscipit ut, vulputate nec massa. Quisque fermentum, ipsum at fermentum malesuada, metus dui luctus nunc, at";

        AQ.app.updateScreen( template );
    },

    // multi line text
    b_3 : function()
    {
        var template = $.extend(true, {}, TS.STATE.b);

        template.template_content.text_right_main = "Line 1\n Line 2\n Line 3\n";

        AQ.app.updateScreen( template );
    },

    // random chinese characters
    b_4 : function()
    {
        var template = $.extend(true, {}, TS.STATE.b);

        template.template_content.text_right_main = "譾躒 緅 笓粊紒 獫瘯皻, 逯郹酟 棰椻楒 諃 褅褌 鞜餲 輣鋄銶 鴸鼢曘 糋罶羬 楋 騩鰒鰔 軹軦軵 鶊鵱鶆 憢 裧頖 墆 絒翗 鑳鱨鱮 剆坲姏 椵楘溍 禖 堮婸 摬摙敳 茇茺苶. 摲 迗俀 鋱鋟鋈 畟痄笊 禒箈箑, 橁橖澭 厏吪吙 巂鞪 骱, 涀缹 禖 玾珆玸 迡俶倗 蕡蕇蕱 棦殔湝 脀蚅蚡 瞂 碡碙, 咥垵 爂犤繵 滭滹漇 滍, 濇燖燏 觛詏貁 鑴鱱爧 萶葠 憢 蛶 魆魦魵 瞗穇縍 裍裚 蟣襋 澂 磩磟窱 珝砯砨, 棰椻楒 碞碠粻 廞 蓪蔰 鱍鱕 嘕 獝瘝磈 鮂鮐嚃";

        AQ.app.updateScreen( template );
    },

    // random arabic characters
    b_5 : function()
    {
        var template = $.extend(true, {}, TS.STATE.b);

        template.template_content.text_right_main = "الجيش وتزويده بحق ما. ٣٠ بالحرب لوكسيمبورج، بعد, على بـ حلّت أعمال. ٣٠ سقط شرسة وتعدد مقاومة. إذ إتفاقية المبرمة الجديدة، أسر, كل لهذه ترتيب ومدني، فقد. جوي دخول عشوائية الأبرياء هو, بل بحث تحرّك الإنذار،.. لم حاول ٢٠٠٤ والديون كما, كل أما الدمج اعتداء بالقصف, جهة هزيمة الحصار ما. سابق العصبة وفرنسا أسر أم, حول ووصف أواخر الرئيسية مع, بـ قام وتعدد العصبة بالسيطرة. لم لأداء النفط وهولندا، هذه, عن جندي الإعتداء فصل, إذ الا وبعدما منشوريا الأرواح. فرنسية وسمّيت أنجلو-فرنسية ذات و, هو الامم حادثة الثانية عام.";

        AQ.app.updateScreen( template );
    },
    /*** END OF TEMPLATE B ***/


    /**
    *
    * Template C
    *
    **/
    c_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "c_0"
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
    },

    // Empty page
    c_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.c);

        template.template_content.header = "";

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
                "text" : ""
            });
        }

        AQ.app.updateScreen( template );
    },

    // 3 List items
    c_1 : function()
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
    },

    // 20 List items
    c_2 : function()
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
    },
    /*** END OF TEMPLATE C ***/


    /**
    *
    * Template D
    *
    **/
    d_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "d_0"
        },{
            "text" : "3 items",
            "action" : "loadPage",
            "value" : "d_1"
        },{
            "text" : "20 items",
            "action" : "loadPage",
            "value" : "d_2"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    d_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.d);

        template.template_content.header = "";
        template.template_content.image_left = "";

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
                "text" : "",
                "image" : ""
            });
        }

        AQ.app.updateScreen( template );
    },


    // 3 List items
    d_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.d);

        // 3 list items
        for(var i=1; i<=3; i++)
        {
            template.template_content.list.push({
                "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i,
                "image" : TS.IMAGES.a1
            });
        }

        AQ.app.updateScreen( template );
    },

    // 20 List items
    d_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.d);

        // 20 list items
        for(var i=1; i<=20; i++)
        {
            template.template_content.list.push({
                "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i,
                "image" : TS.IMAGES.a1
            });
        }

        AQ.app.updateScreen( template );
    },
    /*** END OF TEMPLATE D ***/


    /**
    *
    * Template E
    *
    **/
    e_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "e_0"
        },{
            "text" : "Regular Image",
            "action" : "loadPage",
            "value" : "e_1"
        },{
            "text" : "Large Image - ~950KB",
            "action" : "loadPage",
            "value" : "e_2"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    e_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.e);

        // all buttons
        for(var i=1; i<=6; i++)
        {
            template.template_content.buttons[i] = {
                "image" : ""
            }
        }

        template.template_content.header = "";
        template.template_content.image_main = "";

        AQ.app.updateScreen( template );
    },

    // short text - 56 characters
    e_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.e);

        AQ.app.updateScreen( template );
    },


    // short text - 56 characters
    e_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.e);

        // 950KB Image
        template.template_content.image_main = "http://farm9.staticflickr.com/8084/8401825180_0ccd27acd4_k.jpg";

        AQ.app.updateScreen( template );
    },
    /*** END OF TEMPLATE E ***/


    /**
    *
    * Template F
    *
    **/
    f_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "f_0"
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
    },

    // Empty Page
    f_0 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);

        template.template_content.header = "";
        template.template_content.text_main = "";

        AQ.app.updateScreen( template );
    },

    // no selection
    f_1 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);

        AQ.app.updateScreen( template );
    },

    // all selection
    f_2 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);

        template.template_content.text_bottom[1] = { text: "TX1" };
        template.template_content.text_bottom[2] = { text: "TX2" };
        template.template_content.text_bottom[3] = { text: "TX3" };

        AQ.app.updateScreen( template );
    },

    // selection 1
    f_3 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);
        template.template_content.text_bottom[1] = { text: "TX1" };
        AQ.app.updateScreen( template );
    },

    // selection 2
    f_4 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);
        template.template_content.text_bottom[2] = { text: "TX2" };
        AQ.app.updateScreen( template );
    },

    // selection 3
    f_5 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);
        template.template_content.text_bottom[3] = { text: "TX3" };
        AQ.app.updateScreen( template );
    },

    // selection 1 and 2
    f_6 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);
        template.template_content.text_bottom[1] = { text: "TX1" };
        template.template_content.text_bottom[2] = { text: "TX2" };
        AQ.app.updateScreen( template );
    },

    // selection 2 and 3
    f_7 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);
        template.template_content.text_bottom[2] = { text: "TX2" };
        template.template_content.text_bottom[3] = { text: "TX3" };
        AQ.app.updateScreen( template );
    },

    // selection 1 and 3
    f_8 : function( data )
    {
        var template = $.extend(true, {}, TS.STATE.f);
        template.template_content.text_bottom[1] = { text: "TX1" };
        template.template_content.text_bottom[3] = { text: "TX3" };
        AQ.app.updateScreen( template );
    },
    /*** END OF TEMPLATE F ***/


    /**
    *
    * Template G_1
    *
    **/
    g_1_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "g_1_0"
        },{
            "text" : "4 items",
            "action" : "loadPage",
            "value" : "g_1_1"
        },{
            "text" : "20 items",
            "action" : "loadPage",
            "value" : "g_1_2"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    g_1_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_1);

        template.template_content.header = "";

        // 20 list items
        for(var i=1; i<=20; i++)
        {
            template.template_content.list.push({
                "image1" : "",
                "text" : ""
            });
        }

        AQ.app.updateScreen( template );
    },

    // 3 List items
    g_1_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_1);

        // 3 list items
        for(var i=1; i<=4; i++)
        {
            template.template_content.list.push({
                "image1" : TS.IMAGES.a1,
                "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
            });
        }

        AQ.app.updateScreen( template );
    },

    // 20 List items
    g_1_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_1);

        // 20 list items
        for(var i=1; i<=20; i++)
        {
            template.template_content.list.push({
                "image1" : TS.IMAGES.a1,
                "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
            });
        }

        AQ.app.updateScreen( template );
    },
    /*** END OF TEMPLATE G_1 ***/


    /**
    *
    * Template G_2
    *
    **/
    g_2_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "g_2_0"
        },{
            "text" : "4 items",
            "action" : "loadPage",
            "value" : "g_2_1"
        },{
            "text" : "20 items",
            "action" : "loadPage",
            "value" : "g_2_2"
        }];

        AQ.app.updateScreen( template );
    },

    // Empty Page
    g_2_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_2);

        template.template_content.header = "";
        
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
    },

    // 3 List items
    g_2_1 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_2);

        // 3 list items
        for(var i=1; i<=4; i++)
        {
            template.template_content.list.push({
                "image1" : TS.IMAGES.a1,
                "image2" : TS.IMAGES.a2,
                "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
            });
        }

        AQ.app.updateScreen( template );
    },

    // 20 List items
    g_2_2 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_2);

        // 20 list items
        for(var i=1; i<=20; i++)
        {
            template.template_content.list.push({
                "image1" : TS.IMAGES.a1,
                "image2" : TS.IMAGES.a2,
                "text" : "Text "+i+" - Text "+i+" - Text "+i+" - Text "+i+" - Text "+i
            });
        }

        AQ.app.updateScreen( template );
    },
    /*** END OF TEMPLATE G_2 ***/


    /**
    *
    * Template G_3
    *
    **/
    g_3_main : function()
    {
        var template = $.extend(true, {}, TS.STATE.menu);

        template.template_content.list = [{
            "text" : "Empty Page",
            "action" : "loadPage",
            "value" : "g_3_0"
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
    },

    // Empty Page
    g_3_0 : function()
    {
        var template = $.extend(true, {}, TS.STATE.g_3);

        template.template_content.header = "";
        
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
    },

    // 3 List items
    g_3_1 : function()
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
    },

    // 20 List items
    g_3_2 : function()
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
    },
    /*** END OF TEMPLATE G_3 ***/
}