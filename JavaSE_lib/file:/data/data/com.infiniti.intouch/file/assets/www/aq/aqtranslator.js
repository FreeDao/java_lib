/**
 * Template parser. Converts the formatted JSON to an array of decimals integers
 * to send to Java layer.
 */
AQ.template = {

	/**
	 * Converts the given array of JSONs to an array of decimals based
	 * on the agreement between HAP and HMI.
	 * Structure of the final array:
	 * [length, value, length, value, ...]
	 *
	 * The lengh describes the length of the next value in bytes.
	 *
	 * @param: data (an array of json with keys "value" in decimal and "length" in bytes)
	 * @return: array
	 */
	translate : function(data)
	{
	    log("*****RESP_BINARY_DATA*****");

	  	var result = [];
	  	for(var i=0; i<data.length; i++)
	  	{
	  	    var b16 = data[i].value.toString(16);
	  	    var prepend = (data[i].length * 2) - b16.length;
	  	    for(var x=0; x<prepend; x++)
	  	    {
	  	        b16 = "0" + b16;
	  	    }

	  	    // Print out the binary messages to be sent for debugging
	  	    log( "0x"+ b16.toUpperCase() +" -- "+ data[i].name );

	  	    for(var x=2; x<=b16.length; x+=2)
            {
                var byte = b16.slice(x-2, x);
                result.push(byte);
            }
	  	}
		log("*****END OF RESP_BINARY_DATA*****");
		//log( "Actions assigned: "+ JSON.stringify(AQ.hash.actions, undefined, 2) );
		//log( "Text assigned: "+ JSON.stringify(AQ.hash.text, undefined, 2) );
		//log( "Images assigned: "+ JSON.stringify(AQ.hash.images, undefined, 2) );
		return result;
	},


	/**
	 * General template parser
	 */
	parse : function(data)
	{
		//Clear the actions map
		AQ.storage.clearAction();

		//Initialize an empty array to collect results
		var result = [];

		//Template id is prepended later in the switch() - 1 byte

		//Reserve
		result.push({value:0, length:1, name: "reserve"});
		result.push({value:0, length:1, name: "reserve"});
		result.push({value:0, length:1, name: "reserve"});

		//Application id - 2 bytes
		var appId = AQ.app.getAppId();
		result.push({value:appId, length:2, name: "application id"});

		//Screen id
		result.push({value: AQ.storage.generateScreenId() , length:2, name: "screen id"});

		switch( data.template_id.toLowerCase() )
		{
			case "a" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:1, length:1, name:"template id"});
				return AQ.template.a(data, result);

			case "b" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:2, length:1, name:"template id"});
				return AQ.template.b(data, result);

			case "c" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:3, length:1, name:"template id"});
				return AQ.template.c(data, result);
/*
			case "d" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:4, length:1, name:"template id"});
				return AQ.template.d(data, result);

			case "e" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:5, length:1, name:"template id"});
				return AQ.template.e(data, result);
*/
			case "f" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:6, length:1, name:"template id"});
				return AQ.template.f(data, result);

			case "g" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:7, length:1, name:"template id"});
				return AQ.template.g(data, result);

			case "h" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:8, length:1, name:"template id"});
				return AQ.template.h(data, result);

			// Test template
			case "j" :
				//Template id - 1 byte - prepending it to the results array
				result.unshift({value:10, length:1, name:"template id"});
				return AQ.template.j(data, result);
		}
	},


	/**
	 * Template A
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("a"/"A"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"list_pattern" : (1/2/3),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 * 					"image" : (image url),
	 *	 				"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 *		 	},
     *          "active_key" : (1 to list.length),
	 *		 	"list" : [{
	 *		 		"image1" : (image url),
	 *		 		"text" : (string),
	 *		 		"action" : (string),
	 * 				"value" : (string)
	 *		 	}]
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		| img08 | text01           | img09 | img10 | /\ |  <- List pattern 3. For other list patterns, see _parseListPattern()
	 * 		|                                          |    |
	 * 		| img11 | text02           | img12 | img13 |    |
	 * 		|                                          |    |
	 * 		| img14 | text03           | img15 | img16 |    |
	 * 		|                                          |    |
	 * 		| img17 | text04           | img18 | img19 | \/ |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */
	a : function(data, result)
	{
		// log("Original number of items: "+ data.template_content.list.length);

		//If more than 2 list items are passed, max only at 20
		var listLength = data.template_content.list.length > 20 ? 20 : data.template_content.list.length;

		//Number of list items - 1 byte
		result.push({value:listLength, length:1, name: "number of list items"});

		//Active key id - 1 byte
		var activeKey = 1; //Default value to 1
		if( typeof data.template_content.active_key != "undefined" &&
		    data.template_content.active_key <= listLength &&
		    data.template_content.active_key > 0 )
		{
			activeKey = data.template_content.active_key;
		}
		result.push({value:activeKey, length:1, name: "active key id"});

		//List pattern - 1 byte
		var listPattern = 1;
		if( typeof data.template_content.list_pattern != "undefined" &&
		    data.template_content.list_pattern >= 1 &&
		    data.template_content.list_pattern <= 3 )
		{
			listPattern = data.template_content.list_pattern;
		}
		result.push({value:listPattern, length:1, name: "list pattern id"});

		//Reserve - 1 byte
		result.push({value:0, length:1, name: "reserve"});


		//based on the list pattern, call relevant function to parse it
		var list = AQ.template._parseListPattern( data.template_content, {templateId:1, patternId:listPattern} );


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},


	/**
	 * List Pattern 1 - | image | text                         |
	 * List Pattern 2 - | image | text                 | image |
	 * List Pattern 3 - | image | text         | image | image |
	 *
	 * Designated only for template A
	 */
	_parseListPattern : function(content, args)
	{
		//TODO:: Pass the template ID here from the calling function
		var defaults = {
			patternId : 1,
			includeButtons : true,
			templateId : 1
		}
		args = $.extend(defaults, args, true);
		var patternId = args.patternId;
		var includeButtons = args.includeButtons;
		var templateId = args.templateId;

		var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
		var imageId = 0;

		//Header image id
		imageId++;
		var header =  (typeof  content.header.value != "undefined")? content.header.value : content.header;
		
		list.image_ids.push({
			value : AQ.storage.getImageId( header, imageId, templateId ),
			length : 2,
			name: "image id - header"
		});

		//Header image hatching
		var hatchingInfoHeader =  AQ.policy.getPolicyRules(content.header.policy);
		list.image_hatching.push({value:hatchingInfoHeader, length:1, name: "image hatching - header"});

		if( includeButtons === true )
		{
			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;

				var hatchingInfoButtons = 0;
				
				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button"+key });
	                }else{
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, templateId ),
	                        length : 2,
	                        name: "image id - button"+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
				    hatchingInfoButtons = AQ.policy.getPolicyRules(content.buttons[key].policy);
				}

				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButtons, length:1, name:"image hatching - button "+key});
			}
		}//end of if includeButtons


		//Handle the case when there's no list items are passed
		if(typeof content.list == "undefined")
		{
			content.list = [];
		}

		//If more than 2 list items are passed, max only at 20
		var listLength = content.list.length > 20 ? 20 : content.list.length;

		//Go through the content list and append text ids, image ids, and hatching info
		for(var i=0; i<listLength; i++)
		{
		    var listItemCount = i+1;
		    
		    var listItemPolicy =  (typeof content.list[i].policy != "undefined")? content.list[i].policy : [];

			imageId++;
			//Image id
			if(typeof content.list[i].image1 != "undefined"){
				var image1 = (typeof content.list[i].image1.value != "undefined")? content.list[i].image1.value : content.list[i].image1;
				list.image_ids.push({
					value : AQ.storage.getImageId( image1, imageId, templateId ),
					length : 2,
					name:"image id - image 1 in list item "+listItemCount
				});
				//Image hatching
				var image1_policy = (typeof content.list[i].image1.policy != "undefined")? content.list[i].image1.policy : [];
				var hatchingInfo_Image1 =  AQ.policy.getPolicyRules(image1_policy.concat(listItemPolicy));
				list.image_hatching.push({value:hatchingInfo_Image1,length:1, name:"image hatching - image 1 in list item "+listItemCount});

			}else{
				list.image_ids.push({
					value : 0,
					length : 2,
					name:"image id - image 1 in list item "+listItemCount
				});
				list.image_hatching.push({value:0,length:1, name:"image hatching - image 1 in list item "+listItemCount});
			}
			

			//List Pattern 3
			imageId++;
			if( patternId == 3 ) //if the list pattern is 3, put the image2 in the 2nd area
			{
				//Image id
				var image2 = (typeof content.list[i].image2.value != "undefined")? content.list[i].image2.value : content.list[i].image2;
				list.image_ids.push({
					value : AQ.storage.getImageId( image2, imageId, templateId ),
					length : 2,
					name:"image id - image 2 in list item "+listItemCount
				});
			}else
			{
				//Image id - null
				list.image_ids.push({
					value : 0,
					length : 2,
					name:"image id - image 2 in list item "+listItemCount
				});
			}
			//Image hatching
			var hatchingInfo_Image2 = 0;
			if(typeof content.list[i].image2 != "undefined"){
				var image2_policy = (typeof content.list[i].image2.policy !="undefined")?content.list[i].image2.policy : [];
				hatchingInfo_Image2 = AQ.policy.getPolicyRules(image2_policy.concat(listItemPolicy));
			}
            list.image_hatching.push({value:hatchingInfo_Image2,length:1, name:"image hatching - image 2 in list item "+listItemCount});

			//List Pattern 2 and 3
			imageId++;
			if( patternId == 2 ) //if the list pattern is 2, put the image2 in the 3rd area
			{
				//Image id
				var image_2 = (typeof content.list[i].image2.value != "undefined")? content.list[i].image2.value : content.list[i].image2;
				list.image_ids.push({
					value : AQ.storage.getImageId( image_2, imageId, templateId ),
					length : 2,
					name:"image id - image 3 in list item "+listItemCount
				});
			}else if( patternId == 3 ) //if the list pattern is 3, put the image3 in the 3rd area
			{
				//Image id
				var image_3 = (typeof content.list[i].image3.value != "undefined")? content.list[i].image3.value : content.list[i].image3;
				list.image_ids.push({
					value : AQ.storage.getImageId( image_3, imageId, templateId ),
					length : 2,
					name:"image id - image 3 in list item "+listItemCount
				});
			}else
			{
				//Image id - null
				list.image_ids.push({
					value : 0,
					length : 2,
					name:"image id - image 3 in list item "+listItemCount
				});
			}
			//Image hatching
			var hatchingInfo_Image3 = 0;
			if(typeof content.list[i].image3 !="undefined"){
				var image3_policy = (typeof content.list[i].image3.policy !="undefined")? content.list[i].image3.policy : [];
				hatchingInfo_Image3 = AQ.policy.getPolicyRules(image3_policy.concat(listItemPolicy));
			}
            list.image_hatching.push({value:hatchingInfo_Image3,length:1, name:"image hatching - image 3 in list item "+listItemCount});

			//Text id
            var text = (typeof content.list[i].text.value != "undefined")? content.list[i].text.value : content.list[i].text;
			list.text_ids.push({
				value : AQ.storage.getTextId( text ),
				length : 2,
				name:"text id - list item "+listItemCount
			});

			//Text hatching
			var text_policy = (typeof content.list[i].text.policy !="undefined")?content.list[i].text.policy:[];
			var hatchingInfoText = AQ.policy.getPolicyRules(text_policy.concat(listItemPolicy));
			list.text_hatching.push({value:hatchingInfoText,length:1, name:"text hatching - list item "+listItemCount});

			//Save list actions
			if(typeof content.list[i].action != "undefined" || typeof content.list[i].value != "undefined")
			{
				AQ.storage.setAction( listItemCount, content.list[i].action, content.list[i].value );
			}
		}

		// log("AQ.hash.actions after assigning:");
		// log( JSON.stringify( AQ.hash.actions, undefined, 2 ) );

		return list;
	},


	/**
	 * Template B
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("b"/"B"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 * 					"image" : (image url),
	 *	 				"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 *		 	},
	 *		 	"text_left_top" : (string),
	 * 			"text_left_bottom" : (string),
	 * 			"text_right_main" : (string),
	 * 			"image_left" : (image url)
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		| |   text01   |   __________________________   |
	 * 		|  ____________   |                          |  |
	 * 		| |   img08    |  |                          |  |
	 * 		| |____________|  |          text03          |  |
	 * 		|                 |                          |  |
	 * 		| |   text02   |  |__________________________|  |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */
	b: function(data, result)
	{
		 console.log(JSON.stringify(data));
		//Number of list items - fixed - 1 byte
		result.push({value:0, length:1, name:"number of list items"});

		//Active key id - fixed - 1 byte
		result.push({value:0, length:1, name:"active key id"});

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});

		//Anonymous function to parse and group the content of the page
		var list = (function(content)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			imageId++;
			//Header image id

			var header = (typeof  content.header.value != "undefined")? content.header.value:content.header;
			list.image_ids.push({
				value : AQ.storage.getImageId( header, imageId, 2 ),
				length : 2,
				name : "image id - header"
			});

			//Header image hatching
			var hatchingInfoHeader = AQ.policy.getPolicyRules(content.header.policy);
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"image hatching - header"});

			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;
				
				var hatchingInfoButton = 0;
				
				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button"+key });
	                }else{
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, 1 ),
	                        length : 2,
	                        name: "image id - button"+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
					
					if( typeof content.buttons[key].policy != "undefined"){
						hatchingInfoButton = AQ.policy.getPolicyRules(content.buttons[key].policy);
					}
				}
				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButton, length:1, name:"image hatching - button "+key});
			}

			//Left side image id
			imageId++;
			list.image_ids.push({
				value : AQ.storage.getImageId( content.image_left.value, imageId, 2 ),
				length : 2,
				name:"image id - left side image"
			});
			//Left side image hatching
			var hatchingInfoImages = AQ.policy.getPolicyRules(content.image_left.policy);
			list.image_hatching.push({value:hatchingInfoImages,length:1, name:"image hatching - left side image"});

			//Left top text id - text01
			var text_left_top = (typeof content.text_left_top.value != "undefined")? content.text_left_top.value: content.text_left_top;
			list.text_ids.push({
				value : AQ.storage.getTextId( text_left_top ),
				length : 2,
				name : "text id - left top"
			});
			//Left top text hatching
			var hatchingInfoText_left_top = AQ.policy.getPolicyRules(content.text_left_top.policy);
			list.text_hatching.push({value:hatchingInfoText_left_top,length:1,name : "text hatching info - left top"});
			
			//Left bottom text id - text02
			var text_left_bottom = (typeof content.text_left_bottom.value != "undefined")? content.text_left_bottom.value: content.text_left_bottom;
			list.text_ids.push({
				value : AQ.storage.getTextId( text_left_bottom ),
				length : 2,
				name : "text id - left bottom"
			});
			//Left bottom text hatching
			var hatchingInfoText_left_bottom = AQ.policy.getPolicyRules(content.text_left_bottom.policy);
			list.text_hatching.push({value:hatchingInfoText_left_bottom,length:1,name : "text hatching info - left bottom"});

			
			//Right main text id - text03
			var text_right_main = (typeof content.text_right_main.value != "undefined")? content.text_right_main.value: content.text_right_main;
			list.text_ids.push({
				value : AQ.storage.getTextId( text_right_main ),
				length : 2,
				name : "text id - right main"
			});
			//Right main text hatching
			var hatchingInfoText_rightmain = AQ.policy.getPolicyRules(content.text_right_main.policy);
			list.text_hatching.push({value:hatchingInfoText_rightmain,length:1,name : "text hatching info - right main"});

			return list;
		})( data.template_content ); //end of the anonymous function to group the content of the page

		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
        result = this._setActions(data, result);

		return AQ.template.translate(result);
	},



	/**
	 * Template C
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("c"/"C"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 * 					"image" : (image url),
	 *	 				"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 *		 	},
     *          "active_key" : (1 to list.length),
	 *		 	"list" : [{
	 *		 		"text" : (string),
	 *		 		"action" : (string),
	 * 				"value" : (string)
	 *		 	}]
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		| text01                                   | /\ |
	 * 		|                                          |    |
	 * 		| text02                                   |    |
	 * 		|                                          |    |
	 * 		| text03                                   |    |
	 * 		|                                          |    |
	 * 		| text04                                   | \/ |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */
	c: function(data, result)
	{
		// log("Original number of items: "+ data.template_content.list.length);

		//If more than 2 list items are passed, max only at 20
		var listLength = data.template_content.list.length > 20 ? 20 : data.template_content.list.length;

		//Number of list items - 1 byte
		result.push({value:listLength, length:1, name:"number of list items"});

		//Active key id - 1 byte
		var activeKey = 1; //Default value to 1
		if( typeof data.template_content.active_key != "undefined" &&
		    data.template_content.active_key <= listLength &&
		    data.template_content.active_key > 0 )
		{
			activeKey = data.template_content.active_key;
		}
		result.push({value:activeKey, length:1, name: "active key id"});

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});


		//Anonymous function to parse and group the content of the page
		var list = (function(content, listLength)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			//Header image id
			imageId++;
			
			var header = (typeof  content.header.value != "undefined")? content.header.value:content.header;
			list.image_ids.push({
				value : AQ.storage.getImageId( header, imageId, 3),
				length : 2,
				name : "image id - header"
			});

			//Header image hatching
			var hatchingInfoHeader = AQ.policy.getPolicyRules(content.header.policy);
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"hatching - header image"});

			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;
				var hatchingInfoButtons = 0 ;
				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
	                }else
	                {
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, 3 ),
	                        length : 2,
	                        name: "image id - button "+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
				    
					if(typeof content.buttons[key].policy != "undefined"){
						hatchingInfoButtons = AQ.policy.getPolicyRules(content.buttons[key].policy);
					}
				}
				
				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButtons, length:1, name:"image hatching - button "+key});
			}


			//Handle the case when there's no list items are passed
			if(typeof content.list == "undefined")
			{
				content.list = [];
			}

			//Go through the content list and append text ids and hatching info
			for(var i=0; i<listLength; i++)
			{
				var listItemCount = i+1;

				//Text id
				list.text_ids.push({
					value : AQ.storage.getTextId( content.list[i].text),
					length : 2,
					name:"text id - list item "+listItemCount
				});

				var hatchingInfoText = AQ.policy.getPolicyRules(content.list[i].policy);
				list.text_hatching.push({value:hatchingInfoText,length:1,name:"text hatching - list item "+listItemCount});


				//Save list actions
				if(typeof content.list[i].action != "undefined" || typeof content.list[i].value != "undefined")
				{
					AQ.storage.setAction( listItemCount, content.list[i].action, content.list[i].value );
				}
			}

			return list;
		})( data.template_content, listLength ); //end of the anonymous function to group the content of the page


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},



	/**
	 * Template D
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("d"/"D"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 * 					"image" : (image url),
	 *	 				"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 *		 	},
	 * 			"image_left" : (image url),
     *          "active_key" : (1 to list.length),
	 *		 	"list" : [{
	 *		 		"image" : (image url),
	 *		 		"text" : (string),
	 *		 		"action" : (string),
	 * 				"value" : (string)
	 *		 	}]
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		|  _______________  | img09 | text01       | /\ |
	 * 		| |               |                        |    |
	 * 		| |               | | img10 | text02       |    |
	 * 		| |     img08     |                        |    |
	 * 		| |               | | img11 | text03       |    |
	 * 		| |               |                        |    |
	 * 		| |_______________| | img12 | text04       | \/ |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */
/*
	d: function(data, result)
	{
		// log("Original number of items: "+ data.template_content.list.length);

		//If more than 2 list items are passed, max only at 20
		var listLength = data.template_content.list.length > 20 ? 20 : data.template_content.list.length;

		//Number of list items - 1 byte
		result.push({value:listLength, length:1, name:"number of list items"});

		//Active key id - 1 byte
		var activeKey = 1; //Default value to 1
		if( typeof data.template_content.active_key != "undefined" &&
		    data.template_content.active_key <= listLength &&
		    data.template_content.active_key > 0 )
		{
			activeKey = data.template_content.active_key;
		}
		result.push({value:activeKey, length:1, name: "active key id"});

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});

		//Anonymous function to parse and group the content of the page
		var list = (function(content, listLength)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			//Get the policy data / hatching info
			var hatchingInfoHeader = AQ.policy.getPolicyHeader();
			var hatchingInfoImages = AQ.policy.getPolicyImage();
			var hatchingInfoButtons = AQ.policy.getPolicyButton();
			var hatchingInfoText = AQ.policy.getPolicyText();

			//Header image id
			imageId++;
			list.image_ids.push({
				value : AQ.storage.getImageId( content.header, imageId, 4 ),
				length : 2,
				name: "image id - header"
			});

			//Header image hatching
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"image hatching - header"});

			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;

				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
	                }else
	                {
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, 3 ),
	                        length : 2,
	                        name: "image id - button "+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
				}

				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButtons, length:1, name:"image hatching - button "+key});
			}


			//Left image id
			imageId++;
			list.image_ids.push({
				value : AQ.storage.getImageId( content.image_left, imageId, 4 ),
				length : 2,
				name : "image id - main left"
			});

			//Left image hatching
			list.image_hatching.push({value:hatchingInfoImages, length:1, name:"image hatching - main left"});


			//Handle the case when there's no list items are passed
			if(typeof content.list == "undefined")
			{
				content.list = [];
			}

			//Go through the content list and append text ids, image ids, and hatching info
			for(var i=0; i<listLength; i++)
			{
				var listItemCount = i+1;

				//Image id
				imageId++;
				list.image_ids.push({
					value : AQ.storage.getImageId( content.list[i].image, imageId, 4 ),
					length : 2,
					name : "image id - list item "+listItemCount
				});

				//Image hatching
				list.image_hatching.push({value:hatchingInfoImages,length:1,name:"image hatching - list item "+listItemCount});


				//Text id
				list.text_ids.push({
					value : AQ.storage.getTextId( content.list[i].text),
					length : 2,
					name : "text id - list item "+listItemCount
				});

				//Text hatching
				list.text_hatching.push({value:hatchingInfoText,length:1,name:"text hatching - list item "+listItemCount});


				//Save list actions
				if(typeof content.list[i].action != "undefined" || typeof content.list[i].value != "undefined")
				{
					AQ.storage.setAction( listItemCount, content.list[i].action, content.list[i].value );
				}
			}

			return list;
		})( data.template_content, listLength ); //end of the anonymous function to group the content of the page


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},
*/
	/**
	 * Template E
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("e"/"E"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 * 					"image" : (image url),
	 *	 				"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 *		 	},
	 *		 	"image_main" : (image url),
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		|   _________________________________________   |
	 * 		|  |                                         |  |
	 * 		|  |                                         |  |
	 * 		|  |                img08                    |  |
	 * 		|  |                                         |  |
	 * 		|  |                                         |  |
	 * 		|  |_________________________________________|  |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */
/*
	e: function(data, result)
	{
		//Number of list items - fixed - 1 byte
		result.push({value:0, length:1, name:"number of list items"});

		//Active key id - fixed - 1 byte
		result.push({value:1, length:1, name:"active key id"});

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});


		//Anonymous function to parse and group the content of the page
		var list = (function(content)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			//Get the policy data / hatching info
			var hatchingInfoHeader = AQ.policy.getPolicyHeader();
			var hatchingInfoImages = AQ.policy.getPolicyImage();
			var hatchingInfoButtons = AQ.policy.getPolicyButton();
			var hatchingInfoText = AQ.policy.getPolicyText();

			//Header image id
			imageId++;
			list.image_ids.push({
				value : AQ.storage.getImageId( content.header, imageId, 5 ),
				length : 2,
				name : "image id - header"
			});

			//Header image hatching
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"image hatching - header"});


			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;

				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
	                }else
	                {
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, 3 ),
	                        length : 2,
	                        name: "image id - button "+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
				}

				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButtons, length:1, name:"image hatching - button "+key});
			}


			//Main image id
			imageId++;
			list.image_ids.push({
				value : AQ.storage.getImageId( content.image_main, imageId, 5 ),
				length : 2,
				name : "image id - main image"
			});

			//Image hatching
			list.image_hatching.push({value:hatchingInfoImages,length:1,name:"image hatching - main image"});


			return list;
		})( data.template_content ); //end of the anonymous function to group the content of the page


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},
*/


	/**
	 * Template F
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("f"/"F"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"text_main" : (string),
	 * 			"text_bottom" : {
	 * 				"1" : {},
	 * 				"2" : {},
	 * 				"3" : {
	 * 					"text" : (string),
	 * 					"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 * 			}
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		|   _________________________________________   |
	 * 		|  |                                         |  |
	 * 		|  |  text01                                 |  |
	 * 		|  |                                         |  |
	 * 		|  |                                         |  |
	 * 		|  |                                         |  |
	 * 		|  | --------------------------------------- |  |
	 * 		|  |   text02   |    text03     |   text04   |  |
	 * 		|  |_________________________________________|  |
	 * 		|_______________________________________________|
	 */
	f: function(data, result)
	{
		//Number of list items - fixed - 1 byte
		result.push({value:0, length:1, name:"number of list items"});

		//Active key id - fixed - 1 byte
		result.push({value:1, length:1, name:"active key id"});
		/*
		//Active key id - fixed - 1 byte
		var activeKey = 1; //Default value to 1
		if( typeof data.template_content.active_key != "undefined" &&
		    data.template_content.active_key <= 3 && data.template_content.active_key > 0 )
		{
			activeKey = data.template_content.active_key;
		}
		result.push({value:activeKey, length:1, name: "active key id"});
		*/

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});


		//Anonymous function to parse and group the content of the page
		var list = (function(content)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			//Header image id
			imageId++;
			var header = (typeof content.header.value != "undefined") ? content.header.value : content.header;
			list.image_ids.push({
				value : AQ.storage.getImageId( header, imageId, 6 ),
				length : 2,
				name : "image id - header"
			});
			//Header image hatching
			var hatchingInfoHeader = AQ.policy.getPolicyRules(content.header.policy);
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"image hatching - header"});

			//Main text id
			var text_main = (typeof content.text_main.value != "undefined") ? content.text_main.value : content.text_main;
			list.text_ids.push({
				value : AQ.storage.getTextId( text_main ),
				length : 2,
				name : "text id - main text"
			});
			//Text hatching
			var hatchingInfoText = AQ.policy.getPolicyRules(content.text_main.policy);
			list.text_hatching.push({value:hatchingInfoText,length:1,name:"text hatching - main text"});

			if(typeof content.text_bottom == "undefined")
			{
				content.text_bottom = {};
			}
			//Bottom text ids
			for( var i=1; i<=3; i++ )
			{

				var hatchingInfoButtons = 0;
				
				if( typeof content.text_bottom[i] == 'undefined' )
				{
					list.text_ids.push({ value:0, length:2, name:"text id - text item "+i });
				}else{
					list.text_ids.push({
						value : AQ.storage.getTextId( content.text_bottom[i].text ),
						length : 2,
						name:"text id - text item "+i
					});

					//Save text actions
					if(typeof content.text_bottom[i].action != "undefined" || typeof content.text_bottom[i].value != "undefined")
					{
						AQ.storage.setAction(i, content.text_bottom[i].action, content.text_bottom[i].value);
					}
					
					hatchingInfoButtons = AQ.policy.getPolicyRules(content.text_bottom[i].policy);
				}

				//Bottom text hatching
				list.text_hatching.push({value:hatchingInfoButtons,length:1,name:"text hatching - text item "+i});
			}

			return list;
		})( data.template_content ); //end of the anonymous function to group the content of the page


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},


	/**
	 * Template G
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("g"/"G"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"list_pattern" : (1/2/3),
     *          "active_key" : (1 to list.length),
	 *		 	"list" : [{
	 *		 		"image1" : (image url),
	 *		 		"text" : (string),
	 *		 		"action" : (string),
	 * 				"value" : (string)
	 *		 	}]
	 *		 }
	 *	  }
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		| img02 | text01           | img03 | img04 | /\ |  <- List pattern 3. For other list patterns, see _parseListPattern()
	 * 		|                                          |    |
	 * 		| img05 | text02           | img06 | img07 |    |
	 * 		|                                          |    |
	 * 		| img08 | text03           | img09 | img10 |    |
	 * 		|                                          |    |
	 * 		| img11 | text04           | img12 | img13 |    |
	 * 		|                                          |    |
	 * 		| img14 | text05           | img15 | img16 | \/ |
	 * 		|_______________________________________________|
	 */
	g : function(data, result)
	{
		// log("Original number of items: "+ data.template_content.list.length);

		//If more than 2 list items are passed, max only at 20
		var listLength = data.template_content.list.length > 20 ? 20 : data.template_content.list.length;

		//Number of list items - 1 byte
		result.push({value:listLength, length:1, name: "number of list items"});

		//Active key id - 1 byte
		var activeKey = 1; //Default value to 1
		if( typeof data.template_content.active_key != "undefined" &&
		    data.template_content.active_key <= listLength &&
		    data.template_content.active_key > 0 )
		{
			activeKey = data.template_content.active_key;
		}
		result.push({value:activeKey, length:1, name: "active key id"});

		//List pattern - 1 byte
		var listPattern = 1;
		if( typeof data.template_content.list_pattern != "undefined" &&
		    data.template_content.list_pattern >= 1 &&
		    data.template_content.list_pattern <= 3 )
		{
			listPattern = data.template_content.list_pattern;
		}
		result.push({value:listPattern, length:1, name: "list pattern id"});

		//Reserve - 1 byte
		result.push({value:0, length:1, name: "reserve"});


		//based on the list pattern, call relevant function to parse it
		var list = AQ.template._parseListPattern( data.template_content, {templateId:7, patternId:listPattern, includeButtons:false} );


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},


	/**
	 * Template H
	 *
	 * The sample JSON:
	 *	{
	 *		"template_id" : ("h"/"H"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 *					"image" : (image url),
	 *	 				"action" : (string),
	 *					"value" : (string)
	 *				}
	 *		 	},
	 *		 	main_image: (string),
	 *		 	items : {
	 *				"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {
	 * 					"image" : (image url),
	 *	 				"text" : (string)
	 * 				}
	 *		 	}
	 *		}
	 *	}
	 *
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		|  image08 |  text01           |  ____________  |
	 * 		|                                 |          |  |
	 * 		|  image09 |  text02           |  | image12  |  |
	 * 		|                                 |__________|  |
	 * 		|  image10 |  text03           |                |
	 * 		|                                               |
	 * 		|  image11 |  text04           |                |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */
	// TODO
	h: function(data, result)
	{
		// log("Original number of items: "+ data.template_content.list.length);
		
		var templateId = 8;
		
		//Number of list items: No available - fixed - 1 byte
		result.push({value:0, length:1, name:"number of list items"});

		//Active key id - 1 byte
		/*
		var activeKey = 1; //Default value to 1
		
		if( typeof data.template_content.active_key != "undefined" &&
		    data.template_content.active_key <= 4 &&
		    data.template_content.active_key > 0 )
		{
			activeKey = data.template_content.active_key;
		}*/
		result.push({value:0, length:1, name: "active key id"});

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});

		//Anonymous function to parse and group the content of the page
		var list = (function(content,templateId)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			//Header image id
			imageId++;
			var header = (typeof content.header.value != "undefined")? content.header.value : content.header;
			list.image_ids.push({
				value : AQ.storage.getImageId( header, imageId, templateId ),
				length : 2,
				name: "image id - header"
			});

			//Header image hatching
			var hatchingInfoHeader = AQ.policy.getPolicyRules(content.header.policy);
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"image hatching - header"});

			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;
			
				var hatchingInfoButtons = 0;
				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
	                }else
	                {
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, templateId ),
	                        length : 2,
	                        name: "image id - button "+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
				    
				    hatchingInfoButtons = AQ.policy.getPolicyRules(content.buttons[key].policy);
				}

				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButtons, length:1, name:"image hatching - button "+key});
			}


			//Handle the case when there's no list items are passed
			if(typeof content.items == "undefined")
			{
				content.items = {};
			}

			//Go through the content list items and append text ids, image ids, and hatching info
			for(var i = 1; i <= 4; i++)
			{
				//Image id
				imageId++;
				var item_policy = (typeof content.items[i].policy != "undefined" )? content.items[i].policy : [];
				
				//Check whether the image is set with this key
				if( typeof content.items[i] == "undefined" || content.items[i] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - list item "+ i });
				}else
				{
					var item_image = (typeof content.items[i].image.value != "undefined" )? content.items[i].image.value : content.items[i].image;
					list.image_ids.push({
						value : AQ.storage.getImageId( item_image, imageId, templateId ),
						length : 2,
						name : "image id - list item "+ i
					});
					
					//Image hatching
					var image_policy = (typeof content.items[i].image.policy != "undefined")? content.items[i].image.policy : [];
					var hatchingInfoItem_Image = AQ.policy.getPolicyRules(image_policy.concat(item_policy));
					list.image_hatching.push({value:hatchingInfoItem_Image,length:1,name:"image hatching - list item "+i});

				}

				if( typeof content.items[i] == "undefined" || content.items[i] == null )
				{
				    list.text_ids.push({ value:0, length:2, name: "text id - list item "+ i });
				}else
				{
					var item_text = (typeof content.items[i].text.value != "undefined" )? content.items[i].text.value : content.items[i].text;
					//Text id
					list.text_ids.push({
						value : AQ.storage.getTextId( item_text),
						length : 2,
						name : "text id - list item "+ i
					});
	
					//Text hatching
					var text_policy = (typeof content.items[i].text.policy != "undefined")? content.items[i].text.policy : [];
					var hatchingInfoItem_Text = AQ.policy.getPolicyRules(text_policy.concat(item_policy));
					list.text_hatching.push({value:hatchingInfoItem_Text,length:1,name:"text hatching - list item "+i});
	
					//Save list actions
					if(typeof content.items[i].action != "undefined" || typeof content.items[i].value != "undefined")
					{
						AQ.storage.setAction( i, content.items[i].action, content.items[i].value );
					}
				}
				
			}//End for

			//Right image id
			imageId++;
			var main_image = (typeof content.main_image.value != "undefined")? content.main_image.value:content.main_image;
			list.image_ids.push({
				value : AQ.storage.getImageId( main_image, imageId, templateId ),
				length : 2,
				name : "image id - main right"
			});

			//Right image hatching
			var hatchingInfoImages = AQ.policy.getPolicyRules(content.main_image.policy);
			list.image_hatching.push({value:hatchingInfoImages, length:1, name:"image hatching - main Right"});

			return list;
		})( data.template_content ,templateId); //end of the anonymous function to group the content of the page


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},



	/**
	 * Template J
	 *
	 * The sample JSON:
	 *   {
	 *		"template_id" : ("j"/"J"),
	 *		"template_content" : {
	 *		 	"header" : (image url),
	 *		 	"buttons" : {
	 *		 		"1" : {},
	 *		 		"2" : {},
	 *		 		"3" : {},
	 *		 		"4" : {},
	 *		 		"5" : {},
	 *		 		"6" : {
	 * 					"image" : (image url),
	 *	 				"action" : (string),
	 * 					"value" : (string)
	 * 				}
	 *		 	},
	 *		 	"text_main" : (string),
	 *		 }
	 *	  }
	 *
	 * 		 _______________________________________________
	 * 		| img01               |                         |
	 * 		|-----------------------------------------------|
	 * 		|  __________________________________________   |
	 * 		| | text01                                   |  |
	 * 		| |                                          |  |
	 * 		| |                                          |  |
	 * 		| |                                          |  |
	 * 		| |                                          |  |
	 * 		| |__________________________________________|  |
	 * 		|_______________________________________________|
	 * 		| img02 | img03 | img04 | img05 | img06 | img07 |
	 * 		|_______|_______|_______|_______|_______|_______|
	 */

	j : function(data, result)
	{
		//Number of list items - fixed - 1 byte
		result.push({value:0, length:1, name:"number of list items"});

		//Active key id - fixed - 1 byte
		result.push({value:1, length:1, name:"active key id"});

		//Reserve - 2 bytes
		result.push({value:0, length:1, name:"reserve"});
		result.push({value:0, length:1, name:"reserve"});


		//Anonymous function to parse and group the content of the page
		var list = (function(content)
		{
			var list = { image_ids:[], text_ids:[], text_hatching:[], image_hatching:[] };
			var imageId = 0;

			//Header image id
			imageId++;
			var header = (typeof content.header.value != "undefined")? content.header.value : content.header;
			list.image_ids.push({
				value : AQ.storage.getImageId( header, imageId, 5 ),
				length : 2,
				name : "image id - header"
			});

			//Header image hatching
			var hatchingInfoHeader = AQ.policy.getPolicyRules(content.header.policy);
			list.image_hatching.push({value:hatchingInfoHeader, length:1, name:"image hatching - header"});


			//Handle the case when there are no buttons passed
			if(typeof content.buttons == "undefined")
			{
				content.buttons = {};
			}

			//Buttons image ids
			//Go through buttons 1 to 6 inclusive
			for( var key = 1; key <= 6; key++ )
			{
				imageId++;
				var hatchingInfoButtons = 0;
				//Check whether the button is set with this key
				if( typeof content.buttons[key] == "undefined" || content.buttons[key] == null )
				{
				    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
				}else
				{
				    if( typeof content.buttons[key].image == 'undefined' )
	                {
	                    list.image_ids.push({ value:0, length:2, name: "image id - button "+key });
	                }else
	                {
	                    list.image_ids.push({
	                        value : AQ.storage.getImageId( content.buttons[key].image, imageId, 3 ),
	                        length : 2,
	                        name: "image id - button "+key
	                    });

	                    //Save button actions
	                    if(typeof content.buttons[key].action != "undefined" || typeof content.buttons[key].value != "undefined")
	                    {
	                        key = parseInt(key);
	                        AQ.storage.setAction(0xF0 + key, content.buttons[key].action, content.buttons[key].value);
	                    }
	                }
				    hatchingInfoButtons = AQ.policy.getPolicyRules(content.buttons[key].policy);
				}

				//Button image hatching
				list.image_hatching.push({value:hatchingInfoButtons, length:1, name:"image hatching - button "+key});
			}

			//Main text id
			var text_main = (typeof content.text_main.value != "undefined")? content.text_main.value : content.text_main;
			list.text_ids.push({
				value : AQ.storage.getTextId( text_main ),
				length : 2,
				name : "text id - main text"
			});

			//Text hatching
			var hatchingInfoText = AQ.policy.getPolicyRules(content.text_main.policy);
			list.text_hatching.push({value:hatchingInfoText,length:1,name:"text hatching - main text"});


			return list;
		})( data.template_content ); //end of the anonymous function to group the content of the page


		//Text ids
		result = result.concat( list.text_ids );

		//Image ids
		result = result.concat( list.image_ids );

		//Text hatching info
		result = result.concat( list.text_hatching );

		//Image hatching info
		result = result.concat( list.image_hatching );

		//Action parameters
		result = this._setActions(data, result);

		return AQ.template.translate(result);
	},


	/**
	 * TODO - TEMP implementation for action parameters
	 */
	_setActions: function(data, result)
	{
	    //FIXME: This is temporary implementation for testing purposes only.
        //Implement attaching actions for placing a call properly.

        //Action ID - 2 bytes
        result.push({value:0, length:2, name: "action id"});

        //Action trigger key id - 2 byte
        result.push({value:0, length:2, name: "action trigger key id"});

        //Action parameter size - 1 byte
        result.push({value:0, length:1, name: "action parameter size"});

        //Reserve - 1 byte
        result.push({value:0, length:1, name: "reserve"});

        return result;
	}
}
