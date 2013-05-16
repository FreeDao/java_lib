/**
 * Internet Audio M-CAN data translator for Nissan's meter/cluster component.
 */
AQ.meter = {

	// M-CAN constants
	INTERRUPT_DISABLE : 0,
	INTERRUPT_ENABLE : 1,
	UPDATE_NOT_UPDATED : 0,
	UPDATE_UPDATED : 1,
	CHARSET_UTF_8 : 5,
	TRACK_LABEL_TRACK : 1,
	TRACK_NUMBER_FIELD_SIZE : 3, // bytes
	GROUP_LABEL_ALBUM : 2,
	GROUP_NUMBER_FIELD_SIZE : 2, // bytes
	ARTIST_LABEL_ARTIST : 1,

	/**
	 * <p>
	 * Translates the specified meter data to Nissan's Internet_Audio M-CAN
	 * message.
	 * </p>
	 * 
	 * <p>
	 * Limitations:
	 * <br>-Popup/interrupt only available for Track parameter, matches
	 * current Nissan spec.
	 * <br>-"No" sub-parameters in Track and Group set to 0.  Current meter
	 * doesn't display the "No" sub-parameter.
	 * <br>-Always sets the Label sub-parameters as follows: Track=Track,
	 * Group=Album, Artist=Artist.  Not thought to have any affect on the meter
	 * display.
	 * </p>
	 *
	 * @see <a href="https://rm.airbiquity.com/dmsf_files/2448?download=">
	 * Message Set Specification for Multimedia-CAN, M-B02-01, Version 2.5.0,
	 * Section 1.10 Internet_Audio[FunctionID-101h]</a>
	 * @param data
	 * <br>source = audio source (e.g. Pandora, iHeartRadio)
	 * <br>track = track name (e.g. Heavy Chevy)
	 * <br>popup = flag that indicates if the "track" data should popup
	 * on the meter screen regardless of if the user is on the audio
	 * screen of the meter
	 * <br>album = album name (e.g. Boys & Girls).  Note: this actually
	 * translates the the M-CAN "Group" parameter, which we are currently
	 * using "Label" = "Album" for.  Renamed the input to "album" to limit
	 * the confusion with the "artist" parameter to here.
	 * <br>artist = artist name (e.g. Alabama Shakes)
	 *
	 * @return: String hex encoded Internet_Audio M-CAN message
	 */
	translate : function(data)
	{
		var result = [];

		// Interrupt parameter
		// Controls if one of the other sub-parameters (e.g. Internet_Audio_Source,
		// Track, Group, Artist) with "Update" = "Updated" will popup on the
		// meter if the meter is not on the Audio screen.
		result.push((data.popup) ? this.INTERRUPT_ENABLE : this.INTERRUPT_DISABLE);

		// Internet_Audio_Source parameter
		result.push(this.UPDATE_NOT_UPDATED); // Update sub-parameter
		result = result.concat(this._translateTextInfo(data.source));

		// Track parameter
		// Current Nissan spec is to only display popup/interrupt for the
		// Track parameter in some cases.  Only checking for popup here.
		result.push((data.popup) ? this.UPDATE_UPDATED : this.UPDATE_NOT_UPDATED); // Update sub-parameter
		result.push(this.TRACK_LABEL_TRACK); // Label sub-parameter
		result = result.concat(this._bytesFromInt(0, this.TRACK_NUMBER_FIELD_SIZE)); // No sub-parameter
		result = result.concat(this._translateTextInfo(data.track));

		// Group parameter
		result.push(this.UPDATE_NOT_UPDATED); // Update sub-parameter
		result.push(this.GROUP_LABEL_ALBUM); // Label sub-parameter
		result = result.concat(this._bytesFromInt(0, this.GROUP_NUMBER_FIELD_SIZE)); // No sub-parameter
		result = result.concat(this._translateTextInfo(data.album));

		// Artist parameter
		result.push(this.UPDATE_NOT_UPDATED); // Update sub-parameter
		result.push(this.ARTIST_LABEL_ARTIST); // Label sub-parameter
		result = result.concat(this._translateTextInfo(data.artist));

		return this._byte2HexString(result);
	},

    /**
	 * Translates the specified string to the Nissan M-CAN Text_Info parameter.
	 * @private
     * @param str string to be translated
     * @return Array byte array containing M-CAN formatted Text_Info
	 */
	_translateTextInfo : function(str)
	{
		var result = [];
        var utf8Data = this._bytesFromUtf8(str); // convert text to UTF-8 code

		result.push(this.CHARSET_UTF_8); // Character_Set sub-parameter
		result.push(utf8Data.length); // Length sub-parameter
		result = result.concat(utf8Data); // Text_Body sub-parameter

		return result;
	},

	/**
	 * Converts a specified integer to a byte array of specified size.
	 * TODO: duplicated from elsewhere, consider moving to util
	 * @private
	 * @param x integer to be converted
	 * @param arraySize size of array (in bytes) to convert x to
	 * @return Array byte array of specified size containing the value of x
	 */
	_bytesFromInt : function(x, arraySize)
	{
		var bytes = [];
		var i = arraySize;
		do {
			bytes[--i] = x & (255);
			x = x >> 8;
		} while (i);
		return bytes;
	},

	/**
	 * Converts the specified string to a byte array containing
	 * the UTF-8 code for each character in the string.
	 * TODO: duplicated from elsewhere, consider moving to util
	 * @private
	 * @param str string to be converted
	 * @return Array byte array containing the UTF-8 code for each
	 * character in the specified string.
	 */
	_bytesFromUtf8 : function(str) {
		var byteArray = [];
		for (var i = 0; i < str.length; i++)
			if (str.charCodeAt(i) <= 0x7F)
				byteArray.push(str.charCodeAt(i));
			else {
				var h = encodeURIComponent(str.charAt(i)).substr(1).split('%');
				for (var j = 0; j < h.length; j++)
					byteArray.push(parseInt(h[j], 16));
			}
		return byteArray;
	},

	/**
	 * Converts the specified array of bytes to a string containing
	 * the hex representation of each byte.
	 * TODO: duplicated from elsewhere, consider moving to util
	 * @private
	 * @param array byte array to be converted
	 * @return String string containing the space delimited hex representation
     * of each byte in the specified byte array
	 */
	_byte2HexString : function(array)
	{
		var hexStrings = "";
	    $(array).each(function(index, item) {
	    	var hexString = parseInt(item).toString(16);
	     	if (hexString.length < 2) {
	      		hexString = "0" + hexString;
	     	}
	     	hexStrings = hexStrings + hexString.toUpperCase() + " ";
	    });
	    return hexStrings.trim();
	}

};
