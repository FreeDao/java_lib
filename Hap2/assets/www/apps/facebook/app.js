log("Facebook app is initializing...");

var APP;
var FB = {};

$.jsperanto.init(function(t)
{
	AQ.app.loadScript(["util.js", "cache.js", "constants.js", "generator.js", "model.js"], function()
    {
		APP = new Facebook();
		APP.init();
	});
}, {
	lang : AQ.app.getLanguage(),
	dicoPath : "locales"
});

/**
 * Facebook app class
 */
 function Facebook()
 {
    /**
    * Initialize the app and respond the first page
    **/
    this.init = function()
    {
		log("Facebook Initialized");
        this.loadHome();
    };

    /**
    * Initialize the app and respond the first page
    **/
    this.loadHome = function()
    {
        FB.Display.home();
    };

    /**
    * Display the news feed items
    **/
    this.loadNewsFeed = function()
    {
        var args = {
            "data" : {
                "maxMessages" : "20",
                "includeImages" : true
            },
            "success" : function(data){
                var viewData = {
                    "refreshAction" : "loadNewsFeed",
                    "posts" : data
                };
                FB.Cache.saveNewsFeedItems({
                    data: data.data,
                    paging: data.paging
                });
                FB.Cache.saveImage(data.images);
                FB.Display.postList(viewData);
            }
        };
        FB.Display.showLoading();
        FB.Model.getLiveFeed(args);
        return;
    };

    /**
    * Show News Feed Item Details Page
    **/
    this.loadNewsDetails= function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var newsFeedItem = FB.Cache.getNewsFeedItems( data.postId );

        FB.Display.showPostDetail( newsFeedItem );
        return;
    };


    /**
    * Show pre-set comments
    **/
    this.loadPresetComments = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var postId = data.postId;
        var presetComments = FB.DEFAULTS.presetComments;
        FB.Display.showPresetCommentList({data:presetComments, postId:postId});
        return;
    };

    this.loadCommentList = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}
		if(data.type=="checkin"){
			var checkinComments = FB.Cache.getNearbyFriendsCheckinComments( {checkin_id:data.checkinId} );
			FB.Display.showCommentList({type : "checkin", comments:checkinComments, id:data.checkinId});
		}else{//data.type=="post"
			var newsFeedComments = FB.Cache.getNewsFeedComments( {postId:data.postId} );
			FB.Display.showCommentList({type : "post",comments:newsFeedComments, id:data.postId});
		}
        return;
    };

    this.loadCommentDetails = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}
        var comment = {};
		if(data.type == "checkin"){
			comment = FB.Cache.getNearbyFriendsCheckinCommentbyId( {checkinId:data.id, commentId:data.commentId} );
		}else{
			comment = FB.Cache.getNewsFeedComments( {postId:data.id, commentId:data.commentId} );
		}
        FB.Display.showCommentDetail(comment);
        return;
    };

    this.loadPostCommentConfirm = function(data){
        try{
            data = JSON.parse(data);
        }catch(e){}

        var message = {
            "msg" : $.t("post_Confirm").replace("[post_message]",data.message),
            "confirm_action": "postComment",
            "value" : JSON.stringify(data)
        }
        FB.Display.showConfirmation(message);
        return;
    };

    this.postComment = function(data){
        try{
            data = JSON.parse(data);
        }catch(e){}

        var that = this;
        var postId = data.postId;
        var message = data.message;

        var postCommentAction = {
            data : {
                id : postId,
                message : message
            },
            success: function(data){
                var currentPage = FB.Display.getHistory();
                if(currentPage.page == "showPresetCommentList")
                {
                    FB.Display._historyRemove(2);
                    that.loadNewsDetails({postId:postId});
                }else{
                    FB.Display._historyRemove(3);
                    that.loadNewsDetails({postId:postId});
				}
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        };

        FB.Display.showLoading();
        FB.Model.postComment(postCommentAction);
        return;
    };

    this.loadPresetStatus = function(){
        var presetStatus = FB.DEFAULTS.presetStatus;
        FB.Display.showPresetStatusList(presetStatus);
        return;
    };

    this.loadPostStatusConfirm = function(data){
        var message = {
            "msg" : $.t("post_Confirm").replace("[post_message]",data),
            "confirm_action": "postStatus",
            "value" : data
        }
        FB.Display.showConfirmation(message);
        return;
    };

    this.postStatus = function(status){
        var that = this;
        var postStatusAction = {
            data : { message : status},
            success: function(data){
                FB.Display._historyRemove(2);
                that.loadNewsFeed();
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        };
        FB.Display.showLoading();
        FB.Model.postStatus(postStatusAction);
        return;
    };

    this.loadEventsMenu = function(){
        var event_submenu = [
        { text : $.t("past_Events"), action:"loadEvents", value : JSON.stringify( {eventType: "past"} ) },
        { text : $.t("future_Events"), action:"loadEvents", value : JSON.stringify( {eventType: "future"} ) },
        { text : $.t("birthdays"), action:"loadBirthdayEvents", value : JSON.stringify( {eventType: "birthday"} ) }
        ];
        FB.Display.submenu(event_submenu);
        return;
    };

    this.loadBirthdayEvents = function(data)
    {
        //TODO Cache the birthday events and don't load anymore until explicitly refreshed
        try{
            data = JSON.parse(data);
        }catch(e){}

        var type = data.eventType;

        console.log("loadBirthdayEvents"+ JSON.stringify(data, undefined, 2) );

        var args = {
            data: {
                offset : 0
            },
            success: function(resp)
            {
                console.log("loadBirthdayEvents success"+ JSON.stringify(resp, undefined, 2));
                FB.Display.eventListBirthday(resp);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.getFriendsBirthday(args);
        return;
    }

    this.loadEvents = function(data)
    {
        //TODO Cache the events and don't load anymore until explicitly refreshed
        try{
            data = JSON.parse(data);
        }catch(e){}

        var type = data.eventType;
        var daysBack = type == "past" ? 31 : 0;

        console.log("loadEvents"+ JSON.stringify(data, undefined, 2) );

        var args = {
            data: {
                daysBack: daysBack
            },
            success: function(resp)
            {
                console.log("loadEvents success"+ JSON.stringify(resp, undefined, 2) );
                FB.Display.eventList(resp);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.getEventsRequest(args);
        return;
    };

    this.loadNearbyFriends = function(){
        var args = {
            success: function(resp){
                console.log("loadNearbyFriends response"+ JSON.stringify(resp, undefined, 2) );
				FB.Cache.saveNearbyFriendsCheckinItems({data:resp});
                FB.Display.showNearbyFriends(resp);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.getNearbyFriends(args);
        return;
    };

    this.loadCheckInDetail = function(data){
        var checkInInfo = JSON.parse(data);
        FB.Display.showNearByDetail(checkInInfo);
        return;
    };

    this.loadFriends = function(data){
        var args = {
            data: {
				limit : 20,
                offset : 0
            },
            success: function(resp)
            {
                console.log("loadFriends success"+ JSON.stringify(resp, undefined, 2));
				var frineds = resp.data;
				FB.Display.friendList(frineds);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.getFriendsRequest(args);
        return;
    };

    this.loadFriendWall = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var friendId = data.friendId;

		//TODO : get post wall by id
		var args = {
            data: {
				"friendId" : friendId
            },
            success: function(resp){
				var viewData = {
					"refreshAction" : "loadFriendWall",
					"posts" : resp
				};
                //FB.Display.postList(viewData);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
		//FB.Display.showLoading();

        FB.Model.getFriendsWall(args); //TODO : No API available now
		return;
    };

    this.loadMessages = function(data){

        try{
            data = JSON.parse(data);
        }catch(e){}

        //Merge with default values
        var defaults = {refresh:false};
        data = $.extend(true, defaults, data);

        if( data.refresh === true )
        {
            var messages = FB.Cache.getMessages();
            if( messages != null || messages.length == 0 )
            {
                FB.Display.showMessageList(messages);
                return;
            }
        }

        var args = {
            success: function(resp)
            {
                FB.Cache.saveMessages(resp);

                FB.Display.showMessageList(resp);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.getPrivateMessage(args);
        return;
    };

    this.loadMessageDetail = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

        var defaults = {conversationId:null, index:1};

        data = $.extend(true, defaults, data);

        console.log( JSON.stringify(data, undefined, 2) );

        var conversation = FB.Cache.getMessage(data.conversationId);

        // Get the given index - starting from the end - newest first
        var message = conversation.comments.data[ conversation.comments.data.length - data.index ];

        message.index = data.index;
        message.total = conversation.comments.data.length;
        message.conversationId = data.conversationId;

        FB.Display.showMessageDetail(message);
        return;
    };

    this.getPhoneNum= function(data){
        var args = {
            phoneNumber : "425-555-1234",//TODO : parse num from data
            regionCode : "US"
        }
        var phoneNums = [];
		phoneNums.push(FB.Util.parsePhoneNumber(args));
        if(phoneNums.length <1){
            return;
        }else{
            var data=[];
            for(var i in phoneNums){
                data.push({
                    "text" : phoneNums[i],
                    "action" : "aq_call",
                    "value" : phoneNums[i]
                });
            }
            FB.Display.submenu(data);
        }
    };

    this.loadCheckInLocations = function()
    {
        var args = {
            success: function(resp){
                console.log("loadCheckInLocations resp"+ JSON.stringify(resp, undefined, 2));
                FB.Display.showCheckInLocationList(resp);
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.getNearbyPlaces(args);
        return;
    };

    this.loadCheckInConfirm = function(data){
        try{
            data = JSON.parse(data);
        }catch(e){}

        var name = data.name;

        var message = {
            "msg" : $.t("check_In_Confirm").replace("[location]",name),
            "confirm_action": "postCheckin",
            "value" : JSON.stringify(data)
        }
        FB.Display.showConfirmation(message);
        return;
    };

    this.postCheckin = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}

		var that = this;
        var args = {
            data:data,
            success: function(resp){
                console.log("postCheckin resp"+ JSON.stringify(resp, undefined, 2));
                FB.Display._historyRemove(2);
                that.loadHome();
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
        FB.Display.showLoading();
        FB.Model.postCheckIn(args);
        return;
    };

    this.postLike = function(data)
    {
        try{
            data = JSON.parse(data);
        }catch(e){}
        var itemId = data;
        var args = {
            data: {
				id : itemId
            },
            success: function(resp)
            {
                console.log("post like success"+ JSON.stringify(resp, undefined, 2));
            },
            error: function(jqXHR)
            {
                console.log("ERROR");
                FB.Display.historyPageReload();
            }
        }
		log("post Like for : " + itemId);
        FB.Model.postLike(args);
    }

	//TODO
    this.playTTS = function(data)
    {
        console.log("Play TTS request: "+ JSON.stringify(data) );
        return;
    }

    //Handle the back button
    this.back = function(data){
        FB.Display.historyBack(data);
        return;
    }

    /**
     * Handles the actions
     */
     this.handleAction = function( action, value ){

        this[action]( value );
        return;

        /*
        try{
            this[action]( value );
        }catch(err){
            log("Unable to complete action: "+action);
            log("Error: "+err);
        }*/
    }
}//end of constructor