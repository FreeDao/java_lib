var NativeBridge = {
  callbacksCount : 1,
  callbacks : {},

  // Automatically called by native layer when a result is available
  resultForCallback : function resultForCallback(callbackId, resultArray) {
    try {
    var callback = NativeBridge.callbacks[callbackId];
    if (!callback) return;

    callback.apply(null,resultArray);
    } catch(e) {alert(e)}
  },

  // Use this in javascript to request native objective-c code
  // functionName : string (I think the name is explicit :p)
  // args : array of arguments
  // callback : function with n-arguments that is going to be called when the native code returned
    call : function call(functionName, args) {

        log("NativeBridge: function name: "+ functionName);
        log("NativeBridge: args name: "+ args);

        var iframe = document.createElement("IFRAME");
        iframe.setAttribute("src", "update:" + functionName + ":" + encodeURIComponent(args));
        document.documentElement.appendChild(iframe);
        iframe.parentNode.removeChild(iframe);
        iframe = null;
    }
};