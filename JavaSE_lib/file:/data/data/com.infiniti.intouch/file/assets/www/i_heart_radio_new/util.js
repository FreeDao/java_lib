iHR.Utils = {
	binaryToBase64 : function(input) {
		var base64_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		var ret = new Array();
		var i = 0;
		var j = 0;
		var char_array_3 = new Array(3);
		var char_array_4 = new Array(4);
		var in_len = input.length;
		var pos = 0;
		while (in_len--) {
			char_array_3[i++] = input[pos++];
			if (i == 3) {
				char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
				char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
				char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
				char_array_4[3] = char_array_3[2] & 0x3f;

				for ( i = 0; (i < 4); i++)
					ret += base64_chars.charAt(char_array_4[i]);
				i = 0;
			}
		}
		if (i) {
			for ( j = i; j < 3; j++)
				char_array_3[j] = 0;

			char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
			char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
			char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
			char_array_4[3] = char_array_3[2] & 0x3f;

			for ( j = 0; (j < i + 1); j++)
				ret += base64_chars.charAt(char_array_4[j]);

			while ((i++ < 3))
			ret += '=';

		}
		return ret;
	},

	strToCharArray : function(str) {
		var arrObj = [];
		if (str) {
			for (var i = 0; i < str.length; i++) {
				arrObj[i] = str.charCodeAt(i);
			}
		}
		return arrObj;
	},

	bin2String : function(array) {
		return String.fromCharCode.apply(String, array);
	},

	bufferDisplay : function(buffer) {
		var cmdStr = "";
		for (var i = 0; i < buffer.length; i++) {
			cmdStr += buffer[i] + ",";
		}
		return cmdStr;
	},
	
	sleep : function(numberMillis) {
		var now = new Date();    
		var exitTime = now.getTime() + numberMillis;    
		while (true) {
			now = new Date();
			if (now.getTime() > exitTime)
			return;
		}
	}
};

console.log("Finished load util.js");
