/**
 * AQ.constant wraps all constant variables
 */


AQ.constants =
{
	// environment: "production",
	environment: "development",


	//TODO: these should be gotten from the HAP
	apps: {
		"home" : 1,
		"facebook" : 2,
		"template_tester" : 3,
		"audio_tester" : 4,
		"nbc_news" : 5,
		"i_heart_radio":6,
		"test_suite" : 7,
		"pandora_test" : 8,
		"pandora" : 9,
	},

	/**
	 * Hard key id map
	 */
	hardKeys :
	{
		1 : "apps",
		2 : "seek_next", // seek up/right
		3 : "seek_previous", // seek down/left
		4 : "back"
	},

	url :
	{
		"common" : "file:///android_asset/www/aq/",
		"common_hmi" : "aq:///",

		"apps" : "file:///android_asset/www/",
		"apps_hmi" : "file:///",
	},


	defaultButtons :
	{
		'5_up' : "http://63.149.221.165/pan/images/generic/5-pageup.png", //TODO: Load local images
        '6_down' : "http://63.149.221.165/pan/images/generic/6-pagedown.png" //TODO: Load local images
	},

	// TODO: Create a map of id - string representation
	languages :
	{
	 	0 : "en-us", // 0x00 - North American English
	 	1 : "fr-ca", // 0x01 - Canadian French
	 	2 : "sp-us", // 0x02 - North American Spanish
	 	3 : "en-uk", // 0x03 - UK English
	 	4 : "fr-fr", // 0x04 - French
	 	5 : "it-it", // 0x05 - Italian
	 	6 : "de-de", // 0x06 - German
	 	A : "ar-ar", // 0x0A - Standard Arabic
	 	B : "zh-cn", // 0x0B - Mandarin
	 	C : "ru-ru", // 0x0C - Russian
	 	D : "ko"  // 0x0D - Korean
	},


	/**
	 * Policy information
	 */
	hatchingInfo:
	{
		"none" : 0,
		"hide" : 1,
		"disable" : 2
	},


	/**
	 * Image sizes for each template -> image id
	 *
	 * Sample accessing the data:
	 * AQ.constants.imageSizes[templateId][imageId].w => width
	 */
	imageSizes:
	{
		// Template A_1, A_2, A_3
		1 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 },  //footer 6
			8 : { w:38, h:32 },  //image 1-1
			9 : { w:54, h:24 },  //image 1-2
			10 : { w:54, h:24 }, //image 1-3
			11 : { w:38, h:32 }, //image 2-1
			12 : { w:54, h:24 }, //image 2-2
			13 : { w:54, h:24 }, //image 2-3
			14 : { w:38, h:32 }, //image 3-1
			15 : { w:54, h:24 }, //image 3-2
			16 : { w:54, h:24 }, //image 3-3
			17 : { w:38, h:32 }, //image 4-1
			18 : { w:54, h:24 }, //image 4-2
			19 : { w:54, h:24 }, //image 4-3
			20 : { w:38, h:32 }, //image 5-1
			21 : { w:54, h:24 }, //image 5-2
			22 : { w:54, h:24 }, //image 5-3
			23 : { w:38, h:32 }, //image 6-1
			24 : { w:54, h:24 }, //image 6-2
			25 : { w:54, h:24 }, //image 6-3
			26 : { w:38, h:32 }, //image 7-1
			27 : { w:54, h:24 }, //image 7-2
			28 : { w:54, h:24 }, //image 7-3
			29 : { w:38, h:32 }, //image 8-1
			30 : { w:54, h:24 }, //image 8-2
			31 : { w:54, h:24 }, //image 8-3
			32 : { w:38, h:32 }, //image 9-1
			33 : { w:54, h:24 }, //image 9-2
			34 : { w:54, h:24 }, //image 9-3
			35 : { w:38, h:32 }, //image 10-1
			36 : { w:54, h:24 }, //image 10-2
			37 : { w:54, h:24 }, //image 10-3
			38 : { w:38, h:32 }, //image 11-1
			39 : { w:54, h:24 }, //image 11-2
			40 : { w:54, h:24 }, //image 11-3
			41 : { w:38, h:32 }, //image 12-1
			42 : { w:54, h:24 }, //image 12-2
			43 : { w:54, h:24 }, //image 12-3
			44 : { w:38, h:32 }, //image 13-1
			45 : { w:54, h:24 }, //image 13-2
			46 : { w:54, h:24 }, //image 13-3
			47 : { w:38, h:32 }, //image 14-1
			48 : { w:54, h:24 }, //image 14-2
			49 : { w:54, h:24 }, //image 14-3
			50 : { w:38, h:32 }, //image 15-1
			51 : { w:54, h:24 }, //image 15-2
			52 : { w:54, h:24 }, //image 15-3
			53 : { w:38, h:32 }, //image 16-1
			54 : { w:54, h:24 }, //image 16-2
			55 : { w:54, h:24 }, //image 16-3
			56 : { w:38, h:32 }, //image 17-1
			57 : { w:54, h:24 }, //image 17-2
			58 : { w:54, h:24 }, //image 17-3
			59 : { w:38, h:32 }, //image 18-1
			60 : { w:54, h:24 }, //image 18-2
			61 : { w:54, h:24 }, //image 18-3
			62 : { w:38, h:32 }, //image 19-1
			63 : { w:54, h:24 }, //image 19-2
			64 : { w:54, h:24 }, //image 19-3
			65 : { w:38, h:32 }, //image 20-1
			66 : { w:54, h:24 }, //image 20-2
			67 : { w:54, h:24 }  //image 20-3
		},
		// END OF Template A_1, A_2, A_3

		// Template B
		2 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 },  //footer 6
			8 : { w:153, h:80 }  //left side main image
		},
		// END OF Template B

		// Template C
		3 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 }   //footer 6
		},
		// END OF Template C

		// Template D
		4 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 },  //footer 6
			8 : { w:174, h:152 },//left side main image
			9 : { w:38, h:32 },  //right list image 1
			10 : { w:38, h:32 }, //right list image 2
			11 : { w:38, h:32 }, //right list image 3
			12 : { w:38, h:32 }, //right list image 4
			13 : { w:38, h:32 }, //right list image 5
			14 : { w:38, h:32 }, //right list image 6
			15 : { w:38, h:32 }, //right list image 7
			16 : { w:38, h:32 }, //right list image 8
			17 : { w:38, h:32 }, //right list image 9
			18 : { w:38, h:32 }, //right list image 10
			19 : { w:38, h:32 }, //right list image 11
			20 : { w:38, h:32 }, //right list image 12
			21 : { w:38, h:32 }, //right list image 13
			22 : { w:38, h:32 }, //right list image 14
			23 : { w:38, h:32 }, //right list image 15
			24 : { w:38, h:32 }, //right list image 16
			25 : { w:38, h:32 }, //right list image 17
			26 : { w:38, h:32 }, //right list image 18
			27 : { w:38, h:32 }, //right list image 19
			28 : { w:38, h:32 }  //right list image 20
		},
		// END OF Template D

		// Template E
		5 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 },  //footer 6
			8 : { w:456, h:142 } //main image
		},
		// END OF Template E

		// Template F
		6 : {
			1 : { w:247, h:40 } //header
		},
		// END OF Template F

		// Template G_1, G_2, G_3
		7 : {
			1 : { w:247, h:40 }, //header
			2 : { w:38, h:32 },  //image 1-1
			3 : { w:54, h:24 },  //image 1-2
			4 : { w:54, h:24 },  //image 1-3
			5 : { w:38, h:32 },  //image 2-1
			6 : { w:54, h:24 },  //image 2-2
			7 : { w:54, h:24 },  //image 2-3
			8 : { w:38, h:32 },  //image 3-1
			9 : { w:54, h:24 },  //image 3-2
			10 : { w:54, h:24 }, //image 3-3
			11 : { w:38, h:32 }, //image 4-1
			12 : { w:54, h:24 }, //image 4-2
			13 : { w:54, h:24 }, //image 4-3
			14 : { w:38, h:32 }, //image 5-1
			15 : { w:54, h:24 }, //image 5-2
			16 : { w:54, h:24 }, //image 5-3
			17 : { w:38, h:32 }, //image 6-1
			18 : { w:54, h:24 }, //image 6-2
			19 : { w:54, h:24 }, //image 6-3
			20 : { w:38, h:32 }, //image 7-1
			21 : { w:54, h:24 }, //image 7-2
			22 : { w:54, h:24 }, //image 7-3
			23 : { w:38, h:32 }, //image 8-1
			24 : { w:54, h:24 }, //image 8-2
			25 : { w:54, h:24 }, //image 8-3
			26 : { w:38, h:32 }, //image 9-1
			27 : { w:54, h:24 }, //image 9-2
			28 : { w:54, h:24 }, //image 9-3
			29 : { w:38, h:32 }, //image 10-1
			30 : { w:54, h:24 }, //image 10-2
			31 : { w:54, h:24 }, //image 10-3
			32 : { w:38, h:32 }, //image 11-1
			33 : { w:54, h:24 }, //image 11-2
			34 : { w:54, h:24 }, //image 11-3
			35 : { w:38, h:32 }, //image 12-1
			36 : { w:54, h:24 }, //image 12-2
			37 : { w:54, h:24 }, //image 12-3
			38 : { w:38, h:32 }, //image 13-1
			39 : { w:54, h:24 }, //image 13-2
			40 : { w:54, h:24 }, //image 13-3
			41 : { w:38, h:32 }, //image 14-1
			42 : { w:54, h:24 }, //image 14-2
			43 : { w:54, h:24 }, //image 14-3
			44 : { w:38, h:32 }, //image 15-1
			45 : { w:54, h:24 }, //image 15-2
			46 : { w:54, h:24 }, //image 15-3
			47 : { w:38, h:32 }, //image 16-1
			48 : { w:54, h:24 }, //image 16-2
			49 : { w:54, h:24 }, //image 16-3
			50 : { w:38, h:32 }, //image 17-1
			51 : { w:54, h:24 }, //image 17-2
			52 : { w:54, h:24 }, //image 17-3
			53 : { w:38, h:32 }, //image 18-1
			54 : { w:54, h:24 }, //image 18-2
			55 : { w:54, h:24 }, //image 18-3
			56 : { w:38, h:32 }, //image 19-1
			57 : { w:54, h:24 }, //image 19-2
			58 : { w:54, h:24 }, //image 19-3
			59 : { w:38, h:32 }, //image 20-1
			60 : { w:54, h:24 }, //image 20-2
			61 : { w:54, h:24 }  //image 20-3
		},
		// END OF Template G

		// Template H
		8 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 },  //footer 6
			8 : { w:106, h:30 },  //image  8
			9 : { w:106, h:30 },  //image  9
			10 : { w:106, h:30 },  //image  10
			11 : { w:106, h:30 },  //image  11
			12 : { w:80, h:70 } //main image 12
		},
		// END OF Template H

		// Template I
		9 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 }   //footer 6
		},
		// END OF Template I

		// Template J
		10 : {
			1 : { w:247, h:40 }, //header
			2 : { w:76, h:29 },  //footer 1
			3 : { w:76, h:29 },  //footer 2
			4 : { w:76, h:29 },  //footer 3
			5 : { w:76, h:29 },  //footer 4
			6 : { w:76, h:29 },  //footer 5
			7 : { w:76, h:29 }   //footer 6
		}
		// END OF Template J
	}
}
