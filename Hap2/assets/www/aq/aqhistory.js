AQ.historyWrapper = [];


/**
 * AQ.history class manages the navigation history,
 * and has helpful functions to push new data to the
 * history and retrieve/load the current and last pages. 
 */
AQ.history = 
{
	/**
	 * Saves the given JSON to the history.
	 * 
	 * @param json: formatted JSON object
	 */
	save : function(json)
	{
		try{
			AQ.historyWrapper.push(json);
			
			var length = AQ.historyWrapper.length;
			
			//If the length is more than the max history length, remove the last one.
			if(length > AQ.constants.maxHistoryLength)
			{
				AQ.historyWrapper.shift();
			}
		}catch(err){ log("Not able to save history: " +err); }
		
		log("Successfully saved the json to the history.");
		
		return true;
	},
	
	
	/**
	 * Updates the page with the previous page and removes it from the array.
	 */
	loadPrevPage : function()
	{
		//Get the length of the history array
		var length = AQ.historyWrapper.length;
		
		//If there's only 1 entry in the history, return it
		if( length <= 1 )
		{
			//Get the current page data and remove it from the history
			var resp_json = AQ.historyWrapper.pop();
			
			//Update the screen
			AQ.respond.updateScreen( resp_json );
			return;
		}
		
		
		//Remove the current page data
		AQ.historyWrapper.pop();
		
		//Get the previous page data and remove it from the history
		var resp_json = AQ.historyWrapper.pop();
		
		//Update the screen
		AQ.respond.updateScreen( resp_json );
		return;
	},
	
	
	/**
	 * Returns the current page's JSON.
	 */
	getCurrentPage: function()
	{
		var length = AQ.historyWrapper.length;
		
		return AQ.historyWrapper[ length-1 ];
	},
	
	
	/**
	 * Returns the previous page's JSON.
	 */
	getPrevPage: function()
	{
		var length = AQ.historyWrapper.length;
		
		//If there are less than 2 items, get the current page's content
		if( length < 2 )
		{
			return AQ.history.getCurrentPage();
		}
		
		return AQ.historyWrapper[ length-2 ];
	},
	
	
	/**
	 * Clears the navigation history.
	 */
	clear : function()
	{
		AQ.historyWrapper = [];
		return;
	}
};