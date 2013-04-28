AQ.util = {

  _tableStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",
  _table : ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").split(""),

  _atob : function (base64)
  {
    if (/(=[^=]+|={3,})$/.test(base64)) throw new Error("String contains an invalid character");
    base64 = base64.replace(/=/g, "");
    var n = base64.length & 3;
    if (n === 1) throw new Error("String contains an invalid character");
    for (var i = 0, j = 0, len = base64.length / 4, bin = []; i < len; ++i) {
      var a = AQ.util._tableStr.indexOf(base64[j++] || "A"), b = AQ.util._tableStr.indexOf(base64[j++] || "A");
      var c = AQ.util._tableStr.indexOf(base64[j++] || "A"), d = AQ.util._tableStr.indexOf(base64[j++] || "A");
      if ((a | b | c | d) < 0) throw new Error("String contains an invalid character");
      bin[bin.length] = ((a << 2) | (b >> 4)) & 255;
      bin[bin.length] = ((b << 4) | (c >> 2)) & 255;
      bin[bin.length] = ((c << 6) | d) & 255;
    };
    return String.fromCharCode.apply(null, bin).substr(0, bin.length + n - 4);
  },

  _btoa : function (bin)
  {
    for (var i = 0, j = 0, len = bin.length / 3, base64 = []; i < len; ++i) {
      var a = bin.charCodeAt(j++), b = bin.charCodeAt(j++), c = bin.charCodeAt(j++);
      if ((a | b | c) > 255) throw new Error("String contains an invalid character");
      base64[base64.length] = AQ.util._table[a >> 2] + AQ.util._table[((a << 4) & 63) | (b >> 4)] +
                              (isNaN(b) ? "=" : AQ.util._table[((b << 2) & 63) | (c >> 6)]) +
                              (isNaN(b + c) ? "=" : AQ.util._table[c & 63]);
    }
    return base64.join("");
  },

  hexToBase64 : function(str)
  {
    if( typeof str == "undefined" || str == null || str.length == 0 )
    {
      return false;
    }

    if(typeof str == "object")
    {
      try{
        str = str.join(" ");
      }catch(e){}
    }

    return AQ.util._btoa(String.fromCharCode.apply(null,
      str.replace(/\r|\n/g, "").replace(/([\da-fA-F]{2}) ?/g, "0x$1 ").replace(/ +$/, "").split(" "))
    );
  },


  base64ToHex : function(str)
  {
    if( typeof str == "undefined" || str == null || str.length == 0 )
    {
      return false;
    }

    for (var i = 0, bin = AQ.util._atob(str.replace(/[ \r\n]+$/, "")), hex = []; i < bin.length; ++i) {
      var tmp = bin.charCodeAt(i).toString(16);
      if (tmp.length === 1) tmp = "0" + tmp;
      hex[hex.length] = tmp;
    }
    return hex.join(" ");
  },

	base64ToArray: function(str) {
		var mapEn = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".split('');
		var mapDe = {};
		for(var i=0; i<64; i++){
			mapDe[mapEn[i]] = i;
		}
		var buf = [];
		var arr = str.split('');
		var map = mapDe;
		var n = arr.length;
		var val;
		var i=0;
		if(n % 4){
			return;
		}
		while(i < n){
			val = (map[arr[ i ]] << 18) |  (map[arr[i+1]] << 12) |  (map[arr[i+2]] << 6)  | (map[arr[i+3]]);
			buf.push(val>>16, val>>8 & 0xFF,   val & 0xFF);
			i += 4;
		}
		while(arr[--n] == '='){
			buf.pop();
		}
		return buf;
	}
}
/** End of AQ Util **/