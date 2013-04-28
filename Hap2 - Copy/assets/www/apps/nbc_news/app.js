/**
 * NBC News
 *
 *
 * @author: skutfiddinov
 */


log("NBC News app is initializing...");

//Make the APP variable globally accessible
var APP;
var NBC = {};

$.jsperanto.init(function(t) {
	AQ.app.loadScript(["constants.js", "generator.js", "model.js"], function()
	{
		APP = new NBC_news();
		APP.init();
	});
}, {
	lang : AQ.app.getLanguage(),
	dicoPath : "locales"
});


/**
 * Home app class
 */
function NBC_news()
{
    this.defaults = {
        currentTopics: [],
        currentTopicTitles: {},
        currentStories: {},
        currentPage: 1
    }

	/**
	 * Initialize the app and load the home screen
	 */
	this.init = function()
	{
		log("NBC News is initialized");
		this.loadHome();
	}

	/**
	 * Load home screen - list of apps
	 */
	this.loadHome = function()
	{
	    var that = this;

	    //Show the loading page
        NBC.Display.loading();

	    NBC.Model.getNewsTopic({
	        success: function(data)
	        {
	            log("News topics: "+ JSON.stringify(data));
	            that.defaults.currentTopics = data;

	            NBC.Display.home(data);
	        },
	        error: function(jqXHR, textStatus, errorThrown)
	        {
                log(textStatus +" - "+ errorThrown);
	            NBC.Display.error();
	        }
	    });

	    return;

	}

	/**
     * Load home screen - list of apps
     */
    this.loadTopic = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var page = data.page || 1;
        var topicId = data.topicId;
        var isRefresh = data.isRefresh || false;
        var currentPage = this.defaults.currentPage;

        if(topicId == 0){return;}
        var that = this;

        if( !isRefresh && that.defaults.currentTopicTitles[topicId] != null && currentPage == page )
        {
            NBC.Display.topic( that.defaults.currentTopicTitles[topicId] );
            return;
        }

        NBC.Display.loading();

        NBC.Model.getTopicStory({
            data:{
                topicId : topicId,
                page: page
            },
            success: function(resp)
            {
                if(typeof resp.error != "undefined")
                {
                    NBC.Display.error();
                    return;
                }

                log("Topic stories: "+ JSON.stringify(resp));
                resp.topicId = topicId;
                that.defaults.currentTopicTitles[topicId] = resp;
                that.defaults.currentPage = page;
                resp.page = page;
                NBC.Display.topic( resp );
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                log(textStatus +" - "+ errorThrown);
            }
        });

        return;
    }

    /**
     * Refresh the topics
     */
    this.refreshTopics = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var topicId = data.topicId;

        if(topicId == 0){return;}

        this.loadTopic( {topicId:topicId, isRefresh:true, page:page} );
    }

    /**
     * Load home screen - list of apps
     */
    this.loadStory = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var storyId = data.storyId;
        var topicId = data.topicId;

        if(storyId == 0){return;}
        var that = this;

        if( that.defaults.currentStories[storyId] != null )
        {
            NBC.Display.story( that.defaults.currentStories[storyId] );
            return;
        }

        NBC.Display.loading();

        NBC.Model.getNewsStory({
            data:{
                id : storyId
            },
            success: function(resp)
            {
                if(typeof resp.error != "undefined")
                {
                    NBC.Display.error();
                    return;
                }

                // Some content doesn't have space after dot, which is messing up TTS
                // add a space after each dot.
                resp.content = resp.content.replace('.', '. ');

                resp.topicId = topicId;
                resp.storyId = storyId;
                that.defaults.currentStories[storyId] = resp;

                NBC.Display.story( resp );
            },
            error: function(jqXHR)
            {
                console.error(jqXHR);
            }
        });
    }

    /**
     * Loads the next story
     */
    this.loadNextStory = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var currentStoryId = data.currentStoryId;
        var topicId = data.topicId;

        var topics = this.defaults.currentTopicTitles[ topicId ].newsStories;

        for(var i=0; i<topics.length; i++)
        {
            if(topics[i].storyId == currentStoryId)
            {
                if( typeof topics[i+1] != "undefined" )
                {
                    NBC.Display.historyRemove();
                    this.loadStory( {topicId:topicId, storyId:topics[i+1].storyId} );
                    return;
                }else{
                    NBC.Display.historyRemove();
                    this.loadStory( {topicId:topicId, storyId:topics[0].storyId} );
                    return;
                }
            }
        }
    }

    /**
     * Loads the next story
     */
    this.loadPrevStory = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var currentStoryId = data.currentStoryId;
        var topicId = data.topicId;

        var topics = this.defaults.currentTopicTitles[ topicId ].newsStories;

        for(var i=0; i<topics.length; i++)
        {
            if(topics[i].storyId == currentStoryId)
            {
                if( typeof topics[i-1] != "undefined" )
                {
                    NBC.Display.historyRemove();
                    this.loadStory( {topicId:topicId, storyId:topics[i-1].storyId} );
                    return;
                }else{
                    NBC.Display.historyRemove();
                    this.loadStory( {topicId:topicId, storyId:topics[ topics.length -1 ].storyId} );
                    return;
                }
            }
        }
    }

    /**
     * Load home screen - list of apps
     */
    this.loadShare = function()
    {
        NBC.Display.share();
    }

	/**
     * Handle the back button
     */
    this.back = function(data)
    {
        NBC.Display.historyBack(data);
    }

    this.playTTS = function(text){
        AQ.respond.processTts(text);
    }
    this.TTS_READY = function( resp ){
        AQ.respond.playTts(resp.ttsId);
    }
    this.TTS_END = function( resp ){
        // TODO show some notification
        log("TTS is finished for id: "+resp.ttsId);
    }

	/**
	 * Handles the actions
	 */
	this.handleAction = function( action, value )
	{
	    this[action](value);

	    return;

        //TODO: for production, use try/catch
		// try{
		// 	this[action](value);
		// }catch(err){
		// 	log("Unable to complete action: "+action);
		// 	log(err);
		// }
	}
}//end of contructor