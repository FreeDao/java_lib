/**
*
* Template B
*
**/
TS.Display.b_main = function()
{
    var template = $.extend(true, {}, TS.STATE.menu);

    template.template_content.list = [{
        "text" : "Empty Page",
        "action" : "loadPage",
        "value" : "b_0"
    },{
        "text" : "Policy: Disabled Page",
        "action" : "loadPage",
        "value" : "b_disabled"
    },{
        "text" : "Policy: Hidden Page",
        "action" : "loadPage",
        "value" : "b_hidden"
    },{
        "text" : "Policy: Mixed Page",
        "action" : "loadPage",
        "value" : "b_mixed"
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
        "text" : "Chinese Characters",
        "action" : "loadPage",
        "value" : "b_4"
    },{
        "text" : "Arabic Characters",
        "action" : "loadPage",
        "value" : "b_5"
    },{
        "text" : "Russian Characters",
        "action" : "loadPage",
        "value" : "b_6"
    }];

    AQ.app.updateScreen( template );
}

// Empty Page
TS.Display.b_0 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    // all buttons
    for(var i=1; i<=6; i++)
    {
        template.template_content.buttons[i] = {
        	"policy" : [],
            "image" : ""
        }
    }

    // template.template_content.header = "";
    template.template_content.image_left.value = "";
    template.template_content.text_left_bottom.value = "";
    template.template_content.text_left_top.value = "";
    template.template_content.text_right_main.value = "";

    AQ.app.updateScreen( template );
}

//short text - 56 characters
TS.Display.b_disabled = function()
{
    var template = $.extend(true, {}, TS.STATE.b_disabled);

    template.template_content.text_right_main.value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    AQ.app.updateScreen( template );
}

//short text - 56 characters
TS.Display.b_hidden = function()
{
    var template = $.extend(true, {}, TS.STATE.b_hidden);

    template.template_content.text_right_main.value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    AQ.app.updateScreen( template );
}

//short text - 56 characters
TS.Display.b_mixed = function()
{
    var template = $.extend(true, {}, TS.STATE.b_mixed);

    template.template_content.text_right_main.value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    AQ.app.updateScreen( template );
}

// short text - 56 characters
TS.Display.b_1 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    template.template_content.text_right_main.value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    AQ.app.updateScreen( template );
}

// Long text - 1,500 characters including spaces
TS.Display.b_2 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    template.template_content.text_right_main.value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam lorem metus, aliquam et suscipit eget, volutpat at odio. Etiam id nibh ac erat dignissim egestas non eget nisl. Quisque rutrum lacus non neque gravida at ornare orci dignissim. Praesent pellentesque mi bibendum sem dictum at tempus tortor pretium. Cras libero est, vulputate non commodo at, facilisis ut nisl. Suspendisse potenti. Mauris gravida rhoncus condimentum. Nam rutrum quam et mauris accumsan aliquam. Nam eget nisl eget nisl vehicula adipiscing vel ac nisi. Suspendisse mollis sagittis nibh quis auctor. Maecenas quis orci mi. Vestibulum tellus enim, consequat eget suscipit ut, vulputate nec massa. Quisque fermentum, ipsum at fermentum malesuada, metus dui luctus nunc, at. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam lorem metus, aliquam et suscipit eget, volutpat at odio. Etiam id nibh ac erat dignissim egestas non eget nisl. Quisque rutrum lacus non neque gravida at ornare orci dignissim. Praesent pellentesque mi bibendum sem dictum at tempus tortor pretium. Cras libero est, vulputate non commodo at, facilisis ut nisl. Suspendisse potenti. Mauris gravida rhoncus condimentum. Nam rutrum quam et mauris accumsan aliquam. Nam eget nisl eget nisl vehicula adipiscing vel ac nisi. Suspendisse mollis sagittis nibh quis auctor. Maecenas quis orci mi. Vestibulum tellus enim, consequat eget suscipit ut, vulputate nec massa. Quisque fermentum, ipsum at fermentum malesuada, metus dui luctus nunc, at";

    AQ.app.updateScreen( template );
}

// multi line text
TS.Display.b_3 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    template.template_content.text_right_main.value = "Line 1\n Line 2\n Line 3\n";

    AQ.app.updateScreen( template );
}

// random chinese characters
TS.Display.b_4 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    template.template_content.text_right_main.value = "譾躒 緅 笓粊紒 獫瘯皻, 逯郹酟 棰椻楒 諃 褅褌 鞜餲 輣鋄銶 鴸鼢曘 糋罶羬 楋 騩鰒鰔 軹軦軵 鶊鵱鶆 憢 裧頖 墆 絒翗 鑳鱨鱮 剆坲姏 椵楘溍 禖 堮婸 摬摙敳 茇茺苶. 摲 迗俀 鋱鋟鋈 畟痄笊 禒箈箑, 橁橖澭 厏吪吙 巂鞪 骱, 涀缹 禖 玾珆玸 迡俶倗 蕡蕇蕱 棦殔湝 脀蚅蚡 瞂 碡碙, 咥垵 爂犤繵 滭滹漇 滍, 濇燖燏 觛詏貁 鑴鱱爧 萶葠 憢 蛶 魆魦魵 瞗穇縍 裍裚 蟣襋 澂 磩磟窱 珝砯砨, 棰椻楒 碞碠粻 廞 蓪蔰 鱍鱕 嘕 獝瘝磈 鮂鮐嚃";

    AQ.app.updateScreen( template );
}

// random arabic characters
TS.Display.b_5 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    template.template_content.text_right_main.value = "الجيش وتزويده بحق ما. ٣٠ بالحرب لوكسيمبورج، بعد, على بـ حلّت أعمال. ٣٠ سقط شرسة وتعدد مقاومة. إذ إتفاقية المبرمة الجديدة، أسر, كل لهذه ترتيب ومدني، فقد. جوي دخول عشوائية الأبرياء هو, بل بحث تحرّك الإنذار،.. لم حاول ٢٠٠٤ والديون كما, كل أما الدمج اعتداء بالقصف, جهة هزيمة الحصار ما. سابق العصبة وفرنسا أسر أم, حول ووصف أواخر الرئيسية مع, بـ قام وتعدد العصبة بالسيطرة. لم لأداء النفط وهولندا، هذه, عن جندي الإعتداء فصل, إذ الا وبعدما منشوريا الأرواح. فرنسية وسمّيت أنجلو-فرنسية ذات و, هو الامم حادثة الثانية عام.";

    AQ.app.updateScreen( template );
}

// random russian characters
TS.Display.b_6 = function()
{
    var template = $.extend(true, {}, TS.STATE.b);

    template.template_content.text_right_main.value = "Новум альбюкиюс копиожаы ты нам, еюж нэ чонэт квюаэчтио юрбанйтаж. Квуым номинави зальютатуж нэ вим, ыт ыльит квюаэчтио трактатоз про, вим дэчырюёжжэ рыпримёквуы эа. Хаж трётанё дежпютатионй ан, квюоджё луптатум рэпудёандаэ йн ыам. Пюрто омныз ан пэр.";

    AQ.app.updateScreen( template );
}
/*** END OF TEMPLATE B ***/