/**
 * Template generator - populates the template data and updates the screen
 *
 * @author: kenny, skutfiddinov
 */

FB.Display = {
    defaults: {
        historyWrapper: []
    },

    /**
     * App homepage - main menu
     */
    home: function()
    {
        this._historySave({page:'home'});
        AQ.app.updateScreen(FB.STATE.main_menu);
    },


    submenu : function(data)
    {
        this._historySave({page:'submenu', value:data});
        var template_data = $.extend(true,{}, FB.STATE.submenu);
        for(var i in data){
            template_data.template_content.list.push({
                "text" : data[i].text,
                "action" : data[i].action,
                "value" : data[i].value
            });
        }
        AQ.app.updateScreen(template_data);
    },

    /**
     * Show list of news feed posts
     */
    postList: function(data)
    {
        this._historySave({page:'postList', value:data});

        var template_data = $.extend(true,{}, FB.STATE.post_list);

		if(typeof data.posts.data == "undefined" || data.posts.data.length == 0){
			this.noItemPage({
                "text" : $.t("not_Found_Notification"),//"No information found... Go back?",
                "action" : "back"
            });
            return;
		}

        template_data.template_content.buttons["1"] =  {
            "image" : FB.IMAGES.button['1_refresh'],
            "action" : data.refreshAction, //"loadNewsFeed" "loadFriendWall"
            "value" : JSON.stringify( {reload:true} )
        }


        // Go through each data item
        for ( var i=0; i < data.posts.data.length; i++ )
        {
            var image1 = FB.Cache.getImage( data.posts.data[i].from.id ) || FB.IMAGES.list.default_profile;
            // If it's not a status post, ignore the current iteration
            if( typeof data.posts.data[i].message == "undefined" )
                continue;

            var message = data.posts.data[i].message;
            var image2 = FB.IMAGES.list[ data.posts.data[i].type.toLowerCase() ];


            // Push the data to the template
            template_data.template_content.list.push({
                "image1" : image1, //TODO: get the user's profile image
                "text" : data.posts.data[i].from.name +": "+ message,
                "image2" : image2,
                "action" : "loadNewsDetails",
                "value" : JSON.stringify({ postId:data.posts.data[i].id })
            });
        }

        AQ.app.updateScreen(template_data);
    },
    /*-----  End of postList()  ------*/

    /**
     * Show the details page of the news feed posts
     */
    showPostDetail: function(data)
    {
        this._historySave({page:'showPostDetail', value:data});

        var template_data = $.extend(true,{}, FB.STATE.detail_page);
        template_data.template_content.buttons =  {
            1 : {
                "image" : FB.IMAGES.button['1_tts'],
                "action" : "playTTS",
                "value" : JSON.stringify(data.id)
            },
            2 : {
                "image" : FB.IMAGES.button['2_like'],
                "action" : "postLike",
                "value" : JSON.stringify(data.id)
            },
            3 : {
                "image" : FB.IMAGES.button['3_add_comment'],
                "action" : "loadPresetComments",
                "value" : JSON.stringify( {postId:data.id} )
            }
        }

        if(data.comments.count != 0 && data.comments.data.length != 0)
        {
            template_data.template_content.buttons[4] = {
                "image" : FB.IMAGES.button['4_comments'],
                "action" : "loadCommentList",
                "value" : JSON.stringify( {type : "post", postId:data.id} )
            }
        }

        template_data.template_content.text_left_top = data.from.name;
        template_data.template_content.text_left_bottom = FB.Util.formatDate(data.created_time);
        template_data.template_content.text_right_main = (data.story || data.message);
        //Show the user image or if doesn't exists, show the default one
        template_data.template_content.image_left = FB.Cache.getImage( data.from.id ) || FB.IMAGES.list.default_profile;

        AQ.app.updateScreen(template_data);
    },
    /*-----  End of showPostDetail()  ------*/


    /**
    *
    * Show preset comments to be posted
    *
    **/
    showPresetCommentList: function(args){
        var data = args.data;
        var postId = args.postId;

        this._historySave({page:'showPresetCommentList', value:args});
        var template_data = $.extend(true,{}, FB.STATE.preset_list);

        for(var i in data){
            template_data.template_content.list.push({
                "text" : data[i],
                "action" : "loadPostCommentConfirm",
                "value" : JSON.stringify({message:data[i], postId:postId})
            });
        }
        AQ.app.updateScreen(template_data);
    },
    /*-----  End of showPresetCommentList() ------*/

    /**
    *
    * Show the list of comments
    *
    **/
    showCommentList: function(args)
    {
        this._historySave({page:'showCommentList', value:args});

        var template_data = $.extend(true,{}, FB.STATE.checkin_list);
        var comments = args.comments || {data:[], count:0};
        var postId = args.id;
		var type = args.type;

        if( typeof comments.data == "undefined" )
        {
			this.noItemPage({
				"text" : $.t("not_Found_Notification"),//"No information found... Go back?",
				"action" : "back"
			});
			return;
        }

        for(var i=0; i<comments.data.length; i++)
        {
            var image1 = FB.Cache.getImage( comments.data[i].from.id ) || FB.IMAGES.list.default_profile;

            template_data.template_content.list.push({
                "image1" : image1,
                "text" : comments.data[i].from.name +": "+ comments.data[i].message,
                "image2" : FB.IMAGES.list.comment,
                "action" : "loadCommentDetails",
                "value" : JSON.stringify( {type : type, id:postId, commentId: comments.data[i].id} )
            });
        }

        /*if( comments.count > comments.data.length )
        {
            template_data.template_content.list.push({
                "text" : "Showing "+comments.data.length+" out of "+comments.count+". Load more...",
                "action" : null, //TODO "loadMoreComments"
                "value" : JSON.stringify( {postId:postId} )
            });
        }

        if(template_data.template_content.list.length == 0)
        {
            template_data.template_id = "c";

            template_data.template_content.list.push({
                "text" : $.t("not_Found_Notification"),//"No comments found. Go back?",
                "action" : "back"
            });
        }*/

        AQ.app.updateScreen(template_data);
    },
    /*-----  End of showCommentList()  ------*/

    /**
    *
    * Show the actual comment text
    *
    **/
    showCommentDetail: function(comment)
    {
        this._historySave({page:'showCommentDetail', value:comment});

        var template_data = $.extend(true,{}, FB.STATE.detail_page);

        if(typeof comment == "undefined")
        {
            template_data.template_content.text_right_main = "Error occured. Couldn't find this comment. Please try again later."
            AQ.app.updateScreen(template_data);
            return;
        }

        template_data.template_content.buttons["1"] =  {
            "image" : FB.IMAGES.button['1_tts'],
            "action" : "playTTS",
            "value" : JSON.stringify(comment.id)
        };
        template_data.template_content.buttons["2"] =  {
            "image" : FB.IMAGES.button['2_like'],
            "action" : "postLike",
            "value" : JSON.stringify(comment.id)
        };
        template_data.template_content.text_left_top = comment.from.name;
        template_data.template_content.text_left_bottom = FB.Util.formatDate(comment.created_time);
        template_data.template_content.text_right_main = comment.message;
        template_data.template_content.image_left = FB.Cache.getImage(comment.from.id) || FB.IMAGES.list.default_profile;

        AQ.app.updateScreen(template_data);
    },
    /*-----  End of showCommentDetail()  ------*/

    /**
    *
    * Show the list of pre-set status messages to be posted
    *
    **/
    showPresetStatusList: function(data)
    {
        this._historySave({page:'showPresetStatusList', value:data});

        var template_data = $.extend(true,{}, FB.STATE.preset_list);

        for(var i in data)
        {
            template_data.template_content.list.push({
                "text" : data[i],
                "action" : "loadPostStatusConfirm",
                "value" : data[i]
            });
        }

        AQ.app.updateScreen(template_data);
    },

    eventList : function(data)
    {
            this._historySave({page:'eventList', value:data});
			if(typeof(data.events) == "undefined")
			{
                this.noItemPage({
                    "text" : $.t("not_Found_Notification"),//"No birthdays found... Go back?",
                    "action" : "back"
                });
                return;
			}
            var template_data = $.extend(true,{}, FB.STATE.events_page);
			var events = data.events;
			for(var i = 0; i < events.length; i++)
            {
                template_data.template_content.list.push({
                    //"image1" : events[i].pic_small,
                    //"image2" : FB.IMAGES.list.event_birthday,
                    "text" : events[i].name + " -- " + FB.Util.formatDate(events[i].start_time),
                    "action" : "",
                    "value" : JSON.stringify( {id : events[i].eid} )
                });
            }
            AQ.app.updateScreen(template_data);
    },

    eventListBirthday : function(data)
    {
            this._historySave({page:'eventListBirthday', value:data});

            var template_data = $.extend(true,{}, FB.STATE.events_birthday_page);
            var birthdays = data.data;

            if(birthdays.length == 0)
            {
                this.noItemPage({
                    "text" : $.t("not_Found_Notification"),//"No birthdays found... Go back?",
                    "action" : "back"
                });
                return;
            }

            for(var i = 0; i < birthdays.length; i++)
            {
                if(typeof birthdays[i].birthday_date == "undefined")
                {
                    continue;
                }

                template_data.template_content.list.push({
                    "image1" : birthdays[i].pic_small,
                    "image2" : FB.IMAGES.list.event_birthday,
                    "text" : birthdays[i].birthday_date + " - " + birthdays[i].name,
                    "action" : "loadFriendWall", //TODO This should open the user's wall page
                    "value" : {friendId : birthdays[i].uid}
                });
            }
            AQ.app.updateScreen(template_data);
    },

    showNearbyFriends: function(data)
    {
        this._historySave({page:'showNearbyFriends', value:data});

        if(typeof(data.data) == "undefined" || data.data.length == 0 )
        {
            this.noItemPage({
                "text" : $.t("not_Found_Notification"),//"No friends found nearby... Go back?",
                "action" : "back"
            });
            return;
        }

        var template_data = $.extend(true,{}, FB.STATE.checkin_list);

        template_data.template_content.buttons["1"] =  {
            "image" : FB.IMAGES.button['1_refresh'],
            "action" : "showNearbyFriends"
        };


        for(var i = 0; i < data.data.length; i++)
        {
            template_data.template_content.list.push({
                "image1" : FB.IMAGES.list.photo,
                "text" : data.data[i].authorName + " @ " + data.data[i].placeName,
                "action" : "loadCheckInDetail",
                "value" : JSON.stringify(data.data[i])
            });
        }

        AQ.app.updateScreen(template_data);
    },

    showNearByDetail: function(data)
    {
        this._historySave({page:'showNearByDetail', value:data});
        var template_data = $.extend(true,{}, FB.STATE.detail_page);
        template_data.template_content.buttons["1"] =  {
                    "image" : FB.IMAGES.button['1_tts'],
                    "action" : "playTTS",
                    "value" : JSON.stringify(data.checkin_id)
                };
        template_data.template_content.buttons["2"] =  {
                    "image" : FB.IMAGES.button['2_like'],
                    "action" : "postLike",
                    "value" : JSON.stringify(data.checkin_id)
                };
        template_data.template_content.buttons["4"] =  {
                    "image" : FB.IMAGES.button['4_comments'],
                    "action" : "loadCommentList",
                    "value" :  JSON.stringify( {type : "checkin", checkinId:data.checkin_id} )
                };
        template_data.template_content.text_left_top = data.authorName;
        template_data.template_content.text_left_bottom = data.timestamp;
        template_data.template_content.text_right_main = data.placeName;
		template_data.template_content.image_left = FB.IMAGES.list.photo;

        AQ.app.updateScreen(template_data);
    },


    /** Show the list of messages **/
    showMessageList: function(resp)
    {
        this._historySave({page:'showMessageList', value:resp});

        var data = resp.data;

        console.log("data.length: "+ data.length);

        // console.log( JSON.stringify(data, undefined, 2) );

        if( data.length == 0 )
        {
            this.noItemPage({
                "text" : $.t("not_Found_Notification"),//"No friends found nearby... Go back?",
                "action" : "back"
            });
            return;
        }


        var template_data = $.extend(true,{}, FB.STATE.list_page);

        template_data.template_id = "c";

        template_data.template_content.buttons["1"] =  {
            "image" : FB.IMAGES.button['1_refresh'],
            "action" : "loadMessages",
            "value" : {refresh: true}
        };

		for( var i = 0; i < data.length; i++ )
        {
            if( typeof data[i].comments == "undefined" || typeof data[i].comments.data == "undefined" )
            {
                continue;
            }

            console.log( "data["+i+"].comments.data.length" + data[i].comments.data.length );

            //TODO: find the people involved in the conversation - missing in choreo API

            //////////TEMP CODE
            var tmp_names = {};
            var tmp_names_str = "";

            for(var k=0; k<data[i].comments.data.length; k++)
            {
                tmp_names[data[i].comments.data[k].from.name] = true;
            }
            for(var k in tmp_names)
            {
                tmp_names_str += k+", ";
            }
            tmp_names_str = tmp_names_str.substring(0, tmp_names_str.length - 2);
            //////////TEMP CODE END

			template_data.template_content.list.push({
				"text" : tmp_names_str, //TEMP
				"action" : "loadMessageDetail",
				"value" : {conversationId: data[i].id}
			});
		}

        AQ.app.updateScreen(template_data);
    },

    /** Show the message details **/
    showMessageDetail: function(data)
    {
        this._historySave({page:'showMessageDetail', value:data});

        var template_data = $.extend(true,{}, FB.STATE.detail_page);

        template_data.template_content.buttons["1"] =  {
            "image" : FB.IMAGES.button['1_tts'],
            "action" : "playTTS",
            "value" : "play"
        };

        // TODO
        // template_data.template_content.buttons["2"] =  {
        //             "image" : FB.IMAGES.button['2_call'],
        //             "action" : "getPhoneNum",
        //             "value" : "" + data.messages
        //         };

        if( data.index > 1 )
        {
            template_data.template_content.buttons["3"] =  {
                "image" : FB.IMAGES.button['3_prev'],
                "action" : "loadMessageDetail",
                "value" : {
                    conversationId : data.conversationId,
                    index : data.index - 1
                }
            };
        }

        if( data.index < data.total )
        {
            template_data.template_content.buttons["4"] =  {
                "image" : FB.IMAGES.button['4_next'],
                "action" : "loadMessageDetail",
                "value" : {
                    conversationId : data.conversationId,
                    index : data.index + 1
                }
            };
        }

        template_data.template_content.text_left_top = data.from.name;
        template_data.template_content.text_left_bottom = FB.Util.formatDate(data.created_time);
        template_data.template_content.text_right_main = data.message;

        AQ.app.updateScreen(template_data);
    },

    showCheckInLocationList: function(data){
        this._historySave({page:'showCheckInLocationList', value:data});
        var template_data = $.extend(true,{}, FB.STATE.checkin_list);
        var paging = data.paging;
        var data = data.data;

        if(data.length == 0)
        {
            this.noItemPage({
                "text" : $.t("not_Found_Notification"),//"No locations found nearby... Go back?",
                "action" : "back"
            });
            return;
        }


        for(var i = 0; i < data.length; i++)
        {
            template_data.template_content.list.push({
                "text" : data[i].name,
                "action" : "loadCheckInConfirm",
                "value" : { name: data[i].name, placeId : data[i].id, latitude:data[i].location.latitude, longitude:data[i].location.longitude }
            });
        }
        AQ.app.updateScreen(template_data);
    },

    friendList: function(data){
        this._historySave({page:'friendList', value:data});
        var template_data = $.extend(true,{}, FB.STATE.submenu);
        for(var i in data){
            template_data.template_content.list.push({
                "image1" : data[i].pic_small,
                "text" : data[i].name,
                "action" : "loadFriendWall",//TODO This should open the user's wall page
                "value" : { friendId: data[i].uid }
            });
        }
        AQ.app.updateScreen(template_data);
    },

    showConfirmation: function(data){
        this._historySave({page:'showConfirmation'});
        var template_data = $.extend(true,{}, FB.STATE.confirmation);
        template_data.template_content.text_main = data.msg;
        template_data.template_content.text_bottom = {
            "1":{
                    "text": $.t("yes"),
                    "action":data.confirm_action,
                    "value" : data.value
                },
            "3":{
                    "text" : $.t("no"),
                    "action" : "back"
                }
        };
        AQ.app.updateScreen(template_data);
    },

    /**
    *
    * Usually used when no item is found
    *
    **/
    noItemPage: function(data)
    {
        var template_data = $.extend(true,{}, FB.STATE.no_item_page);
        template_data.template_content.list.push(data);
        AQ.app.updateScreen(template_data);
    },

    showLoading: function()
    {
        AQ.respond.showLoading();
    },

    /*
    newFeeds: function(feeds){
        var template_data = $.extend(true,{}, FB.STATE.news_feed);
        for(var id in FB.DATA.newsFeed){
            if( typeof FB.DATA.newsFeed[id].message != "undefined" )
            {
                var text = FB.DATA.newsFeed[id].from.name +": "+ FB.DATA.newsFeed[id].message;
                var userId = FB.DATA.newsFeed[id].from.id;

                if( typeof FB.DATA.images[userId] != 'undefined' )
                {
                    var image = FB.DATA.images[userId];
                }else{
                    var image = "https://rm.airbiquity.com/panasonic_temp/facebook/avatar-generic.png";
                }

                template_data.template_content.list.push({
                    "image1" : image,
                    "text" : text,
                    "action" : "loadComments",
                    "value" : id
                });
            }
        }

        AQ.app.updateScreen(template_data);
    },


    comments: function(postId){
        var response = $.extend(true, {}, FB.STATE.comments);
        var comments = FB.DATA.newsFeed[ postId ].comments;

        if( comments.count == 0 ){
            response.template_content.list.push({
                "image1" : null,
                "text" : "No comments",
                "action" : ""
            });
        }else{
            //Populate the content to the list
            for( var i = 0; i < comments.data.length; i++ ){
                var text = comments.data[i].from.name +": "+ comments.data[i].message;
                var userId = comments.data[i].from.id;

                if( typeof FB.DATA.images[userId] != 'undefined' ){
                    var image = FB.DATA.images[userId];
                }else{
                    var image = "https://rm.airbiquity.com/panasonic_temp/facebook/avatar-generic.png";
                }

                response.template_content.list.push({
                    "image1" : image,
                    "text" : text,
                    "action" : ""
                });
            }
        }

        AQ.app.updateScreen(response);
    },
    */

    _parsePhoneNum : function(text){
        var nums=[];


        return nums;
    },



    /**
     * Save the page in the history
     */
    _historySave : function(history)
    {
        //If the previous page was the temp history page, remove it from the history and relaunch the function
        if( FB.Display.defaults.historyWrapper.length > 1 && FB.Display.defaults.historyWrapper[FB.Display.defaults.historyWrapper.length-1].isTemp === true )
        {
            FB.Display.defaults.historyWrapper.pop();
            FB.Display._historySave(history);
            return;
        }


        if( FB.Display.defaults.historyWrapper.length == 0 )
        {
            FB.Display.defaults.historyWrapper.push( history );
        }
        else if( FB.Display.defaults.historyWrapper[FB.Display.defaults.historyWrapper.length-1].page != history.page )
        {
            FB.Display.defaults.historyWrapper.push( history );
        }else
        {
            console.log("Same history. Overwriting the data.");
            FB.Display.defaults.historyWrapper[ FB.Display.defaults.historyWrapper.length -1 ] = history;
        }

        return;
    },

    /**
     * Reset the history
     */
    _historyReset : function()
    {
        FB.Display.defaults.historyWrapper = [];
    },

    /**
     * Removes the number of history items starting with the newest
     */
    _historyRemove : function(num)
    {
        num = num || 1;

        for(var i=0; i<num; i++)
        {
            FB.Display.defaults.historyWrapper.pop();
        }
    },

    getHistory: function(index)
    {
        index = index || 0;

        index = (FB.Display.defaults.historyWrapper.length - 1) - index;

        return FB.Display.defaults.historyWrapper[index];
    },

    historyPageReload: function()
    {
        FB.Display.defaults.historyWrapper.push({});
        this.historyBack();
    },

    /**
     * Move back in the history and load the previous page
     */
    historyBack : function()
    {
        //Remove the current page from the history array
        FB.Display.defaults.historyWrapper.pop();

        console.warn( "History:", FB.Display.defaults.historyWrapper );

        if(FB.Display.defaults.historyWrapper.length < 1){
            //FB.Display.home();
			AQ.app.load("home");
            return;
        }

        var history = FB.Display.defaults.historyWrapper.pop();
        console.log("Going back to"+ JSON.stringify(history, undefined, 2) );

        //Simplifying the history function call event
        FB.Display[ history.page ]( history.value );

/*
        switch(history.page)
        {
            case "home":
                FB.Display.home();
                break;

            case "submenu":
                FB.Display.submenu(history.value);
                break;

            case "postList":
                FB.Display.postList( history.value );
                break;

            case "showPostDetail":
                FB.Display.showPostDetail( history.value );
                break;

            case "showPresetCommentList":
                FB.Display.showPresetCommentList( history.value );
                break;

            case "commentList":
                FB.Display.showCommentList( history.value );
                break;

            case "showCommentDetail":
                FB.Display.showCommentDetail( history.value );
                break;

            case "showPresetStatusList":
                FB.Display.showPresetStatusList( history.value );
                break;

            case "eventList":
                FB.Display.eventList( history.value );
                break;

            case "showNearbyFriends":
                FB.Display.showNearbyFriends( history.value );
                break;

            case "showNearByDetail":
                FB.Display.showNearByDetail( history.value );
                break;

            case "showMessageList":
                FB.Display.showMessageList( history.value );
                break;

            case "showCheckInLocationList":
                FB.Display.showCheckInLocationList( history.value );
                break;

            case "friendList":
                FB.Display.friendList( history.value );
                break;

            default:
                return;
        }
*/
    }
};
