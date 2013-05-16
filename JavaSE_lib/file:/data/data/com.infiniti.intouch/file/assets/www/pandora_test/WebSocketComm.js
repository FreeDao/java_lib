/**
 *
 * @type {*}
 */
var ws = null;

var WebSocketComm = PandoraComm.extend({

	init:function (address) {
		this.address = address;
	},

	startComm:function() {
		if ("WebSocket" in window)
		{
			// Let us open a web socket
			ws = new WebSocket("ws://" + this.address + ":9003/echo");
			ws.onopen = function()
			{
				if (controller.pComm.callbackObj == null) {
					console.log("No callback object has been registered for onOpen!");
					return;
				}
				controller.pComm.callCallback(controller.pComm.callbackObj.onOpen);
			};
			ws.onmessage = function (evt)
			{
				//console.log("Data: " + evt.data);
				var dataString = evt.data.toString();
				// Remove all whitespace
				dataString = dataString.replace(/[\t \n]+/g,"");
				//console.log("Data String: " + dataString);
				var message = jQuery.base64.decode(dataString);
				var frame = toa(message);
				//console.log("Rec Data: " + hex(bytes));
				// bytes can contain multiple frames, but now parsing them out in controller
				controller.pComm.callCallbackWithFrame(controller.pComm.callbackObj.onReceiveFrame, frame);
			};
			ws.onclose = function()
			{
				// websocket is closed.
				console.log("Connection is closed...");
				controller.pComm.callCallback(controller.pComm.callbackObj.onClose);
			};
		}
		else
		{
			// The browser doesn't support WebSocket
			//alert("WebSocket NOT supported by your Browser!");
			return false;
		}
	},

	sendFrame:function(frameData) {
		var frameDataBase64 = jQuery.base64.encode(frameData);
        //console.log("Send Frame: " + hex(frameData));
		//console.log("Send Base64: " + frameDataBase64);
        ws.send(frameDataBase64);
	}
});
