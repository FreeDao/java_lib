AQ.respond = {
	/**
	 * Responds to update the screen based on the formatted JSON object
	 * passed from the app and to be converted to the formatted array
	 * for further processing on HAP.
	 *
	 * @param json resp_json: formatted JSON object coming from the app and to be parsed by the translator
	 */
	updateScreen: function(resp_json)
	{
        // AQ.hash.huState.sendOk = false;

		//Parse the JSON and convert it to a formatted array
        var resp_array = AQ.template.parse(resp_json);

		// try{
		// 	var resp_array = AQ.template.parse(resp_json);
		// 	log(">>>>>TEMPLATE IS PARSED<<<<<");
		// }catch(err){
		  //           log("Not able to parse the template: "+err);
		
		  //           window.onNotification({name:"hupScreenUpdated"});
		  //           return;
		  //       }
		
		//Stringify the array
		resp_array = JSON.stringify(resp_array);

		try{
		    switch(AQ.hash.platform)
            {
                case "android":
                    window.android.setHupScreen(resp_array);
                    break;

                case "ios":
                    NativeBridge.call("updateScreen", resp_array);
                    break;
            }
		}catch(err){
            log("Not able to send the data to HAP: "+err);

            window.onNotification({name:"hupScreenUpdated"});
            return;
        }

        return;
	},

    showLoading: function(state)
    {
        if( typeof state != "number" || state < 0 || state > 1 )
        {
            state = 1;
        }

        // Reset the last saved page
        AQ.hash.lastPage = {};

        switch(AQ.hash.platform)
        {
            case "android":
                window.android.setSystemScreen(1, state);
                break;

            case "ios":
                NativeBridge.call("setSystemScreen", JSON.stringify([1, state]) );
                break;
        }

        return;
    },

	/**
	 * Responds to the text request
	 *
	 * @param int appId: application ID
	 * @param json textData: stringified JSON of the id:value map for text data
	 */
	updateText: function(appId, textData)
	{
	    switch(AQ.hash.platform)
        {
            case "android":
                window.android.updateText(appId, textData);
                break;

            case "ios":
                NativeBridge.call("updateText", JSON.stringify([appId, textData]));
                break;
        }
	},


	/**
	 * Responds to the image request
	 *
	 * @param int appId: application id
	 * @param int imgId: image id
     * @param int imgWidth: image width
     * @param int imgHeight: image height
	 * @param string imgData: image source reference
	 */
	updateImage: function(appId, imgId, imgWidth, imgHeight, imgData)
	{
		switch(AQ.hash.platform)
        {
            case "android":
                window.android.updateImage(appId, imgId, imgWidth, imgHeight, imgData);
                break;

            case "ios":
                NativeBridge.call("updateImage", JSON.stringify([appId, imgId, imgWidth, imgHeight, imgData]));
                break;
        }
        return;
	},


    /**
    * Process the TTS request
    *
    * TODO: Add more descriptive comments
    **/
    processTts: function(text, language)
    {
        if(typeof text == "undefined" || text.length == 0)
        {
            return {status:"error", error:"NO_TEXT"};
        }

        language = language || 0;
        //log("processTts request for text : "+text+"; language : "+language);

        switch(AQ.hash.platform)
        {
            case "android":
                var ttsId = window.android.processTts(text, language);
                break;

            case "ios":
                NativeBridge.call("processTts", JSON.stringify([text, language, "objCttsId"]));
                var ttsId = objCttsId;
                break;
        }

        return {status:"success", ttsId:ttsId};
    },


    /**
    * Play the TTS request
    *
    * TODO: Add more descriptive comments
    **/
    playTts: function(ttsId)
    {
        if(typeof ttsId == "undefined")
        {
            return {status:"error", error:"NO_TTS_ID"};
        }

        log("playTts request for ttsId : "+ttsId);

        switch(AQ.hash.platform)
        {
            case "android":
                window.android.playTts(ttsId);
                break;

            case "ios":
                NativeBridge.call("playTts", JSON.stringify([ttsId]));
                break;
        }

        return {status:"success"};
    },

    /**
    * Stop the TTS request
    *
    **/
	stopTts: function(ttsId)
    {
        if(typeof ttsId == "undefined")
        {
            return {status:"error", error:"NO_TTS_ID"};
        }

        log("stopTts request for ttsId : "+ttsId);

        switch(AQ.hash.platform)
        {
            case "android":
                window.android.stopTts(ttsId);
                break;

            case "ios":
                NativeBridge.call("stopTts", JSON.stringify([ttsId]));
                break;
        }

        return {status:"success"};
    },



    /**
    * Process the VR play request
    *
    * TODO: Add more descriptive comments
    **/
    startVR: function(language)
    {
        language = language || 0;
        log("processTts request for language : "+language);

        switch(AQ.hash.platform)
        {
            case "android":
                var vrId = window.android.startVR(language);
                break;

            case "ios":
                // TODO
                NativeBridge.call("startVR", JSON.stringify([language, "objCvrId"]));
                var vrId = objCvrId;
                break;
        }

        return {status:"success", vrId:vrId};
    },


    /**
    * Process the VR stop request
    *
    * TODO: Add more descriptive comments
    **/
    stopVR: function(vrId)
    {
        if(typeof vrId == "undefined")
        {
            return {status:"error", error:"NO_VR_ID"};
        }

        switch(AQ.hash.platform)
        {
            case "android":
                window.android.stopVR(vrId);
                break;

            case "ios":
                // TODO
                NativeBridge.call("stopVR", JSON.stringify([vrId]));
                break;
        }

        return {status:"success"};
    },


    sendAppRequest: function(args)
    {
        var defaults = {appName:"", contentType:"", contentTransferEncoding:"", content:""};
        args = $.extend(true, defaults, args);

        switch(AQ.hash.platform)
        {
            case "android":
                // sendAppRequest(final String appName, final String contentType, final String contentTransferEncoding, final String content)

                var seqNum = window.android.sendAppRequest(args.appName, args.contentType, args.contentTransferEncoding, args.content);
                break;

            case "ios":
                // TODO
                NativeBridge.call("sendAppRequest", JSON.stringify([args.appName, args.contentType, args.contentTransferEncoding, args.content, "objCseqNum"]));
                var seqNum = objCseqNum;
                break;
        }

        return seqNum;
    },
    /**
    * send the Dial Request
    * 
    * phoneNumber is defined in "ITU-T V.250 [14] dial command D" (no"-" or ".")
    *
    **/
    dialNumber: function(phoneNumber)
    {
        switch(AQ.hash.platform)
        {
            case "android":
                var isCallSucceed = window.android.sendPhoneDialRequestToHeadUnit(phoneNumber);
                console.log("is dialNum succeed : " + isCallSucceed );
                break;
                
            case "ios":
                //TODO :
		//NativeBridge.call("dialNumber", JSON.stringify([phoneNumber, "isCallSucceed"]));
                NativeBridge.call("dialNumber", JSON.stringify([phoneNumber]));
                log("dialNumber");
                break;
        }
        
        return ;
    },

    switchToAudioMode : function(id)
    {
    	//TODO : if sendOk == true, send it and sendOk = false; otherwise put into queue
        switch(AQ.hash.platform)
        {
            case "android":
                window.android.sendStreamAudioRequest(id);
                break;

            case "ios":
                // TODO 
                NativeBridge.call("sendStreamAudioRequest", JSON.stringify([id]));
                return false;
                break;
        }

        return true;
    },

    /**
     * Sends audio info to the head unit for display on the meter (aka cluster).
     *
     * @param data audio info to be displayed on the meter
     * <br>source = audio source (e.g. Pandora, iHeartRadio)
     * <br>track = track name (e.g. Heavy Chevy)
     * <br>popup = flag that indicates if the "track" data should popup
     * on the meter screen regardless of if the user is on the audio
     * screen of the meter
     * <br>album = album name (e.g. Boys & Girls)
     * <br>artist = artist name (e.g. Alabama Shakes)
     *
     * @return true if successfully sent to the head unit, otherwise false
     */
    updateMeter : function(data)
    {
        var mCanData = AQ.meter.translate(data);
        log("updateMeter :>> >>> >>" + mCanData);

        var mCanDataBase64 = AQ.util.hexToBase64(mCanData);

        switch(AQ.hash.platform)
        {
            case "android":
                var resp = window.android.sendAudioInfoToHeadUnit( mCanDataBase64 );
                break;

            case "ios":
                // TODO 
                return false;
                break;
        }

        log("updateMeter :>> >>> >>>>>" + resp);

        return resp;
    }

}
