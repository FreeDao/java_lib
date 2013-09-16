/**
 *
 * @type {*}
 */

var HuComm = PandoraComm.extend({

    init:function () {
    },

    startComm:function() {
    },

    _byte2HexString : function(array){
        var hexStrings = "";
        $(array).each(function(index, item) {
            var hexString = parseInt(item).toString(16);
            if (hexString.length < 2) {
                hexString = "0" + hexString;
            }
            hexStrings = hexStrings + hexString.toUpperCase() + " ";
        });
        return hexStrings.trim();
    },

    sendFrame:function(frameData) {
        var requestData = this._byte2HexString(frameData);

        log("=======SENDING DATA TO HAP hex: "+ requestData);

        var data = AQ.util.hexToBase64(requestData);

        log("=======SENDING DATA TO HAP base64: "+ data);

        AQ.app.sendAppRequest({
            appName: "Pandora",
            contentType:"application/octet-stream",
            contentTransferEncoding:"base64",
            content: data
        });

        return;
    }
});
