/**
 * Template generator - populates the template data and updates the screen
 *
 * @author: skutfiddinov
 */

NBC.Display = {
    defaults: {
    	ttsStatus:{
			"isplaying" : false,
			"text" : null
    	},
        historyWrapper: []
    },

    /**
     * Main home page - list of categories
     */
    home: function(data)
    {
        this.historyReset();

        this.historySave({page:'home', value:data});

        var template = $.extend(true, {}, NBC.STATE.home);

        for(var i=0; i<data.length; i++)
        {
            var topicId = data[i].topicId;
            var imageId = "cat_" + topicId;
            var image = typeof NBC.IMAGES.list[imageId] == "undefined" ? NBC.IMAGES.list.cat_default : NBC.IMAGES.list[imageId];

            template.template_content.list.push({
                "image1" : image,
                "text" : data[i].name,
                "action" : "loadTopic",
                "value" : JSON.stringify({ topicId:topicId, page: 1 })
            });
        }

        AQ.app.updateScreen( template );
    },


    /**
     * Topic page - list of stories in the category
     */
    topic: function(data)
    {
        this.historySave({page:'topic', value:data});

        var template = $.extend(true, {}, NBC.STATE.topic);
        var topicId = data.topicId;
        var page = data.page || 1;

        for(var i=0; i<data.newsStories.length; i++)
        {
            var storyId = data.newsStories[i].storyId;
            var text = data.newsStories[i].title;

            template.template_content.list.push({
                "text" : text,
                "action" : "loadStory",
                "value" : JSON.stringify( {storyId:storyId, topicId:topicId} )
            });
        }

        if(page > 1)
        {
            template.template_content.list.unshift({
                "text" : $.t("previous"),
                "action" : "loadTopic",
                "value" : JSON.stringify( {topicId:topicId, page:(page-1)} )
            });
        }

        if(data.newsStories.length == 19)
        {
            template.template_content.list.push({
                "text" : $.t("next"),
                "action" : "loadTopic",
                "value" : JSON.stringify( {topicId:topicId, page:(page+1)} )
            });
        }else if(data.newsStories.length > 19)
        {
            template.template_content.list[19] = {
                "text" : $.t("next"),
                "action" : "loadTopic",
                "value" : JSON.stringify( {topicId:topicId, page:(page+1)} )
            };
        }

        template.template_content.buttons[1] = {
            "image" : NBC.IMAGES.button['1_refresh'],
            "action" : "refreshTopics",
            "value" : JSON.stringify( {topicId:topicId, page:page} )
        }

        // Removed by Sherzod: currently Nissan is discussing about whether to have this functionality here or not.
        // template.template_content.buttons[2] = {
        //     "image" : NBC.IMAGES.button['2_tts']
        // }

        AQ.app.updateScreen( template );
    },


    /**
     * Story page
     */
    story: function(data)
    {
        this.historySave({page:'story', value:data});

        var template = $.extend(true, {}, NBC.STATE.story);

        template.template_content.text_left_top.value = data.title || "";
        //template.template_content.text_left_bottom = data.publishDate || "";
		template.template_content.text_left_bottom.value = NBC.Model.shortenDate(data.publishDate) || "";//Shorten Date "08:30 PM UTC, Mon, Dec 10, 2012" To "Dec 10, 2012"
        template.template_content.text_right_main.value = data.content || "";
        template.template_content.image_left.value = data.imageURL || null;
        
        var TTStext = template.template_content.text_left_top.value +". "+ template.template_content.text_right_main.value;
        var ttsImage = (NBC.Display.defaults.ttsStatus.isplaying && NBC.Display.defaults.ttsStatus.text == TTStext)? NBC.IMAGES.button['1_tts_active'] : NBC.IMAGES.button['1_tts'];
        template.template_content.buttons["1"] = {
            "image" : ttsImage,
            "action" : "playTTS",
            "value" : TTStext
        };
        
        
        template.template_content.buttons["2"] = {
            "image" : NBC.IMAGES.button['2_prev'],
            "action" : "loadPrevStory",
            "value" : JSON.stringify( {currentStoryId:data.storyId, topicId:data.topicId} )
        };
        template.template_content.buttons["3"] = {
            "image" : NBC.IMAGES.button['3_next'],
            "action" : "loadNextStory",
            "value" : JSON.stringify( {currentStoryId:data.storyId, topicId:data.topicId} )
        };
        template.template_content.buttons["4"] = {
            "image" : NBC.IMAGES.button['4_share'],
            "action" : "loadShare"
        };
        template.template_content.buttons["5"] = {
            "image" : NBC.IMAGES.button['5_up']
        };
        template.template_content.buttons["6"] = {
            "image" : NBC.IMAGES.button['6_down']
        };

        AQ.app.updateScreen( template );
    },
    
    reloadStoryPage:function(isTTSplaying,TTStext)
    {
    	NBC.Display.defaults.ttsStatus = {
    			"isplaying" : isTTSplaying,
    			"text" : TTStext
    	};
        NBC.Display.defaults.historyWrapper.push({});
        this.historyBack();
    },

    /**
     * Share page - sharing in facebook & twitter
     */
    share: function(data)
    {
        this.historySave({page:'share', value:data});

        var template = $.extend(true, {}, NBC.STATE.share);

        AQ.app.updateScreen( template );
    },

    /**
    * Display the platform loading screen
    **/
    loading: function()
    {
        AQ.app.showLoading();
    },

    error: function()
    {
        var template = $.extend(true, {}, NBC.STATE.error);

        AQ.app.updateScreen( template );
    },

    /**
     * Save the page in the history
     */
    historySave : function(history)
    {
        //If the previous page was the temp history page, remove it from the history and relaunch the function
        if( NBC.Display.defaults.historyWrapper.length > 1 && NBC.Display.defaults.historyWrapper[NBC.Display.defaults.historyWrapper.length-1].isTemp === true )
        {
            NBC.Display.defaults.historyWrapper.pop();
            NBC.Display.historySave(history);
            return;
        }


        if( NBC.Display.defaults.historyWrapper.length == 0 )
        {
            NBC.Display.defaults.historyWrapper.push( history );
        }
        else if( NBC.Display.defaults.historyWrapper[NBC.Display.defaults.historyWrapper.length-1].page != history.page )
        {
            NBC.Display.defaults.historyWrapper.push( history );
        }else
        {
            NBC.Display.defaults.historyWrapper[ NBC.Display.defaults.historyWrapper.length -1 ] = history;
        }

        return;
    },
    
    /**
     * Reset the history
     */
    historyReset : function()
    {
        NBC.Display.defaults.historyWrapper = [];
    },

    /**
     * Removes the number of history items starting with the newest
     */
    historyRemove : function(num)
    {
        num = num || 1;

        for(var i=0; i<num; i++)
        {
            NBC.Display.defaults.historyWrapper.pop();
        }
    },

    getHistory: function(index)
    {
        index = index || 0;

        index = (NBC.Display.defaults.historyWrapper.length - 1) - index;

        return NBC.Display.defaults.historyWrapper[index];
    },

    /**
     * Move back in the history and load the previous page
     */
    historyBack : function()
    {
        //Remove the current page from the history array
        NBC.Display.defaults.historyWrapper.pop();

        if(NBC.Display.defaults.historyWrapper.length < 1){
            AQ.app.load("home");
            return;
        }

        var history = NBC.Display.defaults.historyWrapper.pop();

        switch(history.page)
        {
            case "home":
                NBC.Display.home( history.value );
                break;

            case "topic":
                NBC.Display.topic( history.value );
                break;

            case "story":
                NBC.Display.story( history.value );
                break;

            case "share":
                NBC.Display.share( history.value );
                break;

            default:
                return;
        }
    }
}
