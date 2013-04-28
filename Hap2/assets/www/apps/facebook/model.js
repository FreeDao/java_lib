/**
 * Facebook app model
 *
 * @author: kenny, skutfiddinov
 */


 FB.Model = {
    defaults: {
        errorMessage:{},
		facebookNewsFeedRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/feed?maxMessages=%maxMessages%&includeImages=%includeImages%",
			"requestMethod" : "GET"
		},
		facebookHomeRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/home?maxMessages=%maxMessages%&includeImages=%includeImages%",
			"requestMethod" : "GET"
		},
		facebookStatusUpdateRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/status?message=%message%",
			"requestMethod" : "PUT"
		},
		facebookLikeRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/like?id=%id%",
			"requestMethod" : "PUT"
		},
		facebookUnlikeRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/like?id=%id%",
			"requestMethod" : "DELETE"
		},
		facebookPostCommentRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/comment?id=%id%&comment=%comment%",
			"requestMethod" : "PUT"
		},
		facebookGetCommentRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/comments?id=%id%&limit=%limit%&offset=%offset%&afterId=%afterId%",
			"requestMethod" : "GET"
		},
		facebookSearchPlacesRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/places?name=%name%&latitude=%latitude%&longitude=%longitude%&limit=%limit%&offset=%offset%&afterId=%afterId%",
			"requestMethod" : "GET"
		},
		facebookCheckinRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/checkins?placeId=%placeId%&message=%message%&latitude=%latitude%&longitude=%longitude%",
			"requestMethod" : "PUT"
		},
		facebookNearbyFriendsRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/checkins?time=%time%&distance=%distance%&latitude=%latitude%&longitude=%longitude%",
			"requestMethod" : "GET"
		},
		facebookImageRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/images",
			"requestMethod" : "POST"
		},
		facebookEventsRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/events?days_back=%days_back%",
			"requestMethod" : "GET"
		},
		facebookFriendsRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/friends?limit=%limit%&offset=%offset%",
			"requestMethod" : "GET"
		},
		facebookFriendsBirthdayRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/friends/birthday?limit=%limit%&offset=%offset%",
			"requestMethod" : "GET"
		},
		facebookPrivateMessage : {
			"requestUri" : "mip_services/content/api/1.0/facebook/inbox",
			"requestMethod" : "GET"
		},
		facebookShareLinkRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/link?link=%link%&message=%message%",
			"requestMethod" : "PUT"
		},
		facebookProfileRequest : {
			"requestUri" : "mip_services/content/api/1.0/facebook/profile",
			"requestMethod" : "GET"
		}
    },

    /**** GET NEWS FEED ****/
    getNewsFeed : function(args)
    {
        var maxMessages = args.data.maxMessages || 20;
        var includeImages = args.data.includeImages || false;
        var facebookNewsFeedRequest = this.defaults.facebookNewsFeedRequest;

        facebookNewsFeedRequest.requestUri = facebookNewsFeedRequest.requestUri.replace("%maxMessages%", maxMessages);
        facebookNewsFeedRequest.requestUri = facebookNewsFeedRequest.requestUri.replace("%includeImages%", includeImages);

        this.sendHttpRequest({
            url : facebookNewsFeedRequest.requestUri,
            type : facebookNewsFeedRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET HOME ****/
    getLiveFeed : function(args)
    {
        console.log("Getting livefeed...");
        var maxMessages = args.data.maxMessages;
        var includeImages = args.data.includeImages;
        var facebookHomeRequest = this.defaults.facebookHomeRequest;
        facebookHomeRequest.requestUri = facebookHomeRequest.requestUri.replace("%maxMessages%", maxMessages);
        facebookHomeRequest.requestUri = facebookHomeRequest.requestUri.replace("%includeImages%", includeImages);

        this.sendHttpRequest({
            url : facebookHomeRequest.requestUri,
            type : facebookHomeRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** PUT STATUS ****/
    postStatus : function(args)
    {
        var message = encodeURIComponent(args.data.message);
        var facebookStatusUpdateRequest = this.defaults.facebookStatusUpdateRequest;
        facebookStatusUpdateRequest.requestUri = facebookStatusUpdateRequest.requestUri.replace("%message%", message);

        this.sendHttpRequest({
            url : facebookStatusUpdateRequest.requestUri,
            type : facebookStatusUpdateRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** PUT LIKE ****/
    postLike : function(args)
    {
        var id = args.data.id;
        id = encodeURIComponent(id);

        var facebookLikeRequest = this.defaults.facebookLikeRequest;
        facebookLikeRequest.requestUri = facebookLikeRequest.requestUri.replace("%id%", id);

        this.sendHttpRequest({
            url : facebookLikeRequest.requestUri,
            type : facebookLikeRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** DELETE UNLIKE ****/
    postUnlike : function(args)
    {
        var id = args.data.id;
        id = encodeURIComponent(id);

        var facebookUnlikeRequest = this.defaults.facebookUnlikeRequest;
        facebookUnlikeRequest.requestUri = facebookUnlikeRequest.requestUri.replace("%id%", id);

        this.sendHttpRequest({
            url : facebookUnlikeRequest.requestUri,
            type : facebookUnlikeRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** PUT COMMENT ****/
    postComment : function(args)
    {
        var id = args.data.id;
        var message = args.data.message;

        message = encodeURIComponent(message);
        id = encodeURIComponent(id);

        var facebookPostCommentRequest = this.defaults.facebookPostCommentRequest;
        facebookPostCommentRequest.requestUri = facebookPostCommentRequest.requestUri.replace("%id%", id);
        facebookPostCommentRequest.requestUri = facebookPostCommentRequest.requestUri.replace("%comment%", message);

        this.sendHttpRequest({
            url : facebookPostCommentRequest.requestUri,
            type : facebookPostCommentRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET ALL COMMENTS ****/
    getComments : function(args)
    {
        var limit = 10;
        var id = args.data.id;
        var offset = args.offset;
        var afterId = args.afterId;

        var facebookGetCommentRequest = this.defaults.facebookGetCommentRequest;
        facebookGetCommentRequest.requestUri = facebookGetCommentRequest.requestUri.replace("%ID%", id);
        facebookGetCommentRequest.requestUri = facebookGetCommentRequest.requestUri.replace("%LIMIT%", limit);
        facebookGetCommentRequest.requestUri = facebookGetCommentRequest.requestUri.replace("%offset%", offset);
        facebookGetCommentRequest.requestUri = facebookGetCommentRequest.requestUri.replace("%afterId%", afterId);

        this.sendHttpRequest({
            url : facebookGetCommentRequest.requestUri,
            type : facebookGetCommentRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET NEARBY PLACES ****/
    getNearbyPlaces : function(args)
    {
        args.data = $.extend(true, {}, args.data);
        var location = AQ.app.getLocation();
        var latitude = location.latitude;
        var longitude = location.longitude;

        var name = args.data.name || "";
        var limit = args.data.limit || 20;
        var offset = args.data.offset || 0;
        var afterId = args.data.afterId || null;


        var facebookSearchPlacesRequest = this.defaults.facebookSearchPlacesRequest;

        facebookSearchPlacesRequest.requestUri = facebookSearchPlacesRequest.requestUri.replace("%name%", name);
        facebookSearchPlacesRequest.requestUri = facebookSearchPlacesRequest.requestUri.replace("%latitude%", latitude);
        facebookSearchPlacesRequest.requestUri = facebookSearchPlacesRequest.requestUri.replace("%longitude%", longitude);
        facebookSearchPlacesRequest.requestUri = facebookSearchPlacesRequest.requestUri.replace("%limit%", limit);
        facebookSearchPlacesRequest.requestUri = facebookSearchPlacesRequest.requestUri.replace("%offset%", offset);
        facebookSearchPlacesRequest.requestUri = facebookSearchPlacesRequest.requestUri.replace("%afterId%", afterId);


        this.sendHttpRequest({
            url : facebookSearchPlacesRequest.requestUri,
            type : facebookSearchPlacesRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** POST CHECK IN ****/
    postCheckIn : function(args)
    {
        var latitude = args.data.latitude;
        var longitude = args.data.longitude;
        var placeId = args.data.placeId;
        var message = "";

        var facebookCheckinRequest = this.defaults.facebookCheckinRequest;
        facebookCheckinRequest.requestUri = facebookCheckinRequest.requestUri.replace("%placeId%", placeId);
        facebookCheckinRequest.requestUri = facebookCheckinRequest.requestUri.replace("%message%", message);
        facebookCheckinRequest.requestUri = facebookCheckinRequest.requestUri.replace("%latitude%", latitude);
        facebookCheckinRequest.requestUri = facebookCheckinRequest.requestUri.replace("%longitude%", longitude);

        this.sendHttpRequest({
            url : facebookCheckinRequest.requestUri,
            type : facebookCheckinRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET NEARBY FRIENDS(get checkins) ****/
    getNearbyFriends : function(args)
    {
        args.data = $.extend(true, {}, args.data);

        var location = AQ.app.getLocation();
        var latitude = location.latitude;
        var longitude = location.longitude;

        var time = args.data.time || 60 * 24 * 7; // 7 days
        var distance = args.data.distance || 10;

        var facebookNearbyFriendsRequest = this.defaults.facebookNearbyFriendsRequest;

        facebookNearbyFriendsRequest.requestUri = facebookNearbyFriendsRequest.requestUri.replace("%latitude%", latitude);
        facebookNearbyFriendsRequest.requestUri = facebookNearbyFriendsRequest.requestUri.replace("%longitude%", longitude);
        facebookNearbyFriendsRequest.requestUri = facebookNearbyFriendsRequest.requestUri.replace("%distance%", distance);
        facebookNearbyFriendsRequest.requestUri = facebookNearbyFriendsRequest.requestUri.replace("%time%", time);

        this.sendHttpRequest({
            url : facebookNearbyFriendsRequest.requestUri,
            type : facebookNearbyFriendsRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET IMAGES ****/
    getImages : function(args)
    {
        var facebookImageRequest = this.defaults.facebookImageRequest;

        this.sendHttpRequest({
            url : facebookImageRequest.requestUri,
            type : facebookImageRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET Events ****/
    getEventsRequest : function(args)
    {
        var daysBack = args.data.daysBack || 0;

        var facebookEventsRequest = this.defaults.facebookEventsRequest;

        facebookEventsRequest.requestUri = facebookEventsRequest.requestUri.replace("%days_back%", daysBack);


        this.sendHttpRequest({
            url : facebookEventsRequest.requestUri,
            type : facebookEventsRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET Friends ****/
    getFriendsRequest : function(args)
    {
        var limit = 10;
        var offset = args.data.offset;

        var facebookFriendsRequest = this.defaults.facebookFriendsRequest;

        facebookFriendsRequest.requestUri = facebookFriendsRequest.requestUri.replace("%limit%", limit);
        facebookFriendsRequest.requestUri = facebookFriendsRequest.requestUri.replace("%offset%", offset);

        this.sendHttpRequest({
            url : facebookFriendsRequest.requestUri,
            type : facebookFriendsRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

	//TODO : this api was missing
	getFriendsWall : function(args)
    {
		args.success("no data found");
    },

    /**** GET Friends Birthday ****/
    getFriendsBirthday : function(args)
    {
        var limit = args.data.limit || 20;
        var offset = args.data.offset || 0;

        var facebookFriendsBirthdayRequest = this.defaults.facebookFriendsBirthdayRequest;

        facebookFriendsBirthdayRequest.requestUri = facebookFriendsBirthdayRequest.requestUri.replace("%limit%", limit);
        facebookFriendsBirthdayRequest.requestUri = facebookFriendsBirthdayRequest.requestUri.replace("%offset%", offset);

        this.sendHttpRequest({
            url : facebookFriendsBirthdayRequest.requestUri,
            type : facebookFriendsBirthdayRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** GET Inbox Message ****/
    getPrivateMessage : function(args)
    {
        var facebookPrivateMessage = this.defaults.facebookPrivateMessage;

        this.sendHttpRequest({
            url : facebookPrivateMessage.requestUri,
            type : facebookPrivateMessage.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** PUT Share Link ****/
    putShareLink : function(args)
    {
        var link = args.data.link;
        var message = encodeURIComponent(args.data.message);
        var facebookShareLinkRequest = this.defaults.facebookShareLinkRequest;
        facebookShareLinkRequest.requestUri = facebookShareLinkRequest.requestUri.replace("%link%", link);
        facebookShareLinkRequest.requestUri = facebookShareLinkRequest.requestUri.replace("%message%", message);

        this.sendHttpRequest({
            url : facebookShareLinkRequest.requestUri,
            type : facebookShareLinkRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },

    /**** Get Profile ****/
    getProfile : function(args)
    {
        var facebookProfileRequest = this.defaults.facebookProfileRequest;

        this.sendHttpRequest({
            url : facebookProfileRequest.requestUri,
            type : facebookProfileRequest.requestMethod,
            success : args.success,
            error : args.error
        });
    },


    sendHttpRequest : function(args)
    {
        AQ.app.ajax({
            url : args.url,
            type : args.type || "GET",
            dataType: 'json',
            success : args.success,
            error: function(jqXHR){
                console.error(jqXHR);
            }
        });
    }
};
