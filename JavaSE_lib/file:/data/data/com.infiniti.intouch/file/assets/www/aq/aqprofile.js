
AQ.profile = {
	profileSyncFiles : {},
	//sync : function(notification){
		//TODO : store in HAP and using getter to get the applist
		//this.profileSyncFiles = notification;
		
		
		/*
		  “availableApps” : [],
		  “updatingApps” : [],
		  “newApps” : [
				{ “name” : “<Application Name>”, “path” : “<Path relative to app root>”} ,
				{ “name” : “<Application Name>”, “path” : “<Path relative to app root>”} 
			]
		*/

		/*
		    “name” : “profile”,
            “state” : “complete”,
            “availableApps” :  [
				{ “name” : “<Application Name>”, “path” : “<Path relative to app root>”} ,
				{ “name” : “<Application Name>”, “path” : “<Path relative to app root>”} 
			]
		*/
	//},

	sync : function(notification){
		switch(notification.state)
		{
			case "start":
				var template_data =  {
			        "template_id" : "f",
			        "template_content" : {
			            "header" : "aq:///images/header.png",
			            "buttons" : {},
			            "text_main" : "Profile sync......",
			            "text_bottom" : {}
			        }
				};
				AQ.app.updateScreen(template_data);
				
				break;
			case "inProgress":
				/*for(var i in notification.availableApps){
					template_data.template_content.list.push({
						"text" : notification.availableApps[i].name,
						"action" : "",
						"value" : ""
					});
				}
				for(var i in notification.updatingApps){
					template_data.template_content.list.push({
						"text" : "Update version : " + notification.updatingApps[i].name,
						"action" : "",
						"value" : ""
					});
				}
				for(var i in notification.newApps){
					template_data.template_content.list.push({
						"text" : "New app : " + notification.newApps[i].name,
						"action" : "",
						"value" : ""
					});
				}*/
				break;
			case "complete":
				
				AQ.app.load("home");
				
				break;
		}
		
		break;
	}
}