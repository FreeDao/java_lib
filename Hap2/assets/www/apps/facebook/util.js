FB.Util = {

    /* Accepts the date string and returns human readable string */
    formatDate: function(arg){
        var obj = new Date(arg);
        var diff = parseInt( (new Date()).getTime() - obj.getTime() )/1000; //in seconds for easier use

        console.log(":::::::::::::::::::::::");
        console.log( arg +" == "+ obj );

        var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var month = months[ obj.getMonth() ];
        var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        var day = days[ obj.getDay() ];
        var date = obj.getDate();
        var year = obj.getFullYear();
        var hour = obj.getHours();
        var minute = obj.getMinutes();

        if(hour > 12){
            hour -= 12;
            var timePostfix = "pm";
        }else if(hour == 12){
            var timePostfix = "pm";
        }else{
            var timePostfix = "am";
        }

        if(minute < 10){
            minute = "0"+minute;
        }


        if( diff < 10 ){ //10 seconds

            return "just now";

        }else if( diff < 60 ){ //1 minute

            return "1 min. ago";

        }else if( diff < 3600 ){ //60 minutes

            return parseInt( diff / 60 )+" min. ago";

        }
        else if( diff < 3600*24 ){ //24 hours

            return parseInt( diff / 3600 )+" hours ago";

        }else if( diff < 3600*24*6 ){ //6 days

            return day;

        }else if( diff > 3600*24*6 ){ //Greater than 7 days

            return month+" "+date;

        }else if( year == (new Date()).getFullYear() ){ //the same year

          return month+" "+date+" at "+hour+":"+minute+""+timePostfix;

        }else{

          return month+" "+date+", "+year+" at "+hour+":"+minute+""+timePostfix;

        }
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