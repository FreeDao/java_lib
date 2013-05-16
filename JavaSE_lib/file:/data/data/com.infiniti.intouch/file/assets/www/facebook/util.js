FB.Util = {

    /* Accepts the date string and returns human readable string */
    formatDate: function(arg)
    {
    	var obj = null;
    	
    	//console.log(myDate.getDate());
    	try{
        	
	        var iso8601SplitReplace = /[- :T\.Z\+]/;
	        arg = arg.replace(/\+0000/, "Z");//TODO :  Uncaught TypeError: Object 1364371200 has no method 'replace'
	
	        argArray = arg.split(this.iso8601SplitReplace);
	        var obj = new Date(argArray[0], argArray[1]-1, argArray[2], argArray[3], argArray[4], argArray[5]);
    	}catch(e){}
    	
        console.log(":::::::::::::::::::::::");
        console.log( arg +" == "+ obj );// 2013-03-24T22:31:51Z == Invalid Date

        if(obj == null){
        	obj = new Date(arg);
        }

        if( obj.toString() == "Invalid Date" )
        {
            return "";
        }

        var diff = parseInt( (new Date()).getTime() - obj.getTime() )/1000; //in seconds for easier use
        var months = $.t("months");//["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var month = months[ obj.getMonth() ];
        var days = $.t("week_Days");//["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        var day = days[ obj.getDay() ];
        var date = obj.getDate();
        var year = obj.getFullYear();
        var hour = obj.getHours();
        var minute = obj.getMinutes();

        if(hour > 12){
            hour -= 12;
            var timePostfix =  $.t("pm");
        }else if(hour == 12){
            var timePostfix =  $.t("pm");
        }else{
            var timePostfix =  $.t("am");
        }

        if(minute < 10){
            minute = "0"+minute;
        }


        if( diff < 10 ){ //10 seconds

            return $.t("just_Now");

        }else if( diff < 60 ){ //1 minute

            return "1 " + $.t("min_Ago");

        }else if( diff < 3600 ){ //60 minutes

            return parseInt( diff / 60 )+ " " + $.t("min_Ago");

        }
        else if( diff < 3600*24 ){ //24 hours

            return parseInt( diff / 3600 )+" "+ $.t("hour_Ago");

        }else if( diff < 3600*24*6 ){ //6 days

            return day;

        }else if( diff > 3600*24*6 ){ //Greater than 7 days

            return month+" "+date;

        }else if( year == (new Date()).getFullYear() ){ //the same year

          return month+" "+date+" "+ $.t("at") +" "+hour+":"+minute+""+timePostfix;

        }else{

          return month+" "+date+", "+year+" "+ $.t("at") +" "+hour+":"+minute+""+timePostfix;

        }
    },

	//get numbers from text
    //check with the expression and return an array
	parseNumFromText : function(text){        
        //var text  = "sdc2065506522,(206)5506533ldsfskjdfaskdfadfs(206)550-6544fchueufaskdfadfs(206)550 6555dua";
        
        var nums=[];

        var num = "";
        for(var i=0; i<text.length; i++){
        	var currentChar = text.charAt(i);
        	if(!isNaN(currentChar) || currentChar == "(" || currentChar == ")" || currentChar == "-"|| currentChar == "+"){
        		if(currentChar!=" "){
        			num += currentChar;
        		}
        	}else{
        		if(num!=""){        			
        			//TODO : Check and format those numbers
        			/*
        	        var args = {
    	                phoneNumber : num,
    	                regionCode : "US" //TODO : map with language code
    	            }
        	        var formatedNum = FB.Util.parsePhoneNumber(args);
        	        if(formatedNum!=null && formatedNum!=""){
        	        	nums.push(formatedNum);
        	        }
        	        */

        			nums.push(num);
    				num = "";
        		}
        	}
        }
    	if(num!=""){			
			//TODO : Check and format those numbers
    		nums.push(num);
    	}//last char is num

        return nums;
    },
	 //return an array
	parsePhoneNumber : function(arg){
		try {
			var phoneNumber =  arg.phoneNumber; //044 668 18 00
			var regionCode =  arg.regionCode; //CH
			var phoneUtil = i18n.phonenumbers.PhoneNumberUtil.getInstance();
			var numberObj = phoneUtil.parseAndKeepRawInput(phoneNumber, regionCode);

		} catch (e) {
			return null;
		}
		var PNF = i18n.phonenumbers.PhoneNumberFormat;
		return phoneUtil.format(numberObj, PNF.NATIONAL);
	}
}