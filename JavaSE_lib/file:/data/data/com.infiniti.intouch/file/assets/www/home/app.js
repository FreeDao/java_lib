log("Home app is initializing...");
var APP = new Home();
APP.init();


/**
 * Home app class
 */
function Home()
{
	/**
	 * Initialize the app and respond the first page
	 */
	this.init = function()
	{
		log("Home Initialized");
		var applist = this.model.getAppList();

		this.display.main(applist);

		AQ.app.exitAudioMode();
	};


	var that = this;
	/**
	 * Holds the available states/pages for this app
	 */
	this.state =
	{
		"app_list" : {
			"template_id" : "g",
			"screen_id" : 0,
			"template_content" : {
			 	"header" : "aq:///images/header.png",
			 	"list_pattern" : 1,
			 	"buttons" : {},
			 	"list" : []
			}
		}
	
	
	};
	
	this.display = {
		    main: function(apps)
		    {
		        var template = $.extend(true, {}, that.state.app_list);
		        
		        //Populate the list of apps
		        for(var i=0; i<apps.length; i++)
		        {
		            var image1 = apps[i].app_icon;
		            
		            template.template_content.list.push({
		                "image1" : image1,
		                "text" : apps[i].name,
		                "action" : "loadApp",
		                "value" : apps[i].folder
		            })
		        }
		        
		        AQ.app.updateScreen( template );
		    }
	};
	
	
	this.model= {
	    getAppList: function()
	    {
	        //TODO: Once the HAP provides an API to get the list of apps, implement it here
	        //return window.java.getAppList();
	    	 var apps = [
	 	   			{name:"Pandora", folder:"pandora", app_icon : "file:///pandora/images/launcher.png"},
	 	            {name:"Facebook", folder:"facebook", app_icon : "file:///facebook/images/launcher.png"},
	 	            {name:"iHeartRadio", folder:"i_heart_radio", app_icon : "file:///i_heart_radio/images/launcher.png"},
	 	            {name:"NBC News", folder:"nbc_news", app_icon : "file:///nbc_news/images/launcher.png"},
	 	            {name:"Template Testing Suite", folder:"test_suite",app_icon : "file:///test_suite/images/launcher.png"},
	 	            {name:"Audio Tester", folder:"audio_tester", app_icon : "file:///audio_tester/images/launcher.png"}
	 	    ];
	        return apps;
	    }
	};
	


	/**
	 * Holds the set of actions available on this app
	 */
	this.actions = {
		"loadApp" : function(appName){
			AQ.app.load( appName );
		}
	};


	/**
	 * Handles the actions
	 */
	this.handleAction = function( action, value )
	{
		try{
			this.actions[action]( value );
		}catch(err){
			log("Unable to complete action: "+action);
			log(err);
		}
	};
}//end of constructor