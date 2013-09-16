/**
 * NBC News app model
 *
 */

NBC.Model = {

    defaults : {
		getNewsTopicRequest : "mip_services/content/api/1.0/news/topics",
		getTopicStoryRequest : "mip_services/content/api/1.0/news/topic/%topicId%/stories?start=%start%&count=%count%",
		getNewsStoryRequest : "mip_services/content/api/1.0/news/story/%id%",
		requestHeaders : {
			"Content-Type" : "application/json",
			"Mip-Id" : "9ce6c2e4-4a64-11e2-8cc0-8957384cb92e",
			"hu-Id" : "ITG522233344466"
		}
    },

    /**
     * Gets the topics/categories
     */
    getNewsTopic : function(args)
    {
        var getNewsTopicRequest = this.defaults.getNewsTopicRequest;
        var data = {
            topics : 12 //all 12 topics
        }

        this.sendHttpRequest({
            data : data,
            url : getNewsTopicRequest,
            success : args.success,
            error : args.error
        });
    },

    /**
     * Gets the stories for the given topic/category
     */
    getTopicStory : function(args)
    {
        var topicId = args.data.topicId;
        var page = args.data.page || 1;
        var start = 1;
        var count = 0;

        //Manage pagination
        for( var i = 1; i <= page; i++ )
        {
            if(i == 1)
            {
                count = 19;
            }else if(i == 2)
            {
                start += 19;
                count = 18;
            }else
            {
                start += 18;
                count = 18;
            }

            console.log("Iteration", i, "; Start", start, "; Count", count);
        }


        var getTopicStoryRequest = "";
        getTopicStoryRequest = this.defaults.getTopicStoryRequest.replace("%topicId%", topicId);
        getTopicStoryRequest = getTopicStoryRequest.replace("%start%", start);
        getTopicStoryRequest = getTopicStoryRequest.replace("%count%", count);

        this.sendHttpRequest({
            url : getTopicStoryRequest,
            success : args.success,
            error : args.error
        });
    },

    /**
     * Gets the specific story details
     */
    getNewsStory : function(args)
    {
        var id = args.data.id;

        var getNewsStoryRequest = this.defaults.getNewsStoryRequest.replace("%id%", id);

        this.sendHttpRequest({
            url : getNewsStoryRequest,
            success : args.success,
            error : args.error
        });
    },

	/**
     * Shorten Date -- Shorten Date "08:30 PM UTC, Mon, Dec 10, 2012" To "Dec 10, 2012"
     */
    shortenDate: function(data){
		if(data){
			var obj = data.split(",");  //["09:38 AM UTC", " Tue", " Dec 11", " 2012"]
			return obj[2] + "," + obj[3];
		}else{
			return "";
		}
    },

    sendHttpRequest : function(args)
    {
        //url = encodeURIComponent(args.url);
        //console.log(url);

        //args.url = this.defaults.CHOREO + args.url;

        AQ.app.ajax({
            url : args.url,
            type : 'GET',
			//headers : this.defaults.requestHeaders,
            dataType: 'json',
            success : args.success,
            error : function(jqXHR){
                console.log("Error Message : " + JSON.stringify(jqXHR));
                args.error(jqXHR);
            }
        });
    }
}