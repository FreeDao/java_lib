package com.airbiquity.hap;

/**
 * Callback interface implemented by handset applications.  These methods are
 * invoked by the Handset Application Proxy (HAP).  The callback interface is
 * supplied to HAP in the aqHapInit() method of the IHandsetApplicationProxy
 * interface.
 */
interface IHapCallback {

    /**
     * Notifies handset application when the head unit connection state changes.
     *
     * @param connectionState 0: head unit and handset got connected.
     *                        1: head unit and handset got disconnected.
     */
    void onHapConnectionStateChange(in int connectionState);

    /**
     * Passes a message from the head unit to a handset application.
     *
     * @param sequenceNumber Non-zero sequence number of the request.  This sequence
     * number must be echoed back in the response sent to HAP by the application in order
     * to match the request and response.
     *
     * @param payload Payload sent from the head unit to the handset application.
     *
     * @param contentType HTTP 1.1 Content Type of the payload. 
     */
    void onHapCommandReceived(in int sequenceNumber, in byte[] payload, in String contentType);

}
