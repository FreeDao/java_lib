/*================================================
=            Caching the data locally            =
================================================*/

FB.Cache = {
    defaults: {
        cache:{
            profile : null,
            images : {},
            newsFeedItems : { data:[], paging:{} },
			nearbyFriendsCheckinItems : {},
            messages: null
        }
    },

    saveImage: function(images)
    {
        for(var i=0; i<images.length; i++)
        {
            this.defaults.cache.images[images[i].id] = images[i].imageData;
        }

        return;
    },

    getImage : function(id)
    {
        return this.defaults.cache.images[id] || null;
    },

    saveNewsFeedItems : function(data)
    {
        this.defaults.cache.newsFeedItems = data;

        return;
    },

    getNewsFeedItems : function(id)
    {
        if( typeof id == "undefined" )
        {
            return this.defaults.cache.newsFeedItems;
        }

        for( var i = 0; i < this.defaults.cache.newsFeedItems.data.length; i++ )
        {
            if( id == this.defaults.cache.newsFeedItems.data[i].id )
            {
                return this.defaults.cache.newsFeedItems.data[i];
            }
        }

        return null;
    },

    // gets the comments for the given post id (required) and comment id (optional)
    getNewsFeedComments : function(args)
    {
        var postId = args.postId;
        var commentId = args.commentId || null;

        // Go through each news feed item and find the given post id
        for( var i = 0; i < this.defaults.cache.newsFeedItems.data.length; i++ )
        {
            // Find the given post id
            if( postId == this.defaults.cache.newsFeedItems.data[i].id )
            {
                var comments = this.defaults.cache.newsFeedItems.data[i].comments;

                // If the comment id is given
                if( commentId != null )
                {
                    //Check whether the comments are empty
                    if(typeof comments.data == 'undefined')
                    {
                        return null;
                    }else{
                        //Go through each comment and find the one that matches the given comment id
                        for(var k=0; k<comments.data.length; k++)
                        {
                            if(commentId == comments.data[k].id)
                            {
                                return comments.data[k];
                            }
                        }
                        return null;
                    }
                }
                return comments;
            }
        }

        return null;
    },

    saveNearbyFriendsCheckinItems : function(data)
    {
        this.defaults.cache.nearbyFriendsCheckinItems = data;

        return;
    },

	getNearbyFriendsCheckinComments : function(args)
    {
        var checkinId = args.checkin_id;
        // Go through each news feed item and find the given post id
        for( var i = 0; i < this.defaults.cache.nearbyFriendsCheckinItems.data.data.length; i++ )
        {
            if( checkinId == this.defaults.cache.nearbyFriendsCheckinItems.data.data[i].checkin_id )
            {
                return this.defaults.cache.nearbyFriendsCheckinItems.data.data[i].comments;
            }
        }
        return null;
    },
	getNearbyFriendsCheckinCommentbyId : function(args)
    {
        var checkinId = args.checkinId;
		var commentId = args.commentId;

        // Go through each news feed item and find the given post id
        for( var i = 0; i < this.defaults.cache.nearbyFriendsCheckinItems.data.data.length; i++ )
        {
            if( checkinId == this.defaults.cache.nearbyFriendsCheckinItems.data.data[i].checkin_id )
            {
				 for( var j = 0; j < this.defaults.cache.nearbyFriendsCheckinItems.data.data[i].comments.data.length; j++ )
				{
					if( commentId == this.defaults.cache.nearbyFriendsCheckinItems.data.data[i].comments.data[j].id )
					{
						return this.defaults.cache.nearbyFriendsCheckinItems.data.data[i].comments.data[j];
					}
				}
            }
        }
        return null;
    },

    saveMessages: function(messages)
    {
        this.defaults.cache.messages = messages;

        return;
    },

    getMessages : function()
    {
        return this.defaults.cache.messages;
    },

    getMessage : function(conversationId)
    {
        if(typeof conversationId == "undefined")
        {
            return null;
        }

        for( var i = 0; i < this.defaults.cache.messages.data.length; i++ )
        {
            if( conversationId == this.defaults.cache.messages.data[i].id )
            {
                return this.defaults.cache.messages.data[i];
            }
        }

        return null;
    },
};

/*-----  End of Caching the data locally  ------*/